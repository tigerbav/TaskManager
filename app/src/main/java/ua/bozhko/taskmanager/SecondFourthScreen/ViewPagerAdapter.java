package ua.bozhko.taskmanager.SecondFourthScreen;
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


    //определение позиции фрагмента
    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    //возвращает размер childFragments
    @Override
    public int getCount() {
        return childFragments.length;
    }

}
