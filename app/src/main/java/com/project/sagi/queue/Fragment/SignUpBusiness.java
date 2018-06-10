package com.project.sagi.queue.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import com.project.sagi.queue.AdapterWorker;
import com.project.sagi.queue.AddWorker;
import com.project.sagi.queue.R;
import com.project.sagi.queue.SocketService;
import com.project.sagi.queue.clsBusiness;
import com.project.sagi.queue.clsBusinessType;
import com.project.sagi.queue.clsWorkType;
import com.project.sagi.queue.clsWorker;
import com.project.sagi.queue.clsWorkingHours;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class SignUpBusiness extends Fragment {
    private AddWorker awDialog;
    private boolean m_isWorkerImage;
    final private Context m_context = getActivity();
    private View m_view;

    private List<clsWorker> GetWorkers() {
        if (GetBusiness().BusinessWorkers == null) {
            m_business.BusinessWorkers = new ArrayList<clsWorker>();
        }
        return m_business.BusinessWorkers;
    }

    private AdapterWorker _workerAdapter;
    private AdapterWorker GetWorkTypeAdapter() {
        if (_workerAdapter == null) {
            _workerAdapter = new AdapterWorker(getActivity(), GetWorkers());
            ListView listView = (ListView) m_view.findViewById(R.id.lstWorkers);
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstaceState){
        View view = inflater.inflate(R.layout.activity_manager_settings, container, false);
        try{
            view.setId(R.id.SignUpBusiness);
            m_view = view;
            AutoCompleteTextView txtBusinessCity = ((AutoCompleteTextView)view.findViewById(R.id.txtBusinessCity));
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

            AutoCompleteTextView txtBusinessStreet = ((AutoCompleteTextView)view.findViewById(R.id.txtBusinessStreet));
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

            ImageView imgBusinessImage=(ImageView)view.findViewById(R.id.imgBusinessImage);
            imgBusinessImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickBusinessImage(v);
                }
            });

            EditText txtBusinessType=(EditText)view.findViewById(R.id.txtBusinessType);
            txtBusinessType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectBusinessType(v);
                }
            });

            Button btn_addWorker=(Button)view.findViewById(R.id.btn_addWorker);
            btn_addWorker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenAddWorkerDialog(v);
                }
            });

            Button btnSignUpBusiness=(Button)view.findViewById(R.id.btnSignUpBusiness);
            btnSignUpBusiness.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signUpBusiness(v);
                }
            });

        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), requestCode, resultCode, data);
            String fileGuid = UUID.randomUUID().toString();
            if (m_isWorkerImage == true) {
                awDialog.setWorkerImage(bitmap);
                awDialog.setWorkerImage(fileGuid + ".JPEG");
            } else {
                GetBusiness().BusinessImage = fileGuid + ".JPEG";
                setBusinessImage(bitmap);
            }

            SocketService.UploadImage(bitmap, getActivity(), fileGuid);
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    protected void setBusinessImage(Bitmap bitmap) {
        try{
            ImageView srcImageView = m_view.findViewById(R.id.imgBusinessImage);
            if (bitmap != null && srcImageView != null) {
                srcImageView.setImageBitmap(bitmap);
            }
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    public void PickBusinessImage(View view){
        try{
            m_isWorkerImage = false;
            ImagePicker.setMinQuality(8000, 8000);
            ImagePicker.pickImage(getActivity(), "בחר תמונה");
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    public void PickUserImage(View view){
        try{
            m_isWorkerImage = true;
            ImagePicker.setMinQuality(600, 600);
            ImagePicker.pickImage(getActivity(), "בחר תמונה");
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    public void OpenAddWorkerDialog(View v){
        try{
            awDialog = new AddWorker(getActivity(), this);
            awDialog.show();

            Window window = awDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    public void addNewWorkingHours(View v) {
        try{
            awDialog.addNewWorkingHours(v);
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    public void addNewBreakingHours(View v) {
        try{
            awDialog.addNewBreakingHours(v);
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    public void addNewWorkType(View v) {
        try{
            awDialog.addNewWorkType(v);
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    public void RemoveWorkHourRow(View view) {
        try{
            awDialog.RemoveWorkHourRow(view);
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    public void RemoveWorkTypeRow(View view) {
        try{
            awDialog.RemoveWorkTypeRow(view);
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
            List<clsWorkingHours> lst = awDialog.GetWorkingHours();
            if (view.getResources().getResourceEntryName(((View)view.getParent().getParent().getParent().getParent()).getId()).contains("Break") == true) {
                lst = awDialog.GetBreakingHours();
            }
            final List<clsWorkingHours> finalLst = lst;

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), R.style.DialogTheme,new TimePickerDialog.OnTimeSetListener() {
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

            List<clsWorkingHours> lst = awDialog.GetWorkingHours();
            if (view.getResources().getResourceEntryName(((View)view.getParent().getParent().getParent().getParent()).getId()).contains("Break") == true) {
                lst = awDialog.GetBreakingHours();
            }
            final List<clsWorkingHours> finalLst = lst;

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), R.style.DialogTheme,new TimePickerDialog.OnTimeSetListener() {
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

    public void openTimePickerDialog_Duration(View view) {
        try{
            final View selectedView = view;
            final EditText Edit_Time = (EditText)view.findViewById(R.id.txtWorkDuration);
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = 0;
            int minute = 10;

            final List<clsWorkType> lst = awDialog.GetWorkTypes();

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), R.style.DialogTheme,new TimePickerDialog.OnTimeSetListener() {
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

    public void openDayPickerDialog(View view) {
        try {
            final View selectedView = view;
            final EditText Edit_Time = (EditText) view.findViewById(R.id.txtDays);
            final String[] multiChoiceItems = getResources().getStringArray(R.array.dialog_multi_choice_array);
            List<clsWorkingHours> lst = awDialog.GetWorkingHours();
            if (view.getResources().getResourceEntryName(((View) view.getParent().getParent().getParent().getParent()).getId()).contains("Break") == true) {
                lst = awDialog.GetBreakingHours();
            }
            final List<clsWorkingHours> finalLst = lst;

            String selectedDays = Edit_Time.getText().toString();
            final boolean[] checkedItems = {selectedDays.contains("א"), selectedDays.contains("ב"), selectedDays.contains("ג"), selectedDays.contains("ד"), selectedDays.contains("ה"), selectedDays.contains("ו"), selectedDays.contains("ש")};
            new AlertDialog.Builder(getActivity(), R.style.DialogTheme)
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

    public void addNewWorker(View v) {
        try {
            clsWorker newWorker = awDialog.GetNewWorker();
            if (newWorker != null) {
                GetWorkers().add(newWorker);
                GetWorkTypeAdapter().notifyDataSetChanged();

                ListView listView = (ListView) m_view.findViewById(R.id.lstWorkers);
                float scale = getResources().getDisplayMetrics().density;
                int totalHeight = (int) (GetWorkers().size() * 85 * scale);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, totalHeight);
                listView.setLayoutParams(parms);
            }
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    public void signUpBusiness(View view) {
        try {

            Activity activity = getActivity();
            clsBusiness business = GetBusiness();
            String txtBusinessName = ((EditText) m_view.findViewById(R.id.txtBusinessName)).getText().toString();
            String txtBusinessDescription = ((EditText) m_view.findViewById(R.id.txtBusinessDescription)).getText().toString();
            String txtBusinessPhone = ((EditText) m_view.findViewById(R.id.txtBusinessPhone)).getText().toString();
            String txtBusinessCity = ((EditText) m_view.findViewById(R.id.txtBusinessCity)).getText().toString();
            String txtBusinessStreet = ((EditText) m_view.findViewById(R.id.txtBusinessStreet)).getText().toString();

            if (txtBusinessName == null || txtBusinessName.length() < 3) {
                Toasty.warning(activity, "הזן את שם העסק", Toast.LENGTH_SHORT, true).show();
                return;
            }
            if (txtBusinessPhone == null || txtBusinessPhone.length() < 5) {
                Toasty.warning(activity, "הזן את מספר הטלפון שלך", Toast.LENGTH_SHORT, true).show();
                return;
            }
            if (txtBusinessCity == null || txtBusinessCity.length() < 3) {
                Toasty.warning(activity, "הזן את העיר שבה ממוקם העסק", Toast.LENGTH_SHORT, true).show();
                return;
            }
            if (txtBusinessStreet == null || txtBusinessStreet.length() < 3) {
                Toasty.warning(activity, "הזן את הכתובת שבה ממוקם העסק", Toast.LENGTH_SHORT, true).show();
                return;
            }
            if (business.BusinessWorkers == null || business.BusinessWorkers.size() == 0) {
                Toasty.warning(activity, "הזן לפחות עובד אחד שעובד בעסק", Toast.LENGTH_SHORT, true).show();
                return;
            }

            if (business.BusinessType == 0) {
                Toasty.warning(activity, "בחר את סוג העסק", Toast.LENGTH_SHORT, true).show();
                return;
            }

            if (GetBusiness().BusinessWorkers.size() > 50) {
                Toasty.warning(activity, "לא ניתן להזין יותר מ- 50 עובדים", Toast.LENGTH_SHORT, true).show();
                return;
            }

            final Button btnSignUpBusiness = (Button) m_view.findViewById(R.id.btnSignUpBusiness);
            btnSignUpBusiness.setEnabled(false);


            //business.UserOwners = txtBusinessName;
            business.BusinessName = txtBusinessName;
            business.BusinessDescription = txtBusinessDescription;
            business.BusinessPhone = txtBusinessPhone;
            business.BusinessCity = txtBusinessCity;
            business.BusinessStreet = txtBusinessStreet;
            SocketService.SignUpBusiness(business, activity);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSignUpBusiness.setEnabled(true);
                }
            }, 2000);
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    public void selectBusinessType(View view) {
        try {
            AlertDialog.Builder b = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
            b.setTitle("בחר סוג עסק");
            final EditText Edit_Time = (EditText) view.findViewById(R.id.txtBusinessType);
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(),
                    R.layout.combo_box_item, new clsBusinessType[]{
                    new clsBusinessType(1, "עיצוב שיער"),
                    new clsBusinessType(2, "קוסמטיקה"),
                    new clsBusinessType(3, "מוסך")
            });

            b.setAdapter(spinnerArrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clsBusinessType BusinessType = (clsBusinessType) spinnerArrayAdapter.getItem(which);
                    Edit_Time.setText(BusinessType.BusinessType);
                    GetBusiness().BusinessType = BusinessType.Id;
                    dialog.dismiss();

                }
            });

            b.show();
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }
}
