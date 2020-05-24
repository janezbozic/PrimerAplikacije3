package si.uni_lj.fri.pbd.miniapp3.adapter.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

//Class for async download of images from API
public class AsyncImageDownload extends AsyncTask<String, Integer, Bitmap> {

    ImageView imageView;
    Context context;

    public AsyncImageDownload (ImageView image, Context c){
        imageView = image;
        context = c;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        for (String s: strings){
            URL url = null;
            try {
                url = new URL(s);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }
}
