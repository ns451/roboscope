package pruebasPablo;

public class Prueba {

	public static void main(String args[])
	{
		
	 Conversion conversion;
	 double[] coordenadas=Conversion.convertir(57,32,15,32,56,12,12,4,2014,20,20,23,56,35,36,10,20,30);
	 double altitud=coordenadas[12];
	 double azimut=coordenadas[13];
     String ENVIO=(int)altitud+" "+(int)azimut+"#";	
     System.out.println(ENVIO);
	}
}
