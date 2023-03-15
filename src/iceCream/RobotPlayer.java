package iceCream;

import battlecode.common.*;

import iceCream.robots.amplifier.*;
import iceCream.robots.booster.*;
import iceCream.robots.carrier.*;
import iceCream.robots.destabilizer.*;
import iceCream.robots.headquarters.*;
import iceCream.robots.launcher.*;
import iceCream.utils.Log;

/**
 * RobotPlayer is the class that describes your main robot strategy.
 * The run() method inside this class is like your main function: this is what we'll call once your robot
 * is created!
 */
public strictfp class RobotPlayer {
    /**
     * We will use this variable to count the number of turns this robot has been alive.
     * You can use static variables like this to save any information you want. Keep in mind that even though
     * these variables are static, in Battlecode they aren't actually shared between your robots.
     */
    static int turnCount = 0;
    static int birthRound = -1;
    static final boolean printBytecode = false;

    public static Team OPPONENT = null;
    public static Team MY_TEAM = null;

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * It is like the main function for your robot. If this method returns, the robot dies!
     *
     * @param rc  The RobotController object. You use it to perform actions from this robot, and to get
     *            information on its current status. Essentially your portal to interacting with the world.
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        // Hello world! Standard output is very useful for debugging.
        // Everything you say here will be directly viewable in your terminal when you run a match!
        // System.out.println("I'm a " + rc.getType() + " and I just got created! I have health " + rc.getHealth());

        // You can also use indicators to save debug notes in replays.
        rc.setIndicatorString("Hello world!");

        // Set the round this robot was born
        birthRound = rc.getRoundNum();

        // Set the logger's rc
        Log.rc = rc;

        // Set the team variables
        MY_TEAM = rc.getTeam();
        OPPONENT = MY_TEAM.opponent();

        while (true) {
            // This code runs during the entire lifespan of the robot, which is why it is in an infinite
            // loop. If we ever leave this loop and return from run(), the robot dies! At the end of the
            // loop, we call Clock.yield(), signifying that we've done everything we want to do.

            // Keep turnCount and round number in sync. These can be de-synced if
            //  we overuse bytecode. Note that this does NOT catch the case of an infinite loop.
            // if (turnCount != rc.getRoundNum()-birthRound) {
            //   Log.println("I'm a " + rc.getType() + " and my turnCount is " + turnCount + " when it should be " + (rc.getRoundNum()-birthRound));
            //   rc.resign();
            // }

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode.
            try {
                // The same run() function is called for every robot on your team, even if they are
                // different types. Here, we separate the control depending on the RobotType, so we can
                // use different strategies on different robots. If you wish, you are free to rewrite
                // this into a different control structure!
                switch (rc.getType()) {
                    case HEADQUARTERS:      Headquarters.run(rc);   break;
                    case CARRIER:           Carrier.run(rc);        break;
                    case LAUNCHER:          Launcher.run(rc);       break;
                    case BOOSTER:           Booster.run(rc);        break;
                    case DESTABILIZER:      Destabilizer.run(rc);   break;
                    case AMPLIFIER:         Amplifier.run(rc);      break;
                }

                // If enabled, print the amount of bytecode left per round for this robot. This is useful for performance
                //  analysis and debugging.
                if (printBytecode) {
                  Log.println(rc.getType() + " bytecode " + rc.getLocation() + " $" + Clock.getBytecodesLeft());
                }
            } catch (GameActionException e) {
                // Oh no! It looks like we did something illegal in the Battlecode world. You should
                // handle GameActionExceptions judiciously, in case unexpected events occur in the game
                // world. Remember, uncaught exceptions cause your robot to explode!
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();

            } catch (Exception e) {
                // Oh no! It looks like our code tried to do something bad. This isn't a
                // GameActionException, so it's more likely to be a bug in our code.
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();

            } finally {
                // Signify we've done everything we want to do, thereby ending our turn.
                // This will make our code wait until the next turn, and then perform this loop again.
                Clock.yield();
                turnCount += 1;  // We have now been alive for one more turn!
            }
            // End of loop: go back to the top. Clock.yield() has ended, so it's time for another turn!
        }

        // Your code should never reach here (unless it's intentional)! Self-destruction imminent...
    }
}
