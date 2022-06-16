package xyz.discobiscuit.hoplyfork.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Optional;

import xyz.discobiscuit.hoplyfork.database.HoplyRepository;
import xyz.discobiscuit.hoplyfork.database.Post;
import xyz.discobiscuit.hoplyfork.database.Reaction;

public class PostViewModel extends AndroidViewModel {

    // Reference to the repository.
    private final HoplyRepository repository;

    // Lists containing posts and reactions.
    private final LiveData<List<Post>> allPosts;
    private final LiveData<List<Reaction>> allReactions;

    // Returns a post view model.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public PostViewModel(@NonNull Application app ) {

        super( app );

        // Sets the reference to the repository.
        repository = HoplyRepository
                .getInstance(getApplication().getApplicationContext());

        // If the local database contains posts, fetch them.
        if ( repository.getAllPosts().isPresent() )
            allPosts = repository.getAllPosts().get();
        // Otherwise, just create new LiveData.
        else
            allPosts = new MutableLiveData<>();

        // If the local database contains reactions, fetch them.
        if ( repository.getAllReactions().isPresent() )
            allReactions = repository.getAllReactions().get();
        // Otherwise, just create new LiveData.
        else
            allReactions = new MutableLiveData<>();

    }

    // Inserts the given post into the local database.
    public void insert( Post post ) {
        repository.insertPosts( post );
    }

    // Returns the posts contained in this class.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public LiveData<List<Post>> getAllPosts() {
        return allPosts;
    }

    // Returns the reactions contained in this class.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public LiveData<List<Reaction>> getAllReactions() {
            return allReactions;
    }

}
