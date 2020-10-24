package com.example.childdraw.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.childdraw.Interface.ViewOnClick;
import com.example.childdraw.R;

public class ToolsViewHolder extends RecyclerView.ViewHolder {
    public ImageView icone;
    public TextView name;

    private ViewOnClick viewOnClick;

    public void setViewOnClick(ViewOnClick viewOnClick){
        this.viewOnClick = viewOnClick;
    }

    public ToolsViewHolder(@NonNull View itemView) {
        super(itemView);

        icone = itemView.findViewById(R.id.tools_icone);
        name = itemView.findViewById(R.id.tools_name);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewOnClick.onClick(getAdapterPosition());
            }
        });

    }
}
