package ua.bozhko.taskmanager.FirstScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
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
        // отображение представления, в данном случае активити
        setContentView(R.layout.activity_logo);

        //определение переменной для хранения локальных данных
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //создание класса "поток", который работает параллельно с основным(поток, проходящий по всей программе)
        new Thread(()->{
            //try(ловим ошибки)/catch(если словили, то обрабатываем) необходим для Thread.sleep, ибо данное обращение этого требует
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(FirebaseAuth.getInstance().getCurrentUser() != null ||
                    GoogleSignIn.getLastSignedInAccount(this) != null){
                Intent intent = new Intent(Logo.this, MainActivity.class);
                //подготовка к удалению ЛОГО и созданию МэйнАктивити
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else{
                Intent intent;
                //смотри мне!
                if(sharedPreferences.getBoolean(Constants.FIRST_LOAD, false))
                    intent = new Intent(Logo.this, Login.class);
                else
                    intent = new Intent(Logo.this, GeneralScreenSFS.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//удаляем с памяти активити,
                startActivity(intent);
            }

        }).start();

    }

}
