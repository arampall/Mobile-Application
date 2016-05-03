package edu.uncc.mad.group14_hw08;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ViewMessages extends AppCompatActivity {
    Firebase ref;
    ArrayList<Message> messages = new ArrayList<>();
    TextView msg;
    User user;
    User contact;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);
        Firebase.setAndroidContext(this);

        ref = new Firebase(LoginActivity.FIREBASE_ENDPOINT);
        final Firebase mRef = ref.child("messages");
        list = (ListView) findViewById(R.id.messages_list);
        final AuthData authData = ref.getAuth();

        if (authData != null) {
            Log.d("d", "auth data uid " + authData.getUid());
            ref.child("users").orderByChild("uid").equalTo(authData.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("d", "snapshot " + snapshot);
                        user = snapshot.getValue(User.class);
                        Log.d("d", "user " + user.toString());
                    }

                    contact = (User) getIntent().getExtras().getSerializable(ConversationsActivity.USER_KEY);
                    final MessageAdapter adapter = new MessageAdapter(ViewMessages.this, R.layout.layout_message, messages, user, contact, mRef);
                    list.setAdapter(adapter);

                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            adapter.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Message message = snapshot.getValue(Message.class);

                                if ((message.getSender().equals(user.getUid()) && message.getReceiver().equals(contact.getUid()) ||
                                        (message.getSender().equals(contact.getUid()) && message.getReceiver().equals(user.getUid()))))
                                {
                                    Log.d("demo", "out cndn " + message.getMessage_text() + "-" + message.isMessage_read());
                                    if (message.getReceiver().equals(user.getUid())) {
                                        Log.d("demo", "in -" + message.getReceiver() + " -----" + user.getFullName());
                                        message.setMessage_read(true);
                                        mRef.child(message.getUid()).setValue(message);
                                    }
                                    adapter.add(message);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


//            ref.child("users").child(authData.getUid()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    messages = new ArrayList<>();
//                    user = dataSnapshot.getValue(User.class);
//                    list = (ListView) findViewById(R.id.messages_list);
//                    final Firebase mRef = ref.child("messages").child(setKey());
//                    mRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            messages.clear();
//                            Message message;
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                message = snapshot.getValue(Message.class);
//                                Log.d("demo","out cndn "+message.getMessage_text()+"-"+message.isMessage_read());
//                                if(message.getReceiver().equals(user.getFullName())) {
//                                    Log.d("demo","in -"+message.getReceiver()+" -----"+user.getFullName());
//                                    message.setMessage_read(true);
//                                    ref.child("messages").child(setKey()).child(snapshot.getKey()).setValue(message);
//                                }
//                                messages.add(message);
//                            }
//                            MessageAdapter adapter = new MessageAdapter(ViewMessages.this, R.layout.layout_message, messages, user, mRef);
//                            list.setAdapter(adapter);
//                        }
//
//                        @Override
//                        public void onCancelled(FirebaseError firebaseError) {
//
//                        }
//                    });
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//
//                }
//            });

            findViewById(R.id.button_send).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msg = (TextView) findViewById(R.id.message_text);
                    String text = msg.getText().toString();


                    if (text.isEmpty()) {
                        Toast.makeText(ViewMessages.this, "Please Insert a Message", Toast.LENGTH_SHORT).show();
                    } else if (text.length() > 140) {
                        Toast.makeText(ViewMessages.this, "Message cannot be longer than 140 characters", Toast.LENGTH_SHORT).show();
                    } else {
                        Message message = new Message();
                        message.setMessage_text(msg.getText().toString());
                        message.setSender(authData.getUid());
                        message.setReceiver(contact.getUid());
                        message.setMessage_read(false);
                        String timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(new Date());
                        message.setTimestamp(timeStamp);
                        Log.d("demo", message.toString());
                        Firebase newMessage = ref.child("messages").push();
                        message.setUid(newMessage.getKey());
                        newMessage.setValue(message, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                msg.setText(null);
                            }
                        });
                    }
                }
            });
        }
    }

    public String setKey() {
        String key;
        int compare = this.user.getFullName().compareTo(contact.getFullName());
        if (compare < 0) {
            key = user.getFullName() + "@" + contact.getFullName();
        } else if (compare > 0) {
            key = contact.getFullName() + "@" + user.getFullName();
        } else {
            key = null;
        }
        return key;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.msg_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            //Start edit activity
            case R.id.view_contact:
                intent = new Intent(ViewMessages.this, ViewContact.class);
                intent.putExtra(ConversationsActivity.USER_KEY,contact);
                startActivity(intent);
                return true;


            //logout and return to login activity
            case R.id.call_contact:
//                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getPhoneNumber()));
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                }
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+contact.getPhoneNumber()));
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
