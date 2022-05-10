package com.davidbanda.modulosuelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

public class EstadoGeneralActivity extends AppCompatActivity implements View.OnClickListener {

    TextView vt, rese1, rese2, rese3, tipo;
    Button calul, buscar;
    EditText temp1;
    EditText temp2;
    EditText hum1;
    EditText hum2;
    EditText ph1;
    EditText ph2;
    EditText lugar, medic;
    EditText longitud, latitud;
    Button estadogen;
    private Location loc;
    private LocationManager locManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_general);

        vt = findViewById(R.id.fecha);

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        int dia = today.monthDay;
        int mes = today.month;
        int ano = today.year;
        mes = mes + 1;
        vt.setText(ano + "/" + mes + "/" + dia);

        medic = findViewById(R.id.id_med1);
        temp1 = findViewById(R.id.temp1);
        temp2 = findViewById(R.id.temp2);
        hum1 = findViewById(R.id.hum1);
        hum2 = findViewById(R.id.hum2);
        ph1 = findViewById(R.id.ph1);
        ph2 = findViewById(R.id.ph2);
        rese1 = findViewById(R.id.rese1);
        rese2 = findViewById(R.id.rese2);
        rese3 = findViewById(R.id.rese3);
        tipo = findViewById(R.id.tiposuelo1);
        calul = findViewById(R.id.calcular);
        lugar = findViewById(R.id.lugar_medicion);

        String idmedicion = getIntent().getStringExtra("idmedicion");
        medic.setText(idmedicion);
        String humedadmedicion = getIntent().getStringExtra("humedadmedicion");
        hum2.setText(humedadmedicion);
        String temperaturamedicion = getIntent().getStringExtra("temperaturamedicion");
        temp2.setText(temperaturamedicion);
        String phmedicion = getIntent().getStringExtra("phmedicion");
        ph2.setText(phmedicion);
        String suelomedicion = getIntent().getStringExtra("suelomedicion");
        tipo.setText(suelomedicion);
        String lugarmedicion = getIntent().getStringExtra("lugarmedicion");
        lugar.setText(lugarmedicion);

        buscar = (Button) findViewById(R.id.buscar);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = medic.getText().toString();
                String b = lugar.getText().toString();
                busquedaMedicion("https://utcflores.000webhostapp.com/searchMedicion.php?codigo=" + a + "&lugar=" + b);
            }
        });

        estadogen = (Button) findViewById(R.id.estadogen1);
        estadogen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarServicio("https://utcflores.000webhostapp.com/registerResultado.php");
            }
        });
        calul.setOnClickListener(this);
    }


    private void guardarServicio(String URL) {

        HashMap<String,String> parametros = new HashMap<>();
        parametros.put("Codigo", medic.getText().toString());
        parametros.put("Humedad", rese2.getText().toString());
        parametros.put("Temperatura", rese1.getText().toString());
        parametros.put("Ph", rese3.getText().toString());
        parametros.put("Fecha", vt.getText().toString());
        parametros.put("Lugar", lugar.getText().toString());
        parametros.put("Suelo", tipo.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(parametros), null, null);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
        Intent estadogen = new Intent(EstadoGeneralActivity.this, RecomendacionesActivity.class);
        startActivity(estadogen);
    }


    private void busquedaMedicion(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        hum2.setText(jsonObject.getString("humedad"));
                        temp2.setText(jsonObject.getString("temperatura"));
                        ph2.setText(jsonObject.getString("ph"));
                        tipo.setText(jsonObject.getString("suelo"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), "NO CONEXION ", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    @Override
    public void onClick(View v) {
        double t1 = Double.valueOf(temp1.getText().toString());
        double t2 = Double.valueOf(temp2.getText().toString());
        double hu1 = Double.valueOf(hum1.getText().toString());
        double hu2 = Double.valueOf(hum2.getText().toString());
        double p1 = Double.valueOf(ph1.getText().toString());
        double p2 = Double.valueOf(ph2.getText().toString());
        double r1 = t1 - t2;
        double r2 = hu1 - hu2;
        double r3 = p1 - p2;
        rese1.setText(obtieneDosDecimales(r1));
        rese2.setText(obtieneDosDecimales(r2));
        rese3.setText(obtieneDosDecimales(r3));
    }


    private String obtieneDosDecimales(double valor) {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2);
        return format.format(valor);
    }

}
