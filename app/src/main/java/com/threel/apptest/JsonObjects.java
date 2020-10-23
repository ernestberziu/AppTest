package com.threel.apptest;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import dagger.Module;
import dagger.Provides;
@Module
public class JsonObjects {

    @SerializedName("total_pages")
    @Expose
    private String total_pages;
    @SerializedName("total")
    @Expose
    private String total_users;
    @SerializedName("data")
    @Expose
    private List<Users> data;

    @Provides
    String select(){
        return "Select Items";
    }

    public static class Users{
        @SerializedName("data")
        @Expose
        private Users users;
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("first_name")
        @Expose
        public String name;
        @SerializedName("last_name")
        @Expose
        public String surname;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("avatar")
        @Expose
        public String avatar;
        public Bitmap image;
        public String color;
        public String pseudonim;
        public String changed;
        public byte[] ava;
        public String  backgroundColor;
        public Users(){

        }
        public Users(String id, String name, String surname, String email, Bitmap avatar, String color, String pseudonim, String changed,String backgroundColor) {
            this.setId(id);
            this.setName(name);
            this.setSurname(surname);
            this.setEmail(email);
            this.setImage(avatar);
            this.setColor(color);
            this.setPseudonim(pseudonim);
            this.setChanged(changed);
            this.setBackgroundColor(backgroundColor);

        }
        public Users(String id, String name, String surname, String email, byte[] avatar,String color, String pseudonim, String changed) {
            this.setId(id);
            this.setName(name);
            this.setSurname(surname);
            this.setEmail(email);
            this.setColor(color);
            this.setAva(avatar);
            this.setPseudonim(pseudonim);
            this.setChanged(changed);


        }
        public Users(String id, String name, String surname, String email,String color, String pseudonim, String changed) {
            this.setId(id);
            this.setName(name);
            this.setSurname(surname);
            this.setEmail(email);
            this.setColor(color);

            this.setPseudonim(pseudonim);
            this.setChanged(changed);


        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getSurname() {
            return surname;
        }
        public void setSurname(String surname) {
            this.surname = surname;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getAvatar() {
            return avatar;
        }
        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
        public String getBackgroundColor() {
            return backgroundColor;
        }
        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }
        public Users getUsers() {
            return users;
        }
        public void setUsers(Users users) {
            this.users = users;
        }
        public String getColor() {
            return color;
        }
        public void setColor(String color) {
            this.color = color;
        }
        public String getPseudonim() {
            return pseudonim;
        }
        public void setPseudonim(String pseudonim) {
            this.pseudonim = pseudonim;
        }
        public String getChanged() {
            return changed;
        }
        public void setChanged(String changed) {
            this.changed = changed;
        }
        public Bitmap getImage() {
            return image;
        }
        public void setImage(Bitmap image) {
            this.image = image;
        }

        public byte[] getAva() {
            return ava;
        }

        public void setAva(byte[] ava) {
            this.ava = ava;
        }
    }

    public String getTotal_pages() {
        return total_pages;
    }
    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }
    public List<Users> getData() {
        return data;
    }
    public void setData(List<Users> data) {
        this.data = data;
    }
    public String getTotal_users() {
        return total_users;
    }
    public void setTotal_users(String total_users) {
        this.total_users = total_users;
    }

}
