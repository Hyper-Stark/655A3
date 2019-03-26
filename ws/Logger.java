/****************************************************************************************************************
 * File: Logger.java
 * Course: 17655
 * Project: Assignment A3
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

    private static FileWriter fw;
    private static BufferedWriter logger;
    private static SimpleDateFormat format;

    static {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            fw = new FileWriter(new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"-client.log");
            logger = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /******************************************
     * Record information on info level
     * @param msg messages need to be record
     ******************************************/
    public static void info(String msg){
        log("[INFO]",msg);
    }

    /******************************************
     * Record information on error level
     * @param msg messages need to be record
     ******************************************/
    public static void error(String msg){
        log("[ERROR]",msg);
    }

    /******************************************
     * The real method which writes message into the file
     * @param level the log level need to be write down
     * @param msg messages need to be record
     ******************************************/
    private static void log(String level, String msg){
        try {
            logger.write(format.format(new Date())+" "+level+" "+msg+"\n");
            logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
