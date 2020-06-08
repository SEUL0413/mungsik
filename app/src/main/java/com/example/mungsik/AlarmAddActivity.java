package com.example.mungsik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class AlarmAddActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);

        final int ItemID = (Integer)getIntent().getSerializableExtra("ItemID");
        final TimePicker picker=(TimePicker)findViewById(R.id.timePicker);
        picker.setIs24HourView(true);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int hour = picker.getHour();
                int minute = picker.getMinute();

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                Date SelectedTime = calendar.getTime();

                SharedPreferences.Editor editor = getSharedPreferences("AlarmTime", MODE_PRIVATE).edit();
                editor.putLong(Integer.toString(ItemID), SelectedTime.getTime());
                editor.apply();

                Intent intent = new Intent(getApplicationContext(),AlarmActivity.class);
                AlarmList data = new AlarmList(SelectedTime, ItemID);
                intent.putExtra("Item", data);
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }
}