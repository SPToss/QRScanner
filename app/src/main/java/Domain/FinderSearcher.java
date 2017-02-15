package Domain;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.InputFilter;

import java.lang.reflect.Array;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import DataTransferObject.Enums.ImageStatus;
import DataTransferObject.FinderSearcherResultDto;
import DataTransferObject.ImageDto;
import DataTransferObject.PointDto;
import DataTransferObject.QrCodeImageDto;

/**
 * Created by Sebastian on 02.02.2016.
 */
public class FinderSearcher
{

    public QrCodeImageDto FinderBase(ImageDto image)
    {
        ImageDto processImage = new ImageDto();
        // process 1 part
        processImage.SetBitmap(Bitmap.createBitmap(image.GetBitmap(),0,0,image.GetWidth() / 2 , image.GetHeight() / 2));
        FinderSearcherResultDto firstZone = SearchForFinder(processImage);
        FinderSearcherResultDto secondZone;
        FinderSearcherResultDto thirdPart;
        FinderSearcherResultDto fourthPart = new FinderSearcherResultDto();
        if (firstZone.IsHaveFinder())
        {
            // Find finder in part one
            processImage.SetBitmap(Bitmap.createBitmap(image.GetBitmap(), image.GetWidth() / 2, 0 , image.GetWidth() / 2,image.GetHeight() / 2));
            secondZone = SearchForFinder(processImage);
            if (!secondZone.IsHaveFinder())
            {
                // Second zone finder failed check zonde 3
                processImage.SetBitmap(Bitmap.createBitmap(image.GetBitmap(),0,image.GetHeight() / 2, image.GetWidth() / 2, image.GetHeight() / 2));
                thirdPart = SearchForFinder(processImage);
                if(!thirdPart.IsHaveFinder())
                {
                    // Third zone finder failed no QR markers find
                    return null;
                }
                processImage.SetBitmap(Bitmap.createBitmap(image.GetBitmap(),image.GetWidth() / 2,image.GetHeight() / 2, image.GetWidth() / 2, image.GetHeight() / 2));
                fourthPart = SearchForFinder(processImage);
                if(!fourthPart.IsHaveFinder())
                {
                    // NO QR found
                    return null;
                }
                return FoldQrCode(new FinderSearcherResultDto[]{firstZone,secondZone,thirdPart,fourthPart},image,ImageStatus.Right);

            }
            else
            {
                processImage.SetBitmap(Bitmap.createBitmap(image.GetBitmap(),0,image.GetHeight() / 2, image.GetWidth() / 2, image.GetHeight() / 2));
                thirdPart = SearchForFinder(processImage);
                if(!thirdPart.IsHaveFinder())
                {
                    processImage.SetBitmap(Bitmap.createBitmap(image.GetBitmap(),image.GetWidth() / 2,image.GetHeight() / 2, image.GetWidth() / 2, image.GetHeight() / 2));
                    fourthPart = SearchForFinder(processImage);
                    if(!fourthPart.IsHaveFinder())
                    {
                        // NO QR found
                        return null;
                    }
                    return FoldQrCode(new FinderSearcherResultDto[]{firstZone,secondZone,thirdPart,fourthPart},image,ImageStatus.Left);
                }
                return FoldQrCode(new FinderSearcherResultDto[]{firstZone,secondZone,thirdPart,fourthPart},image,ImageStatus.Normal);
            }
        }
        else
        {
            // Didynt find finder in part one checking part two
            processImage.SetBitmap(Bitmap.createBitmap(image.GetBitmap(), image.GetWidth() / 2, 0 , image.GetWidth() / 2,image.GetHeight() / 2));
            secondZone = SearchForFinder(processImage);
            if (!secondZone.IsHaveFinder())
            {
                // Second zone finder failed no QR markers find
                return null;
            }
            // Marker in second part was find serch part 3
            processImage.SetBitmap(Bitmap.createBitmap(image.GetBitmap(),0,image.GetHeight() / 2, image.GetWidth() / 2, image.GetHeight() / 2));
            thirdPart = SearchForFinder(processImage);
            if(!thirdPart.IsHaveFinder())
            {
                // Third zone finder failed no QR markers find
                return null;
            }
            // Have 2 markers serching for last one
            processImage.SetBitmap(Bitmap.createBitmap(image.GetBitmap(),image.GetWidth() / 2,image.GetHeight() / 2, image.GetWidth() / 2, image.GetHeight() / 2));
            fourthPart = SearchForFinder(processImage);
            if(!fourthPart.IsHaveFinder())
            {
                // NO QR found
                return null;
            }
            return FoldQrCode(new FinderSearcherResultDto[]{firstZone,secondZone,thirdPart,fourthPart},image,ImageStatus.Revert);
        }
    }

