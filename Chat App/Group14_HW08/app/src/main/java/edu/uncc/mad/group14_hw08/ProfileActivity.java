package edu.uncc.mad.group14_hw08;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity{

    Firebase ref;
    AuthData authData;
    String to64;
    TextView textView;
    EditText editText;
    User user;
    String oldEmail;
    String oldPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Firebase.setAndroidContext(this);

        ref = new Firebase(LoginActivity.FIREBASE_ENDPOINT);

        //Start gallery by clicking image
        findViewById(R.id.user_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
            }
        } );


        authData = ref.getAuth();
        ref.child("users").orderByChild("uid").equalTo(authData.getUid()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        displayDetails(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });



        //update user details
        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                user.setPic(to64);
                editText = (EditText) findViewById(R.id.name_box);
                user.setFullName(editText.getText().toString());
                editText = (EditText) findViewById(R.id.phone_number);
                user.setPhoneNumber(editText.getText().toString());

                EditText emailField = (EditText) findViewById(R.id.email);
                String email = emailField.getText().toString();//email.setText(email.getText().toString());
                EditText passField = (EditText) findViewById(R.id.password);
                final String pass = passField.getText().toString();//pass.setText(pass.getText().toString());

                oldEmail = user.getEmail();
                oldPass = user.getPassword();

                user.setEmail(email);
                user.setPassword(pass);

                if(!oldEmail.equals(email)) {
                    ref.changeEmail(oldEmail, oldPass, email, new Firebase.ResultHandler() {
                        @Override
                        public void onSuccess() {
                            changePassword(pass);
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Log.d("d", "Error " + firebaseError.getMessage());
                            Toast.makeText(ProfileActivity.this, "Error: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
                } else if (!oldPass.equals(pass)) {
                    changePassword(pass);
                } else{
                    updateUser();
                }




//                Map users = new HashMap();
//                users.put(ref.getAuth().getUid(), user);
//                ref.child("users").updateChildren(users);


            }


        });





        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ProfileActivity.this, ConversationsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void changePassword(String newPass){
        if(!oldPass.equals(newPass)) {
            ref.changePassword(user.getEmail(), oldPass, newPass, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    updateUser();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    Log.d("d", "Error " + firebaseError.getMessage());
                    Toast.makeText(ProfileActivity.this, "Error: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            updateUser();
        }
    }

    private void updateUser(){
        ref.child("users").child(user.getUid()).setValue(user, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                Intent intent = new Intent(ProfileActivity.this, ConversationsActivity.class);
                startActivity(intent);
                Toast.makeText(ProfileActivity.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    //Get image from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView image = (ImageView) findViewById(R.id.user_image);
        try {
            if (requestCode == requestCode && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                Log.d("d", "Image URI: " + selectedImage);

                Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                image.setImageBitmap(bm);

                //Encoding image to base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                to64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.d("d", "Encoded String: " + to64);

            }
        }catch (IOException e){
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_LONG).show();
        }

    }


    public void displayDetails(DataSnapshot dataSnapshot){
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Log.d("d", "snapshot " + snapshot);
            user = snapshot.getValue(User.class);
            Log.d("d", "user " + user.toString());
        }
        //user = snapshot.getValue(User.class);

        //populate labels
        ImageView image = (ImageView) findViewById(R.id.user_image);
        if(user.getPic() != null && !user.getPic().equals("")) {
            byte[] decodedString = Base64.decode(user.getPic(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
        } else {
            image.setImageResource(R.drawable.default_profile);
        }
        textView = (TextView) findViewById(R.id.name_label);
        textView.setText(user.getFullName());
        editText = (EditText) findViewById(R.id.name_box);
        editText.setText(user.getFullName());
        editText = (EditText) findViewById(R.id.email);
        editText.setText(user.getEmail());
        editText = (EditText) findViewById(R.id.phone_number);
        editText.setText(user.getPhoneNumber());
        editText = (EditText) findViewById(R.id.password);
        editText.setText(user.getPassword());
    }


    //on back button go to conversationsActivity
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ProfileActivity.this, ConversationsActivity.class);
        startActivity(intent);
        finish();
    }
}
