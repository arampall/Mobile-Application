package edu.uncc.mad.group14_hw08;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ConversationsActivity extends AppCompatActivity {
    Firebase fbRef = new Firebase(LoginActivity.FIREBASE_ENDPOINT);
    Firebase usersRef = fbRef.child("users");
    Firebase messagesRef = fbRef.child("messages");
    ArrayList<User> users = new ArrayList<>();
    AuthData authData;
    ConversationsAdapter adapter;
    static String USER_KEY = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        Firebase.setAndroidContext(this);


        authData = fbRef.getAuth();
        if (authData != null) {
            Log.d("d", "user is authenticated");
            ListView listView = (ListView) findViewById(R.id.conversations);
            adapter = new ConversationsAdapter(ConversationsActivity.this, R.layout.conversation_row, users, messagesRef, authData);
            adapter.setNotifyOnChange(true);
            Log.d("d", "oncreate users " + users.size());
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    User user = users.get(position);
                    Log.d("d", "sending user to messages activity " + user.toString());
                    Intent intent = new Intent(ConversationsActivity.this, ViewMessages.class);
                    intent.putExtra(USER_KEY, user);
                    startActivity(intent);
                }
            });

            displayContacts();
        } else {
            Log.d("d", "user is NOT authenticated!");
        }
    }

    private void displayContacts(){
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("d", "on data changed displaying contacts");
                //users.clear();
                adapter.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if (!user.getUid().equals(authData.getUid())) {
                        adapter.add(user);
                    }
                }

//                System.out.println("There are " + users.size() + " users");
//                Log.d("d", "users " + users.toString());
//                adapter.addAll(users);
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    //Create Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.conversations_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.menu_edit_profile:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_logout:
                fbRef.unauth();
                intent = new Intent(ConversationsActivity.this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
