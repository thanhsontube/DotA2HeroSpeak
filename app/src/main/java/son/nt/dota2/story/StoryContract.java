package son.nt.dota2.story;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import son.nt.dota2.dto.story.StoryPartDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class StoryContract {

    public interface View {
        void showAddList(List<StoryPartDto> dtos);

        void doFinish();
    }

    public interface Presenter {
        void createAddList();

        void saveStory(String s, FirebaseUser firebaseUser);

        void playStory(String title, FirebaseUser firebaseUser);

        void stopStory();
    }
}
