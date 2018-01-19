package regalos.example.com.gestorderegalos.Eventos;

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
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventosFragment extends Fragment {

    private RecyclerView recyclerEventos;
    private List<Evento> listaEventos;
    private EventoAdapter adapter;
    private FloatingActionButton aniadirEvento, actualizar;

    private static final int COD_EVENTO = 1;

    private VolleyRP volleyRP, volleyRP2, volleyRP3, volleyRP4;
    private RequestQueue requestQueue, requestQueue2, requestQueue3, requestQueue4;
    public static String evento;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EventosFragment() {
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
    public static EventosFragment newInstance(String param1, String param2) {
        EventosFragment fragment = new EventosFragment();
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
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_eventos, container, false);
        aniadirEvento = (FloatingActionButton) view.findViewById(R.id.aniadirEvento);
        actualizar = (FloatingActionButton) view.findViewById(R.id.actualizar);

        volleyRP = VolleyRP.getInstance(getContext());
        requestQueue = volleyRP.getRequestQueue();

        volleyRP2 = VolleyRP.getInstance(getContext());
        requestQueue2 = volleyRP2.getRequestQueue();


        aniadirEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarNuevoEvento(view);
            }
        });

        cargarEventos();

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarEventos();
            }
        });

        listaEventos = new ArrayList<>();

        recyclerEventos = (RecyclerView) view.findViewById(R.id.recyclerViewEvento);
        recyclerEventos.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerEventos.setLayoutManager(lm);

        adapter = new EventoAdapter(getContext(), listaEventos);
        recyclerEventos.setAdapter(adapter);

        return view;
    }

    public void lanzarNuevoEvento(View v) {
        Intent i = new Intent(v.getContext(), NuevoEvento.class);
        startActivityForResult(i, COD_EVENTO);
    }

    public void onActivityResult(int resultado, int codigo, final Intent data){
        if(resultado==COD_EVENTO && codigo==RESULT_OK){
            String nombre = data.getExtras().getString("NombreEvento");
            String presupuesto = data.getExtras().getString("Presupuesto");

            crearEvento(nombre, presupuesto);
        }
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

    public void crearEvento(String nombre, String presupuesto){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("nombre", nombre);
        hashMap.put("presupuesto", presupuesto);
        hashMap.put("creador", MainActivity.nombreUser);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL.CREAR_EVENTOS, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equalsIgnoreCase("Evento creado correctamente")) {
                        Toast.makeText(getContext(), estado, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), estado, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "No se pudo crear el evento", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se pudo crear el evento", Toast.LENGTH_SHORT).show();
            }
        });

        volleyRP.addToQueue(solicitud, requestQueue, getContext(), volleyRP);
    }

    public void cargarEventos(){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL.EVENTOS_GET + MainActivity.nombreUser, null, new Response.Listener<JSONObject>() {
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
        volleyRP2.addToQueue(solicitud, requestQueue2, getContext(), volleyRP2);
    }

    public void cargarRecycler(JSONArray jsonArray){
        listaEventos.clear();
        for(int i=0; i<jsonArray.length(); i++){
            Evento eventoAux = new Evento();
            try {
                eventoAux.setId(jsonArray.getJSONObject(i).getString("id_evento"));
                eventoAux.setNombreEvento(jsonArray.getJSONObject(i).getString("nombre"));
                eventoAux.setDineroFalta("Presupuesto: "+jsonArray.getJSONObject(i).getString("presupuesto")+"€");
                eventoAux.setCreador(jsonArray.getJSONObject(i).getString("creador"));
                eventoAux.setMaxProgres(jsonArray.getJSONObject(i).getInt("Num_regalos"));
                eventoAux.setMinProgres(jsonArray.getJSONObject(i).getInt("Regalos_comp"));
                if(eventoAux.getCreador().equals(MainActivity.nombreUser)){
                    eventoAux.setPrivilegios(true);
                }

                listaEventos.add(eventoAux);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        EventoAdapter adaptador = new EventoAdapter(getContext(), listaEventos);
        recyclerEventos.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
    }
}
