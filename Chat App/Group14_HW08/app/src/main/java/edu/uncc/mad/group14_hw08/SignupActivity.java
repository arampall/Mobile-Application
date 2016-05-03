package edu.uncc.mad.group14_hw08;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Firebase.setAndroidContext(this);
        final Firebase fbRef = new Firebase(LoginActivity.FIREBASE_ENDPOINT);

        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.signupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameField = (EditText) findViewById(R.id.fullnameEditText);
                String name = nameField.getText().toString();
                EditText emailField = (EditText) findViewById(R.id.emailEditText);
                String email = emailField.getText().toString();
                EditText pwField = (EditText) findViewById(R.id.pwEditText);
                String pw = pwField.getText().toString();
                EditText phoneField = (EditText) findViewById(R.id.phoneEditText);
                String phone = phoneField.getText().toString();
                EditText pwConfField = (EditText) findViewById(R.id.pwConfEditText);
                String pwConf = pwConfField.getText().toString();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || pw.isEmpty() || pwConf.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                } else if(!pw.equals(pwConf)) {
                    Toast.makeText(SignupActivity.this, "Confirmation does not match password", Toast.LENGTH_SHORT).show();
                } else {

                    final User user = new User();
                    user.setEmail(email);
                    user.setPassword(pw);
                    user.setFullName(name);
                    user.setPhoneNumber(phone);

//                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.girl);
//                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
//                    final byte[] image=stream.toByteArray();
//                    System.out.println("byte array:" + image);
//                    final String img_str = Base64.encodeToString(image, 0);
//                    Log.d("d", "image");
//                    Log.d("d", img_str);
//                    user.setPic(img_str);

                    fbRef.createUser(user.getEmail(), user.getPassword(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            System.out.println("Successfully created user account with uid: " + result.get("uid"));
                            user.setUid(result.get("uid").toString());
                            Toast.makeText(SignupActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            fbRef.child("users").child(user.getUid()).setValue(user, new Firebase.CompletionListener() {
                                @Override
                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                    Log.d("d", "user saved");
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                ;
                            });
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Log.d("d", "failed account creation");
                            Log.d("d", firebaseError.getMessage());
                            Log.d("d", firebaseError.getDetails());
                            Toast.makeText(SignupActivity.this, "Failed to create: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
