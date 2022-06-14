package xyz.discobiscuit.hoplyfork.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity( primaryKeys = { "user_id", "post_id", "stamp" } )
public class Reaction {

    @NonNull
    @ColumnInfo( name = "user_id" )
    public String userId;

    @NonNull
    @ColumnInfo( name = "post_id" )
    public int postId;

    @ColumnInfo( name = "type" )
    public int type;

    @ColumnInfo( name = "stamp" )
    public double stamp;

    public Reaction( String userId, int postId, int type ) {

        this.userId = userId;
        this.postId = postId;
        this.type = type;
        this.stamp = System.currentTimeMillis();

    }

}
