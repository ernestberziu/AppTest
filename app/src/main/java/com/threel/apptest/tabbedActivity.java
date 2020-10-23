package com.threel.apptest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class tabbedActivity extends AppCompatActivity implements fragmentInterface {

    public static ArrayList<JsonObjects.Users> saved;
    byte[]imageArray;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        saved=new ArrayList<>();

        DB db= new DB(getApplicationContext());
        final FloatingActionButton fab = findViewById(R.id.fab);
        final FloatingActionButton fab1 = findViewById(R.id.fab1);
        FloatingActionButton fab2 = findViewById(R.id.fab2);
        SQLiteDatabase db2 = db.getReadableDatabase();
        try {
            String rawQuery = "Select id, name,surname,email,avatar,color,pseudonim from UsersByPage";
            Cursor c = db2.rawQuery(rawQuery, null);
            c.moveToFirst();
            try {
                do {
//                    byte[] bitmap= c.getBlob(4);
//                    InputStream inputStream = new ByteArrayInputStream(bitmap);
//                    Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream);


                    saved.add(c.getPosition(),new JsonObjects.Users(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getBlob(4),c.getString(5),c.getString(6),""));
                } while (c.moveToNext());
            } finally {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final TabLayout tabs = findViewById(R.id.tabs);
        final TabItem id1= findViewById(R.id.id1);
        Fragment edit = new PlaceholderFragment(tabbedActivity.this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, edit,null);
        transaction.commit();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Demo.username=String.valueOf(tab.getPosition()+1);
                Fragment edit = new PlaceholderFragment(tabbedActivity.this);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, edit,null);
                transaction.commit();
                fab1.setVisibility(View.GONE);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        final TabItem id2= findViewById(R.id.id2);
        final TabItem id3= findViewById(R.id.id3);
        final TabItem id4= findViewById(R.id.id4);
        final  TabItem id5= findViewById(R.id.id5);
        final TabItem id6= findViewById(R.id.id6);
        final TabItem id7= findViewById(R.id.id7);
        final TabItem id8= findViewById(R.id.id8);
        final TabItem id9= findViewById(R.id.id9);
        final TabItem id10= findViewById(R.id.id10);
        final TabItem id11= findViewById(R.id.id11);
        final TabItem id12= findViewById(R.id.id12);




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0 ;i<saved.size();i++){
                    if(saved.get(i).getChanged().equals("changed")){
                        tabs.getTabAt(Integer.parseInt(saved.get(i).getId())-1).setIcon(R.drawable.ic_baseline_build_24);
                    }
                }
                fab1.setVisibility(View.VISIBLE);

            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCompletableObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getCompletableObserver());
                fab1.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Demo.username="0";
                Demo.tabbedActivity=false;
                Intent discover = new Intent(tabbedActivity.this, MainActivity.class);
                startActivity(discover);
                finish();
            }
        });

    }
    private Completable getCompletableObservable(){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                try {
                    DB db = new DB(getApplicationContext());
                    SQLiteDatabase db4= db.getWritableDatabase();
                    for(int i=0; i<saved.size();i++) {
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        Bitmap bitmap=  saved.get(i).getImage();
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
//                        setImageArray(stream.toByteArray());

                        db.updateUsers(db4, getSaved().get(i).getName(), getSaved().get(i).getSurname(), getSaved().get(i).getEmail(),getSaved().get(i).getAva(), getSaved().get(i).getColor(), getSaved().get(i).getPseudonim(), getSaved().get(i).getId());
                    }
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
                Toast.makeText(getApplicationContext(),"You edited users succesfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {
            }
        };
    }
    @Override
    public void setArraylist(JsonObjects.Users users) {
        saved.set(Integer.parseInt(Demo.username)-1,users);
    }

    public byte[] getImageArray() {
        return imageArray;
    }

    public void setImageArray(byte[] imageArray) {
        this.imageArray = imageArray;
    }

    public static ArrayList<JsonObjects.Users> getSaved() {
        return saved;
    }
}