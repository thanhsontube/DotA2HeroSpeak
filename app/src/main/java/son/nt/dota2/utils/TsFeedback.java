package son.nt.dota2.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.IntentCompat;
import android.text.TextUtils;

public class TsFeedback {
    /**
     * send a email, using Gmail application;
     *
     * @param context
     * @param receiver
     * @param subject
     */
    public static void sendEmailbyGmail(Context context, String receiver, String subject, String body) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("plain/text");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sendIntent.setData(Uri.parse(receiver));
        sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{receiver});
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (!TextUtils.isEmpty(body)) {
            sendIntent.putExtra(Intent.EXTRA_TEXT, body);
        }
        context.startActivity(sendIntent);
    }

    public static void sendEmail(Context context, String receiver, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{receiver});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        try {
            context.startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }

    public static void rating(Context context) {
        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
        );
        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
        context.startActivity(i);
    }

}
