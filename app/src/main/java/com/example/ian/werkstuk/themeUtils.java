package com.example.ian.werkstuk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

/**
 * Created by ian on 28/12/2017.
 */

public class themeUtils {
    private static int cTheme;
    public final static int GREY = 0;
    public final static int BLACK = 1;
    public final static int ORANGE = 2;

    public static void changeToTheme(Activity activity, int theme) {
        cTheme = theme;
        activity.finish();
        onActivityCreateSetTheme(activity);
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (cTheme)

        {

            default:

            case GREY:
                activity.setTheme(R.style.AppTheme);
                break;
            case BLACK:
                activity.setTheme(R.style.BlackTheme);
                break;
            case ORANGE:
                activity.setTheme(R.style.OrangeTheme);
                break;
        }

    }
}
