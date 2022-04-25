package com.davidbanda.modulosuelo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import ServiciosWeb.Servicios;
import ServiciosWeb.Servidor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Terrenos extends AppCompatActivity {

    Retrofit objetoRetrofit;
    Servicios peticionesWeb;
    Servidor miServidor;
    String[] listaSplit;
    ListView lstTerrenos;
    ArrayList<String> listaTerrenos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terrenos);

        lstTerrenos=findViewById(R.id.lstTerrenos);
        miServidor=new Servidor();
        objetoRetrofit=new Retrofit.Builder()
                .baseUrl(miServidor.obtenerurlBase())
                .addConverterFactory(GsonConverterFactory.create()).build();
        peticionesWeb=objetoRetrofit.create(Servicios.class);

        seleccionServiciosClick();
    }

    public void consultarServ(View view){
        listaTerrenos.clear();
        Call llamadaHTTP=peticionesWeb.consultarTerrenos();
        llamadaHTTP.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if(response.isSuccessful()){
                        String resultadoJson = new Gson().toJson(response.body());
                        JsonObject objetoJson = new JsonParser().parse(resultadoJson).getAsJsonObject();
                        if(objetoJson.get("estado").getAsString().equalsIgnoreCase("ok")){
                            JsonArray listadoObtenido = objetoJson.getAsJsonArray("datos");

                            for(JsonElement servicioTemporal:listadoObtenido){
                                String id = servicioTemporal.getAsJsonObject().get("id_ter").toString();
                                String provincia = servicioTemporal.getAsJsonObject().get("provincia_ter").toString();
                                String canton = servicioTemporal.getAsJsonObject().get("canton_ter").toString();
                                String barrio = servicioTemporal.getAsJsonObject().get("barrio_ter").toString();
                                //String foto = servicioTemporal.getAsJsonObject().get("foto_ser").toString();


                                listaTerrenos.add(id.replace("\"","") + "|" +provincia.replace("\"","") + "|" + canton.replace("\"","") + "|" + barrio.replace("\"",""));


                            }
                            ArrayAdapter<String> adaptadorServicios = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listaTerrenos);
                            lstTerrenos.setAdapter(adaptadorServicios);
                        } else {
                            Toast.makeText(getApplicationContext(), "No se encontraron Terrenos",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error al traer los DATOS",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex){
                    Toast.makeText(getApplicationContext(), "Error en la App Web -> " +ex.toString(),
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR DE CONEXION (IP)",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void abrirFormularioIngreso(View view){
        //Objeto para manipular la actividad Menu
        Intent ventanIngreso = new Intent(getApplicationContext(), FormularioTerrenosActivity.class);
        startActivity(ventanIngreso); //Solicitando que se abra el Menu
    }

    public void seleccionServiciosClick(){

        lstTerrenos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Seleccionaste el elemento: " + listaUsuarios.get(position), Toast.LENGTH_SHORT).show();
                //finish();// Cerrando ventana actual
                Intent ventanaEditarServicios = new Intent(getApplicationContext(), FormularioEditarTerrenosActivity.class);
                listaSplit = listaTerrenos.get(position).split("\\|");
                ventanaEditarServicios.putExtra("id", listaSplit[0]);
                ventanaEditarServicios.putExtra("provincia", listaSplit[1]);
                ventanaEditarServicios.putExtra("canton", listaSplit[2]);
                ventanaEditarServicios.putExtra("barrio", listaSplit[3]);
                startActivity(ventanaEditarServicios);
                //Toast.makeText(getApplicationContext(), "Seleccionaste el elemento: " + listaSplit[0].replace(" ", ""), Toast.LENGTH_SHORT).show();
            }
        });
    }
}