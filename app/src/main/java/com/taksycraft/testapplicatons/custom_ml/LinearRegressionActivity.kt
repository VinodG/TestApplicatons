package com.taksycraft.testapplicatons.custom_ml

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.taksycraft.testapplicatons.R
import com.taksycraft.testapplicatons.databinding.ActivityLinearRegressionBinding
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class LinearRegressionActivity : AppCompatActivity() {
    private lateinit var interpreter: Interpreter
    private lateinit var binding: ActivityLinearRegressionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_linear_regression)
        try {
            loadModelFile()?.let {
                interpreter = Interpreter(it,1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.btnPredict.setOnClickListener {
            loadModelFile()
            if(!binding.etPredict.text.toString().isEmpty())
                binding.tvPredict.text = doInference(binding.etPredict.text.toString().toFloat()).toString()
            else
                Toast.makeText(this@LinearRegressionActivity, "Please enter number to predict", Toast.LENGTH_SHORT).show()
        }

    }

    private fun doInference(predict_x: Float): Float {
        var floatArray = floatArrayOf(1.0f)
        floatArray[0] = predict_x
        var outarray = Array(1){floatArray}
//        var floatArray = StaticType.get1Dim()
//        floatArray[0] = predict_x
//        var outarray = StaticType.get2Dim()
        interpreter.run(floatArray, outarray)
        println(outarray[0][0])
        return outarray[0][0]

    }

    private fun loadModelFile():  MappedByteBuffer? {
        var assetFileDescriptor = assets.openFd("linear.tflite")
        var fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        var fileChannel = fileInputStream.channel
        var startOffSet = assetFileDescriptor.startOffset
        var length = assetFileDescriptor.length
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffSet,length)
    }
}