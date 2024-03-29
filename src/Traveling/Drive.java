/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Traveling;

import ComView.ComView;
import FieldCommander.Cell;
import FieldCommander.FieldCommander;
import MainPack.Main;
import References.AbsoluteReferencePoint;
import References.ReferencePoint;
import Sequence.JobController;
import Sequence.StateMachine;
import environmentSensing.positioning.OptimizationPosition;
import java.awt.AWTException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian
 */
public class Drive extends Thread
{

    private int startCellX, printCellX, printCellY;
    private int startCellY;
    private int startPosPhi;
    public static final int FIELDSIZE = 560;
    public int deltaX, deltaY, deltaAbsX, deltaAbsY;
    private int deltaCellsX;
    private int deltaCellsY;
    private int endTargetX, endTargetY, endTargetPhi;
    private int xAbsolut, yAbsolut, oldX, oldY;
    boolean calculate = true;
    private boolean running = true;
    boolean onMachine = false;
    boolean turnedToMachine;
    public boolean endPosReached;
    public boolean takeNext = true;
    //AVOID
    public boolean avoid, avoidX, avoidY;
    int cellRest, currentXcell, currentYcell;
    Cell finalTarget;
    String nextStep;
    int alrdyDrivenY, alrdyDrivenX;
    int restWayX, restWayY;
    int lastStartCellX, lastStartCellY, lastDeltaX, lastDeltaY;
    public boolean paused;
    private Cell endCell;
    private Cell startCell;
    int turnPhi;
    String step = "INIT";
    String state = "RUNNING";
    String avoidStep;
    boolean fromLeft, fromRight, firstY;
    FieldCommander fc;
    ComView comView;
    
    
    OptimizationPosition pos;
    private static Drive instance = null;
    StateMachine sM;

    public static Drive getInstance() throws AWTException
    {
        if (instance == null)
        {
            instance = new Drive();
        }
        return instance;
    }

    private Drive() throws AWTException
    {

        this.comView = ComView.getInstance();
        this.fc = FieldCommander.getInstance();

        this.sM = StateMachine.getInstance();
        pos = new OptimizationPosition();
    }

