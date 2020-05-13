package ua.bozhko.taskmanager.WorkingSpace.ToDoList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class VPAdapterToDoList extends FragmentPagerAdapter {
    private Fragment[] childFragments;

    VPAdapterToDoList(FragmentManager fm) {
        super(fm);
        childFragments = new Fragment[]{
                new TodayGeneralClass(),
                new Month()
        };
    }

    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    @Override
    public int getCount() {
        return childFragments.length;
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Today" : "Month";
    }
}