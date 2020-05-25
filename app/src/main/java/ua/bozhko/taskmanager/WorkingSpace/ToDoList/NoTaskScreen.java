package ua.bozhko.taskmanager.WorkingSpace.ToDoList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.bozhko.taskmanager.R;

public class NoTaskScreen extends Fragment {
    @BindView(R.id.addList) ImageButton imageButton;
    private GeneralList generalList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist_no_task, container, false);
        ButterKnife.bind(this, view);

        generalList = new GeneralList();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodayGeneralClass.fTrans = getFragmentManager().beginTransaction();
                TodayGeneralClass.fTrans.replace(R.id.frameLayout, generalList).commit();
            }
        });
        return view;
    }
}
