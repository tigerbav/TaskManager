package ua.bozhko.taskmanager.WorkingSpace.ToDoList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ua.bozhko.taskmanager.DataBaseFirebase;
import ua.bozhko.taskmanager.R;

public class GeneralList extends Fragment implements View.OnClickListener {
    private LinearLayout linearLayout;
    private TextView add_new_list;

    private int[] buttonName = {R.string.urgently, R.string.important, R.string.desirable};
    private ArrayList<Button> allButtons = new ArrayList<>();
    public static int ID = 0;
    private DataBaseFirebase dataBaseFirebase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist_first_list, container, false);
        linearLayout = view.findViewById(R.id.listOfTasks);
        add_new_list = view.findViewById(R.id.add_new_list);

        dataBaseFirebase = DataBaseFirebase.createOrReturn();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 15, 15, 15);


        for (int value : buttonName) {
            Button button = new Button(getContext(), null, R.style.buttons_todolist_general_list, R.style.buttons_todolist_general_list);
            button.setText(value);
            button.setId(++ID);
            button.setLayoutParams(layoutParams);
            linearLayout.addView(button);
            allButtons.add(button);
            button.setOnClickListener(GeneralList.this);
        }
        dataBaseFirebase.readFromDBGeneralList(getContext(), layoutParams, linearLayout, allButtons, GeneralList.this);

        add_new_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatingAlertDialog(layoutParams);
            }
        });
        return view;
    }

    private void creatingAlertDialog( LinearLayout.LayoutParams layoutPar){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(), R.style.my_custom_alert_dialog);

        alert.setTitle("Adding new list");
        alert.setCancelable(false);

        final EditText input = new EditText(getContext());
        input.getBackground().clearColorFilter();
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if(!value.equals(""))
                {
                    Button button = new Button(getContext(), null, R.style.buttons_todolist_general_list, R.style.buttons_todolist_general_list);
                    button.setText(value);
                    button.setId(++ID);
                    button.setLayoutParams(layoutPar);
                    linearLayout.addView(button);
                    allButtons.add(button);
                    button.setOnClickListener(GeneralList.this);
                    dataBaseFirebase.writeToDBGeneralList(value);
                }
            }
        });

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            // Canceled.
        });

        alert.show();
    }

    @Override
    public void onClick(View view) {
        for(Button clicked : allButtons){
            if(clicked.getId() == view.getId()){
                ListOfWorking listOfWorking = new ListOfWorking();

                Bundle bundle = new Bundle();
                bundle.putString("BundleButton", clicked.getText().toString());
                listOfWorking.setArguments(bundle);

                TodayGeneralClass.fTrans = getFragmentManager().beginTransaction();
                TodayGeneralClass.fTrans.replace(R.id.frameLayout, listOfWorking);
                //TodayGeneralClass.fTrans.addToBackStack(null);
                TodayGeneralClass.fTrans.commit();

                break;
            }
        }
    }
}
