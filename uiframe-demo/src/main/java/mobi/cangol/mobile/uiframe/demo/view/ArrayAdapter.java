package mobi.cangol.mobile.uiframe.demo.view;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * A true ArrayList adapter providing access to all ArrayList methods.
 */
public abstract class ArrayAdapter<T> extends BaseAdapter {
	protected LayoutInflater mInflater;
	protected Context mContext;
	private List<T> mItems;
	public static final int TYPE_SELECT = 1;
	public static final int TYPE_UNSELECT = 0;
	protected boolean isEditMode;
	public ArrayAdapter(Context context) {
		this(context,null);
	}
	/**
	 * Creates a new ArrayAdapter with the specified list, or an empty list if
	 * items == null.
	 */
	public ArrayAdapter(Context context,List<T> items) {
		mContext=context;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mItems = new ArrayList<T>();
		if (items != null) {
			mItems.addAll(items);
		}
	}
	public boolean isEditMode() {
		return isEditMode;
	}
	public void setEditMode(boolean isEditMode) {
		if(this.isEditMode==isEditMode)return;
		this.isEditMode = isEditMode;
		this.clearSelected();
		notifyDataSetChanged();
	}
	public List<T> getItems() {
		return mItems;
	}
	public void setItems(List<T> mItems) {
		this.mItems = mItems;
	}
	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public T getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	/**
	 * Appends the specified element to the end of the list.
	 */
	public void add(T item) {
		mItems.add(item);
		notifyDataSetChanged();
	}

	/**
	 * Inserts the specified element at the specified position in the list.
	 */
	public void add(int position, T item) {
		mItems.add(position, item);
		notifyDataSetChanged();
	}

	/**
	 * Appends all of the elements in the specified collection to the end of the
	 * list, in the order that they are returned by the specified collection's
	 * Iterator.
	 */
	public void addAll(Collection<? extends T> items) {
		mItems.addAll(items);
		notifyDataSetChanged();
	}

	/**
	 * Appends all of the elements to the end of the list, in the order that
	 * they are specified.
	 */
	public void addAll(T... items) {
		Collections.addAll(mItems, items);
		notifyDataSetChanged();
	}

	/**
	 * Inserts all of the elements in the specified collection into the list,
	 * starting at the specified position.
	 */
	public void addAll(int position, Collection<? extends T> items) {
		mItems.addAll(position, items);
		notifyDataSetChanged();
	}

	/**
	 * Inserts all of the elements into the list, starting at the specified
	 * position.
	 */
	public void addAll(int position, T... items) {
		for (int i = position; i < (items.length + position); i++) {
			mItems.add(i, items[i]);
		}
		notifyDataSetChanged();
	}

	/**
	 * Removes all of the elements from the list.
	 */
	public void clear() {
		mItems.clear();
		notifyDataSetChanged();
	}

	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element.
	 */
	public void set(int position, T item) {
		mItems.set(position, item);
		notifyDataSetChanged();
	}

	/**
	 * Removes the specified element from the list
	 */
	public void remove(T item) {
		mItems.remove(item);
		mSelect.remove(item);
		notifyDataSetChanged();
	}

	/**
	 * Removes the element at the specified position in the list
	 */
	public void remove(int position) {
		mItems.remove(position);
		mSelect.remove(position);
		notifyDataSetChanged();
	}

	/**
	 * Removes all elements at the specified positions in the list
	 */
	public void removePositions(Collection<Integer> positions) {
		ArrayList<Integer> positionsList = new ArrayList<Integer>(positions);
		Collections.sort(positionsList);
		Collections.reverse(positionsList);
		for (int position : positionsList) {
			mItems.remove(position);
			mSelect.remove(position);
		}
		notifyDataSetChanged();
	}

	/**
	 * Removes all of the list's elements that are also contained in the
	 * specified collection
	 */
	public void removeAll(Collection<T> items) {
		mItems.removeAll(items);
		mSelect.removeAll(items);
		notifyDataSetChanged();
	}

	/**
	 * Retains only the elements in the list that are contained in the specified
	 * collection
	 */
	public void retainAll(Collection<T> items) {
		mItems.retainAll(items);
		notifyDataSetChanged();
	}

	/**
	 * Returns the position of the first occurrence of the specified element in
	 * this list, or -1 if this list does not contain the element. More
	 * formally, returns the lowest position <tt>i</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
	 * or -1 if there is no such position.
	 */
	public int indexOf(T item) {
		return mItems.indexOf(item);
	}

	private List<T> mSelect = new ArrayList<T>();

	public boolean getItemSelected(int position) {
		if(getItemViewSelectType(position)==TYPE_SELECT){
			return true;
		}
		return false;
	}
	public int getItemViewSelectType(int position) {
		return mSelect.contains(getItem(position)) ? TYPE_SELECT : TYPE_UNSELECT;
	}

	public void invertSelected(int position) {
		if (mSelect.contains(getItem(position))) {
			mSelect.remove(getItem(position));
		} else {
			mSelect.add(getItem(position));
		}
		notifyDataSetChanged();
	}

	public void selectAll() {
		for (int i = 0; i < getCount(); i++) {
			if (!mSelect.contains(getItem(i))) {
				mSelect.add(getItem(i));
			}
		}
		notifyDataSetChanged();
	}

	public void invertAll() {
		for (int i = 0; i < getCount(); i++) {
			if (mSelect.contains(getItem(i))) {
				mSelect.remove(getItem(i));
			} else {
				mSelect.add(getItem(i));
			}
		}
		notifyDataSetChanged();
	}
	public List<Integer> getPositionSelected() {
		List<Integer> list=new ArrayList<Integer>();
		for (int i = 0; i < mSelect.size(); i++) {
			list.add(indexOf(mSelect.get(i)));
		}
		return list;
	}
	public List<T> getSelected() {
		return mSelect;
	}
	public void clearSelected(T item){
		mSelect.remove(item);
		notifyDataSetChanged();
	}
	public void clearSelected(int position){
		mSelect.remove(getItem(position));
		notifyDataSetChanged();
	}
	
	public void clearSelected(Collection<T> items){
		mSelect.removeAll(items);
		notifyDataSetChanged();
	}

	
	public void clearSelected(){
		mSelect.clear();
		notifyDataSetChanged();
	}
}