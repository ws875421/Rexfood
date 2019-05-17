package div.rex.seekfood.main;

import android.content.Context;


public class Holder {
    private static Context applicationContext;

    public static void initial(Context context) {
        applicationContext = context;
    }

    public static Context getContext() {
        return applicationContext;
    }


}

