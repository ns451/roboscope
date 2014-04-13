//Definición de pines
//===================================
int motorPaP2pin1 = 4;
int motorPaP2pin2 = 5;
int motorPaP2pin3 = 6;
int motorPaP2pin4 = 7;

int motorPaPpin1 = 8;
int motorPaPpin2 = 9;
int motorPaPpin3 = 10;
int motorPaPpin4 = 11;

int pinLecturaPotPaP = A0;
//===================================

//Variables auxiliares puerto Serial
//===================================
int numASCII = 0;
int comandosIngresados=1;
char charSerial = 0;
String comando1 = "";
String comando2 = "";
boolean analizarDatos=false;
//===================================

//Variables auxiliares motor PaP
//===================================
int gradosALTURA = 0;
int posicionGradosAnteriorPaP = 0;
int gradosTotalesPaP = 0;
int limitePaP = 0;
int delayTimePaP = 20;
int delayTimePaP2 = 50;
int lecturaPotPaP = 0;
int lecturaPosIniPaP = 489;

int gradosBASE = 0;
int posicionGradosAnteriorPaP2 = 0;
int gradosTotalesPaP2 = 0;
int limitePaP2 = 0;
//===================================


int contador=0;
char letra;

boolean habilitado=true;
boolean primera=true;

void setup() {
  TCCR1B=2;
  TCCR2B=2;
  Serial.begin(9600);
  
  pinMode(motorPaPpin1, OUTPUT);
  pinMode(motorPaPpin2, OUTPUT);
  pinMode(motorPaPpin3, OUTPUT);
  pinMode(motorPaPpin4, OUTPUT);
  contador=0;
  posicionIniPaP();
}

void loop(){
  while(Serial.available()>0){
    charSerial=Serial.read();
    numASCII=(int)charSerial;

    if((numASCII!=35) && (numASCII!=32)){
      if(comandosIngresados==1){
        comando1=comando1+charSerial;
      }
      if(comandosIngresados==2){
        comando2=comando2+charSerial;
      }
    }
    else{
      if(numASCII==35){
        analizarDatos=true;
      }
      else{
        if(numASCII==32){
          comandosIngresados++;
        }
      }
    }
  }

  if(Serial.available()==0 && analizarDatos){
    comando1.toUpperCase();
    comando2.toUpperCase();

    interpretarComando(comando1,comando2);

    comando1="";
    comando2="";
    comandosIngresados=1;
    analizarDatos=false;
    Serial.flush();
  }
}


void interpretarComando(String comando1, String comando2){
  if(comando1!="" && comando2!=""){
    gradosALTURA=comando1.toInt();
    moverALTURA(gradosALTURA);

    gradosBASE=comando2.toInt();
    moverBASE(gradosBASE);
    
    Serial.println("A");
  }
}


void moverALTURA(int gradosALTURA){
  gradosTotalesPaP=gradosALTURA - posicionGradosAnteriorPaP;
  posicionGradosAnteriorPaP=gradosALTURA;
  if(gradosTotalesPaP>0){
    limitePaP=((1.6)*gradosTotalesPaP);
    for(int iPaP=0;iPaP<=limitePaP;iPaP++){
      motorPaPAlturasentidoHorario();
    }
  }
  else{
    if(gradosTotalesPaP<0){
      limitePaP=abs(((1.6)*gradosTotalesPaP));
      for(int iPaP=0;iPaP<=limitePaP;iPaP++){
      motorPaPAlturasentidoAntiHorario();
      }
    }
  }
}

void moverBASE(int gradosBASE){
  gradosTotalesPaP2=gradosBASE - posicionGradosAnteriorPaP2;
  posicionGradosAnteriorPaP2=gradosBASE;
  if(gradosTotalesPaP2>0){
    limitePaP2=((0.69)*gradosTotalesPaP2);
    for(int iPaP=0;iPaP<=limitePaP2;iPaP++){
      motorPaPBASEsentidoHorario();
    }
  }
  else{
    if(gradosTotalesPaP2<0){
      limitePaP2=abs(((0.69)*gradosTotalesPaP2));
      for(int iPaP=0;iPaP<=limitePaP2;iPaP++){
      motorPaPBASEsentidoAntiHorario();
      }
    }
  }
}

void posicionIniPaP(){
  lecturaPotPaP=analogRead(pinLecturaPotPaP);
  if(lecturaPotPaP>lecturaPosIniPaP){
    while(lecturaPosIniPaP<=(analogRead(pinLecturaPotPaP))){
      motorPaPAlturasentidoAntiHorario();
    }
  }
  else{
    if(lecturaPotPaP<lecturaPosIniPaP){
      while(lecturaPosIniPaP>=(analogRead(pinLecturaPotPaP))){
        motorPaPAlturasentidoHorario();
      }
    }
  }
}

