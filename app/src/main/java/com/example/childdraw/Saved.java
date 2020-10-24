package com.example.childdraw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.childdraw.Common.Common;
import com.example.childdraw.adabters.FilesAdabters;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Saved extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout4;
    NavigationView navigationView4;
    ImageView menuIcon4;
    RelativeLayout contentView4;

    List<File> fileList;

    static final float END_SCALE = 0.7f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        drawerLayout4 = findViewById(R.id.drawer_layout4);
        navigationView4 = findViewById(R.id.navigation_view4);
        menuIcon4 = findViewById(R.id.menu_icon4);
        contentView4 = findViewById(R.id.content4);

//        initToolbar();

        initViews();

        navigationView4.setCheckedItem(R.id.nav_home);

        navigationDrawer();

    }

    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_files);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        fileList = loadFile();
        FilesAdabters filesAdabters = new FilesAdabters(this, fileList);
        recyclerView.setAdapter(filesAdabters);
    }

    private List<File> loadFile() {
        ArrayList<File> inFiles = new ArrayList<>();
        File parendDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+getString(R.string.app_name));
        File[] files = parendDir.listFiles();
        for(File file : files){
            if(file.getName().endsWith(".png"))
                inFiles.add(file);
        }
        TextView textView = findViewById(R.id.status_empty);
        if(files.length>0){
            textView.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.VISIBLE);
        }
        return inFiles;
    }

//    private void initToolbar() {
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Pictures");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.DELETE)){
            deleteFile(item.getOrder());
            initViews();
        }
        return true;
    }

    private void deleteFile(int order) {
        fileList.get(order).delete();
        fileList.remove(order);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home)
            finish();
        return true;
    }

    private void navigationDrawer() {
        navigationView4.bringToFront();
        navigationView4.setNavigationItemSelectedListener(this);
        navigationView4.setCheckedItem(R.id.nav_home);

        menuIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout4.isDrawerVisible(GravityCompat.START)){
                    drawerLayout4.closeDrawer(GravityCompat.START);
                }else{
                    drawerLayout4.openDrawer(GravityCompat.START);
                }
            }
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout4.setScrimColor(getResources().getColor(R.color.colorPrimary));
        drawerLayout4.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView4.setScaleX(offsetScale);
                contentView4.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView4.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView4.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout4.isDrawerVisible(GravityCompat.START)){
            drawerLayout4.closeDrawer(GravityCompat.START);
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
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
        drawerLayout4.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

}