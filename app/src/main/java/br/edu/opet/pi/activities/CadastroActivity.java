package br.edu.opet.pi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import br.edu.opet.pi.R;
import br.edu.opet.pi.data.DBManager;
import br.edu.opet.pi.services.CreateUserService;
import br.edu.opet.pi.services.RESTService;
import br.edu.opet.pi.model.CEP;
import br.edu.opet.pi.data.DBHelper;
import br.edu.opet.pi.util.Mascara;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CadastroActivity extends AppCompatActivity {
    private final String URL = "https://viacep.com.br/ws/";

    private Retrofit retrofitCEP;
    private TextInputEditText username, email, password, repassword;
    private TextInputEditText txtCEP, txtUF, txtLocalidade, txtLogradouro, txtBairro;
    private TextInputLayout layCEP;
    private Button signup, btnConsultarCEP;
    private DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        txtCEP = findViewById(R.id.txtCEP);
        layCEP = findViewById(R.id.layoutCEP);
        txtLogradouro = findViewById(R.id.txtLogradouro);
        txtBairro = findViewById(R.id.txtBairro);
        txtUF = findViewById(R.id.txtUf);
        txtLocalidade = findViewById(R.id.txtLocalidade);
        signup = (Button) findViewById(R.id.signup);
        btnConsultarCEP = (Button) findViewById(R.id.btnConsultarCEP);
        DB = new DBHelper(this);

        //Aplicando a máscara para CEP
        txtCEP.addTextChangedListener(Mascara.insert(Mascara.MASCARA_CEP, txtCEP));

        //configura os recursos do retrofit
        retrofitCEP = new Retrofit.Builder()
                .baseUrl(URL)                                       //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();

        btnConsultarCEP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnConsultarCEP:
                        if (validarCamposCEP()) {
                            esconderTeclado();
                            consultarCEP();
                        }
                        break;
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
            }

        });
    }

    private void validarCampos() {
        //DBManager.open();
        String user = username.getText().toString();
        String e_mail = email.getText().toString();
        String pass = password.getText().toString();
        String repas = repassword.getText().toString();
        String cep = txtCEP.getText().toString();
        String endereco = txtLogradouro.getText().toString();
        String bairro = txtBairro.getText().toString();
        String estado = txtUF.getText().toString();
        String cidade = txtLocalidade.getText().toString();
        if (user.equals("") || pass.equals("") || repas.equals("") ||
                e_mail.equals("") || cep.equals("") || endereco.equals("") || bairro.equals("") ||
                estado.equals("") || cidade.equals("")) {
            Toast.makeText(CadastroActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
        } else {
            if (pass.equals(repas)) {
                Boolean checkuser = DB.checkemail(e_mail);
                if (checkuser == false) {
                    Boolean insert = DB.insertData(user, e_mail, pass,
                            endereco, cidade, estado, cep, bairro);
                    if (insert == true) {
                        Toast.makeText(CadastroActivity.this, "Cadastro realizado!", Toast.LENGTH_SHORT).show();

                        //Método para fazer requisição de forma assincrona
                        //createUser(user, e_mail);

                        //Chama o service que faz a requisição de forma síncrona na background thread
                        callService();

                        Intent loginIntent = new Intent(CadastroActivity.this, LoginCreation.class);
                        startActivity(loginIntent);

                    } else {
                        Toast.makeText(CadastroActivity.this, "Falha no cadastro", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroActivity.this, "Usuario já existe", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CadastroActivity.this, "Senhas não correspondem", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void callService() {
        Intent intent = new Intent(CadastroActivity.this, CreateUserService.class);
        String user = username.getText().toString();
        String e_mail = email.getText().toString();
        String u_pass = password.getText().toString();
        String u_end = txtLogradouro.getText().toString();
        String u_bairro = txtBairro.getText().toString();
        String u_cidade = txtLocalidade.getText().toString();
        String u_estado = txtUF.getText().toString();
        intent.putExtra("username", user);
        intent.putExtra("useremail", e_mail);
        intent.putExtra("userpass", u_pass);
        intent.putExtra("userend", u_end);
        intent.putExtra("userbairro", u_bairro);
        intent.putExtra("usercidade", u_cidade);
        intent.putExtra("userestado", u_estado);
        startService(intent);
    }

    private Boolean validarCamposCEP() {

        Boolean status = true;
        String cep = txtCEP.getText().toString().trim();

        if (cep.isEmpty()) {
            txtCEP.setError("Digite um CEP válido.");
            status = false;
        }

        if ((cep.length() > 1) && (cep.length() < 10)) {
            txtCEP.setError("O CEP deve possuir 8 dígitos");
            status = false;
        }
        return status;
    }

    private void esconderTeclado() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void consultarCEP() {
        String sCep = txtCEP.getText().toString().trim();
        //removendo o ponto e o traço do padrão CEP
        sCep = sCep.replaceAll("[.-]+", "");
        //instanciando a interface
        RESTService restService = retrofitCEP.create(RESTService.class);
        //passando os dados para consulta
        Call<CEP> call = restService.consultarCEP(sCep);
        //colocando a requisição na fila para execução
        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {
                // if (response.isSuccessful()) {
                CEP cep = response.body();
                txtLogradouro.setText(cep.getLogradouro());
                txtBairro.setText(cep.getBairro());
                txtUF.setText(cep.getUf());
                txtLocalidade.setText(cep.getLocalidade());
                Toast.makeText(getApplicationContext(), "CEP consultado com sucesso", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro ao tentar consultar o CEP. Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
/*
    private void createUser(String username, String email) {

        RESTService restService = ServiceGenerator.createService(RESTService.class);
        User user = new User(username, null, email);
        Call<User> call = restService.createUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Codigo: " + response.code(), Toast.LENGTH_LONG).show();
                    Log.d("Message: ", response.message());
                    return;
                }
                Log.d("Sucesso: ", response.message());
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro ao tentar realizar o PUT. Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.getMessage());
            }
        });
    }

 */

}