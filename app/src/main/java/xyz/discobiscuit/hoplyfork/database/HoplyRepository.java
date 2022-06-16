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

    // The executor service.
    private static ExecutorService executor;

    // The DAOs.
    private static UserDao userDao;
    private static PostDao postDao;
    private static ReactionDao reactionDao;
    private static MapLocationDao mapLocationDao;

    // The repository.
    private static volatile HoplyRepository INSTANCE;

    // Returns a new instance of the repository.
    private HoplyRepository(Context context ) {

        HoplyDB database = HoplyDB.getInstance( context );

        executor = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() );
        userDao = database.userDao();
        postDao = database.postDao();
        reactionDao = database.reactionDao();
        mapLocationDao = database.locationDao();
    }

    // Singleton Structure (as the database):
    public static HoplyRepository getInstance( Context context ) {

        if ( INSTANCE == null )
            synchronized ( HoplyDB.class ) {

                if ( INSTANCE == null )
                    INSTANCE = new HoplyRepository( context );

            }

        return INSTANCE;

    }

    /// USER RELATED METHODS ///

    // Inserts a new user into the local database using an async task (defined below).
    public void insertUser( User user ) {
        new InsertUserAsyncTask( userDao ).execute( user );
    }

    // Returns an optional of the desired user from the local database.
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

    // Insert a new post into the local database using an async task (defined below).
    public void insertPosts( Post... posts ) {
        new InsertPostsAsyncTask( postDao ).execute( posts );
    }

    // Insert a new post into the local database and return the id using an async task (defined below).
    public long insertPostsReturnId(Post... posts ) throws ExecutionException, InterruptedException {
       return new InsertPostsReturnIdAsyncTask( postDao ).execute( posts ).get();
    }

    // Returns an optional of the desired post from the local database.
    @RequiresApi( api = Build.VERSION_CODES.N )
    public Optional<Post> findPostById( int id ) {

        Future<Post> postFuture = executor.submit( () -> postDao.findById( id ) );

        try {

            Post post = postFuture.get();

            if ( post == null )
                return Optional.empty();
            else
                return Optional.of( post );

        } catch ( ExecutionException | InterruptedException e ) {

            e.printStackTrace();
            return Optional.empty();

        }

    }

    // Returns an optional of all posts in the local database.
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

    // Deletes all posts in the local database.
    public void deleteAllPosts() {
        new DeleteAllPostsAsyncTask( postDao ).execute();
    }

    /// REACTION RELATED METHODS ///

    // Returns an optional of all reactions in the local database.
    @RequiresApi( api = Build.VERSION_CODES.N )
    public Optional<LiveData<List<Reaction>>> getAllReactions() {

        Future<LiveData<List<Reaction>>> reactionsFuture =
                executor.submit( () -> reactionDao.getAll() );

        try {

            LiveData<List<Reaction>> reactions = reactionsFuture.get();

            if ( reactions == null ) {
                return Optional.empty();
            } else
                return Optional.of( reactions );

        } catch ( ExecutionException | InterruptedException e ) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    // Returns an optional of all the likes (reaction.type == 1) in the local database.
    @RequiresApi( api = Build.VERSION_CODES.N )
    public Optional<LiveData<List<Reaction>>> getAllLikes() {

        Future<LiveData<List<Reaction>>> likesFuture =
                executor.submit( () -> reactionDao.getAllLikes() );

        try {

            LiveData<List<Reaction>> reactions = likesFuture.get();

            if ( reactions == null ) {
                return Optional.empty();
            } else
                return Optional.of( reactions );

        } catch ( ExecutionException | InterruptedException e ) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    // Returns an optional of all the dislikes (reaction.type == 1) in the local database.
    @RequiresApi( api = Build.VERSION_CODES.N )
    public Optional<LiveData<List<Reaction>>> getAllDislikes() {

        Future<LiveData<List<Reaction>>> dislikesFuture =
                executor.submit( () -> reactionDao.getAllDislikes() );

        try {

            LiveData<List<Reaction>> reactions = dislikesFuture.get();

            if ( reactions == null ) {
                return Optional.empty();
            } else
                return Optional.of( reactions );

        } catch ( ExecutionException | InterruptedException e ) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    // Inserts reactions into the local database.
    public void insertReactions( Reaction... reactions ) {
        new InsertReactionsAsyncTask( reactionDao ).execute( reactions );
    }

    /// LOCATION RELATED METHODS ///

    // Returns a map location from the given post id from the local database.
    public MapLocationEntity findLocationById(int post_id ) {

        Future<MapLocationEntity> locationFuture = executor.submit(() -> mapLocationDao.findById( post_id));
        try {
            return locationFuture.get();
        } catch (Exception e){
            return null;
        }

    }

    // Inserts the given location into the local database.
    public void insertLocation(MapLocationEntity mapLocationEntity) {
        new InsertLocationAsyncTask(mapLocationDao).execute(mapLocationEntity);
    }


    /// ASYNC TASKS ///

    // Each asynchronous task simply implements a `doInBackground` method
    // that... runs in the background on another thread.

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

    private static class InsertPostsReturnIdAsyncTask extends AsyncTask<Post, Void, Long> {

        private PostDao postDao;

        private InsertPostsReturnIdAsyncTask( PostDao postDao ) {
            this.postDao = postDao;
        }

        @Override
        protected Long doInBackground(Post... posts ) {

            return postDao.insertReturnId(posts [0] );

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

    private static class InsertLocationAsyncTask extends AsyncTask<MapLocationEntity, Void, Void>{

        private MapLocationDao mapLocationDao;

        private InsertLocationAsyncTask( MapLocationDao mapLocationDao){ this.mapLocationDao = mapLocationDao; }

        @Override
        protected Void doInBackground(MapLocationEntity... mapLocationEntities) {

            mapLocationDao.insert(mapLocationEntities[ 0 ]);
            return null;
        }
    }

}
