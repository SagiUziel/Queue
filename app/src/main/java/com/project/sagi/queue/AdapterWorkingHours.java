package com.project.sagi.queue;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdapterWorkingHours extends ArrayAdapter<clsWorkingHours> {
    private ArrayList<clsWorkingHours>
            _workingHours;
    private Context _c;
    private View _view;
    AdapterWorkingHours(Context context, ArrayList<clsWorkingHours> workingHours) {
        super(context, 0, workingHours);
        _c = context;
        _workingHours = workingHours;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        _view = convertView;
        clsWorkingHours workingHour = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.working_hour_row, parent, false);
        }
        TextView txtDays = convertView.findViewById(R.id.txtDays);
        TextView txtStartTime = convertView.findViewById(R.id.txtStartTime);
        TextView txtEndTime = convertView.findViewById(R.id.txtEndTime);
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

        txtDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDayPickerDialog(v);
            }
        });

        txtStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePickerDialog_Start(v);
            }
        });

        txtEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePickerDialog_End(v);
            }
        });

        Button btn_cleanDays_Work = convertView.findViewById(R.id.btn_cleanDays);
        btn_cleanDays_Work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveWorkHourRow(v);
            }
        });
        return convertView;
    }

    private void openDayPickerDialog(View view) {
        try {
            final View selectedView = view;
            final EditText Edit_Time = view.findViewById(R.id.txtDays);
            final String[] multiChoiceItems = _c.getResources().getStringArray(R.array.dialog_multi_choice_array);

            final List<clsWorkingHours> finalLst = _workingHours;

            String selectedDays = Edit_Time.getText().toString();
            final boolean[] checkedItems = {selectedDays.contains("א"), selectedDays.contains("ב"), selectedDays.contains("ג"), selectedDays.contains("ד"), selectedDays.contains("ה"), selectedDays.contains("ו"), selectedDays.contains("ש")};
            new AlertDialog.Builder(_c, R.style.DialogTheme)
                    .setTitle("בחר ימי עבודה")
                    .setMultiChoiceItems(multiChoiceItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int index, boolean isChecked) {
                            checkedItems[index] = isChecked;
                        }
                    })
                    .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String textValue = "";
                            if (checkedItems[0] == true) {
                                textValue += "א',";
                            }
                            if (checkedItems[1] == true) {
                                textValue += "ב',";
                            }
                            if (checkedItems[2] == true) {
                                textValue += "ג',";
                            }
                            if (checkedItems[3] == true) {
                                textValue += "ד',";
                            }
                            if (checkedItems[4] == true) {
                                textValue += "ה',";
                            }
                            if (checkedItems[5] == true) {
                                textValue += "ו',";
                            }
                            if (checkedItems[6] == true) {
                                textValue += "ש',";
                            }
                            if (textValue.length() > 0) {
                                textValue = textValue.substring(0, textValue.length() - 1);
                            }
                            Edit_Time.setText(textValue);
                            View parentRow = (View) selectedView.getParent();
                            ListView clickedListView = (ListView) parentRow.getParent().getParent().getParent();
                            int position = clickedListView.getPositionForView(parentRow);
                            finalLst.get(position).WorkingDays_Display = textValue;
                            finalLst.get(position).WorkingDays = checkedItems;
                        }
                    })
                    .setNegativeButton("ביטול", null)
                    .show();
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    public void openTimePickerDialog_Start(View view) {
        try{
            final View selectedView = view;
            final EditText Edit_Time = view.findViewById(R.id.txtStartTime);
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            List<clsWorkingHours> lst = _workingHours;
            if (view.getResources().getResourceEntryName(((View)view.getParent().getParent().getParent().getParent()).getId()).contains("Break") == true) {
                lst = _workingHours;
            }
            final List<clsWorkingHours> finalLst = lst;

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(_c, R.style.DialogTheme,new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    View parentRow = (View)selectedView.getParent();
                    ListView clickedListView = (ListView)parentRow.getParent().getParent().getParent();
                    int position = clickedListView.getPositionForView(parentRow);
                    finalLst.get(position).FromTime = new Time(selectedHour, selectedMinute, 0);
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

    public void openTimePickerDialog_End(View view) {
        try{
            final View selectedView = view;
            final EditText Edit_Time = (EditText)view.findViewById(R.id.txtEndTime);
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            List<clsWorkingHours> lst = _workingHours;
            final List<clsWorkingHours> finalLst = lst;

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(_c, R.style.DialogTheme,new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    View parentRow = (View)selectedView.getParent();
                    ListView clickedListView = (ListView) parentRow.getParent().getParent().getParent();
                    int position = clickedListView.getPositionForView(parentRow);
                    finalLst.get(position).ToTime = new Time(selectedHour, selectedMinute, 0);
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

    public void RemoveWorkHourRow(View view) {
        View parentRow = (View) view.getParent();
        ListView clickedListView = (ListView) parentRow.getParent();
        int position = clickedListView.getPositionForView(parentRow);
        _workingHours.remove(position);
        notifyDataSetChanged();
    }
}
