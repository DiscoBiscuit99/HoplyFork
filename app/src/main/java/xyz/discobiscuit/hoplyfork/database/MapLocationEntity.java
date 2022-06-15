package xyz.discobiscuit.hoplyfork.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;

@Entity (foreignKeys = {@ForeignKey(entity = Post.class,
        parentColumns = "id",
        childColumns = "post_id",
        onDelete = ForeignKey.CASCADE)
})
public class MapLocationEntity {

    @PrimaryKey
    @ColumnInfo(name = "post_id", index = true)
    public int post_id;

    @ColumnInfo(name = "lang")
    public double lang;

    @ColumnInfo(name = "longi")
    public double longi;

    public MapLocationEntity(int post_id, double lang, double longi) {
        this.post_id = post_id;
        this.lang = lang;
        this.longi = longi;
    }
}
