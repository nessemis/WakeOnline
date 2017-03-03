package nessemis_developments.wakeonline;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by Dennis Arets on 01-Mar-17.
 */

public class Utility {
    public static Serializable ReadPrivateFile(String filename, Context context){
        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream is = new ObjectInputStream(fis);
            Serializable object = (Serializable) is.readObject();
            is.close();
            fis.close();
            return object;
        }
        catch (FileNotFoundException e){
            return null;
        }
        catch (Exception e){
            return null;
        }

    }

    public static void WritePrivateFile(String filename, Serializable object, Context context){
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(object);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
