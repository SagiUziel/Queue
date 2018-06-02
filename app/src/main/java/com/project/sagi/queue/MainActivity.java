package com.project.sagi.queue;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static Pattern pattern;
    private Matcher matcher;

    public MainActivity() {
        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        SocketService.InitListeners();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clsGlobalHelper.setLocalLanguage(getBaseContext());

        //test
        Intent myIntent = new Intent(this, ManagerSettings.class);
        this.startActivity(myIntent);
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void signUp(View v){
        clsGlobalHelper.closeKeyboard(getCurrentFocus(), this);
        String txtUserName = ((EditText)findViewById(R.id.txtUserName)).getText().toString();
        String txtUserMail = ((EditText)findViewById(R.id.txtUserMail)).getText().toString();
        String txtUserPassword = ((EditText)findViewById(R.id.txtUserPassword)).getText().toString();
        String txtUserPasswordConfirm = ((EditText)findViewById(R.id.txtUserPasswordConfirm)).getText().toString();
        if (txtUserName == null || txtUserName.length() < 3 || txtUserName.contains(" ") == false) {
            Toasty.warning(this, "הכנס את שמך המלא", Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (txtUserMail == null || validateEmail(txtUserMail) == false) {
            Toasty.warning(this, "הכנס כתובת דו\"אל תקינה", Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (txtUserPassword == null || txtUserPassword.length() < 6) {
            Toasty.warning(this, "הכנס את סיסמה באורך 6 תווים לפחות", Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (txtUserPassword.equals(txtUserPasswordConfirm) == false) {
            Toasty.warning(this, "הכנס סיסמאות זהות", Toast.LENGTH_SHORT, true).show();
            return;
        }
        final Button btn_signup=(Button)findViewById(R.id.btn_signup);
        btn_signup.setEnabled(false);

        clsUser user = new clsUser();
        user.UserName = txtUserName;
        user.UserMail = txtUserMail;
        user.UserPassword = txtUserPassword;
        SocketService.SignUp(user, this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_signup.setEnabled(true);
            }
        }, 2000);
    }
}
