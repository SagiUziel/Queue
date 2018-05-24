package com.project.sagi.queue;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class AdapterWorkType extends ArrayAdapter<clsWorkType> {
    private ArrayList<clsWorkType> _workTypes;
    public AdapterWorkType(Context context, ArrayList<clsWorkType> workTypes) {
        super(context, 0, workTypes);
        _workTypes = workTypes;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int finalPosition = position;
        clsWorkType workType = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.work_type_row, parent, false);
        }
        final TextView txtWorkType = (TextView) convertView.findViewById(R.id.txtWorkType);
        TextView txtWorkDuration = (TextView) convertView.findViewById(R.id.txtWorkDuration);

        if (workType.WorkDuration != null) {
            txtWorkDuration.setText(workType.WorkDuration.toString());
        } else {
            txtWorkDuration.setText("");
        }
        if (workType.WorkType != null) {
            txtWorkType.setText(workType.WorkType.toString());
        } else {
            txtWorkType.setText("");
        }

        txtWorkType.addTextChangedListener(new TextWatcher() {
            int len=0;
            @Override
            public void afterTextChanged(Editable s) {
                String str = txtWorkType.getText().toString();
                _workTypes.get(finalPosition).WorkType = str;
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
}



