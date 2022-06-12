package xyz.discobiscuit.hoplyfork.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDao {

    @Insert
    void insert( Post post );

    @Delete
    void delete( Post post );

    @Query( "SElECT * FROM post WHERE id LIKE :id LIMIT 1" )
    Post findById( int id );

    @Query( "SELECT * FROM post" )
    LiveData<List<Post>> getAll();

    @Query( "DELETE FROM post" )
    void deleteAll();

}
