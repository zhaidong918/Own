package com.smiledon.own.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;
import com.smiledon.own.service.model.CookMenu;

import java.util.List;


/**
 * @author East Chak
 * @date 2018/1/16 11:54
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    List<CookMenu> menuList;

    public ImageAdapter() {

    }

    public void addImageList(List<CookMenu> menuList) {
        this.menuList = menuList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(AppApplication.getInstance()).inflate(R.layout.item_image_layout, parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        CookMenu menu = menuList.get(position);
        Glide.with(AppApplication.getInstance())
                .load(menu.thumbnail)
                .into(holder.itemIv);
    }

    @Override
    public int getItemCount()
    {
        return menuList == null ? 0 : menuList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView itemIv;

        public MyViewHolder(View view)
        {
            super(view);
            itemIv =  view.findViewById(R.id.item_iv);
        }
    }
}


