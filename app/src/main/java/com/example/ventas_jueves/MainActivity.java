package com.example.ventas_jueves;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Clientes(View view){
        Intent intclientes=new Intent(this,ClientesActivity.class);
        startActivity(intclientes);
    }//Fin metodo Clientes

    public void Vehiculos(View view){
        Intent intvehiculos=new Intent(this, VehiculosActivity.class);
        startActivity(intvehiculos);
    }//fin metodo Vehiculos

    public void Facturas(View view){
        Intent intfacturas=new Intent(this,FacturasActivity.class);
        startActivity(intfacturas);
    }//fin metodo Facturas
}