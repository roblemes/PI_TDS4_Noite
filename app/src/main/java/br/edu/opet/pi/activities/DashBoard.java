package br.edu.opet.pi.activities;

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
import android.widget.Toast;

import br.edu.opet.pi.R;
import br.edu.opet.pi.data.DBHelper;
import br.edu.opet.pi.data.DBManager;

public class DashBoard extends AppCompatActivity {

    private DBManager dbManager;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    private Intent intent;

    final String[] from = new String[]  {
            DBHelper.TAREFAS_ID, DBHelper.TAREFAS_SUBJECT, DBHelper.TAREFAS_DESC
    };
    final int[] to = new int[] {R.id.id, R.id.title, R.id.desc};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK){
                dbManager = new DBManager(this);
                dbManager.open();

                intent = getIntent();
                String assigned_user_id = intent.getStringExtra("userId");

                Cursor cursor = dbManager.fetch(assigned_user_id);

                listView = findViewById(R.id.list_view);
                listView.setEmptyView(findViewById(R.id.empty_tasks));

                adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record,
                        cursor, from , to , 0 );

                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        }
    }

    /* @    requires Abrir as tarefas compartilhadas com o usuário logado
    @       ensures Listar as tarefas criadas para um usuário.
    @       se nada existir. Tela em Branco.
    @*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_dash_board);

        dbManager = new DBManager(this);
        dbManager.open();

        intent = getIntent();
        String assigned_user_id = intent.getStringExtra("userId");

        Cursor cursor = dbManager.fetch(assigned_user_id);

        listView = findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty_tasks));

        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record,
                cursor, from , to , 0 );

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            /*@  requires Usuário permitido alterar as tarefas
            @    ensures Disponibilidade do botão alterar
            @    método onClick que inicia uma Activity de mudança de conteúdo da
            @    tarefa
            @*/

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewid) {

                SQLiteCursor sql = (SQLiteCursor) listView.getItemAtPosition(position);

                String id = sql.getString(sql.getColumnIndex("_id"));
                String desc = sql.getString(sql.getColumnIndex("description"));
                String title = sql.getString(sql.getColumnIndex("subject"));

                Intent modify_intent = new Intent(getApplicationContext(),
                        ModifyTaskActivity.class);

                modify_intent.putExtra("title",title);
                modify_intent.putExtra("desc",desc);
                modify_intent.putExtra("id",id);

                startActivityForResult(modify_intent,1);
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
            String user_id = intent.getStringExtra("userId");
            String user_name = intent.getStringExtra("user_name");
            String assigned_user_id = intent.getStringExtra("userId");

            intent = new Intent(DashBoard.this, AddTaskActivity.class);

            intent.putExtra("assigned_user_id",assigned_user_id);
            intent.putExtra("user_name",user_name);
            intent.putExtra("userId",user_id);
            startActivityForResult(intent,1);

        }
        return super.onOptionsItemSelected(item);
    }
}