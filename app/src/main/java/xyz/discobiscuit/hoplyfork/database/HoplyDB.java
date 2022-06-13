package xyz.discobiscuit.hoplyfork.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database( version = 7, entities = { User.class, Post.class, Reaction.class }, exportSchema = false )
public abstract class HoplyDB extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract PostDao postDao();
    public abstract ReactionDao reactionDao();

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
                            .addCallback( roomCallback ) // TODO: rmeove
                            .build();

            }

        return INSTANCE;

    }

    /// TODO: remove ////////////////
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate( @NonNull SupportSQLiteDatabase database ) {

            super.onCreate( database );
            new PopulateDbAsyncTask( INSTANCE ).execute();

        }

    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private PostDao postDao;
        private ReactionDao reactionDao;

        private PopulateDbAsyncTask( HoplyDB database ) {

            postDao = database.postDao();
            reactionDao = database.reactionDao();

        }

        @Override
        protected Void doInBackground( Void... voids ) {

            postDao.insert( new Post( "disco", "Content 1" ) );
            postDao.insert( new Post( "disco", "Content 2" ) );
            postDao.insert( new Post( "disco", "Content 3" ) );

            reactionDao.insert( new Reaction( "disco", 0, 0 ) );
            reactionDao.insert( new Reaction( "disco", 0, 0 ) );
            reactionDao.insert( new Reaction( "disco", 0, 0 ) );

            return null;

        }

    }
    /// TODO ends ///////////////////////

}
