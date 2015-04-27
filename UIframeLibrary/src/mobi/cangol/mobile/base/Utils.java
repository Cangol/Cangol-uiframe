package mobi.cangol.mobile.base;

public class Utils {
	//开发模式 显示log、生命周期等
	public static final boolean LIFECYCLE = true;
	public static String makeLogTag(Class<?> cls) {
		String tag = cls.getSimpleName();
		return (tag.length() > 50) ? tag.substring(0, 50) : tag;
	}
}
