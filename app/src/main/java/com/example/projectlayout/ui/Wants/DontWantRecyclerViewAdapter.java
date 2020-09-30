package com.example.projectlayout.ui.Wants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectlayout.R;

import java.util.ArrayList;

public class DontWantRecyclerViewAdapter extends RecyclerView.Adapter<DontWantRecyclerViewAdapter.ViewHolder> {

    private ArrayList<want> item;
    private LayoutInflater mInflater;
    private RecycleViewInterface recycleViewInterface;

    public interface RecycleViewInterface {
        void onItemClick(int position);
    }




    public DontWantRecyclerViewAdapter(ArrayList<want> data, Context context, RecycleViewInterface recycleViewInterface) {
        this.item = data;
        this.mInflater = LayoutInflater.from(context);
        this.recycleViewInterface = recycleViewInterface;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = mInflater.inflate(R.layout.rowlayout,parent,false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        want task = item.get(position);

        holder.txtName.setText(task.name);
        String set = String.valueOf(task.listOrder+1)+ ".";
        holder.number.setText(set);
    }



    @Override
    public int getItemCount() {
        return item.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView number;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.label);
            number = itemView.findViewById(R.id.number);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recycleViewInterface.onItemClick(getAdapterPosition());
                }
            });
        }

    }



}
