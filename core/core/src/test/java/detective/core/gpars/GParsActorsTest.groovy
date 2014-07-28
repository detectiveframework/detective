package detective.core.gpars;

import static org.junit.Assert.*;

import org.junit.Test;

import groovyx.gpars.actor.Actor
import groovyx.gpars.actor.DefaultActor

class GParsActorsTest {

  class GameMaster extends DefaultActor {
    int secretNum

    void afterStart() {
      secretNum = new Random().nextInt(10)
    }

    void act() {
      loop {
        react { int num ->
          if (num > secretNum)
            reply 'too large'
          else if (num < secretNum)
            reply 'too small'
          else {
            reply 'you win'
            terminate()
          }
        }
      }
    }
  }

  class Player extends DefaultActor {
    String name
    Actor server
    int myNum

    void act() {
      loop {
        myNum = new Random().nextInt(10)
        server.send myNum
        react {
          switch (it) {
            case 'too large': println "$name: $myNum was too large"; break
            case 'too small': println "$name: $myNum was too small"; break
            case 'you win': println "$name: I won $myNum"; terminate(); break
          }
        }
      }
    }
  }

  @Test
  public void test() {
    def master = new GameMaster().start()
    def player = new Player(name: 'Player', server: master).start()

    //this forces main thread to live until both actors stop
    [master, player]*.join()
  }
}
