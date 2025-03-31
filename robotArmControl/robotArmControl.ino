#include <ESP32Servo.h>
#include <Stepper.h>

// Servo Objects
Servo myServo1;
Servo myServo12;
Servo myServo2;
Servo myServo3;
Servo myServo4;
Servo myServo5;

// initialize the stepper object
// with a stepsPerRevolution of 200
// on pins 3, 8, 18, 17
Stepper myStepper(200, 3, 8, 18, 17);

// Servo Pins
// Shoulder 2 servos
const int Servo1_pin = 4;
const int Servo12_pin = 5;
// Elbow arm servo
const int Servo2_pin = 6;
// Wrist servo
const int Servo3_pin = 7;
// Wrist rotation servo
const int Servo4_pin = 15;
// Gripper servo
const int Servo5_pin = 16;

// Variables
static String input;
static int motorNum;
static int pos;

// Create potentiometer value reading variable
int pot;
const int pot_pin = 10;  // Use pin 9 as input for potentiometer
float pot_val = 0.0;

void setUpServos() {
  myServo1.attach(Servo1_pin);
  myServo12.attach(Servo12_pin);
  myServo2.attach(Servo2_pin);
  myServo3.attach(Servo3_pin);
  myServo4.attach(Servo4_pin);
  myServo5.attach(Servo5_pin);
  safeMode();
}
  Serial.begin(9600);

void setup() {
  // Open serial communications and wait for port to open:
  while (!Serial) {
    // wait for serial port to connect. Needed for native USB port only
  }

  Serial.println("\n\nPREPARING");

  setUpServos(); // get the servos to a position
  moveStepper(180);  // move base to center at 180 degrees

  // set the stepper motor speed at 60 rpm
  myStepper.setSpeed(60);

  // send an intro:
  Serial.println("\n\nREADY");
}

void loop() {
  // Read serial input:
  while (Serial.available() > 0) {

    String input = Serial.readStringUntil('\n');
    Serial.print("Input: " + input);

    // makes sure that the input is atleast 1-4 digits long
    if (input.length() > 1 && input.length() <= 4) {

      int motorNum = input.substring(0, 1).toInt(); //Get the first digit for the motor
      int pos = input.substring(1).toInt(); // the rest is the position angle

      Serial.println(" | Motor: " + String(motorNum) + " | Position: " + pos);

      motorSelect(motorNum, pos);

    } else if (input.equals("SAFEMODE")) {
      Serial.print(" | GOING INTO SAFEMODE");
      Serial.println();

      safeMode();
    }
  }
}

// Bring all servos and stepper motor to a safe position
void safeMode() {
  motorSelect(1, 130);
  delay(500);
  motorSelect(2, 180);
  delay(500);
  motorSelect(3, 60);
  delay(500);
  motorSelect(4, 90);
  delay(500);
  motorSelect(6, 180);
}

// Select motor to move and call the function to move the motor to the required position
void motorSelect(int number, int position) {
  switch (number) {
    case 1:
      moveServo(myServo1, position);
      delay(10);
      moveServo(myServo12, 180 - position);
      break;
    case 2:
      moveServo(myServo2, position);
      break;
    case 3:
      moveServo(myServo3, position);
      break;
    case 4:
      moveServo(myServo4, position);
      break;
    case 5:
      moveServo(myServo5, position);
      break;
    case 6:
      moveStepper(position);
      break;
    default:
      delay(100);
      break;
  }
}

// Move servo motor
void moveServo(Servo &servoObject, int position) {
  servoObject.write(position);
}

// Move stepper motor
void moveStepper(int newPos) {
  // Get current potentiometer reading
  get_pot_value();

  // Print current and target positions
  Serial.println("Move stepper from " + String(pot_val) + " to " + String(newPos));

  // Calculate the difference between the current position and the target.
  // 890 chosen as thats is the number of steps needed for 360 degrees of base movement.
  // Has to be negative
  int stepsToMove = -(newPos - pot_val) * (890.0 / 360.0);

  if (stepsToMove != 0) {
    myStepper.step(stepsToMove);  // Move the stepper motor
    Serial.println("Stepper moved " + String(stepsToMove) + " steps.");
  } else {
    Serial.println("Already at target position.");
  }

  // Update the potentiometer reading after the move
  get_pot_value();
}

// Get potentiometer values
int get_pot_value() {
  pot = analogRead(pot_pin);
  pot_val = pot * (360.0 / 4095.0);  // Map potentiometer value to 0-360 degrees and 12-bit value 
  Serial.println("Current position (degrees): " + String(pot_val));
  return pot_val;
}