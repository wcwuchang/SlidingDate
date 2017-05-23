package main.com.slidingdate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SlidingDate.DateChangedListener{

    TextView mtvCurrent;
    TextView mtvInterval;
    TextView mtvSelect;
    TextView mtvWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SlidingDate slidingDate=(SlidingDate)findViewById(R.id.sd1);
        mtvCurrent=(TextView)findViewById(R.id.tv_current_date);
        mtvInterval=(TextView)findViewById(R.id.tv_interval_date);
        mtvSelect=(TextView)findViewById(R.id.tv_select_date);
        mtvWeek=(TextView)findViewById(R.id.tv_week_date);
        slidingDate.setListener(this);

        mtvCurrent.setText(slidingDate.getCurrentDate());
    }

    @Override
    public void currentDate(int year,int month,int day) {
        Toast.makeText(this,year+"年"+month+"月"+day+"日",Toast.LENGTH_SHORT).show();
        mtvCurrent.setText(year+"年"+month+"月"+day+"日");
    }

    @Override
    public void selectDate(String date) {
        Toast.makeText(this,date,Toast.LENGTH_SHORT).show();
        mtvSelect.setText(date);
    }

    @Override
    public void timeInterval(String date0, String date1) {
        Toast.makeText(this,"开始"+date0+"结束"+date1,Toast.LENGTH_SHORT).show();
        mtvInterval.setText(date0+"至"+date1);
    }

    @Override
    public void ymw(int year, int month,int day, String week) {
        mtvWeek.setText(year+"年"+month+"月"+day+"日       周"+week);
    }
}
