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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import ua.bozhko.taskmanager.Constants;
import ua.bozhko.taskmanager.DataBaseFirebase;
import ua.bozhko.taskmanager.R;
import ua.bozhko.taskmanager.WorkingSpace.MainActivity;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;

public class SignIn extends AppCompatActivity {
    private Button loginBtn;
    private ImageButton twitter, google, facebook;
    private TextInputLayout email, password;

    final AwesomeValidation mAwesomeValidationNameEmailPassword = new AwesomeValidation(TEXT_INPUT_LAYOUT);
    static private DataBaseFirebase dataBaseFirebase;
    private SignIn signIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        dataBaseFirebase = DataBaseFirebase.createOrReturn();


        twitter = findViewById(R.id.signInTwitter);
        google = findViewById(R.id.signInGoogle);
        facebook = findViewById(R.id.signInFacebook);

        email = findViewById(R.id.loginString);
        password = findViewById(R.id.passwordString);

        mAwesomeValidationNameEmailPassword.addValidation(SignIn.this, R.id.loginString, android.util.Patterns.EMAIL_ADDRESS, R.string.error_gmail_phone);
        mAwesomeValidationNameEmailPassword.addValidation(SignIn.this, R.id.passwordString, ".{6,}", R.string.password_error);

        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAwesomeValidationNameEmailPassword.validate())
                    dataBaseFirebase.signIn(email.getEditText().getText().toString(),
                            password.getEditText().getText().toString(), SignIn.this);

            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseFirebase.googleSignIn(SignIn.this);
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseFirebase.signInTwitter(SignIn.this);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            dataBaseFirebase.handleSignInResult(task, this);
        }
    }
}
