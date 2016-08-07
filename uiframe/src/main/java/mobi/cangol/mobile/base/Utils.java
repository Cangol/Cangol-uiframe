package mobi.cangol.mobile.base;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import mobi.cangol.mobile.logging.Log;

public class Utils {
    //开发模式 显示log、生命周期等
    public static final boolean LIFECYCLE = true;

    public static String makeLogTag(Class<?> cls) {
        String tag = cls.getSimpleName();
        return (tag.length() > 50) ? tag.substring(0, 50) : tag;
    }

    public static void setLogTag(Object obj) {
        setLogTag(obj.getClass(), obj);
    }

    private static Field findField(Class clazz, String name) {
        if (clazz != Object.class) {
            try {
                Field field  = clazz.getDeclaredField(name);
                return field;
            } catch (NoSuchFieldException e) {
                return findField(clazz.getSuperclass(),name);
            }
        }
        return null;
    }

    public static void setLogTag(Class clazz, Object obj) {
        if (clazz != Object.class) {
            Field field = null;
            try {
                field = findField(clazz,"TAG");
                if(field!=null){
                    if(!Modifier.isPrivate(field.getModifiers())){
                        field.setAccessible(true);
                        field.set(obj, makeLogTag(clazz));
                    }else{
                        Log.e("field TAG is private!");
                    }
                }else{
                    Log.e("field TAG is not found");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
