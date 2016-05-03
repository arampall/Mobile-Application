package edu.uncc.mad.group14_inclass12;

/**
 * Created by bradlamotte on 4/18/16.
 */
public interface Controller {
    void goToSignup();
    void goToLogin();
    void goToAddExpense();
    void goToExpense(Expense expense);
    void goToExpenses();
    void closeCurrent();
}
