package br.edu.opet.pi.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import br.edu.opet.pi.model.User;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateUserService extends IntentService {

    public static final String API_BASE_URL = "https://60d37e7561160900173c9360.mockapi.io/api/v1/users/";

    public CreateUserService() {
        super("CreateUserService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String userName = intent.getStringExtra("username");
        String userEmail = intent.getStringExtra("useremail");
        String userPass = intent.getStringExtra("userpass");
        String userEnd = intent.getStringExtra("userend");
        String userBairro = intent.getStringExtra("userbairro");
        String userCidade = intent.getStringExtra("usercidade");
        String userEstado = intent.getStringExtra("userestado");

        //Instância do retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        RESTService service = retrofit.create(RESTService.class);
        Call<User> call = service.createUser(getUser(userName,userEmail,
                userPass,userEnd,userBairro,userCidade,userEstado));

        try {
            Response<User> result = call.execute();
            Log.i("Retrofit","Success");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Retrofit","Fail");
        }
    }

    public User getUser(String userName, String userEmail,
                        String userPass,
                        String userEnd,
                        String userBairro,
                        String userCidade,
                        String userEstado
                        ){
        User user = new User(userName,
                null,
                userEmail,
                userPass,
                userEnd,
                userBairro,
                userCidade,
                userEstado);
        return user;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("CreateUserService","Serviço terminado.");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}