package Sequence;

import ComView.ComView;
import FieldCommander.FieldCommander;
import MainPack.Main;
import Refbox.Handler;
import Tools.Maintenance;

import Traveling.Drive;
import java.awt.AWTException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos;

/**
 *
 * @author alain controls the states and the phases of the competition is
 * responsable for the flow of the game
 */
public class StateMachine extends Thread
{

    //------> vorläufig manuell anpassen 
    private final String STARTPHI = "E";
    private final int BACKWAY = 240;
    private static StateMachine instance = null;
    private boolean running;
    private ComView comView;
    private JobController way;
    private Handler handler;
    private Drive drive;
    private FieldCommander fc;
    private String phase;//="EXPLORATION"; 
    private String state;//="RUNNING";
    private String exploStep = "IN_FIELD";
    private String prodStep;
    private int ende = 0;
    private int ready = 0;
    public int phaseInt;
    private int prodCount = 0;
    private int deliveryCount = 0;
    private String BrownT5;
    private String PinkT1;
    private String PinkT2;
    private String PinkT3;
    private String BlondT1;
    private String BlondT2;
    private String BlondT4;
    private boolean prodFinalStep = false;
    private boolean firstProd = true;
    private boolean begin = true;

    String nextStep;
    Maintenance mtc;
    public static StateMachine getInstance()
    {
        return instance;
    }

