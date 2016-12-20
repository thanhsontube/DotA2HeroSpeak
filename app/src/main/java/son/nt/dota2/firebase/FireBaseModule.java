package son.nt.dota2.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;
import son.nt.dota2.di.scoped.ActivityScoped;

/**
 * Created by sonnt on 8/20/16.
 */
@Module
public class FireBaseModule {

    @ActivityScoped
    @Provides
    FirebaseAuth provideFirebaseAuth()
    {
        return FirebaseAuth.getInstance();
    }

    @ActivityScoped
    @Provides
    FirebaseUser provideFirebaseUser(FirebaseAuth firebaseAuth)
    {
        return firebaseAuth.getCurrentUser();
    }

    @ActivityScoped
    @Provides
    DatabaseReference provideDatabaseReference()
    {
        return FirebaseDatabase.getInstance().getReference();
    }

}
