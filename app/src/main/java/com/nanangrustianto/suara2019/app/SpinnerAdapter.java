package com.nanangrustianto.suara2019.app;

/**
 * Created by Nanang on 23/10/2017.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import  com.nanangrustianto.suara2019.R;

import com.nanangrustianto.suara2019.app.DataObject.DataObject;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter{
    private LayoutInflater layoutInflater;
    private List<DataObject> listData;
    private Context context;
    public SpinnerAdapter(Context context, List<DataObject> listData) {
        this.context = context;
        layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listData = listData;
    }
    @Override
    public int getCount() {
        return listData.size();
    }
    @Override
    public Object getItem(int position) {
        return (DataObject)listData.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder spinnerHolder;
        if(convertView == null){
            spinnerHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.spinner_list, parent, false);
            spinnerHolder.spinnerItemList = (TextView)convertView.findViewById(R.id.spinner_list_item);
            convertView.setTag(spinnerHolder);
        }else{
            spinnerHolder = (ViewHolder)convertView.getTag();
        }
        spinnerHolder.spinnerItemList.setText(listData.get(position).getName());
        return convertView;
    }
    class ViewHolder{
        TextView spinnerItemList;
    }
}

