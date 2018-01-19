package regalos.example.com.gestorderegalos.Eventos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import regalos.example.com.gestorderegalos.R;

public class NuevoEvento extends AppCompatActivity {

    private EditText nombreEventoET, presupuestoET;
    private Button newEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_evento);

        nombreEventoET = (EditText) findViewById(R.id.newEventoET);
        presupuestoET = (EditText) findViewById(R.id.presupuestoET);
        newEventButton = (Button) findViewById(R.id.newEventoButton);

        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearEvento(nombreEventoET.getText().toString(), presupuestoET.getText().toString());
            }
        });

    }

    public void crearEvento(String nombre, String presupuesto){

        presupuesto = presupuesto.replaceFirst ("^0*", "");
        Intent i = new Intent();
        i.putExtra("NombreEvento", nombre);
        i.putExtra("Presupuesto", presupuesto);
        setResult(RESULT_OK, i);
        finish();
    }
}
