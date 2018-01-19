package regalos.example.com.gestorderegalos;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import android.Manifest;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import static android.provider.CalendarContract.CalendarCache.URI;

/**
 * Created by Alejandro on 22/12/2017.
 */

public abstract class GotevUploadService {
    public abstract void onProgress(UploadInfo uploadInfo);
    public abstract void onError(UploadInfo uploadInfo, Exception exception);
    public abstract void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse);
    public abstract void onCancelled(UploadInfo uploadInfo);

    // Permisos
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int GALERIA = 50;
    private static final int CAMERA = 100;
    private static String APP_DIRECTORY = "GestorDeRegalos/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "Fotos";
    private boolean eliminarFoto = true;
    private String pathCamera = "";
    private String URL = "";

    private Activity activity;
    private String parameterNamePhoto;

    public GotevUploadService(Activity activity,String URL) {
        this.activity=activity;
        this.URL = URL;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public String getParameterNamePhoto() {
        return parameterNamePhoto;
    }

    public void setParameterNamePhoto(String parameterNamePhoto) {
        this.parameterNamePhoto = parameterNamePhoto;
    }

    public boolean isEliminarFoto() {
        return eliminarFoto;
    }

    public void setEliminarFoto(boolean eliminarFoto) {
        this.eliminarFoto = eliminarFoto;
    }

    public void subirFotoGaleria(){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/jpeg");
        //i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        activity.startActivityForResult(Intent.createChooser(i,"Selecciona una foto"), GALERIA);
    }

    public void subirFotoCamara(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        pathCamera = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator +"temp.jpg";
        File newFile = new File(pathCamera);
        File directorio = new File(Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator);
        directorio.mkdirs();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        activity.startActivityForResult(intent, CAMERA);
    }

    public void obtenerRuta(){

    }

    //Se ejecuta despues de elegir una foto
    public Uri onActivityResult(int requestCode, int resultCode, Intent data){
        Uri fotoTemp = null;
        try {
            String uploadId = UUID.randomUUID().toString();
            MultipartUploadRequest request = new MultipartUploadRequest(activity,uploadId, URL);
            if(requestCode == GALERIA && resultCode == Activity.RESULT_OK){
                Uri u = data.getData();
                fotoTemp = u;
                request.addFileToUpload(getPath(u), getParameterNamePhoto());
            }else if(requestCode == CAMERA && resultCode == Activity.RESULT_OK){
                fotoTemp = URI.parse(pathCamera);
                request.addFileToUpload(pathCamera,getParameterNamePhoto());
                request.setAutoDeleteFilesAfterSuccessfulUpload(eliminarFoto);
            }
            request.addParameter("id", MainActivity.nombreUser);
            request.setNotificationConfig(new UploadNotificationConfig());
            request.setMaxRetries(2);

            request.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(UploadInfo uploadInfo) {
                    GotevUploadService.this.onProgress(uploadInfo);
                }
                @Override
                public void onError(UploadInfo uploadInfo, Exception exception) {
                    GotevUploadService.this.onError(uploadInfo, exception);
                }
                @Override
                public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                    GotevUploadService.this.onCompleted(uploadInfo,serverResponse);
                }
                @Override
                public void onCancelled(UploadInfo uploadInfo) {
                    GotevUploadService.this.onCancelled(uploadInfo);
                }
            });
            request.startUpload();
        } catch (Exception exc) {
            Toast.makeText(activity,"Error: "+exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return fotoTemp;
    }

    //Metodo para obteners la ruta de una imagen
    public String getPath(Uri uri) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = activity.getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        assert cursor != null;
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    //Verificar los permisos de nuestra aplicacion para leer y escribir
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
