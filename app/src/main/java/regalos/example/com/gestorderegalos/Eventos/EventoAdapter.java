package regalos.example.com.gestorderegalos.Eventos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import regalos.example.com.gestorderegalos.R;
import regalos.example.com.gestorderegalos.Regalos.RegaloRecycler;
import regalos.example.com.gestorderegalos.URL;
import regalos.example.com.gestorderegalos.VolleyRP;

/**
 * Created by Alejandro on 19/10/2017.
 */

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventosViewHolder>{

    private List<Evento> listaEventos;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;
    private Context contexto;

    //Inicializar los atributos
    public EventoAdapter(Context contexto, List<Evento> listaEventos) {
        this.listaEventos = listaEventos;
        this.contexto = contexto;
        volleyRP = VolleyRP.getInstance(contexto);
        requestQueue = volleyRP.getRequestQueue();
    }

    @Override
    //Asociar el layout
    public EventosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_eventos, null);
        return new EventosViewHolder(v);
    }

    @Override
    //Asociar los views con los holder
    public void onBindViewHolder(final EventosViewHolder holder, final int position) {
        holder.nombreEvento.setText(listaEventos.get(position).getNombreEvento());
        holder.dineroFalta.setText(listaEventos.get(position).getDineroFalta());
        holder.progressBar.setProgress(listaEventos.get(position).getMinProgres());
        holder.progressBar.setMax(listaEventos.get(position).getMaxProgres());
        holder.progreso.setText(listaEventos.get(position).getMinProgres()+"/"+listaEventos.get(position).getMaxProgres());
        if(listaEventos.get(position).getPrivilegios()== true){
            //Solo el creador del evento puede borrarlo
            holder.borrarEvento.setVisibility(View.VISIBLE);
            holder.borrarEvento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarEvento(listaEventos.get(position).getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    private void borrarEvento(String id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id_evento", id);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL.EVENTOS_DELETE, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equalsIgnoreCase("Evento borrado correctamente")) {
                        Toast.makeText(contexto, estado, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(contexto, estado, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(contexto, "No se pudo borrar el evento", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contexto, "No se pudo borrar el evento", Toast.LENGTH_SHORT).show();
            }
        });

        volleyRP.addToQueue(solicitud, requestQueue, contexto, volleyRP);
    }


    class EventosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nombreEvento;
        TextView dineroFalta;
        TextView progreso;
        ProgressBar progressBar;
        ImageButton borrarEvento;

        EventosViewHolder(View view){
            super(view);
            nombreEvento = (TextView) view.findViewById(R.id.nombreEventoTV);
            dineroFalta = (TextView) view.findViewById(R.id.dineroFaltaTV);
            progreso = (TextView) view.findViewById(R.id.progreso);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            borrarEvento = (ImageButton) view.findViewById(R.id.borrarEvento);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(contexto, RegaloRecycler.class);
            String id_evento = listaEventos.get(getAdapterPosition()).getId();
            boolean privilegios = listaEventos.get(getAdapterPosition()).getPrivilegios();
            i.putExtra("id_evento", id_evento);
            i.putExtra("privilegios", privilegios);
            contexto.startActivity(i);
        }
    }
}
