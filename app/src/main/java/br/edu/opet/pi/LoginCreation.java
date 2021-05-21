package br.edu.opet.pi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.edu.opet.pi.data.DBHelper;

public class LoginCreation extends AppCompatActivity {
    EditText username,password;
    Button buttonCadastro;
    Button btnlogin;
    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_creation);

        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        buttonCadastro = (Button) findViewById(R.id.buttonCadastro);

        DB = new DBHelper(this);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.equals("")||pass.equals("")){
                    Toast.makeText(LoginCreation.this, "Informe ID e SENHA", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkuserpass = DB.checkupassword(user, pass);
                    if (checkuserpass == true){
                        Toast.makeText(LoginCreation.this, "Credenciais válidas", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginCreation.this, DashBoard.class);
                        // (FUNCIONANDO)Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginCreation.this, "Credenciais invalidas", Toast.LENGTH_SHORT).show();

                    }
                }


            }
        });
        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginCreation.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}