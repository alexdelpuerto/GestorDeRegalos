package regalos.example.com.gestorderegalos.Regalos;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.android.volley.RequestQueue;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import regalos.example.com.gestorderegalos.GotevUploadServiceReg;
import regalos.example.com.gestorderegalos.R;
import regalos.example.com.gestorderegalos.URL;
import regalos.example.com.gestorderegalos.VolleyRP;

public class NuevoRegalo extends AppCompatActivity {

    private EditText nombreReg, precioReg, descReg;
    private ImageButton camaraButton, galeriaButton;
    private GotevUploadServiceReg gotevUploadServiceReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_regalo);

        nombreReg = (EditText)findViewById(R.id.nombreAgregarRegET);
        precioReg = (EditText)findViewById(R.id.precAgregarRegET);
        descReg = (EditText)findViewById(R.id.descAgregarRegET);
        camaraButton = (ImageButton)findViewById(R.id.camaraRegButton);
        galeriaButton = (ImageButton)findViewById(R.id.galeriaRegButton);

        final String id_evento = getIntent().getExtras().getString("id_evento");

        gotevUploadServiceReg = new GotevUploadServiceReg(this, URL.SUBIR_REGALOS) {
             @Override
             public void onProgress(UploadInfo uploadInfo) {

             }

             @Override
             public void onError(UploadInfo uploadInfo, Exception exception) {

             }

             @Override
             public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {

             }

             @Override
             public void onCancelled(UploadInfo uploadInfo) {

             }
         };
        gotevUploadServiceReg.setParameterNamePhoto("file");
        //no eliminar la foto temporal al momento de subirla
        gotevUploadServiceReg.setEliminarFoto(false);

        galeriaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotevUploadServiceReg.actualizarParametros(nombreReg.getText().toString(), descReg.getText().toString(), precioReg.getText().toString(), id_evento);
                gotevUploadServiceReg.subirFotoGaleria();
            }
        });

        camaraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotevUploadServiceReg.actualizarParametros(nombreReg.getText().toString(), descReg.getText().toString(), precioReg.getText().toString(), id_evento);
                gotevUploadServiceReg.subirFotoCamara();
            }
        });
        GotevUploadServiceReg.verifyStoragePermissions(this);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri u = gotevUploadServiceReg.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
