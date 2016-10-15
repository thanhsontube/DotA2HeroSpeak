package son.nt.dota2.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 10/14/16.
 */

public class FireBaseRepository implements IFireBaseRepository {

    DatabaseReference mDatabaseReference;
    CallBack mCallBack;


    @Override
    public void setCallBack(CallBack callBack) {
        this.mCallBack = callBack;
    }

    public FireBaseRepository(DatabaseReference databaseReference) {
        mDatabaseReference = databaseReference;
    }

    @Override
    public void loginEmail(String email, String password) {

    }

    @Override
    public void loginFacebook() {

    }

    @Override
    public void loginGoogle() {

    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public void logout() {

    }

    @Override
    public void getHeroBasic(String group) {
        mDatabaseReference.child(HeroBasicDto.class.getSimpleName()).addListenerForSingleValueEvent(valueEventListener);
//        Query query = mDatabaseReference.child(HeroBasicDto.class.getSimpleName()).orderByChild("group").equalTo(group);
//        query.addListenerForSingleValueEvent(valueEventListener);
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<HeroBasicDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                HeroBasicDto post = postSnapshot.getValue(HeroBasicDto.class);
                list.add(post);
            }
            if (mCallBack != null) {
                mCallBack.onGetData(list);
            }
//            OttoBus.post(new BasicHeroListResponse(list));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


}
