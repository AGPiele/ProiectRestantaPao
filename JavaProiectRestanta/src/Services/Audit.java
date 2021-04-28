package Services;

import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Audit {
    private Audit(){}
    public static void printQuery(String queryName,String threadName) throws IOException{
        FileWriter fileWriter=new FileWriter("data/audit.csv",true);
        BufferedWriter writer=new BufferedWriter(fileWriter);

        SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date d=new Date();

        writer.write(queryName+","+date.format(d)+","+threadName);
        writer.newLine();
        writer.close();
    }
}
