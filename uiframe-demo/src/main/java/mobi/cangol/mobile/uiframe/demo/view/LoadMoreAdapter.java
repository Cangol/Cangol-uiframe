package mobi.cangol.mobile.uiframe.demo.view;


import java.util.List;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
/**
 * @Description:
 * @version $Revision: 1.0 $
 * @author Cangol
 */
public class LoadMoreAdapter<T> extends BaseAdapterDecorator<T> {
	private OnLoadCallback mOnLoadCallback;
	private View mFooterView;
	private ListView mListView;
	private ArrayAdapter<T> baseAdapter;
	public LoadMoreAdapter(ArrayAdapter<T> baseAdapter,View footerView) {
		super(baseAdapter);
		this.baseAdapter=baseAdapter;
		this.mFooterView=footerView;
	}
	@Override
	public void setAbsListView(AbsListView listView) {
		super.setAbsListView(listView);
		listView.setOnScrollListener(makeScrollListener());
		mListView=(ListView) listView;
		mListView.addFooterView(mFooterView);
	}
	public AbsListView.OnScrollListener makeScrollListener() {
        return new AbsListView.OnScrollListener() {
        	@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
        		if(mOnLoadCallback==null)return;
        		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE&&
						view.getLastVisiblePosition()==(view.getCount()-1)) {
        			if(mOnLoadCallback.hasNext(view.getCount())){
        				showLoading();
        				mOnLoadCallback.loadMoreData();
        			}
        		}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
        };
    }
	private void showLoading(){
		if(mListView.getFooterViewsCount()==0){
			mListView.addFooterView(mFooterView);
		}
	}
	private void hideLoading(){
		if(mListView.getFooterViewsCount()>0){
			mListView.removeFooterView(mFooterView);
		}
	}
	public void addMoreData(List<T> list){
		baseAdapter.addAll(list);
		hideLoading();
	}
	public void addMoreComplete(){
		hideLoading();
	}
	public boolean  hasNext(int count){
		return mOnLoadCallback.hasNext(count);
	}
	public void loadMoreData(){
		showLoading();
		mOnLoadCallback.loadMoreData();
	}
	public interface OnLoadCallback{
		boolean hasNext(int count);
		void loadMoreData();
	}
	public OnLoadCallback OnLoadCallback() {
		return mOnLoadCallback;
	}
	public void setOnLoadCallback(OnLoadCallback mOnLoadCallback) {
		this.mOnLoadCallback = mOnLoadCallback;
		hideLoading();
	}
	
}
