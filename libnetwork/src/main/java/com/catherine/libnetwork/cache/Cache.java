package com.catherine.libnetwork.cache;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Entity(tableName = "cache"
//        ,foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "id", childColumns = "key",onDelete = ForeignKey.RESTRICT,onUpdate = ForeignKey.SET_DEFAULT)}
//        indices = {@Index(value = {"key", "id"})}
)
public class Cache implements Serializable {
    @PrimaryKey
    @NotNull
    public String key;
    //    @ColumnInfo(name = "data")
    public byte[] data;
    /*@Relation(entity = User.class, parentColumn = "id", entityColumn = "id", projection = "")
    public User mUser;*/

    /*@TypeConverters(value = {DateConverter.class})
    public Date mDate;*/
    /*//字段前面加前缀和自身的同名字段区分
    @Embedded(prefix = "user_")
    public User account;*/

}
