package br.edu.opet.pi.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import br.edu.opet.pi.R;
import br.edu.opet.pi.receiver.Receiver;
import br.edu.opet.pi.data.DBHelper;
import br.edu.opet.pi.data.DBManager;

public class LoginCreation extends AppCompatActivity {
    private TextInputEditText username,password;
    private Button buttonCadastro;
    private Button btnlogin;
    private DBHelper DB;
    private DBManager DBManager;

    private Receiver receiver = new Receiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_creation);

        username =  findViewById(R.id.username1);
        password =  findViewById(R.id.password1);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        buttonCadastro = (Button) findViewById(R.id.buttonCadastro);

        DB = new DBHelper(this);

        DBManager = new DBManager(this);
        DBManager.open();

        btnlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String user_id = String.valueOf(DBManager.GetUserID(DBHelper.TABELA_USERS ,user));
                String pass = password.getText().toString();

                if (user.equals("")||pass.equals("")){
                    Toast.makeText(LoginCreation.this, "Informe ID e SENHA", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkuserpass = DB.checkupassword(user, pass);
                    if (checkuserpass == true){
                        Toast.makeText(LoginCreation.this, "Credenciais válidas", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginCreation.this, WelcomeActivity.class);

                        intent.putExtra("user_name",user);
                        intent.putExtra("userId",user_id);

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
                Intent intent = new Intent(LoginCreation.this, CadastroActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


}