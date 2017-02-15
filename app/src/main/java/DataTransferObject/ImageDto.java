package DataTransferObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 26.01.2016.
 */
public class ImageDto
{
    // Bitmap object
    private Bitmap _bitmap;

    public Bitmap GetBitmap(){
        return _bitmap;
    }

    public void SetBitmap(Bitmap value){
        _bitmap = value;
        if(_bitmap != null)
        {
            _height = _bitmap.getHeight();
            _width = _bitmap.getWidth();
        }
      //  setBinaryTable();

    }

    // Height object
    // This opbject have only geter and it is set when bitmap is set;

    private int _height;

    public int GetHeight(){
        return _height;
    }

    // Width object
    // Same as Height this is set only with Bitmap object

    private int _width;

    public int GetWidth(){
        return  _width;
    }


    private int[][] BinaryTable;

    public int[][] getBinaryTable()
    {
        return BinaryTable;
    }

    public void setBinaryTable()
    {

        int height = GetHeight();
        int width = GetWidth();
        BinaryTable = new int[height][width];
        int red;
        int blue;
        int green;
        int pixel;
        int sum;
        for(int i = 0; i< height; i++){
            for (int j = 0 ; j< width;j++){
                pixel = _bitmap.getPixel(j,i);
                red = Color.red(pixel);
                blue = Color.blue(pixel);
                green = Color.green(pixel);
                sum = red + green + blue;
                if(sum > 525){
                    BinaryTable[i][j] = 0;
                }
                else {
                    BinaryTable[i][j] = 1;
                }
            }
        }
    }
}
