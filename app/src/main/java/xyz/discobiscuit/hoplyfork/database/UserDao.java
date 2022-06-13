package xyz.discobiscuit.hoplyfork.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert( User user );

    @Delete
    void delete( User user );

    @Query( "SElECT * FROM user WHERE id LIKE :id LIMIT 1" )
    User findById( String id );

    @Query( "SELECT * FROM user WHERE name LIKE :name LIMIT 1" )
    User findByUsername( String name );

    @Query( "SELECT * FROM user" )
    List<User> getAll();

    @Query( "DELETE FROM user" )
    void deleteAll();

}
