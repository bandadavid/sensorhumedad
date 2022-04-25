package ServiciosWeb;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Servicios {
    @GET("index.php/terrenos/listarTerrenos")
    Call<Object> consultarTerrenos();

    @FormUrlEncoded
    @POST("index.php/terrenos/guardarTerreno")
    Call<Object> guardarTerreno(
            @Field("provincia_ter") String provincia,
            @Field("canton_ter") String canton,
            @Field("parroquia_ter") String parroquia,
            @Field("barrio_ter") String barrio,
            @Field("direccion_ter") String direccion
    );
}
