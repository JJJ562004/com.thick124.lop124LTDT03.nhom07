package com.thicuoiky124.lttd03.nhom07.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.albumia.CLasses.chonanh;
import com.example.albumia.R;

import java.util.ArrayList;


public class AdapterGridChonAnh extends ArrayAdapter<chonanh> {
    Context context;
    int IdLayout;
    ArrayList<chonanh> mylist;

    public AdapterGridChonAnh(Context context, int idLayout, ArrayList<chonanh> mylist) {
        super(context, idLayout,mylist);
        this.context = context;
        IdLayout = idLayout;
        this.mylist = mylist;
    }

    // gọi hàm getView đẻ Adapter để lấy dữ liệu và hiển thị

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater myflactor = LayoutInflater.from(context);
            convertView = myflactor.inflate(R.layout.grvlayout, parent, false);
        }
//        LayoutInflater myflactor = LayoutInflater.from(context);
//        //tao view
//        convertView = myflactor.inflate(IdLayout, null);
        //lay 1 phan tu trong bang
        chonanh myitem = mylist.get(position);
        ImageView img_item = convertView.findViewById(R.id.imageView);
        img_item.setImageResource(myitem.getImage());
        return convertView;
    }
}