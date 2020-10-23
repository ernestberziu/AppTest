package com.threel.apptest;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GroupsAdapter extends  RecyclerView.Adapter<GroupsAdapter.ViewHolder> {
    Context context;
    List<Table> tables;
    Activity activity1;
    SelectAdapter SelectAdapter;
    ProgressBar progressBar;
    RecyclerView selectUser;
    public GroupsAdapter(List<Table> getDataAdapter, Activity context, Activity activity1) {
        this.tables = getDataAdapter;
        this.context =  context;
        this.activity1 =  activity1;
    }

    @Override
    public GroupsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groupslist, parent, false);
        GroupsAdapter.ViewHolder viewHolder = new GroupsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupsAdapter.ViewHolder holder, final int position) {

        Table getDataAdapter1 = tables.get(position);

        holder.name.setText(getDataAdapter1.getName().toString());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Demo.groupclicked=true;
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                ProgressBar progressBar=activity.findViewById(R.id.progressBar);
                RecyclerView selectUser = activity.findViewById(R.id.selectUsers);
                RecyclerView groupList = activity.findViewById(R.id.showGroupsList);
                FloatingActionButton fab = activity.findViewById(R.id.fab);
                FloatingActionButton save = activity.findViewById(R.id.save);
                progressBar.setVisibility(View.VISIBLE);
                selectUser.setVisibility(View.GONE);
                groupList.setVisibility(View.GONE);
                Demo.setGroup_name(tables.get(position).getName());
                MainActivity mainActivity = new MainActivity();
                DB db = new DB(context);
                GroupsDB grdb = new GroupsDB(context);
                SQLiteDatabase db4= db.getWritableDatabase();
                SQLiteDatabase db2 = grdb.getReadableDatabase();
                try {
                    String rawQuery = "Select id, backgroundColor from "+Demo.getGroup_name();
                    Cursor c = db2.rawQuery(rawQuery, null);
                    c.moveToFirst();
                    try {
                        do {
                            mainActivity.getSelectedItems1().set(c.getPosition(),new selectedItems(c.getString(0),c.getString(1)));
                        } while (c.moveToNext());
                    } finally {
                        c.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for(int k=0; k<mainActivity.getSelectedItems1().size();k++){ db.updateUsersList(db4,mainActivity.getSelectedItems1().get(k).getBackgroundColor(),mainActivity.getSelectedItems1().get(k).getIndex()); }
                try {
                    getCompletableObservable()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getCompletableObserver());
                } catch (Exception e) {
                    Toast.makeText(context,"Please add a group",Toast.LENGTH_SHORT).show();
                }
                selectUser.setVisibility(View.GONE);
                groupList.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout cardview;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.groupName);
            cardview=(LinearLayout)itemView.findViewById(R.id.cardview2);
        }
    }
    private Completable getCompletableObservable(){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {

try {
    DB db = new DB(activity1.getApplicationContext());

    selectUser = (RecyclerView) activity1.findViewById(R.id.selectUsers);
    ArrayList<JsonObjects.Users> usersArrayList = new ArrayList<>();
    progressBar = (ProgressBar) activity1.findViewById(R.id.progressBar);
    SelectAdapter = new SelectAdapter(usersArrayList, activity1.getApplicationContext());
    SQLiteDatabase db2 = db.getReadableDatabase();
    Cursor cursor = db.getUserData(db2);
    String id, name, surname, email, color, pseudonim;
    byte[] avatar;
    String changed, backgroundColor;
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

        JsonObjects.Users users = new JsonObjects.Users(id, name, surname, email, bitmap, color, pseudonim, changed, backgroundColor);
        usersArrayList.add(users);


    }
} catch (Exception e) {
    e.printStackTrace();
}
                if (!emitter.isDisposed())
                    emitter.onComplete();
            }}
    );}
    CompletableObserver getCompletableObserver(){
        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onComplete() {
                progressBar=activity1.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
                selectUser.setLayoutManager(new LinearLayoutManager(context));
                selectUser.setAdapter(SelectAdapter);
                selectUser.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError(Throwable e) {
            }
        };
    }
}


