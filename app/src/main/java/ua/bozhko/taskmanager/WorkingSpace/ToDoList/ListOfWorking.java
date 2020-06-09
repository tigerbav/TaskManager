package ua.bozhko.taskmanager.WorkingSpace.ToDoList;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import ua.bozhko.taskmanager.Constants;
import ua.bozhko.taskmanager.DataBaseFirebase;
import ua.bozhko.taskmanager.R;
import ua.bozhko.taskmanager.Receiver;

public class ListOfWorking extends Fragment implements ICallBack.IDay, ICallBack.INotification {
    private LinearLayout generalTask;
    private LinearLayout toDoList;
    private TextView textView;
    private ImageButton btnBack;

    private DialogSetDay dialogSetDay;
    private NotificationDialog notificationDialog;
    private DataBaseFirebase dataBaseFirebase;
    private String generalList;
    private SharedPreferences sharedPreferences;

    public static List<CheckBox> listOfToDoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist_list_working, container, false);
        generalTask = view.findViewById(R.id.generalTask);
        toDoList = view.findViewById(R.id.todolist);
        textView = view.findViewById(R.id.add_new_list_2);
        btnBack = view.findViewById(R.id.backBtn);

        Bundle bundle = this.getArguments();
        dialogSetDay = new DialogSetDay();


        dataBaseFirebase = DataBaseFirebase.createOrReturn();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(!sharedPreferences.getString(Constants.NOTIFICATION_MAIN_TEXT, "").equals(""))
        {
            NotificationDialog.setNotification(this, sharedPreferences.getString(Constants.MAIN_LIST, ""),
                    sharedPreferences.getString(Constants.NOTIFICATION_MAIN_TEXT, ""));
            notificationDialog = new NotificationDialog();
            notificationDialog.setCancelable(false);
            notificationDialog.show(getFragmentManager(), "Notification Dialog");

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.NOTIFICATION_MAIN_TEXT, "").apply();
        }

        if(bundle != null)
        {
            generalList = bundle.getString("BundleButton");
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(15, 15, 15, 20);

            Button button = new Button(getContext(),  null, R.style.buttons_todolist_general_list, R.style.buttons_todolist_general_list);
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodayGeneralClass.fTrans = getFragmentManager().beginTransaction();
                TodayGeneralClass.fTrans.replace(R.id.frameLayout, new GeneralList()).commit();
            }
        });


        dataBaseFirebase.readFromDBMainList(generalList, getContext(), toDoList, dialogSetDay, getFragmentManager(), this);
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
                    String currentDay = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
                    Set<String> stringSet = sharedPreferences.getStringSet(Constants.MAIN_TASKS_FOR_LOCAL, null);
                    if(stringSet == null){
                        stringSet = new HashSet<>();
                    }
                    stringSet.add(generalList + value);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet(Constants.MAIN_TASKS_FOR_LOCAL, stringSet ).apply();
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
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    dataBaseFirebase.writeToDBCheckList(generalList, value);
            }
        });
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
           for(CheckBox tempList : listOfToDoList){
               if(value.equals(tempList.getText().toString()))
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

        Intent intent = new Intent(getContext(), Receiver.class);
        intent.putExtra(Constants.MAIN_LIST, mainList);
        intent.putExtra(Constants.GLOBAL_LIST, generalList);
        intent.putExtra(Constants.FROM, tempHoursBefore + ":" + tempMinutesBefore);
        intent.putExtra(Constants.TO, tempHoursAfter + ":" + tempMinutesAfter);

        setUpAlarm(getContext(), intent,
                hoursBefore, minutesBefore);
        dataBaseFirebase.setAllDataToDB(generalList, mainList,
                hoursBefore, minutesBefore,
                hoursAfter, minutesAfter,
                repeat, sound, holdOn, daysOfWeek);
    }

    public static void setUpAlarm(final Context context, final Intent intent,
                                  int hoursClock, int minutesClock) {
        long timeInterval = 0;
        String hours = new SimpleDateFormat("HH", Locale.UK).format(new Date());
        String minutes = new SimpleDateFormat("mm", Locale.UK).format(new Date());
        String seconds = new SimpleDateFormat("ss", Locale.UK).format(new Date());

        int h = Integer.parseInt(hours);
        int m = Integer.parseInt(minutes);
        int s = Integer.parseInt(seconds);

        int currentTimeInMillSec = h * 60 * 60 * 1000 + m * 60 * 1000 + s * 1000;
        int timeForClock = hoursClock * 60 * 60 * 1000 + minutesClock * 60 * 1000;

        if(currentTimeInMillSec < timeForClock) {
            timeInterval = timeForClock - currentTimeInMillSec;
        }

        final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final PendingIntent pi = PendingIntent.getBroadcast(context, (int) timeInterval, intent, 0);
        am.cancel(pi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + timeInterval, pi);
            am.setAlarmClock(alarmClockInfo, pi);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeInterval, pi);
        else
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeInterval, pi);
    }

    @Override
    public void setCheckBox(String checkBoxText) {
        for(CheckBox temp : listOfToDoList) {
            if(temp.getText().toString().equals(checkBoxText)) {
                temp.setChecked(true);
            }
        }
    }
}
