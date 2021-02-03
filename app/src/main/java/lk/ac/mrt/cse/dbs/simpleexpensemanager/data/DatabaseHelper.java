package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "180044L";
    private static final int DATABASE_VERSION = 7;
    //tables
    public static final String ACCOUNTS_TABLE = "accounts";
    public static final String TRANSACTIONS_TABLE = "transactions";

    //keys

    public static final String KEY_BANKNAME = "bankName";
    public static final String KEY_BALANCE = "balance";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_ACCOUNTHOLDER = "accountHolderName";
    private static final String KEY_TRANSACTIONID = "id";
    public static final String KEY_DATE = "transaactiondate";
    public static final String KEY_ACCOUNTNUM = "accountNo";
    public static final String KEY_EXPENSETYPE = "expenseType";

    //account
    private static final String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + ACCOUNTS_TABLE + "("
            + KEY_ACCOUNTNUM + " TEXT PRIMARY KEY," + KEY_BANKNAME + " TEXT,"
            + KEY_ACCOUNTHOLDER + " TEXT," + KEY_BALANCE + " REAL" + ")";

    //transaction
    private static final String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TRANSACTIONS_TABLE + "("
            + KEY_TRANSACTIONID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + " TEXT," + KEY_ACCOUNTNUM + " TEXT,"
            + KEY_EXPENSETYPE + " TEXT," + KEY_AMOUNT + " REAL," + "FOREIGN KEY(" + KEY_ACCOUNTNUM +
            ") REFERENCES "+ ACCOUNTS_TABLE +"(" + KEY_ACCOUNTNUM + ") )";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNTS_TABLE);
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS '" + ACCOUNTS_TABLE + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TRANSACTIONS_TABLE + "'");

        // recreation
        onCreate(db);
    }
}
