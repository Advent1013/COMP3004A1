import java.io.*;
import junit.framework.*;
import java.util.Scanner;

import Blackjack.Card;
public class BlackjackTest extends TestCase{
	//Sample test file locations
	private String ValidSampleTest1 = "TestCases/ValidSampleTest1.txt";
	private String ValidSampleTest2 = "PlayerHitsMultipleTimesAndBusts.txt";
	private String ValidSampleTest3 = "PlayerHitsOnceAndStandsDealerHitsTwiceAndStands.txt";
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
		Blackjack GameFile = new Blackjack();
		assertEquals(false, GameFile.CreateDeckFromFile(ValidSampleTest1+"13adsad")); //Enusres funcion fails when invalid file name passed
		assertEquals(true, GameFile.CreateDeckFromFile(ValidSampleTest1)); //Deck creation should return true if creation ended without any issue.
		assertNotNull(GameFile.GameDeck);
		
		File file = new File(ValidSampleTest1);
		//Read from file directly to endure Deck object has the same cards and maintains the order.
		Scanner FileScanner = null;
		try {
			FileScanner = new Scanner(file);
		} catch (FileNotFoundException e) {	}
		String FileResults = "";
		String FileContents = FileScanner.nextLine();
		FileScanner.close();
		//Making sure to only add cards.
		String[] FileContentsSplit = FileContents.split("\\s+");
		for(String Content : FileContentsSplit) {
			if(Content.length() == 2) {
				FileResults += Content + " ";
				assertEquals(true, Blackjack.Deck.IsCardValid(Content)); //Assuming all cards in the test are valid, this should always return true for every card.
			}
		}
		assertEquals(GameFile.GameDeck.toString(),FileResults.trim());
		
		//Checks how deck creation handles duplicate files.
		Blackjack GameErrorDuplicateCard = new Blackjack();
		assertEquals(false, GameErrorDuplicateCard.CreateDeckFromFile(ErrorDublicateCard));
		assertEquals(null, GameErrorDuplicateCard.GameDeck);
		
	}
	
	//Checks if decks generated at random actually generates a deck and each card is unique.	
	public void testDeckCreationAtRandom() {
		Blackjack GameConsole = new Blackjack();
		GameConsole.CreateDeckAtRandom();
		assertEquals(true, GameConsole.GameDeck.GetCards().size() > 0);
		assertEquals(true, Blackjack.Deck.AreCardsUnique(GameConsole.GameDeck));
	}
	
	//Test to see point ranking of cards.
	public void testCardScores() {
		Blackjack.Hand TestHand = new Blackjack.Hand();
		//Checking score of a single Ace and then a double Ace should be 11, and then 11+1 afterwards.
		Hand.addCard(new Blackjack.Card(Blackjack.Card.SUIT.Clubs,Blackjack.Card.RANK.Ace));
		AssertEquals(11,Blackjack.CalcScore(Hand));
		Hand.addCard(new Blackjack.Card(Blackjack.Card.SUIT.Diamonds,Blackjack.Card.RANK.Ace));
		AssertEquals(12,Blackjack.CalcScore(Hand));
		//Checking score to see if both aces are valued as 1 each, when a ten card is added, making the score 10+1+1
		Hand.addCard(new Blackjack.Card(Blackjack.Card.SUIT.Diamonds,Blackjack.Card.RANK.Nine));
		AssertEquals(12,Blackjack.CalcScore(Hand));
		
		
		//Testing to see if Jing Queen and Jack are worth 10 each
		Blackjack.Hand TestHand = new Blackjack.Hand();
		Hand.addCard(new Blackjack.Card(Blackjack.Card.SUIT.Clubs,Blackjack.Card.RANK.King));
		AssertEquals(10,Blackjack.CalcScore(Hand));
		
		Hand.addCard(new Blackjack.Card(Blackjack.Card.SUIT.Clubs,Blackjack.Card.RANK.Queen));
		AssertEquals(10,Blackjack.CalcScore(Hand));
		
		Hand.addCard(new Blackjack.Card(Blackjack.Card.SUIT.Clubs,Blackjack.Card.RANK.Jack));
		AssertEquals(10,Blackjack.CalcScore(Hand));
	}
	
	//Test to see if player can hit and bust.
	public void testPlayerHitandBust() {
		//Test player hits.
		Blackjack GamePlayerHits = new Blackjack();
		GamePlayerHits.SetGameContols('f');
		GamePlayerHits.CreateDeckFromFile(ValidSampleTest2);
		GamePlayerHits.init();
		GamePlayerHits.NextStep();
		//Check if step is to distribute cards.
		assertEquals(Blackjack.GameState.DistributeCards, GamePlayerHits.CurrentState);
		GamePlayerHits.NextStep();
		//Check visible cards, two player cards and one dealer card
		assertEquals(2,GamePlayerHits.Player.VisibleCards.size());
		assertEquals(1,GamePlayerHits.Dealer.VisibleCards.size());
		assertEquals(Blackjack.GameState.ShowHands, GamePlayerHits.CurrentState);
		GamePlayerHits.NextStep();
		
		//Player hit
		assertEquals(Blackjack.GameState.PlayerHits, GamePlayerHits.CurrentState);
		GamePlayerHits.NextStep();
		
		//Player hit
		assertEquals(Blackjack.GameState.PlayerHits, GamePlayerHits.CurrentState);
		GamePlayerHits.NextStep();
		
		//Player busts
		assertEquals(Blackjack.GameState.DealerWins, GamePlayerHits.CurrentState);
	}
	
	//Test to see if the player can stand and if the dealer can hit and and stand
	public void testPlayerStandandDealerHitandStand() {
		//Test player hits.
		Blackjack GamePlayerHits = new Blackjack();
		GamePlayerHits.SetGameContols('f');
		GamePlayerHits.CreateDeckFromFile(ValidSampleTest3);
		GamePlayerHits.init();
		GamePlayerHits.NextStep();
		//Check if step is to distribute cards.
		assertEquals(Blackjack.GameState.DistributeCards, GamePlayerHits.CurrentState);
		GamePlayerHits.NextStep();
		
		assertEquals(Blackjack.GameState.ShowHands, GamePlayerHits.CurrentState);
		GamePlayerHits.NextStep();
		
		assertEquals(Blackjack.GameState.PlayerHits, GamePlayerHits.CurrentState);
		GamePlayerHits.NextStep();
		
		assertEquals(Blackjack.GameState.PlayerStands, GamePlayerHits.CurrentState);
		GamePlayerHits.NextStep();
		
		assertEquals(Blackjack.GameState.DealerHits, GamePlayerHits.CurrentState);
		GamePlayerHits.NextStep();
		
		assertEquals(Blackjack.GameState.DealerHits, GamePlayerHits.CurrentState);
		GamePlayerHits.NextStep();
		
		assertEquals(Blackjack.GameState.DealerStands, GamePlayerHits.CurrentState);
		GamePlayerHits.NextStep();
		
		assertEquals(Blackjack.GameState.DealerWins, GamePlayerHits.CurrentState);
		GamePlayerHits.NextStep();
	}
	
	//Test to ensure winners are selected appropriately.
	public void testWinConditions() {
		//Autoplay function will be used
	}
}
