package com.project.sagi.queue;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import es.dmoral.toasty.Toasty;

public class AddWorker extends Dialog  {
    public Activity c;
    public Dialog d;

    public AddWorker(Activity a) {
        super(a);
        this.c = a;
    }

    private ArrayList<clsWorkingHours> _workingHours;
    public ArrayList<clsWorkingHours> GetWorkingHours(){
        if (_workingHours == null) {
            _workingHours = new ArrayList<clsWorkingHours>();
        }
        return _workingHours;
    }

    private ArrayList<clsWorkingHours> _breakingHours;
    public ArrayList<clsWorkingHours> GetBreakingHours(){
        if (_breakingHours == null) {
            _breakingHours = new ArrayList<clsWorkingHours>();
        }
        return _breakingHours;
    }

    private ArrayList<clsWorkType> _workTypes;
    public ArrayList<clsWorkType> GetWorkTypes(){
        if (_workTypes == null) {
            _workTypes = new ArrayList<clsWorkType>();
        }
        return _workTypes;
    }

    private AdapterWorkingHours _adapter;
    private AdapterWorkingHours GetWorkingHoursAdapter() {
        if (_adapter == null) {
            _adapter = new AdapterWorkingHours(c, GetWorkingHours());
            ListView listView = (ListView) findViewById(R.id.lstWorkingHours);
            LinearLayout layoutList = (LinearLayout) findViewById(R.id.lilWorkingHours);
            listView.setAdapter(_adapter);
        }
        return _adapter;
    }

    private AdapterWorkingHours _breakingHoursAdapter;
    private AdapterWorkingHours GetBreakingHoursAdapter() {
        if (_breakingHoursAdapter == null) {
            _breakingHoursAdapter = new AdapterWorkingHours(c, GetBreakingHours());
            ListView listView = (ListView) findViewById(R.id.lstBreakHours);
            LinearLayout layoutList = (LinearLayout) findViewById(R.id.lilBreakHours);
            listView.setAdapter(_breakingHoursAdapter);
        }
        return _breakingHoursAdapter;
    }

    private AdapterWorkType _workTypeAdapter;
    private AdapterWorkType GetWorkTypeAdapter() {
        if (_workTypeAdapter == null) {
            _workTypeAdapter = new AdapterWorkType(c, GetWorkTypes());
            ListView listView = (ListView) findViewById(R.id.lstWorkType);
            LinearLayout layoutList = (LinearLayout) findViewById(R.id.lilWorkType);
            listView.setAdapter(_workTypeAdapter);
        }
        return _workTypeAdapter;
    }

