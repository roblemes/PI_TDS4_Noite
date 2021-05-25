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
    public void insert(String name, String desc){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.TAREFAS_SUBJECT, name);
        contentValues.put(DBHelper.TAREFAS_DESC, desc);
        database.insert(DBHelper.TABELA_TAREFAS, null,contentValues);
    }

    public Cursor fetch(){
        String[] columns = new String[]{DBHelper.TAREFAS_ID,
                DBHelper.TAREFAS_SUBJECT,DBHelper.TAREFAS_DESC};

        Cursor cursor = database.query(DBHelper.TABELA_TAREFAS,
                columns, null, null,
                null,
                null,
                null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
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

}
