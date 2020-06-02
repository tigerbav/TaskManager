package ua.bozhko.taskmanager.FifthSeventhScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import ua.bozhko.taskmanager.Constants;
import ua.bozhko.taskmanager.DataBaseFirebase;
import ua.bozhko.taskmanager.FirstScreen.Logo;
import ua.bozhko.taskmanager.R;
import ua.bozhko.taskmanager.WorkingSpace.MainActivity;

public class Login extends AppCompatActivity {
    private Button sign_in_btn;
    private Button sign_up_btn;

    private ImageButton facebook;
    private ImageButton google;
    private ImageButton twitter;

    private DataBaseFirebase dataBaseFirebase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sign_in_btn = findViewById(R.id.signInBtn);
        sign_up_btn = findViewById(R.id.signUpBtn);

        facebook = findViewById(R.id.loginFacebook);
        google = findViewById(R.id.loginGoogle);
        twitter = findViewById(R.id.loginTwitter);


        dataBaseFirebase = DataBaseFirebase.createOrReturn();
        DataBaseFirebase.setGSO();

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseFirebase.googleSignIn(Login.this);
            }
        });

        CallbackManager callbackManager = CallbackManager.Factory.create();

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignIn.class);
                startActivity(intent);
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
