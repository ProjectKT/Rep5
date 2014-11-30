package content;
import java.util.ArrayList;


public class Observable<T> {
	protected final ArrayList<Observer<T>> observers = new ArrayList<Observer<T>>();
	
	/**
	 * オブザーバーを登録する
	 * @param observer
	 */
	public void registerObserver(Observer<T> observer) {
		synchronized (observers) {
			observers.add(observer);
		}
	}
	
	/**
	 * オブザーバーの登録を削除する
	 * @param observer
	 */
	public void unregisterObserver(Observer<T> observer) {
		synchronized (observers) {
			observers.remove(observer);
		}
	}
	
	/**
	 * データに変更が加えられたことを通知する
	 * @param data
	 */
	public void notifyDataChanged(T data) {
		synchronized (observers) {
			for (int i = observers.size()-1; i >= 0; --i) {
				observers.get(i).onDataChanged(data);
			}
		}
	}
	
	/**
	 * データセットに変更が加えられたことを通知する
	 */
	public void notifyDataSetChanged() {
		synchronized (observers) {
			for (int i = observers.size()-1; i >= 0; --i) {
				observers.get(i).onDataSetChanged();
			}
		}
	}
}
