package br.edu.opet.pi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class Receiver extends BroadcastReceiver{


        AlertDialog.Builder alertBuilder;
        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra("level",0);

            //ProgressBar progressBar = (ProgressBar)findViewById(R.id.pb);
           // progressBar.setProgress(level);

            //TextView tv = (TextView)findViewById(R.id.tv);
            //tv.setText("Battery Level: " + Integer.toString(level) + "%");

            if (level<25){
                Toast.makeText(context, "Bateria baixa!", Toast.LENGTH_SHORT).show();

                alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setTitle("Bateria baixa");
                alertBuilder.setMessage("Nivel de bateria muito baixo, por favor carregue o seu aparelho!");
                alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
            }
        }


}
