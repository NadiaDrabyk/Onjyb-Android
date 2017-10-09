package com.onjyb.reqreshelper;

import android.util.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSonParser {
    private static final String tag = "JSonParser";

    public static Object parse(Class objClass, JSONObject jObject) {
        Object obj = null;
        try {
            obj = objClass.newInstance();
        } catch (IllegalAccessException e) {
            Log.e(tag, "IllegalAccessException: " + e);
        } catch (InstantiationException e2) {
            Log.e(tag, "InstantiationException: " + e2);
        }
        for (Field field : objClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (isWrapper(field.getType())) {
                try {
                    field.setAccessible(true);
                    if (field.getType() == Integer.TYPE) {
                        field.set(obj, Integer.valueOf(jObject.getInt(field.getName())));
                    } else if (field.getType() == Float.TYPE) {
                        field.set(obj, Long.valueOf(jObject.getLong(field.getName())));
                    } else if (field.getType() == Long.TYPE) {
                        field.set(obj, Long.valueOf(jObject.getLong(field.getName())));
                    } else {
                        field.set(obj, jObject.getString(field.getName()));
                    }
                } catch (Exception e3) {
                }
            } else if (field.getType().isArray()) {
                try {
                    JSONArray jArray = jObject.getJSONArray(field.getName());
                    Object objArray = Array.newInstance(field.getType().getComponentType(), jArray.length());
                    for (int i = 0; i < jArray.length(); i++) {
                        Object objElem = field.getType().getComponentType().newInstance();
                        if (isWrapper(field.getType().getComponentType())) {
                            Object objElement = jArray.get(i);
                            if (objElement != null) {
                                if (field.getType().getComponentType() == Integer.TYPE) {
                                    objElem = Integer.valueOf(Integer.parseInt(objElement.toString()));
                                } else if (field.getType().getComponentType() == Float.TYPE) {
                                    objElem = Float.valueOf(Float.parseFloat(objElement.toString()));
                                } else if (field.getType().getComponentType() == Long.TYPE) {
                                    objElem = Long.valueOf(Long.parseLong(objElement.toString()));
                                } else {
                                    objElem = objElement.toString();
                                }
                            }
                        } else {
                            objElem = parse(field.getType().getComponentType(), jArray.getJSONObject(i));
                        }
                        Array.set(objArray, i, objElem);
                    }
                    field.set(obj, objArray);
                } catch (Exception e4) {
                    Log.e(tag, "Exception Array: " + e4);
                }
            } else {
                try {
                    Object objSub = field.getType().newInstance();
                    field.set(obj, parse(field.getType(), jObject.getJSONObject(field.getName())));
                } catch (Exception e42) {
                    Log.e(tag, "else Exception: " + e42);
                }
            }
        }
        return obj;
    }

    public static void showFields(Class objClass) {
        for (Field field : objClass.getDeclaredFields()) {
            if (!isWrapper(field.getType())) {
                if (field.getType().isArray()) {
                    showFields(field.getType().getComponentType());
                } else {
                    showFields(field.getType());
                }
            }
        }
    }

    public static boolean isWrapper(Class objClass) {
        if (objClass == Boolean.TYPE || objClass == Integer.TYPE || objClass == String.class || objClass == Double.TYPE || objClass == Float.TYPE || objClass == Long.TYPE) {
            return true;
        }
        return false;
    }
}
