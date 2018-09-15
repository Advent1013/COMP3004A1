import junit.framework.*;

public class BlackjackTest extends TestCase{

	//Tests temporarily hardcoded while I adjust to TDD
	public void testGameStartMode() {
		Blackjack Game = new Blackjack();
		//Game mode selection should return whatever enum Blackjack.GameUsing.File, Blackjack.GameUsing.Console or Blackjack.GameUsing.Error
		assertEquals(Blackjack.GameUsing.File, Game.SetGameContols('f'));
		assertEquals(Blackjack.GameUsing.Console, Game.SetGameContols('c'));
		assertEquals(Blackjack.GameUsing.Error, Game.SetGameContols('d'));
	}
	
	
}
