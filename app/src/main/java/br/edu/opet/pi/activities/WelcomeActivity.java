package br.edu.opet.pi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import br.edu.opet.pi.R;
import br.edu.opet.pi.data.DBHelper;
import br.edu.opet.pi.data.DBManager;

public class WelcomeActivity extends AppCompatActivity {

    private TextView user, email, endereco;
    //private DBManager dbManager;
    private MaterialButton btnTarefas, btnSair;
    private Intent intent;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        user = findViewById(R.id.txtvUsername);
        imageView = findViewById(R.id.imageViewId);
        email = findViewById(R.id.txtvEmail);
        endereco = findViewById(R.id.txtvEndereco);
        btnTarefas = findViewById(R.id.btnTarefas);
        btnSair = findViewById(R.id.btnSair);

        intent = getIntent();
        String user_id = intent.getStringExtra("userId");
        String user_avatar = intent.getStringExtra("user_avatar");
        String user_email = intent.getStringExtra("user_email");
        String user_name = intent.getStringExtra("user_name");
        String user_end = intent.getStringExtra("user_end");
        String user_bairro = intent.getStringExtra("user_bairro");
        String user_cidade = intent.getStringExtra("user_cidade");
        String user_estado = intent.getStringExtra("user_estado");

        Picasso.get().load(user_avatar).into(imageView);

        //dbManager = new DBManager(this);
        //dbManager.open();
        //Cursor cursor = dbManager.findUserById(user_id);

        email.setText(user_email);
        user.setText(user_name);

        StringBuilder sb = new StringBuilder();
        sb.append(user_end + ", " +user_cidade + ", " +user_bairro+ ", " + user_estado);
        endereco.setText(sb);
        /*
        StringBuilder sb = new StringBuilder();
        sb.append(cursor.getString( cursor.getColumnIndex("endereco") )
        + ", " + cursor.getString( cursor.getColumnIndex("bairro") )
                + ", " + cursor.getString( cursor.getColumnIndex("cidade") )
                        + ", " + cursor.getString( cursor.getColumnIndex("estado") )
        );
        endereco.setText(sb);
*/

        btnTarefas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(getApplicationContext(), DashBoard.class);

                intent.putExtra("user_email",user_email);
                intent.putExtra("userId",user_id);

                startActivity(intent);
            }
        });
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(getApplicationContext(), LoginCreation.class);
                startActivity(intent);
            }
        });

    }
}