package xyz.discobiscuit.hoplyfork.database;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(
    entity = User.class,
    parentColumns = "id",
    childColumns = "user_id",
    onDelete = ForeignKey.CASCADE
))
public class Post {

    @PrimaryKey( autoGenerate = true )
    public int id;

    @ColumnInfo( name = "user_id", index = true )
    public String userId;

    @ColumnInfo( name = "content" )
    public String content;

    @ColumnInfo( name = "stamp" )
    public String stamp;

    public Post( String userId, String content, String stamp ) {

        this.userId = userId;
        this.content = content;
        this.stamp = stamp;

    }

}