void motorPaPBASEsentidoHorario() {
  digitalWrite(motorPaP2pin1, HIGH);
  digitalWrite(motorPaP2pin2, LOW);
  digitalWrite(motorPaP2pin3, LOW);
  digitalWrite(motorPaP2pin4, HIGH);
  delay(delayTimePaP2);

  digitalWrite(motorPaP2pin1, HIGH);
  digitalWrite(motorPaP2pin2, LOW);
  digitalWrite(motorPaP2pin3, HIGH);
  digitalWrite(motorPaP2pin4, LOW);
  delay(delayTimePaP2);

  digitalWrite(motorPaP2pin1, LOW);
  digitalWrite(motorPaP2pin2, HIGH);
  digitalWrite(motorPaP2pin3, HIGH);
  digitalWrite(motorPaP2pin4, LOW);
  delay(delayTimePaP2);

  digitalWrite(motorPaP2pin1, LOW);
  digitalWrite(motorPaP2pin2, HIGH);
  digitalWrite(motorPaP2pin3, LOW);
  digitalWrite(motorPaP2pin4, HIGH);
  delay(delayTimePaP2);
}

void motorPaPBASEsentidoAntiHorario(){
  digitalWrite(motorPaP2pin1, LOW);
  digitalWrite(motorPaP2pin2, HIGH);
  digitalWrite(motorPaP2pin3, LOW);
  digitalWrite(motorPaP2pin4, HIGH);
  delay(delayTimePaP2);

  digitalWrite(motorPaP2pin1, LOW);
  digitalWrite(motorPaP2pin2, HIGH);
  digitalWrite(motorPaP2pin3, HIGH);
  digitalWrite(motorPaP2pin4, LOW);
  delay(delayTimePaP2);

  digitalWrite(motorPaP2pin1, HIGH);
  digitalWrite(motorPaP2pin2, LOW);
  digitalWrite(motorPaP2pin3, HIGH);
  digitalWrite(motorPaP2pin4, LOW);
  delay(delayTimePaP2);

  digitalWrite(motorPaP2pin1, HIGH);
  digitalWrite(motorPaP2pin2, LOW);
  digitalWrite(motorPaP2pin3, LOW);
  digitalWrite(motorPaP2pin4, HIGH);
  delay(delayTimePaP2);
}

void motorPaPAlturasentidoHorario() {
  digitalWrite(motorPaPpin1, HIGH);
  digitalWrite(motorPaPpin2, LOW);
  digitalWrite(motorPaPpin3, LOW);
  digitalWrite(motorPaPpin4, LOW);
  delay(delayTimePaP);

  digitalWrite(motorPaPpin1, LOW);
  digitalWrite(motorPaPpin2, HIGH);
  digitalWrite(motorPaPpin3, LOW);
  digitalWrite(motorPaPpin4, LOW);
  delay(delayTimePaP);

  digitalWrite(motorPaPpin1, LOW);
  digitalWrite(motorPaPpin2, LOW);
  digitalWrite(motorPaPpin3, HIGH);
  digitalWrite(motorPaPpin4, LOW);
  delay(delayTimePaP);

  digitalWrite(motorPaPpin1, LOW);
  digitalWrite(motorPaPpin2, LOW);
  digitalWrite(motorPaPpin3, LOW);
  digitalWrite(motorPaPpin4, HIGH);
  delay(delayTimePaP);
}

void motorPaPAlturasentidoAntiHorario() {
  digitalWrite(motorPaPpin1, LOW);
  digitalWrite(motorPaPpin2, LOW);
  digitalWrite(motorPaPpin3, LOW);
  digitalWrite(motorPaPpin4, HIGH);
  delay(delayTimePaP);

  digitalWrite(motorPaPpin1, LOW);
  digitalWrite(motorPaPpin2, LOW);
  digitalWrite(motorPaPpin3, HIGH);
  digitalWrite(motorPaPpin4, LOW);
  delay(delayTimePaP);

  digitalWrite(motorPaPpin1, LOW);
  digitalWrite(motorPaPpin2, HIGH);
  digitalWrite(motorPaPpin3, LOW);
  digitalWrite(motorPaPpin4, LOW);
  delay(delayTimePaP);

  digitalWrite(motorPaPpin1, HIGH);
  digitalWrite(motorPaPpin2, LOW);
  digitalWrite(motorPaPpin3, LOW);
  digitalWrite(motorPaPpin4, LOW);
  delay(delayTimePaP);
}