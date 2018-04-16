package com.inscode.shadowpreviewer;

import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Map;

public class Util {

    /**
     * Get the system fonts list.
     *
     * @return A list of font pairs. <font name, font typeface>
     */
    public static Map<String, Typeface> getSSystemFontMap() {
        Map<String, Typeface> sSystemFontMap = null;
        try {
            Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
            Field f = Typeface.class.getDeclaredField("sSystemFontMap");
            f.setAccessible(true);
            sSystemFontMap = (Map<String, Typeface>) f.get(typeface);
        } catch (Exception e) {
            Log.e(MainActivity.TAG,
                    "Failed to get the system fonts. Error message: " + e.getMessage());
        }
        return sSystemFontMap;
    }

}
