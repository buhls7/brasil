package Tools;

import java.awt.Button;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Schreiben von Objekten
 *
 * @author roa
 * @version 1.0
 */
public class ObjFile
{
  public static boolean fileExists(String filename)
  {
    File f=new File(filename);    
    return f.exists();
  }
  
  public static void obj2FileXML(Object obj,String filename)
  {  
    try
    {
      FileOutputStream datAus = new FileOutputStream(filename);       // Datenstrom zum Schreiben in die Datei erzeugen      
      BufferedOutputStream oAus = new BufferedOutputStream(datAus);       // Objektstrom darueber legen
      XMLEncoder oXML=new XMLEncoder(oAus);                           // Umwandlung in XML 
      oXML.writeObject(obj);                                          // Datensaetze in die Datei schreiben            
      oXML.close();                                                   // Dateistrom schliessen      
    } catch (FileNotFoundException e)
    {
      System.out.println("Fehler beim Schreiben: " + e);
    }
  }
    
  public static void obj2File(Object obj, String filename)
  {
    try
    {
      FileOutputStream datAus = new FileOutputStream(filename);       // Datenstrom zum Schreiben in die Datei erzeugen      
      ObjectOutputStream oAus = new ObjectOutputStream(datAus);       // Objektstrom darueber legen
      oAus.writeObject(obj);                                          // Datensaetze in die Datei schreiben            
      oAus.close();                                                   // Dateistrom schliessen      
    } catch (IOException e)
    {
      System.out.println("Fehler beim Schreiben: " + e);
    }
  }

  public static Object fileXML2Obj(String filename)
  {
    Object obj = null;
    try
    {
      FileInputStream datEin = new FileInputStream(filename);         // Datenstrom zum Lesen aus der Datei erzeugen      
      BufferedInputStream oEin;                                         // Objektstrom darueberlegen
      oEin = new BufferedInputStream(datEin);
      XMLDecoder xmlInput=new XMLDecoder(oEin);                       // Umwandlung von XML
      obj = xmlInput.readObject();                                        // Datensatz aus der Datei lesen und deren Datensatzfelder
      System.out.println(obj);                                        // zur Kontrolle auf den Bildschirm ausgeben

      oEin.close();                                                   // Dateistrom schliessen
    } catch (IOException e)
    {
      System.out.println("Fehler beim Lesen: " + e);
    }
    return obj;
  }

  public static Object file2Obj(String filename)
  {
    Object obj = null;
    try
    {
      FileInputStream datEin = new FileInputStream(filename);         // Datenstrom zum Lesen aus der Datei erzeugen      
      ObjectInputStream oEin;                                         // Objektstrom darueberlegen
      oEin = new ObjectInputStream(datEin);

      obj = oEin.readObject();                                        // Datensatz aus der Datei lesen und deren Datensatzfelder
      System.out.println(obj);                                        // zur Kontrolle auf den Bildschirm ausgeben

      oEin.close();                                                   // Dateistrom schliessen
    } catch (IOException | ClassNotFoundException e)
    {
      System.out.println("Fehler beim Lesen: " + e);
    }
    return obj;
  }
  
  public static void main(String[] args)
  {   
    String filename = "MeineDaten.dat";
    ObjFile.obj2FileXML(new Button("hello"), filename);  
    Button btn=(Button)ObjFile.fileXML2Obj(filename);
    System.out.println(btn);
  }
}