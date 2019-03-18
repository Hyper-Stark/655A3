import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static FileWriter fw;
    private static BufferedWriter logger;
    private static SimpleDateFormat format;
    private static int counter;

    static {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            fw = new FileWriter("log.txt");
            logger = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void info(String msg){
        counter ++;
        log("[INFO]",msg);
    }

    public static void error(String msg){
        counter ++;
        log("[ERROR]",msg);
    }

    private static void log(String level, String msg){
        try {
            logger.write(format.format(new Date())+" "+level+" "+msg);
            if (counter > 20){
                logger.flush();
                counter = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
