package br.edu.opet.pi.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.edu.opet.pi.R;
import br.edu.opet.pi.data.DBManager;

public class ModifyTaskActivity extends Activity implements View.OnClickListener {

    private EditText titleText;
    private Button updateBtn, deleteBtn;
    private EditText descText;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    private long _id;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Task");
        setContentView(R.layout.activity_modify_task);

        dbManager = new DBManager(this);
        dbManager.open();

        titleText = findViewById(R.id.subject_edittext);
        descText = findViewById(R.id.description_edittext);

        updateBtn = findViewById(R.id.btn_update);
        deleteBtn = findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");

        _id = Long.parseLong(id);

        titleText.setText(name);
        descText.setText(desc);
        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_update:
                String title = titleText.getText().toString();
                String desc = descText.getText().toString();
                dbManager.update(_id, title, desc);
                Toast.makeText(ModifyTaskActivity.this , "Tarefa atualizada", Toast.LENGTH_LONG).show();

                this.returnHome();
                break;
            case R.id.btn_delete:

                openDialog();
                break;
        }
    }
public void openDialog(){

    builder = new AlertDialog.Builder(ModifyTaskActivity.this);
    builder.setTitle("Excluir Tarefa");
    builder.setMessage("Tem certeza que deseja excluir a tarefa?");

    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dbManager.delete(_id);
            Toast.makeText(ModifyTaskActivity.this , "Tarefa excluida", Toast.LENGTH_LONG).show();
            returnHome();
        }
    });

    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    });
    dialog = builder.create();
    dialog.show();

}

    public void returnHome(){
        Intent home_intent = new Intent(getApplicationContext(),
                DashBoard.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                setResult(RESULT_OK,home_intent);
                finish();
    }
}