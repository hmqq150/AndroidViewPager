package com.example.mac_jack.myapplication.Util;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.mac_jack.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac-jack on 2018/9/28.
 */

public class DataAdapter extends BaseAdapter {

    private List<Data> dataList;
    private LayoutInflater inflater;

    public DataAdapter(List<Data> dataList, Context context) {
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList!=null?dataList.size():0;
    }




    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Data fruit = (Data) getItem(position);
        View view = inflater.inflate(R.layout.listviewitem, null);
        MyImageView dataImage = (MyImageView) view.findViewById(R.id.listviewItemimg);
         TextView dataText = (TextView) view.findViewById(R.id.listviewItemtext);
        //dataImage.setImageResource(Integer.parseInt(((Data) getItem(position)).getThumb_url()));
        dataImage.setImageURL(((Data) getItem(position)).getThumb_url());
        dataText.setText(((Data) getItem(position)).getTitle());
        return view;
    }
}
