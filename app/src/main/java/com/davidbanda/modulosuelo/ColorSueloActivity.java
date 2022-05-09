package com.davidbanda.modulosuelo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.palette.graphics.Palette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class ColorSueloActivity extends AppCompatActivity {

    //Definiendo objetos para capturar imagenes

    private TextView txt1,txt2,txt3,txt4,txt5;
    ImageView imgFoto;
    String rutaImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_suelo);

        //Mapeo de elementos XML a Objeto Java
        imgFoto=findViewById(R.id.imgFoto);

    }

    //Metodo para crear una imagen
    public File crearImagen() throws IOException {
        String nombreImagen="foto_";//definiendo un estandar para el nombre de nuestras imagenes
        File carpeta=getExternalFilesDir(Environment.DIRECTORY_PICTURES);//definiendo la ruta donde se guardaran las imagenes
        File imagenTemporal=File.createTempFile(nombreImagen,".jpg",carpeta); //creando la imagen temporal
        rutaImagen=imagenTemporal.getAbsolutePath();//Obteniendo la ruta de la imagen creada
        return imagenTemporal;//retornando la imagen temporal que se ha creado
    }
    //Metodo para capturar una imagen al dar click en el boton correspondiente
    public void capturarImagen(View vista){
        Intent ventanaCamara= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//Creando una actividad para acceder a la camara
        if(ventanaCamara.resolveActivity(getPackageManager())!=null){
            //continuar con el proceso de captura de la imagen
            File imagenArchivo=null;
            try{
                imagenArchivo=crearImagen(); //crear la imagen
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),"Error -> "+ex.toString(),
                        Toast.LENGTH_SHORT).show();
            }
            if(imagenArchivo!=null){
                Uri FotoUri= FileProvider.getUriForFile(this,"com.davidbanda.modulosuelo.fileprovider",imagenArchivo);
                ventanaCamara.putExtra(MediaStore.EXTRA_OUTPUT,FotoUri);//capturando la imagen almacenada
                startActivityForResult(ventanaCamara,1);
            }
        }else{
            Toast.makeText(getApplicationContext(),"Error al iniciar cámara",Toast.LENGTH_SHORT).show();
        }
    }

    //metodo para presentar la imagen dentro del image view correspondiente
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);//llamando al metodo padre
        if (requestCode==1 && resultCode==RESULT_OK){//validamos que la captura de la imagen sea exitosa
            //creando un bitmap mediante la ruta de la imagen creada
            init();
            Bitmap bitmap= BitmapFactory.decodeFile(rutaImagen);
            crearPalette(bitmap);
            //mensaje para presentar la ruta donde se almacena la imagen
            Toast.makeText(getApplicationContext(),"RUTA:"+rutaImagen,Toast.LENGTH_SHORT).show();
            //asignando la imagen capturada como vista previa del ImageView
            imgFoto.setImageBitmap(bitmap);


        }
    }

    private void crearPalette(Bitmap imagenBitmap) {

        Palette.from(imagenBitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (vibrant!=null){
                    txt1.setBackgroundColor(vibrant.getRgb());
                    txt1.setTextColor(vibrant.getTitleTextColor());
                    String textColor= String.valueOf(vibrant.getRgb());
                    txt1.setText("color"+textColor+"La Tierra no es apta");

                }

                Palette.Swatch muted = palette.getMutedSwatch();
                if (muted!=null){
                    txt2.setBackgroundColor(muted.getRgb());
                    txt2.setTextColor(muted.getTitleTextColor());
                    String textColor= String.valueOf(muted.getRgb());
                    txt2.setText("Tierra Blanca "+"La Tierra no es apta");


                }

                Palette.Swatch dominant = palette.getDominantSwatch();
                if (dominant!=null){
                    txt3.setBackgroundColor(dominant.getRgb());
                    txt3.setTextColor(dominant.getTitleTextColor());
                    String textColor= String.valueOf(dominant.getRgb());
                    txt3.setText("Tierra Blanca"+"La Tierra no es apta");
                }

                Palette.Swatch darkMuted = palette.getDarkMutedSwatch();
                if (darkMuted!=null){
                    txt4.setBackgroundColor(darkMuted.getRgb());
                    txt4.setTextColor(darkMuted.getTitleTextColor());
                    String textColor= String.valueOf(darkMuted.getRgb());
                    txt4.setText("Tierra Negra"+"La Tierra es apta");

                }

                Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();
                if (darkVibrant!=null){
                    txt5.setBackgroundColor(darkVibrant.getRgb());
                    txt5.setTextColor(darkVibrant.getTitleTextColor());
                    String textColor= String.valueOf(darkMuted.getRgb());
                    txt5.setText("Tierra Negra1"+"La Tierra no es apta");
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