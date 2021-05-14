package com.code.travellog.ai;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.ai.aiboost.AiBoostInterpreter;
import com.code.travellog.util.StringUtil;
import com.mvvm.event.LiveBus;

import org.tensorflow.lite.gpu.GpuDelegate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;

/**
 * @description: AIBoost 管理器  
 * @date: 2021/3/11
 */
public class AiBoostYoloV5Classifier {
    private static final int DIM_BATCH_SIZE = 1;
    private static final int DIM_PIXEL_SIZE = 3;
    private static final int BYTE_NUM_PER_CHANNEL = 1;
    private static final int IMAGE_SIZE_X = 320;
    private static final int IMAGE_SIZE_Y = 320;
    private static final int IMAGE_MEAN = 0;
    private static final float IMAGE_STD = 255.0f;
    private static final int FILTER_STAGES = 3;
    private int NUM_OF_LABELS;
    private static final float FILTER_FACTOR = 0.4f;
    private String LABEL_PATH;
    private static final int RESULTS_TO_SHOW = 3;
    public static String EVENT_KEY_OBJECTLIST = null;
    public static String EVENT_KEY_OBJECTTEST = null;
    private Semaphore semaphore;
    private Bitmap bmp = null;
    private Activity activity ;
    private final static String TAG = "AiBoostDemo";
    private ByteBuffer imgData = null;
    private int[] intValues = new int[IMAGE_SIZE_X * IMAGE_SIZE_Y];
    private List<String> labelList;
    private float[] probabilityList;
    private final float[][] filterLabelProbArray = null;
    private byte[][] labelProbArray = null;
    AiBoostInterpreter.Options options = null;
    public AiBoostInterpreter aiboost = null;
    private ByteBuffer modelbuf ;
    private AssetManager assetManager;
    private int[][] input_shapes;
    private List<Data> result;
    private String modelPath;
    private int Turn ;
    private AsyncTask task;
    private int Total ;
    public static class Data{
        Data(String type,Float value){
            this.type = type ;
            this.value = value;
        }
        public String type;
        public Float value;
    }
    public AiBoostYoloV5Classifier() {
        if(EVENT_KEY_OBJECTLIST == null ) EVENT_KEY_OBJECTLIST = StringUtil.getEventKey();
        if(EVENT_KEY_OBJECTTEST == null ) EVENT_KEY_OBJECTTEST = StringUtil.getEventKey();

    }

    public static AiBoostYoloV5Classifier newInstance() {
        return new AiBoostYoloV5Classifier();
    }

    public List <Data> getResult() {
        return result;
    }

    public void setResultEmpty() {
        if(result != null) result.clear();
    }

