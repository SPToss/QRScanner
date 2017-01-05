package Domain.Mask;

/**
 * Created by Sebastian on 05.04.2016.
 */
public class ThirdMask implements IMask
{
    @Override
    public Boolean Calculate(int i, int j)
    {
        return (i + j) % 2 == 0;
    }
}
