package xyz.discobiscuit.hoplyfork.database;

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
    public String user_id;

    @ColumnInfo( name = "content" )
    public String content;

    @ColumnInfo( name = "stamp" )
    public String stamp;

    public Post( String user_id, String content, String stamp ) {

        this.user_id = user_id;
        this.content = content;
        this.stamp = stamp;

    }

}
