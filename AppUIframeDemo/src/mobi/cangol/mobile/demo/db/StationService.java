package mobi.cangol.mobile.demo.db;

import java.util.List;

import mobi.cangol.mobile.db.Dao;
import mobi.cangol.mobile.db.QueryBuilder;
import mobi.cangol.mobile.demo.model.Station;
import mobi.cangol.mobile.demo.utils.Constants;
import android.content.Context;
import android.database.SQLException;
import android.util.Log;

public class StationService implements BaseService<Station> {
	private static final String TAG = Constants.makeLogTag(StationService.class);
	private Dao<Station, Integer> dao;

	public StationService(Context context) {
		try {
			DatabaseHelper dbHelper = DatabaseHelper
					.createDataBaseHelper(context);
			 dao = dbHelper.getDao(Station.class);
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e(TAG, "StationService init fail!");
		}
	}

	@Override
	public int save(Station obj) {
		int result = -1;
		try {
			if (obj.get_id() >0)
				result=dao.update(obj);
			else
				result=dao.create(obj);
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e(TAG, "StationService save fail!");
		}
		return result;
	}

	@Override
	public void refresh(Station obj) {
		try {
			dao.refresh(obj);
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e(TAG, "StationService refresh fail!");
		}
	}

	@Override
	public void delete(Integer id) {
		try {
			dao.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "StationService delete fail!");
		}

	}

	@Override
	public Station find(Integer id) {
		try {
			return dao.queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e(TAG, "StationService find fail!");
		}
		return null;
	}

	@Override
	public int getCount() {
		try {
			return dao.queryForAll().size();
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e(TAG, "StationService getCount fail!");
		}
		return -1;
	}

	public List<Station> getAllList() {
		try {
			return dao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e(TAG, "StationService getAllList fail!");
		}
		return null;
	}

	@Override
	public List<Station> findList(long from, long total) {
		QueryBuilder queryBuilder=new QueryBuilder(Station.class);
		queryBuilder.addQuery("name", "test", "elike");
		queryBuilder.orderBy("_id asc");
		queryBuilder.offset(from);
		queryBuilder.limit(total);
		List<Station> list=dao.query(queryBuilder);
		return list;
	}

}
