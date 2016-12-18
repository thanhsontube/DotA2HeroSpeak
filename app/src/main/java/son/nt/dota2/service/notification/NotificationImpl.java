package son.nt.dota2.service.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import son.nt.dota2.R;
import son.nt.dota2.activity.HomeActivity;
import son.nt.dota2.dto.heroSound.ISound;


/**
 * Created by sonnt on 7/28/16.
 */
public class NotificationImpl implements INotification {

    public static final int NOTI_ID = 22;
    private static final String TAG = NotificationImpl.class.getSimpleName();

    private static final int REQUEST_CODE = 100;

    public static final String ACTION_PLAY = "com.example.android.sonnt.play";
    public static final String ACTION_PREV = "com.example.android.sonnt.prev";
    public static final String ACTION_NEXT = "com.example.android.sonnt.next";
    public static final String ACTION_CLOSE = "com.example.android.sonnt.close";

    ISound audioFile;
    Context context;
    Service service;

    Notification builder;
    RemoteViews remoteView;

    public NotificationImpl(Service mService, Context context) {
        this.context = context;
        this.service = mService;

        String pkg = mService.getPackageName();
        PendingIntent mPlayIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_PLAY).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent mPreviousIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_PREV).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent mNextIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_NEXT).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent mCloseIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_CLOSE).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);

        remoteView = new RemoteViews(context.getPackageName(), R.layout.notification_view);

        remoteView.setOnClickPendingIntent(R.id.noti_toggle, mPlayIntent);
        remoteView.setOnClickPendingIntent(R.id.noti_next, mNextIntent);
        remoteView.setOnClickPendingIntent(R.id.noti_prev, mPreviousIntent);
        remoteView.setOnClickPendingIntent(R.id.noti_close, mCloseIntent);
    }

    @Override
    public void setData(ISound currentItem) {
        this.audioFile = currentItem;

        //prepare notification
        createNotification(currentItem);

    }

    @Override
    public void doPlay() {
        if (builder == null) {
            createNotification(audioFile);
        }
        //        remoteView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
        builder.contentView.setImageViewResource(R.id.noti_img_toggle, R.drawable.icon_paused);

        service.startForeground(NOTI_ID, builder);

    }

    @Override
    public void doPause() {
        if (builder == null) {
            createNotification(audioFile);
        }
        builder.contentView.setImageViewResource(R.id.noti_img_toggle, R.drawable.icon_played);

        service.startForeground(NOTI_ID, builder);

    }

    @Override
    public void doDetach() {
        if (service != null) {

            service.stopForeground(true);
        }
    }

    private void createNotification(ISound audioFile) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteView.setTextViewText(R.id.noti_title, audioFile.getGroup());

        builder = new NotificationCompat.Builder(context) //
                .setContentText(audioFile.getTitle())//
                .setOngoing(false)//
                .setContentIntent(pi)//
                .setTicker(audioFile.getTitle())//
                .setContentTitle(audioFile.getTitle())//
                .setSmallIcon(R.drawable.ic_launcher)//
                .setContent(remoteView)//
                .build();
    }
}
