package co.gdera.javacvtest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_videoio;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity {
ImageView imageView; Bitmap bitmap;
    Handler handler;
    Frame vFrame = null;
    AndroidFrameConverter androidFrameConverter;
    FrameGrabber videoGrabber;
    FFmpegFrameRecorder recorder;
    BlockingQueue<Bitmap> queue;
    Bitmap img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            imageView=(ImageView) findViewById(R.id.imageView);
           imageView.setImageResource(R.mipmap.kk);
             queue=new LinkedBlockingDeque<Bitmap>() ;

       //You need to have an image, of course!
         videoGrabber = new FFmpegFrameGrabber("/storage/emulated/0/data/kk.mp4");
        // recorder = new FFmpegFrameRecorder("/storage/emulated/0/Download/test2.mpeg",200,150);
        try
        {
            videoGrabber.setFormat("mp4");//mp4 for example
            videoGrabber.start();
          //  recorder.setFrameRate(30);
          //  recorder.start();
        } catch (Exception e)
        {
            Log.e("javacv", "Failed to start grabber" + e);
        }
        handler=new Handler();

        androidFrameConverter=new AndroidFrameConverter();

    Thread t=    new Thread(new Runnable() {
            public void run() {
                do

                {
                    try {
                        vFrame = videoGrabber.grabFrame();
                        if (vFrame != null) {
                            bitmap = androidFrameConverter.convert(vFrame);
                            Log.d("javacv", "................... start grabber");
                            queue.put(bitmap);
                           /* runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                  try{  img=queue.poll(1000L, TimeUnit.MILLISECONDS);}catch (Exception e){}
                                     imageView.setImageBitmap(img);
                                }
                            });*/
                           // recorder.record(vFrame);
                        }


                    } catch (Exception e) {
                        Log.e("javacv", "video grabFrame failed: " + e);
                    }
                }
                while (vFrame != null);
            } });

        /*Thread t2= new Thread(new Runnable() {
            public void run() {
               int i=0;
               while(++i<5000){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{ img=queue.poll(1000L, TimeUnit.MILLISECONDS);
                     if(img!=null)  imageView.setImageBitmap(img);}catch (Exception e){}
                    }
                });}

            }
         });*/
        t.start();
      //  t2.start();

    /*    try
        {
            videoGrabber.stop();
        }catch (FrameGrabber.Exception e)
        {
            Log.e("javacv", "failed to stop video grabber", e);
        }
*/

    /*opencv_core.IplImage img = opencv_imgcodecs.cvvLoadImage("/storage/emulated/0/Download/images.jpg");
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("/storage/emulated/0/Download/test.mpeg",200,150);

        try {
            recorder.setFrameRate(30);
            recorder.start();

            for (int i=0;i<100;i++)
            {
                recorder.record(new OpenCVFrameConverter.ToIplImage().convert(img));
            }
            recorder.stop();
        }
        catch (Exception e){
            e.printStackTrace();
        }
*/


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
