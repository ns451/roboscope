/*
 * RoboScope Project for SpaceApps Challenge 2014
 *  
 */

package roboscope;

import examples.util.IOUtil;
import java.io.IOException;
import org.apache.commons.net.telnet.TelnetClient;

/**
 * @author ElectroPumas
 */
public class RoboScope
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        TelnetClient telnet;

        telnet = new TelnetClient();

        try
        {
            telnet.connect("horizons.jpl.nasa.gov", 6775);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        IOUtil.readWrite(telnet.getInputStream(), telnet.getOutputStream(),
                         System.in, System.out);

        try
        {
            telnet.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        System.exit(0);
    }
    
}
