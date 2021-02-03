package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DatabaseHelper databaseHelper;

    public PersistentAccountDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbersList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ DatabaseHelper.KEY_ACCOUNTNUM +" FROM " + DatabaseHelper.ACCOUNTS_TABLE, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding account to list
                accountNumbersList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.ACCOUNTS_TABLE, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Double.parseDouble(cursor.getString(3))
                );
                // Adding account to list
                accountList.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {



        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.ACCOUNTS_TABLE + " WHERE " + DatabaseHelper.KEY_ACCOUNTNUM + " = ? ", new String[] {accountNo});

        if (cursor != null) {
            cursor.moveToFirst();
            Account account = new Account(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Double.parseDouble(cursor.getString(3))
            );
            cursor.close();
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_ACCOUNTNUM, account.getAccountNo()); // Account No
        values.put(DatabaseHelper.KEY_BANKNAME, account.getBankName()); // Bank Name
        values.put(DatabaseHelper.KEY_ACCOUNTHOLDER, account.getAccountHolderName()); // Holder Name
        values.put(DatabaseHelper.KEY_BALANCE, account.getBalance()); // Balance

        // Inserting Row
        db.insert(DatabaseHelper.ACCOUNTS_TABLE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DatabaseHelper.ACCOUNTS_TABLE, DatabaseHelper.KEY_ACCOUNTNUM + " = ?",
                new String[] { accountNo });
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if(accountNo == null){
            throw new InvalidAccountException("Account number is not valid");
        }

        Account account = this.getAccount(accountNo);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                values.put(DatabaseHelper.KEY_BALANCE, account.getBalance() - amount);
                break;
            case INCOME:
                values.put(DatabaseHelper.KEY_BALANCE, account.getBalance() + amount);
                break;
        }
        // updating row
        db.update(DatabaseHelper.ACCOUNTS_TABLE, values, DatabaseHelper.KEY_ACCOUNTNUM + " = ?",
                new String[] { accountNo });
    }
}
