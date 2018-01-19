package regalos.example.com.gestorderegalos.Amigos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import regalos.example.com.gestorderegalos.R;

/**
 * Created by Alejandro on 04/12/2017.
 */

public class AgregarAdapter extends RecyclerView.Adapter<AgregarAdapter.AmigosViewHolder> {
    private List<Amigo> listaAmigos;
    private Context context;

    //Inicializar atributos
    public AgregarAdapter(List<Amigo> listaAmigos, Context context) {
        this.listaAmigos = listaAmigos;
        this.context = context;
    }

    @Override
    //Asociar el layout
    public AmigosViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_amigos_agregar, null);
        return new AmigosViewHolder(v);
    }

    @Override
    //Asociar los views del layout con los del holder
    public void onBindViewHolder(final AmigosViewHolder holder, final int position) {
        holder.nick.setText(listaAmigos.get(position).getNick());
        holder.nombre.setText(listaAmigos.get(position).getNombreComp());
        Picasso.with(context).load(listaAmigos.get(position).getFotoPerfil()).error(R.drawable.ic_account_circle).resize(42, 41).centerCrop().into(holder.foto);

        holder.checkBox.setChecked(listaAmigos.get(position).getMarcado());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listaAmigos.get(position).setMarcado(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAmigos.size();
    }

    public class AmigosViewHolder extends RecyclerView.ViewHolder {

        TextView nick;
        TextView nombre;
        ImageView foto;
        CheckBox checkBox;

        public AmigosViewHolder(View itemView) {
            super(itemView);
            nick = (TextView)itemView.findViewById(R.id.nickAgregarTV);
            nombre = (TextView)itemView.findViewById(R.id.nombreAgregarTV);
            foto = (ImageView)itemView.findViewById(R.id.fotoAgregar);
            checkBox = (CheckBox)itemView.findViewById(R.id.checkBox);
        }
    }
}
