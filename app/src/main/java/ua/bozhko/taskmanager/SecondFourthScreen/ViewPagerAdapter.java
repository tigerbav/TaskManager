package ua.bozhko.taskmanager.SecondFourthScreen;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] childFragments;

    ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        childFragments = new Fragment[]{
                new ToDoListScreen(),
                new GoalsScreen(),
                new HabitsScreen()
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

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        String title = getItem(position).getClass().getName();
//        return title.subSequence(title.lastIndexOf(".") + 1, title.length());
//    }
}
