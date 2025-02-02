package com.thicuoiky124.lttd03.nhom07;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;




import com.example.albumia.R;
import com.thicuoiky124.lttd03.nhom07.Adapters.ViewPageAdapter;
import com.thicuoiky124.lttd03.nhom07.Camera.CameraFragment;
import com.thicuoiky124.lttd03.nhom07.Camera.them_anh;
import com.thicuoiky124.lttd03.nhom07.Customize.CustomViewPager;
import com.thicuoiky124.lttd03.nhom07.Fragments.ThemthongtinanhActivity;
import com.thicuoiky124.lttd03.nhom07.Fragments.ThongbaoActivity;
import com.thicuoiky124.lttd03.nhom07.Fragments.about;
import com.thicuoiky124.lttd03.nhom07.Fragments.bottomNav;
import com.thicuoiky124.lttd03.nhom07.Fragments.comment_section;
import com.thicuoiky124.lttd03.nhom07.Fragments.login;
import com.thicuoiky124.lttd03.nhom07.Fragments.signup;
import com.thicuoiky124.lttd03.nhom07.Fragments.uploadedimage;

public class MainActivity extends AppCompatActivity {
    public CustomViewPager customViewPager;
    //public ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Add test database record
//        FireBaseTest firebase = new FireBaseTest();
//        firebase.addUser("John Doe", "john.doe@example.com");
//        firebase.readUsers();

        customViewPager = findViewById(R.id.main_viewpager);
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new login()); //0
        adapter.addFragment(new signup()); //1
        adapter.addFragment(new bottomNav()); //2
        adapter.addFragment(new comment_section()); //3
        adapter.addFragment(new CameraFragment()); //4
        adapter.addFragment(new them_anh()); //5
        adapter.addFragment(new uploadedimage()); //6
        adapter.addFragment(new ThemthongtinanhActivity()); //7
        adapter.addFragment(new ThongbaoActivity()); //8
        adapter.addFragment((new about())); //9

        customViewPager.setAdapter(adapter);
        customViewPager.setSwipeEnabled(false);
        customViewPager.setCurrentItem(2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void switchToFragment(int position) {
        customViewPager.setCurrentItem(position);
    }
}
