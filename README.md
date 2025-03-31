# DCU FYP Robot arm interface
Repo for everything done in the final year project for ECE4 (IoT) code wise.

This repo contains the robot arm software code and the main GUI application software code.

It is broken down into 3 groups
- Arduino Files
- Java Files
- Python Files

### 3D Printed Parts
This software is built to work on this project.
[Printables Link](https://www.printables.com/model/1245572-robotic-arm)

## Arduino Files
Contains the Arduino .ino file to be compile and uploaded to a microcontroller (Ideally an ESP32)

Needs the [ESP32Servo library](https://docs.arduino.cc/libraries/esp32servo/)

## Java Files
Contains two different attempts of the GUI application. They are two seperate packages ArmControl and GUITest.
I used [Eclipse](https://eclipseide.org/) to develop these applications.
It requires [jSerialComm library](https://fazecast.github.io/jSerialComm/) for both packages
For GUITest you need to install [Eclipse WindowBuilder](https://eclipse.dev/windowbuilder/)

The best application to use for Java is ArmControl as its the most recent developed application but it has been abandoned in favour of Python.

## Python Files
Contains all the necessary Python files to run the GUI application for the robot arm. Just run main.py.
Requires [PySide6](https://pypi.org/project/PySide6/), [pySerial](https://pypi.org/project/pyserial/) and [pyGame](https://pypi.org/project/pygame/)

The GUI was designed using [Qt Designer](https://build-system.fman.io/qt-designer-download)

Its the best GUI application for controlling the robot arm.

