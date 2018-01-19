package regalos.example.com.gestorderegalos.Amigos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;
import regalos.example.com.gestorderegalos.MainActivity;
import regalos.example.com.gestorderegalos.R;
import regalos.example.com.gestorderegalos.Registro;
import regalos.example.com.gestorderegalos.URL;
import regalos.example.com.gestorderegalos.VolleyRP;

/**
 * Created by Alejandro on 13/11/2017.
 */

public class BuscadorAdapter extends RecyclerView.Adapter<BuscadorAdapter.AmigosViewHolder> {

    private List<Amigo> listaAmigos;
    private Context contexto;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;

    //Inicializar atributos
    public BuscadorAdapter(Context contexto, List<Amigo> listaAmigos) {
        this.contexto = contexto;
        this.listaAmigos = listaAmigos;
        volleyRP = VolleyRP.getInstance(contexto);
        requestQueue = volleyRP.getRequestQueue();
    }

    @Override
    //Asociar el layout
    public AmigosViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_amigos_buscar, null);
        return new AmigosViewHolder(v);
    }

    @Override
    //Asociar los views con los holder
    public void onBindViewHolder(AmigosViewHolder holder, final int position) {
        holder.nick.setText(listaAmigos.get(position).getNick());
        holder.nombreComp.setText(listaAmigos.get(position).getNombreComp());
        Picasso.with(contexto).load(listaAmigos.get(position).getFotoPerfil()).error(R.drawable.ic_account_circle).resize(42,41).centerCrop().into(holder.fotoPerfil);

        holder.agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitar(listaAmigos.get(position).getNick());
            }
        });
    }

    private void solicitar(String nick) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("amigo1", MainActivity.nombreUser);
        hashMap.put("amigo2", nick);
        hashMap.put("aceptado", 0+"");

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL.CREAR_SOLICITUD, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equalsIgnoreCase("Solicitud creada correctamente")) {
                        Toast.makeText(contexto, estado, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(contexto, estado, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(contexto, "No se pudo crear la solicitud", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contexto, "No se pudo crear la solicitud", Toast.LENGTH_SHORT).show();
            }
        });

        volleyRP.addToQueue(solicitud, requestQueue, contexto, volleyRP);
    }

    @Override
    public int getItemCount() {
        return listaAmigos.size();
    }

    static class AmigosViewHolder extends RecyclerView.ViewHolder{

        TextView nick;
        TextView nombreComp;
        ImageView fotoPerfil;
        ImageButton agregar;

        AmigosViewHolder(View view){
            super(view);
            nick = (TextView) view.findViewById(R.id.nickBTV);
            nombreComp = (TextView) view.findViewById(R.id.nombreCompBTV);
            fotoPerfil = (ImageView) view.findViewById(R.id.fotoPerfilB);
            agregar = (ImageButton) view.findViewById(R.id.agregarAmigoButton);
        }
    }
}


