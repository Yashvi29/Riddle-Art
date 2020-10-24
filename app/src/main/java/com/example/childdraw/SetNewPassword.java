package com.example.childdraw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SetNewPassword extends AppCompatActivity {

    private TextInputLayout newPassword, _newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        newPassword = findViewById(R.id.set_password);
        _newPassword = findViewById(R.id.confirm_password);

    }

    public void setNewPassword(View view){
        CheckInternet checkInternet = new CheckInternet();
        if(!checkInternet.isConnected(this)){
            showCustomDialog();
            return;
        }
        if(!validatePassword() || !validateConfirmPassword() || !ValidateBoth()){
            return;
        }
//        progressBar.setVisibility(View.VISIBLE);
        String _newPassword = newPassword.getEditText().getText().toString().trim();
        String _phoneNumber = getIntent().getStringExtra("phoneNo");
        String user_name = getIntent().getStringExtra("hint_Username");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(user_name).child("password").setValue(_newPassword);
        startActivity(new Intent(getApplicationContext(), ForgetPasswordSuccessMessage.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();

    }

    private boolean validateConfirmPassword() {
        String val = newPassword.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            newPassword.setError("Field cannot be empty");
            newPassword.requestFocus();
            return false;
        } else {
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = _newPassword.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            _newPassword.setError("Field cannot be empty");
            _newPassword.requestFocus();
            return false;
        } else {
            _newPassword.setError(null);
            _newPassword.setErrorEnabled(false);
            return true;
        }
    }

    public boolean ValidateBoth(){
        String val = _newPassword.getEditText().getText().toString().trim();
        String valc = newPassword.getEditText().getText().toString().trim();
        if(!val.equals(valc)){
            Toast.makeText(this, "Passwords are not Same!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder((SetNewPassword.this));
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
}