package farm.rododo.lass4u;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cht.android.App;
import com.cht.android.Log;
import com.cht.android.ViewById;
import com.cht.android.ViewUtils;
import com.cht.iot.persistence.entity.data.Rawdata;
import com.cht.iot.util.JsonUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReportActivity extends AppCompatActivity {
    @Log
    Logger LOG;

    @App
    MainApplication application;

    @ViewById(R.id.chart)
    BarChart chart;

    List<Rawdata> rawdatas;

    long min = Long.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ViewUtils.bind(this);

        rawdatas = application.get("rawdatas");

        XAxis xaxis = chart.getXAxis();
        xaxis.setValueFormatter(new AxisValueFormatter() {
            final DateFormat DF = new SimpleDateFormat("MM/dd HH:mm");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Date t = new Date(TimeUnit.MINUTES.toMillis((long) value + min));
                return DF.format(t);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        ArrayList<BarEntry> es = new ArrayList<>();
        for (Rawdata rawdata : rawdatas) {

            try {
                Date t = Constants.UTC.parse(rawdata.getTime());
                long x = TimeUnit.MILLISECONDS.toMinutes(t.getTime());

                min = Math.min(min, x);

                float y = Float.parseFloat(rawdata.getValue()[0]);
                es.add(new BarEntry(x - min, y));

            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }

        IBarDataSet s = new BarDataSet(es, "Trend Report");

        ArrayList<IBarDataSet> sets = new ArrayList<>();
        sets.add(s);

        BarData data = new BarData(sets);

        chart.setData(data);
    }
}
