package edu.uncc.mad.group14_inclass12;


//import android.os.Bundle;
//import android.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class AddExpenseFragment extends Fragment {
//
//
//    public AddExpenseFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_expense, container, false);
//    }
//
//}

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class AddExpenseFragment extends Fragment {
    Spinner spinner;
    TextView text;
    Controller controller;

    public AddExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Firebase.setAndroidContext(getContext());
        spinner = (Spinner) getActivity().findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.categories,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        getActivity().findViewById(R.id.button_expense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase ref = new Firebase(MainActivity.FIREBASE_ENDPOINT);
                ref.child("expenses/");

                AuthData authData = ref.getAuth();

                Expense expense = new Expense();
                expense.setUser_email(authData.getUid());
                text = (EditText) getActivity().findViewById(R.id.name_value);
                expense.setName(text.getText().toString());
                text = (EditText) getActivity().findViewById(R.id.amount_value);
                if (!text.getText().toString().isEmpty())
                    expense.setAmount(Float.parseFloat(text.getText().toString()));
                text = (EditText) getActivity().findViewById(R.id.date_value);
                expense.setDate(text.getText().toString());
                expense.setCategory(spinner.getSelectedItem().toString());
                if (expense.isEmpty()) {
                    Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                } else {

                    Firebase fbNewExpense = ref.child("expenses").push();
                    expense.setKey(fbNewExpense.getKey());
                    fbNewExpense.setValue(expense, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            controller.closeCurrent();
                        }
                    });
                }
            }
        });
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
}
