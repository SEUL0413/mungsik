package com.example.mungsik;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BluetoothActivity extends AppCompatActivity {
    Button on_btn;
    Button off_btn;
    Button connect_btn;
    Button back_btn;

    BlueToothController mBluetoothController = BlueToothController.getBluetoothController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        on_btn = (Button) findViewById(R.id.on_btn);
        off_btn = (Button) findViewById(R.id.off_btn);
        connect_btn = (Button) findViewById(R.id.connect_btn);
        back_btn = (Button) findViewById(R.id.back_btn);

        on_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = mBluetoothController.On();
                if (result)
                {
                    Toast.makeText(getApplicationContext(), "블루투스가 이미 활성화 되어 있습니다.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "블루투스를 활성화 되었습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        off_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = mBluetoothController.Off();
                switch (result) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "블루투스를 비활성화 했습니다.", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "블루투스가 이미 비활성화 되어 있습니다.", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        connect_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = mBluetoothController.listPairedDevices();

                AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothActivity.this);
                builder.setTitle("장치 선택");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        mBluetoothController.connectSelectedDevice(items[item].toString());
                   }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        back_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 호출된 액티비티 종료
    }
}
