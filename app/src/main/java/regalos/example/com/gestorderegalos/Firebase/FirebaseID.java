package regalos.example.com.gestorderegalos.Firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Alejandro on 15/11/2017.
 */

public class FirebaseID extends FirebaseInstanceIdService {
    @Override
    //Metodo que se encarga de generar un token para el dispositivo
    public void onTokenRefresh() {
        super.onTokenRefresh();
    }
}
