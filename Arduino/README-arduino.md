# Hardware part of Smart foosball IoT

Hardware part of Smart foosball fixes Goal events and sends them over a serial port. It consists of:

* Arduino UNO-like board
* 2 Laser dot diode module
* 2 Photoresistor (for example VT90N2)
* 2 Resistors (for example 100 kΩ)
* USB cable (A — B)
* OTG cable (USB-B - microUSB)

**NOTEs:** 
* Laser dot diode module will be connected to Arduino 5V so, please, check that these modules have appropriate resistors. If no, you have to add this resistor in your scheme manually.
* Electrical resistance of resistors should be calculated based on chosen photoresistor. It should be approximately equal to the resistance of photoresistor in the lit room.

Scheme of connection:

![scheme](scheme.png)

Photoresistors must be placed in the gates, lasers must be placed near and point to photoresistors. When the ball gets in the gate, it has to break line between laser and photoresistor. Photoresistor will change its resistance and Arduino board will be able to fix this and recognize a Goal event.

This part of the project has been developed in [PlatformIO IDE](http://platformio.org/platformio-ide). It's much more practical and effective tool than Arduino IDE. To use this code with Arduino IDE you could copy-paste all content from files in *Arduino/src* dir to Arduino IDE project.

The application reads signals from analog ports, fixes goals based on this information and sends Goal event through the serial port. Android part parses it and counts goals.