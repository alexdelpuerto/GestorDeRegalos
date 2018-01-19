package regalos.example.com.gestorderegalos.Regalos;

import android.content.Context;
import android.content.Intent;
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

/**
 * Created by Alejandro on 27/11/2017.
 */

public class RegaloAdapter extends RecyclerView.Adapter<RegaloAdapter.RegalosViewHolder> {

    private List<Regalo> listaRegalos;
    private Context context;
    private boolean privilegios;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;
    private ArrayList<String> lista_users;
    private String id_regalo, id_evento;

    //Inicializar atributos
    public RegaloAdapter(Context context, List<Regalo> listaRegalos, boolean privilegios) {
        this.listaRegalos = listaRegalos;
        this.context = context;
        this.privilegios = privilegios;
        volleyRP = VolleyRP.getInstance(context);
        requestQueue = volleyRP.getRequestQueue();
    }

    @Override
    //Asociar el layout
    public RegalosViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_regalos, null);
        return new RegalosViewHolder(v);
    }

    @Override
    //Asociar los views al holder
    public void onBindViewHolder(RegalosViewHolder holder, final int position) {
        holder.nombreRegalo.setText(listaRegalos.get(position).getNombre());
        holder.precio.setText(listaRegalos.get(position).getPrecio()+"€");
        Picasso.with(context).load(listaRegalos.get(position).getFoto()).error(R.drawable.ic_menu_gallery).resize(140,140).centerInside().into(holder.fotoRegalo);
        holder.comprado.setImageResource(R.drawable.ic_action_cart);
        if(listaRegalos.get(position).getComprado()){
            holder.noComp.setVisibility(View.VISIBLE);
            holder.comprado.setVisibility(View.GONE);

        } else {
            holder.comprado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id_regalo = listaRegalos.get(position).getId();
                    id_evento = listaRegalos.get(position).getEvento();
                    obtenerUsersPertenencia(listaRegalos.get(position).getEvento(), MainActivity.nombreUser);
                }
            });
        }

    }

    private void obtenerUsersPertenencia(String evento, String usuario) {
        JsonObjectRequest solicitud = new JsonObjectRequest(URL.GET_USERS_PERTENENCIA + "id_evento=" + evento + "&id_usuario=" + usuario, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try{
                    String codigo = datos.getString("resultado");
                    if(codigo.equals("OK")){
                        JSONArray jsonArray = new JSONArray(datos.getString("datos"));
                        comprarRegalo(jsonArray);
                    }
                }catch (JSONException e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });
        volleyRP.addToQueue(solicitud, requestQueue, context, volleyRP);
    }

    private void comprarRegalo(JSONArray jsonArray) {
        lista_users = new ArrayList<>();
        lista_users.add(id_regalo);
        lista_users.add(id_evento);
        lista_users.add(MainActivity.nombreUser);
        try {
            for (int i=0; i<jsonArray.length(); i++){
                lista_users.add(jsonArray.getJSONObject(i).getString("usuario"));
            }
        } catch (JSONException e) {
                e.printStackTrace();
        }

        HashMap<String, ArrayList> hashMap = new HashMap<>();
        hashMap.put("usuarios", lista_users);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL.REGALOS_COMPRAR, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equalsIgnoreCase("Pago completado")) {
                        Toast.makeText(context, estado, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, estado, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Error en el pago", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error en el pago", Toast.LENGTH_SHORT).show();
            }
        });

        volleyRP.addToQueue(solicitud, requestQueue, context, volleyRP);

    }

    @Override
    public int getItemCount() {
        return listaRegalos.size();
    }

    class RegalosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nombreRegalo;
        ImageView fotoRegalo;
        TextView precio;
        ImageButton comprado;
        ImageView noComp;

        RegalosViewHolder(View itemView) {
            super(itemView);
            nombreRegalo = (TextView)itemView.findViewById(R.id.nombreRegaloTV);
            fotoRegalo = (ImageView)itemView.findViewById(R.id.fotoRegalo);
            precio = (TextView)itemView.findViewById(R.id.precioTV);
            comprado = (ImageButton)itemView.findViewById(R.id.compradoButton);
            noComp = (ImageView) itemView.findViewById(R.id.compradoImagen);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, VistaRegalo.class);
            i.putExtra("id_regalo", listaRegalos.get(getAdapterPosition()).getId());
            i.putExtra("nombreReg", listaRegalos.get(getAdapterPosition()).getNombre());
            i.putExtra("precioReg", listaRegalos.get(getAdapterPosition()).getPrecio());
            i.putExtra("descReg", listaRegalos.get(getAdapterPosition()).getDescripcion());
            i.putExtra("fotoReg", listaRegalos.get(getAdapterPosition()).getFoto());
            i.putExtra("comprado", listaRegalos.get(getAdapterPosition()).getComprado());
            i.putExtra("privilegios", privilegios);
            context.startActivity(i);
        }
    }
}
