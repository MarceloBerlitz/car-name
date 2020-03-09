package dev.berlitz.carname.fragment.mycars;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.berlitz.carname.R;

public class CarsViewHolder extends RecyclerView.ViewHolder {

    TextView make, model, year, confidence;
    ImageView picture;

    public CarsViewHolder(@NonNull View itemView) {
        super(itemView);
        make = itemView.findViewById(R.id.make);
        model = itemView.findViewById(R.id.model);
        year = itemView.findViewById(R.id.year);
        confidence = itemView.findViewById(R.id.confidence);
        picture = itemView.findViewById(R.id.picture);
    }
}