    private clsWorker _worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_worker);
        _worker = new clsWorker();
        addNewWorkingHours(null);
        addNewBreakingHours(null);
        addNewWorkType(null);
    }

    public void RemoveWorkHourRow(View view) {
        View parentRow = (View) view.getParent();
        ListView clickedListView = (ListView) parentRow.getParent();
        int position = clickedListView.getPositionForView(parentRow);

        if (view.getResources().getResourceEntryName(((View)view.getParent().getParent()).getId()).contains("Break") == true){
            ListView listView = (ListView) findViewById(R.id.lstBreakHours);
            LinearLayout layoutList = (LinearLayout) findViewById(R.id.lilBreakHours);
            GetBreakingHours().remove(position);
            GetBreakingHoursAdapter().notifyDataSetChanged();
            float scale = getContext().getResources().getDisplayMetrics().density;
            int totalHeight = (int) (GetBreakingHours().size() * 57 * scale);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,totalHeight);
            listView.setLayoutParams(parms);
        } else {
            ListView listView = (ListView) findViewById(R.id.lstWorkingHours);
            LinearLayout layoutList = (LinearLayout) findViewById(R.id.lilWorkingHours);
            GetWorkingHours().remove(position);
            GetWorkingHoursAdapter().notifyDataSetChanged();
            float scale = getContext().getResources().getDisplayMetrics().density;
            int totalHeight = (int) (GetWorkingHours().size() * 57 * scale);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,totalHeight);
            listView.setLayoutParams(parms);
        }
    }

    public void RemoveWorkTypeRow(View view) {
        View parentRow = (View) view.getParent();
        ListView clickedListView = (ListView) parentRow.getParent();
        int position = clickedListView.getPositionForView(parentRow);

        ListView listView = (ListView) findViewById(R.id.lstWorkType);
        LinearLayout layoutList = (LinearLayout) findViewById(R.id.lilWorkType);
        GetWorkTypes().remove(position);
        GetWorkTypeAdapter().notifyDataSetChanged();
        float scale = getContext().getResources().getDisplayMetrics().density;
        int totalHeight = (int) (GetWorkTypes().size() * 57 * scale);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,totalHeight);
        listView.setLayoutParams(parms);
    }

    protected void addNewWorkingHours(View v) {
        final ArrayList<clsWorkingHours> workingHoursLst = GetWorkingHours();
        workingHoursLst.add(new clsWorkingHours());
        ListView listView = (ListView) findViewById(R.id.lstWorkingHours);

        GetWorkingHoursAdapter().notifyDataSetChanged();

        final float scale = getContext().getResources().getDisplayMetrics().density;
        int totalHeight = (int) (workingHoursLst.size() * 57 * scale);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,totalHeight);
        listView.setLayoutParams(parms);
    }

    protected void addNewBreakingHours(View v) {
        final ArrayList<clsWorkingHours> breakingHoursLst = GetBreakingHours();
        breakingHoursLst.add(new clsWorkingHours());
        ListView listView = (ListView) findViewById(R.id.lstBreakHours);

        GetBreakingHoursAdapter().notifyDataSetChanged();

        final float scale = getContext().getResources().getDisplayMetrics().density;
        int totalHeight = (int) (breakingHoursLst.size() * 57 * scale);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,totalHeight);
        listView.setLayoutParams(parms);
    }

    protected void addNewWorkType(View v) {
        final ArrayList<clsWorkType> workTypesLst = GetWorkTypes();
        workTypesLst.add(new clsWorkType());
        ListView listView = (ListView) findViewById(R.id.lstWorkType);

        GetWorkTypeAdapter().notifyDataSetChanged();

        final float scale = getContext().getResources().getDisplayMetrics().density;
        int totalHeight = (int) (workTypesLst.size() * 57 * scale);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,totalHeight);
        listView.setLayoutParams(parms);
    }

    protected void setWorkerImage(Bitmap bitmap) {
        CircularImageView circularImageView = (CircularImageView)findViewById(R.id.WorkerImage);
        if (bitmap != null && circularImageView != null) {
            circularImageView.setImageBitmap(bitmap);
        }
    }

    public void setWorkerImage(String imageName){
        _worker.WorkerImage = imageName;
    }

    public clsWorker GetNewWorker() {
        String WorkerName = ((EditText)(findViewById(R.id.txtWorkerName))).getText().toString();
        List<clsWorkingHours> WorkerWorkingHours = GetWorkingHours();

        if (WorkerName == null || WorkerName.length() < 2) {
            Toasty.warning(c, "הזן את שם העובד", Toast.LENGTH_SHORT, true).show();
            return null;
        }

        if (WorkerWorkingHours == null || WorkerWorkingHours.size() == 0) {
            Toasty.warning(c, "הזן שעות עבודה", Toast.LENGTH_SHORT, true).show();
            return null;
        }

        for (int i = 0; i< WorkerWorkingHours.size(); i++){
            clsWorkingHours workHour = WorkerWorkingHours.get(i);
            if (workHour.FromTime == null) {
                Toasty.warning(c, "הזן שעת התחלת עבודה", Toast.LENGTH_SHORT, true).show();
                return null;
            }
            if (workHour.ToTime == null) {
                Toasty.warning(c, "הזן שעת סיום עבודה", Toast.LENGTH_SHORT, true).show();
                return null;
            }
            boolean isWorkDayOk = false;
            if (workHour.WorkingDays == null){
                Toasty.warning(c, "הזן ימי עבודה", Toast.LENGTH_SHORT, true).show();
                return null;
            }
            for (int j = 0; j < workHour.WorkingDays.length; j++)
            {
                if (workHour.WorkingDays[j] == true)
                {
                    isWorkDayOk = true;
                    break;
                }
            }
            if (isWorkDayOk == false) {
                Toasty.warning(c, "הזן ימי עבודה", Toast.LENGTH_SHORT, true).show();
                return null;
            }
        }

        List<clsWorkType> WorkTypes = GetWorkTypes();

        for (int i = 0; i< WorkTypes.size(); i++){
            clsWorkType workHour = WorkTypes.get(i);
            if (workHour.WorkType == null) {
                Toasty.warning(c, "הזן סוג טיפול", Toast.LENGTH_SHORT, true).show();
                return null;
            }
            if (workHour.WorkDuration == null) {
                Toasty.warning(c, "הזן זמן טיפול", Toast.LENGTH_SHORT, true).show();
                return null;
            }
        }

        clsWorker worker = _worker;
        worker.WorkerBreakingHours = GetBreakingHours();
        worker.WorkerWorkingHours = WorkerWorkingHours;
        worker.WorkerWorkTypes = GetWorkTypes();
        worker.WorkerName = WorkerName;
        dismiss();
        return worker;
    }
}

