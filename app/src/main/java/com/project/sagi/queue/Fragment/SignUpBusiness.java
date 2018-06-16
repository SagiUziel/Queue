package com.project.sagi.queue.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
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
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;
import com.project.sagi.queue.AdapterWorker;
import com.project.sagi.queue.AddWorker;
import com.project.sagi.queue.R;
import com.project.sagi.queue.SocketService;
import com.project.sagi.queue.clsBusiness;
import com.project.sagi.queue.clsBusinessType;
import com.project.sagi.queue.clsWorker;

import java.util.ArrayList;
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
            ListView listView = m_view.findViewById(R.id.lstWorkers);
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
        View view = inflater.inflate(R.layout.fragment_manager_settings, container, false);
        try{
            view.setId(R.id.SignUpBusiness);
            m_view = view;
            AutoCompleteTextView txtBusinessCity = view.findViewById(R.id.txtBusinessCity);
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

            AutoCompleteTextView txtBusinessStreet = view.findViewById(R.id.txtBusinessStreet);
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

            ImageView imgBusinessImage= view.findViewById(R.id.imgBusinessImage);
            imgBusinessImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickBusinessImage();
                }
            });

            EditText txtBusinessType= view.findViewById(R.id.txtBusinessType);
            txtBusinessType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectBusinessType(v);
                }
            });

            Button btn_addWorker= view.findViewById(R.id.btn_addWorker);
            btn_addWorker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenAddWorkerDialog();
                }
            });

            Button btnSignUpBusiness= view.findViewById(R.id.btnSignUpBusiness);
            btnSignUpBusiness.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signUpBusiness();
                }
            });

        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
        return view;
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

    public void PickBusinessImage(){
        try{
            m_isWorkerImage = false;
            ImagePicker.setMinQuality(8000, 8000);
            ImagePicker.pickImage(getActivity(), "בחר תמונה");
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    public void OpenAddWorkerDialog(){
        try{
            awDialog = new AddWorker(getActivity(), this);
            awDialog.show();
            m_isWorkerImage = true;
            Window window = awDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    public void addNewWorker() {
        try {
            m_isWorkerImage = false;
            clsWorker newWorker = awDialog.GetNewWorker();
            if (newWorker != null) {
                GetWorkers().add(newWorker);
                GetWorkTypeAdapter().notifyDataSetChanged();

                ListView listView = m_view.findViewById(R.id.lstWorkers);
                float scale = getResources().getDisplayMetrics().density;
                int totalHeight = (int) (GetWorkers().size() * 85 * scale);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, totalHeight);
                listView.setLayoutParams(parms);
            }
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    public void signUpBusiness() {
        try {

            Activity activity = getActivity();
            clsBusiness business = GetBusiness();
            String txtBusinessName = ((EditText) m_view.findViewById(R.id.txtBusinessName)).getText().toString();
            String txtBusinessDescription = ((EditText) m_view.findViewById(R.id.txtBusinessDescription)).getText().toString();
            String txtBusinessPhone = ((EditText) m_view.findViewById(R.id.txtBusinessPhone)).getText().toString();
            String txtBusinessCity = ((EditText) m_view.findViewById(R.id.txtBusinessCity)).getText().toString();
            String txtBusinessStreet = ((EditText) m_view.findViewById(R.id.txtBusinessStreet)).getText().toString();

            SocketService.SignUpBusiness(business, activity);

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

            final Button btnSignUpBusiness = m_view.findViewById(R.id.btnSignUpBusiness);
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
            final EditText Edit_Time = view.findViewById(R.id.txtBusinessType);
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

    public void setImage(int requestCode, int resultCode, Intent data) {
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
}
