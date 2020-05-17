package ua.bozhko.taskmanager.WorkingSpace.ToDoList;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ua.bozhko.taskmanager.Constants;
import ua.bozhko.taskmanager.R;

public class DialogSetDay extends DialogFragment implements ICallBack.ITime{
    private Switch[] switchDayOfWeek;
    private TextView cancelTV, saveTV, setTimeBefore, setTimeAfter;
    private FrameLayout[] frame = new FrameLayout[2];
    private DialogSetTime dialogSetTime;

    private int hoursFrom = 0, minutesFrom = 0, hoursTo = 0, minutesTo = 0;
    private String repeat = (String) Constants.NO_DATA, sound = (String) Constants.NO_DATA;
    private boolean holdOver = false;
    static private String generalList, mainList;
    static private TextView fromToClock;

    static private ICallBack.IDay iDay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_set_day, container, false);

        setTimeBefore = view.findViewById(R.id.setTimeBefore_id);
        setTimeAfter = view.findViewById(R.id.setTimeAfter_id);

        switchDayOfWeek = new Switch[]{view.findViewById(R.id.switchMonday),
                view.findViewById(R.id.switchTuesday),
                view.findViewById(R.id.switchWednesday),
                view.findViewById(R.id.switchThursday),
                view.findViewById(R.id.switchFriday),
                view.findViewById(R.id.switchSaturday),
                view.findViewById(R.id.switchSunday)};
        cancelTV = view.findViewById(R.id.cancelTV);
        saveTV = view.findViewById(R.id.saveTV);

        frame[0] = view.findViewById(R.id.fra);
        frame[1] = view.findViewById(R.id.fra2);

        dialogSetTime = new DialogSetTime();

        boolean[] flagDayOfWeek = new boolean[switchDayOfWeek.length];

        for(int i = 0 ; i < switchDayOfWeek.length; i++){
            final int index = i;
            switchDayOfWeek[index].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    flagDayOfWeek[index] = b;
                    if(b)
                        switchDayOfWeek[index].setTextColor(getResources().getColor(R.color.switchColor));
                    else
                        switchDayOfWeek[index].setTextColor(getResources().getColor(R.color.black));
                }
            });
        }

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        frame[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSetTime.setCallBack(DialogSetDay.this, Constants.FROM);
                dialogSetTime.show(getFragmentManager(), "DialogSetTimeFROM");
            }
        });

        frame[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSetTime.setCallBack(DialogSetDay.this, Constants.TO);
                dialogSetTime.show(getFragmentManager(), "DialogSetTimeTO");
            }
        });

        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iDay != null){
                    iDay.setDayDialogFragment(generalList, mainList,
                            hoursFrom, minutesFrom,
                            hoursTo, minutesTo,
                            fromToClock,
                            repeat, sound, holdOver,
                            flagDayOfWeek);
                    dismiss();
                }
            }
        });
        return view;
    }

    @Override
    public void setTimeDialogFragment(int hours, int minutes, String repeat, String sound, boolean holdOver, String typeTime) {
        int tempHoursInt = hours >= 12 ? hours - 12 : hours;
        String tempHours = tempHoursInt > 9 ? String.valueOf(tempHoursInt) : "0" + tempHoursInt;
        String tempMinutes = minutes > 9 ? String.valueOf(minutes) : "0" + minutes;
        String time;
        if(hours >= 12)
            time = tempHours + ":" + tempMinutes + " PM";
        else
            time = tempHours + ":" + tempMinutes + " AM";

        switch (typeTime){
            case Constants.FROM:
                hoursFrom = hours;
                minutesFrom = minutes;
                setTimeBefore.setText(time);
                break;
            case Constants.TO:
                hoursTo = hours;
                minutesTo = minutes;
                setTimeAfter.setText(time);
                break;
        }
        this.repeat = repeat;
        this.sound = sound;

        this.holdOver = holdOver;
    }

    static void setCallBack(ICallBack.IDay day, String generaList, String value, TextView fromToClock){
        iDay = day;
        DialogSetDay.generalList = generaList;
        mainList = value;
        DialogSetDay.fromToClock = fromToClock;
    }
}
