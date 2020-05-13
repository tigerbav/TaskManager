package ua.bozhko.taskmanager.WorkingSpace.ToDoList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.bozhko.taskmanager.DataBaseFirebase;
import ua.bozhko.taskmanager.R;

public class ListOfWorking extends Fragment implements ICallBack.IDay {
    @BindView(R.id.generalTask) LinearLayout generalTask;
    @BindView(R.id.todolist) LinearLayout toDoList;
    @BindView(R.id.add_new_list_2) TextView textView;

    private DialogSetDay dialogSetDay;
    private DataBaseFirebase dataBaseFirebase;
    private String generalList;

    public static List<String> listOfToDoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist_list_working, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = this.getArguments();
        dialogSetDay = new DialogSetDay();

        dataBaseFirebase = DataBaseFirebase.createOrReturn();

        if(bundle != null)
        {
            generalList = bundle.getString("BundleButton");
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(15, 15, 15, 20);

            Button button = new Button(getContext(), null, R.style.buttons_todolist_general_list, R.style.buttons_todolist_general_list);
            button.setText(generalList);
            generalTask.addView(button);
            button.setLayoutParams(layoutParams);
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatingAlertDialog();
            }
        });
        dataBaseFirebase.readFromDBMainList(generalList, getContext(), toDoList, dialogSetDay, getFragmentManager());
        return view;
    }


    private void creatingAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(), R.style.my_custom_alert_dialog);

        alert.setTitle("Adding new list");
        alert.setCancelable(false);

        final EditText input = new EditText(getContext());
        input.getBackground().clearColorFilter();
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if(!value.equals("") && !repeatList(value))
                {
                    createAList(value);
                }
            }
        });

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
        });

        alert.show();
    }

    private void createAList(String value) {
        LinearLayout.LayoutParams layoutColumn = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutColumn.setMargins(0, 30, 15, 0);

        LinearLayout threeColumn = new LinearLayout(getContext());
        threeColumn.setOrientation(LinearLayout.HORIZONTAL);
        threeColumn.setGravity(Gravity.CENTER);
        threeColumn.setLayoutParams(layoutColumn);

        LinearLayout.LayoutParams forWeight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        forWeight.weight = 1;

        int id = 1;
        CheckBox checkBox = new CheckBox(getContext());
        checkBox.setText(value);
        checkBox.setId(id);
        checkBox.setLayoutParams(forWeight);
        threeColumn.addView(checkBox);


        TextView fromToClock = new TextView(getContext());
        fromToClock.setTextSize(16f);
        fromToClock.setGravity(Gravity.END);
        fromToClock.setPadding(0,0,15,0);
        fromToClock.setLayoutParams(forWeight);
        threeColumn.addView(fromToClock);

        ImageView clock = new ImageView(getContext());
        clock.setImageResource(R.drawable.element_clock);
        clock.setScaleType(ImageView.ScaleType.FIT_END);
        threeColumn.addView(clock);
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSetDay.setCallBack(ListOfWorking.this, generalList, value, fromToClock);
                dialogSetDay.show(getFragmentManager(), "DialogSetDay");
            }
        });

        toDoList.addView(threeColumn);

        ImageView line = new ImageView(getContext());
        line.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.line));
        toDoList.addView(line);
        line.setScaleType(ImageView.ScaleType.FIT_XY);

        ViewGroup.MarginLayoutParams marginLayoutParamsImage = (ViewGroup.MarginLayoutParams) line.getLayoutParams();
        marginLayoutParamsImage.setMarginEnd(15);
        marginLayoutParamsImage.setMarginStart(15);
        line.setLayoutParams(marginLayoutParamsImage);

        dataBaseFirebase.writeToDBMainList(generalList, value);
    }

    private boolean repeatList(String value){
       if(listOfToDoList != null) {
           for(String tempList : listOfToDoList){
               if(value.equals(tempList))
                   return true;
           }
       }
        return false;
    }

    @Override
    public void setDayDialogFragment(String generalList, String mainList,
                                     int hoursBefore, int minutesBefore,
                                     int hoursAfter, int minutesAfter,
                                     TextView fromToClock,
                                     String repeat, String sound,
                                     boolean holdOn, boolean[] daysOfWeek) {
        String tempHoursBefore = hoursBefore > 9 ? String.valueOf(hoursBefore) : "0" + hoursBefore;
        String tempHoursAfter = hoursAfter > 9 ? String.valueOf(hoursAfter) : "0" + hoursAfter;
        String tempMinutesBefore = minutesBefore > 9 ? String.valueOf(minutesBefore) : "0" + minutesBefore;
        String tempMinutesAfter = minutesAfter > 9 ? String.valueOf(minutesAfter) : "0" + minutesAfter;

        fromToClock.setText(tempHoursBefore + ":" + tempMinutesBefore + "-" + tempHoursAfter + ":" + tempMinutesAfter);

        dataBaseFirebase.setAllDataToDB(generalList, mainList,
                hoursBefore, minutesBefore,
                hoursAfter, minutesAfter,
                repeat, sound, holdOn, daysOfWeek);
    }
}
