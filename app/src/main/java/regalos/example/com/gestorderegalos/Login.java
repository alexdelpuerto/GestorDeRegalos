package regalos.example.com.gestorderegalos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class Login extends AppCompatActivity {

    private EditText usuario, password;
    private Button iniciar, registro;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;
    private String USER, PASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = (EditText) findViewById(R.id.usuarioET);
        password = (EditText) findViewById(R.id.passwordET);
        iniciar = (Button) findViewById(R.id.loginButton);
        registro = (Button)findViewById(R.id.registroButton);

        volleyRP = VolleyRP.getInstance(this);
        requestQueue = volleyRP.getRequestQueue();


        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Comprobando datos...", Toast.LENGTH_SHORT).show();
                verificarUsuario(usuario.getText().toString(), password.getText().toString());
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarRegistro();
            }
        });
    }

    public void verificarUsuario(String usuario, String password){
        //Cogemos el id de usuario y se asigna a la url para la peticion
        USER = usuario;
        PASSWORD = password;
        solicitudJSON(URL.LOGIN_GETID+usuario);
    }

    public void solicitudJSON(String URL){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject datos) {
                verificarPassword(datos);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });
        volleyRP.addToQueue(solicitud, requestQueue, this, volleyRP);
    }

    public void verificarPassword(JSONObject datos){
        //Conprobar los datos que llegan del Json
        try {
            String codigo = datos.getString("resultado");
            if (codigo.equals("OK")){
                //El usuario existe

                //"datos" es el nombre del json cuando haces una petición al servidor
                //Obtenemos los datos sin la parte del codigo de OK
                JSONObject json = new JSONObject(datos.getString("datos"));
                String id = json.getString("id_usuario");
                String pw = json.getString("password");
                //String imagen = json.getString("imagen");

                if((id.equals(USER)) && pw.equals(PASSWORD)){
                    //Actualizar el token
                    String Token = FirebaseInstanceId.getInstance().getToken();
                    actualizarToken(USER, Token);
                    //Cargamos la activity main
                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("Nombre", id);
                    //i.putExtra("imagen", imagen);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(this, "La contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, codigo, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {}
    }

    public void lanzarRegistro(){
        Intent i = new Intent(this, Registro.class);
        startActivity(i);
    }

    private void actualizarToken(String user, String token){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id_usuario", user);
        hashMap.put("token", token);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL.TOKEN_UPDATE, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject datos) {
                Toast.makeText(Login.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });
        volleyRP.addToQueue(solicitud, requestQueue, this, volleyRP);
    }
}
