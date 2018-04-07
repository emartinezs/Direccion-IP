package com.example.direccionip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    EditText ipEditText;
    EditText netmaskEditText;
    TextView netidTextView;
    TextView broadcastTextView;
    TextView hostnumberTextView;
    TextView networkTextView;
    TextView hostTextView;
    Button calculateButton;

    int ip[] = new int[4];
    int netmask[] = new int[4];
    int netid[] = new int[4];
    int broadcast[] = new int[4];
    int hostNumber;
    int network[] = new int[4];
    int host[] = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipEditText = findViewById(R.id.ipEditText);
        netmaskEditText = findViewById(R.id.netmaskEditText);
        netidTextView = findViewById(R.id.netidTextView);
        broadcastTextView = findViewById(R.id.broadcastTextView);
        hostnumberTextView = findViewById(R.id.hostNumberTextView);
        networkTextView = findViewById(R.id.networkTextView);
        hostTextView = findViewById(R.id.hostTextView);
        calculateButton = findViewById(R.id.calculateButton);

        calculateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                calculate();
            }
        });
    }

    private void calculate(){
        //Convierte la ip de string a int
        String ipInput[] = ipEditText.getText().toString().split("\\.");
        for (int i=0; i<4; i++){
            ip[i] = Integer.parseInt(ipInput[i]);
        }

        //Crea la mascara de red y la mascara de red invertida
        int netmaskInput = Integer.parseInt(netmaskEditText.getText().toString());
        int auxNetmask= netmaskInput;
        int hostmask[] = new int[4];
        for (int i=0; i<4; i++){
            if (auxNetmask - 8 >= 0){
                netmask[i] = 255;
                hostmask[i] = 0;
            }else {
                if (auxNetmask < 0){
                    auxNetmask = 0;
                }
                netmask[i] = (int)(Math.pow(2,8)-Math.pow(2,8-auxNetmask));
                hostmask[i] = (int)(Math.pow(2,8-auxNetmask)-1);
            }
            auxNetmask -= 8;
        }

        //Calculo de netID
        for (int i=0; i<4; i++) {
            netid[i] = ip[i] & netmask[i];
        }
        netidTextView.setText(netid[0]+"."+netid[1]+"."+netid[2]+"."+netid[3]);

        //Calculo de broadcast
        for (int i=0; i<4; i++) {
            broadcast[i] = ip[i] | hostmask[i];
            Log.d("TEST",hostmask[i]+"");
        }
        broadcastTextView.setText(broadcast[0]+"."+broadcast[1]+"."+broadcast[2]+"."+broadcast[3]);

        //Calculo de numero de hosts
        hostNumber = (int)Math.pow(2,32-netmaskInput)-2;
        hostnumberTextView.setText(hostNumber+"");

        //Calculo parte de red
        for (int i=0; i<4; i++){
            network[i] = ip[i] & netmask[i];
        }
        networkTextView.setText(network[0]+"."+network[1]+"."+network[2]+"."+network[3]);

        //Calculo parte de host
        for (int i=0; i<4; i++){
            host[i] = ip[i] & hostmask[i];
        }
        hostTextView.setText(host[0]+"."+host[1]+"."+host[2]+"."+host[3]);
    }
}
