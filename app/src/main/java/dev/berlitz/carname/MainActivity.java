package dev.berlitz.carname;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import dev.berlitz.carname.converters.PathConverter;
import dev.berlitz.carname.integration.response.CarMakeAndModel;
import dev.berlitz.carname.integration.response.CarResponse;

public class MainActivity extends AppCompatActivity implements AsyncTaskHandler<Void, CarResponse[]> {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_PICK_IMAGE = 2;
    public static final String DATA_IMAGE_JPEG_BASE_64 = "data:image/jpeg;base64,";

    private ImageView imageView;
    private ProgressBar progressBar;

    private TextView marca;
    private TextView modelo;
    private TextView ano;
    private TextView certeza;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

        marca = findViewById(R.id.marca);
        modelo = findViewById(R.id.modelo);
        ano = findViewById(R.id.ano);
        certeza = findViewById(R.id.certeza);
    }


    public void takePicture(View view) {
        dispatchTakePictureIntent();
    }

    public void selectPicture(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        "dev.berlitz.carname.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    handleImage(currentPhotoPath);
                    break;
                case REQUEST_PICK_IMAGE:
                    handleImage(PathConverter.convertMediaUriToPath(getApplicationContext(), data.getData()));
                    break;
            }
        }
    }



    private void handleImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        if(hasStoragePermission()) { //Ta bugado
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            CarMakeAndModel carMakeAndModel = new CarMakeAndModel(this);
            carMakeAndModel.execute(DATA_IMAGE_JPEG_BASE_64 + getImageBase64(bitmap));
            imageView.setImageBitmap(bitmap);
        } else {
            requestStoragePermission();
            handleImage(path);
        }
    }

    private boolean hasStoragePermission() {
        return !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
    }

    private String getImageBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void handleProgress(Void... progress) {

    }

    @Override
    public void handlePostExecute(CarResponse[] carResponse) {
        progressBar.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
        marca.setText(carResponse[0].getMake());
        modelo.setText(carResponse[0].getModel());
        ano.setText(carResponse[0].getModel_year());
        certeza.setText(formatPercent(Double.parseDouble(carResponse[0].getConfidence()), 0));
    }

    public static String formatPercent(double done, int digits) {
        DecimalFormat percentFormat = new DecimalFormat("0.0%");
        percentFormat.setDecimalSeparatorAlwaysShown(false);
        percentFormat.setMinimumFractionDigits(digits);
        percentFormat.setMaximumFractionDigits(digits);
        return percentFormat.format(done);
    }

    @Override
    public void handlePreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void handleError(Exception ex) {
        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
    }
}
