package xyz.discobiscuit.hoplyfork.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Reaction {

    @ColumnInfo( name = "user_id" )
    public String userId;

    @ColumnInfo( name = "post_id" )
    public int postId;

    @ColumnInfo( name = "type" )
    public int type;

    @PrimaryKey
    @ColumnInfo( name = "stamp" )
    public double stamp;

    public Reaction( String userId, int postId, int type ) {

        this.userId = userId;
        this.postId = postId;
        this.type = type;
        this.stamp = System.currentTimeMillis();

    }

}
