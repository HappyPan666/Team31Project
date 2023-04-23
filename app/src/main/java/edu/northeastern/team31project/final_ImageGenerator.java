package edu.northeastern.team31project;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class final_ImageGenerator {
    public static byte[] bytesFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
