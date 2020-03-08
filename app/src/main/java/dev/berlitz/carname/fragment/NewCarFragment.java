package dev.berlitz.carname.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import dev.berlitz.carname.AsyncTaskHandler;
import dev.berlitz.carname.R;
import dev.berlitz.carname.converters.PathConverter;
import dev.berlitz.carname.integration.response.CarMakeAndModel;
import dev.berlitz.carname.integration.response.CarResponse;

public class NewCarFragment extends Fragment implements AsyncTaskHandler<Void, CarResponse[]> {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private static final String DATA_IMAGE_JPEG_BASE_64 = "data:image/jpeg;base64,";
    private ProgressBar progressBar;
    private String currentPhotoPath;

    private Bitmap currentImage;

    private Button camera, gallery;

    private CarViewFragment carViewFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_car, container, false);

        progressBar = new ProgressBar(getActivity().getApplicationContext());//view.findViewById(R.id.progressBar);
        camera = view.findViewById(R.id.camera);
        gallery = view.findViewById(R.id.gallery);

        addEventListeners();

        carViewFragment = new CarViewFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameIn, carViewFragment);
        fragmentTransaction.commit();
        return view;
    }

    private void addEventListeners() {
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasStoragePermission()) {
                    dispatchTakePictureIntent();
                } else {
                    requestStoragePermission();
                    onClick(v);
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasStoragePermission()) {
                    dispatchSelectPictureIntent();
                } else {
                    requestStoragePermission();
                    onClick(v);
                }
            }
        });
    }

    private void dispatchSelectPictureIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                toast("dispatchTakePictureIntent: N deu");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                        "dev.berlitz.carname.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void toast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    handleImage(currentPhotoPath);
                    break;
                case REQUEST_PICK_IMAGE:
                    handleImage(PathConverter.convertMediaUriToPath(getActivity().getApplicationContext(), data.getData()));
                    break;
            }
        } else {
            toast("onActivityResult: N deu :T");
        }
    }


    private void handleImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        if (bitmap != null) {
            currentImage = bitmap;
//            imageView.setImageBitmap(bitmap);
            CarMakeAndModel carMakeAndModel = new CarMakeAndModel(this);
            carMakeAndModel.execute(DATA_IMAGE_JPEG_BASE_64 + getImageBase64(bitmap));
//            if (!new File(path).delete()) {
//                toast("Erro ao excluir foto."); //Necessário validar se é da câmera
//            }
        } else {
            toast("Erro ao recuperar foto");
        }
    }

    private boolean hasStoragePermission() {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private String getImageBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        carViewFragment = new CarViewFragment(carResponse[0], currentImage);
        fragmentTransaction.replace(R.id.frameIn, carViewFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void handlePreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.remove(carViewFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void handleError(Exception ex) {
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity().getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
    }
}
