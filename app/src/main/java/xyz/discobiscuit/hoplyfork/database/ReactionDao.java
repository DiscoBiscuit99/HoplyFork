package xyz.discobiscuit.hoplyfork.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReactionDao {

    @Insert
    void insert( Reaction reaction );

    @Delete
    void delete( Reaction reaction );

    @Query( "SELECT * FROM reaction WHERE post_id LIKE :postId" )
    LiveData<List<Reaction>> findByPostId( String postId );

    @Query( "SELECT * FROM reaction" )
    LiveData<List<Reaction>> getAll();

}
