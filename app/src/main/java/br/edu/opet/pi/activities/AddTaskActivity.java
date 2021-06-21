package br.edu.opet.pi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.edu.opet.pi.R;
import br.edu.opet.pi.data.DBManager;

public class AddTaskActivity extends Activity  {

    private Button addTodoBtn;
    private EditText subjectEditText;
    private EditText descEditText;
    private DBManager dbManager;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Adicionar tarefa");
        setContentView(R.layout.activity_add_task);

        subjectEditText = findViewById(R.id.subject_edittext);
        descEditText = findViewById(R.id.description_edittext);
        addTodoBtn = findViewById(R.id.add_record);

        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = getIntent();

                String assigned_user_id = intent.getStringExtra("assigned_user_id");

                final String name = subjectEditText.getText().toString();
                final String desc = descEditText.getText().toString();

                if (name.equals("") || desc.equals("")){
                    Toast.makeText(AddTaskActivity.this , "Preencha os campos para inserir", Toast.LENGTH_LONG).show();
                }else{
                    dbManager.insert(name,desc,assigned_user_id);

                    Toast.makeText(AddTaskActivity.this , "Tarefa inserida", Toast.LENGTH_LONG).show();

                    Intent main = new Intent(AddTaskActivity.this,
                            DashBoard.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    setResult(RESULT_OK,main);
                    finish();
                }

            }
        });
    }


}