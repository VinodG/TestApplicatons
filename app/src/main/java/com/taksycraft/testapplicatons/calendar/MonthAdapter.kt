package com.taksycraft.testapplicatons.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.taksycraft.testapplicatons.R
import com.taksycraft.testapplicatons.databinding.ItemCalendarMonthBinding

class MonthAdapter(var list: MutableList<CalendarDO> = mutableListOf() ): RecyclerView.Adapter<MonthAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ItemCalendarMonthBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.tv.text = list[position].name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_calendar_month, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = list.size
}