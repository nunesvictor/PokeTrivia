package br.edu.ifto.pdmii.avaliacao02.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import br.edu.ifto.pdmii.avaliacao02.R;
import br.edu.ifto.pdmii.avaliacao02.receivers.ScheduledNotificationReceiver;
import br.edu.ifto.pdmii.avaliacao02.util.RandomInt;

public class NotificationHandler {
    private static final String CHANNEL_ID = UUID.randomUUID().toString();

    public static void createNotificationChannel(Context context) {
        CharSequence channelName = "PokeTrivia";
        String channelDescription = "PokeTrivia Notification Channel";
        int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, channelImportance);
        channel.setDescription(channelDescription);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public static void throwNotification(Context context, PendingIntent pendingIntent, String text) {
        int notificationId = ThreadLocalRandom.current().nextInt(1, 4096 + 1);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_pokeball_dark)
                .setContentTitle("Pokémon Trivia")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(notificationId, builder.build());
    }

    public static void scheduleNotifications(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ScheduledNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, RandomInt.between(1, 4096), intent, PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, LocalDateTime.now().getHour());
        calendar.set(Calendar.MINUTE, LocalDateTime.now().getMinute() + 1);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HALF_DAY,
                pendingIntent);

        Log.d(NotificationHandler.class.getSimpleName(),
                "createScheduledNotification: " +
                        String.format(Locale.getDefault(), "Alarme criado para %s",
                                calendar.getTime()));
    }
}
