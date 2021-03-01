package com.taksycraft.testapplicatons.files

import android.os.Bundle
import android.webkit.WebChromeClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.taksycraft.testapplicatons.R
import com.taksycraft.testapplicatons.databinding.ActivityPdfBinding

class PdfActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,  R.layout.activity_pdf)
        loadPdfFile()
    }

    private fun loadPdfFile() {
        binding.wv.apply {
            settings.javaScriptEnabled = true
            webChromeClient = WebChromeClient()
            settings.supportZoom()
            //        var  filename ="http://www3.nd.edu/~cpoellab/teaching/cse40816/android_tutorial.pdf";
            var  filename ="https://mindorks.s3.ap-south-1.amazonaws.com/courses/MindOrks_Android_Online_Professional_Course-Syllabus.pdf";
            loadUrl("http://docs.google.com/gview?embedded=true&url=" + filename);
        }

    }
}