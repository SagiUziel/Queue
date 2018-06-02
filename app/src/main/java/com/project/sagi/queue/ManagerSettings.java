package com.project.sagi.queue;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import fr.ganfra.materialspinner.MaterialSpinner;

public class ManagerSettings extends AppCompatActivity {
    private AddWorker awDialog;
    private boolean m_isWorkerImage;
    final private Context m_context = this;

    private List<clsWorker> GetWorkers() {
        if (GetBusiness().BusinessWorkers == null) {
            m_business.BusinessWorkers = new ArrayList<clsWorker>();
        }
        return m_business.BusinessWorkers;
    }

    private AdapterWorker _workerAdapter;
    private AdapterWorker GetWorkTypeAdapter() {
        if (_workerAdapter == null) {
            _workerAdapter = new AdapterWorker(ManagerSettings.this, GetWorkers());
            ListView listView = (ListView) findViewById(R.id.lstWorkers);
            listView.setAdapter(_workerAdapter);
        }
        return _workerAdapter;
    }

    private clsBusiness m_business;
    public clsBusiness GetBusiness() {
        if (m_business == null){
            m_business = new clsBusiness();
        }
        return m_business;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_settings);
        clsGlobalHelper.setLocalLanguage(getBaseContext());

//        String[] ITEMS = {"עיצוב שיער", "קוסמטיקה"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.input_BusinessType);
//        spinner.setAdapter(adapter);

        AutoCompleteTextView txtBusinessCity = ((AutoCompleteTextView)findViewById(R.id.txtBusinessCity));
        txtBusinessCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SocketService.SearchCity(s.toString(), m_context);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        AutoCompleteTextView txtBusinessStreet = ((AutoCompleteTextView)findViewById(R.id.txtBusinessStreet));
        txtBusinessStreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SocketService.SearchAddress(s.toString(), m_context);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        String fileGuid = UUID.randomUUID().toString();
        if (m_isWorkerImage == true) {
            awDialog.setWorkerImage(bitmap);
            awDialog.setWorkerImage(fileGuid + ".JPEG");
        } else {
            GetBusiness().BusinessImage = fileGuid + ".JPEG";
            setBusinessImage(bitmap);
        }

