package regalos.example.com.gestorderegalos.Notificaciones;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import regalos.example.com.gestorderegalos.URL;
import regalos.example.com.gestorderegalos.VolleyRP;

/**
 * Created by Alejandro on 14/11/2017.
 */

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.NotificacionesViewHolder> {

    private List<Notificaciones> listaNotificaciones;
    private Context contexto;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;

    //Inicializar los atributos
    public NotificacionesAdapter(Context contexto, List<Notificaciones> listaNotificaciones) {
        this.listaNotificaciones = listaNotificaciones;
        this.contexto = contexto;
        volleyRP = VolleyRP.getInstance(contexto);
        requestQueue = volleyRP.getRequestQueue();
    }

    @Override
    //Asociar el layout
    public NotificacionesViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.solicitudes_amistad, null);
        return new NotificacionesViewHolder(v);
    }

    @Override
    //Asociar los views con los holder
    public void onBindViewHolder(NotificacionesViewHolder holder, final int position) {

        Picasso.with(contexto).load(listaNotificaciones.get(position).getFotoPerfil()).error(R.drawable.ic_account_circle).resize(42,41).centerCrop().into(holder.fotoPerfil);
        holder.solicitud.setText(listaNotificaciones.get(position).getNotificacion());
        holder.aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aceptar(listaNotificaciones.get(position).getNotificacion());
                actualizarAmistad(listaNotificaciones.get(position).getNotificacion());
            }
        });
        holder.cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelar(listaNotificaciones.get(position).getNotificacion());
            }
        });
    }

    private void aceptar(String nick){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("amigo1", nick);
        hashMap.put("amigo2", MainActivity.nombreUser);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL.ACEPTAR_SOLICITUD, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equalsIgnoreCase("Solicitud aceptada")) {
                        Toast.makeText(contexto, estado, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(contexto, estado, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(contexto, "No se pudo aceptar la solicitud", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contexto, "No se pudo aceptar la solicitud", Toast.LENGTH_SHORT).show();
            }
        });

        volleyRP.addToQueue(solicitud, requestQueue, contexto, volleyRP);
    }

    private void actualizarAmistad(String nick){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("amigo1", MainActivity.nombreUser);
        hashMap.put("amigo2", nick);
        hashMap.put("aceptado", 1+"");

        JsonObjectRequest solicitud2 = new JsonObjectRequest(Request.Method.POST, URL.CREAR_SOLICITUD, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
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

        volleyRP.addToQueue(solicitud2, requestQueue, contexto, volleyRP);
    }

    private void cancelar(String nick){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("amigo1", nick);
        hashMap.put("amigo2", MainActivity.nombreUser);

        JsonObjectRequest solicitud3 = new JsonObjectRequest(Request.Method.POST, URL.CANCELAR_SOLICITUD, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equalsIgnoreCase("Solicitud cancelada")) {
                        Toast.makeText(contexto, estado, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(contexto, estado, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(contexto, "No se pudo cancelar la solicitud", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contexto, "No se pudo cancelar la solicitud", Toast.LENGTH_SHORT).show();
            }
        });

        volleyRP.addToQueue(solicitud3, requestQueue, contexto, volleyRP);
    }

    @Override
    public int getItemCount() {
        return listaNotificaciones.size();
    }

    static class NotificacionesViewHolder extends RecyclerView.ViewHolder{

        ImageView fotoPerfil;
        TextView solicitud;
        Button aceptar, cancelar;

        public NotificacionesViewHolder(View itemView) {
            super(itemView);
            fotoPerfil = (ImageView) itemView.findViewById(R.id.fotoPerfilNotify);
            solicitud = (TextView) itemView.findViewById(R.id.notificacionTV);
            aceptar = (Button)itemView.findViewById(R.id.aceptarNotifyButton);
            cancelar = (Button)itemView.findViewById(R.id.cancelarNotifyButton);
        }
    }
}
