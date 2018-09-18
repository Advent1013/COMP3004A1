import java.io.*;
import junit.framework.*;
import java.util.Scanner;
public class BlackjackTest extends TestCase{
	//Sample test file locations
	private String ValidSampleTest_1 = "TestCases/ValidSampleTest1.txt";
	private String ErrorDublicateCard = "TestCases/ErrorDuplicateCard.txt";
	
	//Testing if the game supports input mode selection.
	public void testGameStartMode() {
		Blackjack Game = new Blackjack();
		//Game mode selection should change Game.GetGameInputMode() to whatever enum Blackjack.GameUsing.File, Blackjack.GameUsing.Console or Blackjack.GameUsing.Error
		assertEquals(Blackjack.GameUsingInput.Undefined, Game.GetGameInputMode());
		
		//File
		Game.SetGameContols('f');
		assertEquals(Blackjack.GameUsingInput.File, Game.GetGameInputMode());
		
		//Console
		Game.SetGameContols('c');
		assertEquals(Blackjack.GameUsingInput.Console, Game.GetGameInputMode());
		
		//Invalid
		Game.SetGameContols('d');
		assertEquals(Blackjack.GameUsingInput.Error, Game.GetGameInputMode());
		
		
	}
	
	//Testing whether or not support for game input mode via user selection works.
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
	
	//Test if Deck creation works as intended.
	public void testDeckCreationFromFile() {
		//Read deck contents from file and then manually read and compare with test sample.
		Blackjack Game_File = new Blackjack();
		assertEquals(false, Game_File.CreateDeckFromFile(ValidSampleTest_1+"13adsad")); //Enusres funcion fails when invalid file name passed
		assertEquals(true, Game_File.CreateDeckFromFile(ValidSampleTest_1)); //Deck creation should return true if creation ended without any issue.
		assertNotNull(Game_File.Game_Deck);
		
		File file = new File(ValidSampleTest_1);
		//Read from file directly to endure Deck object has the same cards and maintains the order.
		Scanner File_Scanner = null;
		try {
			File_Scanner = new Scanner(file);
		} catch (FileNotFoundException e) {	}
		String File_Results = "";
		String File_Contents = File_Scanner.nextLine();
		File_Scanner.close();
		//Making sure to only add cards.
		String[] File_Contents_Split = File_Contents.split("\\s+");
		for(String Content : File_Contents_Split) {
			if(Content.length() == 2) {
				File_Results += Content + " ";
				assertEquals(true, Blackjack.Deck.IsCardValid(Content)); //Assuming all cards in the test are valid, this should always return true for every card.
			}
		}
		assertEquals(Game_File.Game_Deck.toString(),File_Results.trim());
		
		//Checks how deck creation handles duplicate files.
		Blackjack Game_Error_Duplicate_Card = new Blackjack();
		assertEquals(false, Game_Error_Duplicate_Card.CreateDeckFromFile(ErrorDublicateCard));
		assertEquals(null, Game_Error_Duplicate_Card.Game_Deck);
		
	}
	
	//Checks if decks generated at random actually generates a deck and each card is unique.	
	public void testDeckCreationAtRandom() {
		Blackjack Game_Console = new Blackjack();
		Game_Console.CreateDeckAtRandom();
		assertEquals(true, Game_Console.Game_Deck.GetCards().size() > 0);
		assertEquals(true, Blackjack.Deck.AreCardsUnique(Game_Console.Game_Deck));
		

	}
	
	//Test to see if player and dealer hands work
	public void testPlayerandDealerHands() {
		
	}
	
	//Test to see point ranking of cards.
	public void testCardandScores() {
		
	}
	
}
