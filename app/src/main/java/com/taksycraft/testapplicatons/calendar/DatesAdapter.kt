package com.taksycraft.testapplicatons.calendar

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taksycraft.testapplicatons.R
import com.taksycraft.testapplicatons.databinding.ItemCalendarDateBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(var list: MutableList<CalendarDO> = mutableListOf() ): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    private var isSingleDateSelection: Boolean = true
    private var singleDateSelection: ((Calendar) -> Unit)? =null
    private var dateRangeListener: ((Calendar,Calendar) -> Unit)? =null
    private var selectedDate: Calendar = Calendar.getInstance()
    private var firstDate: Calendar? = null
    private var secondDate: Calendar? = null

    inner class ViewHolder(var binding: ItemCalendarDateBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            var obj = list[position]
            binding.root.setBackgroundColor(Color.parseColor("#FFFFFF"))
            if(obj.name.isEmpty()){
                binding.tv.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            }else if(isSingleDateSelection){
                setUi(binding.tv, selectedDate, obj)
            } else {
                if(firstDate ==null && secondDate ==null){
                    binding.tv.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
                }else{
                    if(firstDate!=null && secondDate!=null){
                        if((firstDate!!.timeInMillis<=obj.cal.timeInMillis).and(obj.cal.timeInMillis<= secondDate!!.timeInMillis)){
                            binding.root.setBackgroundColor(Color.parseColor("#FFFF00"))
                        }
                        if((firstDate!!.timeInMillis>obj.cal.timeInMillis).or(obj.cal.timeInMillis> secondDate!!.timeInMillis)){
                            binding.tv.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        }
                    }
                    firstDate?.let {
                        if(it.toDate() == obj.cal.toDate()){
                            binding.tv.setBackgroundResource(R.drawable.circle_drawable)
                        }
                    }
                    secondDate?.let {
                        if(it.toDate() == obj.cal.toDate()){
                            binding.tv.setBackgroundResource(R.drawable.circle_drawable)
                        }
                    }
                }
            }

            binding.tv.text = obj.name
            binding.root.setOnClickListener {
                if(!obj.name.isEmpty()){
                    if(isSingleDateSelection){
                        selectedDate = obj.cal
                        singleDateSelection?.invoke(selectedDate)
                    }else{
                        if(firstDate==null)
                            firstDate = obj.cal
                        else if(secondDate==null){
                            firstDate?.let {
                                if(obj.cal.timeInMillis <= it.timeInMillis){
                                    secondDate = null
                                    firstDate = obj.cal
                                }else{
                                    secondDate = obj.cal
                                    dateRangeListener?.invoke(it, secondDate!!)
                                }

                            }
                        }else{
                            secondDate = null
                            firstDate = obj.cal
                        }
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun setUi(tv: TextView, selectedDate: Calendar, obj: CalendarDO) {
        if(selectedDate.toDate() == obj.cal.toDate()){
            tv.setBackgroundResource(R.drawable.circle_drawable)
        }else
            tv.setBackgroundColor(Color.parseColor("#FFFFFFFF"))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_calendar_date, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = list.size
    fun refresh( list: MutableList<CalendarDO> ){
        this.list = list
        notifyDataSetChanged()
    }

    fun setDateSelectionListener(singleDateSelection: (Calendar) -> Unit) {
        isSingleDateSelection = true
        this.singleDateSelection = singleDateSelection
    }
    fun setDateRangeListener(dateRangeListener: (Calendar,Calendar) -> Unit) {
        isSingleDateSelection = false
        this.dateRangeListener = dateRangeListener
    }
}

private fun Calendar.toDate(): String {
    return SimpleDateFormat("dd-MM-yyyy").format(this.time)
}

data class CalendarDO(var name:String="", var cal: Calendar =  Calendar.getInstance())
inline fun  RecyclerView.onSelectedPage(crossinline onPage:(Int) -> Unit, crossinline onLastPage:(Int)->Unit  ){
    this.  addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = getLayoutManager() as LinearLayoutManager
            val pos = layoutManager.findLastCompletelyVisibleItemPosition()
            val numItems: Int = getAdapter()?.getItemCount() ?: 0
            Log.e("page", "onScrolled: ${pos}  ${numItems}"  )
            if (pos >= numItems - 1) {
                onLastPage.invoke(pos)
            } else if (pos >= 0) {
                onPage.invoke(pos)
            }
        }
    })
}