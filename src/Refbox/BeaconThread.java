/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Refbox;

import org.robocup_logistics.llsf_comm.ProtobufBroadcastPeer;
import org.robocup_logistics.llsf_comm.ProtobufMessage;
import org.robocup_logistics.llsf_msgs.BeaconSignalProtos;
import org.robocup_logistics.llsf_msgs.TeamProtos;
import org.robocup_logistics.llsf_msgs.TimeProtos;
import org.robocup_logistics.llsf_utils.NanoSecondsTimestampProvider;

/**
 *
 * @author alain.rohr
 */
public class BeaconThread implements Runnable
{
  ProtobufBroadcastPeer peer;
  String TEAM_NAME;
  TeamProtos.Team TEAM_COLOR;
  public BeaconThread(ProtobufBroadcastPeer peer,String TEAM_NAME,TeamProtos.Team TEAM_COLOR)
  {
    this.peer=peer;
    this.TEAM_NAME=TEAM_NAME;
    this.TEAM_COLOR=TEAM_COLOR;
  }
    
  public void run()
  {
    while (true)
    {
      try
      {
        Thread.sleep(2000);
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }

      NanoSecondsTimestampProvider nstp = new NanoSecondsTimestampProvider();

      long ms = System.currentTimeMillis();
      long ns = nstp.currentNanoSecondsTimestamp();

      int sec = (int) (ms / 1000);
      long nsec = ns - (ms * 1000000L);

      TimeProtos.Time t = TimeProtos.Time.newBuilder().setSec(sec).setNsec(nsec).build();
      BeaconSignalProtos.BeaconSignal bs = BeaconSignalProtos.BeaconSignal.newBuilder().
        setTime(t).setSeq(1).setNumber(1).setPeerName("R-1").setTeamName(TEAM_NAME).setTeamColor(TEAM_COLOR).build();

      ProtobufMessage msg = new ProtobufMessage(2000, 1, bs);
      peer.enqueue(msg); //private
    }
  }
}
