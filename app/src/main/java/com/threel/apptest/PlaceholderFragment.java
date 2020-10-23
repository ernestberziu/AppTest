package com.threel.apptest;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;


public class PlaceholderFragment extends Fragment {
    Button color,modify;
    EditText name,surname,email,pseudonim;
    public String chosenColor;
    tabbedActivity tabbedActivity;
    FloatingActionButton fab;
    fragmentInterface fragmentInterface;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    ImageView image;
    byte[] picturePath;
    Bitmap bitmap1;
    public PlaceholderFragment(fragmentInterface fragmentInterface){
        this.fragmentInterface=fragmentInterface;
    }
    public PlaceholderFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit, container, false);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final DB db = new DB(getContext());
        SQLiteDatabase db1= db.getReadableDatabase();
        tabbedActivity=new tabbedActivity();
        fab=getActivity().findViewById(R.id.fab);
        image=getActivity().findViewById(R.id.image);



        Cursor c = db1.rawQuery("SELECT * FROM UsersByPage WHERE id = '"+Demo.username+"'", null);
        c.moveToNext();
        color = getView().findViewById(R.id.color);
        setChosenColor(c.getString(c.getColumnIndex("color")));
        if(getChosenColor()==null){

        }else{
            color.setBackgroundColor(Color.parseColor(c.getString(c.getColumnIndex("color"))));
        }


        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getContext(), color);
                popup.getMenuInflater().inflate(R.menu.colors, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("WHITE")){

                            setChosenColor("#FFFFFF");
                            color.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                        else if(item.getTitle().equals("RED")){

                            setChosenColor("#FF0000");
                            color.setBackgroundColor(Color.parseColor("#FF0000"));
                        }
                        else if(item.getTitle().equals("PINK")){

                            setChosenColor("#FFC0CB");
                            color.setBackgroundColor(Color.parseColor("#FFC0CB"));
                        }
                        else if(item.getTitle().equals("BLACK")){

                            setChosenColor("#000000");
                            color.setBackgroundColor(Color.parseColor("#000000"));
                        }
                        else if(item.getTitle().equals("GREEN")){

                            setChosenColor("#008000");
                            color.setBackgroundColor(Color.parseColor("#008000"));
                        }
                        else if(item.getTitle().equals("YELLOW")){

                            setChosenColor("#FFFF00");
                            color.setBackgroundColor(Color.parseColor("#FFFF00"));
                        }
                        else if(item.getTitle().equals("GRAY")){

                            setChosenColor("#080808");
                            color.setBackgroundColor(Color.parseColor("#080808"));
                        }
                        else if(item.getTitle().equals("BLUE")){

                            setChosenColor("#0000FF");
                            color.setBackgroundColor(Color.parseColor("#0000FF"));
                        }

                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose your profile picture");

                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (options[item].equals("Take Photo")) {
                            if(hasCameraPermission()){
                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                startActivityForResult(intent, 0);
                            }else{
                                requestCameraPermission();
                            }
                        } else if (options[item].equals("Choose from Gallery")) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto , 1);

                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();


            }
        });

        modify= getView().findViewById(R.id.modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

if(Demo.tabbedActivity==false){
    getCompletableObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getCompletableObserver());
    Toast.makeText(getContext(),"Saved",Toast.LENGTH_SHORT);
    Intent discover = new Intent(getActivity(), MainActivity.class);
    startActivity(discover);
    getActivity().finish();

}if(Demo.tabbedActivity==true){
                JsonObjects.Users users=new JsonObjects.Users(Demo.username,name.getText().toString(),surname.getText().toString(),email.getText().toString(),getPicturePath(),getChosenColor(),pseudonim.getText().toString(),"changed");
                fragmentInterface.setArraylist(users);
                fab.setVisibility(View.VISIBLE);
                }
            }
        });
        name=  getView().findViewById(R.id.name);
        name.setText(c.getString(c.getColumnIndex("name")));
        surname= getView().findViewById(R.id.surname);
        surname.setText(c.getString(c.getColumnIndex("surname")));
        email= getView().findViewById(R.id.email);
        email.setText(c.getString(c.getColumnIndex("email")));
        pseudonim= getView().findViewById(R.id.pseudonim);
        pseudonim.setText(c.getString(c.getColumnIndex("pseudonim")));
        InputStream inputStream = new ByteArrayInputStream( c.getBlob(c.getColumnIndex("avatar")));
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        image.setImageBitmap(bitmap);
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        bitmap1=drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 90, stream);
        setPicturePath(stream.toByteArray());

    }

    private Completable getCompletableObservable(){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                try {
                    DB db = new DB(getContext());
                    SQLiteDatabase db4= db.getWritableDatabase();



                    db.updateUsers(db4,name.getText().toString(),surname.getText().toString(),email.getText().toString(),getPicturePath(),getChosenColor(),pseudonim.getText().toString(),Demo.username);
                }catch (Exception e){

                }
                if(!emitter.isDisposed())
                    emitter.onComplete();
            }
        });
    }
    CompletableObserver getCompletableObserver(){
        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Toast.makeText(getContext(),"You edited user succesfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {
            }
        };
    }
    public String getChosenColor() {
        return chosenColor;
    }
    public void setChosenColor(String chosenColor) {
        this.chosenColor = chosenColor;
    }

    public byte[] getPicturePath() {
        return picturePath;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0)
        {
            if(data != null)
            {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                photo = Bitmap.createScaledBitmap(photo, 80, 80, false);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 90, stream);
                setPicturePath(stream.toByteArray());
                image.setImageBitmap(photo);
            }
            else{
            }
        }else if (requestCode == 1){
            if (resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                image.setImageURI(selectedImage);
                BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                setPicturePath(stream.toByteArray());


            }
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA},
                MY_CAMERA_REQUEST_CODE );
    }



    public void setPicturePath(byte[] picturePath) {
        this.picturePath = picturePath;
    }
    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
}