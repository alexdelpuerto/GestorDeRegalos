package regalos.example.com.gestorderegalos.Amigos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
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
import regalos.example.com.gestorderegalos.MainActivity;
import regalos.example.com.gestorderegalos.R;
import regalos.example.com.gestorderegalos.URL;
import regalos.example.com.gestorderegalos.VolleyRP;

public class NuevoAmigo extends AppCompatActivity {

    private RecyclerView recyclerBuscar;
    private List<Amigo> listaAmigosBuscar;
    private BuscadorAdapter adapter;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_amigo);

        volleyRP = VolleyRP.getInstance(this);
        requestQueue = volleyRP.getRequestQueue();

        recyclerBuscar = (RecyclerView)findViewById(R.id.recyclerBuscar);
        recyclerBuscar.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerBuscar.setLayoutManager(lm);

        listaAmigosBuscar = new ArrayList<>();
        adapter = new BuscadorAdapter(getApplicationContext(),listaAmigosBuscar);

        recyclerBuscar.setAdapter(adapter);

        searchView = (SearchView)findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Método que se ejecuta al pulsar en el botón de enter (o lupa) en el teclado
                query = query.trim();
                if(!query.equals("")){
                    listaAmigosBuscar.clear();
                    cargarBuscarAmigos(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Método que se ejecuta cada vez que se pulsa en una tecla
                return false;
            }
        });
    }

    public void cargarBuscarAmigos(String query){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL.LOGIN_BUSCAR + "buscar=" +query + "&id_usuario=" + MainActivity.nombreUser, null, new Response.Listener<JSONObject>() {
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

        for(int i=0; i<jsonArray.length(); i++){
            Amigo amigoAux = new Amigo();
            try {
                amigoAux.setNick(jsonArray.getJSONObject(i).getString("id_usuario"));
                amigoAux.setNombreComp(jsonArray.getJSONObject(i).getString("nombre")+" "+jsonArray.getJSONObject(i).getString("apellidos"));
                amigoAux.setFotoPerfil(jsonArray.getJSONObject(i).getString("imagen"));

                if(!amigoAux.getNick().equals(MainActivity.nombreUser)){
                    listaAmigosBuscar.add(amigoAux);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        BuscadorAdapter adaptador = new BuscadorAdapter(getApplicationContext(),listaAmigosBuscar);

        recyclerBuscar.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
    }
}
