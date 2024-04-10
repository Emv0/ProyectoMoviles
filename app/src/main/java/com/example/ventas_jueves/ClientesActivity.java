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

public class ClientesActivity extends AppCompatActivity {

    EditText etidentificacion,etnombre,etdireccion,ettelefono;
    Button btbuscar,btguardar,btanular,btlimpiar,btregresar;
    CheckBox cbactivo;
    String identificacion,nombre,direccion,telefono;
    Long respuesta;
    boolean sw;
    ClsOpenHelper objconexion=new ClsOpenHelper(this,"Concesionario1.db",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        //Asociar objetos Java con objetos Xml
        etidentificacion=findViewById(R.id.etidentificacion);
        etnombre=findViewById(R.id.etnombre);
        etdireccion=findViewById(R.id.etdireccion);
        ettelefono=findViewById(R.id.ettelefono);
        btbuscar=findViewById(R.id.btbuscar);
        btguardar=findViewById(R.id.btguardar);
        btanular=findViewById(R.id.btanular);
        btlimpiar=findViewById(R.id.btlimpiar);
        btregresar=findViewById(R.id.btregresar);
        cbactivo=findViewById(R.id.cbactivo);
        etidentificacion.requestFocus();
        sw=false;
    }//fin metodo onCreate

    public void Guardar(View view) {
        //Pasar la informacion de los objetos a las variables
        identificacion=etidentificacion.getText().toString();
        nombre=etnombre.getText().toString();
        telefono=ettelefono.getText().toString();
        direccion=etdireccion.getText().toString();
        //Validar que no hay variables vacias
        if (!identificacion.isEmpty() && !nombre.isEmpty() && !direccion.isEmpty()
           && !telefono.isEmpty()){
            //Abrir la conexion en modo de escritorio
            SQLiteDatabase admin=objconexion.getWritableDatabase();
            //Instanciar el contenedor
            ContentValues registro=new ContentValues();
            registro.put("Identificacion",identificacion);
            registro.put("Nombre",nombre);
            registro.put("Direccion",direccion);
            registro.put("Telefono",telefono);
            //Guardar el registro en la tabla TbLCliente de la base de datos Concesionario.db
            if (sw == false)
                respuesta = admin.insert("TblCliente",null,registro);
            else{
                if (cbactivo.isChecked())
                    registro.put("Activo","Si");
                else
                    registro.put("Activo","No");
                respuesta=(long)admin.update("TblCliente",registro,"identificacion='"+identificacion+"'",null);
            }

            //Validar si lo hizo o no
            if (respuesta > 0){
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }else{
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etidentificacion.requestFocus();
        }
    }//fin metodo Guardar

    public void Consultar(View view){
        //Llevar la informacion del objeto a la variable
        identificacion=etidentificacion.getText().toString();
        //Validar que si digito la identificacion
        if (!identificacion.isEmpty()){
            //Abrir la base de datos en modo lectura
            SQLiteDatabase admin=objconexion.getReadableDatabase();
            //Definir la tabla en memoria RAM donde voy a guardar el resultado de la consulta
            Cursor fila=admin.rawQuery("select * from TblCliente where identificacion='"+identificacion+"'",null);
            if (fila.moveToNext()){
                sw=true;
                etnombre.setText(fila.getString(1));
                etdireccion.setText(fila.getString(2));
                ettelefono.setText(fila.getString(3));
                if (fila.getString(4).equals("Si"))
                    cbactivo.setChecked(true);
                else
                    cbactivo.setChecked(false);
                btanular.setEnabled(true);
                cbactivo.setEnabled(true);
            }else{
                Toast.makeText(this, "Registro no encontrado", Toast.LENGTH_SHORT).show();
            }
            etidentificacion.setEnabled(true);
            etnombre.setEnabled(true);
            etdireccion.setEnabled(true);
            ettelefono.setEnabled(true);
            btguardar.setEnabled(true);
            etidentificacion.setEnabled(false);
            etnombre.requestFocus();
        }else{
            Toast.makeText(this, "La identificacion es requerida", Toast.LENGTH_SHORT).show();
            etidentificacion.requestFocus();
        }
    }//Fin metodo Consultar

    public void Anular(View view){
        SQLiteDatabase admin=objconexion.getWritableDatabase();
        ContentValues registro=new ContentValues();
        registro.put("Activo","No");
        //Anular el registro
        respuesta=(long)admin.update("TblCliente",registro,"identificacion='"+identificacion+"'",null);
        if (respuesta > 0){
            Toast.makeText(this, "Registro anulado", Toast.LENGTH_SHORT).show();
            Limpiar_campos();
        }else
            Toast.makeText(this, "Error anulando registro", Toast.LENGTH_SHORT).show();
        admin.close();
    }//fin metodo Anular

    public void Limpiar(View view){
        Limpiar_campos();
    }//fin metodo Limpiar

    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }//fin metodo Regresar
    private void Limpiar_campos(){
        etidentificacion.setText("");
        etnombre.setText("");
        etdireccion.setText("");
        ettelefono.setText("");
        etidentificacion.requestFocus();
        etidentificacion.setEnabled(true);
        etnombre.setEnabled(false);
        etdireccion.setEnabled(false);
        ettelefono.setEnabled(false);
        cbactivo.setChecked(false);
        cbactivo.setEnabled(false);
        btguardar.setEnabled(false);
        btanular.setEnabled(false);
        sw=false;
    }//Fin metodo Limpiar_campos
}