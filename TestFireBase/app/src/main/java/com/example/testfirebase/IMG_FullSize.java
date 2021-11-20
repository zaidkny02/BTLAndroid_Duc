package com.example.testfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.artjimlop.altex.AltexImageDownloader;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class IMG_FullSize extends AppCompatActivity implements View.OnClickListener {
    ImageView img;
    ArrayList<Uri> imageUri = new ArrayList<Uri>();
    int pos;
    ProgressDialog mProgressDialog;
    Button fullsize_pre_btn,fullsize_next_btn,download_btn,fullsize_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img__full_size);
        getView();
        getSource();
        if(imageUri.size() == 1){
            fullsize_next_btn.setVisibility(View.INVISIBLE);
            fullsize_next_btn.setEnabled(false);
            fullsize_pre_btn.setVisibility(View.INVISIBLE);
            fullsize_pre_btn.setEnabled(false);
        }
        if(pos == imageUri.size() - 1) {

            fullsize_next_btn.setEnabled(false);
            fullsize_next_btn.setVisibility(View.INVISIBLE);
        }
        if(pos == 0) {
            fullsize_pre_btn.setEnabled(false);
            fullsize_pre_btn.setVisibility(View.INVISIBLE);
        }
        Glide.with(this).load(imageUri.get(pos)).into(img);
       // Display display = getWindowManager().getDefaultDisplay();
       // int displayWidth = getWindowManager().getDefaultDisplay().getHeight();
       // img.getLayoutParams().height = displayWidth / 2;
       // img.getLayoutParams().width = display.getWidth();
      //  img.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));


      //  img.setScaleType(ImageView.ScaleType.FIT_XY);
      //  img.setY(display.getHeight()/2);

    }
    private void getView(){
        img = findViewById(R.id.fullsize_img);
        fullsize_next_btn = findViewById(R.id.fullsize_next_btn);
        fullsize_next_btn.setOnClickListener(this);
        fullsize_pre_btn = findViewById(R.id.fullsize_pre_btn);
        fullsize_pre_btn.setOnClickListener(this);
        download_btn = findViewById(R.id.download_btn);
        download_btn.setOnClickListener(this);
        fullsize_back = findViewById(R.id.fullsize_back);
        fullsize_back.setOnClickListener(this);
    }
    private void getSource(){
        Intent intent = getIntent();
        pos =  intent.getIntExtra("position",0);
        imageUri = intent.getParcelableArrayListExtra("imageuri");
      //  Toast.makeText(IMG_FullSize.this,String.valueOf(pos),Toast.LENGTH_SHORT).show();
        if(imageUri == null)
        {
            Intent allpostintent = new Intent(IMG_FullSize.this, AllPost.class);
            //intent.putExtra("user_position",user_position);
            startActivity(allpostintent);
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //AltexImageDownloader.writeToDisk(this, imageUri.get(pos).toString(), "Downloads") ;
                //Toast.makeText(IMG_FullSize.this,"Download thành công",Toast.LENGTH_SHORT).show();
                new MyAsyncTask().execute();
            }
            else{
                //Toast.makeText(IMG_FullSize.this,"Không nhận được quyền",Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void  Checkpermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        else{
           // AltexImageDownloader.writeToDisk(this, imageUri.get(pos).toString(), "Downloads");
           // Toast.makeText(IMG_FullSize.this,"Download thành công",Toast.LENGTH_SHORT).show();
            new MyAsyncTask().execute();
        }
        //Toast.makeText(IMG_FullSize.this,"ABCD",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fullsize_pre_btn:
                if (pos > 0) {
                    // decrease the position by 1
                    pos--;
                    img.setImageURI(imageUri.get(pos));
                    Glide.with(this).load(imageUri.get(pos)).into(img);
                    fullsize_next_btn.setEnabled(true);
                    fullsize_next_btn.setVisibility(View.VISIBLE);
                    if(pos == 0) {
                        fullsize_pre_btn.setEnabled(false);
                        fullsize_pre_btn.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case R.id.fullsize_next_btn:
                if (pos < imageUri.size() - 1) {
                    // increase the position by 1
                    pos++;
                    Glide.with(this).load(imageUri.get(pos)).into(img);
                    fullsize_pre_btn.setEnabled(true);
                    fullsize_pre_btn.setVisibility(View.VISIBLE);
                    if(pos == imageUri.size() - 1) {

                        fullsize_next_btn.setEnabled(false);
                        fullsize_next_btn.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case R.id.download_btn:
                Checkpermission();
                break;
            case R.id.fullsize_back:
                finish();
            default:
                break;
        }
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(IMG_FullSize.this);
            mProgressDialog.setTitle("Download Image");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            AltexImageDownloader.writeToDisk(IMG_FullSize.this, imageUri.get(pos).toString(), "Downloads");
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            Toast.makeText(IMG_FullSize.this,"Download thành công",Toast.LENGTH_SHORT).show();
        }
    }

}
