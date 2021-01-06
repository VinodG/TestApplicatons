package com.taksycraft.testapplicatons.flow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.taksycraft.testapplicatons.R
import com.taksycraft.testapplicatons.databinding.ActivityFlowTestingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class FlowTestingActivity : AppCompatActivity() {

    private lateinit var flw: Flow<Int>
    private lateinit var binding: ActivityFlowTestingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_flow_testing)
        setupFlow()
        binding.btnClick.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                flw.collect {
                    println("emitted values - $it")
                }
            }
        }
    }
    fun setupFlow(){
        flw = flow{
            (0..19).forEach {
                delay(1000)
                emit(it)
            }
        }
    }
}