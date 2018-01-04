package com.sandeepshabd.dailysync.activities

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
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
        entryForChart.add(PieEntry(4f, "Mon"))
        entryForChart.add(PieEntry(8f, "Tue"))
        entryForChart.add(PieEntry(6f, "Wed"))
        entryForChart.add(PieEntry(9f, "Thu"))
        entryForChart.add(PieEntry(15f, "Fri"))

        var dataSet = PieDataSet(entryForChart,getResources().getString(R.string.numbers))
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toMutableList()

        var pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.data.setValueTextSize(15f)
        pieChart.data.setValueTextColor(Color.DKGRAY)


        var description =  Description()
        description.text = "Pie chart for Daily sync"
        pieChart.setDescription(description)
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setHoleRadius(30f);




    }
}
