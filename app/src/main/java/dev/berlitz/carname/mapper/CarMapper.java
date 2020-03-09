package dev.berlitz.carname.mapper;

import dev.berlitz.carname.database.Car;
import dev.berlitz.carname.model.CarModel;

public class CarMapper {
    public static Car mapFrom(CarModel carModel) {
        Car car = new Car();
        car.body_style = carModel.getBody_style();
        car.confidence = carModel.getConfidence();
        car.make = carModel.getMake();
        car.model = carModel.getModel();
        car.model_year = carModel.getModel_year();
        car.picture = carModel.getPicture();
        return car;
    }
}
