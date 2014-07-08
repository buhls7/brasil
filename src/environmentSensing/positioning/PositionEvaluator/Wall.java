package environmentSensing.positioning.PositionEvaluator;

import References.AReferencePoint;
import environmentSensing.NewLaser.ReflectionPoint;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simon.buehlmann
 */
public class Wall
{

    private AReferencePoint reference;

    //Lines
    private StraightLine mainLine;
    private StraightLine tolALine;
    private StraightLine tolBLine;
    private StraightLine controllLine;

    //Tolerances
    private int symetricTol;
    private int rssiTol;

    private int minSuccessivePoints;

    //ReflectionPoints
    private List<ReflectionPoint> associatedPoints;

    public Wall(AReferencePoint reference, ReflectionPoint pointA, ReflectionPoint pointB)
    {
        //Define tolerances
        this.symetricTol = 25;
        this.rssiTol = 5;
        this.minSuccessivePoints = 3;

        //Generate lines
        this.mainLine = new StraightLine(reference, pointA.getPosition(), pointB.getPosition());
        this.controllLine = new StraightLine();
        this.tolALine = this.mainLine.cloneParameters(new StraightLine());
        this.tolALine.shiftLineParallel90Degree(this.symetricTol);
        this.tolBLine = this.mainLine.cloneParameters(new StraightLine());
        this.tolBLine.shiftLineParallel90Degree(this.symetricTol * -1);

        //ReflectionPoints Buffer
        this.associatedPoints = new ArrayList();
    }

    public boolean reflectionPointBelongsToWall(ReflectionPoint reflectionPoint)
    {
        //In Future: Check RSSI Tolerance

        //Check the position
        return this.pointBelongsToFunction(reflectionPoint.getPosition());
    }

    public void addReflectionPoint(ReflectionPoint reflectionPoint)
    {
        this.associatedPoints.add(reflectionPoint);
        this.correctDifferations(reflectionPoint.getPosition());
    }

    public boolean enoughDefinitionPoints()
    {
        return (this.associatedPoints.size() >= this.minSuccessivePoints);
    }

    //private Methods
    private boolean pointBelongsToFunction(Point point)
    {
        // Shift ControllLine
        this.controllLine = this.mainLine.cloneParameters(this.controllLine);
        this.controllLine.setBasePoint(point);

        // Check
        // Step1: If the main line is horizontal, only the Y must be checked
        if (this.mainLine.getAngle() == 0)
        {
            return (point.getY() > this.tolALine.getBasePoint().getY() && point.getY() > this.tolBLine.getBasePoint().getY());
        }

        // Step 2: If the main line is vertical, only the X must be checked
        if (this.mainLine.getAngle() == 90)
        {
            return (point.getX() > this.tolALine.getBasePoint().getX() && point.getX() > this.tolBLine.getBasePoint().getX());
        }

        // Step 3: If the Angle is >0 and <=45 degrees, calculate with Y-Axis
        if (this.mainLine.getAngle() <= 45 && this.mainLine.getAngle() > 0)
        {
            try
            {
                int tolAComparablePoint = (int) this.tolALine.getXOnDefinedY(0);
                int tolBComparablePoint = (int) this.tolBLine.getXOnDefinedY(0);
                
                int controllLineComparablePoint = this.controllLine.getXOnDefinedY(0);
                
                return (controllLineComparablePoint < tolAComparablePoint && controllLineComparablePoint > tolBComparablePoint);
            }
            catch (Exception ex)
            {
                Logger.getLogger(Wall.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // Step 4: If the Angle is >45 and <180 degrees, calculate with X-Axis
        if (this.mainLine.getAngle() > 45 && this.mainLine.getAngle() < 180)
        {
            try
            {
                int tolAComparablePoint = (int) this.tolALine.getYOnDefinedX(0);
                int tolBComparablePoint = (int) this.tolBLine.getYOnDefinedX(0);
                
                int controllLineComparablePoint = this.controllLine.getYOnDefinedX(0);
                
                return (controllLineComparablePoint < tolAComparablePoint && controllLineComparablePoint > tolBComparablePoint);
            }
            catch (Exception ex)
            {
                Logger.getLogger(Wall.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // Step 5: Default Return
        return false;
    }

    private void correctDifferations(Point p)
    {
        this.controllLine.defineLineWithTwoPoints(reference, this.mainLine.getBasePoint(), p);
        double angleDifferences = this.controllLine.getAngle() - this.mainLine.getAngle();
        this.mainLine.setAngle(this.mainLine.getAngle() + angleDifferences/2);
    }

    @Override
    public String toString()
    {
        return "Base Point X: " + this.mainLine.getBasePoint().getX() + " Base Point Y: " + this.mainLine.getBasePoint().getY() + " Angle: " + this.mainLine.getAngle();
    }

}
