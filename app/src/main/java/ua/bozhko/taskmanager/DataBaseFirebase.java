package ua.bozhko.taskmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import ua.bozhko.taskmanager.WorkingSpace.MainActivity;
import ua.bozhko.taskmanager.WorkingSpace.ToDoList.DialogSetDay;
import ua.bozhko.taskmanager.WorkingSpace.ToDoList.GeneralList;
import ua.bozhko.taskmanager.WorkingSpace.ToDoList.ICallBack;
import ua.bozhko.taskmanager.WorkingSpace.ToDoList.ListOfWorking;
import ua.bozhko.taskmanager.WorkingSpace.ToDoList.NoTaskScreen;


//Singleton class
public class DataBaseFirebase {

    static DataBaseFirebase dataBaseFirebase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String, Boolean> flag = new HashMap<>();

    private  ConnectivityManager connectivityManager;
    private NetworkInfo wifiInfo;


    private String currentData;

    private SharedPreferences sharedPreferences;

    private static GoogleSignInOptions gso;

    private DataBaseFirebase(){
        currentData = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        //dayOfWeek = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1 == 0) ? Calendar.DAY_OF_WEEK : Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        flag.put(Constants.FLAG, true);

    }

    static public DataBaseFirebase createOrReturn(){
        if(dataBaseFirebase == null)
            dataBaseFirebase = new DataBaseFirebase();
        return dataBaseFirebase;
    }