    private QrCodeImageDto FoldQrCode(FinderSearcherResultDto[] finderSearcherResultDtos, ImageDto image,ImageStatus imageStatus)
    {
        if(finderSearcherResultDtos.length != 4)
        {
            return null;
        }
        List<PointDto> firstPoints;
        List<PointDto> secondPoints;
        List<PointDto> thirdPoints;
        switch (imageStatus)
        {
            case Normal:
            {
                firstPoints = finderSearcherResultDtos[0].getPointDtoList();
                secondPoints = finderSearcherResultDtos[1].getPointDtoList();
                thirdPoints = finderSearcherResultDtos[2].getPointDtoList();
                break;
            }
            case Right:
            {
                firstPoints = finderSearcherResultDtos[2].getPointDtoList();
                secondPoints = finderSearcherResultDtos[1].getPointDtoList();
                thirdPoints = finderSearcherResultDtos[3].getPointDtoList();
                image.SetBitmap(RotateBitmap(image.GetBitmap(),270));
                break;
            }
            case Left:
            {
                firstPoints = finderSearcherResultDtos[1].getPointDtoList();
                secondPoints = finderSearcherResultDtos[3].getPointDtoList();
                thirdPoints = finderSearcherResultDtos[0].getPointDtoList();
                image.SetBitmap(RotateBitmap(image.GetBitmap(),90));
                break;
            }
            case Revert:
            {
                firstPoints = finderSearcherResultDtos[3].getPointDtoList();
                secondPoints = finderSearcherResultDtos[2].getPointDtoList();
                thirdPoints = finderSearcherResultDtos[1].getPointDtoList();
                image.SetBitmap(RotateBitmap(image.GetBitmap(),180));
                break;
            }
            default:
            {
                return null;
            }
        }
        // Checking positioning of finder in P1 and P2 to connect them straight line
        if(firstPoints.get(0).getX() > secondPoints.get(1).getX())
        {
            firstPoints.set(0, PointChange(secondPoints.get(1).getY(),firstPoints.get(0).getY()));
        }
        // Checking positioning of finder in P1 and P3 to connect them straight line
        if(firstPoints.get(0).getX() > thirdPoints.get(3).getY())
        {
            firstPoints.set(0,PointChange(firstPoints.get(0).getY(),thirdPoints.get(3).getX()));
        }
        ImageDto qrImage = new ImageDto();
        Bitmap bmp = image.GetBitmap();
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        Bitmap test = Bitmap.createBitmap(bmp,firstPoints.get(0).getX(),
                firstPoints.get(0).getY(),((w/2 - firstPoints.get(0).getX()) + (w /2 - secondPoints.get(1).getX())),((h / 2 - firstPoints.get(0).getY()) + (h / 2 - thirdPoints.get(2).getY())));
        qrImage.SetBitmap(test);

        int marker = CalculateMarker(finderSearcherResultDtos[0].GetSectionSize(), finderSearcherResultDtos[1].GetSectionSize(),
                finderSearcherResultDtos[2].GetSectionSize(), finderSearcherResultDtos[3].GetSectionSize());

        return new QrCodeImageDto(qrImage,marker);
    }