        SocketService.UploadImage(bitmap, this, fileGuid);
    }

    protected void setBusinessImage(Bitmap bitmap) {
        ImageView srcImageView = (ImageView)findViewById(R.id.imgBusinessImage);
        if (bitmap != null && srcImageView != null) {
            srcImageView.setImageBitmap(bitmap);
        }
    }

    public void PickBusinessImage(View view){
        m_isWorkerImage = false;
        ImagePicker.setMinQuality(8000, 8000);
        ImagePicker.pickImage(this, "בחר תמונה");
    }

    public void PickUserImage(View view){
        m_isWorkerImage = true;
        ImagePicker.setMinQuality(600, 600);
        ImagePicker.pickImage(this, "בחר תמונה");
    }

    public void OpenAddWorkerDialog(View v){
        awDialog = new AddWorker(this);
        awDialog.show();
        //awDialog.o

        Window window = awDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    public void addNewWorkingHours(View v) {
        awDialog.addNewWorkingHours(v);
    }

    public void addNewBreakingHours(View v) {
        awDialog.addNewBreakingHours(v);
    }

    public void addNewWorkType(View v) {
        awDialog.addNewWorkType(v);
    }

    public void RemoveWorkHourRow(View view) {
        awDialog.RemoveWorkHourRow(view);
    }

    public void RemoveWorkTypeRow(View view) {
        awDialog.RemoveWorkTypeRow(view);
    }

    public void openTimePickerDialog_Start(View view) {
        final View selectedView = view;
        final EditText Edit_Time = view.findViewById(R.id.txtStartTime);
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        List<clsWorkingHours> lst = awDialog.GetWorkingHours();
        if (view.getResources().getResourceEntryName(((View)view.getParent().getParent().getParent().getParent()).getId()).contains("Break") == true) {
            lst = awDialog.GetBreakingHours();
        }
        final List<clsWorkingHours> finalLst = lst;

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ManagerSettings.this, R.style.DialogTheme,new TimePickerDialog.OnTimeSetListener() {
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
    }

    public void openTimePickerDialog_End(View view) {
        final View selectedView = view;
        final EditText Edit_Time = (EditText)view.findViewById(R.id.txtEndTime);
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        List<clsWorkingHours> lst = awDialog.GetWorkingHours();
        if (view.getResources().getResourceEntryName(((View)view.getParent().getParent().getParent().getParent()).getId()).contains("Break") == true) {
            lst = awDialog.GetBreakingHours();
        }
        final List<clsWorkingHours> finalLst = lst;

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ManagerSettings.this, R.style.DialogTheme,new TimePickerDialog.OnTimeSetListener() {
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
    }

    public void openTimePickerDialog_Duration(View view) {
        final View selectedView = view;
        final EditText Edit_Time = (EditText)view.findViewById(R.id.txtWorkDuration);
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = 0;
        int minute = 10;

        final List<clsWorkType> lst = awDialog.GetWorkTypes();

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ManagerSettings.this, R.style.DialogTheme,new TimePickerDialog.OnTimeSetListener() {
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
    }

    public void openDayPickerDialog(View view) {
        final View selectedView = view;
        final EditText Edit_Time = (EditText)view.findViewById(R.id.txtDays);
        final String[] multiChoiceItems = getResources().getStringArray(R.array.dialog_multi_choice_array);
        List<clsWorkingHours> lst = awDialog.GetWorkingHours();
        if (view.getResources().getResourceEntryName(((View)view.getParent().getParent().getParent().getParent()).getId()).contains("Break") == true) {
            lst = awDialog.GetBreakingHours();
        }
        final List<clsWorkingHours> finalLst = lst;

        String selectedDays = Edit_Time.getText().toString();
        final boolean[] checkedItems = {selectedDays.contains("א"), selectedDays.contains("ב"), selectedDays.contains("ג"), selectedDays.contains("ד"), selectedDays.contains("ה"), selectedDays.contains("ו"), selectedDays.contains("ש")};
        new AlertDialog.Builder(this, R.style.DialogTheme)
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
                        if (checkedItems[0] == true) { textValue += "א',"; }
                        if (checkedItems[1] == true) { textValue += "ב',"; }
                        if (checkedItems[2] == true) { textValue += "ג',"; }
                        if (checkedItems[3] == true) { textValue += "ד',"; }
                        if (checkedItems[4] == true) { textValue += "ה',"; }
                        if (checkedItems[5] == true) { textValue += "ו',"; }
                        if (checkedItems[6] == true) { textValue += "ש',"; }
                        if (textValue.length() > 0) {
                            textValue = textValue.substring(0, textValue.length() - 1);
                        }
                        Edit_Time.setText(textValue);
                        View parentRow = (View)selectedView.getParent();
                        ListView clickedListView = (ListView) parentRow.getParent().getParent().getParent();
                        int position = clickedListView.getPositionForView(parentRow);
                        finalLst.get(position).WorkingDays_Display = textValue;
                        finalLst.get(position).WorkingDays = checkedItems;
                    }
                })
                .setNegativeButton("ביטול", null)
                .show();
    }

    public void addNewWorker(View v) {
        clsWorker newWorker = awDialog.GetNewWorker();
        if (newWorker != null)
        {
            GetWorkers().add(newWorker);
            GetWorkTypeAdapter().notifyDataSetChanged();

            ListView listView = (ListView) findViewById(R.id.lstWorkers);
            float scale = getResources().getDisplayMetrics().density;
            int totalHeight = (int)(GetWorkers().size() * 85 * scale);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,totalHeight);
            listView.setLayoutParams(parms);
        }
    }

    public void signUpBusiness(View view) {
        String txtBusinessName = ((EditText)findViewById(R.id.txtBusinessName)).getText().toString();
        String txtBusinessDescription = ((EditText)findViewById(R.id.txtBusinessDescription)).getText().toString();
        String txtBusinessPhone = ((EditText)findViewById(R.id.txtBusinessPhone)).getText().toString();
        String txtBusinessCity = ((EditText)findViewById(R.id.txtBusinessCity)).getText().toString();
        String txtBusinessStreet = ((EditText)findViewById(R.id.txtBusinessStreet)).getText().toString();

        if (txtBusinessName == null || txtBusinessName.length() < 3) {
            Toasty.warning(this, "הזן את שם העסק", Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (txtBusinessPhone == null || txtBusinessPhone.length() < 5) {
            Toasty.warning(this, "הזן את מספר הטלפון שלך", Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (txtBusinessCity == null || txtBusinessCity.length() < 3) {
            Toasty.warning(this, "הזן את העיר שבה ממוקם העסק", Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (txtBusinessStreet == null || txtBusinessStreet.length() < 3) {
            Toasty.warning(this, "הזן את הכתובת שבה ממוקם העסק", Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (GetBusiness().BusinessWorkers == null || GetBusiness().BusinessWorkers.size() == 0) {
            Toasty.warning(this, "הזן לפחות עובד אחד שעובד בעסק", Toast.LENGTH_SHORT, true).show();
            return;
        }

        if (GetBusiness().BusinessWorkers.size() > 50) {
            Toasty.warning(this, "לא ניתן להזין יותר מ- 50 עובדים", Toast.LENGTH_SHORT, true).show();
            return;
        }

        final Button btnSignUpBusiness=(Button)findViewById(R.id.btnSignUpBusiness);
        btnSignUpBusiness.setEnabled(false);

        clsBusiness business = GetBusiness();
        //business.UserOwners = txtBusinessName;
        business.BusinessName = txtBusinessName;
        business.BusinessDescription = txtBusinessDescription;
        business.BusinessPhone = txtBusinessPhone;
        business.BusinessCity = txtBusinessCity;
        business.BusinessStreet = txtBusinessStreet;
        //business.BusinessType = txtBusinessStreet;
        SocketService.SignUpBusiness(business, this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btnSignUpBusiness.setEnabled(true);
            }
        }, 2000);
    }

    public void selectBusinessType(View view) {
        AlertDialog.Builder b = new AlertDialog.Builder(this, R.style.DialogTheme);
        b.setTitle("בחר סוג עסק");
        String[] types = {"עיצוב שיער", "קוסמטיקה", "מוסך"};
        b.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        b.show();
    }
}
