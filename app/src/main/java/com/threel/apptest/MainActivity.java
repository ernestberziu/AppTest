package com.threel.apptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Component;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public  class MainActivity extends AppCompatActivity {


    //Use of butterknife
    @BindView(R.id.userList)
    Button users;
    @BindView(R.id.groupList)
    Button groups;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private boolean flag = false;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    FloatingActionButton fab, save;
    RecyclerView listView;

    //use of dagger
    @Inject
    String text;
    TextView textView;
    RecyclerView groupsList, selectUser;
    ArrayList<JsonObjects.Users> saved;
    RecyclerView.Adapter SelectAdapter, UserAdapter,groupsAdapter;
    selectedItems selectedItems = new selectedItems();
    static ArrayList<selectedItems> selectedItems1 = new ArrayList<>();
    ArrayList<Table> tables = new ArrayList<>();
    ArrayList<ID> ids = new ArrayList<>();
    DB db = new DB(MainActivity.this);
    GroupsDB grdb = new GroupsDB(MainActivity.this);
    ArrayList<JsonObjects.Users> usersArrayList = new ArrayList<>();
    API api;
    Bitmap bitmap ;
    byte[] imageArray;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        progressBar = findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.VISIBLE);
        ((com.threel.apptest.component)getApplication())
                .getComponent()
                .inject(this);
        textView = findViewById(R.id.textView);
        textView.setText(text);
