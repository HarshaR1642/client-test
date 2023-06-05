package com.service.keylessrn.utility;

import android.content.Context;

public class Utility {

    public static int pxToDp(final Context context, final float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int dpToPx(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
