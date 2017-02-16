package br.com.etm.checkseries.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

import br.com.etm.checkseries.R;

/**
 * Created by EDUARDO_MARGOTO on 24/10/2015.
 */
public class UtilsImages {

    /*    public final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        public final int cacheSize = maxMemory / 8;
        private LruCache<String, Bitmap> mMemoryCache;

        public LruCache<String, Bitmap> getLruCache() {
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.

                    //return bitmap.getByteCount() / 1024;
                    return getBitmapByteCount(bitmap) / 1024;
                }
            };
            return mMemoryCache;
        }*/
    public static int dpFromPx(final Context context, final int px) {

        return new Float(px / context.getResources().getDisplayMetrics().density).intValue();
    }

    public static int pxFromDp(final Context context, final int dp) {
        return new Float(dp * context.getResources().getDisplayMetrics().density).intValue();
    }

    public static InputStream BitmapToInputStream(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        return bs;
    }

    public static int getBitmapByteCount(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1)
            return bitmap.getRowBytes() * bitmap.getHeight();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return bitmap.getByteCount();
        return bitmap.getAllocationByteCount();
    }

    //arredondar bordar de imagens - para VERSION_SDK < LOLLIPOP
    public static Bitmap getRoundedCornerBitmap(Context context, Bitmap input, int pixels, int w, int h, boolean squareTL, boolean squareTR, boolean squareBL, boolean squareBR) {

        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);

        //make sure that our rounded corner is scaled appropriately
        final float roundPx = pixels * densityMultiplier;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);


        //draw rectangles over the corners we want to be square
        if (squareTL) {
            canvas.drawRect(0, h / 2, w / 2, h, paint);
        }
        if (squareTR) {
            canvas.drawRect(w / 2, h / 2, w, h, paint);
        }
        if (squareBL) {
            canvas.drawRect(0, 0, w / 2, h / 2, paint);
        }
        if (squareBR) {
            canvas.drawRect(w / 2, 0, w, h / 2, paint);
        }


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(input, 0, 0, paint);

        return output;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromInputStream(InputStream inputStream, int reqWidth, int reqHeight) throws IOException {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        byte[] data = UtilsEntitys.streamToBytes(inputStream);
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
//        BitmapFactory.decodeStream(inputStream, null, options);
//        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
//        return BitmapFactory.decodeStream(inputStream, null, options);
//        return BitmapFactory.decodeResource(res, resId, options);
    }

    public void loadBitmapResId(int resId, ImageView imageView, Context context, int width, int height) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, context, width, height);
        final String imageKey = String.valueOf(resId);

        final Bitmap bitmap = UtilsCacheMemory.getInstance().getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
//        else {
//            imageView.setImageResource(R.drawable.image_placeholder);
//            BitmapWorkerTask task = new BitmapWorkerTask(mImageView);
//            task.execute(resId);
//        }


//        BitmapWorkerTask task = new BitmapWorkerTask(imageView, context, width, height);
        task.execute(resId);
    }

    public void loadBitmap(Bitmap bitmap, ImageView imageView, Context context, int width, int height, String imageKey) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, context, width, height);

        final Bitmap bitmapChache = UtilsCacheMemory.getInstance().getBitmapFromMemCache(imageKey);
        if (bitmapChache != null) {
            imageView.setImageBitmap(bitmapChache);
            bitmap = bitmapChache;
        }
        task.onPostExecute(bitmap);
//        task.execute(resId);
    }