//
        selectUser = (RecyclerView) findViewById(R.id.selectUsers);
        SQLiteDatabase db4 = db.getWritableDatabase();
        final SQLiteDatabase db7 = db.getReadableDatabase();
        SQLiteDatabase db2 = db.getReadableDatabase();
        saved = new ArrayList<>();
        try {
            String rawQuery = "Select id, name,surname,email,color,pseudonim from UsersByPage";
            Cursor c = db2.rawQuery(rawQuery, null);
            c.moveToFirst();
            try {
                do {
                    saved.add(c.getPosition(), new JsonObjects.Users(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), ""));
                } while (c.moveToNext());
            } finally {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (saved.size() == 0) {

            getTotalPagessResponse();
            getTotalUsersResponse();

        }
        Demo.groupclicked = false;
        for (int j = 0; j < 12; j++) {
            selectedItems = new selectedItems(String.valueOf(j + 1), "#ffffff");
            selectedItems1.add(j, selectedItems);
        }
        for (int k = 1; k < 13; k++) {
            db.updateUsersList(db4, "#ffffff", String.valueOf(k));
        }
        try {
            SQLiteDatabase db3 = db.getReadableDatabase();
            final Cursor cursor1 = db.getTables(db3);
            String name1;
            while (cursor1.moveToNext()) {

                name1 = cursor1.getString(cursor1.getColumnIndex("name"));
                Table selectedTable = new Table(name1);
                tables.add(selectedTable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        selectUser = findViewById(R.id.selectUsers);
        selectUser.setVisibility(View.GONE);
        listView = findViewById(R.id.showUsersList);

        save = findViewById(R.id.save);
        groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    SQLiteDatabase db5 = db.getWritableDatabase();
                    listView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    selectUser.setVisibility(View.GONE);
                    groupsList.setVisibility(View.GONE);
                    getCompletableObservable("groups")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(getCompletableObserver("groups"));
                    for (int k = 1; k < 13; k++) {
                        db.updateUsersList(db5, "#ffffff", String.valueOf(k));
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Please add a group", Toast.LENGTH_SHORT).show();
                }
                save.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(View.GONE);
                selectUser.setVisibility(View.GONE);
                groupsList.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                getCompletableObservable("users")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getCompletableObserver("users"));
                save.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });
        groupsList = findViewById(R.id.showGroupsList);

        progressBar.setVisibility(View.INVISIBLE);
        fab = findViewById(R.id.fab);
        textView.setVisibility(View.GONE);
        save.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Demo.groupclicked = false;
                openDiaglog();
                selectUser.setVisibility(View.GONE);
                groupsList.setVisibility(View.GONE);
            }
        });

        Cursor cursor2 = db.getId(db7);
        String id;
        while (cursor2.moveToNext()) {

            id = cursor2.getString(cursor2.getColumnIndex("id"));
            ID selecteded = new ID(id);
            ids.add(selecteded);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db1 = grdb.getWritableDatabase();
                SQLiteDatabase db5 = db.getWritableDatabase();
                SQLiteDatabase db7 = db.getReadableDatabase();
                try {
                    getCompletableObservable("groups")
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(getCompletableObserver("groups"));
                    selectUser.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Cursor cursor2 = db.getId(db7);
                String id;
                while (cursor2.moveToNext()) {

                    id = cursor2.getString(cursor2.getColumnIndex("id"));
                    ID selecteded = new ID(id);
                    ids.add(selecteded);
                }
                if (Demo.groupclicked == false) {
                    for (int i = 0; i < selectedItems1.size(); i++) {
                        grdb.addData(db1, ids.get(i).getId(), selectedItems1.get(i).getBackgroundColor(), Demo.getGroup_name());
                    }
                    for (int k = 1; k < 13; k++) {
                        db.updateUsersList(db5, "#ffffff", String.valueOf(k));
                    }
                    Table selectedTable = new Table(Demo.getTable_name());
                    tables.add(selectedTable);
                } else if (Demo.groupclicked == true) {
                    for (int k = 0; k < 12; k++) {
                        grdb.updategroupList(db1, selectedItems1.get(k).getBackgroundColor(), ids.get(k).getId());
                    }
                    for (int k = 1; k < 13; k++) {
                        db.updateUsersList(db5, "#ffffff", String.valueOf(k));
                    }
                }
                for (int b = 0; b < 12; b++) {
                    selectedItems = new selectedItems(String.valueOf(b + 1), "#ffffff");
                    selectedItems1.set(b, selectedItems);
                }
                textView.setVisibility(View.GONE);
                groupsList.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });
    }

    public void openDiaglog() {
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(), "Add New Group");
    }

    private void getTotalPagessResponse() {
        Retrofit retrofit = new Retrofit.Builder()

                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://reqres.in/api/")
                .build();

        api = retrofit.create(API.class);
        Observable<JsonObjects> observable = api.getTotalPages();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonObjects>() {
                    @Override
                    public void accept(JsonObjects responseData) {
                        for (int i = 1; i <= Integer.parseInt(responseData.getTotal_pages()); i++)
                            getTotalPageUsers(String.valueOf(i));
                    }
                });

    }

    private void getTotalPageUsers(String pageNo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(API.class);
        Observable<JsonObjects> observable = api.getPageUsers(pageNo);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonObjects>() {
                               @Override
                               public void accept(JsonObjects responseData) {
                                   for (int i = 0; i < responseData.getData().size(); i++) {
                                       DB db = new DB(getApplicationContext());
                                       SQLiteDatabase db1 = db.getWritableDatabase();
                                      try{
                                          StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                  .permitAll().build();
                                          StrictMode.setThreadPolicy(policy);
                                          URL imageUrl = new URL(responseData.getData().get(i).getAvatar());
                                          URLConnection conn = imageUrl.openConnection();
                                          InputStream is = conn.getInputStream();
                                          BufferedInputStream bis = new BufferedInputStream(is);
                                          bitmap = BitmapFactory.decodeStream(bis);
                                          ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                          bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                                          imageArray = stream.toByteArray();
                                      } catch (Exception e) {
                                          e.printStackTrace();
                                      }
                                       db.addUsersData(db1, responseData.getData().get(i).getId(), responseData.getData().get(i).getName(), responseData.getData().get(i).getSurname(), responseData.getData().get(i).getEmail(), imageArray, "#ffffff", "", "", "#ffffff");
                                   }

                               }
                           }
                );

    }

    private void getTotalUsersResponse() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(API.class);
        Observable<JsonObjects> observable = api.getTotalPages();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonObjects>() {
                    @Override
                    public void accept(JsonObjects responseData) throws Exception {
                        for (int i = 1; i <= Integer.parseInt(responseData.getTotal_users()); i++)
                            getUsersData(String.valueOf(i));
                    }
                });
    }

    private void getUsersData(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(API.class);
        Observable<JsonObjects.Users> observable = api.getUsersById(id);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonObjects.Users>() {
                               @Override
                               public void accept(JsonObjects.Users users) throws Exception {
                                   DB db = new DB(getApplicationContext());
                                   SQLiteDatabase db1 = db.getWritableDatabase();
                                   try {
                                       StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                               .permitAll().build();
                                       StrictMode.setThreadPolicy(policy);
                                       URL imageUrl = new URL(users.getUsers().getAvatar());
                                       URLConnection conn = imageUrl.openConnection();

                                       InputStream is = conn.getInputStream();
                                       BufferedInputStream bis = new BufferedInputStream(is);
                                       bitmap = BitmapFactory.decodeStream(bis);
                                       ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                       bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                                       imageArray = stream.toByteArray();

                                   } catch (Exception e) {}
                                   db.addData(db1, users.getUsers().getId(), users.getUsers().getName(), users.getUsers().getSurname(), users.getUsers().getEmail(), imageArray, "#ffffff", "", "", "#ffffff");



                               }

                           }

                );
