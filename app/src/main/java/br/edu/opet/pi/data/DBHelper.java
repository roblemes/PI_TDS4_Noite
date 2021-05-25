package br.edu.opet.pi.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "OrganizaAi.db";
    public static final String TABELA_USERS = "users";

    // TABELA TAREFA E COLUNAS
    public static final String TABELA_TAREFAS = "tarefas";
    public static final String TAREFAS_ID = "_id";
    public static final String TAREFAS_SUBJECT = "subject";
    public static final String TAREFAS_DESC = "description";

    private static final String CREATE_TABLE_TAREFAS = "create table " +
            TABELA_TAREFAS + "(" + TAREFAS_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TAREFAS_SUBJECT+ " TEXT NOT NULL, " +
            TAREFAS_DESC+ " TEXT );";

    private static final String CREATE_TABLE_USERS = "create table " +
            TABELA_USERS + "( user_id "
            + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT, password TEXT );";

    public DBHelper (Context context){
        super(context, DBNAME, null, 1);
    }
    public void onCreate (SQLiteDatabase MyDB) {
        MyDB.execSQL(CREATE_TABLE_USERS);
        MyDB.execSQL(CREATE_TABLE_TAREFAS);
    }
    public void onUpgrade (SQLiteDatabase MyDB, int i, int i1){
        MyDB.execSQL("drop Table if exists "+TABELA_USERS);
        MyDB.execSQL("drop Table if exists "+TABELA_TAREFAS);
        onCreate(MyDB);
    }
    public Boolean insertData(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put ("username", username);
        contentValues.put ("password", password);
        long result = MyDB.insert("users", null, contentValues);
        if (result == 1) return false;
        else
            return true;
    }
    public Boolean checkusername (String username){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[] {username});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public Boolean checkupassword (String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username, password});
        if (cursor.getCount()>0)
            return true;
        else
            return false;

    }

}
