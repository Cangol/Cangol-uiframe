package mobi.cangol.mobile.uiframe.demo;

/**
 * Created by xuewu.wei on 2017/7/3.
 */

public class LeakSingleton {
    private static final LeakSingleton ourInstance = new LeakSingleton();

    public  static LeakSingleton getInstance() {
        return ourInstance;
    }

    private LeakSingleton() {
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
