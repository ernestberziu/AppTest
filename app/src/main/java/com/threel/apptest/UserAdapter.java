package com.threel.apptest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class UserAdapter extends  RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    List<JsonObjects.Users> users;


    public UserAdapter(List<JsonObjects.Users> getDataAdapter, Context context) {
        this.users = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userslist, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        JsonObjects.Users getDataAdapter1 = users.get(position);
        holder.name.setText(getDataAdapter1.getName().toString());
        holder.surname.setText(getDataAdapter1.getSurname().toString());
        holder.email.setText(getDataAdapter1.getEmail().toString());
        holder.image.setImageBitmap(getDataAdapter1.getImage());
        holder.pseudonim.setText(getDataAdapter1.getPseudonim());
        holder.color.setBackgroundColor(Color.parseColor(getDataAdapter1.getColor()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB db = new DB(context);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Button groups = activity.findViewById(R.id.groupList);
                Button users = activity.findViewById(R.id.userList);
               TextView textView = activity.findViewById(R.id.textView);
                FloatingActionButton save = activity.findViewById(R.id.save);
                FloatingActionButton fab = activity.findViewById(R.id.fab);

                MainActivity mainActivity = new MainActivity();
                SQLiteDatabase db7=db.getReadableDatabase();
                Cursor cursor2 = db.getId(db7);
                String id;
                while (cursor2.moveToNext()) {
                    id = cursor2.getString(cursor2.getColumnIndex("id"));
                    ID selecteded = new ID(id);
                    mainActivity.getIds().add(selecteded);
                }
                Demo.username=mainActivity.getIds().get(position).getId();
                Fragment edit = new PlaceholderFragment();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.list, edit,null);
                transaction.commit();
                textView.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                save.setVisibility(View.GONE);

            }
        });


    }

    @Override
    public int getItemCount() {

        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, surname, email, pseudonim;
        FrameLayout color;
        LinearLayout cardView;
        ImageView image;


        public ViewHolder(View itemView) {

            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            surname = (TextView) itemView.findViewById(R.id.surname);
            email = (TextView) itemView.findViewById(R.id.email);
            image = (ImageView) itemView.findViewById(R.id.avatar);
            color = (FrameLayout) itemView.findViewById(R.id.color);
            pseudonim = (TextView) itemView.findViewById(R.id.pseudonim);
            cardView = (LinearLayout) itemView.findViewById(R.id.cardview);
        }
    }
}


