package dev.berlitz.carname;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import dev.berlitz.carname.fragment.MyCarsFragment;
import dev.berlitz.carname.fragment.NewCarFragment;

public class MainActivity extends AppCompatActivity  {

    private Button newCarButton, myCarsButton;

    private MyCarsFragment myCarsFragment = new MyCarsFragment();
    private NewCarFragment newCarFragment = new NewCarFragment();
    private Fragment active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_main);

        newCarButton = findViewById(R.id.newCarButton);
        myCarsButton = findViewById(R.id.myCarsButton);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        active = newCarFragment;
        transaction.add(R.id.frameIn, myCarsFragment).add(R.id.frameIn, newCarFragment);
        transaction.hide(myCarsFragment).show(newCarFragment);
        transaction.commit();
        createListeners();
    }

    private void createListeners() {
        myCarsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.show(myCarsFragment).hide(active);
                active = myCarsFragment;
                transaction.commit();
            }
        });

        newCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.show(newCarFragment).hide(active);
                active = newCarFragment;
                transaction.commit();
            }
        });
    }
}
