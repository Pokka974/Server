/*
 * SetupEnvironnement.java
 *
 * Created on 21 octobre 2004, 16:26
 */

package Manager;

/**
 *
 * @author  Brinon Laurent
 */

import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Database;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SetupEnvironment {
    
    /** Creates a new instance of SetupEnvironnement */
    public
    SetupEnvironment() {
        
        m_instance = this;
        try { 
        //open and create the db if not already exist, in read and write mode
        setup(new File("C:\\Users\\GEN13\\Documents\\DB"), false);
        
        } catch (Exception e) {
            Logger.getLogger(SetupEnvironment.class.getName()).log(Level.SEVERE, "Error accessing database path "  + " : " + e);     
        }

    }
    
    
    public
    void setup(File envHome, boolean readOnly) throws DatabaseException {
        
        try {
        //Instantiate an environment and database configuration
        crtEnvConfig = new EnvironmentConfig();
        crtDbConfig = new DatabaseConfig();
        
        //Configure the environment and database for the readOnly state
        crtEnvConfig.setReadOnly(readOnly);
        crtDbConfig.setReadOnly(readOnly);
        
        // If the environment is opened for write, then we want be able to create the
        // environment and databases if they do not exist.
        crtEnvConfig.setAllowCreate(true);
        crtDbConfig.setAllowCreate(true);
        
        //crtEnvConfig.setCachePercent(30);
        
        //Instantiate the Environment. This open it and also possibly creates it.
        crtEnv = new Environment(envHome, crtEnvConfig);
        
        //Now create and open our databases
        
      
        userDB = crtEnv.openDatabase(null, "user", crtDbConfig);
        eventDB = crtEnv.openDatabase(null, "event", crtDbConfig);
        //create class catalog
        
        
        } catch(Exception e) {        
            Logger.getLogger(SetupEnvironment.class.getName()).log(Level.SEVERE, "Error opening and configuring database "  + " : " + e);     
        }
    }
    
    
   public
    Environment getEnvironment() {
        return crtEnv;
    }
    
    public
            Database getUserDB(){
                return userDB;
            }
    
    public
            Database getEventDB(){
                return eventDB;
            }
        
    
    public
    static synchronized SetupEnvironment get_Instance() {
        if (m_instance != null)
        return m_instance;
            else m_instance = new SetupEnvironment();
                 return m_instance;
    }
    
    public
    void close() {
        
        if (crtEnv != null) {
            
            try {
                
               userDB.close();
                
            } catch(DatabaseException dbe) {
                Logger.getLogger(SetupEnvironment.class.getName()).log(Level.SEVERE, "Error on closing database "  + " : " + dbe);     
              }
            }
        }
    
    public
    void open() {
        
        if (crtEnv != null) {
            try {
                
        //Now create and open our databases
        
        userDB = crtEnv.openDatabase(null, "event", crtDbConfig);
        
        //Open the class catalog db. this is used for optimize class serialization
        //create class catalog
                
            } catch(DatabaseException dbe) {
                System.err.println("Error closing database : " + dbe.toString());
                                            }
            }
        }
    
    private Environment crtEnv;
    
    private Database                                                            userDB;
    private Database                                                            eventDB;
    
            
    private static SetupEnvironment                                             m_instance;
    
    EnvironmentConfig                                                           crtEnvConfig;
    DatabaseConfig                                                              crtDbConfig;
    
}

