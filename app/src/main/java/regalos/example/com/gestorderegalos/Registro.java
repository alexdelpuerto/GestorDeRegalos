package regalos.example.com.gestorderegalos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class Registro extends AppCompatActivity {

    private EditText nombreReg, passwordReg, correo, nombre, apellidos;
    private Button registrar;

    private VolleyRP volleyRP;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        volleyRP = VolleyRP.getInstance(this);
        requestQueue = volleyRP.getRequestQueue();

        nombreReg = (EditText) findViewById(R.id.ususarioRegET);
        passwordReg = (EditText) findViewById(R.id.passwordRegET);
        correo = (EditText) findViewById(R.id.correoET);
        nombre = (EditText) findViewById(R.id.nombreET);
        apellidos = (EditText) findViewById(R.id.apellidosET);

        registrar = (Button)findViewById(R.id.registrarButton);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar(nombreReg.getText().toString(),
                        passwordReg.getText().toString(),
                        correo.getText().toString(),
                        nombre.getText().toString(),
                        apellidos.getText().toString());
            }
        });
    }

    public void registrar(final String user, String password, String correo, String nombre, String apellidos){
        //Si no hay ningun user con ese nombre en la BD y si la contraseña no está vacía llamamos al main
        if ((!password.equals("")) && (!user.equals(""))) {
            String Token = FirebaseInstanceId.getInstance().getToken();

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id_usuario", user);
            hashMap.put("password", password);
            hashMap.put("nombre", nombre);
            hashMap.put("apellidos", apellidos);
            hashMap.put("correo", correo);
            hashMap.put("token", Token);

            JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL.REGISTRO_INSERT, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject datos) {
                    try {
                        String estado = datos.getString("resultado");
                        if (estado.equalsIgnoreCase("Usuario registrado correctamente")) {
                            Toast.makeText(Registro.this, estado, Toast.LENGTH_SHORT).show();
                            lanzarMain(user);
                        } else {
                            Toast.makeText(Registro.this, estado, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Registro.this, "No se pudo hacer el registro: el nombre de usuario ya existe", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Registro.this, "No se pudo hacer el registro: el nombre de usuario ya existe", Toast.LENGTH_SHORT).show();
                }
            });

            volleyRP.addToQueue(solicitud, requestQueue, this, volleyRP);
        }
        else {
            Toast.makeText(Registro.this, "El usuario ni la contraseña pueden estar vacíos", Toast.LENGTH_SHORT).show();
        }
    }

    private void lanzarMain(String user) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("Nombre", user);
        startActivity(i);
        finish();
    }
}
