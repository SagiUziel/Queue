package com.project.sagi.queue.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.sagi.queue.R;
import com.project.sagi.queue.SocketService;
import com.project.sagi.queue.clsGlobalHelper;
import com.project.sagi.queue.clsUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class SignUp extends Fragment {

    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static Pattern pattern;
    private Matcher matcher;

    public SignUp() {
        try{
            pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
            SocketService.InitListeners();
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstaceState){
        try{
            View view = inflater.inflate(R.layout.activity_sign_up, container, false);
            Button bt1=(Button)view.findViewById(R.id.btn_signup);
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signUp(v);
                }
            });
            return view;
        } catch (Exception ex) {
            SocketService.WriteError(ex);
            return null;
        }
    }

    public boolean validateEmail(String email) {
        try{
            matcher = pattern.matcher(email);
            return matcher.matches();
        } catch (Exception ex) {
            SocketService.WriteError(ex);
            return false;
        }
    }

    public void signUp(View v){
        try{
            Activity activity = getActivity();
            clsGlobalHelper.closeKeyboard(activity.getCurrentFocus(), activity);
            String txtUserName = ((EditText)activity.findViewById(R.id.txtUserName)).getText().toString();
            String txtUserMail = ((EditText)activity.findViewById(R.id.txtUserMail)).getText().toString();
            String txtUserPassword = ((EditText)activity.findViewById(R.id.txtUserPassword)).getText().toString();
            String txtUserPasswordConfirm = ((EditText)activity.findViewById(R.id.txtUserPasswordConfirm)).getText().toString();
            if (txtUserName == null || txtUserName.length() < 3 || txtUserName.contains(" ") == false) {
                Toasty.warning(activity, "הכנס את שמך המלא", Toast.LENGTH_SHORT, true).show();
                return;
            }
            if (txtUserMail == null || validateEmail(txtUserMail) == false) {
                Toasty.warning(activity, "הכנס כתובת דו\"אל תקינה", Toast.LENGTH_SHORT, true).show();
                return;
            }
            if (txtUserPassword == null || txtUserPassword.length() < 6) {
                Toasty.warning(activity, "הכנס את סיסמה באורך 6 תווים לפחות", Toast.LENGTH_SHORT, true).show();
                return;
            }
            if (txtUserPassword.equals(txtUserPasswordConfirm) == false) {
                Toasty.warning(activity, "הכנס סיסמאות זהות", Toast.LENGTH_SHORT, true).show();
                return;
            }
            final Button btn_signup=(Button)activity.findViewById(R.id.btn_signup);
            btn_signup.setEnabled(false);

            clsUser user = new clsUser();
            user.UserName = txtUserName;
            user.UserMail = txtUserMail;
            user.UserPassword = txtUserPassword;
            SocketService.SignUp(user, activity);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn_signup.setEnabled(true);
                }
            }, 2000);
        } catch (Exception ex) {
            SocketService.WriteError(ex);
        }
    }
}
