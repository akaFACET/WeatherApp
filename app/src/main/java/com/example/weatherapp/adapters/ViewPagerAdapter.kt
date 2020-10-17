package com.example.weatherapp.adapters

import android.content.Context
import android.graphics.Color
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.Utils.Util
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerImage
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF


class ViewPagerAdapter(
    private val context: Context,
    var data: List<WeatherPerHour>,
    val chartItemClickListener: OnChartItemClickListener
)// you can pass other parameters in constructor
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), OnChartValueSelectedListener {

    internal val BAR_CHART = 1
    internal val LINE_CHART = 2

    val textColor = ContextCompat.getColor(context,R.color.colorText)

    private inner class ViewHolder1 internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        internal var barChart: BarChart

        init {
            barChart =
                itemView.findViewById(R.id.chart_bch) // Initialize your All views prensent in list items

            barChart.setOnChartValueSelectedListener(this@ViewPagerAdapter)

            barChart.apply {
                setViewPortOffsets(40f, 50f, 40f, 50f)
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(false)
                setPinchZoom(false)
                setDrawGridBackground(false)
                //maxHighlightDistance = 300f
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
                //setFitBars(true)
            }

        }

        internal fun bind(position: Int) {

            var values = mutableListOf<BarEntry>()

            val time: List<String> = data.map {
                //it.dt_txt
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
                        highLightColor = Color.rgb(244, 117, 117)
                        color = Color.parseColor("#fbc02d")
                        valueTextColor = textColor
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return value.toString() + " мм"
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
                        highLightColor = Color.rgb(244, 117, 117)
                        color = Color.parseColor("#fbc02d")
                        valueTextColor = textColor
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return value.toInt().toString() + "%"

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

    private inner class ViewHolder2 internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        internal var lineChart: LineChart


        internal var mv = MyMarkerView(context, R.layout.marker_view)

        init {

            lineChart = itemView.findViewById(R.id.chart_lch)

            lineChart.setOnChartValueSelectedListener(this@ViewPagerAdapter)

            lineChart.marker = mv

            lineChart.apply {
                setViewPortOffsets(30f, 0f, 30f, 50f)
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(false)
                setPinchZoom(false)
                setDrawGridBackground(false)
                //maxHighlightDistance = 300f
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


        internal fun bind(position: Int) {

            var values = mutableListOf<Entry>()

            val time: List<String> = data.map {
                Util.getTimeFromUnixTime(it.dt)
            }

            when (position) {
                0 -> {
                    val temperature: List<Int> = data.map {
                        it.mainTemp
                    }

                    for (i in 0..temperature.size-1) {
                        values.add(Entry(i.toFloat(), temperature.get(i).toFloat()))
                    }

                    val dataSet = LineDataSet(values, "")

                    dataSet.apply {
                        mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                        setDrawHighlightIndicators(false)
                        setDrawCircles(false)
                        lineWidth = 1.8f
                        circleRadius = 7f
                        highLightColor = Color.rgb(244, 117, 117)
                        color = Color.parseColor("#fbc02d")
                        fillColor = Color.parseColor("#fbc02d")
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
                        highLightColor = Color.rgb(244, 117, 117)
                        color = Color.parseColor("#fbc02d")
                        fillColor = Color.parseColor("#fbc02d")
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
                        //setValueTextColor(ResourcesCompat.getColor(R.color.colorText))
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
        ) //if it's not VIEW_TYPE_ONE then its VIEW_TYPE_TWO
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

    override fun getItemCount(): Int = 4 // 4

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
            return MPPointF((-(width / 2)).toFloat(), (-height /2 ).toFloat())
        }

    }

}