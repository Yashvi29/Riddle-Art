package com.example.childdraw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.childdraw.Common.Common;
import com.example.childdraw.Interface.ToolsListener;
import com.example.childdraw.adabters.ToolsAdabters;
import com.example.childdraw.model.ToolsItem;
import com.example.childdraw.widget.PaintView;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaintStart extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ToolsListener {

    private static final int REQUEST_PERMISSION = 1001;
    private static final int PICK_IMAGE = 1000;
    PaintView mPaintView;
    int colorBackground, colorBrush;
    int brushSize, eraserSize;

    DrawerLayout drawerLayout2;
    NavigationView navigationView2;
    ImageView menuIcon2;
    LinearLayout contentView2;

    static final float END_SCALE = 0.7f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_start);

        drawerLayout2 = findViewById(R.id.drawer_layout2);
        navigationView2 = findViewById(R.id.navigation_view2);
        menuIcon2 = findViewById(R.id.menu_icon2);
        contentView2 = findViewById(R.id.content2);

        navigationView2.setCheckedItem(R.id.nav_home);

        navigationDrawer();

        initTools();

    }

    private void initTools() {

        colorBackground = Color.WHITE;
        colorBrush = Color.BLACK;
        eraserSize = brushSize = 12;
        mPaintView = findViewById(R.id.paint_view);

//        mPaintView = findViewById(R.id.paint_view);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_tools);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        ToolsAdabters toolsAdabters = new ToolsAdabters(loadTools(), this);
        recyclerView.setAdapter(toolsAdabters);
    }

    private List<ToolsItem> loadTools() {
        List<ToolsItem> result = new ArrayList<>();
        result.add(new ToolsItem(R.drawable.ic_baseline_brush_24, Common.BRUSH));
        result.add(new ToolsItem(R.drawable.eraser_black, Common.ERASOR));
        result.add(new ToolsItem(R.drawable.ic_baseline_image_24, Common.IMAGE));
        result.add(new ToolsItem(R.drawable.ic_baseline_palette_24, Common.COLORS));
        result.add(new ToolsItem(R.drawable.paint_background, Common.BACKGROUND));
        result.add(new ToolsItem(R.drawable.ic_baseline_undo_24, Common.RETURN));
        return result;

    }

    private void navigationDrawer() {
        navigationView2.bringToFront();
        navigationView2.setNavigationItemSelectedListener(this);
        navigationView2.setCheckedItem(R.id.nav_home);

        menuIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout2.isDrawerVisible(GravityCompat.START)){
                    drawerLayout2.closeDrawer(GravityCompat.START);
                }else{
                    drawerLayout2.openDrawer(GravityCompat.START);
                }
            }
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout2.setScrimColor(getResources().getColor(R.color.colorPrimary));
        drawerLayout2.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView2.setScaleX(offsetScale);
                contentView2.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView2.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView2.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout2.isDrawerVisible(GravityCompat.START)){
            drawerLayout2.closeDrawer(GravityCompat.START);
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
        drawerLayout2.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    public void finishpaint(View view) {
        finish();
    }

//    public void shareApp(View view) {
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        String bodyText = "http://play.google.com/store/apps/details?id+"+getPackageName();
//        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
//        intent.putExtra(Intent.EXTRA_TEXT, bodyText);
//        startActivity(Intent.createChooser(intent, "share this app"));
//    }

    public void showFiles(View view) {
        startActivity(new Intent(this, Saved.class));
    }

    public void saveFile(View view) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
        PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }else{
            saveBitmap();
        }

    }

    private void saveBitmap() {
        Bitmap bitmap = mPaintView.getBitmap();
        String file_name = UUID.randomUUID()+".png";

        File folder = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ File.separator+getString(R.string.app_name));
        if(!folder.exists()){
            folder.mkdirs();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(folder+File.separator+file_name);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            Toast.makeText(this, "picture saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            saveBitmap();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSelected(String name) {
        switch (name){
            case Common.BRUSH:
                mPaintView.desableEreaser();
                showDialogSize(false);
                break;
            case Common.ERASOR:
                mPaintView.enableEreaser();
                showDialogSize(true);
                break;
            case Common.RETURN:
                mPaintView.returnLastAction();
                break;
            case Common.BACKGROUND:
                updateColor(name);
                break;
            case Common.COLORS:
                updateColor(name);
                break;
            case Common.IMAGE:
                getImage();
                break;

        }
    }

    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && data!=null && resultCode==RESULT_OK){
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bitmap  = BitmapFactory.decodeFile(imagePath, options);
            mPaintView.setImage(bitmap);
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateColor(final String name) {
        final int color;
        if(name.equals(Common.BACKGROUND)){
            color = colorBackground;
        }else{
            color = colorBrush;
        }
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(color)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton("OK", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors) {
                        if(name.equals(Common.BACKGROUND)){
                            colorBackground = lastSelectedColor;
                            mPaintView.setColorBackground(colorBackground);
                        }else{
                            colorBrush = lastSelectedColor;
                            mPaintView.setBrushColor(colorBrush);
                        }
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).build()
                .show();
    }

    private void showDialogSize(final boolean isEraser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null, false);
        TextView toolsSelected = view.findViewById(R.id.status_tools_selected);
        final TextView statusSize = view.findViewById(R.id.status_size);
        ImageView ivTools = view.findViewById(R.id.iv_tools);
        SeekBar seekBar = view.findViewById(R.id.seekbar_size);
        seekBar.setMax(99);
        if(isEraser){
            toolsSelected.setText("Eraser Size");
            ivTools.setImageResource(R.drawable.eraser_black);
            statusSize.setText("Selected Size : "+eraserSize);
        }else{
            toolsSelected.setText("Brush Size");
            ivTools.setImageResource(R.drawable.ic_baseline_brush_24);
            statusSize.setText("Selected Size : "+brushSize);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(isEraser){
                    eraserSize = i+1;
                    statusSize.setText("Selected Size : "+eraserSize);
                    mPaintView.setSizeErasor(eraserSize);
                }else{
                    brushSize = i+1;
                    statusSize.setText("Selected Size : "+brushSize);
                    mPaintView.setSizebrush(brushSize);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setView(view);
        builder.show();
    }

}