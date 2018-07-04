package br.com.alura.agenda.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.widget.Toast;

import br.com.alura.agenda.R;
import br.com.alura.agenda.dao.AlunoDAO;

//responsável por receber os SMS
//quando chegar o sms chama-se o método
public class SMSReceiver extends BroadcastReceiver {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        //verificar se é de um aluno
        //conseguir um sms messager
        //recupera as pdus(chaves): cada parte da msg é um pdu)
        Object[] pdus =(Object[]) intent.getSerializableExtra("pdus");
        byte[] pdu = (byte[]) pdus[0];//primeira pdu dentro do array de objetos
        String formato = (String) intent.getSerializableExtra("format");//pega o formato do SMS
        SmsMessage sms = SmsMessage.createFromPdu(pdu, formato);
        //pega o tel de quem está eviando o sms
        String telefone = sms.getDisplayOriginatingAddress();
        AlunoDAO dao = new AlunoDAO(context);
        if(dao.ehAluno(telefone)){
            //context o android vai fornecer o contexto
            Toast.makeText(context,"Chegou um SMS!", Toast.LENGTH_SHORT).show();
            //som no android quando receber a msg
            MediaPlayer mp = MediaPlayer.create(context, R.raw.msg);
            mp.start();
        }
        dao.close();

    }
}
