package div.rex.seekfood.vendor;

import android.graphics.Bitmap;

import java.io.Serializable;

public class vendorImages implements Serializable {

    //drawable's ID
    private Bitmap bitmap;
    private String title;

    public vendorImages() {

    }


    public vendorImages(Bitmap bitmap, String title) {
        this.bitmap = bitmap;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
