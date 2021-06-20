package br.edu.opet.pi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import br.edu.opet.pi.api.RESTService;
import br.edu.opet.pi.cep.CEP;
import br.edu.opet.pi.data.DBHelper;
import br.edu.opet.pi.util.Mascara;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;


public class CadastroActivity extends AppCompatActivity {
    private final String URL = "https://viacep.com.br/ws/";

    private Retrofit retrofitCEP;
    private TextInputEditText username, email, password, repassword;
    private TextInputEditText txtCEP, txtUF, txtLocalidade,txtLogradouro, txtBairro;
    private TextInputLayout layCEP;
    private Button signup, btnConsultarCEP;
    private DBHelper DB;
    private ProgressBar progressBarCEP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        username  = findViewById(R.id.username);
        email  = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        txtCEP = findViewById(R.id.txtCEP);
        layCEP = findViewById(R.id.layoutCEP);
        txtLogradouro =  findViewById(R.id.txtLogradouro);
        txtBairro =  findViewById(R.id.txtBairro);
        txtUF =  findViewById(R.id.txtUf);
        txtLocalidade = findViewById(R.id.txtLocalidade);
        signup = (Button) findViewById(R.id.signup);
        btnConsultarCEP = (Button) findViewById(R.id.btnConsultarCEP);
        progressBarCEP = findViewById(R.id.progressBarCEP);
        DB = new DBHelper(this);

        //configurando como invisível
        progressBarCEP.setVisibility(View.GONE);

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
                        if (validarCampos()) {
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
                String user = username.getText().toString();
                String e_mail = email.getText().toString();
                String pass  = password.getText().toString();
                String repas = repassword.getText().toString();
                String cep = txtCEP.getText().toString();
                String endereco = txtLogradouro.getText().toString();
                String bairro = txtBairro.getText().toString();
                String estado = txtUF.getText().toString();
                String cidade = txtLocalidade.getText().toString();

                if (user.equals("") || pass.equals("")||repas.equals("") ||
                        e_mail.equals("") || cep.equals("") || endereco.equals("") || bairro.equals("") ||
                        estado.equals("") || cidade.equals("")) {
                    Toast.makeText(CadastroActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                } else{
                    if (pass.equals(repas)){
                        Boolean checkuser = DB.checkusername(user);
                        if (checkuser == false){
                            Boolean insert = DB.insertData(user, e_mail, pass,
                                    endereco, cidade, estado, cep, bairro);
                            if (insert == true) {
                                Toast.makeText(CadastroActivity.this, "Cadastro realizado!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(CadastroActivity.this, LoginCreation.class);
                                startActivity(intent);

                            }else{
                                Toast.makeText(CadastroActivity.this, "Falha no cadastro", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(CadastroActivity.this, "Usuario já existe", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this,"Senhas não correspondem",Toast.LENGTH_SHORT).show();
                    }

                }
            }

        });
    }

    private Boolean validarCampos() {

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

        //exibindo a progressbar
        progressBarCEP.setVisibility(View.VISIBLE);

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

                //escondendo a progressbar
                progressBarCEP.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro ao tentar consultar o CEP. Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();

                //escondendo a progressbar
                progressBarCEP.setVisibility(View.GONE);
            }
        });
    }

}