    public void run()
    {
        while (running)
        {

            if (paused)
            {
                try
                {
                    paused();
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(Drive.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            switch (state)
            {
                case "INIT":

                    break;
                case "WAIT_START":

                    break;
                case "RUNNING":
                    try
                    {
                        Thread.sleep(300);
                        drive();
                    } catch (InterruptedException ex)
                    {
                        Logger.getLogger(Drive.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    break;
                case "PAUSED":
                    Main.log.info("PAUSED");

                    break;
            }
            xAbsolut = oldX + comView.getxAktuell();
            yAbsolut = oldY + comView.getyAktuell();
            System.out.println("X: " + xAbsolut);
            System.out.println("Y: " + yAbsolut);

        }
    }

    public void drive() throws InterruptedException
    {
        switch (step)
        {
            case "INIT":

                if (takeNext)
                {

                    System.out.println("blabla geh schon weiter");
                    // berechnungen der Startwerte  
                    endPosReached = false;
                    startCellX = this.startCell.getX();
                    startCellY = this.startCell.getY();
                    printCellX = startCellX;
                    printCellY = startCellY;
                    if (startCellX > 9)
                    {
                        fromRight = true;
                    } else
                    {
                        fromLeft = true;
                    }

                    endTargetX = this.endCell.getX();
                    endTargetY = this.endCell.getY();
                    deltaX = endCell.getRealX() - startCell.getRealX();
                    deltaY = endCell.getRealY() - startCell.getRealY();
                    deltaCellsX = endCell.getX() - startCell.getX();
                    deltaCellsY = endCell.getY() - startCell.getY();
                    //   beginning = false;

                    step = "START";
                }
                break;
            case "START":

                if (((startCellX % 2) != 0) && deltaY != 0)
                {
                    step = "ROTATE_Y";

                }
                if ((((startCellY % 2) != 0) || endTargetY == 0 || startCellY == 0 || endTargetX == 0) && !firstY)
                {
                    step = "ROTATE_X";

                }

                if (endTargetX == startCellX && endTargetY == startCellY)
                {

                    step = "TURN_TO_MACHINE";

                }
                lastStartCellX = startCellX;
                lastStartCellY = startCellY;

                break;
            case "ROTATE_X":
                //<editor-fold defaultstate="collapsed" desc="Drehen">
                if (startPosPhi == 90 || startPosPhi == 270)
                {
                    step = "X";
                }
                if (startCellX >= endTargetX && startPosPhi != 270) // wenn die startposition grösser als die endposition ist muss er ins minus fahren.. folglich nach links drehen
                {
                    rotate("W");
                    step = "WAIT_TURNX";
                }
                if (startCellX < endTargetX && startPosPhi != 90) // nach rechts drehen wenn endposition grösser ist als startposition
                {
                    rotate("E");
                    step = "WAIT_TURNX";
                }

                break;
            case "WAIT_TURNX":

                if (comView.getReady() == 1) // sobald ready vom view kommt soll mit dem senden von Go begonnen werden
                {
                    comView.setGo(1);
                }

                if (comView.getEnde() == 1) // sobald dann die bestätigung kommt, dass die koordinaten geschrieben wurden kann in den nächsten step gewechselt werden
                {
                    step = "X";
                    comView.setGo(0);
                }
                //</editor-fold>
                break;

            case "X":
                //<editor-fold defaultstate="collapsed" desc="in X verfahren">

                /* im Prinzip zwei fälle... wenn er bereits auf einer Maschinenachse ist oder wenn er in einem gang ist
                 muss folglich unterschiedlich weit fahre... zwei oder drei felder
                 Ebenfalls ein sonderfall wenn er noch abbiegen muss in einen gang                 
                 */
                if (((deltaCellsX % 2 != 0) && (startCellX % 2 != 0)) || ((deltaCellsX % 2 == 0) && (startCellX % 2 == 0))) //
                {
                    step = "ON_MACHINE_X";
                }

                if (((deltaCellsX % 2 == 0) && (startCell.getX() % 2 != 0)) || ((deltaCellsX % 2 != 0) && (startCellX % 2 == 0)) || (endTargetX == 0))
                {
                    step = "NOT_ON_MACHINE_X";
                }

                break;

            case "ON_MACHINE_X":
                //pinkiger Fall

                if ((startCellY == endTargetY)) // wenn das Ziel Y auf der gleichen Höhe wie das start Y ist
                {
                    startCellX = startCellX + deltaCellsX;
                    step = "DRIVE_X";
                }

                if (startCellX == this.endTargetX)
                {
                }

                if (startCellY != endTargetY)
                {
                    onMachine = true;
                    // wenn Y nicht auf gleicher Höhe muss ich abbiegen

                    if (deltaCellsX < 0) // Minus bzw Plus rechnen einer Zelle damit man einen Gang erwischt 
                    //ob ich von rechts oder links komme spielt hier eine rolle weil minus oder plus eine Zelle
                    {

                        startCellX = startCellX + deltaCellsX + 1;
                    } else//if (deltaCellsX > 0) //
                    {

                        startCellX = startCellX + deltaCellsX - 1;
                    }

                    step = "DRIVE_X";

                }

                break;

            case "NOT_ON_MACHINE_X":

                startCellX = startCellX + deltaCellsX;

                step = "DRIVE_X";

                break;

            case "DRIVE_X":
                if (deltaX < 0)
                {
                    deltaX = (-1) * deltaX;
                }

                if (deltaX != 0 && onMachine)
                {
                    this.printXRoute(deltaCellsX - 1);
                    deltaX = deltaX - FIELDSIZE;

                }
                if (!onMachine)
                {

                    this.printXRoute(deltaCellsX);
                }
                if (deltaX == 0 && onMachine)
                {
                    this.printXRoute(deltaCellsX - 1);
                    deltaX = deltaX + FIELDSIZE;

                }
                lastDeltaX = deltaX;
                comView.setKoords(deltaX, 0, 0);
                deltaAbsX = deltaX;
                deltaX = (endTargetX - startCellX) * FIELDSIZE;
                deltaCellsX = endTargetX - startCellX;
                printCellX = startCellX;

                calculate = true;
                onMachine = false;
                step = "WAIT";
                break;
            case "WAIT":

                if (avoid || fc.avoidTest)
                {
                    step = "AVOID";
                    avoidX = true;
                }

                if (comView.getReady() == 1) // sobald ready vom view kommt soll mit dem senden von Go begonnen werden
                {

                    comView.setGo(1);
                    //    Thread.sleep(110);

                } else if (comView.getEnde() == 1) // sobald dann die bestätigung kommt, dass die koordinaten geschrieben wurden kann in den nächsten step gewechselt werden
                {
                    comView.setGo(0);

                    if (calculate)
                    {
                        oldX = oldX + this.deltaAbsX;
                    }
                    calculate = false;

                    // prepared for using positioning method
                    // range???
                    // whats to do when not in range? turn and watch other wall?
                    if (deltaCellsX > 9)
                    {
                        ReferencePoint temp = new ReferencePoint(this.xAbsolut, this.yAbsolut, startPosPhi, AbsoluteReferencePoint.getInstance());
                        pos.correctPosition(temp);

                    }

                    if (endTargetX == startCellX && endTargetY == startCellY)
                    {

                        step = "TURN_TO_MACHINE";

                    } else
                    {

                        step = "ROTATE_Y";
                    }
                }

                //</editor-fold>
                break;

            case "ROTATE_Y":
                //<editor-fold defaultstate="collapsed" desc="Drehen">
                if (startPosPhi == 180 || startPosPhi == 0)
                {
                    step = "Y";
                }
                if (startCellY > endTargetY && startPosPhi != 0)
                {
                    rotate("N");
                }
                if (startCellY <= endTargetY && startPosPhi != 180)
                {
                    rotate("S");
                }
                firstY = false;
                step = "WAIT_TURNY";

                break;

            case "WAIT_TURNY":

                if (comView.getReady() == 1) // sobald ready vom view kommt soll mit dem senden von Go begonnen werden
                {
                    comView.setGo(1);
                } else if (comView.getEnde() == 1) // sobald dann die bestätigung kommt, dass die koordinaten geschrieben wurden kann in den nächsten step gewechselt werden
                {
                    comView.setGo(0);

                    step = "Y";
                }

                break;
            //</editor-fold>

            case "Y":

                //<editor-fold defaultstate="collapsed" desc="in Y verfahren">
                if (((deltaCellsY % 2 != 0) && (startCell.getY() % 2 != 0)) || ((deltaCellsY % 2 == 0) && (startCell.getY() % 2 == 0)))
                {
                    step = "ON_MACHINE_Y";
                }

                if (((deltaCellsY % 2 == 0) && (startCell.getY() % 2 != 0)) || ((deltaCellsY % 2 != 0) && (startCell.getY() % 2 == 0)) || endTargetY == 0)
                {
                    step = "NOT_ON_MACHINE_Y";
                }

                break;

            case "ON_MACHINE_Y":
                onMachine = true;

                if ((startCellX == endTargetX) || (startCellX == 1 && endTargetX == 0)) // wenn das Ziel Y auf der gleichen Höhe wie das start Y ist
                {
                    startCellY = startCellY + deltaCellsY;
                    step = "DRIVE_Y";
                } else if (startCellY != endTargetY)
                {
                    // wenn Y nicht auf gleicher Höhe

                    if (deltaCellsY < 0)
                    {
                        startCellY = startCellY + deltaCellsY + 1;
                    }
                    if (deltaCellsY > 0) // else
                    {

                        startCellY = startCellY + deltaCellsY - 1;
                    }
                    step = "DRIVE_Y";
                }
                break;

            case "NOT_ON_MACHINE_Y":

                // blauer Fall
                startCellY = startCellY + deltaCellsY;

                step = "DRIVE_Y";

                break;
            case "DRIVE_Y":

                if (deltaY < 0)
                {
                    deltaY = (-1) * deltaY;
                }

                if (onMachine && (this.endTargetY % 2 != 0))
                {
                    // falls er natürlich eine maschine anfahren muss soll er nicht daran vorbei fahren
                    this.printYroute(deltaCellsY - 1);
                    deltaY = deltaY - FIELDSIZE;

                } else
                {

                    this.printYroute(deltaCellsY);

                }

                comView.setKoords(deltaY, 0, 0);
                lastDeltaY = deltaY;
                deltaAbsY = deltaY;

                deltaY = (endTargetY - startCellY) * FIELDSIZE;

                deltaCellsY = endTargetY - startCellY;
                printCellY = startCellY;

                onMachine = false;
                calculate = true;

                step = "WAIT_Y";
                break;

            case "WAIT_Y":

                if (avoid || fc.avoidTest)
                {
                    step = "AVOID";
                    avoidY = true;
                }

                if (comView.getReady() == 1) // sobald ready vom view kommt soll mit dem senden von Go begonnen werden
                {
                    comView.setGo(1);
                } else if (comView.getEnde() == 1) // sobald dann die bestätigung kommt, dass die koordinaten geschrieben wurden kann in den nächsten step gewechselt werden
                {
                    comView.setGo(0);
                    if (calculate)
                    {
                        oldY = oldY + this.deltaAbsY;
                    }
                    calculate = false;
                    step = "START";

                    // noch in getEnde
                    if (turnedToMachine)
                    {
                        endPosReached = true;
                        takeNext = false;

                        turnedToMachine = false;
                        step = "INIT";

                    }

                }

                break;
            //</editor-fold>
            case "TURN_TO_MACHINE":

                if (endCell.getDirLetter() != null)
                {
                    rotate(endCell.getDirLetter());
                }

                turnedToMachine = true;

                startCell = endCell;

                //  setStartCell(startCellX, startCellY);
                step = "WAIT_Y";
                break;

            case "AVOID":
                if (comView.breakingFactor == 0)
                {

                    restWayX = lastDeltaX - comView.getxAktuell();
                    restWayY = lastDeltaY - comView.getxAktuell();

                    alrdyDrivenX = comView.getxAktuell();
                    alrdyDrivenY = comView.getxAktuell();
                    cellRest = alrdyDrivenX % FIELDSIZE; //wieviele Zellen ergibt der bis jetzt gefahrene Weg? Ausgabe Rest
                    if (avoidX)
                    {
                        if (lastStartCellX > endTargetX)
                        {
                            //   restWayX = -1*(restWayX);
                            currentXcell = -1 * (alrdyDrivenX / FIELDSIZE - lastStartCellX);

                        } else
                        {
                            currentXcell = alrdyDrivenX / FIELDSIZE + lastStartCellX; // schneidet int die zahl ab? denke schon... also hast du dann die fertig gefahrene distanz
                        }

                        currentYcell = startCellY;

                        // achtung schneidet kommastelle ab deshalb bin ich bereits auf "abgerundeter" zelle 
                    } else
                    {
                        if (lastStartCellY > endTargetY)
                        {
                            // restWayY = -1*(restWayY);
                            currentYcell = -1 * (alrdyDrivenX / FIELDSIZE - lastStartCellY);

                        } else
                        {
                            currentYcell = alrdyDrivenX / FIELDSIZE + lastStartCellY; // ACHTUNG Als startzelle wird bereits die endzelle angegeben da diese vorher bereits berechnet wurde
                        }
                        currentXcell = startCellX;

                    }

                    finalTarget = this.endCell;
                    comView.setStation(888);
                    switch (startPosPhi)
                    {
                        case 0:

                            rotate("S");
                            break;

                        case 90:

                            rotate("W");

                            break;

                        case 180:

                            rotate("N");

                            break;

                        case 270:

                            rotate("E");
                            break;
                        default:

                            break;

                    }

                    Thread.sleep(500);
                    // ist evtl ein timing problem dass er noch nach vorne schnellt
                    comView.breakingFactor = 100;
                    comView.setStation(0);
                    nextStep = "GO_BACK";

                    step = "WAIT_AVOID";
                }
                break;

            case "GO_BACK":

                if (avoidX)
                {
                    if (currentXcell % 2 == 0)
                    {

                        // fahre eine zelle mehr zurück wegen maschine    
                        comView.setKoords(cellRest + FIELDSIZE, 0, 0);

                        startCellX = currentXcell - 1;
                    } else
                    {
                        // fahre normal zurück

                        comView.setKoords(cellRest, 0, 0);
                        startCellX = currentXcell;
                    }
                }
                if (avoidY)
                {
                    if (currentYcell % 2 == 0)
                    {

                        // fahre eine zelle mehr zurück wegen maschine    
                        comView.setKoords(cellRest + FIELDSIZE, 0, 0);
                        startCellY = currentYcell - 1;

                    } else
                    {

                        comView.setKoords(cellRest, 0, 0);
                        startCellY = currentYcell;

                    }

                }

                nextStep = "NEW_ROUTE";
                step = "WAIT_AVOID";

                break;

            case "NEW_ROUTE":

                // *************** drehen
                if (avoidX)
                {
                    if (currentYcell <= 5)
                    {
                        rotate("S");
                        startCellY = startCellY + 2;

                        // wenn über 5te y zelle hinaus darf er nicht mehr nach unten ausweichen sondern muss nach oben
                    } else
                    {

                        rotate("N");
                        startCellY = startCellY - 2;
                    }
                }

                if (avoidY)
                {
                    if (currentXcell <= 9)
                    {
                        rotate("E");
                        startCellX = startCellX + 2;
                        // wenn über 5te y zelle hinaus darf er nicht mehr nach unten ausweichen sondern muss nach oben
                    } else
                    {

                        rotate("W");
                        startCellX = startCellX - 2;
                    }
                    firstY = true;
                }
                nextStep = "CHANGE_WAY";
                step = "WAIT_AVOID";

                break;

            case "WAIT_AVOID":

                waitRoboView();
                break;

            case "CHANGE_WAY":

                comView.setKoords(2 * FIELDSIZE, 0, 0);

                // verschieb dich zum zwei felder z.b von da an danach neuen weg evaluieren und wieder probieren
                nextStep = "TO_FINAL";
                step = "WAIT_AVOID";

                break;

            case "TO_FINAL":
                avoidX = false;
                avoidY = false;
                this.avoidX = false;
                fc.avoidTest = false;
                if (avoidY)
                {
                    // ob von oben oder unten heranfahren muss -2 oder -1 sein. (bzw +2 od +1)
                }

                //**************************** WENNN ICH IN Y AUSWEICHE DANN AUCH ZUERST IN Y FAHREN NICHT IN X ****************************************
                setEndTarget(finalTarget); // load old job from the last try and calculate the new way from the new startpositon
                takeNext = true;
                this.setStartCell(this.startCellX, startCellY);

                step = "INIT";
                break;

        }
    }

    public void waitRoboView()
    {

        // globale Variblen machen
        if (comView.getReady() == 1)
        {
            comView.setGo(1);
        } else if (comView.getEnde() == 1)
        {

            step = nextStep;
            comView.setGo(0);
        }

    }

    public void printXRoute(int deltaCellsX)
    {

        if (deltaCellsX > 0)
        {
            for (int i = 0; i <= deltaCellsX; i++)
            {
                fc.cell[printCellX + i][printCellY].setRoute(true);
                fc.cell[printCellX + i][printCellY].repaint();

            }
        } else
        {
            if (onMachine && deltaX != 0)
            {
                deltaCellsX = deltaCellsX + 2;
            }

            for (int i = 0; i >= deltaCellsX; i--)
            {
                fc.cell[printCellX + i][printCellY].setRoute(true);
                fc.cell[printCellX + i][printCellY].repaint();

            }

        }

    }

    public void printYroute(int deltaCellsY)
    {
        if (deltaCellsY > 0)
        {
            for (int i = 0; i <= deltaCellsY; i++)
            {
                fc.cell[printCellX][printCellY + i].setRoute(true);
                fc.cell[printCellX][printCellY + i].repaint();
            }
        } else
        {
            for (int i = 0; i >= deltaCellsY; i--)
            {
                fc.cell[printCellX][printCellY + i].setRoute(true);
                fc.cell[printCellX][printCellY + i].repaint();
            }

        }

    }

    public void paused() throws InterruptedException
    {
        comView.setStation(999);
        Thread.sleep(300);

        /// PAUSENSTATUS
    }

    public void rotate(String getCase) throws InterruptedException
    {
        /*        
         * neu          0° (N) 
         *              | 
         * (W) 270° - Station - 90° (E) 
         *              |
         *             180° (S)
          
         
         Plus ist nach links um       
          
         */

        switch (getCase)
        {
            case "E":

                turnPhi = -(0 - startPosPhi + 90); // minus weil nach links

                if (turnPhi < - 180) // falls kleiner 180 soll er in die andere richtung drehen
                {
                    turnPhi = 360 + turnPhi;
                }

                comView.setKoords(0, 0, turnPhi);

                startPosPhi = 90;

                break;
            case "S":

                turnPhi = -(0 - startPosPhi + 180);
                if (turnPhi < 180)
                {
                    turnPhi = -360 + turnPhi;
                }

                comView.setKoords(0, 0, turnPhi);

                startPosPhi = 180;

                break;

            case "W":

                turnPhi = -(0 - startPosPhi + 270);
                if (turnPhi < 180)
                {
                    turnPhi = -360 + turnPhi;
                }

                // Mitgegebene Wete sind(int Check,int X,int Y,int Phi)
                comView.setKoords(0, 0, turnPhi);

                startPosPhi = 270;

                break;
            case "N":

                turnPhi = -(0 - startPosPhi);
                if (turnPhi < 180)
                {
                    turnPhi = -360 + turnPhi;
                }
                comView.setKoords(0, 0, turnPhi);

                startPosPhi = 0;
                break;

            default:

                break;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public void setStartCell(int x, int y)
    {
        startCell = fc.cell[x][y];
    }

    public boolean isEndPosReached()
    {
        return endPosReached;
    }

    public void setEndPosReached(boolean endPosReached)
    {
        this.endPosReached = endPosReached;
    }

    public void setEndTarget(int x, int y)
    {
        endCell = fc.cell[x][y];
    }

    public void setEndTarget(Cell cell)
    {
        endCell = cell;
    }

    public int getStartCellX()
    {
        return startCellX;
    }

    public void setStartCellX(int startCellX)
    {
        this.startCellX = startCellX;
    }

    public int getStartCellY()
    {
        return startCellY;
    }

    public void setStartCellY(int startCellY)
    {
        this.startCellY = startCellY;
    }

    public int getStartPosPhi()
    {
        return startPosPhi;
    }

    public void setStartPosPhi(int startPosPhi)
    {
        this.startPosPhi = startPosPhi;
    }

    public Cell getStartCell()
    {
        return startCell;
    }

    public void setStartCell(Cell startCell)
    {
        this.startCell = startCell;
    }

    public boolean isRunning()
    {
        return running;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    //</editor-fold>
    // zum testen
    public static void main(String[] args) throws InterruptedException, AWTException
    {
        Drive drive = Drive.getInstance();
        ComView comView = ComView.getInstance();
        JobController jc = JobController.getInstance();
        FieldCommander fc = FieldCommander.getInstance();

        fc.setVisible(true);

        comView.start();

        drive.setStartCell(13, 7);
        drive.setEndTarget(10, 1);
        drive.setStartPosPhi(180);

        drive.start();
    }
}
