package com.threel.apptest;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SelectAdapter  extends  RecyclerView.Adapter<SelectAdapter.ViewHolder> {
    Context context;
    List<JsonObjects.Users> users;
    public SelectAdapter(List<JsonObjects.Users> getDataAdapter, Context context) {

        this.users = getDataAdapter;


        this.context = context;
    }
    @Override
    public SelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selectuser, parent, false);

        SelectAdapter.ViewHolder viewHolder = new SelectAdapter.ViewHolder(view);


        return viewHolder;
    }
    @Override
    public void onBindViewHolder(SelectAdapter.ViewHolder holder, final int position) {
        JsonObjects.Users getDataAdapter1 = users.get(position);
        holder.name.setText(getDataAdapter1.getName().toString());
        holder.surname.setText(getDataAdapter1.getSurname().toString());
        holder.cardView1.setBackgroundColor(Color.parseColor(getDataAdapter1.getBackgroundColor()));
        holder.cardView1.setOnClickListener(new View.OnClickListener() {
            MainActivity main = new MainActivity();
            selectedItems selectedItems = new selectedItems();
            @Override
            public void onClick(View view) {
                if (Color.parseColor(String.valueOf(main.getSelectedItems1().get(position).getBackgroundColor())) == Color.parseColor("#ffffff")) {
                        view.setBackgroundColor(Color.parseColor("#006000"));
                        selectedItems = new selectedItems(String.valueOf(position + 1), "#006000");
                        main.getSelectedItems1().set(position, selectedItems);

                    } else if (Color.parseColor(String.valueOf(main.getSelectedItems1().get(position).getBackgroundColor())) == Color.parseColor("#006000"))  {
                        view.setBackgroundColor(Color.parseColor("#ffffff"));
                        selectedItems = new selectedItems(String.valueOf(position + 1), "#ffffff");
                        main.getSelectedItems1().set(position, selectedItems);

                    }
            }
        });
    }

    @Override
    public int getItemCount() {

        return users.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, surname;
        LinearLayout cardView1;


        public ViewHolder(View itemView) {

            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name1);
            surname = (TextView) itemView.findViewById(R.id.surname1);
            cardView1 = (LinearLayout) itemView.findViewById(R.id.cardview1);


        }

    }
}


