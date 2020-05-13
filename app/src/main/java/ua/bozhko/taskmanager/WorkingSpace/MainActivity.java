package ua.bozhko.taskmanager.WorkingSpace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import ua.bozhko.taskmanager.R;
import ua.bozhko.taskmanager.WorkingSpace.Goals.MainGoals;
import ua.bozhko.taskmanager.WorkingSpace.Habits.MainHabits;
import ua.bozhko.taskmanager.WorkingSpace.Profile.MainProfile;
import ua.bozhko.taskmanager.WorkingSpace.ToDoList.MainToDoList;

public class MainActivity extends AppCompatActivity {
    private FragmentTransaction fragmentTransaction;
    private ImageButton toDoList, profile, habits, goals;
    private MainToDoList mainToDoList;
    private MainGoals mainGoals;
    private MainProfile mainProfile;
    private MainHabits mainHabits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toDoList = findViewById(R.id.toDoList);
        profile = findViewById(R.id.profile);
        habits = findViewById(R.id.habits);
        goals = findViewById(R.id.goals);

        mainToDoList = new MainToDoList();
        mainProfile = new MainProfile();
        mainHabits = new MainHabits();
        mainGoals = new MainGoals();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.relativeLayout, mainToDoList).commit();

        toDoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.relativeLayout, mainToDoList).commit();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.relativeLayout, mainProfile).commit();
            }
        });

        habits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.relativeLayout, mainHabits).commit();
            }
        });

        goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.relativeLayout, mainGoals).commit();
            }
        });
    }
}
