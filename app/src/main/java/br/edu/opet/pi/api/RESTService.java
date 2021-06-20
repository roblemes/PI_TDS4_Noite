package br.edu.opet.pi.api;

import br.edu.opet.pi.cep.CEP;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RESTService {
    //consultar CEP no webservice do ViaCEP
    @GET("{cep}/json/")
    Call<CEP> consultarCEP(@Path("cep") String cep);
}
