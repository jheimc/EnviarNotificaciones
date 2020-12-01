package com.example.enviarnotificaciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button allUsers= findViewById(R.id.btnSOS);

        FirebaseMessaging.getInstance().subscribeToTopic("enviaratodos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this,"registrado para recibir notificaciones",Toast.LENGTH_SHORT).show();
            }
        });

        allUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarAtodos();
            }
        });
    }

    private void llamarAtodos() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("enviaratodos");
        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();
        try {
            //String token="";
            String latitud= "-0151515";
            String longitud= "182541";
            json.put("to","/topics/"+"enviaratodos");
            JSONObject notificacion = new JSONObject();
            notificacion.put("titulo","SOS");
            notificacion.put("detalle","ayudame estoy en peligro! mi ubicacion es "+latitud +" y "+longitud);
            json.put("data",notificacion);

            String URL= "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String,String> header= new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAM3bYKkc:APA91bHvRbkmVGHV7szrN9GcxtvTx4WG1SEip9696n0tUn3h-8fHxqsvVxqXQpl2moRqj47oarfe0BD4xx9h9W4i4FcjupTF3Ctp6jJud0T45tD_WwWyzfL0zB1P-TMok-g0QT7xrX0c");

                    return header;
                }
            };
            myrequest.add(request);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}