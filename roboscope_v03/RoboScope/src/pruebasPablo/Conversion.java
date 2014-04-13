package pruebasPablo;

public class Conversion
{
   
    public static double[] convertir(int AH,int AM,int AS,int DG,int DM, int DS,float DD,int MN,int YR,
                               int TH,int TM,int TS, int LG,int LM,int LS,int BG,int BM,int BS )//B->LATITUD
    {
        double PI=3.1416;
        
        
        //DATOS DE ENTRADA:
        //Asencion recta:
        float RA=AH+AM/60+AS/3600;
        //Declinacion
        float DC=DG+DM/60+DS/3600;
        //Latitud
        float LT=BG+BM/60+BS/3600;
        //Longitud
        LG=LG+LM/60+LS/3600;
        //fecha juliana
        float HR = TH + TM/60 + TS/3600;
        DD=DD+HR/24;
        double DY = Math.floor(DD);
	
        
        double GR;
        
        if (MN<3) {
                YR = YR - 1;
                MN = MN + 12;
                }

         if (YR + MN / 100 + DY / 10000 >= 1582.1015)  
            {
                GR =2-Math.floor(YR/100)+Math.floor(Math.floor(YR/100)/4);
                } 
                else {
                        GR = 0;
                        }
         double JD = Math.floor(365.25* YR)+Math.floor(30.6001*(MN+1))+DY+1720994.5+GR ; 
         double T=(JD- 2415020)/36525;
         double SS= 6.6460656 + 2400.051*T +0.00002581*T*T;
         
         //-->TIEMPO DE SIDEREO O GREENWICH
         double ST =(SS/24-Math.floor(SS/24))*24;
         
         //-->TIEMPO DE SIDEREO LOCAL
         double SA=ST+(DD-Math.floor(DD))*24*1.002737908;
         SA=SA+LG/15;
         
         if (SA<0) {
                SA=SA+24;
                }
         if (SA>24) {
                SA=SA-24;
                }

         //-->Conversion a hms del Tiempo sidereo local
         double  TSH=Math.floor(SA);
         double TSM=Math.floor((SA - Math.floor(SA)) * 60);
         double TSS=((SA -Math.floor(SA)) * 60 - TSM) * 60;
         
         //-->Angulo horario
         
          double H=SA-RA;
        if (H < 0) {
                H = H + 24;
                }
        H = H * 15;      
	  

	double R =180/Math.PI;
        double AL=Math.asin(Math.sin(DC/R)*Math.sin(LT/R)+Math.cos(DC/R)*Math.cos(LT/R)*Math.cos(H/R));
        double AZ=Math.asin((Math.sin(DC/R)*Math.sin(LT/R))/Math.cos(AL));
         AZ = AZ * R;
        if (Math.sin(H / R) < 0) {
                AZ = 360 - AZ;
                }

        //Conversion a gms del acimut
        double AZG=Math.floor(AZ);
        double AZM=Math.floor((AZ - Math.floor(AZ)) * 60);
        double AZS=((AZ -Math.floor(AZ)) * 60 - AZM) * 60;
        AL=AL*R;
        
        double ALG;
        //Conversion a gms de la altura
        double D = Math.abs(AL);
        if (AL>0) {
                ALG=Math.floor(D);
                } else {
                ALG=(-1)*Math.floor(D);
                }
        double ALM=Math.floor((D -Math.floor(D)) * 60);
        double ALS = ((D - Math.floor(D)) * 60 - ALM) * 60;
        if (AL<0) {
                ALM=-ALM;
                ALS=-ALS;
                }
        
	//DATOS PAL ROBOT
	//Altitud
	double RAL;
	double MAL=Math.floor(AL);
	if ((AL-MAL)<0.5) {
		 RAL=Math.floor(AL);
		} else {
		 RAL=Math.round(AL);
	       	}
	if ((RAL%2)==0) {
		RAL=RAL;
		} else {
		RAL=RAL+1;
		}
	
	//Azimut
	double RAZ;
	double MAZ=Math.floor(AZ);
	if ((AZ-MAZ)<0.5) {
		 RAZ=Math.floor(AZ);
		} else {
		 RAZ=Math.round(AZ);
	       	}
	if ((RAZ%2)==0) {
		RAZ=RAZ;
		} else {
		RAZ=RAZ+1;
		}

        double[] resultado=new double[14];
    
        resultado[0]=SA;
        resultado[1]=TSH;
        resultado[2]=TSM;
        resultado[3]=TSS;
        resultado[4]=AZ;
        resultado[5]=AZG;
        resultado[6]=AZM;
        resultado[7]=AZS;
        resultado[8]=AL;
        resultado[9]=ALG;
        resultado[10]=ALM;
        resultado[11]=ALS;
        resultado[12]=RAL;
	    resultado[13]=RAZ;

        return resultado;
    }
}
