package com.example.sejjoh.gsls.viewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sejjoh.gsls.Interface.ItemClickListener;
import com.example.sejjoh.gsls.R;

public class ViewHolderService extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView post_name;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public ViewHolderService(@NonNull View itemView) {
        super(itemView);

        post_name=(TextView)itemView.findViewById(R.id.service_title);
        imageView=itemView.findViewById(R.id.service_image);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}

