package ua.bozhko.taskmanager.FirstScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;

import ua.bozhko.taskmanager.Constants;
import ua.bozhko.taskmanager.FifthSeventhScreen.Login;
import ua.bozhko.taskmanager.R;
import ua.bozhko.taskmanager.SecondFourthScreen.GeneralScreenSFS;
import ua.bozhko.taskmanager.WorkingSpace.MainActivity;

public class Logo extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        new Thread(()->{
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(FirebaseAuth.getInstance().getCurrentUser() != null){
                Intent intent = new Intent(Logo.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else{
                Intent intent;
                if(sharedPreferences.getBoolean(Constants.FIRST_LOAD, false))
                    intent = new Intent(Logo.this, Login.class);
                else
                    intent = new Intent(Logo.this, GeneralScreenSFS.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        }).start();

    }

}
