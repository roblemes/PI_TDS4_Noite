package br.edu.opet.pi.services;

import java.util.List;

import br.edu.opet.pi.model.CEP;
import br.edu.opet.pi.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RESTService {
    //consultar CEP no webservice do ViaCEP
    @GET("{cep}/json/")
    Call<CEP> consultarCEP(@Path("cep") String cep);

    @GET("/users")
    Call<List<User>> consultarUser(@Query("email") String email);

    @POST("/users")
    Call<User> createUser(@Body User user);

}
