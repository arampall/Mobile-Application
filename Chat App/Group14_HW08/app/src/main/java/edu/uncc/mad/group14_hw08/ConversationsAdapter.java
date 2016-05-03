package edu.uncc.mad.group14_hw08;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.List;

public class ConversationsAdapter extends ArrayAdapter<User> {
    List<User> users;
    Context context;
    int resource;
    Firebase ref;
    AuthData authData;

    public ConversationsAdapter(Context context, int resource, List<User> users, Firebase ref, AuthData authData) {
        super(context, resource, users);
        this.users = users;
        this.context = context;
        this.resource = resource;
        this.ref = ref;
        this.authData = authData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource, parent, false);
        }

        final View thisView = convertView;
        final User contact = this.users.get(position);

        //Log.d("d", "showing position " + position + " for row for " + contact.getFullName());

        ImageView pic = (ImageView) convertView.findViewById(R.id.conversationImage);

        if(contact.getPic() != null && !contact.getPic().isEmpty()) {
            Log.d("d", "showing custom pic for " + contact.getFullName());
            byte[] decodedString = Base64.decode(contact.getPic(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            pic.setImageBitmap(decodedByte);
        } else {
            pic.setImageResource(R.drawable.default_profile);
        }

        TextView name = (TextView) convertView.findViewById(R.id.contactName);
        name.setText(contact.getFullName());

        if(contact.getPhoneNumber() != null && !contact.getPhoneNumber().isEmpty()){
            ImageView phoneIcon = (ImageView) thisView.findViewById(R.id.phoneIcon);
            phoneIcon.setVisibility(View.VISIBLE);

            phoneIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("d", "phone icon clicked");
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+contact.getPhoneNumber()));
                    thisView.getContext().startActivity(intent);
                }
            });
        }

        ref.
            orderByChild("receiver").
            equalTo(authData.getUid()).
            addValueEventListener(new ValueEventListener() {
                ImageView unreadNotifier = (ImageView) thisView.findViewById(R.id.unreadNotification);

                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Message message = postSnapshot.getValue(Message.class);

                        if (message.getSender().equals(contact.getUid()) && !message.isMessage_read()) {
                            unreadNotifier.setVisibility(View.VISIBLE);
                            break;
                        } else {
                            unreadNotifier.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
        });

        return convertView;
    }
}

