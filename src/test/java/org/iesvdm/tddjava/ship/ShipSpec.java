package org.iesvdm.tddjava.ship;

import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

@Test
public class ShipSpec {

    private Ship ship;
    private Location location;
    private Planet planet;

    @BeforeMethod
    public void beforeTest() {
        Point max = new Point(50, 50);
        location = new Location(new Point(21, 13), Direction.NORTH);
        List<Point> obstacles = new ArrayList<>();
        obstacles.add(new Point(44, 44));
        obstacles.add(new Point(45, 46));
        planet = new Planet(max, obstacles);
//        ship = new Ship(location);
        ship = new Ship(location, planet);
    }

    public void whenInstantiatedThenLocationIsSet() {
//        Location location = new Location(new Point(21, 13), Direction.NORTH);
//        Ship ship = new Ship(location);

    }

//    public void givenNorthWhenMoveForwardThenYDecreases() {
//        ship.moveForward();
//        assertEquals(ship.getLocation().getPoint().getY(), 12);
//    }
//
//    public void givenEastWhenMoveForwardThenXIncreases() {
//        ship.getLocation().setDirection(Direction.EAST);
//        ship.moveForward();
//        assertEquals(ship.getLocation().getPoint().getX(), 22);
//    }

    public void whenMoveForwardThenForward() {
        assertTrue(ship.moveForward());
        assertEquals(ship.getLocation().getPoint().getY(), 12);
    }

    public void whenMoveBackwardThenBackward() {
        assertTrue(ship.moveBackward());
        assertEquals(ship.getLocation().getPoint().getY(), 14);
    }

    public void whenTurnLeftThenLeft() {
        ship.turnLeft();
        assertEquals(ship.getLocation().getDirection(), Direction.WEST);
    }

    public void whenTurnRightThenRight() {
        ship.turnRight();
        assertEquals(ship.getLocation().getDirection(), Direction.EAST);
    }

    public void whenReceiveCommandsFThenForward() {
        assertEquals(ship.receiveCommands("f"), "O");
        assertEquals(ship.getLocation().getPoint().getY(), 12);
    }

    public void whenReceiveCommandsBThenBackward() {
        assertEquals(ship.receiveCommands("b"), "O");
        assertEquals(ship.getLocation().getPoint().getY(), 14);
    }

    public void whenReceiveCommandsLThenTurnLeft() {
        assertEquals(ship.receiveCommands("l"), "O");
        assertEquals(ship.getLocation().getDirection(), Direction.WEST);
    }

    public void whenReceiveCommandsRThenTurnRight() {
        assertEquals(ship.receiveCommands("r"), "O");
        assertEquals(ship.getLocation().getDirection(), Direction.EAST);
    }

    public void whenReceiveCommandsThenAllAreExecuted() {
        assertEquals(ship.receiveCommands("fflbr"), "OOOOO");
        assertEquals(ship.getLocation().getPoint().getX(), 22);
        assertEquals(ship.getLocation().getPoint().getY(), 11);
        assertEquals(ship.getLocation().getDirection(), Direction.NORTH);
    }

    public void whenInstantiatedThenPlanetIsStored() {
        assertEquals(ship.getPlanet(), planet);
    }

    public void givenDirectionEAndXEqualsMaxXWhenReceiveCommandsFThenWrap() {
        ship.getLocation().setDirection(Direction.EAST);
        ship.getLocation().getPoint().setX(50);
        assertEquals(ship.receiveCommands("f"), "O");
        assertEquals(ship.getLocation().getPoint().getX(), 1);
    }

    public void givenDirectionEAndXEquals1WhenReceiveCommandsBThenWrap() {
        ship.getLocation().setDirection(Direction.EAST);
        ship.getLocation().getPoint().setX(1);
        assertEquals(ship.receiveCommands("b"), "O");
        assertEquals(ship.getLocation().getPoint().getX(), 50);
    }

    public void whenReceiveCommandsThenStopOnObstacle() {
        planet.getObstacles().add(new Point(21, 14));
        assertEquals(ship.receiveCommands("f"), "O");
        assertEquals(ship.getLocation().getPoint().getY(), 12);
    }

    public void whenReceiveCommandsThenOForOkAndXForObstacle() {
        planet.getObstacles().add(new Point(21, 13));
        assertEquals(ship.receiveCommands("f"), "O");
    }
}

