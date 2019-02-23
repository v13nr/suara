package com.nanangrustianto.suara2019;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nanang on 15/10/2017.
 */

public class Menu3Adapter extends BaseAdapter{
    Context context;
    ArrayList<HashMap<String, String>> arrayList;

    public Menu3Adapter(Context context, ArrayList<HashMap<String, String>> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_menu3, null);

        ImageView img = (ImageView)convertView.findViewById(R.id.row_img);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.row_txtTitle);

        Glide.with(context).load(arrayList.get(position).get("image"))
                .thumbnail(0.5f)
                .override(200,200)
                .crossFade()
                .into(img);

        txtTitle.setText(arrayList.get(position).get("name"));

        return convertView;
    }
}
