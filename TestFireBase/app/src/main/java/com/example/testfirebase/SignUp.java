package com.example.testfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    Button signup,backtologin;
    EditText fullname,username,password;
    private ArrayList<String> arr;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getView();
        //String a = root.getKey();
       // Toast.makeText(SignUp.this, a, Toast.LENGTH_SHORT).show();

        arr = new ArrayList<>();
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                     User user = dataSnapshot.getValue(User.class);
                    arr.add(user.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getView(){
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(this);
        backtologin = findViewById(R.id.signup_backtologin);
        backtologin.setOnClickListener(this);
        fullname = findViewById(R.id.signup_fullname);
        username = findViewById(R.id.signup_username);
        password = findViewById(R.id.signup_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup:
               // Toast.makeText(SignUp.this,"ABCD",Toast.LENGTH_SHORT).show();
                uploadToFirebase();
                break;
            case R.id.signup_backtologin:
                Intent backIntent = new Intent(this, Login.class);
                startActivity(backIntent);
                break;
            default:
                break;

        }
    }
    private void uploadToFirebase(){


        if(checkvalue()) {
            String userId = root.push().getKey();
            User user = new User(userId, fullname.getText().toString(), username.getText().toString(), password.getText().toString());
            root.child(userId).setValue(user);
            Intent intent = new Intent(SignUp.this, Login.class);
            intent.putExtra("success",1);
            startActivity(intent);
        }



    }
    private boolean checkvalue() {
        if(fullname.getText().toString().trim().length() == 0 )
        {
            Toast.makeText(SignUp.this,"Vui lòng nhập fullname",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(username.getText().toString().trim().length() == 0 )
        {
            Toast.makeText(SignUp.this,"Vui lòng nhập username",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.getText().toString().trim().length() == 0 )
        {
            Toast.makeText(SignUp.this,"Vui lòng nhập password",Toast.LENGTH_SHORT).show();
            return false;
        }
        for(int i=0;i<arr.size();i++) {
            if (username.getText().toString().equalsIgnoreCase(arr.get(i))) {
                Toast.makeText(SignUp.this, "Đã tồn tại username này", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
