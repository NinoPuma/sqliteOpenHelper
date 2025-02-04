package com.example.slqliteopenhelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    AdminSQLiteOpenHelper gestor;
    EditText etEstudios, etNombre, etEdad;
    Button btn_agregar, btn_modificar, btn_borrar, btn_consultar, btn_5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        gestor = new AdminSQLiteOpenHelper(this, "politico", null, 1);
        etEstudios = findViewById(R.id.etEstudios);
        etNombre = findViewById(R.id.etNombre);
        etEdad = findViewById(R.id.etEdad);
        btn_agregar = findViewById(R.id.btn_agregar);
        btn_modificar = findViewById(R.id.btn_modificar);
        btn_borrar = findViewById(R.id.btn_borrar);
        btn_consultar = findViewById(R.id.btn_consultar);

    }
    public void guardarPolitico(View view){
        SQLiteDatabase db = gestor.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("NOMBRE", etNombre.getText().toString());
        registro.put("EDAD", Integer.parseInt(etEdad.getText().toString()));
        registro.put("ESTUDIOS", etEstudios.getText().toString());
        db.insert("LIDERESPOLITICOS", null, registro);
        db.close();
    }
    public Politico consultarPolitica(View view) {
        Politico politico1 = new Politico();
        SQLiteDatabase db = gestor.getReadableDatabase();
        String[] aux = {etNombre.getText().toString()};
        Cursor fila = db.query("LIDERESPOLITICOS", null, "NOMBRE=?", aux, null, null, null);
        if (fila.moveToFirst()) {
            politico1.setNombre(fila.getString(fila.getColumnIndex("NOMBRE")));
            politico1.setEdad(fila.getInt(fila.getColumnIndex("EDAD")));
            politico1.setEstudios(fila.getString(fila.getColumnIndex("ESTUDIOS")));
            etEdad.setText(String.valueOf(politico1.getEdad()));
            etEstudios.setText(politico1.getEstudios());
            Toast.makeText(this, "Líder encontrado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Líder no encontrado", Toast.LENGTH_SHORT).show();
        }
        fila.close();
        db.close();
        return politico1;
    }
    public void modificarPolitica(View view){
        SQLiteDatabase db = gestor.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("EDAD", Integer.parseInt(etEdad.getText().toString()));
        registro.put("ESTUDIOS", etEstudios.getText().toString());
        int filasUpdateadas = db.update("LIDERESPOLITICOS", registro, "NOMBRE = '" + etNombre.getText().toString() + "'", null);
        if (filasUpdateadas == 1){
            Toast.makeText(this, "Líder modificado", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Líder no encontrado", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
    public void borrarPolitico(View view){
        SQLiteDatabase db = gestor.getWritableDatabase();
        int filasBorradas = db.delete("LIDERESPOLITICOS", "NOMBRE = '" + etNombre.getText().toString() + "'", null);
        if (filasBorradas == 1){
            Toast.makeText(this, "Líder eliminado"+ etNombre.getText().toString(), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Líder no encontrado"+ etNombre.getText().toString(), Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}