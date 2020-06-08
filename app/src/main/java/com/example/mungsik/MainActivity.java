package com.example.mungsik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    //액티비티에서 생성한 버튼을 선언
    Button execute_btn;
    Button bluetooth_setting_btn;
    Button alarm_setting_btn;

    BlueToothController mBluetoothController = BlueToothController.getBluetoothController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        execute_btn = (Button)findViewById(R.id.execute_btn);
        execute_btn.setEnabled(mBluetoothController.isConnected());
        execute_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBluetoothController.isConnected()) {
                    mBluetoothController.execute();
                }
            }
        });

        bluetooth_setting_btn = (Button)findViewById(R.id.bluetooth_setting_btn);
        bluetooth_setting_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BluetoothActivity.class);
                startActivity(intent);
            }
        });

        alarm_setting_btn = (Button)findViewById(R.id.alarm_setting_btn);
        alarm_setting_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        execute_btn = (Button)findViewById(R.id.execute_btn);
        execute_btn.setEnabled(mBluetoothController.isConnected());
    }
}
