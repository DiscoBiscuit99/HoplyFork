package xyz.discobiscuit.hoplyfork.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database( version = 13, entities = { User.class, Post.class, Reaction.class, MapLocationEntity.class }, exportSchema = false )
public abstract class HoplyDB extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract PostDao postDao();
    public abstract ReactionDao reactionDao();
    public abstract MapLocationDao locationDao();

    private static volatile HoplyDB INSTANCE;

    /*
     * Singleton Structure:
     *
     * Checks whether an instance of the database exists, and if so,
     * returns it. Otherwise, an instance of the database is created
     * and returned.
     */
    public static synchronized HoplyDB getInstance( Context context ) {

        if ( INSTANCE == null )
            synchronized ( HoplyDB.class ) {

                if ( INSTANCE == null )
                    INSTANCE =
                        Room.databaseBuilder(
                                context.getApplicationContext(),
                                HoplyDB.class,
                                "HOPLY_DB" )
                            .fallbackToDestructiveMigration()
                            .addCallback( roomCallback ) // NOTE: can be removed
                            .build();

            }

        return INSTANCE;

    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate( @NonNull SupportSQLiteDatabase database ) {

            super.onCreate( database );
            new PopulateDbAsyncTask( INSTANCE ).execute();

        }

    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private UserDao userDao;
        private PostDao postDao;
        private ReactionDao reactionDao;
        private MapLocationDao mapLocationDao;

        private PopulateDbAsyncTask( HoplyDB database ) {
            userDao = database.userDao();
            postDao = database.postDao();
            reactionDao = database.reactionDao();
            mapLocationDao = database.locationDao();
        }

        @Override
        protected Void doInBackground( Void... voids ) {

            userDao.insert( new User( "test", "TestUser", "time" ) );

            postDao.insert( new Post( "test", "Content 1", "time" ) );
            postDao.insert( new Post( "test", "Content 2", "time" ) );
            postDao.insert( new Post( "test", "Content 3", "time" ) );

            return null;

        }

    }

}
