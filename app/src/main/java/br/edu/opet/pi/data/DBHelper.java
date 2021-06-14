package br.edu.opet.pi.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "OrganizaAi.db";
    public static final String TABELA_USERS = "users";
    public static final String USERS_ID = "_id";

    // TABELA TAREFA E COLUNAS
    public static final String TABELA_TAREFAS = "tarefas";
    public static final String TAREFAS_ID = "_id";
    public static final String TAREFAS_SUBJECT = "subject";
    public static final String TAREFAS_DESC = "description";

    // TABELA PROJECT
    public static final String TABELA_PROJECTS = "projects";
    public static final String PROJECTS_ID = "_id";
    public static final String PROJECTS_NAME = "name";

    // TABELA USERS PROJECT
    public static final String TABELA_USERS_PROJECTS = "users_projects";
    public static final String USERS_PROJECTS_ID = "_id";
    public static final String USERS_PROJECTS_USER_ID = "user_id";
    public static final String USERS_PROJECTS_PROJECTS_ID = "project_id";

    private static final String CREATE_TABLE_TAREFAS = "create table " +
            TABELA_TAREFAS + "(" + TAREFAS_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TAREFAS_SUBJECT+ " TEXT NOT NULL, " +
            TAREFAS_DESC+ " TEXT, " +
            "project_id INTEGER NOT NULL, " +
            "assigned_user_id INTEGER NOT NULL, " +
            "FOREIGN KEY ( project_id ) REFERENCES " +TABELA_PROJECTS+"( "+PROJECTS_ID+" )," +
            "FOREIGN KEY ( assigned_user_id ) REFERENCES " +TABELA_USERS+" ( "+USERS_ID+" ));";

    private static final String CREATE_TABLE_USERS = "create table " +
            TABELA_USERS + "("  + USERS_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT NOT NULL, email TEXT NOT NULL, password TEXT NOT NULL );";

    private static final String CREATE_TABLE_PROJECTS = "create table " +
            TABELA_PROJECTS + "(" + PROJECTS_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PROJECTS_NAME + " TEXT NOT NULL );";

    private static final String CREATE_TABLE_USERS_PROJECTS = "create table " +
            TABELA_USERS_PROJECTS + "(" + USERS_PROJECTS_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USERS_PROJECTS_USER_ID + " INTEGER NOT NULL, " +
            USERS_PROJECTS_PROJECTS_ID + " INTEGER NOT NULL," +
            " FOREIGN KEY ( "+USERS_PROJECTS_USER_ID+" ) REFERENCES users( "+USERS_ID+" )," +
            " FOREIGN KEY ( "+USERS_PROJECTS_PROJECTS_ID+" ) REFERENCES projects( "+PROJECTS_ID+" ));";

    public DBHelper (Context context){
        super(context, DBNAME, null, 1);
    }

    public void onCreate (SQLiteDatabase MyDB) {
        MyDB.execSQL(CREATE_TABLE_USERS);
        MyDB.execSQL(CREATE_TABLE_PROJECTS);
        MyDB.execSQL(CREATE_TABLE_USERS_PROJECTS);
        MyDB.execSQL(CREATE_TABLE_TAREFAS);

    }
    public void onUpgrade (SQLiteDatabase MyDB, int i, int i1){
        MyDB.execSQL("drop Table if exists "+TABELA_USERS);
        MyDB.execSQL("drop Table if exists "+TABELA_TAREFAS);
        MyDB.execSQL("drop Table if exists "+TABELA_PROJECTS);
        MyDB.execSQL("drop Table if exists "+TABELA_USERS_PROJECTS);
        onCreate(MyDB);
    }
    public Boolean insertData(String username, String email, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put ("username", username);
        contentValues.put ("email", email);
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
