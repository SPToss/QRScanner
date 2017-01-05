package DataTransferObject;

import java.util.List;

/**
 * Created by Sebastian on 02.02.2016.
 */
public class FinderSearcherResultDto
{
    private int _sectionSize;
    public int GetSectionSize()
    {
        return _sectionSize;
    }
    public void SetSectionSize(int size)
    {
        _sectionSize = size;
    }

    private List<PointDto> pointDtoList;

    private boolean _haveFinder;
    public boolean IsHaveFinder()
    {
        return _haveFinder;
    }

    public void HaveFinder(boolean haveFinder)
    {
        _haveFinder = haveFinder;
    }


    public List<PointDto> getPointDtoList()
    {
        return pointDtoList;
    }

    public void setPointDtoList(List<PointDto> pointDtoList)
    {
        this.pointDtoList = pointDtoList;
    }
}
