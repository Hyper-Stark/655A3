/****************************************************************************************************************
 * File: Logger.java
 * Course: 17655
 * Project: Assignment A3
 * Author: Li Zhang
 *
 * Internal methods:
 * info(String msg)
 * error(String msg)
 *
 * Description:
 * This class is a logger to record information into the specific log file.
 *
 ***************************************************************************************************************/

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private FileWriter fw;
    private BufferedWriter bw;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");;

    /*********************************************
     * Private constructor method.
     * The only way can get a caller-related
     * Logger instance is calling getInstance
     * @param filename
     ********************************************/
    private Logger(String filename){
        try {
            this.fw = new FileWriter(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bw = new BufferedWriter(fw);
    }

    /*******************************************
     * This method is used to return a caller-related
     * Logger instance. Different caller has different
     * log file to write.
     * @param clazz the caller class
     * @return the corresponding Logger
     ******************************************/
    public static Logger getInstance(Class clazz){

        if(clazz == null){
            throw new RuntimeException("Register class should not be null! ");
        }

        String filename = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"-"+clazz.getName()+".log";
        return new Logger(filename);
    }

    /******************************************
     * Record information on info level
     * @param msg messages need to be record
     ******************************************/
    public void info(String msg){
        log("[INFO]",msg);
    }

    /******************************************
     * Record information on error level
     * @param msg messages need to be record
     ******************************************/
    public void error(String msg){
        log("[ERROR]",msg);
    }

    /******************************************
     * The real method which writes message into the file
     * @param level the log level need to be write down
     * @param msg messages need to be record
     ******************************************/
    private void log(String level, String msg){
        try {
            bw.write(format.format(new Date())+" "+level+" "+msg+"\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
