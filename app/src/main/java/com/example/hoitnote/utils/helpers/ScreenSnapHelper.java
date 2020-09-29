package com.example.hoitnote.utils.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/*
* 该类用于截图
* */
public class ScreenSnapHelper {
    public static Bitmap takeScreenSnap(View v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }

    public static Bitmap takeScreenSnapOfRootView(View v) {
        return takeScreenSnap(v.getRootView());
    }

    public static void storeScreenSnap(Context context, Bitmap bitmap, String filename) {
        String path = Environment.getExternalStorageDirectory() + "/Music/" + filename;

        OutputStream out = null;
        File imageFile = new File(path);

        try {
            out = new FileOutputStream(imageFile);
            // choose JPEG format
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
        } catch (FileNotFoundException e) {
            // manage exception ...
            Log.e("FileNotFound:",e.toString());
            Toast.makeText(context, e.toString(),Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // manage exception ...
        } finally {

            try {
                if (out != null) {
                    out.close();
                }

            } catch (Exception exc) {
            }

        }
    }
}
