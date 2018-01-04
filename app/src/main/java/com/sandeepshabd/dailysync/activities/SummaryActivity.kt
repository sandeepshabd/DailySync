package com.sandeepshabd.dailysync.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.sandeepshabd.dailysync.R
import org.jetbrains.anko.find

class SummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        var pieChart = find<PieChart>(R.id.pieChart)

       var entryForChart = mutableListOf<PieEntry>()
        entryForChart.add(PieEntry(4f, 0f))
        entryForChart.add(PieEntry(8f, 0f))
        entryForChart.add(PieEntry(6f, 0f))
        entryForChart.add(PieEntry(9f, 0f))
        entryForChart.add(PieEntry(15f, 0f))

        var dataSet = PieDataSet(entryForChart,getResources().getString(R.string.numbers))
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toMutableList()

        var labelList = ArrayList<String>()
        labelList.add("Mon")
        labelList.add("Tue")
        labelList.add("Wed")
        labelList.add("Thu")
        labelList.add("Fri")

        var pieData = PieData(dataSet)
        pieChart.data = pieData




    }
}
