package mobi.cangol.mobile.uiframe.demo.utils;

public class Constants {
	public static final boolean LOG_FORMAT=false;
	public static final int LOG_LEVEL=android.util.Log.VERBOSE;
	public static final boolean LIFECYCLE=true;
	public static final int DATABASE_VERSION=1;
	public static final String DATABASE_NAME="Mobile";
	public static final String SHARED="Mobile";
	public static final String TAG="Mobile_";
	public static final String APP_DIR="/Mobile";
	public static final String APP_TEMP=APP_DIR+"/temp";
	public static final String APP_IMAGE=APP_DIR+"/image";
	public static final String APP_DB="Mobile.db";
	
	
	public static String makeLogTag(Class<?> cls) {
		String tag = TAG + cls.getSimpleName();
		return (tag.length() > 50) ? tag.substring(0, 50) : tag;
	}
	
}
