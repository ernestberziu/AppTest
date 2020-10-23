package com.threel.apptest;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class Dialog extends AppCompatDialogFragment {
    public EditText addGropName;
    TextView textView;
    ProgressBar progressBar;
    RecyclerView listView,groupList,selectUser;
    FloatingActionButton save,fab;
    ArrayList<JsonObjects.Users> usersArrayList = new ArrayList<>();
    RecyclerView.Adapter SelectAdapter;
    MainActivity mainActivity;



    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_layout,null );
        addGropName=view.findViewById(R.id.newGropName);
        progressBar=getActivity().findViewById(R.id.progressBar);
        listView=getActivity().findViewById(R.id.showUsersList);
        groupList=getActivity().findViewById(R.id.showGroupsList);
        selectUser=getActivity().findViewById(R.id.selectUsers);
        save=getActivity().findViewById(R.id.save);
        fab=getActivity().findViewById(R.id.fab);
        mainActivity = new MainActivity();
        textView=getActivity().findViewById(R.id.textView);
        builder.setView(view)

        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressBar.setVisibility(View.VISIBLE);
                Demo.setTable_name(addGropName.getText().toString());
                final DB db= new DB(getContext());
                GroupsDB grdb = new GroupsDB(getContext());
                final SQLiteDatabase db4= db.getWritableDatabase();
                final SQLiteDatabase db1=grdb.getWritableDatabase();
                for(int k=1; k<13;k++){
                    db.updateUsersList(db4,"#ffffff",String.valueOf(k));
                }
                Demo.setGroup_name(addGropName.getText().toString());
                getCompletableObservable("add")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getCompletableObserver("add"));


                grdb.onCreate(db1);
                fab.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);

                getCompletableObservable("select")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getCompletableObserver("select"));
            }
        });
        return builder.create();
    }

    private Completable getCompletableObservable(String type) {
        if (type.equals("add")) {
            return Completable.create(new CompletableOnSubscribe() {
                @Override
                public void subscribe(CompletableEmitter emitter) throws Exception {
                    try {
                        DB db = new DB(getContext());
                        SQLiteDatabase db4 = db.getWritableDatabase();

                        db.adgrElement(db4, Demo.getTable_name());
                    } catch (Exception e) {

                    }
                    if (!emitter.isDisposed())
                        emitter.onComplete();
                }
            });
        } else if (type.equals("select")) {
            return Completable.create(new CompletableOnSubscribe() {
                @Override
                public void subscribe(CompletableEmitter emitter) throws Exception {
                    try {
                        DB db= new DB(getContext());

                        selectUser = (RecyclerView) getActivity().findViewById(R.id.selectUsers);
                        ArrayList<JsonObjects.Users> usersArrayList = new ArrayList<>();
                        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
                        SelectAdapter = new SelectAdapter(usersArrayList,getActivity().getApplicationContext());
                        SQLiteDatabase db2 = db.getReadableDatabase();
                        Cursor cursor = db.getUserData(db2);
                        String id, name, surname, email, color, pseudonim;
                        byte[]avatar;
                        String changed,backgroundColor;
                        Bitmap bitmap = null;
                        while (cursor.moveToNext()) {
                            id = cursor.getString(cursor.getColumnIndex("id"));
                            name = cursor.getString(cursor.getColumnIndex("name"));
                            surname = cursor.getString(cursor.getColumnIndex("surname"));
                            email = cursor.getString(cursor.getColumnIndex("email"));
                            avatar = cursor.getBlob(cursor.getColumnIndex("avatar"));
                            InputStream inputStream = new ByteArrayInputStream(avatar);
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            color = cursor.getString(cursor.getColumnIndex("color"));
                            pseudonim = cursor.getString(cursor.getColumnIndex("pseudonim"));
                            changed = cursor.getString(cursor.getColumnIndex("changed"));
                            backgroundColor = cursor.getString(cursor.getColumnIndex("backgroundColor"));

                            JsonObjects.Users users = new JsonObjects.Users(id, name, surname, email, bitmap, color, pseudonim, changed,backgroundColor);
                            usersArrayList.add(users);

                        }

                    }catch (Exception e){

                    }
                    if(!emitter.isDisposed())
                        emitter.onComplete();
                }
            });
        }
        return null;
    }
    CompletableObserver getCompletableObserver(String type){
        if(type.equals("add")){

        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Toast.makeText(getContext(),"You added a group", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {
            }
        };}
        else if(type.equals("select")){
            return new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onComplete() {
                    progressBar.setVisibility(View.GONE);
                    selectUser.setLayoutManager(new LinearLayoutManager(mainActivity));
                    selectUser.setAdapter(SelectAdapter);
                    selectUser.setVisibility(View.VISIBLE);


                }

                @Override
                public void onError(Throwable e) {
                }
            };
        }
        return null;
    }



}
