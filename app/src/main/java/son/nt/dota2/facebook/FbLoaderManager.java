package son.nt.dota2.facebook;


public class FbLoaderManager {
	public void load(final FbLoader<?> loader) {
		loader.request(this);
	}
}
