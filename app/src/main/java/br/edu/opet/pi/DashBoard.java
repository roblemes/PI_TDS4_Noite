package br.edu.opet.pi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import br.edu.opet.pi.data.DBHelper;
import br.edu.opet.pi.data.DBManager;

public class DashBoard extends AppCompatActivity {

    private DBManager dbManager;
    private ListView listView;
    private SimpleCursorAdapter adapter;

    final String[] from = new String[]  {
            DBHelper.TAREFAS_ID, DBHelper.TAREFAS_SUBJECT, DBHelper.TAREFAS_DESC
    };

    final int[] to = new int[] {R.id.id, R.id.title, R.id.desc};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_dash_board);

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty_tasks));

        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record,
                cursor, from , to , 0 );
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewid) {

                SQLiteCursor sql = (SQLiteCursor) listView.getItemAtPosition(position);

                String id = sql.getString(sql.getColumnIndex("_id"));
                String desc = sql.getString(sql.getColumnIndex("description"));
                String title = sql.getString(sql.getColumnIndex("subject"));

                // alterar findbiewbyid
                /*
                TextView idTextView = view.findViewById(R.id.id);
                TextView titleTextView = view.findViewById(R.id.title);
                TextView descTextView = view.findViewById(R.id.desc);
                String id = idTextView.getText().toString();
                String title = titleTextView.getText().toString();
                String desc = descTextView.getText().toString(); */

                Intent modify_intent = new Intent(getApplicationContext(),
                        ModifyTaskActivity.class);

                modify_intent.putExtra("title",title);
                modify_intent.putExtra("desc",desc);
                modify_intent.putExtra("id",id);

                startActivity(modify_intent);
            }
        });
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //add_task
       if (id==R.id.add_task){
            Intent add_mem = new Intent(DashBoard.this, AddTaskActivity.class);
            startActivity(add_mem);
        }

        return super.onOptionsItemSelected(item);
    }
}