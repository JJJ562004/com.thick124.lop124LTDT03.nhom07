package com.thicuoiky124.lttd03.nhom07.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.albumia.CLasses.Notification;
import com.example.albumia.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Notification> {
    private Context context;
    private int resource;
    ArrayList<Notification> arrContact;
    public CustomAdapter(Context context, int resource, ArrayList<Notification> arrContact) {
        super(context, resource, arrContact);
        this.context = context;
        this.resource = resource;
        this.arrContact = arrContact;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_row_list_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTbao = (TextView) convertView.findViewById(R.id.tvTBao);
            viewHolder.tvChitiet = (TextView) convertView.findViewById(R.id.tvChitiet);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Notification contact = arrContact.get(position);
        viewHolder.tvTbao.setText(contact.getDescription());
        viewHolder.tvChitiet.setText(contact.getTitle());
        return convertView;
    }
    public class ViewHolder {
        public TextView tvTbao;
        public TextView tvChitiet;
        TextView tvName, tvNumberPhone, tvAvatar;
    }}