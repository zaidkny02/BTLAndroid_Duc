package com.example.testfirebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static android.widget.LinearLayout.*;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static String text;
    private static String name;
    private static int id = 0;
    String Is_User_Login,User_ID;
    private ArrayList<Post> mList;
    private Context context;
    private DatabaseReference userroot = FirebaseDatabase.getInstance().getReference("Users");
    public static final String SHARED_PREFS = "shared_prefs";
    SharedPreferences sharedpreferences;
    // key for storing username
    public static final String USER_KEY = "user_key";



    public MyAdapter(Context context,ArrayList<Post> mList){
        this.context = context;

       // Collections.reverse(mList);
        this.mList = mList;
        sharedpreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        // set to null if not present.
        Is_User_Login = sharedpreferences.getString(USER_KEY ,null);
        userroot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getValue() != null) {
                        User user = dataSnapshot.getValue(User.class);
                        if(user.getUsername().equalsIgnoreCase(Is_User_Login)) {
                            User_ID = user.getId();
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
                Toast.makeText(context,"Lỗi đọc dữ liệu",Toast.LENGTH_SHORT).show();
            }
        });
        //Toast.makeText(context, context.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        if(context.getClass().getSimpleName().equalsIgnoreCase("List_User_Post"))
            id = 1;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item ,parent,false);

        MyViewHolder holder = new MyViewHolder(v);
        // set the Context here

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(mList.get(position).getImgUri().get(0)).into(holder.imageView);
        holder.txtView.setText(mList.get(position).getTitle());
    //    text = mList.get(position).getTitle();
    //    name = mList.get(position).getName_member();
        holder.author.setText("By " +mList.get(position).getName_member());
        holder.imageUri = mList.get(position).getImgUri();
        holder.author_id = mList.get(position).getIdmember();
        if(id == 1){
          //  holder.author.setVisibility(View.INVISIBLE);
          //  holder.author.setClickable(false);
        }
        if(mList.get(position).getImgUri().size() == 1){

            holder.m_next_btn.setVisibility(View.INVISIBLE);
        }
        if(id == 1 && mList.get(position).getIdmember().equalsIgnoreCase(User_ID))
        {
          /*  holder.m_add_img_btn.setVisibility(View.VISIBLE);
            holder.m_add_img_btn.setClickable(true);
            holder.m_del_img_btn.setVisibility(View.VISIBLE);
            holder.m_del_img_btn.setClickable(true);
            holder.m_del_btn.setVisibility(View.VISIBLE);
            holder.m_del_btn.setClickable(true);
            holder.m_edit_btn.setVisibility(View.VISIBLE);
            holder.m_edit_btn.setClickable(true);*/
        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView txtView;
        TextView author;
        Button m_pre_btn,m_next_btn,m_add_img_btn,m_edit_btn,m_del_img_btn,m_del_btn;
        String author_id;
      //  boolean isImageFitToScreen;
        CardView card;
        ArrayList<Uri> imageUri = new ArrayList<Uri>();
        int pos = 0;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            txtView = itemView.findViewById(R.id.m_text);
            card = itemView.findViewById(R.id.Card);
            m_add_img_btn = itemView.findViewById(R.id.m_add_img_btn);
            m_add_img_btn.setOnClickListener(this);
            m_edit_btn = itemView.findViewById(R.id.m_edit_btn);
            m_edit_btn.setOnClickListener(this);
            m_del_img_btn = itemView.findViewById(R.id.m_del_img_btn);
            m_del_img_btn.setOnClickListener(this);
            m_del_btn = itemView.findViewById(R.id.m_del_btn);
            m_del_btn.setOnClickListener(this);
      //      txtView.setText(text);

            author = itemView.findViewById(R.id.m_author);
            author.setOnClickListener(this);
       //     author.setText(name);
            imageView = itemView.findViewById(R.id.m_image);
            imageView.setOnClickListener(this);
            m_pre_btn = itemView.findViewById(R.id.m_pre_btn);
            m_pre_btn.setOnClickListener(this);
            m_next_btn = itemView.findViewById(R.id.m_next_btn);
            m_next_btn.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.m_author:
                    Intent intent = new Intent(itemView.getContext(), List_User_Post.class);
                    intent.putExtra("user_target",author_id);
                    itemView.getContext().startActivity(intent);
                    break;
                case R.id.m_pre_btn:
                    if (pos > 0) {
                        // decrease the position by 1
                        pos--;
                      //  imageView.setImageURI(imageUri.get(pos));
                        Glide.with(itemView.getContext()).load(imageUri.get(pos)).into(imageView);
                        m_next_btn.setEnabled(true);
                        m_next_btn.setVisibility(View.VISIBLE);
                        if(pos == 0) {
                            m_pre_btn.setEnabled(false);
                            m_pre_btn.setVisibility(View.INVISIBLE);
                        }
                    }
                    break;
                case R.id.m_next_btn:
                    if (pos < imageUri.size() - 1) {
                        // increase the position by 1
                        pos++;
                        Glide.with(itemView.getContext()).load(imageUri.get(pos)).into(imageView);
                        m_pre_btn.setEnabled(true);
                        m_pre_btn.setVisibility(View.VISIBLE);
                        if(pos == imageUri.size() - 1) {

                            m_next_btn.setEnabled(false);
                            m_next_btn.setVisibility(View.INVISIBLE);
                        }
                    }
                    break;

                case R.id.m_image:
                    Intent fullintent = new Intent(itemView.getContext(), IMG_FullSize.class);
                    fullintent.putExtra("imageuri",imageUri);
                    fullintent.putExtra("position",pos);
                    itemView.getContext().startActivity(fullintent);
                    break;
                default:
                    break;
            }

        }
    }
}
