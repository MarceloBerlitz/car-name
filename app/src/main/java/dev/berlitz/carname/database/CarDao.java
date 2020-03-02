package dev.berlitz.carname.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CarDao {
    @Query("SELECT * FROM car")
    List<Car> getAll();

    @Query("SELECT * FROM car WHERE uid IN (:carIds)")
    List<Car> loadAllByIds(int[] carIds);

    @Query("SELECT * FROM car WHERE make LIKE :make AND " +
            "model LIKE :model LIMIT 1")
    Car findByName(String make, String model);

    @Insert
    void insertAll(Car... cars);

    @Delete
    void delete(Car car);
}
