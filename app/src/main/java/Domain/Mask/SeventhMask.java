package Domain.Mask;

/**
 * Created by Sebastian on 05.04.2016.
 */
public class SeventhMask implements IMask
{
    @Override
    public Boolean Calculate(int i, int j)
    {
        return (i /2  + j/3)% 2 == 0;
    }
}
