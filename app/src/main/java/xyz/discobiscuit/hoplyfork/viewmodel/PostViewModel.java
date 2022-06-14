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

    private final HoplyRepository repository;

    private final LiveData<List<Post>> allPosts;
    private final LiveData<List<Reaction>> allReactions;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public PostViewModel(@NonNull Application app ) {

        super( app );

        repository = HoplyRepository
                .getInstance(getApplication().getApplicationContext());

        if ( repository.getAllPosts().isPresent() )
            allPosts = repository.getAllPosts().get();
        else
            allPosts = new MutableLiveData<List<Post>>();

        if ( repository.getAllReactions().isPresent() )
            allReactions = repository.getAllReactions().get();
        else
            allReactions = new MutableLiveData<List<Reaction>>();

    }

    public void insert( Post post ) {
        repository.insertPosts( post );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LiveData<List<Post>> getAllPosts() {
        return allPosts;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LiveData<List<Reaction>> getAllReactions() {
            return allReactions;
    }

}
