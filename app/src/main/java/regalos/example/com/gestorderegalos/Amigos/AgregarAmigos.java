package regalos.example.com.gestorderegalos.Amigos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import regalos.example.com.gestorderegalos.MainActivity;
import regalos.example.com.gestorderegalos.R;
import regalos.example.com.gestorderegalos.URL;
import regalos.example.com.gestorderegalos.VolleyRP;

public class AgregarAmigos extends AppCompatActivity {

    private RecyclerView recyclerAgregar;
    private List<Amigo> listaAgregar;
    private AgregarAdapter adapter;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;
    private Button aniadir;
    private String id_evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_amigos);

        id_evento = getIntent().getExtras().getString("id_evento");

        recyclerAgregar = (RecyclerView)findViewById(R.id.recyclerViewAgregar);
        recyclerAgregar.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerAgregar.setLayoutManager(lm);

        aniadir = (Button)findViewById(R.id.agregarButton);

        volleyRP = VolleyRP.getInstance(this);
        requestQueue = volleyRP.getRequestQueue();

        listaAgregar = new ArrayList<>();
        cargarAmigos();
        adapter = new AgregarAdapter(listaAgregar, this);

        recyclerAgregar.setAdapter(adapter);


        aniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> lista_usuarios = new ArrayList<String>();
                lista_usuarios.add(id_evento);
                for (int i=0; i<listaAgregar.size(); i++){
                    if (listaAgregar.get(i).getMarcado()==true){
                        lista_usuarios.add(listaAgregar.get(i).getNick());
                    }
                }
                //Tiene que ser mayor que 1, porque el primer elemento es el id_evento donde se añaden los usuarios
                if (lista_usuarios.size()>1){
                    aniadirGrupo(lista_usuarios);
                }
                finish();
            }
        });
    }

    private void aniadirGrupo(ArrayList lista) {
        HashMap<String, ArrayList> hashMap = new HashMap<>();
        hashMap.put("usuarios", lista);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL.CREAR_PERT_MULT, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equalsIgnoreCase("Usuarios agregados correctamente")) {
                        Toast.makeText(getApplicationContext(), estado, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), estado, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error al agregar usuarios", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al agregar usuarios", Toast.LENGTH_SHORT).show();
            }
        });

        volleyRP.addToQueue(solicitud, requestQueue, getApplicationContext(), volleyRP);
    }

    public void cargarAmigos(){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL.AMIGOS_GET_AGREGAR + "id_usuario=" + MainActivity.nombreUser + "&id_evento=" +id_evento , null, new Response.Listener<JSONObject>() {
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

    public void cargarRecycler(JSONArray jsonArray){
        listaAgregar.clear();
        for(int i=0; i<jsonArray.length(); i++){
            Amigo amigoAux = new Amigo();
            try {
                amigoAux.setNick(jsonArray.getJSONObject(i).getString("id_usuario"));
                amigoAux.setNombreComp(jsonArray.getJSONObject(i).getString("nombre")+" "+jsonArray.getJSONObject(i).getString("apellidos"));
                amigoAux.setFotoPerfil(jsonArray.getJSONObject(i).getString("imagen"));

                listaAgregar.add(amigoAux);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        AgregarAdapter adaptador = new AgregarAdapter(listaAgregar, this);
        recyclerAgregar.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();

    }
}