/*    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            getLruCache().put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return getLruCache().get(key);
    }*/

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0, width = 0, height = 0;
        private Context context;

        public BitmapWorkerTask(ImageView imageView, Context context, int width, int height) {
            this.context = context;
            this.width = width;
            this.height = height;
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        public BitmapWorkerTask(ImageView imageView, Context context) {
            this.context = context;
            this.width = width;
            this.height = height;
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.

        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            return decodeSampledBitmapFromResource(this.context.getResources(), data, width, height);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

/*        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask =
                        getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }*/
    }

    public static View.OnTouchListener imgPress() {
        return imgPress(0x77eeddff); //DEFAULT color
    }

    public static View.OnTouchListener imgPress(final int color) {
        return new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        view.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }

                return true;
            }
        };
    }

    // Salva uma imagem no diretório padrão oculto do aplicativo
    public static void saveImage(Context context, Bitmap image, String filename, String extension, boolean filenameWithExtension) throws FileNotFoundException {

        // O nome do arquivo será gerado pegando o tempo atual
//        filename = System.currentTimeMillis() + "";
        // A extensão será .png
        FileOutputStream out = null;
        if (filenameWithExtension)
            out = context.openFileOutput(filename, Context.MODE_PRIVATE);
        else out = context.openFileOutput(filename + "." + extension, Context.MODE_PRIVATE);
        // Aqui a o armazenamento
        image.compress(Bitmap.CompressFormat.PNG, 100, out);

        // Esse retorno é fundamental, pois através desse nome que será possível recuperar o arquivo
//        return filename;
    }

    public static File getFilesDirectory(Context context, String filename) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, filename);
        return mypath;
    }

    public static String saveToInternalSorage(Bitmap bitmapImage, String filename, Context context) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, filename);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    // Retorna a imagem como nome "nome" do diretório oculto do aplicativo
    public static Bitmap loadImage(Context context, String filename, String extension, boolean filenameWithExtension) throws FileNotFoundException {
        Bitmap imagem = null;

        // Abre o arquivo
        FileInputStream in = null;
        if (filenameWithExtension)
            in = context.openFileInput(filename);
        else in = context.openFileInput(filename + "." + extension);
        // Decodifica a imagem
        imagem = BitmapFactory.decodeStream(in);
        // Libera a entrada
        in = null;

        // Retorna a imagem
        return imagem;
    }

    // Dado o nome do arquivo o exclui do diretório oculto do aplicativo
    public static void removerImagem(Context context, String filename, String extension, boolean filenameWithExtension) {
        if (filenameWithExtension)
            context.deleteFile(filename);
        else context.deleteFile(filename + "." + extension);
    }

    public static int getWidthDensity(Context context, int num) {
        float scale = context.getResources().getDisplayMetrics().density;
        return context.getResources().getDisplayMetrics().widthPixels - (int) (num * scale + 0.5f);

    }

    public static int getWidthDensity(Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return context.getResources().getDisplayMetrics().widthPixels - (int) (14 * scale + 0.5f);

    }

    public static int getWidthAllDensity(Context context) {
//        float scale = context.getResources().getDisplayMetrics().density;
        return context.getResources().getDisplayMetrics().widthPixels;

    }

    public static int getMarginDensity(Context context, int dpValue) {
        float d = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * d);

    }

    public static int getHeightDensity(Context context) {
        return (getWidthDensity(context) / 16) * 9;
    }

    public static int getHeightImageDensity(Context context) {
        return (getWidthDensity(context) / 16) * 7;
    }

    public static int getHeightDensity(Context context, int num) {
        return (getWidthAllDensity(context) / 16) * num;
    }

    public static void darkenImagen(ImageView imageView) {
        imageView.setColorFilter(Color.rgb(123, 123, 123), PorterDuff.Mode.MULTIPLY);
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    public void loadBitmapId(int resId, ImageView imageView, Context context) {
        if (cancelPotentialWork(resId, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, context);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(context.getResources(), BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_avaiable), task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
        }
    }

    public void loadBitmap(Bitmap bitmap, ImageView imageView, Context context) throws ExecutionException, InterruptedException {
        if (cancelPotentialWork(bitmap, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, context);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(context.getResources(), bitmap, task);
            imageView.setImageDrawable(asyncDrawable);
//            task.execute(resId);
            task.onPostExecute(bitmap);
        }
    }

    public static boolean cancelPotentialWork(Bitmap data, ImageView imageView) throws ExecutionException, InterruptedException {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
//            final int bitmapData = bitmapWorkerTask.data;
            final Bitmap bitmapData = bitmapWorkerTask.get();
            // Se bitmapData ainda não está definida ou difere dos novos dados
            if (bitmapData == null || bitmapData != data) {// Cancelar tarefa anterior
                bitmapWorkerTask.cancel(true);
            } else {// O mesmo trabalho já está em progresso
                return false;
            }
        }
        // No tarefa associada com o ImageView, ou uma tarefa existente foi cancelada
        return true;
    }

    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final int bitmapData = bitmapWorkerTask.data;
            // Se bitmapData ainda não está definida ou difere dos novos dados
            if (bitmapData == 0 || bitmapData != data) {// Cancelar tarefa anterior
                bitmapWorkerTask.cancel(true);
            } else {// O mesmo trabalho já está em progresso
                return false;
            }
        }
        // No tarefa associada com o ImageView, ou uma tarefa existente foi cancelada
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

}
