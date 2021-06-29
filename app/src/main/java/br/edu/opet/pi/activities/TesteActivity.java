package br.edu.opet.pi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import br.edu.opet.pi.R;
import br.edu.opet.pi.model.User;
import br.edu.opet.pi.services.RESTService;
import br.edu.opet.pi.services.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TesteActivity extends AppCompatActivity {

    private TextView name, id;
    private TextInputEditText email;
    private ProgressDialog progress;
    boolean is_valid = false;
    private Button btnConsultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        email = findViewById(R.id.inputEmail);
        name = findViewById(R.id.txtvName);
        id = findViewById(R.id.txtvId);
        btnConsultar = findViewById(R.id.btnConsultar);

        listenersButtons();
    }

    public void listenersButtons() {

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress = new ProgressDialog(TesteActivity.this);
                progress.setTitle("enviando...");
                progress.show();

                //pega os valores dos edittextos
                String s_email = email.getText().toString();
                //String s_name = name.getText().toString();
                //String s_id = id.getText().toString();

                //chama o retrofit para fazer a requisição no webservice
                retrofitConverter();
            }
        });
    }

    public void retrofitConverter() {

        String s_email = email.getText().toString().trim();
        RESTService service = ServiceGenerator.createService(RESTService.class);
        Call<List<User>> call = service.consultarUser(s_email);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (response.isSuccessful()) {

                    List<User> userOut = response.body();
                    /*
                    try {
                        JSONObject jsonObj = new JSONObject(response.body().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/

                    //verifica aqui se o corpo da resposta não é nulo
                    if (userOut != null) {

                            progress.dismiss();
                            for (User u : response.body()){

                                String name_s = name.toString();
                                id.setText(u.getId());
                                name.setText(u.getName());

                                if (u.getName() == name_s){
                                    is_valid = true;
                                }
                        }
                            if (is_valid = true){
                                Toast.makeText(getApplicationContext(), "Consulta realizada com sucesso", Toast.LENGTH_LONG).show();
                            }
                    }
                } else {

                    Toast.makeText(getApplicationContext(),"Resposta não foi sucesso", Toast.LENGTH_SHORT).show();
                    // segura os erros de requisição
                    ResponseBody errorBody = response.errorBody();
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"Erro na chamada ao servidor", Toast.LENGTH_SHORT).show();
                Log.d("ERROR", t.getMessage());
                progress.dismiss();
            }
        });

    }
}