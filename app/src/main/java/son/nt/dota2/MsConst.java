package son.nt.dota2;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class MsConst {

    public static final boolean IS_HANDSOME = true;
    public static final String GROUP_STR = "Str";
    public static final String GROUP_AGI = "Agi";
    public static final String GROUP_INTEL = "Intel";

    public static final String KEY_REPEAT = "KEY_REPEAT";

    public static enum RepeatMode {
        MODE_ON (0),
        MODE_OFF(1),
        MODE_ONE(2);

        int i = android.R.color.transparent;

        private int value;

        RepeatMode(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }

        public static RepeatMode getMode(int v) {
            for (RepeatMode dto : RepeatMode.values()) {
                if (dto.getValue() == v) {
                    return dto;
                }
            }
            return RepeatMode.MODE_OFF;
        }


    }

    public static enum MenuSelect {
        FB_SHARE,
        FAVORITE,
        COPY,
        RINGTONE
    }

    public static final String FB_ID_POST_TO = "1637205283182530";

    public static final String FB_COMMENT_TO = "/1637431773159881/comments";
    public static final String FB_FETCH = "http://gdata.youtube.com/feeds/api/playlists/%s?v=2&alt=json";
    public static final String FB_AVATAR_LINK = "https://graph.facebook.com/%s/picture?type=normal";
}
