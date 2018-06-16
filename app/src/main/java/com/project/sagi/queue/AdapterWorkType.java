package com.project.sagi.queue;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class AdapterWorkType extends ArrayAdapter<clsWorkType> {
    private ArrayList<clsWorkType> _workTypes;
    private View _view;
    private Context _c;
    AdapterWorkType(Context context, ArrayList<clsWorkType> workTypes) {
        super(context, 0, workTypes);
        _c = context;
        _workTypes = workTypes;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int finalPosition = position;
        _view = convertView;
        clsWorkType workType = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.work_type_row, parent, false);
        }
        final TextView txtWorkType = convertView.findViewById(R.id.txtWorkType);
        TextView txtWorkDuration = convertView.findViewById(R.id.txtWorkDuration);

        assert workType != null;
        if (workType.WorkDuration != null) {
            txtWorkDuration.setText(workType.WorkDuration.toString());
        } else {
            txtWorkDuration.setText("");
        }
        if (workType.WorkType != null) {
            txtWorkType.setText(workType.WorkType);
        } else {
            txtWorkType.setText("");
        }

        txtWorkDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePickerDialog_Duration(v);
            }
        });

        Button btn_cleanDays = convertView.findViewById(R.id.btn_cleanDays);
        btn_cleanDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveWorkTypeRow(v);
            }
        });

        txtWorkType.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (_workTypes.size() > finalPosition)
                {
                    _workTypes.get(finalPosition).WorkType = txtWorkType.getText().toString();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        return convertView;
    }

    private void openTimePickerDialog_Duration(View view) {
    try{
        final View selectedView = view;
        final EditText Edit_Time = view.findViewById(R.id.txtWorkDuration);
        int hour = 0;
        int minute = 10;

        final List<clsWorkType> lst = _workTypes;

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(_c, R.style.DialogTheme,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                View parentRow = (View)selectedView.getParent();
                ListView clickedListView = (ListView) parentRow.getParent().getParent().getParent();
                int position = clickedListView.getPositionForView(parentRow);
                lst.get(position).WorkDuration = new Time(selectedHour, selectedMinute, 0);
                String Hour = selectedHour + ":";
                if (selectedHour < 10) {
                    Hour = "0" + Hour;
                }
                if (selectedMinute < 10) {
                    Hour = Hour + "0";
                }
                Edit_Time.setText(Hour + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setButton(DialogInterface.BUTTON_POSITIVE, "אישור", mTimePicker);
        mTimePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "ביטול", mTimePicker);
        mTimePicker.show();
    } catch (Exception ex) {
        SocketService.WriteError(ex);
    }
}

    private void RemoveWorkTypeRow(View view) {
        View parentRow = (View) view.getParent();
        ListView clickedListView = (ListView) parentRow.getParent();
        int position = clickedListView.getPositionForView(parentRow);
        _workTypes.remove(position);
        notifyDataSetChanged();
    }
}



