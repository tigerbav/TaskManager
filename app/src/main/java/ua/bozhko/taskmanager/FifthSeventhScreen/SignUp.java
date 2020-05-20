package ua.bozhko.taskmanager.FifthSeventhScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import ua.bozhko.taskmanager.DataBaseFirebase;
import ua.bozhko.taskmanager.R;
import ua.bozhko.taskmanager.WorkingSpace.MainActivity;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;

public class SignUp extends AppCompatActivity {
    private TextInputLayout name, lastName, email, password;
    private ImageButton photo, twitter, google, facebook;
    private Button login;

    final AwesomeValidation mAwesomeValidationNameEmailPassword = new AwesomeValidation(TEXT_INPUT_LAYOUT);

    //класс для работы с базой данных
    static private DataBaseFirebase dataBaseFirebase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.nameString);
        lastName = findViewById(R.id.lastNameString);
        email = findViewById(R.id.gmailPhoneString);
        password = findViewById(R.id.passwordString);

        photo = findViewById(R.id.selectPhoto);
        twitter = findViewById(R.id.signupTwitter);
        google = findViewById(R.id.signupGoogle);
        facebook = findViewById(R.id.signupFacebook);

        login = findViewById(R.id.loginInSystem);

        dataBaseFirebase = DataBaseFirebase.createOrReturn();

        //определяет строки на ошибку
        mAwesomeValidationNameEmailPassword.addValidation(SignUp.this, R.id.nameString, "[a-zA-Z\\s]+", R.string.name_error);
        mAwesomeValidationNameEmailPassword.addValidation(SignUp.this, R.id.lastNameString, "[a-zA-Z\\s]+", R.string.last_name_error);
        mAwesomeValidationNameEmailPassword.addValidation(SignUp.this, R.id.gmailPhoneString, android.util.Patterns.EMAIL_ADDRESS, R.string.error_gmail_phone);
        mAwesomeValidationNameEmailPassword.addValidation(SignUp.this, R.id.passwordString, ".{6,}", R.string.password_error);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //проверка ошибок
                if(mAwesomeValidationNameEmailPassword.validate())
                    if(name.getEditText() != null &&
                            lastName.getEditText() != null &&
                            email.getEditText() != null &&
                            password.getEditText() != null)
                        {
                            dataBaseFirebase.registration(name.getEditText().getText().toString()
                                            + " " + lastName.getEditText().getText().toString(),
                                    email.getEditText().getText().toString(),
                                    password.getEditText().getText().toString(),
                                    SignUp.this);
                        }
            }
        });
    }
}
