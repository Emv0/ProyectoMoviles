package com.example.ventas_jueves;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class ClsOpenHelper extends SQLiteOpenHelper {
    public ClsOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table TblCliente(Identificacion text primary key," +
        "Nombre text not null,Direccion text not null,Telefono text not null," +
        "Activo text default 'Si')");
        db.execSQL("create table TblVehiculo(Placa text primary key," +
        "Marca text not null,Modelo text not null,Valor integer not null," +
        "Activo text default 'Si')");
        db.execSQL("Create table TblFactura(CodFactura text primary key," +
        "fecha text not null,Identificacion text not null,Activo text " +
        "default 'Si',constraint pk_factura foreign key (Identificacion) " +
        "references TblCliente (Identificacion))");
        db.execSQL("Create table TblVehiculo_Factura(CodFactura text," +
        "Placa text,Valor_venta integer not null,constraint pk_detalle " +
        "primary key(CodFactura,PLaca),foreign key (CodFactura) " +
        "references TblFactura(CodFactura),foreign key (Placa) " +
        "references TblVehiculo(Placa))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP table TblVehiculo_Factura");
        db.execSQL("DROP table TblFactura");
        db.execSQL("DROP table TblVehiculo");
        db.execSQL("DROP table TblCliente");{
            onCreate(db);
        }
    }
}
