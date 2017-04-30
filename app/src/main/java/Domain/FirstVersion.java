package Domain;


import android.graphics.Bitmap;

import java.util.ArrayList;

import DataTransferObject.ImageDto;
import DataTransferObject.QrCodeImageDto;
import Domain.Contracts.IQrService;
import Domain.Mask.IMask;

/**
 * Created by Sebastian on 05.04.2016.
 */
public class FirstVersion extends VersionBase implements IQrService
{
    private QrCodeImageDto _image;
    private IMask _mask;
    private int _marker;
    private Network _network;
    public FirstVersion(QrCodeImageDto image){
        _image = image;
        _mask = _image.GetMask();
        _marker = _image.GetMarker();
    }

    @Override
    public ArrayList<Double[]> Decode()
    {
        int length = GetLength();
        ArrayList<Double[]> list = new ArrayList<>();
        Double[] elements = GetDataFromSegment(_image.GetImage().GetWidth() - (2 * _marker), _image.GetImage().GetWidth() - (_marker * 10), 20, 12, 0);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (4 * _marker), _image.GetImage().GetWidth() - (_marker * 12), 18, 10, 1);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (4 * _marker), _image.GetImage().GetWidth() - (_marker * 10), 18, 12, 2);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (4 * _marker), _image.GetImage().GetWidth() - (_marker * 6), 18, 16, 2);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (6 * _marker), _image.GetImage().GetWidth() - (_marker * 2), 16, 2, 3);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (6 * _marker), _image.GetImage().GetWidth() - (_marker * 6), 16, 16, 0);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (6 * _marker), _image.GetImage().GetWidth() - (_marker * 10), 16, 12, 0);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (8 * _marker), _image.GetImage().GetWidth() - (_marker * 12), 14, 10, 1);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (8 * _marker), _image.GetImage().GetWidth() - (_marker * 10), 14, 12, 2);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (8 * _marker), _image.GetImage().GetWidth() - (_marker * 6), 14, 16, 2);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (10 * _marker), _image.GetImage().GetWidth() - (_marker * 2), 12, 20, 3);
        list.add(elements);
        if(--length == 0){
        return list;
         }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (10 * _marker), _image.GetImage().GetWidth() - (_marker * 6), 12, 16, 0);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (10 * _marker), _image.GetImage().GetWidth() - (_marker * 10), 12, 12, 0);
        list.add(elements);
        if(--length == 0){
        return list;
         }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (10 * _marker), _image.GetImage().GetWidth() - (_marker * 14), 12, 8, 0);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (10 * _marker), _image.GetImage().GetWidth() - (_marker * 19), 12, 3, 0);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment((10 * _marker), _marker, 10, 1, 1);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (12 * _marker), _image.GetImage().GetWidth() - (_marker * 19), 10, 3, 2);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (12 * _marker), _image.GetImage().GetWidth() - (_marker * 14), 10, 8, 2);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (12 * _marker), _image.GetImage().GetWidth() - (_marker * 10), 10, 12, 2);
        list.add(elements);
        if(--length == 0){
            return list;
        }
        elements = GetDataFromSegment(_image.GetImage().GetWidth() - (12 * _marker), _image.GetImage().GetWidth() - (_marker * 6), 10, 16, 2);
        list.add(elements);
        return list;
    }


    private int GetLength(){
        Double[] elements = GetDataFromSegment(_image.GetImage().GetWidth() - (2 * _marker), _image.GetImage().GetWidth() - (_marker * 6), 20, 16, 0);
        String lenh = "";
        for (Double element:elements)
        {
            if (element > 0.5)
            {
                lenh += "1";
            }
            else {
                lenh += "0";
            }
        }
        return Integer.parseInt(lenh, 2);

    }

    private Double[] GetDataFromSegment(int startWi,int startHi,int startMi, int startMj, int pos){
        Double[] result = new Double[8];
        if(pos == 0){
            ImageDto processImage = new ImageDto();
            int iterator = 0;
            boolean mask = false;
            for(int i = 3; i>=0; i--){
                processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi + _marker, startHi + (i * _marker), _marker, _marker));
                processImage.setBinaryTable();
                int[][] bitTable = processImage.getBinaryTable();
                mask = _mask.Calculate(startMi + i,startMj + 1);
                result[iterator++] = GetDataFromElement(bitTable,mask);
                processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi , startHi + (i * _marker), _marker, _marker));
                processImage.setBinaryTable();
                int[][] bitTable2 = processImage.getBinaryTable();
                mask = _mask.Calculate(startMi + i,startMj);
                result[iterator++] = GetDataFromElement(bitTable2,mask);
            }
            return result;
        }
        if(pos == 1){
            ImageDto processImage = new ImageDto();
            int iterator = 0;
            boolean mask = false;
            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi +3 *  _marker, startHi + 1 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 3,startMj + 1);
            result[iterator++] = GetDataFromElement(bitTable,mask);

            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi +2 *  _marker, startHi + 1 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable2 = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 2, startMj + 1);
            result[iterator++] = GetDataFromElement(bitTable2,mask);

            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi + 3 * _marker, startHi + 0 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable3 = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 3,startMj + 0);
            result[iterator++] = GetDataFromElement(bitTable3,mask);

            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi +2 *  _marker, startHi + 0 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable4 = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 2,startMj + 0);
            result[iterator++] = GetDataFromElement(bitTable4,mask);

            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi +1 *  _marker, startHi + 0 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable5 = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 1,startMj + 0);
            result[iterator++] = GetDataFromElement(bitTable5,mask);

            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi +0 *  _marker, startHi + 0 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable6 = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 0,startMj + 0);
            result[iterator++] = GetDataFromElement(bitTable6,mask);

            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi +1 *  _marker, startHi + 1 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable7 = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 1,startMj + 1);
            result[iterator++] = GetDataFromElement(bitTable7,mask);

            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi +0 *  _marker, startHi + 1 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable8 = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 0,startMj + 1);
            result[iterator++] = GetDataFromElement(bitTable2,mask);
            return result;
        }
        if(pos == 2){
            ImageDto processImage = new ImageDto();
            int iterator = 0;
            boolean mask = false;
            for(int i = 0; i<=3; i++){
                processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi + _marker, startHi + (i * _marker), _marker, _marker));
                processImage.setBinaryTable();
                int[][] bitTable = processImage.getBinaryTable();
                mask = _mask.Calculate(startMi + i,startMj + 1);
                result[iterator++] = GetDataFromElement(bitTable,mask);
                processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi , startHi + (i * _marker), _marker, _marker));
                processImage.setBinaryTable();
                int[][] bitTable2 = processImage.getBinaryTable();
                mask = _mask.Calculate(startMi + i,startMj);
                result[iterator++] = GetDataFromElement(bitTable2,mask);
            }
            return result;
        }
        if(pos == 3){
            ImageDto processImage = new ImageDto();
            int iterator = 0;
            boolean mask = false;
            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi +3 *  _marker, startHi + 0 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 3,startMj + 0);
            result[iterator++] = GetDataFromElement(bitTable,mask);

            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi +2 *  _marker, startHi + 0 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable2 = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 2, startMj + 0);
            result[iterator++] = GetDataFromElement(bitTable2,mask);

            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi + 3 * _marker, startHi + 1 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable3 = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 3,startMj + 1);
            result[iterator++] = GetDataFromElement(bitTable3,mask);

            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi +2 *  _marker, startHi + 1 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable4 = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 2,startMj + 1);
            result[iterator++] = GetDataFromElement(bitTable4,mask);

            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi +1 *  _marker, startHi + 1 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable5 = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 1,startMj + 1);
            result[iterator++] = GetDataFromElement(bitTable5,mask);

            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi +0 *  _marker, startHi + 1 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable6 = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 0,startMj + 1);
            result[iterator++] = GetDataFromElement(bitTable6,mask);

            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi +1 *  _marker, startHi + 0 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable7 = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 1,startMj + 0);
            result[iterator++] = GetDataFromElement(bitTable7,mask);

            processImage.SetBitmap(Bitmap.createBitmap(_image.GetImage().GetBitmap(), startWi +0 *  _marker, startHi + 0 * _marker, _marker, _marker));
            processImage.setBinaryTable();
            int[][] bitTable8 = processImage.getBinaryTable();
            mask = _mask.Calculate(startMi + 0,startMj + 0);
            result[iterator++] = GetDataFromElement(bitTable2,mask);
            return result;
        }

        return  result;

    }

    private double GetDataFromElement(int[][] bitTable, boolean mask){
        int white = 0;
        int black = 0;
        for (int i = 0; i< bitTable.length; i++)
        {
            for (int j =  0; j<  bitTable[i].length ; j++)
            {
                byte color = (byte)bitTable[i][j];
                if(color == 1 && mask){
                    white++;
                }
                else if (color == 1 && !mask) {
                    black++;
                }
                else if (color == 0 && mask) {
                    black++;
                }
                else if (color == 0 && !mask) {
                    white++;
                }
            }
        }
        return (double)black/((double)black + (double)white);
    }

}
