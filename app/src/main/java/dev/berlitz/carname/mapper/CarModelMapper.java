package dev.berlitz.carname.mapper;

import dev.berlitz.carname.database.Car;
import dev.berlitz.carname.model.CarModel;

public class CarModelMapper {
    public static CarModel mapFrom(Car car) {
        CarModel carModel = new CarModel();
        carModel.setUid(car.uid);
        carModel.setBody_style(car.body_style);
        carModel.setConfidence(car.confidence);
        carModel.setMake(car.make);
        carModel.setModel(car.model);
        carModel.setModel_year(car.model_year);
        carModel.setPicture(car.picture);
        return carModel;
    }
}
