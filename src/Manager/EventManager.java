/*
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import Binding.EventBinding;
import Object.Event;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;




public class EventManager {
    
    private byte[]                                                              keyBytes;
    private DatabaseEntry                                                       key;
    private DatabaseEntry                                                       data;
    private static EventManager m_instance;
    private Cursor cursor = null;
    
    public EventManager() {
        Manager.SetupEnvironment.get_Instance();
    }
    
    //When the Server start he loading the all Client
    public ArrayList<Event> getGEventList() {
     
        ArrayList<Event> eventList = new ArrayList<Event>();
        
        try {

            
    // Open the cursor.
    
    cursor = Manager.SetupEnvironment.get_Instance().getEventDB().openCursor(null,null);
    
    DatabaseEntry foundKey = new DatabaseEntry();
    DatabaseEntry foundData = new DatabaseEntry();
    
    //Load all Gerant from the db and put them in the hashtable
    while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
        
        Event event = (Event)new EventBinding().entryToObject(foundData);
        eventList.add(event);
        
        }
   
    } catch (Exception de) {
        
        Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, "getEventList() Error on cursor reading Event", de);
    }
        
   finally {
        
        try {
    // Cursors must be closed.
    cursor.close(); 
    
    } catch(Exception io) {
        Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, "getEventList() Error on cursor close", io);
       }
        
    }
        return eventList;

 }
    
    //Return the number of record in db event
    public synchronized int getNumberOfEvent() {
        
        try {
            
            int i = 0;
            
            DatabaseEntry foundKey = new DatabaseEntry();
            DatabaseEntry foundData = new DatabaseEntry();

        // Open the cursor.
            
            cursor = Manager.SetupEnvironment.get_Instance().getEventDB().openCursor(null,null);
            
            while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            i++;
            }
            
            return i;
    
        } catch (Exception de) {
            
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, "get_CountOfAllGerant() Error on get", de);
      
            return 0;}
        
        finally {
        
                try {
                    // Cursors must be closed.
                    cursor.close(); 
    
                } catch(Exception io) {
                    
                    Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, "get_CountOfAllGerant() Error on cursor close", io);
       
                    }
        
            }   
    }
    
    /**
     *
     * @param k
     * @return
     */
    public synchronized Event getEvent(String k) {
        
        try {

            // A Gerant key that's known to exist
            keyBytes = k.getBytes("UTF-8");
            // A new DatabaseEntry
            key = new DatabaseEntry(keyBytes);
            // A new DatabaseEntry
            data = new DatabaseEntry();
            
            
            cursor = Manager.SetupEnvironment.get_Instance().getEventDB().openCursor(null,null);
            
           
                
                OperationStatus status = cursor.getSearchKey(key,data, LockMode.DEFAULT);
                
                if (status == OperationStatus.SUCCESS) {
                
                        Event event = (Event)new EventBinding().entryToObject(data);
                        return event;
                    
                } else {
                    return null;
                }
                
            } catch (Exception io)  {
                Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, "Error on cursor reading Event, with key : {0} : {1}", new Object[]{k, io}); 
                return null;
            }  
            finally {
            
            try {
                cursor.close();
            } catch (DatabaseException ex) {
                Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            } 
        }
    
    /**
     *
     * @param dbkey
     * @param client
     */
    public synchronized void putEvent(String dbkey, Event event) {
          
    try {
     
        // The key data.
        keyBytes = dbkey.getBytes("UTF-8");

        // The DataBaseEntry for the Key
        key = new DatabaseEntry(keyBytes);
        
        data = new DatabaseEntry();
        
        TupleBinding binding = new EventBinding();
        binding.objectToEntry(event , data);
    
        // Store the object in db 
        cursor = Manager.SetupEnvironment.get_Instance().getEventDB().openCursor(null,null);
        OperationStatus status = cursor.put(key, data);
            
            
            if (status == OperationStatus.SUCCESS) {
                Logger.getLogger(EventManager.class.getName()).log(Level.INFO, "Operation success for PUT OPERATION for Event n\u00b0 {0}\n", key); 
            } else {
                Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, "Operation unsuccess for PUT OPERATION for Event n\u00b0 {0}\n", key); 
            }
            cursor.close();
     
            //Sync DB
            Manager.SetupEnvironment.get_Instance().getEnvironment().sync();
            Logger.getLogger(EventManager.class.getName()).log(Level.INFO, "Success for Synchronized Data Event" + "\n");

            } catch (Exception en) {
                
                Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, "Operation PUT Error for Event {0}", en);
        }
    
    }
    
    /**
     *
     * @param k
     */
    public synchronized void DeleteEvent(String k) {
      
        
        try {
            
            // A Gerant key that's known to exist
            keyBytes = k.getBytes("UTF-8");
            // A new DatabaseEntry
            key = new DatabaseEntry(keyBytes);
            // A new DatabaseEntry
            data = new DatabaseEntry();
            
            cursor = Manager.SetupEnvironment.get_Instance().getEventDB().openCursor(null,null);
            
                OperationStatus status = cursor.getSearchKey(key,data, LockMode.DEFAULT);
                
                if (status == OperationStatus.SUCCESS)
                    cursor.delete();
                    
                    cursor.close();
                  
       }
        catch (Exception io)  {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, "DeleteEventFromDB() Error on cursor reading Event, with key : {0} : {1}", new Object[]{k, io});
                }
        
    }
    
    
    /**
     *
     */
    public synchronized void deleteAllEvent() {
        
        int i = 0;
        
        try {
            
            DatabaseEntry foundKey = new DatabaseEntry();
            DatabaseEntry foundData = new DatabaseEntry();

            // Open the cursor.
            cursor = Manager.SetupEnvironment.get_Instance().getEventDB().openCursor(null,null);
            
            while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            cursor.delete();
            i++;
            }
            
            
    
        } catch (Exception de) {
            
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, "DeleteAllEvent() Error ", de);
            
        }
        
        finally {
        
            
                try {
                    
                    // Cursors must be closed.
                    cursor.close();
                    
                    //Sync the env of db
                   Manager.SetupEnvironment.get_Instance().getEnvironment().sync();
    
                } catch(Exception io) {
                    
                    Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, "DeleteAllEvent() Error on cursor close operation : ", io);
                    
                }
        
             }   
        
    }
    
    public static synchronized EventManager get_Instance() {
        
        if (m_instance == null)
            m_instance = new EventManager();
           
        return m_instance;
        
    }




    }
