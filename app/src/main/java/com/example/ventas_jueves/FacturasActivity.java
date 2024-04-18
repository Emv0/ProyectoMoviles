package com.example.ventas_jueves;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ViewUtils;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FacturasActivity extends AppCompatActivity {

    EditText etcodigo,etfecha,etidentificacion,etplaca;
    TextView tvnombre,tvtelefono,tvmarca,tvvalor;
    Button btconsultar,btconcliente,btconvehiculo,btadicionar,btanular,btlimpiar,btregresar;
    CheckBox cbactivo;
    String codigo,identificacion,placa,fecha;
    float total;
    ClsOpenHelper objConexion = new ClsOpenHelper(this, "Consesionario1.db", null, 3);
    boolean sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturas);
        //asociar objetos Java con objetos xml
        etcodigo=findViewById(R.id.etcodigo);
        etfecha=findViewById(R.id.etfecha);
        etidentificacion=findViewById(R.id.etidentificacion);
        etplaca=findViewById(R.id.etplaca);
        tvnombre=findViewById(R.id.tvnombre);
        tvtelefono=findViewById(R.id.tvtelefono);
        tvmarca=findViewById(R.id.tvmarca);
        tvvalor=findViewById(R.id.tvvalor);
        btadicionar=findViewById(R.id.btadicionar);
        btanular=findViewById(R.id.btanular);
        btlimpiar=findViewById(R.id.btlimpiar);
        btregresar=findViewById(R.id.btregresar);
        btconcliente=findViewById(R.id.btconcliente);
        btconvehiculo=findViewById(R.id.btconvehiculo);
        btconsultar=findViewById(R.id.btconsultar);
        cbactivo=findViewById(R.id.cbactivo);
        etcodigo.requestFocus();
        sw = false;
    }//Fin metodo onCreate

    public void Guardar_factura(View view){
        codigo = etcodigo.getText().toString();
        fecha = etfecha.getText().toString();
        if (!codigo.isEmpty() && !fecha.isEmpty() && !etidentificacion.getText().toString().isEmpty() && !tvnombre.getText().toString().isEmpty() && !etplaca.getText().toString().isEmpty() && !tvmarca.getText().toString().isEmpty() && !tvvalor.getText().toString().isEmpty()){
            SQLiteDatabase admin = objConexion.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("CodFactura",codigo);
            registro.put("fecha",fecha);
            registro.put("identificacion",etidentificacion.getText().toString());
            ContentValues registro2 = new ContentValues();
            registro2.put("CodFactura",codigo);
            registro2.put("Placa",placa);
            Cursor fila = admin.rawQuery("select valor from TblVehiculo where Placa='"+placa+"'",null);
            if(fila.moveToFirst()){
                float op = (float) (fila.getInt(0) * 19)/100;
                total = fila.getInt(0) + op;
                registro2.put("Valor_venta",total);
            }else {
                Toast.makeText(this,"Error guardando la factura",Toast.LENGTH_SHORT).show();
            }
            if (!sw){
                if (cbactivo.isChecked()){
                    registro.put("Activo","Si");
                }else{
                    registro.put("Activo","No");
                }
                admin.insert("TblFactura",null,registro);
                admin.insert("TblVehiculo_Factura",null,registro2);
                Toast.makeText(this,"Se ha guardado la factura",Toast.LENGTH_SHORT).show();
            }else{
                if (cbactivo.isChecked()){
                    registro.put("Activo","Si");
                }else{
                    registro.put("Activo","No");
                }
                admin.update("TblFactura",registro,"CodFactura='"+codigo+"'",null);
                Toast.makeText(this,"Se ha actualizado la factura",Toast.LENGTH_SHORT).show();
            }
            Limpiar_Campos_Factura();
            admin.close();
        }else{
            Toast.makeText(this,"Todos los campos deben estar llenos para crear la factura", Toast.LENGTH_SHORT).show();
        }
    }

    public void Consultar_factura(View view){
        codigo = etcodigo.getText().toString();
        if (!codigo.isEmpty()){
            SQLiteDatabase admin=objConexion.getReadableDatabase();
            Cursor fila=admin.rawQuery("select fecha,TblFactura.Identificacion," +
            "Nombre,Telefono,TblVehiculo_Factura.Placa,Marca,Valor_venta,TblFactura.Activo " +
            "from TblCliente inner join TblFactura on TbLCliente.Identificacion=" +
            "TbLFactura.Identificacion inner join TblVehiculo_Factura on " +
            "TblFactura.CodFactura=TblVehiculo_Factura.CodFactura inner join " +
            "TblVehiculo on TblVehiculo_Factura.Placa=TblVehiculo.Placa " +
            "where TblFactura.CodFactura='"+codigo+"'",null);
            if (fila.moveToNext()){
                sw = true;
                btconsultar.setEnabled(false);
                etcodigo.setEnabled(false);
                etfecha.setEnabled(true);
                etidentificacion.setEnabled(true);
                btconcliente.setEnabled(true);
                btanular.setEnabled(true);
                cbactivo.setEnabled(true);
                etplaca.setEnabled(true);
                btconvehiculo.setEnabled(true);
                btadicionar.setEnabled(true);
                etfecha.setText(fila.getString(0));
                etidentificacion.setText(fila.getString(1));
                etplaca.setText(fila.getString(4));
                tvvalor.setText(fila.getString(6));
                if (fila.getString(7).equals("Si")){
                    cbactivo.setChecked(true);
                }else{
                    cbactivo.setChecked(false);
                }
            }else{
                btconsultar.setEnabled(false);
                etcodigo.setEnabled(false);
                etfecha.setEnabled(true);
                etidentificacion.setEnabled(true);
                btconcliente.setEnabled(true);
                etplaca.setEnabled(true);
                btconvehiculo.setEnabled(true);
                btadicionar.setEnabled(true);
                sw = false;
                Toast.makeText(this, "Registro no existe", Toast.LENGTH_SHORT).show();
                etfecha.requestFocus();
            }
            admin.close();
        }else{
            Toast.makeText(this, "Codigo es requerido para la consulta", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }
    }//Fin metodo Consultar_factura

    public void  Consultar_cliente(View view){
        identificacion = etidentificacion.getText().toString();
        if (!identificacion.isEmpty()){
            SQLiteDatabase admin = objConexion.getReadableDatabase();
            Cursor fila = admin.rawQuery("select Nombre, Telefono from TblCliente where Identificacion='"+identificacion+"'",null);
            if (fila.moveToNext()){
                btconcliente.setEnabled(false);
                etidentificacion.setEnabled(false);
                tvnombre.setText(fila.getString(0));
                tvtelefono.setText(fila.getString(1));
                etplaca.requestFocus();
            }else{
                Toast.makeText(this,"No se encontró un cliente con esa identificación",Toast.LENGTH_SHORT).show();
                etidentificacion.requestFocus();
            }
            admin.close();
        }else{
            Toast.makeText(this,"Debe ingresar una identificación",Toast.LENGTH_SHORT).show();
            etidentificacion.requestFocus();
        }
    }

    public void Consultar_vehiculo(View view){
        placa = etplaca.getText().toString();
        if (!placa.isEmpty()){
            SQLiteDatabase admin = objConexion.getReadableDatabase();
            Cursor fila = admin.rawQuery("select Marca, Valor from TblVehiculo where Placa='"+placa+"'",null);
            if (fila.moveToNext()){
                etplaca.setEnabled(false);
                btconvehiculo.setEnabled(false);
                tvmarca.setText(fila.getString(0));
                tvvalor.setText("");
                if(tvvalor.getText().toString().equals("")){
                    tvvalor.setText(fila.getString(1));
                }
            }else{
                Toast.makeText(this,"No se encontró esa placa",Toast.LENGTH_SHORT).show();
            }
            admin.close();
        }else{
            Toast.makeText(this,"Debe ingresar una placa",Toast.LENGTH_SHORT).show();
        }
    }

    public void Limpiar_Factura(View view){Limpiar_Campos_Factura();}

    public void Regresar_factura(View view){
        Intent intmain = new Intent(this,MainActivity.class);
        startActivity(intmain);
    }

    public void Anular_Factura(View view){
        cbactivo.setChecked(false);
        Toast.makeText(this,"Factura anulada", Toast.LENGTH_SHORT).show();
    }

    public void Limpiar_Campos_Factura(){
        etcodigo.setText("");
        etfecha.setText("");
        etidentificacion.setText("");
        tvnombre.setText("");
        tvtelefono.setText("");
        etplaca.setText("");
        tvmarca.setText("");
        tvvalor.setText("");
        etcodigo.setEnabled(true);
        btconsultar.setEnabled(true);
        etfecha.setEnabled(false);
        etidentificacion.setEnabled(false);
        btconcliente.setEnabled(false);
        etplaca.setEnabled(false);
        btconvehiculo.setEnabled(false);
        cbactivo.setEnabled(false);
        cbactivo.setChecked(false);
        etidentificacion.requestFocus();
    }
}