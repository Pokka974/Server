/*
 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import Binding.UserBinding;
import Object.User;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManager
{

    private byte[] keyBytes;
    private DatabaseEntry key;
    private DatabaseEntry data;
    private static UserManager m_instance;
    private Cursor cursor = null;

    public UserManager()
    {
        Manager.SetupEnvironment.get_Instance();
    }
    
    //When the Server start he loading the all Client
    public ArrayList<User> getGUserList()
    {

        ArrayList<User> userList = new ArrayList<>();

        try
        {

            // Open the cursor.
            cursor = Manager.SetupEnvironment.get_Instance().getUserDB().openCursor(null, null);

            DatabaseEntry foundKey = new DatabaseEntry();
            DatabaseEntry foundData = new DatabaseEntry();

            //Load all Gerant from the db and put them in the hashtable
            while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS)
            {

                User user = (User) new UserBinding().entryToObject(foundData);
                userList.add(user);

            }

        } catch (Exception de)
        {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, "getUserList() Error on cursor reading User", de);
        } finally
        {

            try
            {
                // Cursors must be closed.
                cursor.close();

            } catch (Exception io)
            {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, "getUserList() Error on cursor close", io);
            }

        }
        return userList;

    }

    //Return the number of record in db user
    public synchronized int getNumberOfUser()
    {

        try
        {

            int i = 0;

            DatabaseEntry foundKey = new DatabaseEntry();
            DatabaseEntry foundData = new DatabaseEntry();

            // Open the cursor.
            cursor = Manager.SetupEnvironment.get_Instance().getUserDB().openCursor(null, null);

            while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS)
            {
                i++;
            }

            return i;

        } catch (Exception de)
        {

            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, "get_CountOfAllGerant() Error on get", de);

            return 0;
        } finally
        {

            try
            {
                // Cursors must be closed.
                cursor.close();

            } catch (Exception io)
            {

                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, "get_CountOfAllGerant() Error on cursor close", io);

            }

        }
    }
    
    
    /**
     *
     * @param k
     * @return
     */
    public synchronized User getUser(String k)
    {

        try
        {

            // A User key that's known to exist
            keyBytes = k.getBytes("UTF-8");
            // A new DatabaseEntry
            key = new DatabaseEntry(keyBytes);
            // A new DatabaseEntry
            data = new DatabaseEntry();

            cursor = Manager.SetupEnvironment.get_Instance().getUserDB().openCursor(null, null);

            OperationStatus status = cursor.getSearchKey(key, data, LockMode.DEFAULT);

            if (status == OperationStatus.SUCCESS)
            {

                User user = (User) new UserBinding().entryToObject(data);
                return user;

            } else
            {
                return null;
            }

        } catch (Exception io)
        {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, "Error on cursor reading User, with key : {0} : {1}", new Object[]
            {
                k, io
            });
            return null;
        } finally
        {

            try
            {
                cursor.close();
            } catch (DatabaseException ex)
            {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    
    /**
     *
     * @param dbkey
     * @param client
     */
    public synchronized void putUser(String dbkey, User user)
    {

        try
        {

            // The key data.
            keyBytes = dbkey.getBytes("UTF-8");

            // The DataBaseEntry for the Key
            key = new DatabaseEntry(keyBytes);

            data = new DatabaseEntry();

//            UUID uuid = java.util.UUID.randomUUID();
//            String uuidString = uuid.toString();
//            user.setId(uuidString);
//
//            System.out.println(uuidString);

            TupleBinding binding = new UserBinding();
            binding.objectToEntry(user, data);

            // Store the object in db 
            cursor = Manager.SetupEnvironment.get_Instance().getUserDB().openCursor(null, null);
            OperationStatus status = cursor.put(key, data);

            if (status == OperationStatus.SUCCESS)
            {
                Logger.getLogger(UserManager.class.getName()).log(Level.INFO, "Operation success for PUT OPERATION for User n\u00b0 {0}\n", key);
            } else
            {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, "Operation unsuccess for PUT OPERATION for User n\u00b0 {0}\n", key);
            }
            cursor.close();

            //Sync DB
            Manager.SetupEnvironment.get_Instance().getEnvironment().sync();
            Logger.getLogger(UserManager.class.getName()).log(Level.INFO, "Success for Synchronized Data User" + "\n");

        } catch (Exception en)
        {

            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, "Operation PUT Error for User {0}", en);
        }

    }

    /**
     *
     * @param k
     */
    public synchronized void DeleteUser(String k)
    {

        try
        {

            // A Gerant key that's known to exist
            keyBytes = k.getBytes("UTF-8");
            // A new DatabaseEntry
            key = new DatabaseEntry(keyBytes);
            // A new DatabaseEntry
            data = new DatabaseEntry();

            cursor = Manager.SetupEnvironment.get_Instance().getUserDB().openCursor(null, null);

            OperationStatus status = cursor.getSearchKey(key, data, LockMode.DEFAULT);

            if (status == OperationStatus.SUCCESS)
            {
                cursor.delete();
            }

            cursor.close();

        } catch (Exception io)
        {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, "DeleteUserFromDB() Error on cursor reading User, with key : {0} : {1}", new Object[]
            {
                k, io
            });
        }

    }

    /**
     *
     */
    public synchronized void deleteAllUser()
    {

        int i = 0;

        try
        {

            DatabaseEntry foundKey = new DatabaseEntry();
            DatabaseEntry foundData = new DatabaseEntry();

            // Open the cursor.
            cursor = Manager.SetupEnvironment.get_Instance().getUserDB().openCursor(null, null);

            while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS)
            {
                cursor.delete();
                i++;
            }

        } catch (Exception de)
        {

            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, "DeleteAllUser() Error ", de);

        } finally
        {

            try
            {

                // Cursors must be closed.
                cursor.close();

                //Sync the env of db
                Manager.SetupEnvironment.get_Instance().getEnvironment().sync();

            } catch (Exception io)
            {

                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, "DeleteAllUser() Error on cursor close operation : ", io);

            }

        }

    }

    public static synchronized UserManager get_Instance()
    {

        if (m_instance == null)
        {
            m_instance = new UserManager();
        }

        return m_instance;

    }

}
