package br.edu.opet.pi.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import br.edu.opet.pi.R;
import br.edu.opet.pi.model.User;
import br.edu.opet.pi.data.DBHelper;
import br.edu.opet.pi.data.DBManager;
import br.edu.opet.pi.services.RESTService;
import br.edu.opet.pi.services.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginCreation extends AppCompatActivity {
    private TextInputEditText userEmail, password;
    private Button buttonCadastro;
    private Button btnlogin;
    private DBHelper DB;
    private DBManager DBManager;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_creation);

        userEmail = findViewById(R.id.inputUserEmail);
        password = findViewById(R.id.password1);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        buttonCadastro = (Button) findViewById(R.id.buttonCadastro);

        DB = new DBHelper(this);

        DBManager = new DBManager(this);
        DBManager.open();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress_init();
                //valida_login();
                retrofitConverter();
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

    public void retrofitConverter() {

        String s_email = userEmail.getText().toString().trim();
        String user_email = userEmail.getText().toString();
        RESTService service = ServiceGenerator.createService(RESTService.class);
        String user_id = String.valueOf(DBManager.GetUserID(DBHelper.TABELA_USERS, user_email));
        String pass = password.getText().toString();

        Call<List<User>> call = service.consultarUser(s_email);

        if (user_email.equals("") || pass.equals("")){

            Toast.makeText(getApplicationContext(), "Erro: Preencha os campos email e senha", Toast.LENGTH_LONG).show();

        }else{
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                    if (response.isSuccessful()) {
                        List<User> userOut = response.body();

                        //verifica aqui se o corpo da resposta não é nulo
                        if (userOut != null) {
                            progress.dismiss();
                            String email_st = userEmail.getText().toString();
                            String pass_st = password.getText().toString();

                            //loop para percorrer o array users que está no json da api rest
                            for (User u : response.body()) {

                                String v_email = u.getEmail();
                                String v_pass = u.getPassword();

                                //verifica se o e mail inserido é igual ao atributo email do objeto json
                                if (v_email.equals(email_st)) {

                                    //verifica se a senha inserida é igual ao atributo password do objeto json
                                    if(pass_st.equals(v_pass)){

                                        //passa as informações dos atributos do objeto json para a welcome activity por intent
                                        Intent intent = new Intent(LoginCreation.this, WelcomeActivity.class);
                                        intent.putExtra("user_email", user_email);
                                        intent.putExtra("user_name", u.getName());
                                        intent.putExtra("user_avatar", u.getAvatar());
                                        intent.putExtra("user_end", u.getEndereco());
                                        intent.putExtra("user_bairro", u.getBairro());
                                        intent.putExtra("user_cidade", u.getCidade());
                                        intent.putExtra("user_estado", u.getEstado());
                                        intent.putExtra("userId", user_id);

                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Erro: Senha inválida", Toast.LENGTH_LONG).show();
                                    }
                                }
                                 else {
                                    Toast.makeText(getApplicationContext(), "Erro: Insira um e-mail válido", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Resposta não foi sucesso", Toast.LENGTH_SHORT).show();
                        // segura os erros de requisição
                        // ResponseBody errorBody = response.errorBody();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), "Erro na chamada ao servidor", Toast.LENGTH_SHORT).show();
                    Log.d("ERROR", t.getMessage());
                    progress.dismiss();
                }
            });

        }

    }



    public void progress_init() {
        progress = new ProgressDialog(LoginCreation.this);
        progress.setTitle("enviando...");
        progress.show();
    }
}