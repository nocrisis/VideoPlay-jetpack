package com.catherine.libnetwork.cache;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.catherine.libcommon.AppGlobals;

@Database(entities = {Cache.class}, version = 1, exportSchema = true)
//通过注解实现相关的功能，在编译时通过annotationProcess来实现相关类，抽象方法运行时生成实现这些方法的类，就不用再复写RoomDatabase几个默认的方法了
public abstract class CacheDatabase extends RoomDatabase {
    private static final CacheDatabase database;

    static {
        //创建一个内存数据库
        //但是这种数据库的数据只存在于内存中，也就是进程被杀之后，数据随之丢失
//        Room.inMemoryDatabaseBuilder()
        database = Room.databaseBuilder(AppGlobals.getApplication(), CacheDatabase.class, "video_play_cache")
                .allowMainThreadQueries()
//        数据库创建和打开后的回调
//        .addCallback();
//        .setQueryExecutor()
//        .openHelperFactory()
//        room的日志模式
//        .setJournalMode()
//        数据库升级异常之后的回滚
//        .fallbackToDestructiveMigration()
//        .addMigrations(CacheDatabase.sMigration);
                .build();
    }

    //版本更新
    static Migration sMigration = new Migration(1, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table teacher rename to student");
            database.execSQL("alter table teacher add column teacher_age INTEGER NOT NULL default 0");
        }
    };

    public abstract CacheDao getCache();

    public static CacheDatabase get() {
        return database;
    }
}
