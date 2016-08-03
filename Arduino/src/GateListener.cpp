#include <GateListener.h>
#include <arduino.h>

GateListener::GateListener (uint8_t _port, char _serialMsg, uint16_t _treshold) {
  isBallIn = false;
  port = _port;
  serialMsg = _serialMsg;
  treshold = _treshold;
  count = 0;
}

void GateListener::check() {
  uint16_t current_value = analogRead(port);
   if (current_value < treshold) {
     isBallIn = true;
     timer_ms = millis() + 200;
   }

   if ((current_value > treshold) && isBallIn) {
     count++;
     Serial.print(serialMsg);Serial.println(count);
     isBallIn = false;
   }

   if (timer_ms < millis() && isBallIn) {
     isBallIn = false;
   }
}
