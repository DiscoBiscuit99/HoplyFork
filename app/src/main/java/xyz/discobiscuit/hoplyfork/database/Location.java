package xyz.discobiscuit.hoplyfork.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Location {
    @PrimaryKey
    @ColumnInfo(name = "Post_id")
    public int id;

    @ColumnInfo(name = "Lang")
    public double lang;

    @ColumnInfo(name = "Longi")
    public double longi;

    public Location(int id, double lang, double longi) {
        this.id = id;
        this.lang = lang;
        this.longi = longi;
    }
}
