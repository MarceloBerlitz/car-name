package dev.berlitz.carname;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import dev.berlitz.carname.fragment.mycars.MyCarsFragment;
import dev.berlitz.carname.fragment.NewCarFragment;

public class MainActivity extends AppCompatActivity  {

    private Button newCarButton, myCarsButton;

    private MyCarsFragment myCarsFragment;
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
        transaction.add(R.id.frameIn, newCarFragment);
        transaction.commit();
        active = newCarFragment;

        newCarButton.setBackgroundColor(getColor(R.color.colorPrimary2));
        createListeners();
    }

    private void createListeners() {
        myCarsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myCarsFragment == active) return;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                myCarsFragment = new MyCarsFragment();
                transaction.add(R.id.frameIn, myCarsFragment).hide(active).commit();
                active = myCarsFragment;

                myCarsButton.setBackgroundColor(getColor(R.color.colorPrimary2));
                newCarButton.setBackgroundColor(getColor(R.color.colorPrimary));
            }
        });

        newCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newCarFragment == active) return;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(myCarsFragment).show(newCarFragment).commit();

                active = newCarFragment;

                newCarButton.setBackgroundColor(getColor(R.color.colorPrimary2));
                myCarsButton.setBackgroundColor(getColor(R.color.colorPrimary));
            }
        });
    }
}
