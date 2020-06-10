package ua.bozhko.taskmanager.WorkingSpace.ToDoList;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ua.bozhko.taskmanager.Constants;
import ua.bozhko.taskmanager.DataBaseFirebase;
import ua.bozhko.taskmanager.R;

public class GeneralList extends Fragment implements View.OnClickListener {
    private LinearLayout linearLayout;
    private TextView add_new_list;

    private int[] buttonName = {R.string.urgently, R.string.important, R.string.desirable};
    private ArrayList<Button> allButtons = new ArrayList<>();
    public static int ID = 0;
    private DataBaseFirebase dataBaseFirebase;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist_first_list, container, false);
        linearLayout = view.findViewById(R.id.listOfTasks);
        add_new_list = view.findViewById(R.id.add_new_list);

        dataBaseFirebase = DataBaseFirebase.createOrReturn();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 15, 15, 15);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        if(!sharedPreferences.getString(Constants.NOTIFICATION_MAIN_TEXT, "").equals(""))
        {
            ListOfWorking listOfWorking = new ListOfWorking();

            Bundle bundle = new Bundle();
            bundle.putString("BundleButton", sharedPreferences.getString(Constants.NOTIFICATION_MAIN_TEXT, ""));
            listOfWorking.setArguments(bundle);

            TodayGeneralClass.fTrans = getFragmentManager().beginTransaction();
            TodayGeneralClass.fTrans.replace(R.id.frameLayout, listOfWorking).commit();
        }
        //конец обработки нотификации))
        for (int value : buttonName) {
            createButton("", value, layoutParams);
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
                    createButton(value, 0, layoutPar);
                    dataBaseFirebase.writeToDBGeneralList(value);

                    Set<String> stringSet = sharedPreferences.getStringSet(Constants.GENERAL_TASK_FOR_LOCAL, null);
                    if(stringSet == null){
                        stringSet = new HashSet<>();
                    }
                    stringSet.add(value);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet(Constants.GENERAL_TASK_FOR_LOCAL, stringSet).apply();
                }
            }
        });

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            dialog.dismiss();
        });

        alert.show();
    }

    private void createButton(String text, int text2, ViewGroup.LayoutParams layoutParams){
        Button button = new Button(getContext(), null, R.style.buttons_todolist_general_list, R.style.buttons_todolist_general_list);
        if(!text.equals(""))
            button.setText(text);
        else
            button.setText(text2);
        button.setId(++ID);
        button.setLayoutParams(layoutParams);
        linearLayout.addView(button);
        allButtons.add(button);
        button.setOnClickListener(GeneralList.this);
    }

    @Override
    public void onClick(View view) {
        for(Button clicked : allButtons){
            if(clicked.getId() == view.getId()){
                ListOfWorking listOfWorking = new ListOfWorking();
            //стягивание текста на след єкран
                Bundle bundle = new Bundle();
                bundle.putString("BundleButton", clicked.getText().toString());
                listOfWorking.setArguments(bundle);

                TodayGeneralClass.fTrans = getFragmentManager().beginTransaction();
                TodayGeneralClass.fTrans.replace(R.id.frameLayout, listOfWorking).commit();

                break;
            }
        }
    }
}
