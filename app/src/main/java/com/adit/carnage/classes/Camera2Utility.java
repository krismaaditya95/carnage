package com.adit.carnage.classes;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.CamcorderProfile;
import android.media.FaceDetector;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

//import com.akag.tessocrkrismaaditya.R;
//import com.akag.tessocrkrismaaditya.fragments.FaceDetectionFragmentDirections;
//import com.akag.tessocrkrismaaditya.fragments.TensorFlowFragment;
//import com.akag.tessocrkrismaaditya.fragments.TessFragment2;

import com.adit.carnage.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by Krisma Aditya on 14/05/2020.
 */
public class Camera2Utility implements SurfaceHolder.Callback{

    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;
    private static final int STATE_PREVIEW = 0;
    private static final String VIDEO_DIRECTORY_NAME = "carnage";

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private String mCameraId;
    private SurfaceView surfaceView;
    private TextureView mTextureView;
    private CameraCaptureSession mCaptureSession;
    private CameraDevice mCameraDevice;
    private Size mPreviewSize;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private ImageReader mImageReader;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CaptureRequest mPreviewRequest;
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static final int REQUEST_RECORD_AUDIO = 3;
    private int mState = STATE_PREVIEW;
    private int facesNum;
    private boolean mFaceDetectSupported;
    private int mFaceDetectMode;
    private Fragment fragment;
    private HashMap<Integer , Face> resultmap = new HashMap<>();
    private Handler handler;
    private Runnable runnable;
    private Image image;
    private Size videoSize;
    private MediaRecorder mediaRecorder;
    private Service service;
    private File currentFile;
    private Integer orientationSensor;
    private File mediaStorageDir;

    public Camera2Utility(Service service){
        mediaRecorder = new MediaRecorder();
        //mediaStorageDir = new File(fragment.getActivity().getExternalFilesDir(null), VIDEO_DIRECTORY_NAME);
        this.service = service;
    }

    public Camera2Utility(Fragment fragment, TextureView textureView){
        this.fragment = fragment;
        this.mTextureView = textureView;
        mediaRecorder = new MediaRecorder();
        mediaStorageDir = new File(fragment.getActivity().getExternalFilesDir(null), VIDEO_DIRECTORY_NAME);
    }

    public Camera2Utility(Fragment fragment, TextureView textureView, SurfaceView surfaceView){
        this.fragment = fragment;
        this.mTextureView = textureView;
        this.surfaceView = surfaceView;
    }

