package xyz.discobiscuit.hoplyfork.database;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

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

    private static volatile HoplyRepository INSTANCE;

    private HoplyRepository(Context context ) {

        HoplyDB database = HoplyDB.getInstance( context );

        this.executor = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() );
        this.userDao = database.userDao();
        this.postDao = database.postDao();

    }

    // Singleton Structure:
    public static HoplyRepository getInstance(Context context ) {

        if ( INSTANCE == null )
            synchronized ( HoplyDB.class ) {

                if ( INSTANCE == null )
                    INSTANCE = new HoplyRepository( context );

            }

        return INSTANCE;

    }

    public void insertUser( User user ) {
        new InsertUserAsyncTask( userDao ).execute( user );
    }

    @RequiresApi( api = Build.VERSION_CODES.N )
    public Optional<User> findUserById(String id ) {

        Future<User> userFuture =
                executor.submit( () -> userDao.findById( id ) );

        try {

            User user = userFuture.get();

            if ( user == null )
                return Optional.empty();
            else
                return Optional.of( userFuture.get() );

        } catch ( ExecutionException | InterruptedException e ) {
            return Optional.empty();
        }

    }

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
                return Optional.of( postsFuture.get() );

        } catch ( ExecutionException | InterruptedException e ) {
            return Optional.empty();
        }

    }

    public void deleteAllPosts() {
        new DeleteAllPostsAsyncTask( postDao ).execute();
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        private InsertUserAsyncTask( UserDao userDao ) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground( User... users ) {

            userDao.insert( users[0] );
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

}
