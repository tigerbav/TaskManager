package ua.bozhko.taskmanager.WorkingSpace.ToDoList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.bozhko.taskmanager.R;

public class MainToDoList extends Fragment {
    @BindView(R.id.vpadapter) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist_after_login, container, false);
        ButterKnife.bind(this, view);
        viewPager.setAdapter(new VPAdapterToDoList(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        for (int tabIndex = 0; tabIndex < tabLayout.getTabCount(); tabIndex++) {
            TextView tabTextView = (TextView)(((LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(tabIndex)).getChildAt(1));
            tabTextView.setAllCaps(false);
        }
        return view;
    }
}
