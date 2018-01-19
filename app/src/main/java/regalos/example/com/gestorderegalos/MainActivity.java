package regalos.example.com.gestorderegalos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import regalos.example.com.gestorderegalos.Amigos.AmigosFragment;
import regalos.example.com.gestorderegalos.Eventos.EventosFragment;
import regalos.example.com.gestorderegalos.Notificaciones.NotificacionesFragment;
import regalos.example.com.gestorderegalos.Pagos.DineroFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EventosFragment.OnFragmentInteractionListener, AmigosFragment.OnFragmentInteractionListener, DineroFragment.OnFragmentInteractionListener, NotificacionesFragment.OnFragmentInteractionListener {

    private TextView usuario;
    public static String nombreUser;
    private ImageView imagenUser;
    private VolleyRP volleyRP;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        volleyRP = VolleyRP.getInstance(this);
        requestQueue = volleyRP.getRequestQueue();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Para que inicie con el fragment de Eventos
        EventosFragment fragment = new EventosFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contenedor, fragment).commit();
        getSupportActionBar().setTitle(getResources().getString(R.string.eventos));


        //Para coger el dato de la cuenta de usuario y la foto
        View headerView = navigationView.getHeaderView(0);
        usuario = (TextView) headerView.findViewById(R.id.userRegistradoTV);
        imagenUser = (ImageView) headerView.findViewById(R.id.imagenUser);


        final Bundle extra = getIntent().getExtras();
        nombreUser = extra.getString("Nombre");
        usuario.setText(nombreUser);

        //Picasso.with(this).load(extra.getString("imagen")).error(R.drawable.ic_account_circle_white).resize(72,61).centerCrop().into(imagenUser);

        cargarDatosUsuario();

        imagenUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarEditarUser();
            }
        });
    }

    private void cargarDatosUsuario() {
        JsonObjectRequest solicitud = new JsonObjectRequest(URL.LOGIN_GETDATOS+MainActivity.nombreUser, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    String codigo = response.getString("resultado");
                    if (codigo.equals("OK")){
                        //El usuario existe
                        JSONObject datos = response.getJSONObject("datos");
                        String foto = datos.getString("imagen");
                        Picasso.with(MainActivity.this).load(foto).error(R.drawable.ic_account_circle).resize(42, 41).centerCrop().into(imagenUser);
                    }else {
                        Toast.makeText(MainActivity.this, codigo, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });

        volleyRP.addToQueue(solicitud, requestQueue, MainActivity.this, volleyRP);
    }

    @Override
    public void onBackPressed(){
        //Que no pueda salir al pulsar en back
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    }

    public void salir() {
        //Solo se sale al pulsar en el botón de cerrar Sesión
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ajustes) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_eventos) {
            EventosFragment fragment = new EventosFragment();
            FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
            FT.replace(R.id.contenedor, fragment).commit();

        } else if (id == R.id.nav_amigos) {
            AmigosFragment fragment = new AmigosFragment();
            FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
            FT.replace(R.id.contenedor, fragment).commit();

        } else if (id == R.id.nav_dinero) {
            DineroFragment fragment = new DineroFragment();
            FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
            FT.replace(R.id.contenedor, fragment).commit();

        } else if (id == R.id.nav_notificaciones) {
            NotificacionesFragment fragment = new NotificacionesFragment();
            FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
            FT.replace(R.id.contenedor, fragment).commit();

        } else if (id == R.id.nav_cerrar_sesion) {
            salir();
        }

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void cargarEditarUser() {
        Intent i = new Intent(this, EditarUsuario.class);
        startActivity(i);
    }
}
