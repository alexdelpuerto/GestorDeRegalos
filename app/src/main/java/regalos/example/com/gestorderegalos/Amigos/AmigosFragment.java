package regalos.example.com.gestorderegalos.Amigos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AmigosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AmigosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AmigosFragment extends Fragment {

    private RecyclerView recycler_amigos;
    private List<Amigo> listaAmigos;
    private AmigoAdapter adapter;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;
    private FloatingActionButton aniadirAmigo, actualizarAmg;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AmigosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AmigosFragment newInstance(String param1, String param2) {
        AmigosFragment fragment = new AmigosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_amigos, container, false);

        actualizarAmg = (FloatingActionButton)vista.findViewById(R.id.actualizarAmg);
        aniadirAmigo = (FloatingActionButton)vista.findViewById(R.id.aniadirAmigo);

        listaAmigos = new ArrayList<>();

        volleyRP = VolleyRP.getInstance(getContext());
        requestQueue = volleyRP.getRequestQueue();

        recycler_amigos = (RecyclerView) vista.findViewById(R.id.recyclerViewAmigo);
        recycler_amigos.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recycler_amigos.setLayoutManager(lm);


        aniadirAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarNuevoAmigo(v);
            }
        });
        actualizarAmg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarAmigos();
            }
        });

        cargarAmigos();

        adapter = new AmigoAdapter(getContext(), listaAmigos);

        recycler_amigos.setAdapter(adapter);

        return vista;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void cargarAmigos(){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL.AMIGOS_GET + MainActivity.nombreUser, null, new Response.Listener<JSONObject>() {
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
                Toast.makeText(getContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });
        volleyRP.addToQueue(solicitud, requestQueue, getContext(), volleyRP);
    }

    public void cargarRecycler(JSONArray jsonArray){
        listaAmigos.clear();
        for(int i=0; i<jsonArray.length(); i++){
            Amigo amigoAux = new Amigo();
            try {
                amigoAux.setNick(jsonArray.getJSONObject(i).getString("id_usuario"));
                amigoAux.setNombreComp(jsonArray.getJSONObject(i).getString("nombre")+" "+jsonArray.getJSONObject(i).getString("apellidos"));
                amigoAux.setFotoPerfil(jsonArray.getJSONObject(i).getString("imagen"));

                listaAmigos.add(amigoAux);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        AmigoAdapter adaptador = new AmigoAdapter(getContext(), listaAmigos);
        recycler_amigos.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();

    }

    private void lanzarNuevoAmigo(View v) {
        Intent i = new Intent(v.getContext(), NuevoAmigo.class);
        startActivity(i);
    }
}
