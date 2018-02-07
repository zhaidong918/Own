package com.smiledon.own.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;
import com.smiledon.own.service.model.CookMenu;
import com.smiledon.own.widgets.DemoCity;

import java.util.List;


/**
 * @author East Chak
 * @date 2018/1/16 11:54
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {

    List<DemoCity> demoCityList;

    public CityAdapter() {

    }

    public void addCityList(List<DemoCity> demoCityList) {
        this.demoCityList = demoCityList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(AppApplication.getInstance()).inflate(R.layout.item_name_layout, parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        DemoCity demoCity = demoCityList.get(position);
        holder.itemTv.setText(demoCity.city);

    }

    @Override
    public int getItemCount()
    {
        return demoCityList == null ? 0 : demoCityList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView itemTv;

        public MyViewHolder(View view)
        {
            super(view);
            itemTv =  view.findViewById(R.id.item_tv);
        }
    }
}


