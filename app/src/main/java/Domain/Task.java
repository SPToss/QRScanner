package Domain;

import java.util.ArrayList;
import java.util.List;

import DataTransferObject.FinderSearcherResultDto;
import DataTransferObject.ImageDto;
import DataTransferObject.PointDto;

public class Task implements Runnable{

    ImageDto shard;
    private FinderSearcherResultDto result;
    public FinderSearcherResultDto getResult(){
        return result;
    }
    public Task(ImageDto imageDto){
        shard = imageDto;
    }
    @Override
    public void run()
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
            result = new FinderSearcherResultDto();
            result.HaveFinder(false);
            return;
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
        result = new FinderSearcherResultDto();
        result.HaveFinder(true);
        result.setPointDtoList(pointList);
        result.SetSectionSize(point);
    }
}