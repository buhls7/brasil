
package References;

/**
 *
 * @author simon.buehlmann
 */
public abstract class AReferencePoint
{
    protected int x, y, angle;
    
    public AReferencePoint(int x, int y, int angle)
    {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }
    
    public int getX()
    {
        return this.x;
    }
    
    public int getY()
    {
        return this.y;
    }
    
    public int getAngle()
    {
        return this.angle;
    }
    
    public abstract int getAbsolutX();
    public abstract int getAbsolutY();
    public abstract int getAbsolutAngle();
    
    public static int calculateAngle(int angle)
    {
        int tempAngle = angle;
        
        while(tempAngle >= 360 || tempAngle < 0)//Angle is not in the 360 Degres Zone
        {
            if(tempAngle >= 360)
            {
                tempAngle = tempAngle - 360;
            }
            if(tempAngle < 0)
            {
                tempAngle = tempAngle + 360;
            }
        }
        
        return tempAngle;
    }
}
