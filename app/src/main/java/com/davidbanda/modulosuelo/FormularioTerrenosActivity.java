package com.davidbanda.modulosuelo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import ServiciosWeb.Servicios;
import ServiciosWeb.Servidor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormularioTerrenosActivity extends AppCompatActivity implements Validator.ValidationListener {

    Retrofit objetoRetrofit;
    Servicios peticionesWeb;
    Servidor miServidor;

    Validator validator;

    @NotEmpty(message = "Campo necesario")
    EditText txtProvincia, txtCanton, txtParroquia, txtBarrio, txtDireccion;

    Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_terrenos);

        miServidor=new Servidor();
        objetoRetrofit=new Retrofit.Builder()
                .baseUrl(miServidor.obtenerurlBase())
                .addConverterFactory(GsonConverterFactory.create()).build();
        peticionesWeb=objetoRetrofit.create(Servicios.class);

        txtProvincia = (EditText) findViewById(R.id.txtProvincia);
        txtCanton = (EditText) findViewById(R.id.txtCanton);
        txtParroquia = (EditText) findViewById(R.id.txtParroquia);
        txtBarrio = (EditText) findViewById(R.id.txtBarrio);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);

        validator = new Validator(this);
        validator.setValidationListener(this);

        btnAgregar = (Button) findViewById(R.id.btnAgregar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    public void botonCancelar(View vista){
        Intent ventanaServicios = new Intent(getApplicationContext(), Terrenos.class);
        startActivity(ventanaServicios);
        finish();
    }

    public void abrirTerrenos(){
        Intent ventanaServicios = new Intent(getApplicationContext(), Terrenos.class);
        startActivity(ventanaServicios);
        finish();
    }

    @Override
    public void onValidationSucceeded() {
        String provinciaIngresada = txtProvincia.getText().toString();
        String cantonIngresado = txtCanton.getText().toString();
        String parroquiaIngresada = txtParroquia.getText().toString();
        String barrioIngresado = txtBarrio.getText().toString();
        String direccionIngresado = txtDireccion.getText().toString();


        Call llamadaHTTP=peticionesWeb.guardarTerreno(provinciaIngresada, cantonIngresado, parroquiaIngresada, barrioIngresado, direccionIngresado);
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(FormularioTerrenosActivity.this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Enviando datos...");
        progressDialog.setTitle("Procesando Terreno");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        llamadaHTTP.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if(response.isSuccessful()){
                        String resultadoJson = new Gson().toJson(response.body());
                        JsonObject objetoJson = new JsonParser().parse(resultadoJson).getAsJsonObject();
                        if(objetoJson.get("estado").getAsString().equalsIgnoreCase("ok")){
                            abrirTerrenos();
                            Toast.makeText(getApplicationContext(), "Terreno ingresado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "No se ingreso el terreno",
                                    Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error al traer los DATOS",
                                Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                } catch (Exception ex){
                    Toast.makeText(getApplicationContext(), "Error en la App Web -> " +ex.toString(),
                            Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR DE CONEXION (IP)",
                        Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}