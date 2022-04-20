package com.davidbanda.modulosuelo;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.util.HashMap;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class TomaMedicionesActivity extends AppCompatActivity{

    TextView tem1, tem2, tem3, hu1, hu2, hu3, ph1, ph2, ph3, re1, re2, re3, tipo;
    EditText lugar, id;
    Button obtener1,obtener2,obtener3,calcular1,calcular2,calcular3, guardar;

    Handler bluetoothIn;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private ConnectedThread MyConexionBT;

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static String address = null;

    private static String typeData = null;
    private int contTemperatura=0, contHumedad=0,contPh=0;
    private int verificar = 0;
    private String adjuntar = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toma_mediciones);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        VerificarEstadoBT();

        tem1 = findViewById(R.id.tem1);
        tem2 = findViewById(R.id.tem2);
        tem3 = findViewById(R.id.tem3);
        hu1 = findViewById(R.id.hu1);
        hu2 = findViewById(R.id.hu2);
        hu3 = findViewById(R.id.hu3);
        ph1 = findViewById(R.id.ph1);
        ph2 = findViewById(R.id.ph2);
        ph3 = findViewById(R.id.ph3);
        re1 = findViewById(R.id.re1);
        re2 = findViewById(R.id.re2);
        re3 = findViewById(R.id.re3);

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    if(typeData.equals("T")){
                        if(contTemperatura==1){
                            adjuntar = adjuntar + readMessage;
                            tem1.setText(adjuntar);
                        }
                        if(contTemperatura==2){
                            adjuntar = adjuntar + readMessage;
                            tem2.setText(adjuntar);
                        }
                        if(contTemperatura==3){
                            adjuntar = adjuntar + readMessage;
                            tem3.setText(adjuntar);
                        }
                    }

                    if(typeData.equals("H")){
                        if(contHumedad==1){
                            adjuntar = adjuntar + readMessage;
                            hu1.setText(adjuntar);
                        }
                        if(contHumedad==2){
                            adjuntar = adjuntar + readMessage;
                            hu2.setText(adjuntar);
                        }
                        if(contHumedad==3){
                            adjuntar = adjuntar + readMessage;
                            hu3.setText(adjuntar);
                        }
                    }

                    if(typeData.equals("P")){
                        if(contPh==1){
                            ph1.setText(readMessage);
                        }
                        if(contPh==2){
                            ph2.setText(readMessage);
                        }
                        if(contPh==3){
                            ph3.setText(readMessage);
                        }
                    }
                }
            }
        };

        lugar = findViewById(R.id.lugar);
        tipo = findViewById(R.id.tipo);
        id = findViewById(R.id.id);

        obtener1 = findViewById(R.id.obtener1);
        obtener1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(contTemperatura<=3){
                    adjuntar = "";
                    typeData = "T";
                    MyConexionBT.write("T");
                    contTemperatura = contTemperatura + 1;
                }
            }
        });

        obtener2 = findViewById(R.id.obtener2);
        obtener2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(contHumedad<=3){
                    adjuntar = "";
                    typeData = "H";
                    MyConexionBT.write("H");
                    contHumedad = contHumedad + 1;
                }
            }
        });

        obtener3 = findViewById(R.id.obtener3);
        obtener3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(contPh<=3){
                    adjuntar = "";
                    typeData = "P";
                    MyConexionBT.write("P");
                    contPh = contPh + 1;
                }
            }
        });

        calcular1 = findViewById(R.id.calcular1);
        calcular1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                totalTemperatura();
                adjuntar = "";
            }
        });

        calcular2 = findViewById(R.id.calcular2);
        calcular2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                totalHumedad();
                adjuntar = "";
            }
        });

        calcular3 = findViewById(R.id.calcular3);
        calcular3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                totalPh();
                adjuntar = "";
            }
        });

        guardar = findViewById(R.id.guardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarMedicion("https://utcflores.000webhostapp.com/registerMediciones.php");
            }
        });

    }

    public void totalTemperatura(){
        double t1 = Double.valueOf(tem1.getText().toString());
        double t2 = Double.valueOf(tem2.getText().toString());
        double t3 = Double.valueOf(tem3.getText().toString());
        double r1 = (t1 + t2 + t3) / 3;
        re1.setText(String.valueOf(obtieneDosDecimales(r1)));
    }

    public void totalHumedad(){
        double h1 = Double.valueOf(hu1.getText().toString());
        double h2 = Double.valueOf(hu2.getText().toString());
        double h3 = Double.valueOf(hu3.getText().toString());
        double r2 = (h1 + h2 + h3) / 3;
        re2.setText(String.valueOf(obtieneDosDecimales(r2)));
    }
    public void totalPh() {
        double p1 = Double.valueOf(ph1.getText().toString());
        double p2 = Double.valueOf(ph2.getText().toString());
        double p3 = Double.valueOf(ph3.getText().toString());
        double r3 = (p1 + p2 + p3) / 3;
        re3.setText(String.valueOf(obtieneDosDecimales(r3)));
        tipo.setText("Suelo Optimo");
    }

    private String obtieneDosDecimales(double valor) {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2);
        return format.format(valor);
    }

    public void guardarMedicion(String URL) {
        HashMap<String,String> medicionguardar = new HashMap<>();
        medicionguardar.put("Id", id.getText().toString());
        medicionguardar.put("Temperatura", re1.getText().toString());
        medicionguardar.put("Humedad", re2.getText().toString());
        medicionguardar.put("Ph", re3.getText().toString());
        medicionguardar.put("Suelo", tipo.getText().toString());
        medicionguardar.put("Lugar", lugar.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(medicionguardar), null, null);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }

        Intent i = new Intent(TomaMedicionesActivity.this, EstadoGeneralActivity.class);
        i.putExtra("idmedicion", id.getText().toString());
        i.putExtra("humedadmedicion", re2.getText().toString());
        i.putExtra("temperaturamedicion", re1.getText().toString());
        i.putExtra("phmedicion", re3.getText().toString());
        i.putExtra("suelomedicion", tipo.getText().toString());
        i.putExtra("lugarmedicion", lugar.getText().toString());
        startActivity(i);
        finish();
    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        address = intent.getStringExtra(DispositivosBT.EXTRA_DEVICE_ADDRESS);//<-<- PARTE A MODIFICAR >->->
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }

        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
            }
        }
        MyConexionBT = new ConnectedThread(btSocket);
        MyConexionBT.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            btSocket.close();
        } catch (IOException e2) {
        }
    }

    private void VerificarEstadoBT() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(String input) {
            try {
                mmOutStream.write(input.getBytes());
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
