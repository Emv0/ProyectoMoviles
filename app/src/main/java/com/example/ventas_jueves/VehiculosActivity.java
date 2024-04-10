package com.example.ventas_jueves;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class VehiculosActivity extends AppCompatActivity {

    String placa, marca, modelo;
    int valor;
    boolean sw;
    long respuesta;
    EditText etPlaca, etMarca, etModelo, etValor;
    Button btBuscar, btGuardar, btAnular, btLimpiar, btRegresar;
    CheckBox cbActivo;
    ClsOpenHelper objConexion = new ClsOpenHelper(this, "Consesionario1.db", null, 2);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);
        etPlaca = findViewById(R.id.etplaca);
        etMarca = findViewById(R.id.etmarca);
        etModelo = findViewById(R.id.etmodelo);
        etValor = findViewById(R.id.etvalor);
        btBuscar = findViewById(R.id.btbuscar);
        btGuardar = findViewById(R.id.btguardar);
        btAnular = findViewById(R.id.btanular);
        btLimpiar = findViewById(R.id.btlimpiar);
        btRegresar = findViewById(R.id.btregresar);
        cbActivo = findViewById(R.id.cbactivo);
        etPlaca.requestFocus();
        sw = false;
    }

    public void Guardar_Vehiculo(View view) {
        placa = etPlaca.getText().toString();
        marca = etMarca.getText().toString();
        modelo = etModelo.getText().toString();
        valor = Integer.parseInt(etValor.getText().toString());
        if (!placa.isEmpty() && !marca.isEmpty() && !modelo.isEmpty() && valor != 0) {
            SQLiteDatabase admin = objConexion.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("Placa", placa);
            registro.put("Marca", marca);
            registro.put("Modelo", modelo);
            registro.put("Valor", valor);
            if (!sw) {
                respuesta = admin.insert("TblVehiculo", null, registro);
            } else {
                if (cbActivo.isChecked()) {
                    registro.put("Activo", "Si");
                } else {
                    registro.put("Activo", "No");
                }
                respuesta = admin.update("TblVehiculo", registro, "Placa='" + placa + "'", null);
            }
            if (respuesta > 0) {
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                etPlaca.setEnabled(true);
                Limpiar_Campos_Vehiculo();
            } else {
                Toast.makeText(this, "Error guardando el registro", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
        }
    }

    public void Consultar_Vehiculo(View view) {
        placa = etPlaca.getText().toString();
        if (!placa.isEmpty()) {
            SQLiteDatabase admin = objConexion.getReadableDatabase();
            Cursor fila = admin.rawQuery("select * from TblVehiculo where Placa='" + placa + "'", null);
            if (fila.moveToNext()) {
                etPlaca.setEnabled(false);
                btAnular.setEnabled(true);
                btGuardar.setEnabled(true);
                cbActivo.setEnabled(true);
                sw = true;
                etMarca.setText(fila.getString(1));
                etModelo.setText(fila.getString(2));
                etValor.setText(String.valueOf(fila.getInt(3)));
                if (fila.getString(4).equals("Si")) {
                    cbActivo.setChecked(true);
                } else {
                    cbActivo.setChecked(false);
                }
            } else {
                Toast.makeText(this, "Registro no encontrado", Toast.LENGTH_SHORT).show();
                etPlaca.requestFocus();
                sw = false;
                etPlaca.setEnabled(false);
                btGuardar.setEnabled(true);
                etMarca.setEnabled(true);
                etModelo.setEnabled(true);
                etValor.setEnabled(true);
                etPlaca.requestFocus();
            }
        } else {
            Toast.makeText(this, "La identificacion es requerida", Toast.LENGTH_SHORT).show();
        }
    }

    public void Anular_Vehiculo(View view) {
        SQLiteDatabase admin = objConexion.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("Activo", "No");
        respuesta = admin.update("TblVehiculo", registro, "Placa='" + placa + "'", null);
        if (respuesta > 0) {
            Toast.makeText(this, "registro anulado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error anulando el registro", Toast.LENGTH_SHORT).show();
        }
    }

    public void Regresar_Vehiculo(View view) {
        Intent intmain = new Intent(this, MainActivity.class);
        startActivity(intmain);
    }

    public void Limpiar_Campos_Vehiculo() {
        etPlaca.setText("");
        etMarca.setText("");
        etModelo.setText("");
        etValor.setText("");
        cbActivo.setChecked(false);
        cbActivo.setEnabled(false);
    }
}