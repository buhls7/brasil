package environmentSensing.positioning.PositionEvaluator;

import environmentSensing.NewLaser.MeasuredResult;
import environmentSensing.NewLaser.ReflectionPoint;
import environmentSensing.positioning.PositionEvaluator.dummyScenarios.Scenario1;
import environmentSensing.positioning.PositionEvaluator.dummyScenarios.Scenario2;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simon.buehlmann
 */
public class WallDetector
{

    private int minFollowingPointsForVector;
    private int ratioTolerance;

    private List<Wall> detectedWalls;

    private IEnvironmentSensor environmentSensor;

    public WallDetector(IEnvironmentSensor environmentSensor, int minFollowingPointsForVector, int ratioTolerance)
    {
        this.environmentSensor = environmentSensor;

        this.minFollowingPointsForVector = minFollowingPointsForVector;
        this.ratioTolerance = ratioTolerance;

        this.detectedWalls = new ArrayList();
    }

    private enum DetectionState
    {

        MATCH_WITH_EXIST_WALL,
        EXIST_TEMP_WALL,
        MATCH_WITH_TEMP_WALL,
        CREATE_TEMP_WALL
    }

    public void recognize()
    {
        MeasuredResult measuredResult = this.environmentSensor.getEnvironmentReflections();

        Wall tempWall = null;
        DetectionState detectionState = DetectionState.MATCH_WITH_EXIST_WALL;
        int activeReflectionPoint = 1;

        while (activeReflectionPoint < measuredResult.getReflectionPoints().size())
        {
            switch (detectionState)
            {
                case MATCH_WITH_EXIST_WALL:
                    for (Wall wall : this.detectedWalls)
                    {
                        if (wall.reflectionPointBelongsToWall(measuredResult.getReflectionPoints().get(activeReflectionPoint)))
                        {
                            wall.addReflectionPoint(measuredResult.getReflectionPoints().get(activeReflectionPoint));

                            activeReflectionPoint++;
                            detectionState = DetectionState.MATCH_WITH_EXIST_WALL;
                            break;
                        }

                        detectionState = DetectionState.EXIST_TEMP_WALL;
                    }
                    detectionState = DetectionState.EXIST_TEMP_WALL;
                    break;

                case EXIST_TEMP_WALL:
                    if (tempWall != null)
                    {
                        detectionState = DetectionState.MATCH_WITH_TEMP_WALL;
                    }
                    else
                    {
                        detectionState = DetectionState.CREATE_TEMP_WALL;
                    }
                    break;

                case CREATE_TEMP_WALL:
                    tempWall = new Wall(measuredResult.getReference(), measuredResult.getReflectionPoints().get(activeReflectionPoint - 1), measuredResult.getReflectionPoints().get(activeReflectionPoint));

                    activeReflectionPoint++;
                    detectionState = DetectionState.MATCH_WITH_EXIST_WALL;
                    break;

                case MATCH_WITH_TEMP_WALL:
                    if (tempWall.reflectionPointBelongsToWall(measuredResult.getReflectionPoints().get(activeReflectionPoint)))
                    {
                        tempWall.addReflectionPoint(measuredResult.getReflectionPoints().get(activeReflectionPoint));

                        if (tempWall.enoughDefinitionPoints())
                        {
                            //Wall Detected
                            System.out.println("Wall Detected!");
                            this.detectedWalls.add(tempWall);
                            tempWall = null;
                        }

                        activeReflectionPoint++;
                        detectionState = DetectionState.MATCH_WITH_EXIST_WALL;
                    }
                    else
                    {
                        tempWall = null;
                        detectionState = DetectionState.MATCH_WITH_EXIST_WALL;
                    }
                    break;
            }
        }

        for (Wall wall : this.detectedWalls)
        {
            System.out.println(wall);
        }
    }

    public void detectWalls()
    {
        // Walls Buffer
        List<Wall> wallsBuffer = new ArrayList<>();
        
        MeasuredResult measuredResult = this.environmentSensor.getEnvironmentReflections();

        int y = 0;

        do
        {
            Wall tempWall = new Wall(measuredResult.getReference(), measuredResult.getReflectionPoints().get(y), measuredResult.getReflectionPoints().get(y + 1));

            int x = y + 2;

            do
            {
                if (tempWall.reflectionPointBelongsToWall(measuredResult.getReflectionPoints().get(x)))
                {
                    tempWall.addReflectionPoint(measuredResult.getReflectionPoints().get(x));
                }

                x++;
            }
            while (x < (measuredResult.getReflectionPoints().size() - 1));

            wallsBuffer.add(tempWall);
            y++;
        }
        while ((y + 2) < (measuredResult.getReflectionPoints().size() - 1));
        
        for(Wall wall : wallsBuffer)
        {
            if(wall.enoughDefinitionPoints())
            {
               System.out.println("Wall Detected!");
               System.out.println(wall.toString());
            }
        }
    }
    
    public void allez()
    {
        List<Wall> wallBuffer = new ArrayList<>();
        Wall tempWall = null;
        
        MeasuredResult measuredResult = this.environmentSensor.getEnvironmentReflections();
        
        int x = 0;
        
        while(x < (measuredResult.getReflectionPoints().size()-1))
        {
            ReflectionPoint tempPoint = measuredResult.getReflectionPoints().get(x);
            System.out.println("New tempPoint: " + tempPoint);
            
            boolean matchesPointWithWallFromBuffer = false;
            Wall matchingWall = null;
            for(Wall wall : wallBuffer)
            {
                if(wall.reflectionPointBelongsToWall(tempPoint))
                {
                    matchesPointWithWallFromBuffer = true;
                    matchingWall = wall;
                    System.out.println("Point matching with exist wall from buffer");
                    System.out.println(wall);
                    break;
                }
            }
            if(!matchesPointWithWallFromBuffer)
            {
                if(tempWall != null)
                {
                    if(tempWall.reflectionPointBelongsToWall(tempPoint))
                    {
                        tempWall.addReflectionPoint(tempPoint);
                        System.out.println("Point matching with tempWall");
                        
                        if(tempWall.enoughDefinitionPoints())
                        {
                            wallBuffer.add(tempWall);
                            System.out.println("Wall Detected!");
                            System.out.println(tempWall);
                            tempWall = null;
                        }
                        x++;
                    }
                    else
                    {
                        tempWall = null;
                        System.out.println("Point dont matching with tempWall");
                        x = x-1;
                    }
                }
                else
                {
                    tempWall = new Wall(measuredResult.getReference(), tempPoint, measuredResult.getReflectionPoints().get(x+1));
                    System.out.println("New tempWall created: " + tempWall);
                    x = x + 2;
                }
            }
            else
            {
                matchingWall.addReflectionPoint(tempPoint);
                x++;
            }
        }
    }

    public static void main(String[] args)
    {
        
        System.out.println("START");
        WallDetector detector = new WallDetector(Scenario2.getInstance(), 3, 100);

        //detector.recognize();
        //detector.detectWalls();
        detector.allez();
    }
}
