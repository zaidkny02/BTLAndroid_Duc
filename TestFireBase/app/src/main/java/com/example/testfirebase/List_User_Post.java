package com.example.testfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class List_User_Post extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView userpost_recyclerview;
    private ArrayList<Post> arr;
    private MyAdapter adapter;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Posts");
    private DatabaseReference userroot = FirebaseDatabase.getInstance().getReference("Users");
    private String target_id;
    private String name;
    Button userpost_back;
    TextView userpost_name,userpost_sumpost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__user__post);
        getUserTarget();
        getView();

        arr = new ArrayList<>();
        userroot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getValue() != null) {
                        User user = dataSnapshot.getValue(User.class);
                       if(user.getId().equalsIgnoreCase(target_id)){
                           name = user.getName();
                           userpost_name.setText(name);
                           break;
                       }
                    }
                    else
                    {
                        // Toast.makeText(AllPost.this,"Đọc xong",Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(List_User_Post.this,"Lỗi đọc dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new MyAdapter(List_User_Post.this,arr);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getValue() != null) {
                        Post post = dataSnapshot.getValue(Post.class);
                        if(post.getIdmember().equalsIgnoreCase(target_id)) {
                            post.getArrUri(post.getImageUrl());
                            arr.add(post);

                            //adapter.notifyDataSetChanged();


                        }
                    }
                    else
                    {
                        // Toast.makeText(AllPost.this,"Đọc xong",Toast.LENGTH_SHORT).show();

                    }

                }
                userpost_recyclerview.setAdapter(adapter);
                userpost_sumpost.setText("Tổng số bài: "+ arr.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(List_User_Post.this,"Lỗi đọc dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private  void getUserTarget(){
        Intent intent = getIntent();
        //int upload_success =  intent.getIntExtra("upload_susscess",0);
        String user_target = intent.getStringExtra("user_target");
        if(user_target == null)
        {
            //Toast.makeText(AllPost.this, "Upload thành công", Toast.LENGTH_SHORT).show();
            Intent newintent = new Intent(List_User_Post.this, AllPost.class);
            startActivity(newintent);
        }
        else {
            //Toast.makeText(List_User_Post.this, user_target, Toast.LENGTH_SHORT).show();
            target_id = user_target;

        }
    }
    private void getView(){
        userpost_recyclerview = findViewById(R.id.userpost_recyclerview);
        userpost_recyclerview.setHasFixedSize(true);
        userpost_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        userpost_sumpost = findViewById(R.id.userpost_sumpost);
        userpost_name = findViewById(R.id.userpost_name);
        userpost_back = findViewById(R.id.userpost_back);
        userpost_back.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.userpost_back:
                Intent intent = new Intent(List_User_Post.this, AllPost.class);
                //intent.putExtra("user_position",user_position);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
