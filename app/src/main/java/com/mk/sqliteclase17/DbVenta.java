package com.mk.sqliteclase17;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbVenta extends DbHelper{
    String resultado = "";
    Context context;
    public DbVenta(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertar(Venta venta) {
        long id = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("numero", venta.getNumero());
            values.put("cliente", venta.getCliente());
            values.put("producto", venta.getProducto());
            values.put("cantidad", venta.getCantidad());
            id = db.insert(TABLE_CASO, null, values);
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
        return id;
    }

    public List<Venta> listar() {
        List<Venta> ventas = new ArrayList<>();
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CASO, null);

        if (cursor.moveToFirst()) {
            do {
                Venta venta = new Venta();
                venta.setNumero(cursor.getString(1));
                venta.setCliente(cursor.getString(2));
                venta.setProducto(cursor.getString(3));
                venta.setCantidad(cursor.getDouble(4));
                ventas.add(venta);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ventas;
    }

}
