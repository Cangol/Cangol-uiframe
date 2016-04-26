package mobi.cangol.mobile.uiframe.demo.model;

import java.io.Serializable;

import mobi.cangol.mobile.db.DatabaseField;
import mobi.cangol.mobile.db.DatabaseTable;
import android.os.Parcel;
import android.os.Parcelable;

@DatabaseTable("Station")
public class Station implements Serializable,Parcelable{
	@DatabaseField(primaryKey=true,notNull=true)
	private int _id;
	@DatabaseField(notNull=true)
	private String name;
	public Station(){}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Station [_id=" + _id + ", name=" + name + "]";
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(_id);
		dest.writeString(name);
		
	}
	public Station(Parcel in) {
		_id = in.readInt();
		name = in.readString();
	}
	public static final Parcelable.Creator<Station> CREATOR = new Parcelable.Creator<Station>() {

		@Override
		public Station createFromParcel(Parcel in) {
			return new Station(in);
		}

		@Override
		public Station[] newArray(int size) {
			return new Station[size];
		}
	};
	
}
