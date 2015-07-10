package son.nt.dota2.provider;

import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.provider.SearchRecentSuggestions;

/**
 * Created by Sonnt on 7/10/15.
 */
public class SearchableProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = ".provider.SearchableProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SearchableProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    private static SearchRecentSuggestions newHelper (Context context) {
        return new SearchRecentSuggestions(context, AUTHORITY, MODE);
    }

    public static void clearHistory (Context context) {
        SearchRecentSuggestions searchRecentSuggestions = newHelper(context);
        searchRecentSuggestions.clearHistory();
    }
    public static void saveQuery (final Context context, final String query) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SearchRecentSuggestions searchRecentSuggestions = newHelper(context);
                searchRecentSuggestions.saveRecentQuery(query, null);
            }
        }).start();
    }
}
