#include <arduino.h>
#define pin_sensor A0
#define START_TRESHOLD 300

int count;
int last_value;
bool isBallIn;
uint64_t timer_ms;
int treshold;

void setup() {
  Serial.begin(115200);
  count = 0;
  isBallIn = false;
  treshold = START_TRESHOLD;
  last_value = analogRead(pin_sensor);
  Serial.println("setup finished");
}

void loop() {
  int current_value = analogRead(pin_sensor);
  treshold = current_value * 1.5 / 100; //correcting treshold
  if (current_value > treshold) {
    // Serial.print("s ");
    // Serial.print(current_value);
    // Serial.print(" ");
    // Serial.println(last_value);
    isBallIn = true;
    timer_ms = millis() + 200;
  }
  if (current_value < treshold  && isBallIn) {
    count++;
    Serial.print("g # "); Serial.println(count);
    isBallIn = false;
  }
  if (timer_ms < millis() && isBallIn) {
    isBallIn = false;
    //Serial.println("t");
  }

  last_value = current_value;

  // if (abs(last_value - current_value) > TRESHOLD) {
  //   Serial.print("value change from ");
  //   Serial.print(last_value);
  //   Serial.print(" to ");
  //   Serial.println(current_value);
  // }
}
