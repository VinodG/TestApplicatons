package com.taksycraft.testapplicatons.generics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.taksycraft.testapplicatons.R
import com.taksycraft.testapplicatons.databinding.ActivityGenericsTestingBinding

class GenericsTestingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenericsTestingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_generics_testing)
        setAdapter()
    }

    private fun setAdapter() {
        binding.rv.adapter = GenericAdapter(mutableListOf<Any>().apply {
            "vinod"
        })
    }

    class GenericAdapter (var list: MutableList<*>):RecyclerView.Adapter<GenericAdapter.ViewHolder>(){

        interface  Generic<T>{
            fun onBind(t:T)
        }

        class ViewHolder(var binding: ViewDataBinding):RecyclerView.ViewHolder(binding.root), Generic<String> {
            override fun onBind(t: String) {

             }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =  ViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item, parent, false)
        )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            (holder as Generic<*>).onBind(list[position])
        }
        override fun getItemCount() = list.size
    }
}