package dev.berlitz.carname.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { Car.class }, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CarDao carDao();
}