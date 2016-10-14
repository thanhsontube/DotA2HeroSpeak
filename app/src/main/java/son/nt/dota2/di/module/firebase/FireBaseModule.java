package son.nt.dota2.di.module.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import son.nt.dota2.firebase.FireBaseRepository;
import son.nt.dota2.firebase.IFireBaseRepository;

/**
 * Created by sonnt on 10/14/16.
 */

@Module
public class FireBaseModule {

    @Singleton
    @Provides
    FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }


    @Singleton
    @Provides
    DatabaseReference provideDatabaseReference(FirebaseDatabase firebaseDatabase) {
        return firebaseDatabase.getReference();
    }

    @Singleton
    @Provides
    IFireBaseRepository provideFireBaseRepo(DatabaseReference reference) {
        return new FireBaseRepository(reference);
    }
}
