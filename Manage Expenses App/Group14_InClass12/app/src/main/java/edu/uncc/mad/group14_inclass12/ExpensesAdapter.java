package edu.uncc.mad.group14_inclass12;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bradlamotte on 3/21/16.
 */
public class ExpensesAdapter extends ArrayAdapter<Expense> {
    List<Expense> expenses;
    Context context;
    int resource;

    public ExpensesAdapter(Context context, int resource, List<Expense> expenses) {
        super(context, resource, expenses);
        this.expenses = expenses;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource, parent, false);
        }

        Expense expense = this.expenses.get(position);
        TextView name = (TextView) convertView.findViewById(R.id.expense_name);
        name.setText(expense.getName());
        TextView amount = (TextView) convertView.findViewById(R.id.amount);
        amount.setText("$" + String.valueOf(expense.getAmount()));

        return convertView;
    }
}

