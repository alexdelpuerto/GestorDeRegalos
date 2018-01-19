package regalos.example.com.gestorderegalos.Pagos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import regalos.example.com.gestorderegalos.URL;
import regalos.example.com.gestorderegalos.VolleyRP;

/**
 * Created by Alejandro on 19/10/2017.
 */

public class DineroAdapter extends RecyclerView.Adapter<DineroAdapter.DineroViewHolder> {

    private List<Dinero> listaDineros;
    private Context context;
    private int tipo;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;

    //Inicializar atributos
    public DineroAdapter(Context context, List<Dinero> listaDineros, int tipo){
        this.context = context;
        this.listaDineros = listaDineros;
        this.tipo = tipo;
        volleyRP = VolleyRP.getInstance(context);
        requestQueue = volleyRP.getRequestQueue();
    }

    @Override
    //Asociar el layout
    public DineroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_pagos, null);
        return new DineroViewHolder(v);
    }

    @Override
    //Asociar los views con los holder
    public void onBindViewHolder(final DineroViewHolder holder, final int position) {
        double precioAPagar = (listaDineros.get(position).getPrecio())/(listaDineros.get(position).getNumUsuarios());
        precioAPagar = (double)Math.round(precioAPagar * 100d)/100d;

        holder.nombreRegaloPago.setText(listaDineros.get(position).getNombreRegalo());
        if (tipo==1){
            holder.nombrePago.setText("Debes "+precioAPagar+"€ a "+listaDineros.get(position).getUsuario());
        } else {
            holder.nombrePago.setText(listaDineros.get(position).getUsuario()+" te debe "+precioAPagar+"€");
            holder.borrarPagoButton.setVisibility(View.VISIBLE);
            holder.borrarPagoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarPago(listaDineros.get(position).getId_regalo(), listaDineros.get(position).getUsuario());
                }
            });
        }
    }

    private void borrarPago(int id_regalo, String id_usuario) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id_regalo", id_regalo+"");
        hashMap.put("id_usuario", id_usuario);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL.PAGOS_DELETE, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equalsIgnoreCase("Pago borrado correctamente")) {
                        Toast.makeText(context, estado, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, estado, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "No se pudo borrar el pago", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No se pudo borrar el pago", Toast.LENGTH_SHORT).show();
            }
        });

        volleyRP.addToQueue(solicitud, requestQueue, context, volleyRP);
    }

    @Override
    public int getItemCount() {
        return listaDineros.size();
    }

    static class DineroViewHolder extends RecyclerView.ViewHolder{

        TextView nombrePago;
        TextView nombreRegaloPago;
        ImageButton borrarPagoButton;

        DineroViewHolder(View view){
            super(view);
            nombrePago = (TextView) view.findViewById(R.id.nombrePago);
            nombreRegaloPago = (TextView) view.findViewById(R.id.nombreRegaloPago);
            borrarPagoButton = (ImageButton)view.findViewById(R.id.borrarPagoButton);
        }
    }
}
