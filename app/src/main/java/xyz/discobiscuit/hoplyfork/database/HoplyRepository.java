package xyz.discobiscuit.hoplyfork.database;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HoplyRepository {

    private static ExecutorService executor;
    private static UserDao userDao;
    private static PostDao postDao;
    private static ReactionDao reactionDao;

    private static volatile HoplyRepository INSTANCE;

    private HoplyRepository(Context context ) {

        HoplyDB database = HoplyDB.getInstance( context );

        this.executor = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() );
        this.userDao = database.userDao();
        this.postDao = database.postDao();
        //this.reactionDao = database.reactionDao();

    }

    // Singleton Structure:
    public static HoplyRepository getInstance( Context context ) {

        if ( INSTANCE == null )
            synchronized ( HoplyDB.class ) {

                if ( INSTANCE == null )
                    INSTANCE = new HoplyRepository( context );

            }

        return INSTANCE;

    }

    /// USER RELATED METHODS ///

    public void insertUser( User user ) {
        new InsertUserAsyncTask( userDao ).execute( user );
    }

    @RequiresApi( api = Build.VERSION_CODES.N )
    public Optional<User> findUserById( String id ) {

        Future<User> userFuture =
                executor.submit( () -> userDao.findById( id ) );

        try {

            User user = userFuture.get();

            if ( user == null )
                return Optional.empty();
            else
                return Optional.of( userFuture.get() );

        } catch ( ExecutionException | InterruptedException e ) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    /// POST RELATED METHODS ///

    public void insertPosts( Post... posts ) {
        new InsertPostsAsyncTask( postDao ).execute( posts );
    }

    @RequiresApi( api = Build.VERSION_CODES.N )
    public Optional<LiveData<List<Post>>> getAllPosts() {

        Future<LiveData<List<Post>>> postsFuture =
                executor.submit( () -> postDao.getAll() );

        try {

            LiveData<List<Post>> posts = postsFuture.get();

            if ( posts == null )
                return Optional.empty();
            else
                return Optional.of( posts );

        } catch ( ExecutionException | InterruptedException e ) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    public void deleteAllPosts() {
        new DeleteAllPostsAsyncTask( postDao ).execute();
    }

    /// REACTION RELATED METHODS ///

    @RequiresApi( api = Build.VERSION_CODES.N )
    public Optional<LiveData<List<Reaction>>> getAllReactions() {

        Future<LiveData<List<Reaction>>> reactionsFuture =
                executor.submit( () -> reactionDao.getAll() );

        try {

            LiveData<List<Reaction>> reactions = reactionsFuture.get();

            if ( reactions.getValue() == null ) {
                return Optional.empty();
            } else
                return Optional.of( reactions );

        } catch ( ExecutionException | InterruptedException e ) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    @RequiresApi( api = Build.VERSION_CODES.N )
    public Optional<LiveData<List<Reaction>>> getAllLikes() {

        Future<LiveData<List<Reaction>>> likesFuture =
                executor.submit( () -> reactionDao.getAllLikes() );

        try {

            LiveData<List<Reaction>> reactions = likesFuture.get();

            if ( reactions.getValue() == null ) {
                return Optional.empty();
            } else
                return Optional.of( reactions );

        } catch ( ExecutionException | InterruptedException e ) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    @RequiresApi( api = Build.VERSION_CODES.N )
    public Optional<LiveData<List<Reaction>>> getAllDislikes() {

        Future<LiveData<List<Reaction>>> dislikesFuture =
                executor.submit( () -> reactionDao.getAllDislikes() );

        try {

            LiveData<List<Reaction>> reactions = dislikesFuture.get();

            if ( reactions.getValue() == null ) {
                return Optional.empty();
            } else
                return Optional.of( reactions );

        } catch ( ExecutionException | InterruptedException e ) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    public void insertReactions( Reaction... reactions ) {
        new InsertReactionsAsyncTask( reactionDao ).execute( reactions );
    }

    /// ASYNC TASKS ///

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        private InsertUserAsyncTask( UserDao userDao ) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground( User... users ) {

            userDao.insert( users[ 0 ] );
            return null;

        }

    }

    private static class InsertPostsAsyncTask extends AsyncTask<Post, Void, Void> {

        private PostDao postDao;

        private InsertPostsAsyncTask( PostDao postDao ) {
            this.postDao = postDao;
        }

        @Override
        protected Void doInBackground( Post... posts ) {

            for ( Post post : posts )
                postDao.insert( post );

            return null;

        }

    }

    private static class DeleteAllPostsAsyncTask extends AsyncTask<Void, Void, Void> {

        private PostDao postDao;

        private DeleteAllPostsAsyncTask( PostDao postDao ) {
            this.postDao = postDao;
        }

        @Override
        protected Void doInBackground( Void... voids ) {

            postDao.deleteAll();
            return null;

        }

    }

    private static class InsertReactionsAsyncTask extends AsyncTask<Reaction, Void, Void> {

        private ReactionDao reactionDao;

        private InsertReactionsAsyncTask( ReactionDao reactionDao ) {
            this.reactionDao = reactionDao;
        }

        @Override
        protected Void doInBackground( Reaction... reactions ) {

            for ( Reaction reaction : reactions )
                reactionDao.insert( reaction );

            return null;

        }

    }

}
