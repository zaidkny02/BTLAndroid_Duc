package com.example.testfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class AllPost extends AppCompatActivity implements View.OnClickListener{
    Button allpost_logout,allpost_addpost;
    private RecyclerView recyclerView;
    private ArrayList<Post> arr;
    private MyAdapter adapter;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Posts");
    String Is_User_Login;
    // variable for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    SharedPreferences sharedpreferences;
    // key for storing username
    public static final String USER_KEY = "user_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_post);
        getView();
    //    getUser();
        getUpload();
        arr = new ArrayList<>();

        adapter = new MyAdapter(AllPost.this,arr);
        //orderbyChild: thuộc tính con
        //limittoFirst: từ đầu danh sách ,limitolast: cuối
        //startAt,endAt/After
        //equalTo
       root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getValue() != null) {
                        Post post = dataSnapshot.getValue(Post.class);
                        post.getArrUri(post.getImageUrl());
                        arr.add(post);

                    }
                    else
                    {
                       // Toast.makeText(AllPost.this,"Đọc xong",Toast.LENGTH_SHORT).show();

                    }

                }
                Collections.reverse(arr);
                adapter.notifyDataSetChanged();

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllPost.this,"Lỗi đọc dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });






    }

    private void getView(){
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        allpost_logout = findViewById(R.id.allpost_logout);
        allpost_logout.setOnClickListener(this);
        allpost_addpost = findViewById(R.id.allpost_addpost);
        allpost_addpost.setOnClickListener(this);
    }
    private void getUpload(){
        Intent intent = getIntent();
        int upload_success =  intent.getIntExtra("upload_susscess",0);
        if(upload_success == 2)
        {
            Toast.makeText(AllPost.this, "Upload thành công", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        // set to null if not present.
        Is_User_Login = sharedpreferences.getString(USER_KEY ,null);
        if(Is_User_Login != null){

        }
        else {
            Intent intent = new Intent(AllPost.this, Login.class);
            //intent.putExtra("user_position",user_position);
            startActivity(intent);
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.allpost_logout:
                SharedPreferences.Editor editor = sharedpreferences.edit();

                // below two lines will put values for
                // email and password in shared preferences.
                editor.putString(USER_KEY, null);

                // to save our data with key and value.
                editor.apply();
                Intent intent = new Intent(AllPost.this, Login.class);
                //intent.putExtra("user_position",user_position);
                startActivity(intent);
                break;
            case R.id.allpost_addpost:
                Intent addintent = new Intent(AllPost.this, Main2Activity.class);
                //intent.putExtra("user_position",user_position);
                startActivity(addintent);
                break;
            default:
                break;
        }
    }
}