    private PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });

    public void setTotal(int total) {
        Total = total;
    }

    public void initialize(Activity activity, String modelPath, int NUM_OF_LABELS, String LABEL_PATH)
    {
        this.activity = activity ;
        this.LABEL_PATH = LABEL_PATH ;
        this.NUM_OF_LABELS =  NUM_OF_LABELS ;
        this.modelPath = modelPath ;

        labelProbArray = new byte[1][NUM_OF_LABELS];
        semaphore = new Semaphore(1);
        result = new ArrayList<>();
        Turn  = 0 ;
        try{
            labelList = loadLabelList(activity);
            probabilityList = new float[NUM_OF_LABELS];
        }catch (IOException e) { e.printStackTrace();}
        try {
            Context context = activity.getApplicationContext();
            assetManager = context.getAssets();
            options = new AiBoostInterpreter.Options();
            options.setNumThreads(1);
            GpuDelegate.Options gpu_options = new GpuDelegate.Options();
            gpu_options.setPrecisionLossAllowed(true); // It seems that the default is true
            gpu_options.setInferencePreference(GpuDelegate.Options.INFERENCE_PREFERENCE_SUSTAINED_SPEED);
            GpuDelegate gpuDelegate = new GpuDelegate(gpu_options);
//            options(d.gpuDelegate);
            options.setDeviceType(AiBoostInterpreter.Device.CPU);
            options.setQComPowerLevel(AiBoostInterpreter.QCOMPowerLEVEL.QCOM_TURBO);
            options.setNativeLibPath(context.getApplicationInfo().nativeLibraryDir);
            Log.e(TAG, "NativeLibPath = " + context.getApplicationInfo().nativeLibraryDir);
            input_shapes = new int[][]{{DIM_BATCH_SIZE, IMAGE_SIZE_Y, IMAGE_SIZE_X, DIM_PIXEL_SIZE}};
        }catch (Exception e){e.printStackTrace();}
    }
    public void run(Bitmap bmp) throws InterruptedException {

        new Thread(){
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    InputStream input = assetManager.open(modelPath);
                    int length = input.available();
                    byte[] buffer = new byte[length];
                    input.read(buffer);
                    modelbuf = ByteBuffer.allocateDirect(length);
                    modelbuf.order(ByteOrder.nativeOrder());
                    modelbuf.put(buffer);
                    aiboost = new AiBoostInterpreter(modelbuf, input_shapes, options);
                    imgData = aiboost.getInputTensor(0);
                    imgData.order(ByteOrder.nativeOrder());
                    sortedLabels.clear();
                    labelProbArray = new byte[1][NUM_OF_LABELS];
                    task = new ComputeTask().execute(bmp);
                }catch(FileNotFoundException var6){

                        Log.d(TAG, var6.toString());
                        throw new RuntimeException(var6);
                } catch (IOException var7) {
                        Log.d(TAG, var7.toString());
                        throw new RuntimeException(var7);
                }

            }
        }.start();
    }
    /** Reads label list from Assets. */
    private List<String> loadLabelList(Activity activity) throws IOException {
        List<String> labelList = new ArrayList<String>();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(activity.getAssets().open(LABEL_PATH)));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }
    @SuppressLint("StaticFieldLeak")
    private class ComputeTask extends AsyncTask<Bitmap, Void, String> {
        @Override
        protected String doInBackground(Bitmap... inputs) {
            // Reusing the same prepared model with different inputs.
            return classifyFrame(inputs[0]);
        }
        @Override
        protected void onPostExecute(String result) {

            Log.w(TAG+"PostExecute result ",Turn + result + Total);
            semaphore.release();
            Turn ++;
            if(aiboost!= null) aiboost.close();
            if (Turn == Total - 1 || Total == 1 ) LiveBus.getDefault().postEvent(EVENT_KEY_OBJECTLIST,getResult());
        }
    }
    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // Convert the image to floating point.
        int pixel = 0;
        long startTime = SystemClock.uptimeMillis();
        for (int i = 0; i < IMAGE_SIZE_X; ++i) {
            for (int j = 0; j < IMAGE_SIZE_Y; ++j) {
                final int pixelValue = intValues[pixel++];
                imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
            }
        }
        long endTime = SystemClock.uptimeMillis();
        Log.d(TAG, "Image data buffer size: " + imgData.position());
        Log.d(TAG, "Timecost to put values into ByteBuffer: " + Long.toString(endTime - startTime));
    }
    private void addPixelValue(int pixelValue) {
//        imgData.put((byte) ((pixelValue >> 16) & 0xFF));
//        imgData.put((byte) ((pixelValue >> 8) & 0xFF));
//        imgData.put((byte) (pixelValue & 0xFF));
        imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
        imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
        imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
    }

    @SuppressLint("DefaultLocale")
    private String printTopKLabels() {

        for (int i = 0; i < NUM_OF_LABELS; ++i) {
            sortedLabels.add(
                    new AbstractMap.SimpleEntry<>(labelList.get(i), probabilityList[i]));
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        String textToShow = "";
        final int size = sortedLabels.size();
        float maxprobability = 0 ;
        for (int i = 0; i < 3; ++i) {
            Map.Entry<String, Float> label = sortedLabels.poll();
            assert label != null;
            if (label.getValue() > maxprobability) {
                maxprobability = label.getValue();
                textToShow = String.format("\n%s : %4.2f", label.getKey(), maxprobability);
            }
            assert label != null;
            result.add(new Data(label.getKey(),label.getValue()));
            Log.w("textToShow grg",label.getKey() + " " +String.valueOf(label.getValue()));
        }
        Log.w("textToShow",textToShow);
        return textToShow;
    }
    // 标准化
//    private float getNormalizedProbability(int labelIndex) {
//        Log.d(TAG, "Probability before normalizing:" + labelProbArray[0][labelIndex]);
//        return (labelProbArray[0][labelIndex] & 0xff) / 255.0f;
//    }

    private String classifyFrame(Bitmap bitmap) {
        Log.d(TAG, "classifyFrame");
        Bitmap scaledBmp = Bitmap.createScaledBitmap(bitmap, IMAGE_SIZE_X, IMAGE_SIZE_Y, false);
        convertBitmapToByteBuffer(scaledBmp);


        ByteBuffer output = aiboost.getOutputTensor(0);
        byte[] result = new byte[output.remaining()];
        long startTime = SystemClock.uptimeMillis();
        aiboost.runWithOutInputOutput();
//        output.get(result, 0,  result.length);
        //ByteBuffer -> byte[]
        long endTime = SystemClock.uptimeMillis();
        ByteBuffer byteBuffer =output;
        byteBuffer.rewind();
        int output_box = (int) ((Math.pow((IMAGE_SIZE_X / 32), 2) + Math.pow((IMAGE_SIZE_X / 16), 2) + Math.pow((IMAGE_SIZE_X / 8), 2)) * 3);
        float[][][] out = new float[1][output_box][NUM_OF_LABELS + 5];
        Log.w("YoloV5Classifier", "out[0] detect start");
        //System.out.println("byteBuffer length" +byteBuffer.toString());
        for (int i = 0; i < output_box; ++i) {
            for (int j = 0; j < NUM_OF_LABELS + 5; ++j) {
                    out[0][i][j] = byteBuffer.getFloat();
                }
            // Denormalize xywh
            for (int j = 0; j < 4; ++j) {
                out[0][i][j] *= IMAGE_SIZE_X;
            }
        }
        for (int i = 0; i < output_box; ++i){
            final int offset = 0;
            final float confidence = out[0][i][4];
            int detectedClass = -1;
            float maxClass = 0;

            final float[] classes = new float[NUM_OF_LABELS];
            for (int c = 0; c < NUM_OF_LABELS; ++c) {
                classes[c] = out[0][i][5 + c];
            }

            for (int c = 0; c < NUM_OF_LABELS; ++c) {
                if (classes[c] > maxClass) {
                    detectedClass = c;
                    maxClass = classes[c];
                }
            }

            final float confidenceInClass = maxClass * confidence;
            probabilityList[detectedClass] = Math.max(probabilityList[detectedClass],confidenceInClass);
            if (confidenceInClass > 0.3) {
                final float xPos = out[0][i][0];
                final float yPos = out[0][i][1];

                final float w = out[0][i][2];
                final float h = out[0][i][3];
                Log.d("YoloV5Classifier",
                        '('+Float.toString(xPos) + ',' + yPos + ")," + w + ',' + h + ',' + confidenceInClass+','+labelList.get(detectedClass));
            }
        }
        // Print the results.
        String textToShow = "耗时 ：" + Long.toString(endTime - startTime) + "ms";
        textToShow += printTopKLabels();
        LiveBus.getDefault().postEvent(EVENT_KEY_OBJECTTEST,textToShow);
        return textToShow;
    }
}
