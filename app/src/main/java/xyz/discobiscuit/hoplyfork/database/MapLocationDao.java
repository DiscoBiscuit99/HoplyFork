package xyz.discobiscuit.hoplyfork.database;

import androidx.room.Dao;
import androidx.room.ForeignKey;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MapLocationDao {

    @Insert
    void insert(MapLocationEntity mapLocationEntity);

    @Query("SELECT * FROM MapLocationEntity WHERE :post_id = :post_id LIMIT 1")
    MapLocationEntity findById(int post_id);
}
