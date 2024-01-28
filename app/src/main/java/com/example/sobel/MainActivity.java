package com.example.sobel;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.sobel.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.nio.IntBuffer;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'sobel' library on application startup.
    static {
        System.loadLibrary("sobel");
    }

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.sobel.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FloatingActionButton button = binding.selectImageButton;
        imageView = binding.imageView;
        button.setOnClickListener(selectImage);
    }
    ActivityResultCallback<Uri> onGetImage = uri -> {

        ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), uri);
        try {
            Bitmap bitmap = ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.RGBA_F16, true);

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            //create array from bitmap
            int[] image_array = new int[height * width];
            bitmap.getPixels(image_array, 0, width, 0, 0, width, height);

            //to grayscale
            for (int i = 0; i < width * height; i++) {
                int color_value = image_array[i];
                int r = Color.red(color_value);
                int b = Color.blue(color_value);
                int g = Color.green(color_value);

                image_array[i] = (int) (0.299 * r + 0.587 * g + 0.114 * b);
            }

            //call to sobel on JNI
            int[] sobel = sobelJNI(image_array, width, height);

            //to bitmap grayscale
            for (int i = 0; i < width * height; i++) {
                int sobel_value = sobel[i];
                int color_value = Color.argb(0, sobel_value, sobel_value, sobel_value);
                sobel[i] = color_value;
            }

            //array to bitmap
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(IntBuffer.wrap(sobel));

            //set on image view
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    ActivityResultLauncher<String> getImage = registerForActivityResult(new ActivityResultContracts.GetContent(), onGetImage);
    View.OnClickListener selectImage = view -> {
        getImage.launch("image/*");
    };

    /**
     * A native method that is implemented by the 'sobel' native library,
     * which is packaged with this application.
     */
    public native int[] sobelJNI(int[] image_array, int width, int height);
}