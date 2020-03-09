package dev.berlitz.carname.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Car {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "model")
    public String model;

    @ColumnInfo(name = "make")
    public String make;

    @ColumnInfo(name = "model_year")
    public String model_year;

    @ColumnInfo(name = "body_style")
    public String body_style;

    @ColumnInfo(name = "confidence")
    public String confidence;

    @ColumnInfo(name = "picture")
    public String picture;
}
