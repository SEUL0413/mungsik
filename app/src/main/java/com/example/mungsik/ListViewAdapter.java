package com.example.mungsik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<AlarmList> {
    private ArrayList<AlarmList> listViewItemList;
    private Context context;

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    public ListViewAdapter(Context context, int resource, ArrayList<AlarmList> objects)
    {
        super(context, resource, objects);
        this.listViewItemList = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.alarmobj, parent, false);
        }

        TextView hourTextView = (TextView) convertView.findViewById(R.id.AlarmInstance) ;

        final AlarmList listViewItem = listViewItemList.get(position);
        String full_date = listViewItem.changeAMPM();
        hourTextView.setText(full_date);

        return convertView;
    }

    public void addItem(AlarmList Item)
    {
        listViewItemList.add(Item);
    }

    public void clearItem(){
        listViewItemList.clear();
    }
}