package regalos.example.com.gestorderegalos.Notificaciones;

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
import android.widget.TextView;
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
 * {@link NotificacionesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificacionesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificacionesFragment extends Fragment {

    private RecyclerView recyclerNotificaciones;
    private List<Notificaciones> listaNotificaciones;
    private NotificacionesAdapter adapter;
    private TextView noSolicitudes;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;
    private FloatingActionButton actualizarSolicitud;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NotificacionesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificacionesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificacionesFragment newInstance(String param1, String param2) {
        NotificacionesFragment fragment = new NotificacionesFragment();
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
        View vista = inflater.inflate(R.layout.fragment_notificaciones, container, false);

        noSolicitudes = (TextView)vista.findViewById(R.id.noSolicitudesTV);
        actualizarSolicitud = (FloatingActionButton) vista.findViewById(R.id.actualizarSolicitud);

        listaNotificaciones = new ArrayList<>();

        volleyRP = VolleyRP.getInstance(getContext());
        requestQueue = volleyRP.getRequestQueue();

        recyclerNotificaciones = (RecyclerView)vista.findViewById(R.id.recyclerViewNotify);
        recyclerNotificaciones.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerNotificaciones.setLayoutManager(lm);

        cargarSolicitudes();

        actualizarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarSolicitudes();
            }
        });

        adapter = new NotificacionesAdapter(getContext(), listaNotificaciones);
        recyclerNotificaciones.setAdapter(adapter);

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

    public void cargarSolicitudes(){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL.NOTIFICACIONES_GET + MainActivity.nombreUser, null, new Response.Listener<JSONObject>() {
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
        listaNotificaciones.clear();
        for(int i=0; i<jsonArray.length(); i++){
            Notificaciones notifyAux = new Notificaciones();
            try {
                notifyAux.setNotificacion(jsonArray.getJSONObject(i).getString("amigo1"));
                notifyAux.setFotoPerfil(jsonArray.getJSONObject(i).getString("imagen"));

                listaNotificaciones.add(notifyAux);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        NotificacionesAdapter adaptador = new NotificacionesAdapter(getContext(), listaNotificaciones);
        recyclerNotificaciones.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
    }
}
