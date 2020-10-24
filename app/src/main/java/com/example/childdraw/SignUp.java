package com.example.childdraw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;


public class SignUp extends AppCompatActivity {

    TextInputLayout regname, regUser, regEmail, regPhoneNo, regPassword;
    Button regBtn;
    TextView regToLoginBtn;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private CountryCodePicker countryCodePicker1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        regname = findViewById(R.id.reg_name);
        regUser = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regPhoneNo = findViewById(R.id.reg_phoneNo);
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_Btn);
        regToLoginBtn = findViewById(R.id.reg_login_Btn);
        countryCodePicker1 = findViewById(R.id.country_code_picker1);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                registerUser(view);
            }
        });

        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it  = new Intent(SignUp.this, Login.class);
                startActivity(it);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });
    }

    private Boolean validateName(){
        String val = regname.getEditText().getText().toString();
        if(val.isEmpty()){
            regname.setError("Field cannot be empty");
            return false;
        }else{
            regname.setError(null);
            regname.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = regUser.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            regUser.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            regUser.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            regUser.setError("White Spaces are not allowed");
            return false;
        } else {
            regUser.setError(null);
            regUser.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = regPhoneNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            regPhoneNo.setError("Field cannot be empty");
            return false;
        } else {
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            regPassword.setError("Password is too weak");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    public void registerUser(View view) {
        if(!validateName() |!validatePassword() | !validatePhoneNo() | !validateEmail() | !validateUsername())
        {
            return;
        }
        final String _completePhoneNumber = '+'+countryCodePicker1.getFullNumber()+regPhoneNo.getEditText().getText().toString();;
        String name = regname.getEditText().getText().toString();
        String username = regUser.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String phoneNo = _completePhoneNumber;
        String password = regPassword.getEditText().getText().toString();
        UserHelperclass helperClass = new UserHelperclass(name, username, email, phoneNo, password);
        reference.child(username).setValue(helperClass);
        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}