    public void registration(String fullName, String gmailPhone, String password, Context context){
        mAuth.createUserWithEmailAndPassword(gmailPhone, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        //взятие данных зарегестрированного пользователя из Firebase в текущее приложение
                        mUser = mAuth.getCurrentUser();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                    else{
                        Toast.makeText(context, Constants.REPEAT_LOGIN, Toast.LENGTH_LONG).show();
                        Log.w("Error: ", task.getException());
                    }

                });
    }

    public void signIn(String gmail, String password, Context context){
        mAuth.signInWithEmailAndPassword(gmail, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        mUser = mAuth.getCurrentUser();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(context, Constants.ERROR_PASSWORD_MAIL, Toast.LENGTH_LONG).show();
                        Log.w("Error: ", task.getException());
                    }

                });
    }

    public void writeToDBGeneralList(String generalList){
        if(hasConnection())
        {
            Map<String, String> generalTask = new HashMap<>();
            generalTask.put(Constants.GENERAL_TASK, generalList);

            if(mUser.getEmail() != null)
            {
                db.collection(mUser.getEmail())
                        .document(Constants.GENERAL_TASK)
                        .collection(Constants.GENERAL_TASK)
                        .document(generalList)
                        .set(generalTask);

                db.collection(mUser.getEmail())
                        .document(currentData)
                        .set(flag);
            }
        }
    }

    public void takeFlag(final FragmentTransaction fTrans, NoTaskScreen noTaskScreen, GeneralList generalList, Context context){
        if(hasConnection()){
            if(mUser.getEmail() != null)
                db.collection(mUser.getEmail())
                        .document(currentData)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    if (task.getResult() != null) {
                                        if(task.getResult().getData() != null){
                                            Map<String, Object> temp = task.getResult().getData();
                                            if ((boolean) temp.get(Constants.FLAG))
                                            {
                                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                                                if(!sharedPreferences.getString(Constants.NOTIFICATION_MAIN_TEXT, "").equals(""))
                                                    fTrans.add(R.id.frameLayout, new GeneralList()).commit();
                                                else
                                                    fTrans.add(R.id.frameLayout, generalList).commit();
                                            }

                                            else
                                                fTrans.add(R.id.frameLayout, noTaskScreen).commit();
                                        }
                                        else
                                        {
                                            fTrans.add(R.id.frameLayout, noTaskScreen);
                                            fTrans.commit();
                                        }
                                    }
                                }

                            }
                        });
        }
        else {

            if(sharedPreferences.getStringSet(Constants.GENERAL_TASK_FOR_LOCAL, null) != null){
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                if(!sharedPreferences.getString(Constants.NOTIFICATION_MAIN_TEXT, "").equals(""))
                    fTrans.add(R.id.frameLayout, new GeneralList()).commit();
                else
                    fTrans.add(R.id.frameLayout, generalList).commit();
            }

            else
                fTrans.add(R.id.frameLayout, noTaskScreen).commit();
        }
    }

    public void readFromDBGeneralList(Context context, LinearLayout.LayoutParams layoutParams, LinearLayout linearLayout, ArrayList<Button> allButtons, GeneralList generalList){
        if(hasConnection()){
            if(mUser.getEmail() != null)
                db.collection(mUser.getEmail())
                        .document(Constants.GENERAL_TASK)
                        .collection(Constants.GENERAL_TASK)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        Map<String, Object> temp = document.getData();
                                        String tempString = (String) temp.get(Constants.GENERAL_TASK);

                                        Button button = new Button(context, null, R.style.buttons_todolist_general_list, R.style.buttons_todolist_general_list);
                                        button.setText(tempString);
                                        button.setId(++GeneralList.ID);
                                        button.setLayoutParams(layoutParams);
                                        linearLayout.addView(button);
                                        allButtons.add(button);
                                        button.setOnClickListener(generalList);
                                    }
                                } else {
                                    Log.d("Error read", "Error getting documents: ", task.getException());
                                }
                            }
                        });
        }
        else {
            Set<String> getTasks = sharedPreferences.getStringSet(Constants.GENERAL_TASK_FOR_LOCAL, null);
            if(getTasks != null){
                for (String getTask : getTasks) {
                    Button button = new Button(context, null, R.style.buttons_todolist_general_list, R.style.buttons_todolist_general_list);
                    button.setText(getTask);
                    button.setId(++GeneralList.ID);
                    button.setLayoutParams(layoutParams);
                    linearLayout.addView(button);
                    allButtons.add(button);
                    button.setOnClickListener(generalList);
                }
            }
        }
    }

    public void writeToDBMainList(String generalList, String mainList){
        if(hasConnection()){
            Map<String, Object> listData = new HashMap<>();
            listData.put(Constants.MAIN_LIST, mainList);

            if(mUser.getEmail() != null)
            {
                db.collection(mUser.getEmail())
                        .document(currentData)
                        .collection(generalList)
                        .document(mainList)
                        .set(listData);

                db.collection(mUser.getEmail())
                        .document(currentData)
                        .set(flag);
            }
        }


    }

    public void writeToDBCheckList(String generalList, String mainList){
        if(hasConnection()){
            Map<String, Object> listData = new HashMap<>();
            listData.put(Constants.CHECK_BOX_COMPLETE, true);

            if(mUser.getEmail() != null)
            {
                db.collection(mUser.getEmail())
                        .document(currentData)
                        .collection(generalList)
                        .document(mainList)
                        .update(listData);
            }
        }


    }

    public void readFromDBMainList(String generalTask, Context context, LinearLayout toDoList, DialogSetDay dialogSetDay, FragmentManager fragmentManager, ICallBack.IDay iDay){
        if(hasConnection()){
            if(mUser.getEmail() != null)
                db.collection(mUser.getEmail())
                        .document(currentData)
                        .collection(generalTask)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Map<String, Object> temp = document.getData();
                                        String listMain = (String) temp.get(Constants.MAIN_LIST);
                                        String time = "";
                                        boolean checkBoxComplete = temp.get(Constants.CHECK_BOX_COMPLETE) == null ? false : (boolean) temp.get(Constants.CHECK_BOX_COMPLETE);
                                        try {
                                            String tempHoursBefore = (long) temp.get(Constants.TIME_BEFORE_HOURS) > 9 ?
                                                    String.valueOf(temp.get(Constants.TIME_BEFORE_HOURS)) :
                                                    "0" + temp.get(Constants.TIME_BEFORE_HOURS);
                                            String tempHoursAfter = (long) temp.get(Constants.TIME_AFTER_HOURS) > 9 ?
                                                    String.valueOf( temp.get(Constants.TIME_AFTER_HOURS)) :
                                                    "0" +  temp.get(Constants.TIME_AFTER_HOURS);
                                            String tempMinutesBefore = (long) temp.get(Constants.TIME_BEFORE_MINUTES) > 9 ?
                                                    String.valueOf(temp.get(Constants.TIME_BEFORE_MINUTES)) :
                                                    "0" + temp.get(Constants.TIME_BEFORE_MINUTES);
                                            String tempMinutesAfter = (long) temp.get(Constants.TIME_AFTER_MINUTES) > 9 ?
                                                    String.valueOf(temp.get(Constants.TIME_AFTER_MINUTES)) :
                                                    "0" + temp.get(Constants.TIME_AFTER_MINUTES);

                                            time = tempHoursBefore + ":" +
                                                    tempMinutesBefore + "-" +
                                                    tempHoursAfter + ":" +
                                                    tempMinutesAfter;
                                        }
                                        catch (Exception e){
                                            Log.w("Null TIME", e);
                                        }

                                        setList(context, listMain, toDoList, dialogSetDay, fragmentManager, time, generalTask, iDay, checkBoxComplete);
                                    }
                                } else {
                                    Log.d("Error read", "Error getting documents: ", task.getException());
                                }
                            }
                        });
        }
        else {
            Set<String> getList = sharedPreferences.getStringSet(Constants.MAIN_TASKS_FOR_LOCAL, null);
            if(getList != null) {
                for (String getTask : getList) {
                    if(getTask.contains(generalTask)){
                        getTask = getTask.replace(generalTask, "");
                        setList(context, getTask, toDoList, dialogSetDay, fragmentManager, "", generalTask, iDay, false);
                    }
                }
            }
        }
    }

    private void setList(Context context, String listMain,
                         LinearLayout toDoList, DialogSetDay dialogSetDay,
                         FragmentManager fragmentManager,
                         String time, String generalTask,
                         ICallBack.IDay iDay, boolean checkBoxComplete){
        LinearLayout.LayoutParams layoutColumn = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutColumn.setMargins(0, 30, 15, 0);

        LinearLayout threeColumn = new LinearLayout(context);
        threeColumn.setOrientation(LinearLayout.HORIZONTAL);
        threeColumn.setGravity(Gravity.CENTER);
        threeColumn.setLayoutParams(layoutColumn);

        LinearLayout.LayoutParams forWeight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        forWeight.weight = 1;

        CheckBox checkBox = new CheckBox(context);
        checkBox.setText(listMain);
        checkBox.setChecked(checkBoxComplete);
        checkBox.setLayoutParams(forWeight);
        threeColumn.addView(checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    dataBaseFirebase.writeToDBCheckList(generalTask, listMain);
            }
        });
        ListOfWorking.listOfToDoList.add(checkBox);


        TextView fromToClock = new TextView(context);
        fromToClock.setTextSize(16f);
        if(!time.equals(""))
            fromToClock.setText(time);
        fromToClock.setGravity(Gravity.END);
        fromToClock.setPadding(0,0,15,0);
        fromToClock.setLayoutParams(forWeight);
        threeColumn.addView(fromToClock);

        ImageView clock = new ImageView(context);
        clock.setImageResource(R.drawable.element_clock);
        clock.setScaleType(ImageView.ScaleType.FIT_END);
        threeColumn.addView(clock);
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSetDay.setCallBack(iDay, generalTask, listMain, fromToClock);
                dialogSetDay.show(fragmentManager, "DialogSetDay");
            }
        });

        toDoList.addView(threeColumn);

        ImageView line = new ImageView(context);
        line.setBackground(ContextCompat.getDrawable(context, R.drawable.line));
        toDoList.addView(line);
        line.setScaleType(ImageView.ScaleType.FIT_XY);

        ViewGroup.MarginLayoutParams marginLayoutParamsImage = (ViewGroup.MarginLayoutParams) line.getLayoutParams();
        marginLayoutParamsImage.setMarginEnd(15);
        marginLayoutParamsImage.setMarginStart(15);
        line.setLayoutParams(marginLayoutParamsImage);
    }

    public void setAllDataToDB(String generalList, String mainList,
                               int hoursBefore, int minutesBefore,
                               int hoursAfter, int minutesAfter,
                               String repeat, String sound,
                               boolean holdOn, boolean[] daysOfWeek){
        Map<String, Object> listData = new HashMap<>();

        listData.put(Constants.MAIN_LIST, mainList);

        listData.put(Constants.MONDAY, daysOfWeek[0]);
        listData.put(Constants.TUESDAY, daysOfWeek[1]);
        listData.put(Constants.WEDNESDAY, daysOfWeek[2]);
        listData.put(Constants.THURSDAY, daysOfWeek[3]);
        listData.put(Constants.FRIDAY, daysOfWeek[4]);
        listData.put(Constants.SATURDAY, daysOfWeek[5]);
        listData.put(Constants.SUNDAY, daysOfWeek[6]);

        listData.put(Constants.TIME_BEFORE_HOURS, hoursBefore);
        listData.put(Constants.TIME_BEFORE_MINUTES, minutesBefore);
        listData.put(Constants.TIME_AFTER_HOURS, hoursAfter);
        listData.put(Constants.TIME_AFTER_MINUTES, minutesAfter);

        listData.put(Constants.REPEAT, repeat);
        listData.put(Constants.SOUND, sound);
        listData.put(Constants.HOLD_OVER, holdOn);

        listData.put(Constants.CHECK_BOX_COMPLETE, false);

        if(mUser.getEmail() != null)
        {
            db.collection(mUser.getEmail())
                    .document(currentData)
                    .collection(generalList)
                    .document(mainList)
                    .set(listData);

            db.collection(mUser.getEmail())
                    .document(currentData)
                    .set(flag);
        }
    }
    public static void setGSO(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(String.valueOf(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    public void googleSignIn(Activity activity){
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask, Activity activity) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            dataBaseFirebase.firebaseAuthWithGoogle(activity, account.getIdToken());
        } catch (Exception e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error: ", "signInResult:failed code=" + e);
        }
    }

    public void firebaseAuthWithGoogle(Activity activity, String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mUser = mAuth.getCurrentUser();
                            Intent intent = new Intent(activity, MainActivity.class);

                            //подготовка к удалению ЛОГО и созданию МэйнАктивити
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Error:", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    public void signInTwitter(Activity activity){
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
        FirebaseAuth.getInstance()
                .startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                mUser = authResult.getUser();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure.
                            }
                        });
    }

    public void setSharedPreferencesAndConMan(SharedPreferences sharedPreferences, ConnectivityManager connectivityManager){
        this.sharedPreferences = sharedPreferences;
        this.connectivityManager = connectivityManager;
    }

    private  boolean hasConnection() {
        wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
            return true;
        wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
            return true;
        wifiInfo = connectivityManager.getActiveNetworkInfo();
        return wifiInfo != null && wifiInfo.isConnected();
    }

}