    private FinderSearcherResultDto SearchForFinder(ImageDto shard)
    {
        shard.setBinaryTable();
        int[][] bitTable = shard.getBinaryTable();
        ArrayList<PointDto> startList = new ArrayList<>();
        ArrayList<Integer> partList = new ArrayList<>();
        for (int i = 0; i< bitTable.length; i++)
        {
            boolean firstPart = false;
            boolean secondPart = false;
            boolean thirdPart = false;
            boolean fourthPart = false;
            boolean fifthPart = false;
            int firstPartLength = 0;
            PointDto firstPartStart = new PointDto();
            int secondPartLength = 0;
            int thirdPartLength = 0;
            int fourthPartLength = 0;
            int fifthPartLenght = 0;
            int partSize = 0;
            for (int j = 0; j< bitTable[i].length ; j++)
            {
                byte color = (byte)bitTable[i][j];
                if(color == 1)
                {
                    if(!firstPart)
                    {
                        // Possible First part located
                        firstPartLength++;
                    }
                    else  if(secondPart && !fourthPart)
                    {

                        if(secondPartLength < (firstPartLength + secondPartLength ) / 2 + 5 && secondPartLength > ((firstPartLength + secondPartLength ) / 2) - 5)
                        {
                            thirdPart = true;
                            // pissible third part
                            thirdPartLength ++;
                        }
                        else
                        {
                            firstPart = false;
                            secondPart =false;
                            firstPartLength = 0;
                            firstPartStart = new PointDto();
                            secondPartLength = 0;

                        }
                    }
                    else if (fourthPart )
                    {
                        if ((fourthPartLength < (firstPartLength + secondPartLength + thirdPartLength + fourthPartLength) / 6 + 5 && fourthPartLength > ((firstPartLength + secondPartLength + thirdPartLength + fourthPartLength) / 6) - 5))
                        {
                            fifthPartLenght ++;
                            fifthPart = true;
                        }
                        else
                        {
                            firstPart = false;
                            secondPart = false;
                            thirdPart = false;
                            fourthPart = false;
                            fifthPart = false;
                            firstPartLength = 0;
                            firstPartStart = new PointDto();
                            secondPartLength = 0;
                            thirdPartLength = 0;
                            fourthPartLength = 0;
                        }

                    }
                }
                else if (color == 0)
                {
                    if(firstPartLength != 0 && !thirdPart && !fifthPart)
                    {
                        if(!firstPart){

                            firstPartStart.setX(j - firstPartLength);
                            firstPartStart.setY(i);
                        }
                        // Posible firs part setted checking for second part
                        firstPart = true;
                        secondPart  = true;
                        secondPartLength++;
                    }
                    else if(thirdPart && !fifthPart)
                    {
                        if ((thirdPartLength / 3 < (firstPartLength + secondPartLength + thirdPartLength ) / 5 + 5 && thirdPartLength / 3 > ((firstPartLength + secondPartLength + thirdPartLength) / 5) - 5))
                        {
                            fourthPart = true;
                            fourthPartLength ++;
                        }
                        else
                        {
                            firstPart = false;
                            secondPart = false;
                            thirdPart = false;
                            fourthPart = false;
                            fifthPart = false;
                            firstPartLength = 0;
                            firstPartStart = new PointDto();
                            secondPartLength = 0;
                            thirdPartLength = 0;
                        }
                    }
                    else if (fifthPart)
                    {
                        partSize = (fifthPartLenght + firstPartLength + secondPartLength + thirdPartLength + fourthPartLength ) / 7;
                        if(fifthPartLenght < partSize + 5 && fifthPartLenght > partSize - 5)
                        {
                            // propably find pattern
                            startList.add(firstPartStart);
                            partList.add(partSize);

                            break;
                        }
                        else
                        {
                            firstPart = false;
                            firstPartLength = 0;
                            firstPartStart = new PointDto();
                            secondPartLength = 0;
                            thirdPartLength = 0;
                            fourthPartLength = 0;
                            fifthPartLenght = 0;
                            partSize = 0;
                        }
                    }

                }
            }
        }
        if(partList.size() < 1 )
        {
            FinderSearcherResultDto result = new FinderSearcherResultDto();
            result.HaveFinder(false);
            return  result;
        }
        int point = 0;
        int sum =0;
        // Chose bigest part
        int max = 0;
        int start = 0;
        int end = 0;
        for (int part: partList)
        {
           if(part > max){
               max = part;
           }
        }
        int maxLeng = 0;
        for (int i= 0;i <= partList.size() - 1;i++)
        {
            if(partList.get(i) == max){
                maxLeng ++;
                if(start == 0){
                    start = i;
                }
                sum += partList.get(i);
            }
        }
        point = sum / maxLeng;
        PointDto leftUp = new PointDto();
        leftUp.setX(startList.get(start).getX());
        leftUp.setY(startList.get(start).getY() - 2 * point);
        PointDto rightUp = new PointDto();
        rightUp.setX(leftUp.getX() + 7 * point);
        rightUp.setY(leftUp.getY());
        PointDto leftDown = new PointDto();
        leftDown.setX(leftUp.getX());
        leftDown.setY(leftUp.getY() + 7 * point);
        PointDto rightDown = new PointDto();
        rightDown.setX(rightUp.getX());
        rightDown.setY(leftDown.getY());
        List<PointDto> pointList = new ArrayList<>();
        pointList.add(leftUp);
        pointList.add(rightUp);
        pointList.add(leftDown);
        pointList.add(rightDown);
        FinderSearcherResultDto result = new FinderSearcherResultDto();
        result.HaveFinder(true);
        result.setPointDtoList(pointList);
        result.SetSectionSize(point);
        return result;
    }


    private PointDto PointChange(int first,int second)
    {
        PointDto temp = new PointDto();
        temp.setX(first);
        temp.setY(second);
        return temp;
    }

    private int CalculateMarker(int firstMarker,int secondMarker,int thirdMarker,int fourthMarker)
    {
        return (firstMarker + secondMarker + thirdMarker + fourthMarker) / 4;
    }

    private Bitmap RotateBitmap(Bitmap bitmap,float degree)
    {
        Matrix matrix = new Matrix();

        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }
}
