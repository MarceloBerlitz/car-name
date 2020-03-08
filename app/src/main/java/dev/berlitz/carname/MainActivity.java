package dev.berlitz.carname;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import dev.berlitz.carname.fragment.MyCarsFragment;
import dev.berlitz.carname.fragment.NewCarFragment;

public class MainActivity extends AppCompatActivity  {

    private Button newCarButton, myCarsButton;
    private Integer currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_main);

        newCarButton = findViewById(R.id.newCarButton);
        myCarsButton = findViewById(R.id.myCarsButton);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameIn, new NewCarFragment());
        transaction.commit();
        createListeners();
    }

    private void createListeners() {
        myCarsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 == currentPage) return;
                currentPage = 0;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameIn, new MyCarsFragment());
                transaction.commit();
            }
        });

        newCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (1 == currentPage) return;
                currentPage = 1;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameIn, new NewCarFragment());
                transaction.commit();
            }
        });
    }
}
