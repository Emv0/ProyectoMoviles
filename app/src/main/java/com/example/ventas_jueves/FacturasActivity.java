package com.example.ventas_jueves;

import androidx.appcompat.app.AppCompatActivity;
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
    String codigo;
    ClsOpenHelper objconexion=new ClsOpenHelper(this,"Concesionario1.db",null,1);

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
    }//Fin metodo onCreate

    public void Consultar_factura(View view){
        codigo=etcodigo.getText().toString();
        if (!codigo.isEmpty()){
            SQLiteDatabase admin=objconexion.getReadableDatabase();
            Cursor fila=admin.rawQuery("select fecha,TblFactura.Identificacion," +
            "Nombre,Telefono,TblVehiculo_Factura.Placa,Marca,Valor_venta,TblFactura.Activo " +
            "from TblCliente inner join TblFactura on TbLCliente.Identificacion=" +
            "TbLFactura.Identificacion inner join TblVehiculo_Factura on " +
            "TblFactura.CodFactura=TblVehiculo_Factura.CodFactura inner join " +
            "TblVehiculo on TblVehiculo_Factura.Placa=TblVehiculo.Placa " +
            "where TblFactura.CodFactura='"+codigo+"'",null);
            if (fila.moveToNext()){
                tvnombre.setText(fila.getString(2));
                tvmarca.setText(fila.getString(5));
            }else{
                Toast.makeText(this, "Registro no existe", Toast.LENGTH_SHORT).show();
                etfecha.requestFocus();
            }
            admin.close();
        }else{
            Toast.makeText(this, "Codigo es requerido para la consulta", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }
    }//Fin metodo Consultar_factura
}