    public final TextureView.SurfaceTextureListener mSurfaceTextureListener =
            new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    openCamera(width, height);
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                    configureTransform(width, height);
                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    return true;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                }
            };

    public final CameraDevice.StateCallback mStateCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraOpenCloseLock.release();
            mCameraDevice = camera;
            //createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            mCameraOpenCloseLock.release();
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            mCameraOpenCloseLock.release();
            camera.close();
            mCameraDevice = null;
        }
    };

    private File getOutputVideoMediaFile(){
//        File mediaStorageDir = new File(fragment.getActivity().getExternalFilesDir(null), VIDEO_DIRECTORY_NAME);

        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                Toast.makeText(fragment.getContext(), "Gagal membuat direktori!", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + getFileName() + ".mp4");
        return mediaFile;
    }

    private void setUpMediaRecorder() throws IOException {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); //mp4

        // create video output file
        currentFile = getOutputVideoMediaFile();

        // set output file in media recorder
        mediaRecorder.setOutputFile(currentFile.getAbsolutePath());
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
        mediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
        mediaRecorder.setVideoFrameRate(profile.videoFrameRate);
        mediaRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioEncodingBitRate(profile.audioBitRate);
        mediaRecorder.setAudioSamplingRate(profile.audioSampleRate);

        int rotation = fragment.getActivity().getWindowManager().getDefaultDisplay().getRotation();
        switch(orientationSensor){
            case 90:
                mediaRecorder.setOrientationHint(ORIENTATIONS.get(rotation));
                break;
            case 270:
                mediaRecorder.setOrientationHint(ORIENTATIONS.get(rotation));
                break;
        }
        mediaRecorder.prepare();
    }

    public void startRecording(){
        mediaRecorder.start();
        //Toast.makeText(fragment.getContext(), "Recording...", Toast.LENGTH_SHORT).show();
    }

    public void stopRecording(){
        mediaRecorder.stop();
        mediaRecorder.reset();
        //Toast.makeText(fragment.getContext(), "Recording stopped", Toast.LENGTH_SHORT).show();
    }

    public void prepareRecording(){
        try {
            setUpMediaRecorder();
            Surface recordSurface = mediaRecorder.getSurface();
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            mPreviewRequestBuilder.addTarget(recordSurface);

            mCameraDevice.createCaptureSession(Arrays.asList(recordSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.setRepeatingRequest(mPreviewRequestBuilder.build(), null, null);
                        startRecording();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, null);

        } catch (IOException | CameraAccessException e) {
            e.printStackTrace();
        }

    }

    public void takePicture(){
        if(null == mCameraDevice){
            Log.e("CAMERA 2", "cameraDevice is null");
            return;
        }

        CameraManager manager = (CameraManager) fragment.getActivity().getSystemService(Context.CAMERA_SERVICE);

        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraDevice.getId());
            Size[] jpegSizes = null;
            if(characteristics != null){
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }

            int width = 640;
            int height = 480;
            if(jpegSizes != null && 0 < jpegSizes.length){
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }

            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);

            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(mTextureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            // orientation
            int rotation = fragment.getActivity().getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            // captureBuilder.set(CaptureRequest.JPEG_ORIENTATION,-90);
//            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, 270);

            // nama file dengan tanggal
            Date captureDate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
            String captureDateFormatted = format.format(captureDate);

//            final File file = new File(Environment.getExternalStorageDirectory() + "/CARNAGE_IMG_" + captureDateFormatted + ".jpg");
            final File file = new File(fragment.getActivity().getExternalFilesDir(null) + "/CARNAGE_IMG_" + captureDateFormatted + ".jpg");

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener(){

                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;

                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(image != null){
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };

            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);

            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {

                public void getFaces(CaptureResult result){
                    Integer mode = result.get(CaptureResult.STATISTICS_FACE_DETECT_MODE);
                    Face faces[]= result.get(CaptureResult.STATISTICS_FACES);
                    if(faces != null && mode != null){
                        if(faces.length > 0){
                            Log.d(fragment.getClass().getSimpleName(), "Wajah : " + faces.length + "\n Jumlah Mode : " + mode);
                            Toast.makeText(fragment.getContext(),"Wajah : " + faces.length +"\nMode : " + mode, Toast.LENGTH_LONG).show();
                        }else{
                            Log.d(fragment.getClass().getSimpleName(), "Tidak ada wajah yang terdeteksi !" + mode);
                            Toast.makeText(fragment.getContext(),"Tidak ada wajah yang terdeteksi !" + mode, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                public int getFacesInt(CaptureResult result){
                    Integer mode = result.get(CaptureResult.STATISTICS_FACE_DETECT_MODE);
                    Face faces[]= result.get(CaptureResult.STATISTICS_FACES);

                    int left = 0;
                    int right = 0;
                    int top = 0;
                    int bottom = 0;

                    if(faces != null && mode != null){
                        if(faces.length > 0){

                            for(int i=0; i<faces.length; i++){
                                left = faces[i].getBounds().left;
                                right = faces[i].getBounds().right;
                                top = faces[i].getBounds().top;
                                bottom = faces[i].getBounds().bottom;

                                Toast.makeText(fragment.getContext(),"Wajah : " + faces.length
                                        +"\nMode : " + mode
                                        +"\nLeft : " + left
                                        +"\nRight : " + right
                                        +"\nTop : " + top
                                        +"\nBottom : " + bottom, Toast.LENGTH_LONG).show();
                            }

                            Log.d(fragment.getClass().getSimpleName(), "Wajah : " + faces.length + "\n Jumlah Mode : " + mode);
                            Toast.makeText(fragment.getContext(),"Wajah : " + faces.length + "\nMode : " + mode, Toast.LENGTH_LONG).show();

                        }else{
                            Log.d(fragment.getClass().getSimpleName(), "Tidak ada wajah yang terdeteksi !" + mode);
                            Toast.makeText(fragment.getContext(),"Tidak ada wajah yang terdeteksi !" + mode, Toast.LENGTH_SHORT).show();
                        }
                    }

                    return faces.length;
                }

                public Face[] getFacesArray(CaptureResult result){
                    Integer mode = result.get(CaptureResult.STATISTICS_FACE_DETECT_MODE);
                    Face faces[]= result.get(CaptureResult.STATISTICS_FACES);
                    return faces;
                }

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(fragment.getActivity(), "Foto disimpan :" + file, Toast.LENGTH_LONG).show();
                    //createCameraPreviewSession();
                }

                @Override
                public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                    super.onCaptureProgressed(session, request, partialResult);
                }
            };

            mCameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, mBackgroundHandler);


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void processDetectFace(Face facesArray[]){
        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;

        if(facesArray != null){
            if(facesArray.length > 0){

                for(int i=0; i<facesArray.length; i++){
                    left = facesArray[i].getBounds().left;
                    right = facesArray[i].getBounds().right;
                    top = facesArray[i].getBounds().top;
                    bottom = facesArray[i].getBounds().bottom;

                    resultmap.put(i, facesArray[i]);

                    Toast.makeText(fragment.getContext(),"Wajah : " + facesArray.length
                            +"\nLeft : " + left
                            +"\nRight : " + right
                            +"\nTop : " + top
                            +"\nBottom : " + bottom, Toast.LENGTH_LONG).show();
                }

                //Log.d(fragment.getClass().getSimpleName(), "Wajah : " + facesArray.length + "\n Jumlah Mode : " + mode);
                //Toast.makeText(fragment.getContext(),"Wajah : " + facesArray.length + "\nMode : " + mode, Toast.LENGTH_LONG).show();

            }else{
                Log.d(fragment.getClass().getSimpleName(), "Tidak ada wajah yang terdeteksi !");
                Toast.makeText(fragment.getContext(),"Tidak ada wajah yang terdeteksi !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getFileName(){
        // nama file dengan tanggal
        Date captureDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        String captureDateFormatted = format.format(captureDate);

        return "CARNAGE_" + captureDateFormatted;
    }

    public void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if(null != mCaptureSession){
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if(null != mCameraDevice){
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if(null != mImageReader){
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Timeout waiting to lock camera opening" ,e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    public void goToResult(String uri, int faces, HashMap<Integer, Face> resultmap){
        //NavDirections action = FaceDetectionFragmentDirections.fdFragmentToFDResultFragment(resultmap).setFaces(faces).setUri(uri);
        //NavHostFragment.findNavController(fragment).navigate(action);
    }

    public void openCamera(int width, int height){
        if(ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestCameraPermission();
            return;
        }

        if(ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestWriteFileOnInternalStorage();
            return;
        }

        if(ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            requestRecordAudio();
            return;
        }

        setUpCameraOutputs(width, height);
        configureTransform(width, height);
        CameraManager manager = (CameraManager) fragment.getActivity().getSystemService(Context.CAMERA_SERVICE);

        try {
            if(!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)){
                throw new RuntimeException("Timeout waiting to lock camera opening");
            }
            manager.openCamera(mCameraId, mStateCallBack, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            throw new RuntimeException("Timeout waiting to lock camera opening" ,e);
        }
    }

    public void setUpCameraOutputs(int width, int height) {
        CameraManager manager = (CameraManager) fragment.getActivity().getSystemService(Context.CAMERA_SERVICE);

        try {
            for(String cameraId : manager.getCameraIdList()){
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if(facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT){
                    continue;
                }

//                if(facing != null && facing == CameraCharacteristics.LENS_FACING_BACK){
//                    continue;
//                }
                orientationSensor = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if(map == null){
                    continue;
                }

                Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, 2);

//                Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.YUV_420_888)), new CompareSizesByArea());
//                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.YUV_420_888, 1);

                mImageReader.setOnImageAvailableListener(null, mBackgroundHandler);
                //mImageReader.setOnImageAvailableListener(this, mBackgroundHandler);

                Point displaySize = new Point();
                fragment.getActivity().getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;

                if(maxPreviewWidth > MAX_PREVIEW_WIDTH){
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }
                if(maxPreviewHeight > MAX_PREVIEW_HEIGHT){
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                videoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth, maxPreviewHeight, largest);
                mCameraId = cameraId;
//                mediaRecorder = new MediaRecorder();
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            Toast.makeText(fragment.getActivity(), "CAMERA 2 API IS NOT SUPPORTED on THIS DEVICE", Toast.LENGTH_LONG).show();
        }
    }

    public void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface surface = new Surface(texture);

            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            //mPreviewRequestBuilder.addTarget(mImageReader.getSurface());
            mPreviewRequestBuilder.addTarget(surface);

            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if(null == mCameraDevice){
                        return;
                    }

                    mCaptureSession = session;

                    try {
                        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                        mPreviewRequest = mPreviewRequestBuilder.build();

                        mCaptureSession.setRepeatingRequest(mPreviewRequest, null, mBackgroundHandler);

                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(fragment.getActivity(), "Failed", Toast.LENGTH_LONG);
                }
            }, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setFaceDetect(CaptureRequest.Builder requestBuilder, int faceDetectMode){
        if(mFaceDetectSupported){
            requestBuilder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, faceDetectMode);
        }
    }

    public void requestCameraPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), Manifest.permission.CAMERA)){
            ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }else{
            ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    public void requestWriteFileOnInternalStorage(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }else{
            ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    public void requestRecordAudio(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), Manifest.permission.RECORD_AUDIO)){
            ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }else{
            ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }

    public void startBackgroundThread(){
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    public void stopBackgroundThread(){
        mBackgroundThread.quitSafely();

        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Size chooseVideoSize(Size[] choices){
        for(Size size : choices){
            if(size.getWidth() == 1920 && size.getHeight() == 1080){
                return size;
            }
        }

        for(Size size : choices){
            if(size.getWidth() == size.getHeight() * 4/3 && size.getWidth() <= 1080){
                return size;
            }
        }

        return choices[choices.length - 1];
    }

    public static Size chooseOptimalSize(Size[] choices, int textureViewWidth, int textureViewHeight,
                                         int maxWidth, int maxHeight, Size aspectRatio){
        List<Size> bigEnough = new ArrayList<>();
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for(Size option : choices){
            if(option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w){
                if(option.getWidth() >= textureViewWidth && option.getHeight() >= textureViewHeight){
                    bigEnough.add(option);
                }else{
                    notBigEnough.add(option);
                }
            }
        }

        if(bigEnough.size() > 0){
            return Collections.min(bigEnough, new CompareSizesByArea());
        }else if(notBigEnough.size() > 0){
            return Collections.max(notBigEnough, new CompareSizesByArea());
        }else{
            Log.e("CAMERA 2", " Couldnt find any suitable preview size!");
            return choices[0];
        }
    }

    public void configureTransform(int viewWidth, int viewHeight){
        if(null == mTextureView || null == mPreviewSize){
            return;
        }

        int rotation = fragment.getActivity().getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();

        if(Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation){
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max((float) viewHeight / mPreviewSize.getHeight(), (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }else if(Surface.ROTATION_180 == rotation){
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size o1, Size o2) {
            return Long.signum((long) o1.getWidth() * o1.getHeight() - (long) o2.getWidth() * o2.getHeight());
        }
    }
}
