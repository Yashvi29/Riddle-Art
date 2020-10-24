package com.example.childdraw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout1;
    NavigationView navigationView1;
    ImageView menuIcon1;
    ScrollView contentView1;
    static final float END_SCALE = 0.7f;

    TextInputLayout fullName, email, phoneNo, password;
    TextView fullNamelabel, usernameLabel;
    DatabaseReference reference;
    String user_username, user_name, user_email, user_phoneNo, user_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        drawerLayout1 = findViewById(R.id.drawer_layout1);
        navigationView1 = findViewById(R.id.navigation_view1);
        menuIcon1 = findViewById(R.id.menu_icon1);
        contentView1 = findViewById(R.id.scroll_view);

        navigationView1.setCheckedItem(R.id.nav_home);

        navigationDrawer();

        reference = FirebaseDatabase.getInstance().getReference("users");

        fullName = findViewById(R.id.full_name_field);
        email = findViewById(R.id.email_profile);
        phoneNo = findViewById(R.id.phone_no_profile);
        password = findViewById(R.id.password_profile);
        fullNamelabel = findViewById(R.id.fullname_field);
        usernameLabel = findViewById(R.id.username_field);

        showAllUserData();

        findViewById(R.id.signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserProfile.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
                startActivity(intent);
                finish();
            }
        });

    }
    public void showAllUserData(){
        user_username = Login.getUsernameFromDB();
        user_name = Login.getNameFromDB();
        user_email = Login.getEmailFromDB();
        user_phoneNo = Login.getPhoneNoFromDB();
        user_password = Login.getPasswordFromDB();

        fullNamelabel.setText(user_name);
        usernameLabel.setText(user_username);
        fullName.getEditText().setText(user_name);
        email.getEditText().setText(user_email);
        phoneNo.getEditText().setText(user_phoneNo);
        password.getEditText().setText(user_password);

    }

    public void Update(View view){
        if(isNameChanged() || isPasswordChanged()){
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Data is same and can not be updated", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPasswordChanged(){
        if(!user_password.equals(password.getEditText().getText().toString()))
        {
            reference.child(user_username).child("password").setValue(password.getEditText().getText().toString());
            user_password=password.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }
    }
    private boolean isNameChanged(){
        if(!user_name.equals(fullName.getEditText().getText().toString())){
            reference.child(user_username).child("name").setValue(fullName.getEditText().getText().toString());
            user_name=fullName.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }

    }

    private void navigationDrawer() {
        navigationView1.bringToFront();
        navigationView1.setNavigationItemSelectedListener(this);
        navigationView1.setCheckedItem(R.id.nav_home);

        menuIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout1.isDrawerVisible(GravityCompat.START)){
                    drawerLayout1.closeDrawer(GravityCompat.START);
                }else{
                    drawerLayout1.openDrawer(GravityCompat.START);
                }
            }
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout1.setScrimColor(getResources().getColor(R.color.colorPrimary));
        drawerLayout1.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView1.setScaleX(offsetScale);
                contentView1.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView1.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView1.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout1.isDrawerVisible(GravityCompat.START)){
            drawerLayout1.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                Intent intentt = new Intent(getApplicationContext(), ViewItems.class);
                startActivity(intentt);
                break;
            case R.id.nav_start:
                Intent intent = new Intent(getApplicationContext(), PaintStart.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                Intent intent1 = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent1);
                break;
            case R.id.nav_signout:
                FirebaseAuth.getInstance().signOut();
                Intent it = new Intent(getApplicationContext(), Login.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
                startActivity(it);
                finish();
                break;
            case R.id.nav_saved:
                Intent intent3 = new Intent(getApplicationContext(), Saved.class);
                startActivity(intent3);
                break;
        }
        drawerLayout1.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

}