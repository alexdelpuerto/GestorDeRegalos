package regalos.example.com.gestorderegalos.Regalos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import regalos.example.com.gestorderegalos.R;
import regalos.example.com.gestorderegalos.URL;
import regalos.example.com.gestorderegalos.VolleyRP;

public class VistaRegalo extends AppCompatActivity {

    private TextView nomVistaReg, precVistaReg, descVistaReg;
    private ImageView fotoVistaReg;
    private ImageButton borrarRegaloButton;
    private boolean privilegios, comprado;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;
    private String id_regalo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_regalo);

        volleyRP = VolleyRP.getInstance(this);
        requestQueue = volleyRP.getRequestQueue();

        nomVistaReg = (TextView)findViewById(R.id.nombreVistaReg);
        precVistaReg = (TextView)findViewById(R.id.precVistaReg);
        descVistaReg = (TextView)findViewById(R.id.descVistaReg);
        fotoVistaReg = (ImageView)findViewById(R.id.fotoVistaReg);
        borrarRegaloButton = (ImageButton)findViewById(R.id.borrarRegaloButton);

        id_regalo = getIntent().getExtras().getString("id_regalo");
        nomVistaReg.setText(getIntent().getExtras().getString("nombreReg"));
        precVistaReg.setText(getIntent().getExtras().getString("precioReg")+"€");
        descVistaReg.setText(getIntent().getExtras().getString("descReg"));
        Picasso.with(this).load(getIntent().getExtras().getString("fotoReg")).error(R.drawable.ic_menu_gallery).resize(270,270).centerInside().into(fotoVistaReg);

        comprado = getIntent().getExtras().getBoolean("comprado");
        privilegios = getIntent().getExtras().getBoolean("privilegios");
        //Solo el creador del evento puede borrar el regalo; y solo si no está comprado porque se generaría un problema en los pagos
        if(privilegios && !comprado) {
            borrarRegaloButton.setVisibility(View.VISIBLE);
            borrarRegaloButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarRegalo(id_regalo);
                    finish();
                }
            });
        }

    }

    private void borrarRegalo(String id_regalo) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id_regalo", id_regalo);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL.REGALOS_DELETE, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equalsIgnoreCase("Regalo borrado correctamente")) {
                        Toast.makeText(VistaRegalo.this, estado, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VistaRegalo.this, estado, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(VistaRegalo.this, "No se pudo borrar el regalo", Toast.LENGTH_SHORT).show();
                }
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VistaRegalo.this, "No se pudo borrar el regalo", Toast.LENGTH_SHORT).show();
            }
        });

        volleyRP.addToQueue(solicitud, requestQueue, VistaRegalo.this, volleyRP);
    }
}
