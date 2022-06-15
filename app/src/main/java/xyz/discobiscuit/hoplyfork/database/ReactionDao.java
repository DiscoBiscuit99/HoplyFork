package xyz.discobiscuit.hoplyfork.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReactionDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    void insert( Reaction reaction );

    @Delete
    void delete( Reaction reaction );

    @Query( "SELECT * FROM reaction WHERE post_id LIKE :postId" )
    LiveData<List<Reaction>> getReactionsByReactionId( String postId );

    @Query( "SELECT * FROM reaction WHERE user_id LIKE :userId" )
    LiveData<List<Reaction>> getReactionsByUserId( String userId );

    @Query( "SELECT * FROM reaction" )
    LiveData<List<Reaction>> getAll();

    @Query( "SELECT * FROM reaction WHERE type LIKE 1" )
    LiveData<List<Reaction>> getAllLikes();

    @Query( "SELECT * FROM reaction WHERE type LIKE 2" )
    LiveData<List<Reaction>> getAllDislikes();

    @Query( "DELETE FROM reaction" )
    void deleteAll();

}
