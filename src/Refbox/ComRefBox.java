/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           07.06.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package Refbox;

import java.awt.AWTException;
import java.io.IOException;
import java.util.Observer;
import org.robocup_logistics.llsf_comm.ProtobufBroadcastPeer;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos.ExplorationInfo;
import org.robocup_logistics.llsf_msgs.GameStateProtos.GameState;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.MachineInfo;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos.OrderInfo;

/**
 *
 * @author stecm1
 */
public class ComRefBox
{
  public Handler handler;
  public ProtobufBroadcastPeer peer;

  public int gamePoints;             // Aktueller Punktestand
  public String gamePhase;           // Aktuelle Game Phase (PRE_GAME, EXPLORATION, PRODUCTION, POST_GAME)
  public String gameState;           // Aktueller Game Status (WAIT_STRT, RUNNING, PAUSED)
  public boolean hasTime;            // Ist noch zeit zur Verfügung
  public int gameTime;               // Aktuelle Spielzeit (EXPLORATION: 0-180, PRODUCTION: 0-900)

  public ComRefBox(String ip, int portIn, int portOut) throws AWTException
  {
    // --------------------------- Verbinden mit Refbox per UDP ----------------------------
    peer = new ProtobufBroadcastPeer(ip, portIn, portOut);
    //peer.<AttentionMessage>add_message(AttentionMessage.class);//refbox --> controller Client-Server
    //peer.<VersionInfo>add_message(VersionInfo.class);//refbox --> any  P2P&C-S
    peer.<ExplorationInfo>add_message(ExplorationInfo.class);//alle 1sec refbox --> robots        
    peer.<GameState>add_message(GameState.class);
    peer.<MachineInfo>add_message(MachineInfo.class);  //0.25sec ??? refbox --> all P2P & C-S    
    peer.<OrderInfo>add_message(OrderInfo.class);//any P2P & C-S        
    //peer.<PuckInfo>add_message(PuckInfo.class);  // 1sec ok refbox --> controller Client-Server
    //peer.<RobotInfo>add_message(RobotInfo.class);
    //peer.<RoboPos>add_message(RoboPos.class);      
    
    handler = new Handler(peer);
    peer.register_handler(handler);
    
    try
    {
      peer.start();

    } catch (IOException e)
    {
      e.printStackTrace();
    }

    peer.register_handler(handler); // muss sicher erst nachher geschehen, da nachricht im handler sonst nicht an peer gehängt werden kann

  }

  public void addObserver(Observer observer)
  {
    handler.addObserver(observer);
  }

  public String getState()
  {
    return handler.getState();
  }

  /**
   *
   * @return Gibt die aktuelle Spiel Phase zurück (PRE_GAME, EXPLORATION,
   * PRODUCTION, POST_GAME).
   */
  public String getPhase()
  {
    return handler.getPhase();
  }

  /**
   *
   * @return Git an ob noch Spielzeit vorhanden ist.
   */
  public String getHasTime()
  {
    return handler.getHasTime();
  }

  /**
   *
   * @return Gibt die aktuelle Spielzeit zurück (EXPLORATION: 0-180, PRODUCTION:
   * 0-900).
   */
  public String getTime()
  {
    return handler.getTime();
  }

  /**
   *
   * @return Gibt den Aktuellen Punktestand zurück.
   */
  public String getPoints()
  {
    return handler.getPoints();
  }
}