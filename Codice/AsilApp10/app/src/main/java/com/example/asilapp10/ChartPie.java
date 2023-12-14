package com.example.asilapp10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class ChartPie extends AppCompatActivity {
    AnyChartView anyChartView;

    String[] months = {"Food", "Drugs", "Other"};
    int[] earnings = {500, 800, 2000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_pie);

        anyChartView = findViewById(R.id.any_chart_view);

        setupPieChart();
    }

    public void setupPieChart(){

        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for ( int i = 0; i < months.length; i++) {
            dataEntries.add(new ValueDataEntry(months[i], earnings[i]));
        }

        pie.data(dataEntries);
        pie.palette(new String[]{"#ED2B2B", "#53C943", "#2D70D3"}); // Ad esempio: rosso, verde, blu
        anyChartView.setChart(pie);
    }
}