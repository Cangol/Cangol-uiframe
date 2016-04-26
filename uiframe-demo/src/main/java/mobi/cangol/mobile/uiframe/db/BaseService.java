package mobi.cangol.mobile.uiframe.db;

import java.util.List;

import android.database.SQLException;

public interface BaseService<T> {
	/**
	 * 
	 * @param obj
	 * @return 
	 */
	void refresh(T obj) throws SQLException ;
	/**
	 * 
	 * @param id
	 */
	void delete(Integer id) throws SQLException ;
	/**
	 * 
	 * @param id
	 * @return
	 */
	T find(Integer id) throws SQLException ;
	/**
	 * 
	 * @return
	 */
	int getCount() throws SQLException ;
	/**
	 * 
	 * @param keywords
	 * @return
	 */
	List<T> getAllList() throws SQLException ;
	/**
	 * 
	 * @param obj
	 */
	int save(T obj);
	/**
	 * 
	 * @param from
	 * @param total
	 * @return
	 */
	List<T> findList(long from,long total) ;
}
