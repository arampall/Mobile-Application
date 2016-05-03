package edu.uncc.mad.group14_inclass12;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity implements Controller {
    static String FIREBASE_ENDPOINT = "https://sweltering-torch-9125.firebaseio.com/";
    static String FRAGMENT_TAG_LOGIN = "login_fragment";
    static String FRAGMENT_TAG_SIGNUP = "signup_fragment";
    static String FRAGMENT_TAG_EXPENSES = "expenses_fragment";
    static String FRAGMENT_TAG_ADD_EXPENSE = "add_expense_fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().add(R.id.fragment_container, new LoginFragment(), FRAGMENT_TAG_LOGIN).commit();
    }

    @Override
    public void goToSignup() {
        getFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, new SignupFragment(), FRAGMENT_TAG_SIGNUP).
                addToBackStack(null).
                commit();
    }

    @Override
    public void goToLogin() {
        getFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, new LoginFragment(), FRAGMENT_TAG_LOGIN).
                commit();
    }

    @Override
    public void goToExpenses() {
        //getFragmentManager().beginTransaction().remove(new FragmentExpensesList());

        getFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, new FragmentExpensesList(), FRAGMENT_TAG_EXPENSES).
                commit();
    }

    @Override
    public void goToExpense(Expense expense) {
        DetailsFragment f = new DetailsFragment();
        f.setExpense(expense);
        getFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, f, FRAGMENT_TAG_SIGNUP).
                addToBackStack(null).
                commit();
    }

    @Override
    public void goToAddExpense() {
        getFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, new AddExpenseFragment(), FRAGMENT_TAG_ADD_EXPENSE).
                addToBackStack(null).
                commit();
    }

    @Override
    public void closeCurrent() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed(){
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }



}
