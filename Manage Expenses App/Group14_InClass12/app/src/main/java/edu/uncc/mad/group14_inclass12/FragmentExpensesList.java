package edu.uncc.mad.group14_inclass12;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by jesus on 4/18/16.
 */
public class FragmentExpensesList extends Fragment{


    //static String FIREBASE_URL = "https://sweltering-torch-9125.firebaseio.com/";
    Controller controller;
    Firebase rootRef = new Firebase(MainActivity.FIREBASE_ENDPOINT);
    Firebase ref = rootRef.child("expenses");
    static String EXPENSE_KEY = "expense";
    static String EXPENSE_KEY_KEY = "expense_key";
    ListView listView;

    final ArrayList<Expense> expenses = new ArrayList<>();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_expenses_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(getActivity());
        final Firebase fbRef = new Firebase(MainActivity.FIREBASE_ENDPOINT);
        listView = (ListView) getView().findViewById(R.id.expense_listview);
        Log.d("d", "listview " + listView.toString());
        final ExpensesAdapter adapter = new ExpensesAdapter(getActivity(), R.layout.expenses_listview, expenses);
        adapter.setNotifyOnChange(true);
        //setListAdapter(adapter);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense expense = expenses.get(position);
                Log.d("d", "sending expense " + expense.toString());
                controller.goToExpense(expense);
            }
        });

        AuthData authData = rootRef.getAuth();
        ref.orderByChild("user_email").equalTo(authData.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //expenses.clear();
                adapter.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Expense expense = postSnapshot.getValue(Expense.class);
                    adapter.add(expense);
                    Log.d("d", "expenses: " + expenses.toString());
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }

        });

    }



//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_expenses_list, container, false);
//        return view;
//    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//
//        Expense expense = expenses.get(position);
//        Log.d("d", "sending expense " + expense.toString());
//        Intent intent = new Intent(getActivity(), ExpenseDetails.class);
//        intent.putExtra(EXPENSE_KEY, expense);
//        intent.putExtra(EXPENSE_KEY_KEY, expense);
//        startActivity(intent);
//    }


//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        AuthData authData = rootRef.getAuth();
//        ref.orderByChild("user_email").equalTo(authData.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                expenses.clear();
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    Expense expense = postSnapshot.getValue(Expense.class);
//                    expenses.add(expense);
//                    Log.d("d", "expenses: " + expenses.toString());
//                }
//
//                ExpensesAdapter adapter = new ExpensesAdapter(getActivity(), R.layout.expenses_listview, expenses);
//                adapter.setNotifyOnChange(true);
//                setListAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
//            }
//
//        });
//    }

//    //Create Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            //Start add expense activity
            case R.id.menu_add_expense:
                controller.goToAddExpense();
                return true;


            //logout and return to login activity
            case R.id.menu_logout:
                Firebase ref = new Firebase(MainActivity.FIREBASE_ENDPOINT);
                ref.unauth();
                controller.goToLogin();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}