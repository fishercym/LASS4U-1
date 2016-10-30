package farm.rododo.lass4u;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cht.android.Log;
import com.cht.android.ViewById;
import com.cht.android.ViewUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

public class ReportActivity extends AppCompatActivity {
    @Log
    Logger LOG;

    @ViewById(R.id.chart)
    BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ViewUtils.bind(this);

        ArrayList<BarEntry> es = new ArrayList<>();
        for (int i = 0;i < 10;i++) {
            float y = (float) (Math.random() * 100f);
            es.add(new BarEntry(i, y));
        }

        IBarDataSet s = new BarDataSet(es, "The year 2017");

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(s);

        BarData data = new BarData(dataSets);

        chart.setData(data);
    }
}
