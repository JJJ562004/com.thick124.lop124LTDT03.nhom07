package com.thicuoiky124.lttd03.nhom07.Fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.albumia.Adapters.CustomAdapter;
import com.example.albumia.CLasses.Notification;
import com.example.albumia.MainActivity;
import com.example.albumia.R;

import java.util.ArrayList;

public class ThongbaoActivity extends Fragment {
        Notification contact1 = new Notification("Thông báo 1","@User đã thích hình ảnh của bạn: @Tên hình");
        Notification contact2 = new Notification("Thông báo 2","@User đã theo dõi bạn");
        Notification contact3 = new Notification("Thông báo 3","@User đã thích hình ảnh của bạn: @Tên hình");
        Notification contact4 = new Notification("Thông báo 4","@User đã theo dõi bạn");
        Notification contact5 = new Notification("Thông báo 5","@User đã thích hình ảnh của bạn: @Tên hình");
        Notification contact6 = new Notification("Thông báo 6","@User đã theo dõi bạn");
        Notification contact7 = new Notification("Thông báo 7","@User đã thích hình ảnh của bạn: @Tên hình");

        ListView lv;
        CustomAdapter customadapter;
        ArrayList<Notification> listThongBao;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.activity_thongbao, container, false);

            Button tb = view.findViewById(R.id.thongBao_button17);
            tb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).switchToFragment(2);
                }
            });
            lv = view.findViewById(R.id.thongBao_lv1);
            listThongBao = new ArrayList<>();
            listThongBao.add(contact1);
            listThongBao.add(contact2);
            listThongBao.add(contact3);
            listThongBao.add(contact4);
            listThongBao.add(contact5);
            listThongBao.add(contact6);
            listThongBao.add(contact7);
            customadapter = new CustomAdapter(getContext(), R.layout.activity_row_list_view, listThongBao);
            lv.setAdapter(customadapter);
            return view;
        }

        }

