package br.edu.opet.pi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.edu.opet.pi.data.DBHelper;


public class MainActivity extends AppCompatActivity {
    EditText username, password, repassword;
    Button signup;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username  = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.signup);
        DB = new DBHelper(this);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass  = password.getText().toString();
                String repas = repassword.getText().toString();

                if (user.equals("") || pass.equals("")||repas.equals(""))
                Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else
                    if (pass.equals(repas)){
                        Boolean checkuser = DB.checkusername(user);
                        if (checkuser == false){
                            Boolean insert = DB.insertData(user, pass);
                            if (checkuser == true) {
                                Toast.makeText(MainActivity.this, "Registered succesfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, DashBoard.class);
                                startActivity(intent);

                            }else{
                                Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }

                        }

                    } else
                    {
                        Toast.makeText(MainActivity.this, "User already Exist", Toast.LENGTH_SHORT).show();
                    }



            }
        });

    }

    public void onClick (View v){
        Intent intent = new Intent(MainActivity.this, DashBoard.class);
        startActivity(intent);
    }
}