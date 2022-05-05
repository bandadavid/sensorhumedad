package com.davidbanda.modulosuelo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ColorSueloActivity extends AppCompatActivity {

    private TextView txt1,txt2,txt3,txt4,txt5;

    //Declaracion de variables JAVA para la camara
    Button btnCamara;
    ImageView imgView;
    //Fin de la Declaracion de variables JAVA para la camara

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_suelo);

        //Mapeo de elementos XML a Objetos JAVA
        btnCamara = findViewById(R.id.btnCamara);
        imgView = findViewById(R.id.imageView);
        //Fin de Mapeo de elementos XML a Objetos JAVA

        //Agregar el evento para el  click del boton
        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamara();
            }
        });
        //Fin de Agregar el evento para el  click del boton



        init();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fondo3);
        crearPalette(bitmap);
    }

    //Metodo para abrir la camara
    private void abrirCamara(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (intent.resolveActivity(getPackageManager()) != null){
        startActivityForResult(intent,1);
        //}
    }
    //Fin del Metodo para abrir la camara

    //Metodo para poder visualizar la imagen
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imgBitmap = (Bitmap) extras.get("data");
            imgView.setImageBitmap(imgBitmap);
        }
    }
    //Fin delMetodo para poder visualizar la imagen


    private void crearPalette(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (vibrant!=null){
                    txt1.setBackgroundColor(vibrant.getRgb());
                    txt1.setTextColor(vibrant.getTitleTextColor());
                    txt1.setText("0");
                }

                Palette.Swatch muted = palette.getMutedSwatch();
                if (muted!=null){
                    txt2.setBackgroundColor(muted.getRgb());
                    txt2.setTextColor(muted.getTitleTextColor());
                    txt2.setText("1");
                }

                Palette.Swatch dominant = palette.getDominantSwatch();
                if (dominant!=null){
                    txt3.setBackgroundColor(dominant.getRgb());
                    txt3.setTextColor(dominant.getTitleTextColor());
                    txt3.setText("2");
                }

                Palette.Swatch darkMuted = palette.getDarkMutedSwatch();
                if (darkMuted!=null){
                    txt4.setBackgroundColor(darkMuted.getRgb());
                    txt4.setTextColor(darkMuted.getTitleTextColor());
                    txt4.setText("3");
                }

                Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();
                if (darkVibrant!=null){
                    txt5.setBackgroundColor(darkVibrant.getRgb());
                    txt5.setTextColor(darkVibrant.getTitleTextColor());
                    txt5.setText("4");
                }
            }
        });


    }

    private void init(){
        this.txt1 =findViewById(R.id.txt1);
        this.txt2 =findViewById(R.id.txt2);
        this.txt3 =findViewById(R.id.txt3);
        this.txt4 =findViewById(R.id.txt4);
        this.txt5 =findViewById(R.id.txt5);
    }
}