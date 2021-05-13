package com.monash.paindiary.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.DragAndDropPermissions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.monash.paindiary.R;
import com.monash.paindiary.databinding.FragmentReport3Binding;
import com.monash.paindiary.entity.PainRecord;
import com.monash.paindiary.helper.WeatherInfo;
import com.monash.paindiary.viewmodel.PainRecordViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Report3Fragment extends Fragment {
    private FragmentReport3Binding binding;
    private LineChart lineChart;
    private MaterialDatePicker.Builder dateRangeBuilder;
    private Date startDate;
    private Date endDate;
    private ArrayAdapter<String> arrayAdapter;
    private String selectedWeatherAtt = "";
    // Do we require? to store data ? because every time time period change data should change
//    private List<Integer> painIntensityList;
//    // TODO change float to long and also in entity class.
//    private List<Triplet<Float, Float, Float>> weatherAttrList;

    public Report3Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReport3Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
//        combinedChart = binding.combineChart;
        lineChart = binding.relationalLineChart;
        dateRangeBuilder = MaterialDatePicker.Builder.dateRangePicker();
        fillSpinner();

        binding.btnSelectDateRange.setOnClickListener(this::btnSelectDateRangeOnClicked);
        binding.btnShow.setOnClickListener(this::btnShowOnClicked);
        binding.btnClear.setOnClickListener(this::btnClearOnClicked);
        binding.editTextWeatherAttribute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedWeatherAtt = String.valueOf(binding.editTextWeatherAttribute.getAdapter().getItem(position));
            }
        });

        setUpRelationalLineChart();
        return view;
    }

    private void fillSpinner() {
        ArrayList<String> arrayList = new ArrayList<String>() {{
            add("Temperature");
            add("Humidity");
            add("Pressure");
        }};
        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arrayList);
        binding.editTextWeatherAttribute.setAdapter(arrayAdapter);
        selectedWeatherAtt = "Temperature";
        binding.editTextWeatherAttribute.setText(selectedWeatherAtt, false);
    }

    private void btnSelectDateRangeOnClicked(View view) {
        binding.layoutSpinnerWeatherAttribute.clearFocus();
        MaterialDatePicker datePicker = dateRangeBuilder
                .setTitleText("Select dates")
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
            Pair<Long, Long> pair = (Pair<Long, Long>) selection;
            startDate = new Date(pair.first);
            endDate = new Date(pair.second);
            binding.btnSelectDateRange.setText(String.format("%s - %s", dateFormat.format(startDate), dateFormat.format(endDate)));
        });
        datePicker.show(getActivity().getSupportFragmentManager(), "DATE_RANGE_PICKER");
    }

    private void btnShowOnClicked(View view) {
        binding.layoutSpinnerWeatherAttribute.clearFocus();
        if (startDate == null || endDate == null) {
            Toast.makeText(getContext(), "Please select date range first.", Toast.LENGTH_SHORT).show();
        } else {
            if (lineChart.getLineData() != null) {
                lineChart.setData(null);
                lineChart.invalidate();
            }
            loadRelationalLineChartData();
        }
    }

    private void btnClearOnClicked(View view) {
        binding.layoutSpinnerWeatherAttribute.clearFocus();
        startDate = null;
        endDate = null;
        lineChart.resetZoom();
        lineChart.setData(null);
        binding.btnSelectDateRange.setText("Select date range");
    }


    private void setUpRelationalLineChart() {
        lineChart.getDescription().setEnabled(false);
        lineChart.setNoDataText("Please select Date Range and Weather Attribute then hit SHOW.");
        lineChart.setNoDataTextColor(getResources().getColor(R.color.teal_500, null));

        Legend l = lineChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
//        rightAxis.setGranularity(1f);
        rightAxis.setAxisMinimum(0f);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
//        leftAxis.setGranularity(10f);
        leftAxis.setAxisMinimum(0f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
//        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one day
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                Date date = new Date((long) value);
                //Specify the format you'd like
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd", Locale.ENGLISH);
                return sdf.format(date);
            }
        });
//        xAxis.setValueFormatter(new ValueFormatter() {
//            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
//
//            @Override
//            public String getFormattedValue(float value) {
//                long millis = TimeUnit.DAYS.toMillis((long) value);
//                return mFormat.format(new Date(millis));
//            }
//        });
    }

    private void loadRelationalLineChartData() {
        new Thread(() -> {
            PainRecordViewModel viewModel = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);
            viewModel.findRecordsBetweenDate(startDate, endDate).thenApply(painRecords -> {
                ArrayList<Entry> entries_pain_intensities = new ArrayList<Entry>();
                ArrayList<Entry> entries_weather_att = new ArrayList<Entry>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM");

                for (PainRecord painRecord : painRecords) {
                    entries_pain_intensities.add(
                            new Entry(
//                                    TimeUnit.MILLISECONDS.toDays(painRecord.getDateTime()),
                                    painRecord.getDateTime(),
                                    painRecord.getPainIntensityLevel()));
                    entries_weather_att.add(
                            new Entry(
                                    painRecord.getDateTime(),
                                    selectedWeatherAtt.toLowerCase().equals("pressure") ?
                                            painRecord.getPressure() :
                                            selectedWeatherAtt.toLowerCase().equals("humidity") ?
                                                    painRecord.getHumidity() :
                                                    painRecord.getTemperature()));
                }

                LineData data = new LineData();

                data.addDataSet(
                        getDataSet(
                                entries_pain_intensities,
                                YAxis.AxisDependency.LEFT,
                                "Pain Intensity",
                                getResources().getColor(R.color.teal_500, null)));
                data.addDataSet(
                        getDataSet(
                                entries_weather_att,
                                YAxis.AxisDependency.RIGHT,
                                selectedWeatherAtt,
                                getResources().getColor(R.color.yellow_theme_dark, null)));
                lineChart.setData(data);
                lineChart.invalidate();

                lineChart.animateX(1000, Easing.EaseInOutSine);

                return painRecords;
            });
        }).start();
    }

    @NonNull
    private LineDataSet getDataSet(List<Entry> entries, YAxis.AxisDependency axisDependency, String label, int color) {
        Collections.sort(entries, new EntryXComparator());
        LineDataSet set = new LineDataSet(entries, label);
        set.setColor(color);
        set.setLineWidth(2.5f);
        set.setCircleColor(color);
        set.setCircleRadius(5f);
        set.setFillColor(color);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(color);

        set.setAxisDependency(axisDependency);
        return set;
    }

}

//class Triplet<T, U, V> {
//
//    private final T first;
//    private final U second;
//    private final V third;
//
//    public Triplet(T first, U second, V third) {
//        this.first = first;
//        this.second = second;
//        this.third = third;
//    }
//
//    public T getFirst() {
//        return first;
//    }
//
//    public U getSecond() {
//        return second;
//    }
//
//    public V getThird() {
//        return third;
//    }
//}
