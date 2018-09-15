import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.*;

public class BlackjackTest extends TestCase{

	//Tests temporarily hardcoded while I adjust to TDD
	public void testGameStartMode() {
		Blackjack Game = new Blackjack();
		//Game mode selection should change Game.GetGameInputMode() to whatever enum Blackjack.GameUsing.File, Blackjack.GameUsing.Console or Blackjack.GameUsing.Error
		assertEquals(Blackjack.GameUsingInput.Undefined, Game.GetGameInputMode());
		
		//File
		Game.SetGameContols('f');
		assertEquals(Blackjack.GameUsingInput.File, Game.GetGameInputMode());
		
		//Console
		Game.SetGameContols('c');
		
		
		//Invalid
		Game.SetGameContols('d');
		assertEquals(Blackjack.GameUsingInput.Error, Game.GetGameInputMode());
		
		
	}
	
	public void testUserGameInputModeSelection() {
		//Overriding system.in to simulate user input.
		InputStream UserEnterF_Stream = new ByteArrayInputStream("f\n".getBytes());
		InputStream UserEnterC_Stream = new ByteArrayInputStream("c\n".getBytes());
		InputStream UserEnterD_Stream = new ByteArrayInputStream("d\n".getBytes());
		
		Blackjack UserEnterF_Game = new Blackjack();
		Blackjack UserEnterC_Game = new Blackjack();
		Blackjack UserEnterD_Game = new Blackjack();
		
		System.setIn(UserEnterF_Stream);
		UserEnterF_Game.AskUserForGameInputMode();
		assertEquals(Blackjack.GameUsingInput.File, UserEnterF_Game.GetGameInputMode());
		
		System.setIn(UserEnterC_Stream);
		UserEnterC_Game.AskUserForGameInputMode();
		assertEquals(Blackjack.GameUsingInput.Console, UserEnterC_Game.GetGameInputMode());
		
		System.setIn(UserEnterD_Stream);
		UserEnterD_Game.AskUserForGameInputMode();
		assertEquals(Blackjack.GameUsingInput.Error, UserEnterD_Game.GetGameInputMode());
		
		System.setIn(System.in);
	}
}