//        observable.doOnComplete(new Action() {
//            @Override
//            public void run() throws Exception {
//                progressBar.setVisibility(View.GONE);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    public ArrayList<ID> getIds() {
        return ids;
    }

    public ArrayList<com.threel.apptest.selectedItems> getSelectedItems1() {
        return selectedItems1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuid = item.getItemId();

        if (menuid == R.id.tabbed) {
            Demo.username = "1";
            Demo.tabbedActivity = true;


            Intent discover = new Intent(MainActivity.this, tabbedActivity.class);
            startActivity(discover);

            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    public Completable getCompletableObservable(String type) {
        if (type.equals("users")) {
            return Completable.create(new CompletableOnSubscribe() {
                @Override
                public void subscribe(CompletableEmitter emitter) throws Exception {
                    try {
                        DB db = new DB(getApplicationContext());
                        listView = (RecyclerView) findViewById(R.id.showUsersList);
                        selectUser = (RecyclerView) findViewById(R.id.selectUsers);
                        groupsList = (RecyclerView) findViewById(R.id.showGroupsList);
                        ArrayList<JsonObjects.Users> usersArrayList = new ArrayList<>();
                        progressBar = (ProgressBar) findViewById(R.id.progressBar);
                        UserAdapter = new UserAdapter(usersArrayList, getApplicationContext());

                        SQLiteDatabase db2 = db.getReadableDatabase();
                        Cursor cursor = db.getUserData(db2);

                        String id, name, surname, email, color, pseudonim, changed, backgroundColor;
                        byte[] avatar;
                        Bitmap bitmap = null;
                        while (cursor.moveToNext()) {
                            id = cursor.getString(cursor.getColumnIndex("id"));
                            name = cursor.getString(cursor.getColumnIndex("name"));
                            surname = cursor.getString(cursor.getColumnIndex("surname"));
                            email = cursor.getString(cursor.getColumnIndex("email"));
                            avatar = cursor.getBlob(cursor.getColumnIndex("avatar"));
                            try {
                                InputStream inputStream = new ByteArrayInputStream(avatar);
                                bitmap = BitmapFactory.decodeStream(inputStream);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            color = cursor.getString(cursor.getColumnIndex("color"));
                            pseudonim = cursor.getString(cursor.getColumnIndex("pseudonim"));
                            changed = cursor.getString(cursor.getColumnIndex("changed"));
                            backgroundColor = cursor.getString(cursor.getColumnIndex("backgroundColor"));

                            JsonObjects.Users users = new JsonObjects.Users(id, name, surname, email, bitmap, color, pseudonim, changed, backgroundColor);
                            usersArrayList.add(users);
                        }
                    } catch (Exception e) {

                    }
                    if (!emitter.isDisposed())
                        emitter.onComplete();
                }
            });
        } else if (type.equals("groups")) {
            return Completable.create(new CompletableOnSubscribe() {
                @Override
                public void subscribe(CompletableEmitter emitter) throws Exception {
                    try {
                        DB db = new DB(getApplicationContext());
                        ArrayList<Table> usersArrayList = new ArrayList<>();
                            groupsAdapter = new GroupsAdapter(usersArrayList, MainActivity.this,MainActivity.this);
                            groupsList=(RecyclerView) findViewById(R.id.showGroupsList);
                            progressBar=(ProgressBar)findViewById(R.id.progressBar);
                            SQLiteDatabase db2= db.getReadableDatabase();
                            Cursor cursor = db.getTables(db2);
                            String name;

                            while (cursor.moveToNext()){

                                name=cursor.getString(cursor.getColumnIndex("name"));

                                Table table = new Table(name);
                                usersArrayList.add(table);

                            }



                    } catch (Exception e) {

                    }
                    if (!emitter.isDisposed())
                        emitter.onComplete();
                }
            });
        }

        return null;
    }

    CompletableObserver getCompletableObserver(String type) {
        if (type.equals("users")) {


            return new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {
                    listView.setVisibility(View.GONE);
                }

                @Override
                public void onComplete() {
//                    progressBar.setVisibility(View.GONE);

                    groupsList.setVisibility(View.GONE);
                    selectUser.setVisibility(View.GONE);
                    listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    listView.setAdapter(UserAdapter);

                    listView.setVisibility(View.VISIBLE);

                }

                @Override
                public void onError(Throwable e) {
                }
            };
        } else if (type.equals("groups")) {
            return new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onComplete() {
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    groupsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    groupsList.setAdapter(groupsAdapter);
                    groupsList.setVisibility(View.VISIBLE);

                }

                @Override
                public void onError(Throwable e) {
                }
            };
        }


        return null;

    }
    @Component(modules = JsonObjects.class)
    public interface component{
        void inject(MainActivity mainActivity);

    }
}