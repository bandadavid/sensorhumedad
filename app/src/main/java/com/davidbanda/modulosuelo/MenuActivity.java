package com.davidbanda.modulosuelo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {
    Button optimo, medidas, general,recomenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //////////boton para ingresar a optimo
        optimo= (Button)findViewById(R.id.optimo1);
        optimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent optimo = new Intent(MenuActivity.this,SueloOptimoActivity.class);
                startActivity(optimo);
            }
        });

        //////////boton para ingresar a medidas
        medidas= (Button)findViewById(R.id.medidas1);
        medidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medidas = new Intent(MenuActivity.this,DispositivosBT.class);
                startActivity(medidas);
            }
        });

        //////////boton para ingresar a general

        general= (Button)findViewById(R.id.general1);
        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent general = new Intent(MenuActivity.this,EstadoGeneralActivity.class);
                startActivity(general);
            }
        });

        //////////boton para ingresar a recomendaciones
        recomenda= (Button)findViewById(R.id.recomenda1);
        recomenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recomenda = new Intent(MenuActivity.this,RecomendacionesActivity.class);
                startActivity(recomenda);
            }
        });



    }
}
