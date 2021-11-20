package com.example.testfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    private Button upload_btn,add_pre,add_next,add_btn_back;
    private ImageView img;
    private EditText txt_title;
    ProgressDialog mProgressDialog;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Posts");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference userroot = FirebaseDatabase.getInstance().getReference("Users");

    ArrayList<Uri> firebaseUri = new ArrayList<Uri>();
   // private Uri imageUri;
   ArrayList<Uri> imageUri = new ArrayList<Uri>();
    private ArrayList<User> UserArr = new ArrayList<User>();
    int position = 0;
    String Is_User_Login,User_Name,User_ID,title;
    // variable for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    SharedPreferences sharedpreferences;
    // key for storing username
    public static final String USER_KEY = "user_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getView();
        userroot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getValue() != null) {
                        User user = dataSnapshot.getValue(User.class);
                        UserArr.add(user);
                    }
                    else
                    {
                        // Toast.makeText(AllPost.this,"Đọc xong",Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Main2Activity.this,"Lỗi đọc dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getView(){
        txt_title = findViewById(R.id.editText2);
        img = findViewById(R.id.imageView);
        img.setOnClickListener(this);
        upload_btn = findViewById(R.id.btn_upload);
        upload_btn.setOnClickListener(this);
        add_pre = findViewById(R.id.add_pre);
        add_pre.setOnClickListener(this);
        add_next = findViewById(R.id.add_next);
        add_next.setOnClickListener(this);
        add_pre.setEnabled(false);
        add_next.setEnabled(false);
        add_pre.setVisibility(View.INVISIBLE);
        add_next.setVisibility(View.INVISIBLE);
        add_btn_back = findViewById(R.id.add_btn_back);
        add_btn_back.setOnClickListener(this);

    }
    private boolean checkUser() {
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        // set to null if not present.
        Is_User_Login = sharedpreferences.getString(USER_KEY ,null);
        if(Is_User_Login != null){
            for(int i=0;i<UserArr.size();i++){
                if(Is_User_Login.equalsIgnoreCase(UserArr.get(i).getUsername())){
                    User_Name = UserArr.get(i).getName();
                    User_ID = UserArr.get(i).getId();
                    return true;
                }
            }
            return false;

        }
        else {
            return false;
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_upload:
                if(txt_title.getText().toString().trim().length()  != 0) {
                    if (imageUri.size() > 0 && checkUser()) {
                        uploadToFirebase(imageUri);
                    } else {
                        Toast.makeText(Main2Activity.this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(Main2Activity.this, "Chưa nhập tiêu đề", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageView:
                // khởi tạo lại arr
                imageUri.clear();
                firebaseUri.clear();
                img.setImageResource(R.drawable.ic_add_a_photo_black_24dp);
                Intent galleryIntent = new Intent();

                galleryIntent.setType("image/*");
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"Select Picture"),2);
                break;
            case R.id.add_pre:
                if (position > 0) {
                    // decrease the position by 1
                    position--;
                    img.setImageURI(imageUri.get(position));
                    add_next.setEnabled(true);
                    add_next.setVisibility(View.VISIBLE);
                    if(position == 0) {
                        add_pre.setEnabled(false);
                        add_pre.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case R.id.add_next:
                if (position < imageUri.size() - 1) {
                    // increase the position by 1
                    position++;

                    img.setImageURI(imageUri.get(position));
                    add_pre.setEnabled(true);
                    add_pre.setVisibility(View.VISIBLE);
                    if(position == imageUri.size() - 1) {

                        add_next.setEnabled(false);
                        add_next.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case R.id.add_btn_back:
                Intent intent = new Intent(Main2Activity.this, AllPost.class);
                //intent.putExtra("user_position",user_position);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*  if(requestCode ==2 && resultCode == RESULT_OK && data != null ){
            int count = data.getClipData().getItemCount(); //evaluate the count before the for loop -
            for(int i = 0; i < count; i++){
                Uri itemUri = data.getClipData().getItemAt(i).getUri();
                imageUri.add(itemUri);
                Toast.makeText(Main2Activity.this,itemUri.toString(),Toast.LENGTH_SHORT).show();
            }
           // imageUri = data.getData();
           // img.setImageURI(imageUri);

        }*/
        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    imageUri.add(imageurl);
                }
                // setting 1st selected image into image switcher
                img.setImageURI(imageUri.get(0));
                position = 0;
              /*  add_pre.setEnabled(false);
                add_next.setEnabled(true);
                add_pre.setVisibility(View.INVISIBLE);
                add_next.setVisibility(View.VISIBLE);*/
            } else {
                Uri imageurl = data.getData();
                imageUri.add(imageurl);
                img.setImageURI(imageUri.get(0));
                position = 0;
               /* add_pre.setEnabled(false);
                add_next.setEnabled(false);
                add_pre.setVisibility(View.INVISIBLE);
                add_next.setVisibility(View.INVISIBLE);*/
            }
            if(imageUri.size() == 1){
                add_pre.setEnabled(false);
                add_next.setEnabled(false);
                add_pre.setVisibility(View.INVISIBLE);
                add_next.setVisibility(View.INVISIBLE);
            }
            else {
                add_pre.setEnabled(false);
                add_next.setEnabled(true);
                add_pre.setVisibility(View.INVISIBLE);
                add_next.setVisibility(View.VISIBLE);
            }
        } else {
            // show this if no image is selected
            //Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }

    }

    private void uploadToFirebase(ArrayList<Uri> uri){

        for(int i=0;i<uri.size();i++) {
            final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri.get(i)));
            fileRef.putFile(uri.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                           // firebaseUri.add(uri);
                          //  Post post = new Post(firebaseUri);
                         //   String postId = root.push().getKey();
                         //   root.child(postId).setValue(post);
                        //    Toast.makeText(Main2Activity.this, String.valueOf(firebaseUri.size()), Toast.LENGTH_SHORT).show();
                            firebaseUri.add(uri);
                            if(firebaseUri.size() == imageUri.size()) {
                                  title = txt_title.getText().toString().trim();
                                  Post post = new Post(User_ID,firebaseUri,title,User_Name);
                                   String postId = root.push().getKey();
                                   root.child(postId).setValue(post);
                                mProgressDialog.dismiss();
                                Intent intent = new Intent(Main2Activity.this, AllPost.class);
                                intent.putExtra("upload_susscess",2);
                                startActivity(intent);
                                //   Toast.makeText(Main2Activity.this, "Upload thành công", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    mProgressDialog = new ProgressDialog(Main2Activity.this);
                    mProgressDialog.setTitle("Upload Image");
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Main2Activity.this, "Upload thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

}
