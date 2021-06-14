package br.edu.opet.pi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import br.edu.opet.pi.data.DBHelper;
import br.edu.opet.pi.data.DBManager;

public class WelcomeActivity extends AppCompatActivity{

    private Context context = this;
    private DBManager dbManager;
    private ListView listView_projects;
    private SimpleCursorAdapter adapter_projects;

    final String[] from = new String[]  {
            DBHelper.PROJECTS_ID, DBHelper.PROJECTS_NAME
    };
    final int[] to = new int[] {R.id.id, R.id.name};
    private Button btn_dashboard;
    private Button btn_add_project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_dashboard = (Button) findViewById(R.id.btn_dashboard);
        btn_add_project = (Button) findViewById(R.id.btn_add_project);
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch_projects();
        listView_projects = findViewById(R.id.list_view_projects);
        listView_projects.setEmptyView(findViewById(R.id.empty_projects));
        adapter_projects = new SimpleCursorAdapter(this, R.layout.activity_view_project,
                cursor, from , to , 0 );
        adapter_projects.notifyDataSetChanged();
        listView_projects.setAdapter(adapter_projects);

        btn_dashboard.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            voltar();
        }
    }
    );
        btn_add_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogLayout = inflater.inflate(R.layout.dialog_add_project, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogLayout);
                final EditText userInput = (EditText) dialogLayout.findViewById(R.id.edit_project_name);

                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String project_name = userInput.getText().toString();
                        dbManager.insert_project(project_name);
                        dialog.dismiss();
                    }
                }
                );
                AlertDialog customAlertDialog = builder.create();
                customAlertDialog.show();
            }
        });
        listView_projects.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewid) {

                SQLiteCursor sql = (SQLiteCursor) listView_projects.getItemAtPosition(position);
                String id = sql.getString(sql.getColumnIndex("_id"));
                String name = sql.getString(sql.getColumnIndex("name"));

                Intent modify_intent = new Intent(getApplicationContext(),
                        DashBoard.class);

                modify_intent.putExtra("name",name);
                modify_intent.putExtra("id",id);

                startActivity(modify_intent);
            }
        });
    }

    public void openDialog(){
        DialogAddProject dialogAddProject = new DialogAddProject();
        dialogAddProject.show(getSupportFragmentManager(),"dialog project");
    }
    public void voltar() {
        Intent intent = new Intent(this, DashBoard.class);
        startActivity(intent);
    }

}