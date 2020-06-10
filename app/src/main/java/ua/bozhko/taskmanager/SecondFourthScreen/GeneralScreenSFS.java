package ua.bozhko.taskmanager.SecondFourthScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import ua.bozhko.taskmanager.Constants;
import ua.bozhko.taskmanager.FifthSeventhScreen.Login;
import ua.bozhko.taskmanager.R;

public class GeneralScreenSFS extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button startBtn;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_sfs);

        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);
        startBtn = findViewById(R.id.getStartedBtn);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //вход произошел первый раз
                editor.putBoolean(Constants.FIRST_LOAD, true);
                editor.apply();
                Intent intent = new Intent(GeneralScreenSFS.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        //опеределение вьюПейджера, который содержит в себе 3 фрагмента
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorWhite));
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < 3; i++) {
            if(i == 0)
                tabLayout.getTabAt(i).setIcon(R.drawable.tab_layout_active);
            else
                tabLayout.getTabAt(i).setIcon(R.drawable.tab_layout_not_active);
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 3; i++) {
                    if(position == i)
                        tabLayout.getTabAt(i).setIcon(R.drawable.tab_layout_active);
                    else
                        tabLayout.getTabAt(i).setIcon(R.drawable.tab_layout_not_active);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
