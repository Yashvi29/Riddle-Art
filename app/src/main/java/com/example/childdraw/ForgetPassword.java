package com.example.childdraw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgetPassword extends AppCompatActivity {

    private ImageView screenIcon;
    private TextView title, description;
    private TextInputLayout phoneNumberTextField, hintUsername;
    private CountryCodePicker countryCodePicker;
    private Button nextBtn;
//    private Animation animation;
//    RelativeLayout progessBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);

        screenIcon = findViewById(R.id.forget_password_icon);
        title = findViewById(R.id.forget_password_title);
        description = findViewById(R.id.forget_password_description);
        phoneNumberTextField = findViewById(R.id.forget_password_phone_number);
        countryCodePicker = findViewById(R.id.country_code_picker);
        nextBtn = findViewById(R.id.forget_password_next_btn);
        hintUsername = findViewById(R.id.hint_username);
//        progessBar = findViewById(R.id.progress_bar);


    }

    public void verifyPhoneNumber(View view){

        CheckInternet checkInternet = new CheckInternet();
        if(!checkInternet.isConnected(this)){
            showCustomDialog();
            return;
        }

        if(!ValidateUsers() || !validateFields()){
            return;
        }
//        progessBar.setVisibility(View.VISIBLE);
        final String _hintUsername = hintUsername.getEditText().getText().toString().trim();
        String _phoneNumber = phoneNumberTextField.getEditText().getText().toString().trim();
        if(_phoneNumber.charAt(0)=='0'){
//            progessBar.setVisibility(View.GONE);
//            phoneNumberTextField.setError("No such user Exist");
            phoneNumberTextField.requestFocus();
        }
        final String _completePhoneNumber = '+'+countryCodePicker.getFullNumber()+_phoneNumber;

        Query checkuser = FirebaseDatabase.getInstance().getReference("users").orderByChild("phoneNo").equalTo(_completePhoneNumber);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    phoneNumberTextField.setError(null);
                    phoneNumberTextField.setErrorEnabled(false);

                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                    intent.putExtra("phoneNo", _completePhoneNumber);
                    intent.putExtra("whatTODo", "updateData");
                    intent.putExtra("hintUsername", _hintUsername);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();

//                    progessBar.setVisibility(View.GONE);
                }else{
//                    progessBar.setVisibility(View.GONE);
                    phoneNumberTextField.setError("No such user Exist");
                    phoneNumberTextField.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private boolean validateFields() {
        String val = phoneNumberTextField.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            phoneNumberTextField.setError("Field cannot be empty");
            phoneNumberTextField.requestFocus();
            return false;
        } else {
            phoneNumberTextField.setError(null);
            phoneNumberTextField.setErrorEnabled(false);
            return true;
        }
    }

    private boolean ValidateUsers(){
        String val = hintUsername.getEditText().getText().toString();
        if (val.isEmpty()) {
            hintUsername.setError("Field cannot be empty");
            return false;
        } else {
            hintUsername.setError(null);
            hintUsername.setErrorEnabled(false);
            return true;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder((ForgetPassword.this));
        builder.setMessage("Please connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
                        finish();
                    }
                });
    }

    public void callBackScreenFromForgetPassword(View view){
        startActivity(new Intent(getApplicationContext(), Login.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }


}