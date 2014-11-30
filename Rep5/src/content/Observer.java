package content;

/**
 * オブザーバー
 * @param <T>
 */
public interface Observer<T> {
	/**
	 * データが変更された際に呼ばれる
	 * @param t
	 */
	public void onDataChanged(T data);
	
	/**
	 * データセットに変更が加えられた際に呼ばれる
	 */
	public void onDataSetChanged();
}
