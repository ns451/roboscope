package examples;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Calendar;
 
public class TelnetIO extends Thread
{
    private static final String END_KEY = "$$EOE";
    private static final String START_KEY = "$$SOE";
    private static final String DATE_DUMMY = "xDATEx";
    private static final String strERROR = "ERROR";
    private static final double[] iERROR = {0,0,0};
    private static final String HOST = "horizons.jpl.nasa.gov";
    private static final int PORT = 6775;
    private static final int TIMEOUT = 5000;
    private String date;
    private String[][] actionTable = {
            {"[E]phemeris"                  , "e"			},
            {"[N]ew-case"                   , "n"			},
            {"Observe, Elements, Vectors"   , "v"			},
            {"Coordinate center"            , "@ssb"			},
            {"Starting CT"                  , DATE_DUMMY + " 00:00"	},
            {"Ending   CT"                  , DATE_DUMMY + " 00:01"	},
            {"Output interval"              , "1m"			},
            {"Confirm selected station"     , "y"			},
            {"Accept default output"        , "y"			},
            {"Reference plane"              , "eclip"			},
            {"Horizons>"                    , "exit"			},
    };

    public static void main(String[] args)
    {
        TelnetIO socket1 = new TelnetIO();
        String data = socket1.getData(299);
        System.out.println("The final result is:\n" + data);
        /* ignore next line, just for completeness :) */
        System.out.println("The z coordinate is: " + transformIntoCoords(data)[2]);
    }

    private TelnetIO()
    {
        Calendar today = Calendar.getInstance();
        date = today.get(Calendar.YEAR)+"-"+(today.get(Calendar.MONTH)+1)+"-"+today.get(Calendar.DAY_OF_MONTH);
        System.out.println("Current date: "+date);

        for(int i=0; i<actionTable.length; i++)
            if(actionTable[i][1].contains(DATE_DUMMY))
                actionTable[i][1] = actionTable[i][1].replaceAll(DATE_DUMMY, date);
    }

    public String getData(int objectid)
    {
        String strdata = strERROR;

        try {
            Socket telnet = new Socket(HOST, PORT);
            telnet.setKeepAlive(true);
            telnet.setSoTimeout(TIMEOUT);
            PrintWriter writer = new PrintWriter(telnet.getOutputStream());
            InputStream reader = telnet.getInputStream();
            byte[] buffer = new byte[8192];
            int len;

            boolean bconnected = false;

            try {
                while ((len = reader.read(buffer)) != -1)
                {
                    String readchar = new String(buffer, 0, len);
                    System.out.println("** ReadChar: "+readchar);

                    /* already inside the chain of command? */
                    if(bconnected)
                    {
                        if (readchar.contains(START_KEY) &&
                                readchar.contains(END_KEY))
                        {
                            /* received data */
                            strdata = readchar.substring(
                                    readchar.indexOf(START_KEY)+START_KEY.length(), 
                                    readchar.indexOf(END_KEY)).trim();
                        }
                        else
                        {
                            //System.out.println(readchar);
                            /* server probably wants interaction */
                            for(int i=0; i<actionTable.length; i++)
                            {
                                if(readchar.contains(actionTable[i][0]))
                                {
                                    writer.print(actionTable[i][1] + "\r\n");
                                    writer.flush();
                                    break;
                                }	
                            }
                        }
                    }
                    else if (readchar.trim().endsWith("Horizons>")) {
                            writer.print(objectid + "\r\n");
                            writer.flush();
                            bconnected = true;
                    }

                }
            } catch (InterruptedIOException e) {
                    System.out.println("Timeout reached. Cancelling!");
            } finally {
                    telnet.close();
            }
        } catch (SocketException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }

        // We only want line 2, 3 and 4
        if(strdata.split("\n").length < 4)
                return strERROR;
        else
                return strdata.split("\r\n")[1] + "\n" + strdata.split("\r\n")[2] + "\n" + strdata.split("\r\n")[3];
    }

    /* ignore the following code - not network related */
    static double[] transformIntoCoords(String data)
    {
        String[] values = data.split("\n");
        System.out.println("transformIntoCoords() values[0]: '"+values[0]+"'");
        String[] coords = values[0].trim().replaceAll("[ ]+", " ").split(" ");
        if(coords.length < 3) {
            System.out.println("transformIntoCoords() - ERROR - coords.length < 3");
            return iERROR;
        }

        double[] xxx = iERROR;
        for(int i=0; i<xxx.length; i++)	{
            System.out.print("transformIntoCoords() coords["+i+"]: '"+coords[i]+"'  -->  ");
            try {
                    xxx[i] = Double.parseDouble(coords[i]);
            } catch (NumberFormatException e) {
                    System.out.println("Error parsing value");
                    xxx = iERROR;
                    break;
            }
            System.out.println(xxx[i]);
        }
        return xxx;
    }
}