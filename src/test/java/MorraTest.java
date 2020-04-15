import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MorraTest {
		MorraInfo myMorra;

		@BeforeEach
		void init()
		{
			myMorra = new MorraInfo();
		}

		@Test
		void test1()
		{
			int player1guess = 1;
			int player2guess = 3;

			assertEquals(2, myMorra.morraGameLogic(player1guess, player2guess, 3), "different guesses, player 2 is correct");
		}

		@Test
		void test2()
		{
			int player1guess = 1;
			int player2guess = 3;

			assertEquals(1, myMorra.morraGameLogic(player1guess, player2guess, 1), "different guesses, player 1 is correct");
		}

		@Test
		void test3()
		{
			int player1guess = 1;
			int player2guess = 3;

			assertEquals(0, myMorra.morraGameLogic(player1guess, player2guess, 4), "different guesses, both players are incorrect");
		}

		@Test
		void test4()
		{
			int player1guess = 4;
			int player2guess = 4;

			assertEquals(0, myMorra.morraGameLogic(player1guess, player2guess, 4), "same guesses, both players are correct");
		}

		@Test
		void test5()
		{
			int player1guess = 4;
			int player2guess = 4;

			assertEquals(0, myMorra.morraGameLogic(player1guess, player2guess, 1), "same guesses, both players are incorrect");
		}

		@Test
		void GuessingTest() {
			myMorra.setGuessing(true);
			assertTrue(myMorra.isGuessing(), "testing isGuessing() and setGuessing() with true");

			myMorra.setGuessing(false);
			assertFalse(myMorra.isGuessing(), "testing isGuessing() and setGuessing() with false");
		}

		@Test
		void TwoPlayersTest() {
			myMorra.setTwoPlayers(true);
			assertTrue(myMorra.isTwoPlayers(), "testing isTwoPlayers() and setTwoPlayers() with true");

			myMorra.setTwoPlayers(false);
			assertFalse(myMorra.isTwoPlayers(), "testing isTwoPlayers() and setTwoPlayers() with false");
		}


		@Test
		void PlayerStringTest() {
			myMorra.setPlayerString("player 1 wins");
			assertEquals("player 1 wins", myMorra.getPlayerString(), "testing getPlayerString() and setPlayerString()");
		}

		@Test
		void pNumTest() {
			myMorra.setpNum(1);
			assertEquals(1, myMorra.getpNum(), "testing setpNum() and getpNum()");
		}

		@Test
		void P1PointsTest() {
			myMorra.setP1Points(2);
			assertEquals(2, myMorra.getP1Points(), "testing setP1PointsTest() and getP1PointsTest");
		}

		@Test
		void P2PointsTest() {
			myMorra.setP2Points(2);
			assertEquals(2, myMorra.getP2Points(), "testing setP2PointsTest() and getP2PointsTest");
		}

		@Test
		void P1PlaysTest() {
			myMorra.setP1Plays("5");
			assertEquals("5", myMorra.getP1Plays(), "testing setP1Plays() and getP1Plays()");
		}

		@Test
		void P2PlaysTest() {
			myMorra.setP2Plays("5");
			assertEquals("5", myMorra.getP2Plays(), "testing setP2Plays() and getP2Plays()");
		}
}
