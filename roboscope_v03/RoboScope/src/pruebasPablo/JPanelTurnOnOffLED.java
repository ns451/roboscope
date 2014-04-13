package pruebasPablo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
 
//Librerias para envio y recepcion mediante puerto serial
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
 


public class JPanelTurnOnOffLED extends JPanel
    implements ActionListener {
 
    private static final String ON = "SEND";
    //private static final String OFF = "2";
    //private static final String TURN_ON = "1";
    //private static final String TURN_OFF = "0";
 
    private JButton switchOnButton;
   // private JButton switchOffButton;
    private JLabel label;
    private JFrame frame;
 
    
    /**
     * Codigo para el envio de datos
     */
    private Conversion conversion;
	private double[] coordenadas=conversion.convertir(57,32,15,32,56,12,12,4,2014,20,20,23,56,35,36,10,20,30);
	private  int altitud=(int)coordenadas[12];
	private  int azimut=(int)coordenadas[13];
	
	private  String ENVIO=altitud+" "+azimut+"#";
    
    /** The output stream to the port */
    private OutputStream output = null;
 
    SerialPort serialPort;
    private final String PORT_NAME = "COM7";
    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;
    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 9600;
 
    public JPanelTurnOnOffLED(){
        switchOnButton = new JButton(ON);
 
        //switchOffButton = new JButton(OFF);
        //switchOffButton.setEnabled(false);
 
        label = new JLabel("ENVIO DE ANGULOS");
 
        switchOnButton.addActionListener(this);
        //switchOffButton.addActionListener(this);
 
        add(label);
        add(switchOnButton);
        //add(switchOffButton);
 
        initializeArduinoConnection();
    }
 
    public void initializeArduinoConnection(){
 
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.
                                getPortIdentifiers();
 
        // iterate through, looking for the port
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier)
                    portEnum.nextElement();
 
            if (PORT_NAME.equals(currPortId.getName())) {
                portId = currPortId;
                break;
            }
        }
 
        if (portId == null) {
            showError("Could not find COM port.");
            System.exit(ERROR);
            return;
        }
 
        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass()
                    .getName(), TIME_OUT);
 
            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
 
            // open the streams
            output = serialPort.getOutputStream();
 
        } catch (Exception e) {
            showError(e.getMessage());
            System.exit(ERROR);
        }
 
    }
 
    public static void createAndShowGUI(){
 
        JFrame frame = new JFrame("ENVIO DE ANGULOS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        JPanelTurnOnOffLED newContentPane = new JPanelTurnOnOffLED();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String args[]){
    	
    	
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
            	
            	createAndShowGUI();
            }
        });
 
    }
 
  
    public void actionPerformed(ActionEvent e) {
        if(ON.equals(e.getActionCommand())){
            //switchOnButton.setEnabled(false);
           // switchOffButton.setEnabled(true);
            sendData(ENVIO);
            System.out.println(ENVIO);
            switchOnButton.setEnabled(false);
            try {
            	Thread.sleep(5000);
            	} catch (InterruptedException ex) {
            	// aqu� tratamos la excepci�n como queramos, haciendo nada, sacando por pantalla el error, ...
            	}
            switchOnButton.setEnabled(true);
        
        }
        
        
 
    }
 
    private void sendData(String data){
 
        try {
            output.write(data.getBytes());
        } catch (IOException e) {
            showError("Error sending data");
            System.exit(ERROR);
        }
    }
 
    private void showError(String errorMessage){
        JOptionPane.showMessageDialog(frame,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
 
}