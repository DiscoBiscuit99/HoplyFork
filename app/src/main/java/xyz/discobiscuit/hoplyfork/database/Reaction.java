package xyz.discobiscuit.hoplyfork.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    primaryKeys = { "user_id", "post_id", "stamp" },
    foreignKeys = {
        @ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "user_id",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Post.class,
            parentColumns = "id",
            childColumns = "post_id",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class Reaction {

    @NonNull
    @ColumnInfo( name = "user_id", index = true )
    public String userId;

    @NonNull
    @ColumnInfo( name = "post_id", index = true )
    public int postId;

    @ColumnInfo( name = "type" )
    public int type;

    @NonNull
    @ColumnInfo( name = "stamp" )
    public String stamp;

    public Reaction( String userId, int postId, int type, String stamp ) {

        this.userId = userId;
        this.postId = postId;
        this.type = type;
        this.stamp = stamp;

    }

    public static Reaction newLike( String userId, int postId ) {
        return new Reaction( userId, postId, 1, "time" );
    }

    public static Reaction newDislike( String userId, int postId ) {
        return new Reaction( userId, postId, 2, "time" );
    }

}
