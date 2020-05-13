package ua.bozhko.taskmanager.WorkingSpace.ToDoList;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

import ua.bozhko.taskmanager.Constants;
import ua.bozhko.taskmanager.R;

public class DialogSetTime extends DialogFragment implements View.OnTouchListener {
    private LinearLayout linearLayoutHours, linearLayoutMinutes;
    private ScrollView scrollViewHours, scrollViewMinutes;
    private TextView saveTV, cancelTV;
    private Switch switchHold;
    private TextView repeat, sound;

    private ArrayList<TextView> hours = new ArrayList<>();
    private ArrayList<TextView> minutes = new ArrayList<>();

    private int scaleTextView = 0, hoursSet, minutesSet;

    static private ICallBack.ITime iTime;
    static private String typeTime;

    private boolean switchHoldOn = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_set_time, container, false);

        linearLayoutHours = view.findViewById(R.id.hoursLinear);
        linearLayoutMinutes = view.findViewById(R.id.minutesLinear);
        scrollViewHours = view.findViewById(R.id.scrol1);
        scrollViewMinutes = view.findViewById(R.id.scrol2);

        saveTV = view.findViewById(R.id.saveTV);
        cancelTV = view.findViewById(R.id.cancelTV);

        switchHold = view.findViewById(R.id.holdOverBool);

        switchHold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchHoldOn = b;
            }
        });

        repeat = view.findViewById(R.id.repeatText);
        sound = view.findViewById(R.id.soundText);

        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iTime != null){
                    iTime.setTimeDialogFragment(hoursSet, minutesSet, repeat.getText().toString(),
                            sound.getText().toString(), switchHoldOn, typeTime);
                    dismiss();
                }
            }
        });

        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        for(int i = -2; i < 26; i++){
            TextView textView = new TextView(getContext(), null, R.style.fontFam, R.style.fontFam);
            String setText = i >= 10 ? Integer.toString(i) : "0" + i;
            if(i < 0 || i >= 24)
                textView.setText("");
            else
                textView.setText(setText);
            hours.add(textView);
            textView.setGravity(Gravity.END);
            textView.setTextSize(18.7607f);
            textView.setPadding(5,5,5,5);
            linearLayoutHours.addView(textView);
            if(i == -2){
                textView.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                scaleTextView = textView.getHeight();
                                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        }
                );
            }
        }
        for(int i = -2; i < 62; i++){
            TextView textView = new TextView(getContext(), null, R.style.fontFam, R.style.fontFam);
            String setText = i >= 10 ? Integer.toString(i) : "0" + i;
            if(i < 0 || i >= 60)
                textView.setText("");
            else
                textView.setText(setText);
            minutes.add(textView);
            textView.setGravity(Gravity.START);
            textView.setTextSize(18.7607f);
            textView.setPadding(5,5,5,5);
            linearLayoutMinutes.addView(textView);
        }

        scrollViewHours.setOnTouchListener(this);
        scrollViewMinutes.setOnTouchListener(this);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.getId() == scrollViewHours.getId()){
            if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                hoursSet = setScrollChange(scrollViewHours);
        }
        else if(view.getId() == scrollViewMinutes.getId())
            if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                minutesSet = setScrollChange(scrollViewMinutes);

        return false;
    }

    private int setScrollChange(ScrollView scrollView){
        int positionY = scrollView.getScrollY(), scrollTo;
        String upOrDown = (positionY % scaleTextView) >= (scaleTextView / 2) ? Constants.UP : Constants.DOWN;
        if(upOrDown.equals(Constants.DOWN))
            scrollTo = positionY - (positionY % scaleTextView);
        else
            scrollTo = positionY - (positionY % scaleTextView) + scaleTextView;
        if(scrollTo < 0)
            scrollView.scrollTo(0, 0);
        else
            scrollView.scrollTo(0, scrollTo);

        return scrollTo / scaleTextView ;
    }

    static void setCallBack(ICallBack.ITime time, String type){
        iTime = time;
        typeTime = type;
    }
}
