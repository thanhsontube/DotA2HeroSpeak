package son.nt.dota2.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.utils.Logger;

/**
 * Created by sonnt on 10/9/16.
 */

public class FireBaseUtils {

    public static final String TAG = FireBaseUtils.class.getSimpleName();

    public static void find(String child, String key, String value, HeroBasicDto data) {
        Logger.debug(TAG, ">>>" + "find:" + child + ";key:" + key + ";value:" + value);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        Query query = reference.child(child).orderByChild(key).equalTo(value);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Logger.debug(TAG, ">>>" + "find onDataChange:" + dataSnapshot);
//                update(dataSnapshot, data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Logger.error(TAG, ">>> Error:" + "find onCancelled:" + databaseError);

            }
        });
    }

    // this one update data to firebase
    public static void update(DataSnapshot dataSnapshot, HeroBasicDto data) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
        String key = nodeDataSnapshot.getKey();
        Logger.debug(TAG, ">>>" + "key:" + key);
        reference.child("/" + dataSnapshot.getKey() + "/" + key).updateChildren(data.toMap());
    }

    // this one update data to firebase
    public static void update(DataSnapshot dataSnapshot, HeroResponsesDto data) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
        String key = nodeDataSnapshot.getKey();
        Logger.debug(TAG, ">>>" + "key:" + key);
        reference.child("/" + dataSnapshot.getKey() + "/" + key).updateChildren(data.toMap());
    }
}
