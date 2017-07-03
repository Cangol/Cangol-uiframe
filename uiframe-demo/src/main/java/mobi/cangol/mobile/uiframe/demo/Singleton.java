package mobi.cangol.mobile.uiframe.demo;

/**
 * Created by xuewu.wei on 2017/7/3.
 */

public class Singleton {
    private static final Singleton ourInstance = new Singleton();

    public  static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }
    private OnTestListener onListener;

    public void setOnTestListener(OnTestListener onListener) {
        this.onListener = onListener;
    }
    public void notifyTest(){
        if(onListener!=null)
            onListener.onTest();
    }
    public interface OnTestListener{
        void onTest();
    }
}
