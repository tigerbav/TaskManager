package ua.bozhko.taskmanager.WorkingSpace.ToDoList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ua.bozhko.taskmanager.DataBaseFirebase;
import ua.bozhko.taskmanager.R;

public class TodayGeneralClass extends Fragment {
    static FragmentTransaction fTrans;
    private NoTaskScreen noTaskScreen;
    private GeneralList generalList;
    private DataBaseFirebase dataBaseFirebase;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_general_class, container, false);

        dataBaseFirebase = DataBaseFirebase.createOrReturn();
        noTaskScreen = new NoTaskScreen();
        generalList = new GeneralList();

        fTrans = getFragmentManager().beginTransaction();
        dataBaseFirebase.takeFlag(fTrans, noTaskScreen, generalList, getContext());
        return view;
    }
}
