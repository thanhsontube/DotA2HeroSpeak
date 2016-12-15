package son.nt.dota2;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class MsConst {

    //type of Sounds
    public static final int TYPE_HERO_SOUND = 1;
    public static final int TYPE_MUSIC_PACK = 2;
    public static final int TYPE_CREATIVE_SOUND = 3;
    public static final int TYPE_ITEM_SOUND = 4;
    public static final int TYPE_ABILITY_SOUND = 4;

    //group
    public static final String GROUP_STR = "Str";
    public static final String GROUP_AGI = "Agi";
    public static final String GROUP_INTEL = "Intel";


    //VoiceGroup
    //LORD
    public static final String LORD_KILLING = "Enemies killing";
    public static final String LORD_MEETING = "Allies meeting";
    public static final String BUYS_ITEMS = "Acquiring an item";

    public static final String PREFETCH = "PREFETCH";


    //firebase table
    public static final String TABLE_LORD_RESPONSES = "HeroLordSounds";
    public static final String TABLE_ITEMS = "ItemsDota2";
    public static final String TABLE_HERO_ITEMS = "dota2_hero_buy_items";
    public static final String TABLE_HERO_KILLING_MEETING = "dota2_hero_killing_meeting";
    public static final String TABLE_HERO_NORMAL_VOICE = "dota2_hero_normal_voice";
    public static final String TABLE_HERO_ABI = "dota2_hero_ability";
    public static final String TABLE_STORY = "dota2_story";
    public static final String TABLE_MONKEY_KING = "hero_monkey_king_sounds";
    public static final String TABLE_MONKEY_KING_KILLING = "hero_monkey_king_killing";
    public static final String TABLE_MONKEY_KING_NORMAL = "hero_monkey_king_normal";

    public static final String TRACK_START = "/start";
    public static final String TRACK_LOGIN = "/fb_login";
    public static final String TRACK_LOGOUT = "/fb_logout";
    public static final String TRACK_SEARCH = "/search";
    public static final String TRACK_CHAT = "/chat_dialog";
    public static final String TRACK_SHARE_VOICE = "/TRACK_SHARE_VOICE";
    public static final String TRACK_PUSH_COMMENT = "/TRACK_PUSH_COMMENT";


    public static final String LINK_STORE = "https://play.google.com/store/apps/details?id=son.nt.dota2";
    public static final boolean IS_HANDSOME = true;



    public static final String KEY_REPEAT = "KEY_REPEAT";
    public static final String KEY_ALLOW_PLAY = "KEY_ALLOW_PLAY";
    public static final String KEY_HELP = "KEY_HELP";

    public static final String EXTRA_HERO = "EXTRA_HERO";
    public static final String ROOT_DOTA2 = "http://www.dota2.com/hero/";

    public static final String TYPE_ADD = "add";
    public static final String TYPE_SOUND_LEFT = "sound left";
    public static final String TYPE_SOUND_RIGHT = "sound right";
    public static final String TYPE_SOUND_MIDDLE = "sound middle";


    public static enum RepeatMode {
        MODE_ON(0),
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

    public static final String CHANNEL_COMMON = "dota2_common_channel";
    public static final String FB_PAGE_ID = "1637205283182530";

}
