package br.edu.opet.pi.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context c){
        context = c;
    }

    public DBManager open() throws SQLException{
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public void insert(String name, String desc, String assigned_user){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.TAREFAS_SUBJECT, name);
        contentValues.put(DBHelper.TAREFAS_DESC, desc);
        contentValues.put(DBHelper.TAREFAS_ASSIGNED_USER, assigned_user);
        database.insert(DBHelper.TABELA_TAREFAS, null,contentValues);
    }

    public Cursor fetch(String assigned_user_id){
        String[] columns = new String[]{DBHelper.TAREFAS_ID,
                DBHelper.TAREFAS_SUBJECT,DBHelper.TAREFAS_DESC,
        DBHelper.TAREFAS_ASSIGNED_USER};

        String where = "assigned_user_id LIKE '%"+assigned_user_id+"%'";

        Cursor cursor = database.query(DBHelper.TABELA_TAREFAS,
                columns, where, null,
                null,
                null,
                null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor findUserById(String usernameId){
        String[] colunas = new String[]{DBHelper.USERS_ID,
                "username",
                "email",
                "password",
                "endereco",
                "cidade",
                "estado",
                "cep",
                "bairro"};
        String where = "_id LIKE '%"+usernameId+"%'";

        Cursor c = database.query(true, "users", colunas,
                where, null, null, null, null, null);

        if(c != null){
            c.moveToFirst();
        }
        return c;
    }

public int update(long _id, String name, String desc){
    ContentValues contentValues = new ContentValues();
    contentValues.put(DBHelper.TAREFAS_SUBJECT, name);
    contentValues.put(DBHelper.TAREFAS_DESC, desc);

    int i = database.update(DBHelper.TABELA_TAREFAS,
            contentValues, DBHelper.TAREFAS_ID +
                    " = " + _id, null
            );
return i;
}
public void delete(long _id){
        database.delete(DBHelper.TABELA_TAREFAS,
                DBHelper.TAREFAS_ID + " = " + _id, null);
}

    public Long GetUserID(String tableName,String user_email) {
        String where = "email LIKE '%"+user_email+"%'";
        String[] colunas = new String[]{DBHelper.USERS_ID,
        "username",
        "email",
        "password",
        "endereco",
        "cidade",
        "estado",
        "cep",
        "bairro"};

        Cursor c = database.query(true, tableName, colunas,
                 where, null, null, null, null, null);

        if(c != null && c.moveToFirst())
            return c.getLong(c.getColumnIndex(dbHelper.USERS_ID));
        else
            return null;
    }

}
