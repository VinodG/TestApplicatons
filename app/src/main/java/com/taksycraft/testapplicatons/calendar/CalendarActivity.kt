package com.taksycraft.testapplicatons.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.PagerSnapHelper
import com.taksycraft.testapplicatons.R
import com.taksycraft.testapplicatons.databinding.ActivityCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {
    private lateinit var adapterDates: CalendarAdapter
    private lateinit var listMonths: MutableList<CalendarDO>
    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_calendar)
        listMonths = getMonthsData()
        adapterDates = CalendarAdapter(listMonths[0].cal.getMonthDatesData())
        binding.rv.adapter = adapterDates
        binding.rvMonth.apply {
            adapter = MonthAdapter(listMonths)
            onSelectedPage(
                    onPage = {updateMonth(it) },
                    onLastPage = {  })
            PagerSnapHelper().attachToRecyclerView(this)
        }
        adapterDates.setDateRangeListener { calendar, calendar2 ->{

        }  }

    }

    private fun updateMonth(position: Int) {
        adapterDates.refresh(listMonths[position].cal.getMonthDatesData())
    }

    inline  fun  Calendar.getCurrentMonthFirstDate() = this.apply {
        set(Calendar.DATE,1)
    }
    inline fun Calendar.getMonthDatesData():MutableList<CalendarDO> {
        var list : MutableList<CalendarDO> = mutableListOf()
        var cal = this.getCurrentMonthFirstDate()
        list.addAll((1 until cal.get(Calendar.DAY_OF_WEEK)).map { CalendarDO("", cal.clone() as Calendar) }.toMutableList())
        list.addAll((1..cal.getActualMaximum(Calendar.DATE)).map { CalendarDO(it.toString(),(cal.clone() as Calendar).apply {
            set(Calendar.DATE,it) }) }.toMutableList())
        return  list
    }

    private fun getMonthsData():MutableList<CalendarDO>{
        var list : MutableList<CalendarDO> = mutableListOf()
        (-12..12).forEach{
            var c = Calendar.getInstance()
            c.add(Calendar.MONTH, it)
            println(c.time)
            list.add( CalendarDO(
                    SimpleDateFormat("MMMM YYYY").format(c.getTime()), c
            ))
        }
        return list
    }

    companion object {
        private const val TAG = "CalendarActivity"
    }
}