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

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginFragment extends Fragment {
    Controller controller;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
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

        getActivity().findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("d", "logging in");
                EditText emailField = (EditText) getActivity().findViewById(R.id.emailEditText);
                String email = emailField.getText().toString();
                EditText pwField = (EditText) getActivity().findViewById(R.id.passwordEditText);
                String pw = pwField.getText().toString();
                fbRef.authWithPassword(email, pw, authResultHandler);
            }
        });

        getActivity().findViewById(R.id.createAccountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.goToSignup();
            }
        });

        AuthData authData = fbRef.getAuth();
        if (authData != null) {
            Log.d("d", "yes authenticated");
            controller.goToExpenses();
        } else {
            Log.d("d", "not authenticated");
        }
    }

    final Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
        @Override
        public void onAuthenticated(AuthData authData) {
            Log.d("d", "user just logged in, sending to expenses");
            Log.d("d", "auth uid " + authData.getUid());
            controller.goToExpenses();
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            Log.d("d", "error when logging in");
            Log.d("d", firebaseError.getMessage());
            Log.d("d", firebaseError.getDetails());
            Toast.makeText(getActivity(), "Login Unsuccessful: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}