    // Lampen Farben der Maschinen in der Explorationsphase
    public StateMachine(Handler handler, JobController way) throws InterruptedException, AWTException
    {
        this.handler = handler;
        comView = ComView.getInstance();
        fc = FieldCommander.getInstance();
         mtc = new Maintenance ();

        this.way = way;

        instance = this;
        drive = Drive.getInstance();
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    @Override
    public void run()
    {
        while (running)
        {
            state = handler.gameState;
            phase = handler.gamePhase;

            try
            {
                Thread.sleep(500);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(StateMachine.class.getName()).log(Level.SEVERE, null, ex);
            }

            switch (state)
            {
                case "INIT":
                    Main.log.info("INIT");
                    break;
                case "WAIT_START":  //Kommentar für Wait Start
                    Main.log.info("WAIT_START");
                    break;
                case "RUNNING":
                    Main.log.info("RUNNING");
                    break;
                case "PAUSED":
                    Main.log.info("PAUSED");
                    drive.paused = true;
                    break;
            }

            switch (phase)
            {
                case "SETUP":
                    Main.log.info("SETUP");
                    preGamePhase();
                    break;
                case "EXPLORATION":

                    if (!exploStep.equals("PROD_POS"))
                    {
                        Main.log.info("EXPLORATION");

                        try
                        {
                            explorationPhase();
                        } catch (InterruptedException ex)
                        {
                            Main.log.error("InterruptException in EXPLORATION");
                        }
                    }

                    break;
                case "PRODUCTION":

                    if (firstProd)
                    {
                        prodStep = "START_ROBO";
                        this.firstProd = false;
                    }

                    try
                    {
                        productionPhase();
                    } catch (InterruptedException ex)
                    {
                        Main.log.error("InterruptException in PRODUCTION");
                    }
                    break;
                case "POST_GAME":
                    postGame();
                    break;
            }
        }
    }

    private void preGamePhase()
    {
        comView.setStation(0);
        comView.setKoords(0, 0, 0);
        exploStep = "IN_FIELD";
    }

    private void explorationPhase() throws InterruptedException
    {
        switch (exploStep) //Step in ExploPhase
        {

            case "IN_FIELD":
                // von aussen ins feld hineinfahren
                JobController job = (JobController) mtc.maintenanceRead("maintenance.dat");
                way.jobCounter = job.jobCounter;
                
                
                Thread.sleep((Main.jerseyNr - 1) * 2000);

                //******************* DAVON AUSGEGANGEN ES WIRD VON 180° GESTARTET UND IN X AUF EINER ZELLENMITTE *************************
                comView.setKoords(way.getStartCell().getRealY() - way.getStartKoordY(),0,0);
                
                nextStep = "START_ROBO";

                exploStep = "WAIT_VIEW";
                break;
            case "START_ROBO":

                Main.log.debug("************* STEP START_ROBO*****************");

                drive.setStartCell(way.getStartCell());
                drive.setStartPosPhi(180);

                /* drive.goStart(way.getStartKoordX(), way.getStartKoordY(), STARTPHI);
                 drive.setStartPosPhi(180);*/
                //******************ACHTUNG: Methode für ins Feld hineinfahren noch nicht implementiert
                exploStep = "TO_START";
                break;

            case "TO_START":
;
                drive.setEndTarget(way.getLoadCellNearMachine(way.getExploJob())); // setEndTarget wird auf die Zelle direkt vor der Maschine gestzt
                drive.takeNext = true;
                

                Main.log.debug(way.getExploJob());
                if (begin)
                {
                    drive.start();
                    begin = false;
                }
                Main.log.debug(way.getLoadCellNearMachine(way.getExploJob()).getX());
                Main.log.debug(way.getLoadCellNearMachine(way.getExploJob()).getY());
                mtc.maintenanceWrite(way, "maintenance.dat");
                exploStep = "WAIT";
                break;

            case "WAIT":
                if (drive.isEndPosReached())
                {
                    exploStep = "AT_STATION";
                    drive.endPosReached = false;
                }

                break;

            case "AT_STATION":

                comView.setKoords(0, 0, 0);

                Main.log.debug(">>>> MASCHINE" + way.getExploJob() + " ERREICHT <<<<<");

                comView.setStation(1); // es soll nun ins stationsanfahren gehen

                nextStep = "BACK";
                exploStep = "WAIT_VIEW";
                break;

            case "WAIT_VIEW":

                waitRoboView();

                break;

            case "BACK":
                comView.setStation(0);
                Main.log.debug("backward");

                int[] lamp = comView.getLamp();
                Main.log.debug("************* STEP BACK*****************");

                handler.sendMachine(way.getExploJob(), lamp); // hier wird der string der aktuellen maschine an die refbox gesendet
                Main.log.debug("STATION: " + way.getExploJob() + String.valueOf(lamp));

                comView.setKoords(-BACKWAY, 0, 0);
                nextStep = "NEXT";
                exploStep = "WAIT_VIEW";

                break;

            case "NEXT":
                comView.setStation(0);
                try
                {
                    way.setNextExploJob(); // hier wird der job auf die nächste nummer gesetzt
                    exploStep = "TO_START";

                } catch (Exception ex)
                {
                    exploStep = "PROD_POS";
                }

                break;

            case "PROD_POS":

                break;

            default:
                Main.log.warn("!!!!! OUT OFF PROGRAM !!!!!");
                comView.setKoords(0, 0, 0);
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
            if (phase.equals("EXPLORATION"))
            {
                exploStep = nextStep;
            }
            if (phase.equals("PRODUCTION"))
            {
                prodStep = nextStep;
            }

            comView.setGo(0);
        }

    }

    private void productionPhase() throws InterruptedException
    {

        // readAndSendPhase();
        /**
         * ***********************************************************************************************************
         * ***********************************************************************************************************
         * Production state machine for Brown (1)
         * ***********************************************************************************************************
         * ***********************************************************************************************************
         */
        //<editor-fold defaultstate="collapsed" desc="Production for Mr.Brown">
        if (way.getRoboNameIdx() == 1) {

            switch (prodStep) //Step in production
            {

                case "START_ROBO":      //Testing step
                    BrownT5 = way.getProdMachine("T5", 1);
                    Main.log.debug("************* STEP START_ROBO*****************");

                    drive.setStartCell(2, 1);
                    drive.setStartPosPhi(180);


                    /* drive.goStart(way.getStartKoordX(), way.getStartKoordY(), STARTPHI);
                     drive.setStartPosPhi(180);*/
                    //******************ACHTUNG: Methode für ins Feld hineinfahren noch nicht implementiert
                    prodStep = "TO_PUCK_AREA";
                    break;

                case "TO_PUCK_AREA":

                    comView.setStation(0);
                    drive.setEndTarget(way.getPuckCell());
                    if (begin) {
                        drive.start();
                        begin = false;
                    }
                    drive.takeNext = true;
                    prodStep = "WAIT_DRIVE";
                    break;

                case "CATCH":
                    Main.log.debug("************* STEP CATCH *****************");
                    comView.setKoords(0, 0, 0);

                    Main.log.debug("CATCH THE PUCK");

                    comView.setStation(3); // es soll nun ins stationsanfahren gehen
                    nextStep = "TO_T5";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "TO_T5":
                    Main.log.debug("************* STEP TO_T5" + way.getLoadCellNearMachine(BrownT5) + " *****************");
                    comView.setStation(0);
                    drive.setStartPosPhi(180);
                    //   drive.setEndTarget(way.getLoadCellNearMachine(BrownT5));
                    drive.setEndTarget(way.getLoadCellNearMachine("M9"));
                    drive.takeNext = true;
                    prodStep = "WAIT_DRIVE";
                    break;

                case "STATION":
                    Main.log.debug("********************  STATION DOCKING  ********************");
                    comView.setKoords(0, 0, 0);

                    comView.setStation(6); // Wait for the finish light (green)

                    nextStep = "TAKE_PUCK";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "TAKE_PUCK":
                    Main.log.debug("************* STEP TAKE_PUCK *****************");
                    comView.setStation(0);
                    comView.setKoords(0, 80, 180);
                    nextStep = "GO_BACK_TO_CELL_NEAR_MACHINE_T5";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "GO_BACK_TO_CELL_NEAR_MACHINE_T5":
                    Main.log.debug("*************  GO_BACK_TO_CELL_NEAR_MACHINE_T5  *****************");
                    comView.setStation(0);
                    comView.setKoords(240, 80, 0);
                    nextStep = "TO_DELIVERY";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "TO_DELIVERY":
                    Main.log.debug("************* STEP TO_DELIVERY * " + way.getDeliveryCell() + "****************");
                    drive.setStartPosPhi(way.getDirectionOfMachine("M9"));
                    drive.setEndTarget(way.getDeliveryCell());
                    drive.takeNext = true;
                    prodStep = "WAIT_DRIVE";

                    break;

                case "SCAN_DELIVERY":
                    Main.log.debug("********************  SCAN DELIVERY  ********************");
                    comView.setKoords(0, 0, 0);

                    comView.setStation(4); // es soll nun ins stationsanfahren gehen

                    nextStep = "ANALYSE_DELIVERY";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "ANALYSE_DELIVERY":
                    Main.log.debug("********************  ANALYSE DELIVERY  ********************");
                    if (comView.getLamp()[2] >= 1) {
                        Main.log.debug("********************  DELIVERY OPEN  ********************");
                        prodStep = "DELIVER";
                    } else {
                        deliveryCount++;
                        switch (deliveryCount) {
                            case 1:
                                Main.log.debug("********************  DELIVERY CLOSE  ********************");
                                prodStep = "GO_LEFT_DELIVERY";
                                break;
                            case 2:
                                Main.log.debug("********************  DELIVERY CLOSE  ********************");
                                prodStep = "GO_RIGHT_DELIVERY";
                                break;
                            default:
                                Main.log.debug("********************  ERROR RESTART CYCLE  ********************");
                                prodStep = "DELIVER";
                                break;
                        }

                    }
                    break;

                case "GO_LEFT_DELIVERY":
                    Main.log.debug("************* STEP GO_LEFT_DELIVERY *****************");
                    comView.setStation(0);
                    comView.setKoords(0, 350, 0);
                    nextStep = "SCAN_DELIVERY";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "GO_RIGHT_DELIVERY":
                    Main.log.debug("************* STEP GO_RIGHT_DELIVERY *****************");
                    comView.setStation(0);
                    comView.setKoords(0, -700, 0);
                    nextStep = "SCAN_DELIVERY";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "DELIVER":
                    Main.log.debug("********************  DELIVERY DOCKING  ********************");
                    comView.setKoords(0, 0, 0);

                    comView.setStation(4); // es soll nun ins stationsanfahren gehen

                    nextStep = "BACK";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "BACK":
                    Main.log.debug("************* STEP BACK *****************");
                    comView.setStation(0);
                    switch (deliveryCount) {
                        case 0:
                            comView.setKoords(-BACKWAY, 0, 0);
                            break;
                        case 1:
                            comView.setKoords(-BACKWAY, -350, 0);
                            break;
                        case 2:
                            comView.setKoords(-BACKWAY, 350, 0);
                            break;
                    }

                    nextStep = "FINISH";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "FINISH":
                    Main.log.debug("************* STEP FINISH *****************");
                    deliveryCount = 0;
                    prodCount = 0;
                    prodStep = "TO_PUCK_AREA";
                    break;

                // Waiting steps (Roboview and Drive)
                case "WAIT_ROBOVIEW":
                    Main.log.debug("************* STEP WAIT_ROBOVIEW *****************");

                    waitRoboView();
                    break;

                case "WAIT_DRIVE":
                    Main.log.debug("************* STEP WAIT_DRIVE *****************");
                    if (drive.isEndPosReached()) {
                        prodCount++;
                        drive.endPosReached = false;
                        switch (prodCount) {
                            case 1:
                                prodStep = "CATCH";
                                break;
                            case 2:
                                prodStep = "STATION";
                                break;
                            case 3:
                                prodStep = "SCAN_DELIVERY";
                                break;

                        }

                    }
                    break;

                default:
                    Main.log.debug("!!!!! OUT OFF PROGRAM !!!!!");
                    comView.setKoords(0, 0, 0);
                    break;

            }
        }
//</editor-fold>
        /**
         * ************************************************************************************************************
         * ************************************************************************************************************
         * Production state machine for Pink (0) and Blond (2)
         * ************************************************************************************************************
         * ************************************************************************************************************
         */
        //<editor-fold defaultstate="collapsed" desc="Production for Mr.Pink and Mr.Blond">
        if ((way.getRoboNameIdx() == 2) || (way.getRoboNameIdx() == 0))
        {

            switch (prodStep) //Step in production
            {

                case "START_ROBO":

                    Main.log.debug("************* STEP START_ROBO*****************");
                    PinkT1 = way.getProdMachine("T1", 1);
                    PinkT2 = way.getProdMachine("T2", 1);
                    PinkT3 = way.getProdMachine("T3", 1);
                    BlondT1 = way.getProdMachine("T1", 2);
                    BlondT2 = way.getProdMachine("T2", 2);
                    BlondT4 = way.getProdMachine("T4", 1);

                    drive.setStartCell(2, 1);       //Only for test
                    drive.setStartPosPhi(180);      //Only for test

                    prodStep = "TO_PUCK_AREA";
                    break;

                case "TO_PUCK_AREA":

                    comView.setStation(0);
                    drive.setEndTarget(way.getPuckCell());
                    if (begin)                      //Only for test
                    {
                        drive.start();
                        begin = false;
                    }
                    drive.takeNext = true;
                    prodStep = "WAIT_DRIVE";
                    break;

                case "CATCH":
                    Main.log.debug("************* STEP CATCH *****************");
                    comView.setKoords(0, 0, 0);

                    Main.log.debug("CATCH THE PUCK");

                    comView.setStation(3); // es soll nun ins stationsanfahren gehen
                    switch (prodCount)
                    {
                        case 1:
                            nextStep = "TO_T1";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 3:
                            nextStep = "TO_T2";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 7:
                            nextStep = "TO_T1";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 9:
                            nextStep = "TO_T3/4";
                            prodStep = "WAIT_ROBOVIEW";
                            break;

                    }
                    break;

                case "TO_T1":
                    if (way.getRoboNameIdx() == 0)          //Machine for Mr.Pink
                    {
                        Main.log.debug("************* STEP TO_T1" + way.getLoadCellNearMachine(PinkT1) + " *****************");
                        comView.setStation(0);
                        drive.setStartPosPhi(180);
                        drive.setEndTarget(way.getLoadCellNearMachine(PinkT1));

                        drive.takeNext = true;
                        prodStep = "WAIT_DRIVE";
                    } else                                  //Machine for Mr.Blond
                    {
                        Main.log.debug("************* STEP TO_T1" + way.getLoadCellNearMachine(BlondT1) + " *****************");
                        comView.setStation(0);
                        drive.setStartPosPhi(180);
                        drive.setEndTarget(way.getLoadCellNearMachine(BlondT1));

                        drive.takeNext = true;
                        prodStep = "WAIT_DRIVE";
                    }
                    break;

                case "TO_T2":
                    if (way.getRoboNameIdx() == 0)          //Machine for Mr.Pink
                    {
                        Main.log.debug("************* STEP TO_T2" + way.getLoadCellNearMachine(PinkT2) + " *****************");
                        comView.setStation(0);
                        drive.setStartPosPhi(180);
                        drive.setEndTarget(way.getLoadCellNearMachine(PinkT2));

                        drive.takeNext = true;
                        prodStep = "WAIT_DRIVE";
                    } else                                  //Machine for Mr.Blond
                    {
                        Main.log.debug("************* STEP TO_T2" + way.getLoadCellNearMachine(BlondT2) + " *****************");
                        comView.setStation(0);
                        drive.setStartPosPhi(180);
                        drive.setEndTarget(way.getLoadCellNearMachine(BlondT2));

                        drive.takeNext = true;
                        prodStep = "WAIT_DRIVE";
                    }
                    break;

                case "TO_T3/4":
                    if (way.getRoboNameIdx() == 0)          //Machine for Mr.Pink
                    {
                        Main.log.debug("************* STEP TO_T3" + way.getLoadCellNearMachine(PinkT3) + " *****************");
                        comView.setStation(0);
                        drive.setStartPosPhi(180);
                        drive.setEndTarget(way.getLoadCellNearMachine(PinkT3));

                        drive.takeNext = true;
                        prodStep = "WAIT_DRIVE";
                    } else                                  //Machine for Mr.Blond
                    {
                        Main.log.debug("************* STEP TO_T4" + way.getLoadCellNearMachine(BlondT4) + " *****************");
                        comView.setStation(0);
                        drive.setStartPosPhi(180);
                        drive.setEndTarget(way.getLoadCellNearMachine(BlondT4));

                        drive.takeNext = true;
                        prodStep = "WAIT_DRIVE";
                    }
                    break;
                case "TO_WAITING_CELL_PINK":
                    Main.log.debug("************* STEP TO_WAITING_CELL_PINK *****************");
                    comView.setStation(0);
                    drive.setStartPosPhi(180);
                    drive.setEndTarget(7, 8);

                    drive.takeNext = true;
                    prodStep = "WAIT_DRIVE";
                    break;

                case "TO_WAITING_CELL_BLOND":
                    Main.log.debug("************* STEP TO_WAITING_CELL_PINK *****************");
                    comView.setStation(0);
                    drive.setStartPosPhi(180);
                    drive.setEndTarget(11, 8);

                    drive.takeNext = true;
                    prodStep = "WAIT_DRIVE";
                    break;

                case "STATION_DOCKING":
                    Main.log.debug("********************  PUCK_PLACED  ********************");
                    comView.setKoords(0, 0, 0);

                    comView.setStation(1); // It don't care about the light
                    switch (prodCount)
                    {
                        case 2://charge first s0 on T1 
                            nextStep = "BACK_STANDARD";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 6:
                            nextStep = "BACK_STANDARD";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 8: //charge second s0 on t1
                            nextStep = "BACK_STANDARD";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 14://charge s1 on t3
                            nextStep = "BACK_STANDARD";
                            prodStep = "WAIT_ROBOVIEW";
                            break;

                    }
                    break;

                case "PUCK_SCANNED":
                    Main.log.debug("********************  PUCK_SCANNED  ********************");
                    comView.setKoords(0, 0, 0);

                    comView.setStation(5); // Wait for intermediate light (orange)
                    switch (prodCount)
                    {
                        case 4:
                            nextStep = "MOVE_LEFT_SIDE_A";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 10:
                            nextStep = "MOVE_LEFT_SIDE_A";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 12:
                            nextStep = "MOVE_RIGHT_SIDE_A";
                            prodStep = "WAIT_ROBOVIEW";
                            break;

                    }
                    break;
                case "PUCK_FINISHED":
                    Main.log.debug("********************  PUCK_FINISHED  ********************");
                    comView.setKoords(0, 0, 0);

                    comView.setStation(6); // Wait for the final light (green)
                    switch (prodCount)
                    {
                        case 5:                 //Take the finished puck from T1
                            nextStep = "PUCK_TAKEN_A";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 9:                 //case for deliver
                            nextStep = "PUCK_TAKEN_A";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 11:
                            nextStep = "PUCK_TAKEN_A";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 13:
                            nextStep = "PUCK_TAKEN_A";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                    }
                    break;
                case "PUCK_TAKEN_A":
                    Main.log.debug("************* PUCK_TAKEN_A *****************");
                    comView.setStation(0);
                    comView.setKoords(0, 80, 180);
                    nextStep = "PUCK_TAKEN_B";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "PUCK_TAKEN_B":
                    Main.log.debug("************* PUCK_TAKEN_B *****************");
                    comView.setStation(0);
                    comView.setKoords(240, 80, 0);
                    switch (prodCount)
                    {
                        case 5:
                            nextStep = "TO_T2";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 9:             //case for waiting cell
                            if (way.getRoboNameIdx() == 0)
                            {
                                nextStep = "TO_WAITING_CELL_PINK";
                                prodStep = "WAIT_ROBOVIEW";
                            }
                            if (way.getRoboNameIdx() == 2)
                            {
                                nextStep = "TO_WAITING_CELL_BLOND";
                                prodStep = "WAIT_ROBOVIEW";
                            }
                            break;
                        case 11:
                            nextStep = "TO_T3/4";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 13:
                            nextStep = "TO_T3/4";
                            prodStep = "WAIT_ROBOVIEW";
                            break;

                    }
                    break;

                case "MOVE_LEFT_SIDE_A":
                    Main.log.debug("************* MOVE_LEFT_SIDE_A *****************");
                    comView.setStation(0);
                    comView.setKoords(0, 100, 0);
                    nextStep = "MOVE_LEFT_SIDE_B";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "MOVE_LEFT_SIDE_B":
                    Main.log.debug("************* MOVE_LEFT_SIDE_B *****************");
                    comView.setStation(0);
                    comView.setKoords(80, 0, 0);
                    nextStep = "BACK_FROM_LEFT_SIDE";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "MOVE_RIGHT_SIDE_A":
                    Main.log.debug("************* MOVE_RIGHT_SIDE_A *****************");
                    comView.setStation(0);
                    comView.setKoords(0, -100, 0);
                    nextStep = "MOVE_RIGHT_SIDE_B";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "MOVE_RIGHT_SIDE_B":
                    Main.log.debug("************* MOVE_RIGHT_SIDE_B *****************");
                    comView.setStation(0);
                    comView.setKoords(80, 0, 0);
                    nextStep = "BACK_FROM_RIGHT_SIDE";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "BACK_FROM_LEFT_SIDE":
                    Main.log.debug("************* BACK_FROM_LEFT_SIDE *****************");
                    comView.setStation(0);
                    comView.setKoords(-80 - BACKWAY, -100, 0);
                    switch (prodCount)
                    {
                        case 4:
                            nextStep = "TO_T1";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 10:
                            nextStep = "TO_T2";
                            prodStep = "WAIT_ROBOVIEW";
                            break;

                    }
                    break;

                case "BACK_FROM_RIGHT_SIDE":
                    Main.log.debug("************* BACK_FROM_RIGHT_SIDE *****************");
                    comView.setStation(0);
                    comView.setKoords(-80 - BACKWAY, 100, 0);

                    nextStep = "TO_T1";
                    prodStep = "WAIT_ROBOVIEW";

                    break;

                case "BACK_STANDARD":
                    Main.log.debug("************* STEP BACK *****************");
                    comView.setStation(0);
                    comView.setKoords(-BACKWAY, 0, 0);
                    switch (prodCount)
                    {
                        case 2:
                            nextStep = "TO_PUCK_AREA";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 6:
                            nextStep = "TO_PUCK_AREA";
                            prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 8:                 //Division between normal way and delivery
                            if (prodFinalStep != true)
                            {
                                nextStep = "TO_PUCK_AREA";
                                prodStep = "WAIT_ROBOVIEW";
                            } else
                            {
                                nextStep = "TO_T3/4";
                                prodStep = "WAIT_ROBOVIEW";
                            }
                            break;
                        case 11:
                             nextStep = "FINISH";
                                prodStep = "WAIT_ROBOVIEW";
                            break;
                        case 14:    //last step and restart
                            prodFinalStep = true;
                            prodCount = 0;
                            nextStep = "TO_PUCK_AREA";
                            prodStep = "WAIT_ROBOVIEW";
                            break;

                    }
                    break;

                //Delivery steps
                case "TO_DELIVERY":
                    Main.log.debug("************* STEP TO_DELIVERY *****************");
                    drive.setStartPosPhi(180);
                    drive.setEndTarget(way.getDeliveryCell());
                    drive.takeNext = true;
                    prodStep = "WAIT_DRIVE";

                    break;

                case "SCAN_DELIVERY":
                    Main.log.debug("********************  SCAN DELIVERY  ********************");
                    comView.setKoords(0, 0, 0);

                    comView.setStation(4); // es soll nun ins stationsanfahren gehen

                    nextStep = "ANALYSE_DELIVERY";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "ANALYSE_DELIVERY":
                    Main.log.debug("********************  ANALYSE DELIVERY  ********************");
                    if (comView.getLamp()[2] >= 1)
                    {
                        Main.log.debug("********************  DELIVERY OPEN  ********************");
                        prodStep = "DELIVER";
                    } else
                    {
                        deliveryCount++;
                        switch (deliveryCount)
                        {
                            case 1:
                                Main.log.debug("********************  DELIVERY CLOSE  ********************");
                                prodStep = "GO_LEFT_DELIVERY";
                                break;
                            case 2:
                                Main.log.debug("********************  DELIVERY CLOSE  ********************");
                                prodStep = "GO_RIGHT_DELIVERY";
                                break;
                            default:
                                Main.log.debug("********************  ERROR RESTART CYCLE  ********************");
                                prodStep = "DELIVER";
                                break;
                        }

                    }
                    break;

                case "GO_LEFT_DELIVERY":
                    Main.log.debug("************* STEP GO_LEFT_DELIVERY *****************");
                    comView.setStation(0);
                    comView.setKoords(0, 350, 0);
                    nextStep = "SCAN_DELIVERY";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "GO_RIGHT_DELIVERY":
                    Main.log.debug("************* STEP GO_RIGHT_DELIVERY *****************");
                    comView.setStation(0);
                    comView.setKoords(0, 700, 0);
                    nextStep = "SCAN_DELIVERY";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "DELIVER":
                    Main.log.debug("********************  DELIVERY DOCKING  ********************");
                    comView.setKoords(0, 0, 0);

                    comView.setStation(5); // es soll nun ins stationsanfahren gehen

                    nextStep = "BACK_STANDARD";
                    prodStep = "WAIT_ROBOVIEW";
                    break;

                case "FINISH":
                    Main.log.debug("************* STEP FINISH *****************");
                    deliveryCount = 0;
                    prodCount = 9;
                    prodStep = "TO_PUCK_AREA";
                    break;

                // Waiting steps (Roboview and Drive)
                case "WAIT_ROBOVIEW":
                    Main.log.debug("************* STEP WAIT_ROBOVIEW *****************");

                    waitRoboView();
                    break;

                case "WAIT_DRIVE":
                    Main.log.debug("************* STEP WAIT_DRIVE *****************");
                    if (drive.isEndPosReached())
                    {

                        drive.endPosReached = false;

                        prodCount++;
                        switch (prodCount)
                        {
                            case 1:
                                prodStep = "CATCH";
                                break;
                            case 2:
                                prodStep = "STATION_DOCKING";
                                break;
                            case 3:
                                prodStep = "CATCH";
                                break;
                            case 4:
                                prodStep = "PUCK_SCANNED";
                                break;
                            case 5:
                                prodStep = "PUCK_FINISHED";
                                break;
                            case 6:
                                prodStep = "STATION_DOCKING";
                                break;
                            case 7:
                                prodStep = "CATCH";
                                break;
                            case 8:
                                prodStep = "STATION_DOCKING";
                                break;
                            case 9:              //Division between normal way and delivery
                                if (prodFinalStep != true)
                                {
                                    prodStep = "CATCH";
                                } else
                                {
                                    prodStep = "PUCK_FINISHED";
                                }
                                break;
                            case 10:
                                if (prodFinalStep != true)
                                {
                                    prodStep = "PUCK_SCANNED";
                                } else
                                {
                                    if (way.getRoboNameIdx() == 0)
                                    {
                                        if (way.isProductDelGateOpen("P1").ANY.getNumber()==2)
                                        {
                                            prodStep = "TO_DELIVERY";
                                        }
                                    }
                                    if (way.getRoboNameIdx() == 2)
                                    {
//                                        if (way.isProductDelGateOpen("P2"))
//                                        {
//                                            prodStep = "TO_DELIVERY";
//                                        }
                                    }
                                }

                                break;
                            case 11:
                                if (prodFinalStep != true)
                                {
                                prodStep = "PUCK_FINISHED";
                                }
                                else
                                {
                                prodStep = "SCAN_DELIVERY";    
                                }
                                break;
                            case 12:
                                prodStep = "PUCK_SCANNED";
                                break;
                            case 13:
                                prodStep = "PUCK_FINISHED";
                                break;
                            case 14:
                                prodStep = "STATION_DOCKING";
                                break;

                        }

                    }
                    break;

                default:
                    Main.log.debug("!!!!! OUT OFF PROGRAM !!!!!");
                    comView.setKoords(0, 0, 0);
                    break;
            }

        }
        //</editor-fold>
    }

    private void postGame()
    {
    }
}
