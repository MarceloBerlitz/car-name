package dev.berlitz.carname.fragment.mycars;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import dev.berlitz.carname.R;
import dev.berlitz.carname.database.AppDatabase;
import dev.berlitz.carname.database.Car;
import dev.berlitz.carname.mapper.CarModelMapper;
import dev.berlitz.carname.model.CarModel;

public class MyCarsFragment extends Fragment {

    private RecyclerView recyclerView;

    private static List<CarModel> mapCars(List<Car> cars) {
        List<CarModel> carModels = new ArrayList<CarModel>();
        for(int i = 0; i < cars.size(); i++) {
            carModels.add(CarModelMapper.mapFrom(cars.get(i)));
        }
        return carModels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_cars, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        final ArrayList<CarModel> carModels = new ArrayList<CarModel>();
        final CarsAdapter adapter = new CarsAdapter(carModels);
        recyclerView.setAdapter(adapter);

        Thread temp = new Thread() {
            @Override
            public void run() {
                AppDatabase db = Room.databaseBuilder(getContext(),
                        AppDatabase.class, "my-database").build();
                final List<Car> cars = db.carDao().getAll();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (cars.size() > 0) {
                            carModels.addAll(mapCars(cars));
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        };

        temp.start();
        return view;
    }
}
