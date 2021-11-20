package com.example.testfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener{
    EditText username,password;
    TextView signup_text;
    private ArrayList<User> arr;
    Button login_btn;
    private int user_position;
    String Is_User_Login;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Users");
    // variable for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    SharedPreferences sharedpreferences;
    // key for storing username
    public static final String USER_KEY = "user_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getView();
        getsignup();


        arr = new ArrayList<>();
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    arr.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            Intent intent = new Intent(Login.this, AllPost.class);
            //intent.putExtra("user_position",user_position);
            startActivity(intent);
        }
     //   Toast.makeText(Login.this, "Quay lại?", Toast.LENGTH_SHORT).show();



    }
    private void getView(){
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        signup_text = findViewById(R.id.login_signup);
        login_btn = findViewById(R.id.login_btnlogin);
        login_btn.setOnClickListener(this);
        signup_text.setOnClickListener(this);
    }


    private void getsignup()
    {
        Intent intent = getIntent();
        int signup_success =  intent.getIntExtra("success",0);
        if(signup_success == 1)
        {
            Toast.makeText(Login.this, "đăng ký thành công", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_signup:
                Intent backIntent = new Intent(Login.this, SignUp.class);
                startActivity(backIntent);
                break;
            case R.id.login_btnlogin:
                if(checktaikhoan())
                {
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    // below two lines will put values for
                    // email and password in shared preferences.
                    editor.putString(USER_KEY, arr.get(user_position).getUsername());

                    // to save our data with key and value.
                    editor.apply();
                    Intent intent = new Intent(Login.this, AllPost.class);
                    //intent.putExtra("user_position",user_position);
                    startActivity(intent);
                }
                break;
            default:
                break;

        }
    }
    private boolean checktaikhoan(){
        if(username.getText().toString().trim().length() == 0)
        {
            Toast.makeText(Login.this,"Vui lòng nhập username",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.getText().toString().trim().length() == 0)
        {
            Toast.makeText(Login.this,"Vui lòng nhập password",Toast.LENGTH_SHORT).show();
            return false;
        }
        for(int i=0;i<arr.size();i++) {
            if (username.getText().toString().equalsIgnoreCase(arr.get(i).getUsername()) && password.getText().toString().equalsIgnoreCase(arr.get(i).getPassword())) {
                user_position = i;
                return true;
            }
        }
        Toast.makeText(Login.this, "Không tìm thấy tên đăng nhập hoặc sai mật khẩu", Toast.LENGTH_SHORT).show();
        return false;
    }
}
