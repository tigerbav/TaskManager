package ua.bozhko.taskmanager.WorkingSpace.ToDoList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import ua.bozhko.taskmanager.Constants;
import ua.bozhko.taskmanager.FifthSeventhScreen.Login;
import ua.bozhko.taskmanager.FirstScreen.Logo;
import ua.bozhko.taskmanager.R;
import ua.bozhko.taskmanager.SecondFourthScreen.GeneralScreenSFS;

public class Month extends Fragment {
    private Button button;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist_month, container, false);
        button = view.findViewById(R.id.exitBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }
}
