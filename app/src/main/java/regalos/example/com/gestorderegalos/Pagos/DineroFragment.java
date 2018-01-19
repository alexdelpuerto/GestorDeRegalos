package regalos.example.com.gestorderegalos.Pagos;

import android.content.Context;
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
 * {@link DineroFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DineroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DineroFragment extends Fragment {

    private RecyclerView recyclerDeben;
    private RecyclerView recyclerDebes;
    private List<Dinero> listaDeben;
    private List<Dinero> listaDebes;
    private DineroAdapter adapter;
    private DineroAdapter adapter2;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;
    private FloatingActionButton actualizarPagos;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DineroFragment() {
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
    public static DineroFragment newInstance(String param1, String param2) {
        DineroFragment fragment = new DineroFragment();
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
        View vista = inflater.inflate(R.layout.fragment_cuentas, container, false);

        actualizarPagos = (FloatingActionButton)vista.findViewById(R.id.actualizarPagos);
        actualizarPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDebes();
                cargarDeben();
            }
        });

        listaDeben = new ArrayList<>();

        recyclerDeben = (RecyclerView) vista.findViewById(R.id.recyclerViewDeben);
        recyclerDeben.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager lin1 = new LinearLayoutManager(getContext());
        recyclerDeben.setLayoutManager(lin1);


        listaDebes = new ArrayList<>();

        recyclerDebes = (RecyclerView) vista.findViewById(R.id.recyclerViewDebes);
        recyclerDebes.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager lin2 = new LinearLayoutManager(getContext());
        recyclerDebes.setLayoutManager(lin2);

        volleyRP = VolleyRP.getInstance(getContext());
        requestQueue = volleyRP.getRequestQueue();

        cargarDebes();
        cargarDeben();

        adapter = new DineroAdapter(getContext(),listaDeben, 2);
        recyclerDeben.setAdapter(adapter);
        adapter2 = new DineroAdapter(getContext(),listaDebes, 1);
        recyclerDebes.setAdapter(adapter2);
        return vista;
    }

    private void cargarDebes() {
        JsonObjectRequest solicitud = new JsonObjectRequest(URL.PAGOS_GET_DEBES + MainActivity.nombreUser, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject datos) {
            try{
                String codigo = datos.getString("resultado");
                if(codigo.equals("OK")){
                    JSONArray jsonArray = new JSONArray(datos.getString("datos"));
                    cargarRecycler(jsonArray, 1);
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

    private void cargarDeben() {
        JsonObjectRequest solicitud = new JsonObjectRequest(URL.PAGOS_GET_DEBEN + MainActivity.nombreUser, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try{
                    String codigo = datos.getString("resultado");
                    if(codigo.equals("OK")){
                        JSONArray jsonArray = new JSONArray(datos.getString("datos"));
                        cargarRecycler(jsonArray, 2);
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

    private void cargarRecycler(JSONArray jsonArray, int tipo) {
        if (tipo==1){
            listaDebes.clear();
        } else {
            listaDeben.clear();
        }

        for(int i=0; i<jsonArray.length(); i++){
            Dinero dineroAux = new Dinero();
            try {
                dineroAux.setNombreRegalo(jsonArray.getJSONObject(i).getString("Nom_regalo"));
                dineroAux.setPrecio(jsonArray.getJSONObject(i).getDouble("precio"));
                dineroAux.setNumUsuarios(jsonArray.getJSONObject(i).getInt("Num_usuarios"));

                if(tipo==1){
                    dineroAux.setUsuario(jsonArray.getJSONObject(i).getString("comprador"));
                    listaDebes.add(dineroAux);
                } else{
                    dineroAux.setUsuario(jsonArray.getJSONObject(i).getString("usuario"));
                    dineroAux.setId_regalo(jsonArray.getJSONObject(i).getInt("id_regalo"));
                    listaDeben.add(dineroAux);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (tipo ==1){
            DineroAdapter adaptador = new DineroAdapter(getContext(), listaDebes, 1);
            recyclerDebes.setAdapter(adaptador);
            adaptador.notifyDataSetChanged();
        } else {
            DineroAdapter adaptador2 = new DineroAdapter(getContext(), listaDeben, 2);
            recyclerDeben.setAdapter(adaptador2);
            adaptador2.notifyDataSetChanged();
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
}