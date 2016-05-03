package edu.uncc.mad.group14_inclass12;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    Controller controller;
    Expense expense;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
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

    public void setExpense(Expense expense){
        this.expense = expense;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //expense = (Expense) getActivity()getIntent().getExtras().getSerializable(ExpensesList.EXPENSE_KEY);
        //expense_key = getIntent().getExtras().getString(ExpensesList.EXPENSE_KEY_KEY);
        //Log.d("d", "expense key " + expense_key);

        TextView textView;
        textView = (TextView) getActivity().findViewById(R.id.details_name_value);
        textView.setText(expense.getName());

        textView = (TextView) getActivity().findViewById(R.id.details_category_value);
        textView.setText(expense.getCategory());
        textView = (TextView) getActivity().findViewById(R.id.details_amount_value);
        textView.setText(String.valueOf(expense.getAmount()));
        textView = (TextView) getActivity().findViewById(R.id.details_date_value);
        textView.setText(expense.getDate().toString());
    }

    //    //Create Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            //Start add expense activity
            case R.id.delete_expense:
                Firebase ref = new Firebase(MainActivity.FIREBASE_ENDPOINT);
                ref.child("expenses").child(expense.getKey()).setValue(null, new Firebase.CompletionListener() {

                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        Toast.makeText(getActivity(), "Expense successfully deleted", Toast.LENGTH_SHORT).show();
                        controller.closeCurrent();
                    }
                });

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
