package com.example.bluetoothtestjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    CheckBox bt_ligado, bt_visivel;
    TextView bt_nome;
    ImageView bt_pesquisa;
    ListView listView;

    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pareados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_ligado = findViewById(R.id.bt_ligado);
        bt_visivel = findViewById(R.id.bt_visivel);
        bt_nome = findViewById(R.id.bt_nome);
        bt_pesquisa = findViewById(R.id.bt_pesquisa);
        listView = findViewById(R.id.listView);

        bt_nome.setText(getLocalBluetoothAdapter());

        BA = BluetoothAdapter.getDefaultAdapter();
        if(BA == null){
            Toast.makeText(this, "Bluetooth não suportado!", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(BA.isEnabled()){
            bt_ligado.setChecked(true);
        }

        bt_ligado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    BA.disable();
                    Toast.makeText(MainActivity.this,  "Desligado!", Toast.LENGTH_SHORT).show();
                    bt_ligado.setChecked(false);
                    bt_visivel.setChecked(false);
                }else{
                    bt_ligado.setChecked(true);
                    Intent intentLigado = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intentLigado, 0);
                    Toast.makeText(MainActivity.this, "Ligado!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_visivel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    BA.disable();
                    Toast.makeText(MainActivity.this,  "Invisível!", Toast.LENGTH_SHORT).show();
                    bt_ligado.setChecked(false);
                    bt_visivel.setChecked(false);
                }else{
                    Intent intentVisivel = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intentVisivel, 0);
                    bt_ligado.setChecked(true);
                    Toast.makeText(MainActivity.this, "Visível por dois minutos!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listar();
            }
        });

    }

    private void listar() {
        pareados = BA.getBondedDevices();

        ArrayList lista = new ArrayList();
        for(BluetoothDevice bt : pareados){
            lista.add(bt.getName());
        }

        Toast.makeText(this, "Mostrando Dispositivos Pareados", Toast.LENGTH_SHORT).show();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        listView.setAdapter(adapter);
    }

    public String getLocalBluetoothAdapter(){
        if(BA == null){
            BA = BluetoothAdapter.getDefaultAdapter();
        }
        String nome = BA.getName();
        if(nome == null){
            nome = BA.getAddress();
        }

        return nome;
    }
}