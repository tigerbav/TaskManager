package ua.bozhko.taskmanager.WorkingSpace.ToDoList;

import android.widget.TextView;

public interface ICallBack {
    interface IDay {
        void setDayDialogFragment(String generalList, String mainList,
                                  int hoursBefore, int minutesBefore,
                                  int hoursAfter, int minutesAfter,
                                  TextView fromToClock,
                                  String repeat, String sound, boolean holdOn,
                                  boolean[] daysOfWeek);
    }

    interface ITime {
        void setTimeDialogFragment(int hours, int minutes, String repeat, String sound, boolean holdOn, String typeTime);
    }

    interface INotification{
        void setCheckBox(String checkBoxText);
    }
}
