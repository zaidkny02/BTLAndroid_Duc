package com.example.testfirebase;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Post {
   // private int id;
    private String imageUrl;
    private String title;
    private String id_member;
    private String name_member;
    private ArrayList<Uri> imgUri  = new ArrayList<Uri>();
    public ArrayList<Uri> getImgUri() {
        return imgUri;
    }

    public void setImgUri(ArrayList<Uri> imgUri) {
        this.imgUri = imgUri;
    }

    public Post(String id_member,ArrayList<Uri> imgUri,String title,String name_member) {
        this.id_member = id_member;
        this.imageUrl = imgUri.toString();
       // this.imageUrl = imgUri.toString();
        this.title = title;
        this.name_member = name_member;

    }
    public Post(){

    }
    public Post(String imageUrl) {
        this.imageUrl = imageUrl;
        this.title = "Tiêu đề";
        this.name_member = "Người đăng";

    }
    public Post(String title,String imageUrl){
        this.title = title;
        this.imageUrl = imageUrl;
    }
    public void getArrUri(String imageUrl){
            String[] parts = imageUrl.replace("[", "").replace("]", "").split(", ");
            for (int i = 0; i < parts.length; i++) {
                Uri a =  Uri.parse(parts[i]);
               this.imgUri.add(a);

            }

    }

   /* public int getId() {
        return id;
    }*/

  /*  public void setId(int id) {
        this.id = id;
    }*/

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName_member() {
        return name_member;
    }

    public void setName_member(String name_member) {
        this.name_member = name_member;
    }

    public String getIdmember() {
        return id_member;
    }

    public void setIdmember(String idmember) {
        this.id_member = idmember;
    }
}
