package id.vitradisapratama.floatingcamera;
 
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
 
public class Camera extends JPanel
{
    Webcam webcam = Webcam.getDefault();
    WebcamPanel panel = new WebcamPanel(webcam);
 
    /**
     * Create the frame.
     */
    public Camera()
    {
        /*
        try {
                captureimage();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        */
        this.add(panel);
    }
 
    private void captureimage() throws IOException
    {
        BufferedImage image = webcam.getImage();
        ImageIO.write(image, "PNG", new File("test.png"));
    }
}