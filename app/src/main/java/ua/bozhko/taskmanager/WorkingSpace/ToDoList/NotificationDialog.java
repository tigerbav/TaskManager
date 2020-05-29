package ua.bozhko.taskmanager.WorkingSpace.ToDoList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ua.bozhko.taskmanager.Constants;
import ua.bozhko.taskmanager.DataBaseFirebase;
import ua.bozhko.taskmanager.R;
import ua.bozhko.taskmanager.Receiver;

public class NotificationDialog extends DialogFragment {
    private ImageButton done;
    private ImageButton skip;
    private ImageButton go;

    private TextView timeStart;
    private TextView timeEnd;

    private static ICallBack.INotification iNotification;
    private static String task, general;

    private DataBaseFirebase dataBaseFirebase;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_notification, container, false);
        done = view.findViewById(R.id.done_img_btn);
        skip = view.findViewById(R.id.skip_img_btn);
        go = view.findViewById(R.id.go_img_btn);

        timeStart = view.findViewById(R.id.timeStart);
        timeEnd = view.findViewById(R.id.timeEnd);

        dataBaseFirebase = DataBaseFirebase.createOrReturn();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        timeStart.setText(sharedPreferences.getString(Constants.FROM, "Error"));
        timeEnd.setText(sharedPreferences.getString(Constants.TO, "Error"));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iNotification != null){
                    iNotification.setCheckBox(task);
                    dismiss();
                    dataBaseFirebase.writeToDBCheckList(general, task);
                }
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Receiver.class);
                intent.putExtra(Constants.MAIN_LIST, task);
                intent.putExtra(Constants.GLOBAL_LIST, general);

                int h = Integer.parseInt(new SimpleDateFormat("HH", Locale.UK).format(new Date()));
                int m = Integer.parseInt(new SimpleDateFormat("mm", Locale.UK).format(new Date()));

                ListOfWorking.setUpAlarm(getContext(), intent, h, m + 15);
                dismiss();
            }
        });

        return view;
    }

    static void setNotification(ICallBack.INotification notification, String taskMain, String generalTask){
        iNotification = notification;
        task = taskMain;
        general = generalTask;
    }
}
