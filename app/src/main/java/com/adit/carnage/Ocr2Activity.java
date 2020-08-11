package com.adit.carnage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class Ocr2Activity extends AppCompatActivity {

    // INI KELAS OCR ACTIVITY YANG PAKAI CAMERA BAWAAN INTENT

    @BindView(R.id.ivPreviewFoto)
    ImageView ivPreviewFoto;

    @BindView(R.id.tvUTFText)
    TextView tvUTFText;

    @BindView(R.id.tvNoPhoto)
    TextView tvNoPhoto;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private String currentPhotoPath;

    public static void startActivity(BaseActivity source){
        Intent i = new Intent(source, Ocr2Activity.class);
        source.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr2);
        ButterKnife.bind(this);
        ivPreviewFoto.setVisibility(View.GONE);
        tvNoPhoto.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnTakePic)
    public void takePicture(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName()+".provider", photoFile);

        if(photoFile != null){
            tvNoPhoto.setVisibility(View.GONE);
            ivPreviewFoto.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(i, 0);
        }
    }

//    public void takePicture(){
//        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(i, 0);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Bitmap bmp = (Bitmap) data.getExtras().get("data");
//        ivPreviewFoto.setImageBitmap(bmp);
//        ocr(bmp);
        progressBar.setVisibility(View.GONE);

        if(requestCode == 0 && resultCode == Activity.RESULT_OK){
            if(currentPhotoPath != null){
                ivPreviewFoto.setVisibility(View.VISIBLE);
                setPhotoToIv();
//                addPhotoToGallery();
            }else{
                Toast.makeText(getApplicationContext(),"Photo path is null",Toast.LENGTH_LONG ).show();
                tvNoPhoto.setText("Photo path is null");
                tvNoPhoto.setVisibility(View.VISIBLE);
            }

        }
    }

    public void ocr(Bitmap bmp){
        TessBaseAPI tessBaseAPI = new TessBaseAPI();

        //String path = Environment.getDataDirectory().getPath();
        //String path1 = getFilesDir().getAbsolutePath() + "/tesseract/tessdata/";
        //String path2 = Environment.getExternalStorageDirectory() + "/tesseract/";
        String path3 = getExternalFilesDir(null).getAbsolutePath() + "/tesseract/";
        //String path4 = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/tesseract/";

        //System.out.println("FUCKING PATH 0 IS : " + path);
        //System.out.println("FUCKING PATH 1 IS : " + path1);
        //System.out.println("FUCKING PATH 2 IS : " + path2);

        // this one is used
//        System.out.println("FUCKING PATH 3 IS : " + path3);


        //System.out.println("FUCKING PATH 4 IS : " + path4);

        tessBaseAPI.init( path3 , "eng");
        tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
        tessBaseAPI.setImage(bmp);

        String result = tessBaseAPI.getUTF8Text();

        System.out.println("OCR RESULT = " + result);
        tvUTFText.setText(result);
    }

    public File saveFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPG-" + timeStamp;
        String path = getExternalFilesDir(null).getAbsolutePath() + "/tesseract/";
        String path2 = Environment.getExternalStorageDirectory() + "/tesseract/";
        File newPath = new File(path);

        File image = File.createTempFile(imageFileName, ".jpg", newPath);

        //asdasd
//        File newFile = new File(path);
//        FileOutputStream out = new FileOutputStream(newFile);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPhotoToIv(){
        int targetWidth = ivPreviewFoto.getWidth();
        int targetHeight = ivPreviewFoto.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoWidth = bmOptions.outWidth;
        int photoHeight = bmOptions.outHeight;

        int scaleFactor = 2;
        if(targetWidth > 0 && targetHeight >0){
            scaleFactor = Math.max(photoWidth / targetWidth, photoHeight / targetHeight);
        }
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Matrix matrix = new Matrix();
        matrix.postRotate(getRotation());

//        Rect rect = new Rect(-50, -25, 150, 75);
//        assert(rect.left < rect.right && rect.top < rect.bottom);

        Bitmap bmp = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        bmp = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(), bmp.getHeight(), matrix, false);
//        bmp = Bitmap.createBitmap(bmp,rect.right-rect.left,rect.bottom-rect.top,200, 30, matrix, false);
        ivPreviewFoto.setImageBitmap(bmp);
        ocr(bmp);
    }

    // jgn dipake
    private void addPhotoToGallery(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    private float getRotation() {
        try {
            ExifInterface ei = new ExifInterface(currentPhotoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch(orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90f;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180f;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270f;
                default:
                    return 0f;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0f;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
