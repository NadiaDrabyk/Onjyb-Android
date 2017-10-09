package com.onjyb.customview;

import android.content.Context;
import android.graphics.Typeface;
import java.util.Hashtable;

public class Typefaces {
    private static final Hashtable<String, Typeface> cache = new Hashtable();

    public static Typeface get(Context c, String name) {
        Typeface typeface;
        synchronized (cache) {
            if (!cache.containsKey(name)) {
                cache.put(name, Typeface.createFromAsset(c.getAssets(), String.format("fonts/%s", new Object[]{name})));
            }
            typeface = (Typeface) cache.get(name);
        }
        return typeface;
    }
}
