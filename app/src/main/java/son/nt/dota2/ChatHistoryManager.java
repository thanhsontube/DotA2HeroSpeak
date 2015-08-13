package son.nt.dota2;

import android.content.Context;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.comments.CommentDto;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.ottobus_entry.GoChatManager;
import son.nt.dota2.utils.OttoBus;

/**
 * Created by Sonnt on 8/13/15.
 */
public class ChatHistoryManager {
    public List<CommentDto> listCmts = new ArrayList<>();
    public List<CommentDto> listHeroCmts = new ArrayList<>();

    static ChatHistoryManager INSTANCE = null;

    private Context context;

    public static void createInstance(Context context) {
        INSTANCE = new ChatHistoryManager(context);
    }

    public ChatHistoryManager(Context context) {
        this.context = context;
        getHistory(null);
    }

    public static ChatHistoryManager getInstance () {
        return INSTANCE;
    }

    public void getHistory(String heroID) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(CommentDto.class.getSimpleName());
        if (heroID != null) {
            query.whereEqualTo("heroID", heroID);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                CommentDto commentDto;
                List<CommentDto> list_ = new ArrayList<CommentDto>();
                for (ParseObject p : list) {

                    String message = p.getString("message");
                    String fromID = p.getString("fromID");
                    String fromName = p.getString("fromName");
                    long createTime = p.getLong("createTime");

                    String heroText = p.getString("heroText");
                    String heroLink = p.getString("heroLink");
                    String heroID = p.getString("heroID");
                    String heroGroup = p.getString("heroGroup");

                    commentDto = new CommentDto();
                    commentDto.setMessage(message);
                    commentDto.setFromID(fromID);
                    commentDto.setFromName(fromName);
                    commentDto.setCreateTime(createTime);

                    SpeakDto speakDto = new SpeakDto();
                    speakDto.heroId = heroID;
                    speakDto.text = heroText;
                    speakDto.voiceGroup = heroGroup;
                    speakDto.link = heroLink;

                    commentDto.setSpeakDto(speakDto);
                    list_.add(commentDto);
                }
                listCmts.clear();
                listCmts.addAll(list_);
                OttoBus.post(new GoChatManager());

            }
        });

    }

    public List<CommentDto> getHeroHistory (String heroID) {

        List<CommentDto> list = new ArrayList<>();
        if (listCmts.size() == 0) {
            getHistory(null);
            return list;
        }
        for (CommentDto d : listCmts) {
            if (d.getSpeakDto().heroId.equals(heroID)) {
                list.add(d);
            }
        }
        return list;

    }

    public void updateHistory () {
        getHistory(null);
    }
}
