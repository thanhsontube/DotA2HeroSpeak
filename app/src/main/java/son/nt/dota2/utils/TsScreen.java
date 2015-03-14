package son.nt.dota2.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

public class TsScreen {
    public static Point getSizeScreen(Context context) {
        // get Width, height screen
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        Point outSize = new Point();
        display.getSize(outSize);
        return outSize;
    }
}
