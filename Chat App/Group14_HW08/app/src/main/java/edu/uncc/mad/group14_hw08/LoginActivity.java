package edu.uncc.mad.group14_hw08;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginActivity extends AppCompatActivity {
    static String FIREBASE_ENDPOINT = "https://sweltering-torch-9125.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);
        final Firebase fbRef = new Firebase(FIREBASE_ENDPOINT);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("d", "logging in");
                EditText emailField = (EditText) findViewById(R.id.emailEditText);
                String email = emailField.getText().toString();
                EditText pwField = (EditText) findViewById(R.id.passwordEditText);
                String pw = pwField.getText().toString();
                fbRef.authWithPassword(email, pw, authResultHandler);
            }
        });

        findViewById(R.id.createAccountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AuthData authData = fbRef.getAuth();
        if (authData != null) {
            Log.d("d", "yes authenticated");
            sendToExpenses();
        } else {
            Log.d("d", "not authenticated");
        }
    }

    final Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
        @Override
        public void onAuthenticated(AuthData authData) {
            Log.d("d", "user just logged in, sending to conversations");
            Log.d("d", "auth uid " + authData.getUid());
            sendToExpenses();
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            Log.d("d", "error when logging in");
            Log.d("d", firebaseError.getMessage());
            Log.d("d", firebaseError.getDetails());
            Toast.makeText(LoginActivity.this, "Login Unsuccessful: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void sendToExpenses(){
        Intent intent = new Intent(LoginActivity.this, ConversationsActivity.class);
        startActivity(intent);
        finish();
    }
}
