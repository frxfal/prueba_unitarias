package org.iesvdm.tddjava.connect4;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testng.Assert.assertEquals;


public class Connect4TDDSpec {

    /**
     * clase bajo testeo
     */
    private Connect4TDD tested;

    private OutputStream output;

    @BeforeEach
    public void beforeEachTest() {
        output = new ByteArrayOutputStream();

        //Se instancia el juego modificado para acceder a la salida de consola
        tested = new Connect4TDD(new PrintStream(output));
    }

    /*
     * The board is composed by 7 horizontal and 6 vertical empty positions
     */
    @Test
    public void whenTheGameStartsTheBoardIsEmpty() {
        assertThat(tested.getNumberOfDiscs()).isEqualTo(0);
    }

    /*
     * Players introduce discs on the top of the columns.
     * Introduced disc drops down the board if the column is empty.
     * Future discs introduced in the same column will stack over previous ones
     */

    @Test
    public void whenDiscOutsideBoardThenRuntimeException() {
        assertThrows(RuntimeException.class, () -> tested.putDiscInColumn(-1));
        assertThrows(RuntimeException.class, () -> tested.putDiscInColumn(7));
    }

    @Test
    public void whenFirstDiscInsertedInColumnThenPositionIsZero() {
        assertThat(tested.putDiscInColumn(0)).isEqualTo(0);
    }

    @Test
    public void whenSecondDiscInsertedInColumnThenPositionIsOne() {
        tested.putDiscInColumn(0);
        assertThat(tested.putDiscInColumn(0)).isEqualTo(1);
    }

    @Test
    public void whenDiscInsertedThenNumberOfDiscsIncreases() {
        tested.putDiscInColumn(0);
        assertThat(tested.getNumberOfDiscs()).isEqualTo(1);
    }

    @Test
    public void whenNoMoreRoomInColumnThenRuntimeException() {
        for (int i = 0; i < 6; i++) {
            tested.putDiscInColumn(0);
        }
        assertThrows(RuntimeException.class, () -> tested.putDiscInColumn(0));
    }

    /*
     * It is a two-person game so there is one colour for each player.
     * One player uses red ('R'), the other one uses green ('G').
     * Players alternate turns, inserting one disc every time
     */

    @Test
    public void whenFirstPlayerPlaysThenDiscColorIsRed() {
        assertThat(tested.getCurrentPlayer()).isEqualTo("R");
    }

    @Test
    public void whenSecondPlayerPlaysThenDiscColorIsGreen() {
        tested.putDiscInColumn(0);
        assertThat(tested.getCurrentPlayer()).isEqualTo("G");
    }

    /*
     * We want feedback when either, event or error occur within the game.
     * The output shows the status of the board on every move
     */

    @Test
    public void whenAskedForCurrentPlayerTheOutputNotice() {
        assertEquals("R", tested.getCurrentPlayer());
    }

    @Test
    public void whenADiscIsIntroducedTheBoardIsPrinted() {
        tested.putDiscInColumn(0);
        assertThat(output.toString()).contains("| | | | | | |");
    }

    /*
     * When no more discs can be inserted, the game finishes and it is considered a draw
     */

    @Test
    public void whenTheGameStartsItIsNotFinished() {
        assertThat(tested.isFinished()).isFalse();
    }

    @Test
    public void whenNoDiscCanBeIntroducedTheGamesIsFinished() {
        for (int column = 0; column < 7; column++) {
            for (int row = 0; row < 6; row++) {
                tested.putDiscInColumn(column);
            }
        }
        assertThat(tested.isFinished()).isTrue();
    }

    /*
     * If a player inserts a disc and connects more than 3 discs of his colour
     * in a straight vertical line then that player wins
     */

    @Test
    public void when4VerticalDiscsAreConnectedThenThatPlayerWins() {
        for (int i = 0; i < 4; i++) {
            tested.putDiscInColumn(0);
            tested.putDiscInColumn(1); // This alternates between players
        }
        assertThat(tested.getWinner()).isEqualTo("R");
    }

    /*
     * If a player inserts a disc and connects more than 3 discs of his colour
     * in a straight horizontal line then that player wins
     */

    @Test
    public void when4HorizontalDiscsAreConnectedThenThatPlayerWins() {
        tested.putDiscInColumn(0); // R
        tested.putDiscInColumn(0); // R
        tested.putDiscInColumn(1); // G
        tested.putDiscInColumn(1); // R
        tested.putDiscInColumn(2); // G
        tested.putDiscInColumn(2); // R
        tested.putDiscInColumn(3); // G
        tested.putDiscInColumn(3); // R

        assertEquals("R", tested.getWinner());
    }

    /*
     * If a player inserts a disc and connects more than 3 discs of his colour
     * in a straight diagonal line then that player wins
     */

    @Test
    public void when4Diagonal1DiscsAreConnectedThenThatPlayerWins() {
        for (int i = 0; i < 3; i++) {
            tested.putDiscInColumn(i);
            tested.putDiscInColumn(i % 2 == 0 ? 1 : 0); // This alternates between players
        }
        tested.putDiscInColumn(3);
        tested.putDiscInColumn(2);
        tested.putDiscInColumn(3);
        tested.putDiscInColumn(3);
        tested.putDiscInColumn(4);
        tested.putDiscInColumn(4);
        tested.putDiscInColumn(3);
        tested.putDiscInColumn(4);
        tested.putDiscInColumn(5);
        assertThat(tested.getWinner()).isEqualTo("R");
    }

    @Test
    public void when4Diagonal2DiscsAreConnectedThenThatPlayerWins() {
        tested.putDiscInColumn(0); // R
        tested.putDiscInColumn(0); // G
        tested.putDiscInColumn(1); // R
        tested.putDiscInColumn(1); // G
        tested.putDiscInColumn(2); // R
        tested.putDiscInColumn(2); // G
        tested.putDiscInColumn(3); // R

        assertEquals("R", tested.getWinner());
    }
}
