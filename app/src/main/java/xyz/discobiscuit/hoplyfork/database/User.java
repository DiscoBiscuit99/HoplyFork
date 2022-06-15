package xyz.discobiscuit.hoplyfork.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey
    @NonNull
    @ColumnInfo( name = "id" )
    public String id;

    @ColumnInfo( name = "name" )
    public String name;

    @ColumnInfo( name = "stamp" )
    public String stamp;

    public User( String id, String name, String stamp ) {

        this.id = id;
        this.name = name;
        this.stamp = stamp;

    }

}
