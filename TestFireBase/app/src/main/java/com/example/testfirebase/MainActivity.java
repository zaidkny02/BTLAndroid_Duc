package com.example.testfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn;
    String TAG="FIREBASE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realtimedb();
        getView();
    }
    private void getView(){
        btn = findViewById(R.id.button);
        btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                Realtimedb();
                break;
            default:
                break;
        }
    }
    private void add() {
        // Lấy ra keyID  String keyID = FirebaseDatabase.getInstance().getReference().push().getKey();

// set key vao object<object_cua_ban>.setKey(keyID); // lưu lại để sau này dùng chẳng hạn

// set data
        FirebaseDatabase.getInstance().getReference().child("ABC").setValue(1);
    }
    private void read(){
        Query allPost = FirebaseDatabase.getInstance().getReference().child("ABC");
      //  Query post = FirebaseDatabase.getInstance().getReference().child("Posts").child("id");
      //  Query allCatePost = FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("cateID").equalTo("id");
      //  Query post = FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("id").equalTo("id");

    }
    private void Realtimedb() {
     //   FirebaseDatabase database = FirebaseDatabase.getInstance();
      //  DatabaseReference myRef = database.getReference().child("ABC");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
       // myRef.setValue("Hello, World!");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);

              //  Toast.makeText(MainActivity.this, value, Toast.LENGTH_LONG).show();

                Log.d(TAG,value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
           //     Toast.makeText(MainActivity.this, "Can't connect", Toast.LENGTH_LONG).show();;
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
