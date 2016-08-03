#ifndef GateListener_h
#define GateListener_h
#include <arduino.h>

class GateListener {
private:
  bool isBallIn;
  uint64_t timer_ms;
  uint16_t treshold;
  uint8_t port;
  char serialMsg;
  uint16_t count;
public:
  GateListener(uint8_t _port, char _serialMsg, uint16_t _treshold);
  void check();
};

#endif
