package com.project.sagi.queue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterWorkingHours extends ArrayAdapter<clsWorkingHours> {

    public AdapterWorkingHours(Context context, ArrayList<clsWorkingHours> workingHours) {
        super(context, 0, workingHours);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        clsWorkingHours workingHour = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.working_hour_row, parent, false);
        }
        TextView txtDays = (TextView) convertView.findViewById(R.id.txtDays);
        TextView txtStartTime = (TextView) convertView.findViewById(R.id.txtStartTime);
        TextView txtEndTime = (TextView) convertView.findViewById(R.id.txtEndTime);
        if (workingHour.WorkingDays_Display != null) {
            txtDays.setText(workingHour.WorkingDays_Display);
        } else {
            txtDays.setText("");
        }
        if (workingHour.FromTime != null) {
            txtStartTime.setText(workingHour.FromTime.toString());
        } else {
            txtStartTime.setText("");
        }
        if (workingHour.ToTime != null) {
            txtEndTime.setText(workingHour.ToTime.toString());
        } else {
            txtEndTime.setText("");
        }



        return convertView;
    }
}
