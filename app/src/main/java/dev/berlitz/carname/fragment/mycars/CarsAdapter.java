package dev.berlitz.carname.fragment.mycars;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.berlitz.carname.R;
import dev.berlitz.carname.model.CarModel;

public class CarsAdapter extends RecyclerView.Adapter<CarsViewHolder> {

    private List<CarModel> cars;

    public CarsAdapter(List<CarModel> cars) {
        this.cars = cars;
    }

    @NonNull
    @Override
    public CarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_car_view, parent, false);
        return new CarsViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull CarsViewHolder holder, int position) {
        CarModel car = cars.get(position);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(car.getPicture(), options);

        if (bitmap != null) {
            holder.make.setText(car.getMake());
            holder.model.setText(car.getModel());
            holder.year.setText(car.getModel_year());
            holder.confidence.setText(car.getConfidence());
            holder.picture.setImageBitmap(bitmap);
        }

    }

    @Override
    public int getItemCount() {
        return cars.size();
    }
}
