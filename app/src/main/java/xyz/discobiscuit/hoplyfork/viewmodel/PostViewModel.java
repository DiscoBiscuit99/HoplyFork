package xyz.discobiscuit.hoplyfork.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import xyz.discobiscuit.hoplyfork.database.HoplyRepository;
import xyz.discobiscuit.hoplyfork.database.Post;

public class PostViewModel extends AndroidViewModel {

    private LiveData<List<Post>> allPosts;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public PostViewModel(@NonNull Application app ) {

        super( app );
        allPosts = HoplyRepository
                .getInstance( getApplication().getApplicationContext() )
                .getAllPosts()
                .get();

    }

    public void insert( Post post ) {

        HoplyRepository
                .getInstance( getApplication().getApplicationContext() )
                .insertPosts( post );

    }

    public LiveData<List<Post>> getAllPosts() {
        return allPosts;
    }

}
