package com.example.childdraw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button login_btn;
    ImageView image;
    TextView logoText, sloganText;
    TextInputLayout username, password;
    TextView forgetPass, callSignUp;
    static String nameFromDB, emailFromDB, usernameFromDB, phoneNoFromDB, passwordFromDB;

    public static String getNameFromDB() {
        return nameFromDB;
    }

    public static String getEmailFromDB() {
        return emailFromDB;
    }

    public static String getUsernameFromDB() {
        return usernameFromDB;
    }

    public static String getPhoneNoFromDB() {
        return phoneNoFromDB;
    }

    public static String getPasswordFromDB() {
        return passwordFromDB;
    }
    //    RelativeLayout progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        callSignUp = findViewById(R.id.signup_screen);
        image = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        sloganText = findViewById(R.id.slogan_name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        forgetPass = findViewById(R.id.forget_password);
//        progressBar = findViewById(R.id.progress_bar1);

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(logoText, "logo_text");
                pairs[2] = new Pair<View, String>(sloganText, "logo_desc");
                pairs[3] = new Pair<View, String>(username, "username_tran");
                pairs[4] = new Pair<View, String>(password, "password_tran");
                pairs[5] = new Pair<View, String>(login_btn, "button_tran");
                pairs[6] = new Pair<View, String>(callSignUp, "login_signup_tran");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        });

    }

    private Boolean validateUsername() {
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    public void loginuser(View view){
        if(!validateUsername() | !validatePassword()){
            return;
        }else{
            isUser();
        }
    }

    private void isUser(){

//        progressBar.setVisibility(View.VISIBLE);

        final String userEnteredusername = username.getEditText().getText().toString();
        final String userEnteredpassword = password.getEditText().getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(userEnteredusername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    username.setError(null);
                    username.setErrorEnabled(false);
                    passwordFromDB = dataSnapshot.child(userEnteredusername).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userEnteredpassword)){
                        username.setError(null);
                        username.setErrorEnabled(false);
                        nameFromDB = dataSnapshot.child(userEnteredusername).child("name").getValue(String.class);
                        emailFromDB = dataSnapshot.child(userEnteredusername).child("emil").getValue(String.class);
                        usernameFromDB = dataSnapshot.child(userEnteredusername).child("username").getValue(String.class);
                        phoneNoFromDB = dataSnapshot.child(userEnteredusername).child("phoneNo").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), ViewItems.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("phoneNo", phoneNoFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("password", passwordFromDB);

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }else{
//                        progressBar.setVisibility(View.GONE);
                        password.setError("Wrong Password!!");
                        password.requestFocus();
                    }
                }else{
//                    progressBar.setVisibility(View.GONE);
                    username.setError("No such User exist!!");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void callForgetPassword(View view){
        startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
    }

}