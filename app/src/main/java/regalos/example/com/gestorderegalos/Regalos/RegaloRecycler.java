package regalos.example.com.gestorderegalos.Regalos;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import regalos.example.com.gestorderegalos.Amigos.AgregarAmigos;
import regalos.example.com.gestorderegalos.R;
import regalos.example.com.gestorderegalos.URL;
import regalos.example.com.gestorderegalos.VolleyRP;

public class RegaloRecycler extends AppCompatActivity {

    private RecyclerView recyclerRegalos;
    private List<Regalo> listaRegalos;
    private RegaloAdapter adapter;
    private String id_evento;
    private boolean privilegios;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;
    private FloatingActionButton actualizarReg, agregarAmigos, agregarRegalo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regalo_recycler);

        volleyRP = VolleyRP.getInstance(getApplicationContext());
        requestQueue = volleyRP.getRequestQueue();

        listaRegalos = new ArrayList<>();
        recyclerRegalos = (RecyclerView) findViewById(R.id.recyclerRegalos);
        recyclerRegalos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerRegalos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        GridLayoutManager gm = new GridLayoutManager(this, 2);
        recyclerRegalos.setLayoutManager(gm);

        actualizarReg = (FloatingActionButton)findViewById(R.id.actualizarReg);
        actualizarReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarRegalos();
            }
        });

        agregarAmigos = (FloatingActionButton)findViewById(R.id.agregarAmigos);
        agregarAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarAgregarAmigos();
            }
        });

        agregarRegalo = (FloatingActionButton)findViewById(R.id.agregarRegalo);
        agregarRegalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarAgregarRegalo();
            }
        });

        id_evento = getIntent().getExtras().getString("id_evento");
        privilegios = getIntent().getExtras().getBoolean("privilegios");
        cargarRegalos();


        adapter = new RegaloAdapter(this,listaRegalos, privilegios);
        recyclerRegalos.setAdapter(adapter);
    }

    private void lanzarAgregarAmigos() {
        Intent i = new Intent(this, AgregarAmigos.class);
        i.putExtra("id_evento", id_evento);
        startActivity(i);
    }

    private void lanzarAgregarRegalo() {
        Intent i = new Intent(this, NuevoRegalo.class);
        i.putExtra("id_evento", id_evento);
        startActivity(i);
    }

    private void cargarRegalos() {
        JsonObjectRequest solicitud = new JsonObjectRequest(URL.REGALOS_GET + id_evento, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try{
                    String codigo = datos.getString("resultado");
                    if(codigo.equals("OK")){
                        JSONArray jsonArray = new JSONArray(datos.getString("datos"));
                        cargarRecycler(jsonArray);
                    }
                }catch (JSONException e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });
        volleyRP.addToQueue(solicitud, requestQueue, getApplicationContext(), volleyRP);
    }

    private void cargarRecycler(JSONArray jsonArray) {
        listaRegalos.clear();
        for(int i=0; i<jsonArray.length(); i++){
            Regalo regaloAux = new Regalo();
            try {
                regaloAux.setId(jsonArray.getJSONObject(i).getString("id_regalo"));
                regaloAux.setNombre(jsonArray.getJSONObject(i).getString("nombre"));
                regaloAux.setDescripcion(jsonArray.getJSONObject(i).getString("descripcion"));
                regaloAux.setPrecio(jsonArray.getJSONObject(i).getString("precio"));
                regaloAux.setComprador(jsonArray.getJSONObject(i).getString("comprador"));
                String comp = jsonArray.getJSONObject(i).getString("comprado");
                if(comp.equals("1")){
                    regaloAux.setComprado(true);
                } else if (comp.equals("0")){
                    regaloAux.setComprado(false);
                }

                regaloAux.setEvento(id_evento);
                regaloAux.setFoto(jsonArray.getJSONObject(i).getString("foto"));

                listaRegalos.add(regaloAux);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        RegaloAdapter adaptador = new RegaloAdapter(this,listaRegalos, privilegios);
        recyclerRegalos.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
    }
}
