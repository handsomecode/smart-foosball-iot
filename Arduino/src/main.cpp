#include <arduino.h>
#include <GateListener.h>

GateListener gateA(A0, 'a', 850);
GateListener gateB(A1, 'b', 750);

void setup() {
  Serial.begin(115200);
}

void loop() {
  gateA.check();
  gateB.check();
}
