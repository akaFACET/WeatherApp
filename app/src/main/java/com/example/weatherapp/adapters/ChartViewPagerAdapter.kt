package com.example.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.Utils.Util
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF


class ChartViewPagerAdapter(
    val context: Context,
    var data: List<WeatherPerHour>,
    val chartItemClickListener: OnChartItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), OnChartValueSelectedListener {

    internal val BAR_CHART = 1
    internal val LINE_CHART = 2

    val textColor = ContextCompat.getColor(context, R.color.secondaryTextColor)

    private inner class ViewHolder1 constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var barChart: BarChart
        var primaryDarkColor = ContextCompat.getColor(context, R.color.primaryDarkColor)
        var primaryColor = ContextCompat.getColor(context, R.color.primaryColor)

        init {
            barChart =
                itemView.findViewById(R.id.chart_bch)
            barChart.setOnChartValueSelectedListener(this@ChartViewPagerAdapter)
            barChart.apply {
                setViewPortOffsets(40f, 50f, 40f, 50f)
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(false)
                setPinchZoom(false)
                setDrawGridBackground(false)
                legend.isEnabled = false
                axisLeft.setDrawLabels(false)
                axisRight.setDrawLabels(false)
                axisLeft.setDrawGridLines(false)
                axisRight.setDrawGridLines(false)
                axisRight.isEnabled = false
                axisLeft.isEnabled = false
                axisLeft.axisMinimum = 0f
                axisRight.axisMinimum = 0f
                xAxis.spaceMax = 0.40f
                xAxis.spaceMin = 0.40f
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f
                xAxis.textColor = textColor
            }

        }

        fun bind(position: Int) {
            val values = mutableListOf<BarEntry>()
            val time: List<String> = data.map {
                Util.getTimeFromUnixTime(it.dt)
            }

            when (position) {
                1 -> {
                    val precipitation: List<Double> = data.map {
                        it.precipitation
                    }

                    for (i in 0..precipitation.size - 1) {
                        values.add(BarEntry(i.toFloat(), precipitation.get(i).toFloat()))
                    }

                    val dataSet = BarDataSet(values, "")

                    dataSet.apply {

                        highLightColor = primaryDarkColor
                        color = primaryColor

                        valueTextColor = textColor
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return value.toString() + " " + context.getString(R.string.millimeters)
                            }
                        }
                    }

                    val data = BarData(dataSet)
                    data.apply {
                        setValueTextSize(9f)
                        setDrawValues(true)
                    }
                    barChart.apply {
                        barChart.data = data
                        xAxis.labelCount = values.size - 1

                        xAxis.valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                if (value.toInt() < time.size) {
                                    return time.get(value.toInt())
                                } else
                                    return "0"
                            }
                        }
                        animateXY(500, 1000)
                        invalidate()
                    }
                }
                3 -> {
                    val cloudsAll: List<Int> = data.map {
                        it.cloudsAll
                    }

                    for (i in 0..cloudsAll.size - 1) {
                        values.add(BarEntry(i.toFloat(), cloudsAll.get(i).toFloat()))
                    }

                    val dataSet = BarDataSet(values, "")

                    dataSet.apply {
                        highLightColor = primaryDarkColor
                        color = primaryColor
                        valueTextColor = textColor
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return value.toInt()
                                    .toString() + context.getString(R.string.percentage)
                            }
                        }
                    }

                    val data = BarData(dataSet)

                    data.apply {
                        setValueTextSize(9f)
                        setDrawValues(true)
                    }

                    barChart.apply {
                        barChart.data = data
                        xAxis.labelCount = values.size - 1
                        xAxis.valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                if (value.toInt() < time.size) {

                                    return time.get(value.toInt())
                                } else
                                    return "0"
                            }
                        }
                        animateXY(500, 1000)
                        invalidate()
                    }

                }
            }

        }
    }

    private inner class ViewHolder2 constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var lineChart: LineChart


        var mv = MyMarkerView(context, R.layout.marker_view)

        init {

            lineChart = itemView.findViewById(R.id.chart_lch)

            lineChart.setOnChartValueSelectedListener(this@ChartViewPagerAdapter)

            lineChart.marker = mv

            lineChart.apply {
                setViewPortOffsets(30f, 0f, 30f, 50f)
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(false)
                setPinchZoom(false)
                setDrawGridBackground(false)
                legend.isEnabled = false
                axisLeft.setDrawLabels(false)
                axisRight.setDrawLabels(false)
                axisLeft.setDrawGridLines(false)
                axisRight.setDrawGridLines(false)
                axisRight.isEnabled = false
                axisLeft.isEnabled = false
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
                xAxis.setDrawGridLines(false)
                xAxis.setAvoidFirstLastClipping(true)
                xAxis.textColor = textColor
                xAxis.granularity = 1f
            }
        }


        fun bind(position: Int) {

            val values = mutableListOf<Entry>()

            val primaryDarkColor = ContextCompat.getColor(context, R.color.primaryDarkColor)
            val primaryColor = ContextCompat.getColor(context, R.color.primaryColor)

            val time: List<String> = data.map {
                Util.getTimeFromUnixTime(it.dt)
            }

            when (position) {
                0 -> {
                    val temperature: List<Int> = data.map {
                        it.mainTemp
                    }

                    for (i in 0..temperature.size - 1) {
                        values.add(Entry(i.toFloat(), temperature.get(i).toFloat()))
                    }
                    val dataSet = LineDataSet(values, "")

                    dataSet.apply {
                        mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                        setDrawHighlightIndicators(false)
                        setDrawCircles(false)
                        lineWidth = 1.8f
                        circleRadius = 7f
                        highLightColor = primaryDarkColor
                        color = primaryColor
                        fillColor = primaryColor
                        valueTextColor = textColor
                        fillAlpha = 100
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return value.toInt().toString()
                            }
                        }
                    }

                    val data = LineData(dataSet)

                    data.apply {
                        setValueTextSize(9f)
                        setDrawValues(true)
                    }

                    lineChart.apply {
                        lineChart.data = data

                        xAxis.labelCount = values.size - 1
                        xAxis.valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                if (value.toInt() < time.size) {
                                    return time.get(value.toInt())
                                } else
                                    return "0"
                            }
                        }
                        animateXY(500, 1000)
                        invalidate()
                    }

                }
                2 -> {
                    val windSpeed: List<Int> = data.map {
                        it.windSpeed
                    }

                    for (i in 0..windSpeed.size - 1) {
                        values.add(Entry(i.toFloat(), windSpeed.get(i).toFloat()))
                    }

                    val dataSet = LineDataSet(values, "")

                    dataSet.apply {
                        mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                        setDrawHighlightIndicators(false)
                        setDrawCircles(false)
                        valueTextColor = textColor
                        lineWidth = 1.8f
                        circleRadius = 7f
                        highLightColor = primaryDarkColor
                        color = primaryColor
                        fillColor = primaryColor
                        fillAlpha = 100
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return value.toInt().toString()
                            }
                        }
                    }

                    val data = LineData(dataSet)
                    data.apply {
                        setValueTextSize(9f)
                        setDrawValues(true)
                    }

                    lineChart.apply {
                        lineChart.data = data
                        xAxis.labelCount = values.size - 1
                        xAxis.valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                if (value.toInt() < time.size) {
                                    return time.get(value.toInt())
                                } else
                                    return "0"
                            }
                        }
                        animateXY(500, 1000)
                        invalidate()
                    }
                }
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == BAR_CHART) {
            ViewHolder1(
                LayoutInflater.from(context)
                    .inflate(R.layout.view_pager_bar_chart_item, parent, false)
            )
        } else ViewHolder2(
            LayoutInflater.from(context).inflate(R.layout.view_pager_line_chart_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            0 -> (holder as ViewHolder2).bind(position)
            1 -> (holder as ViewHolder1).bind(position)
            2 -> (holder as ViewHolder2).bind(position)
            3 -> (holder as ViewHolder1).bind(position)
            else -> (holder as ViewHolder1).bind(position)
        }
    }

    override fun getItemCount(): Int = 4

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> return LINE_CHART
            1 -> return BAR_CHART
            2 -> return LINE_CHART
            3 -> return BAR_CHART
            else -> return BAR_CHART
        }
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        chartItemClickListener.onChartItemClick(data.get(e?.x!!.toInt()))
    }

    inner class MyMarkerView(context: Context?, layoutResource: Int) :
        MarkerView(context, layoutResource) {

        override fun getOffset(): MPPointF {
            return MPPointF((-(width / 2)).toFloat(), (-height / 2).toFloat())
        }

    }

}