/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.classification

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import org.tensorflow.lite.examples.classification.ui.RecognitionAdapter
import org.tensorflow.lite.examples.classification.util.YuvToRgbConverter
import org.tensorflow.lite.examples.classification.viewmodel.Recognition
import org.tensorflow.lite.examples.classification.viewmodel.RecognitionListViewModel
import org.tensorflow.lite.support.common.FileUtil
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.Executors

private const val MAX_RESULT_DISPLAY = 3
private const val TAG = "TFL Classify"
private const val REQUEST_CODE_PERMISSIONS = 999
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
private const val MODEL_PATH = "FlowerModel.tflite"
private const val LABELS_PATH = "labels.txt"
private const val IMAGE_SIZE = 224

typealias RecognitionListener = (recognition: List<Recognition>) -> Unit

class MainActivity : AppCompatActivity() {

    private lateinit var preview: Preview
    private lateinit var imageAnalyzer: ImageAnalysis
    private lateinit var camera: Camera
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    private val resultRecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recognitionResults)
    }
    private val viewFinder by lazy {
        findViewById<PreviewView>(R.id.viewFinder)
    }

    private val recogViewModel: RecognitionListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        val viewAdapter = RecognitionAdapter(this)
        resultRecyclerView.adapter = viewAdapter
        resultRecyclerView.itemAnimator = null

        recogViewModel.recognitionList.observe(this,
            Observer {
                viewAdapter.submitList(it)
            }
        )
    }

    private fun allPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, getString(R.string.permission_deny_text), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder().build()

            imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(Size(IMAGE_SIZE, IMAGE_SIZE))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysisUseCase: ImageAnalysis ->
                    analysisUseCase.setAnalyzer(cameraExecutor, ImageAnalyzer(this) { items ->
                        recogViewModel.updateData(items)
                    })
                }

            val cameraSelector = if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA))
                CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
                preview.setSurfaceProvider(viewFinder.surfaceProvider)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private class ImageAnalyzer(ctx: Context, private val listener: RecognitionListener) :
        ImageAnalysis.Analyzer {

        private val interpreter: org.tensorflow.lite.Interpreter
        private val labels: List<String>

        init {
            try {
                val modelFile = FileUtil.loadMappedFile(ctx, MODEL_PATH)
                interpreter = org.tensorflow.lite.Interpreter(modelFile)
                labels = loadLabels(ctx)
                Log.d(TAG, "模型加载成功！类别数量: ${labels.size}")
            } catch (e: Exception) {
                Log.e(TAG, "模型加载失败", e)
                throw RuntimeException("模型加载失败: ${e.message}")
            }
        }

        private fun loadLabels(context: Context): List<String> {
            val labelsList = mutableListOf<String>()
            val inputStream = context.assets.open(LABELS_PATH)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                line?.let { labelsList.add(it) }
            }
            reader.close()
            return labelsList
        }

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            val items = mutableListOf<Recognition>()

            try {
                val bitmap = toBitmap(imageProxy)
                bitmap?.let {
                    val scaledBitmap = Bitmap.createScaledBitmap(it, IMAGE_SIZE, IMAGE_SIZE, true)
                    val inputBuffer = bitmapToByteBuffer(scaledBitmap)
                    val outputArray = Array(1) { FloatArray(labels.size) }
                    interpreter.run(inputBuffer, outputArray)
                    val results = outputArray[0]

                    val recognitions = mutableListOf<Pair<String, Float>>()
                    for (i in results.indices) {
                        recognitions.add(Pair(labels[i], results[i]))
                    }
                    recognitions.sortByDescending { it.second }

                    for (i in 0 until minOf(MAX_RESULT_DISPLAY, recognitions.size)) {
                        val (label, score) = recognitions[i]
                        items.add(Recognition(label, score))
                    }
                }

                listener(items.toList())
            } catch (e: Exception) {
                Log.e(TAG, "分析图像时出错: ${e.message}")
                listener(emptyList())
            } finally {
                imageProxy.close()
            }
        }

        private fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
            val byteBuffer = ByteBuffer.allocateDirect(4 * IMAGE_SIZE * IMAGE_SIZE * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            val pixels = IntArray(IMAGE_SIZE * IMAGE_SIZE)
            bitmap.getPixels(pixels, 0, IMAGE_SIZE, 0, 0, IMAGE_SIZE, IMAGE_SIZE)

            var pixel = 0
            for (i in 0 until IMAGE_SIZE) {
                for (j in 0 until IMAGE_SIZE) {
                    val pixelVal = pixels[pixel++]
                    val r = (pixelVal shr 16 and 0xFF) / 255.0f
                    val g = (pixelVal shr 8 and 0xFF) / 255.0f
                    val b = (pixelVal and 0xFF) / 255.0f
                    byteBuffer.putFloat(r)
                    byteBuffer.putFloat(g)
                    byteBuffer.putFloat(b)
                }
            }
            byteBuffer.rewind()
            return byteBuffer
        }

        private val yuvToRgbConverter = YuvToRgbConverter(ctx)
        private lateinit var bitmapBuffer: Bitmap
        private lateinit var rotationMatrix: Matrix

        @SuppressLint("UnsafeExperimentalUsageError")
        private fun toBitmap(imageProxy: ImageProxy): Bitmap? {
            val image = imageProxy.image ?: return null

            if (!::bitmapBuffer.isInitialized) {
                rotationMatrix = Matrix()
                rotationMatrix.postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                bitmapBuffer = Bitmap.createBitmap(
                    imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888
                )
            }

            yuvToRgbConverter.yuvToRgb(image, bitmapBuffer)

            return Bitmap.createBitmap(
                bitmapBuffer,
                0,
                0,
                bitmapBuffer.width,
                bitmapBuffer.height,
                rotationMatrix,
                false
            )
        }
    }
}