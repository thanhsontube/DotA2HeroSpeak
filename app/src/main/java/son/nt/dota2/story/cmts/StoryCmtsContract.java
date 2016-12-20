package son.nt.dota2.story.cmts;

import java.util.List;

import son.nt.dota2.comments.FullCmtsDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class StoryCmtsContract {

    public interface View {
        void showList(List<FullCmtsDto> fullCmtsDtos);
    }

    public interface Presenter {

        void setStoryId(String data);

        void getComments();

        void putCommend(String mess);
    }
}
