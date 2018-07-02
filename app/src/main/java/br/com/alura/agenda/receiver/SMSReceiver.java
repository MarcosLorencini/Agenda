package br.com.alura.agenda.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

//responsável por receber os SMS
//quando chegar o sms chama-se o método
public class SMSReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        //context o android vai fornecer o contexto
        Toast.makeText(context,"Chegou um SMS!", Toast.LENGTH_SHORT).show();

    }
}
