package br.edu.opet.pi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import br.edu.opet.pi.R;
import br.edu.opet.pi.data.DBHelper;
import br.edu.opet.pi.data.DBManager;

public class WelcomeActivity extends AppCompatActivity {

    private TextView user, email, endereco;
    private DBManager dbManager;
    private MaterialButton btnTarefas, btnSair;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        user = findViewById(R.id.txtvUsername);
        email = findViewById(R.id.txtvEmail);
        endereco = findViewById(R.id.txtvEndereco);
        btnTarefas = findViewById(R.id.btnTarefas);
        btnSair = findViewById(R.id.btnSair);

        intent = getIntent();
        String user_id = intent.getStringExtra("userId");
        String user_name = intent.getStringExtra("user_name");

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.findUserById(user_id);

        email.setText(cursor.getString( cursor.getColumnIndex("email") ));
        user.setText(cursor.getString( cursor.getColumnIndex("username") ));

        StringBuilder sb = new StringBuilder();
        sb.append(cursor.getString( cursor.getColumnIndex("endereco") )
        + ", " + cursor.getString( cursor.getColumnIndex("bairro") )
                + ", " + cursor.getString( cursor.getColumnIndex("cidade") )
                        + ", " + cursor.getString( cursor.getColumnIndex("estado") )
        );
        endereco.setText(sb);


        btnTarefas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(getApplicationContext(), DashBoard.class);

                intent.putExtra("user_name",user_name);
                intent.putExtra("userId",user_id);

                startActivity(intent);
            }
        });
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(getApplicationContext(), LoginCreation.class);

                //intent.putExtra("user_name",user_name);
                //intent.putExtra("userId",user_id);

                startActivity(intent);
            }
        });

    }
}