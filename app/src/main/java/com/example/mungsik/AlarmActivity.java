package com.example.mungsik;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    static ArrayList<AlarmList> ItemArrayList = new ArrayList<AlarmList>();
    ListView listview;
    ListViewAdapter adapter;
    Intent intent;
    static int ItemID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        adapter = new ListViewAdapter(getApplicationContext(), R.layout.alarmobj, ItemArrayList);

        listview = (ListView)findViewById(R.id.alarmlist);
        listview.setLongClickable(true);
        listview.setAdapter(adapter);

        listview.setOnItemLongClickListener( new ListViewItemLongClickListener() );

        intent = new Intent(getApplicationContext(), AlarmAddActivity.class);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("ItemID", ItemID++);
                startActivityForResult(intent, 0);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AlarmList Item;

        switch (requestCode) {
            case 0:
            {
                if(resultCode == RESULT_OK)
                {
                    Item = (AlarmList) data.getSerializableExtra("Item");
                    setAlarm(Item);

                    if (listview.getAdapter() != null )
                        adapter = (ListViewAdapter)listview.getAdapter();

                    adapter.addItem(Item);
                    adapter.notifyDataSetChanged();
                }
            }
            break;
        }
    }

    void setAlarm(AlarmList newItem)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("AlarmTime", MODE_PRIVATE);
        long millis = sharedPreferences.getLong(newItem.getIDStr(), Calendar.getInstance().getTimeInMillis());

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, newItem.getID(), alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
        }
    }

    void cancelAlarm(AlarmList newItem)
    {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, newItem.getID(), alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
            final int selected = position;
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            final AlarmList deleteItem = ItemArrayList.get(selected);
            String message = deleteItem.changeAMPM();
            message += " 를 삭제하시겠습니까?";
            builder.setTitle(R.string.alert_msg_delete)
                    .setMessage(message);

            builder.setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = getSharedPreferences("AlarmTime", MODE_PRIVATE).edit();
                    editor.remove(deleteItem.getIDStr());
                    editor.apply();

                    cancelAlarm(deleteItem);

                    ItemArrayList.remove(selected);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            return false;
        }
    }
}