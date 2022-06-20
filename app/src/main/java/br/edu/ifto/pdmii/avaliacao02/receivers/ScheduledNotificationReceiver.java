package br.edu.ifto.pdmii.avaliacao02.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.edu.ifto.pdmii.avaliacao02.TitleActivity;
import br.edu.ifto.pdmii.avaliacao02.notification.NotificationHandler;
import br.edu.ifto.pdmii.avaliacao02.util.RandomInt;

public class ScheduledNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(intent);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationIntent.setClass(context, TitleActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, RandomInt.between(1, 4096), notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Log.d(ScheduledNotificationReceiver.class.getSimpleName(), "onReceive: lançando notificação agendada");
        NotificationHandler.throwNotification(context, pendingIntent, "Temos que pegar!!! É hora da sua próxima batalha Pokémon!");
    }
}
