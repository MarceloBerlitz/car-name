package dev.berlitz.carname.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

import dev.berlitz.carname.R;
import dev.berlitz.carname.integration.response.CarResponse;

import static dev.berlitz.carname.shared.PercentageUtil.formatPercent;

public class CarViewFragment extends Fragment {

    private ImageView imageView;

    private TextView marca;
    private TextView modelo;
    private TextView ano;
    private TextView certeza;

    private CarResponse car;
    private Bitmap image;

    public CarViewFragment() {

    }

    public CarViewFragment(CarResponse car, Bitmap image) {
        this.car = car;
        this.image = image;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_car_view, container, false);

        imageView = view.findViewById(R.id.picture);

        marca = view.findViewById(R.id.make);
        modelo = view.findViewById(R.id.model);
        ano = view.findViewById(R.id.year);
        certeza = view.findViewById(R.id.confidence);

        if (car != null && image != null) {
            marca.setText(car.getMake());
            modelo.setText(car.getModel());
            ano.setText(car.getModel_year());
            certeza.setText(formatPercent(Double.valueOf(car.getConfidence()), 0));
            imageView.setImageBitmap(image);
        }

        return view;
    }
}
