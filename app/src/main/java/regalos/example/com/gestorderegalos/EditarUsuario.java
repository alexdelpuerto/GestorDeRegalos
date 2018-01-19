package regalos.example.com.gestorderegalos;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class EditarUsuario extends AppCompatActivity {

    private EditText password, nombre, apellidos, correo;
    private Button editarBoton;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;
    private ImageView fotoPerfil;
    private ImageButton camaraButton, galeriaButton;
    private GotevUploadService gotevUploadService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        password = (EditText) findViewById(R.id.passwordEditET);
        nombre = (EditText) findViewById(R.id.nombreEditET);
        apellidos = (EditText) findViewById(R.id.apellidosEditET);
        correo = (EditText) findViewById(R.id.correoEditET);
        editarBoton = (Button) findViewById(R.id.editarbutton);
        fotoPerfil= (ImageView)findViewById(R.id.imagenEdit);
        camaraButton = (ImageButton)findViewById(R.id.camaraButton);
        galeriaButton = (ImageButton)findViewById(R.id.galeriaButton);

        volleyRP = VolleyRP.getInstance(this);
        requestQueue = volleyRP.getRequestQueue();
        obtenerDatos();

        gotevUploadService = new GotevUploadService(this,URL.IMAGEN_UPLOAD) {
            @Override
            public void onProgress(UploadInfo uploadInfo) {

            }

            @Override
            public void onError(UploadInfo uploadInfo, Exception exception) {

            }

            @Override
            public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {

            }

            @Override
            public void onCancelled(UploadInfo uploadInfo) {

            }
        };
        gotevUploadService.setParameterNamePhoto("file");
        //no eliminar la foto temporal al momento de subirla
        gotevUploadService.setEliminarFoto(false);

        galeriaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotevUploadService.subirFotoGaleria();
            }
        });

        camaraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotevUploadService.subirFotoCamara();
            }
        });
        GotevUploadService.verifyStoragePermissions(this);

        editarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!password.getText().toString().trim().equals("")){
                    editar(password.getText().toString(), nombre.getText().toString(), apellidos.getText().toString(), correo.getText().toString());
                }
                else {
                    Toast.makeText(getApplicationContext(), "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri u = gotevUploadService.onActivityResult(requestCode, resultCode, data);
        if (u!=null){
            fotoPerfil.setImageURI(u);
        }
    }

    public void editar(String pw, String nom, String apell, String email){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id_usuario", MainActivity.nombreUser);
        hashMap.put("password", pw);
        hashMap.put("nombre", nom);
        hashMap.put("apellidos", apell);
        hashMap.put("correo", email);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL.LOGIN_UPDATE, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equalsIgnoreCase("Se actualizó correctamente")) {
                        Toast.makeText(getApplicationContext(), estado, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), estado, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "No se pudo actualizar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se pudo actualizar el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        volleyRP.addToQueue(solicitud, requestQueue, getApplicationContext(), volleyRP);
    }

    public void obtenerDatos(){
        JsonObjectRequest solicitud2 = new JsonObjectRequest(URL.LOGIN_GETDATOS+MainActivity.nombreUser, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    String codigo = response.getString("resultado");
                    if (codigo.equals("OK")){
                        //El usuario existe
                        JSONObject datos = response.getJSONObject("datos");
                        String pw = datos.getString("password");
                        String nom = datos.getString("nombre");
                        String apell = datos.getString("apellidos");
                        String email = datos.getString("correo");
                        String foto = datos.getString("imagen");
                        password.setText(pw);
                        nombre.setText(nom);
                        apellidos.setText(apell);
                        correo.setText(email);
                        Picasso.with(EditarUsuario.this).load(foto).error(R.drawable.ic_account_circle).resize(200, 200).centerCrop().into(fotoPerfil);
                    }else {
                        Toast.makeText(EditarUsuario.this, codigo, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditarUsuario.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });

        volleyRP.addToQueue(solicitud2, requestQueue, EditarUsuario.this, volleyRP);
    }
}
