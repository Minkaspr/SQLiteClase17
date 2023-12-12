package com.mk.sqliteclase17;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

/*
*  Crear el diseño de un APP empresarial sobre la venta de un producto de un minimarket. El siguiente
*  proyecto deberá permitir guardar en BD, así como listar los productos que se han guardado en la BD.
* */

public class MainActivity extends AppCompatActivity {
    EditText etNumero, etCliente, etCantidad, etReporte;
    Button btnGuardar, btnNuevo, btnCerrar, btnListar;
    Spinner sProductos;
    String [] nomProductos = new String [] {"Gaseosa","Jugo","Agua"};
    String nomProducto = "";
    EditText etBuscar;
    Button btnBuscar, btnActualizar;
    private int idVenta = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumero = findViewById(R.id.etNumero);
        etCliente = findViewById(R.id.etCliente);
        etCantidad = findViewById(R.id.etCantidad);
        etReporte = findViewById(R.id.etReporte);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnNuevo = findViewById(R.id.btnNuevo);
        btnCerrar = findViewById(R.id.btnCerrar);
        btnListar = findViewById(R.id.btnListar);

        sProductos= findViewById(R.id.sProductos);
        sProductos.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,nomProductos));

        sProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String posSeleccionado = (String) sProductos.getItemAtPosition(position);
                nomProducto = posSeleccionado;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnCerrar.setOnClickListener(v->finish());
        btnNuevo.setOnClickListener(v-> {
            etNumero.setText("");
            etCliente.setText("");
            etCantidad.setText("");
            etReporte.setText("");
            sProductos.setSelection(0);
        });

        btnGuardar.setOnClickListener(v-> {
            String numero = etNumero.getText().toString();
            String cliente = etCliente.getText().toString();
            String cantidad = etCantidad.getText().toString();

            if (!numero.isEmpty() && !cliente.isEmpty() && !cantidad.isEmpty() && Double.parseDouble(cantidad) >= 1){
                DbVenta dbVenta = new DbVenta(MainActivity.this);
                Venta venta = new Venta();
                venta.setNumero(numero);
                venta.setCliente(cliente);
                venta.setProducto(nomProducto);
                venta.setCantidad(Double.parseDouble(cantidad));
                long id = dbVenta.insertar(venta);
                if (id>0){
                    Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Campos invalidos", Toast.LENGTH_SHORT).show();
            }
        });

        btnListar.setOnClickListener(v->{
            DbVenta dbVenta = new DbVenta(MainActivity.this);
            List<Venta> ventas = dbVenta.listar();
            StringBuilder reporte = new StringBuilder();
            for (Venta venta : ventas) {
                reporte.append("> ID: ").append(venta.getId())
                        .append(" | ").append(venta.getNumero())
                        .append(" | ").append(venta.getCliente())
                        .append(" | ").append(venta.getProducto())
                        .append(" | ").append(venta.getCantidad()).append("\n");
            }
            etReporte.setText(reporte.toString());
        });

        /*
        * Buscar y Actualizar
        * */
        etBuscar = findViewById(R.id.etBuscar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnBuscar.setOnClickListener(v -> {
            String numero = etBuscar.getText().toString();
            if (!numero.isEmpty()) {
                DbVenta dbVenta = new DbVenta(MainActivity.this);
                Venta venta = dbVenta.buscar(numero);
                if (venta != null) {
                    idVenta = venta.getId();
                    etNumero.setText(venta.getNumero());
                    etCliente.setText(venta.getCliente());
                    etCantidad.setText(String.valueOf(venta.getCantidad()));
                    sProductos.setSelection(obtenerIndiceProducto(venta.getProducto()));
                } else {
                    Toast.makeText(this, "No se encontró la venta", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Ingrese un número de venta", Toast.LENGTH_SHORT).show();
            }
        });
        btnActualizar.setOnClickListener(v -> {
            String numero = etNumero.getText().toString();
            String cliente = etCliente.getText().toString();
            String cantidad = etCantidad.getText().toString();

            if (!numero.isEmpty() && !cliente.isEmpty() && !cantidad.isEmpty() && Double.parseDouble(cantidad) >= 1) {
                DbVenta dbVenta = new DbVenta(MainActivity.this);
                Venta venta = new Venta();
                venta.setId(idVenta);
                venta.setNumero(numero);
                venta.setCliente(cliente);
                venta.setProducto(nomProducto);
                venta.setCantidad(Double.parseDouble(cantidad));
                int filasAfectadas = dbVenta.actualizarPorId(venta);
                if (filasAfectadas > 0) {
                    Toast.makeText(this, "Registro actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Campos inválidos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int obtenerIndiceProducto(String producto) {
        for (int i = 0; i < nomProductos.length; i++) {
            if (nomProductos[i].equals(producto)) {
                return i;
            }
        }
        return 0;
    }
}