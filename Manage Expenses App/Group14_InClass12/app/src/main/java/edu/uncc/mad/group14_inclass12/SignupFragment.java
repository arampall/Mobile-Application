package edu.uncc.mad.group14_inclass12;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {
    Controller controller;

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // make sure activity implements interface
        try{
            controller = (Controller) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity should implement interface");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(getActivity());
        final Firebase fbRef = new Firebase(MainActivity.FIREBASE_ENDPOINT);

        getActivity().findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.goToLogin();
            }
        });

        getActivity().findViewById(R.id.signupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameField = (EditText) getActivity().findViewById(R.id.fullnameEditText);
                String name = nameField.getText().toString();
                EditText emailField = (EditText) getActivity().findViewById(R.id.emailEditText);
                String email = emailField.getText().toString();
                EditText pwField = (EditText) getActivity().findViewById(R.id.pwEditText);
                String pw = pwField.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !pw.isEmpty()) {

                    final User user = new User();
                    user.setEmail(email);
                    user.setPassword(pw);
                    user.setFullName(name);

                    fbRef.createUser(user.getEmail(), user.getPassword(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            System.out.println("Successfully created user account with uid: " + result.get("uid"));
                            user.setUid(result.get("uid").toString());
                            Toast.makeText(getActivity(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            fbRef.child("users").child(user.getUid()).setValue(user, new Firebase.CompletionListener() {
                                @Override
                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                    Log.d("d", "user saved");
                                    controller.closeCurrent();
                                }
                            });
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Log.d("d", "failed account creation");
                            Log.d("d", firebaseError.getMessage());
                            Log.d("d", firebaseError.getDetails());
                            Toast.makeText(getActivity(), "Failed to create: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
