import java.util.Collections.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Blackjack {
	//Variables relating to selecting the user input mode for the game.
	public static enum GameUsingInput { File, Console, Error, Undefined }
	
	private GameUsingInput GameInputMode = GameUsingInput.Undefined;
	
	public GameUsingInput GetGameInputMode() { return GameInputMode; }
	
	public void SetGameContols(char Input) { 
		if(Input=='f') GameInputMode = GameUsingInput.File;
		else if(Input=='c') GameInputMode = GameUsingInput.Console;
		else GameInputMode = GameUsingInput.Error;
	}
	
	public void AskUserForGameInputMode() {
		Scanner console_input = new Scanner(System.in);
		System.out.println("Please select an input mode:\nConsole: 'c'\nRead from File: 'f'");
		String input_mode_result = console_input.nextLine().trim().toLowerCase();
		SetGameContols(input_mode_result.charAt(0));
		console_input.close();
	}
	
	
	//Deck class for which the Blackjack game will draw its cards from.
	public static class Deck{
		private Deque<Card> Cards;
		public Deck() {
			Cards = new LinkedList<Card>();
		}
		
		public Deque<Card> GetCards(){ return Cards; }
		
		//Returns true if card was valid and has been added to the deck
		//Returns false if otherwise.
		public boolean addCard(String card) {
			if(!IsCardValid(card)) return false;
			Cards.addLast(new Card(card));
			return true;
		}
		//Outputs the contents of the deck to a string.
		public String toString() {
			LinkedList<Card> Card_List = (LinkedList<Card>) Cards;
			String out = "";
			for(Card Card : Card_List) {
				out += Card.toString() + " ";
			}
			return out.trim();
		}
		//Ensures that each card in the deck are different.
		public static boolean AreCardsUnique(Deck Card_Deck) {
			LinkedList<Card> Card_List = (LinkedList<Card>) Card_Deck.GetCards();
			boolean hasDuplicate = false;
			for(int i = 0; i < Card_List.size() - 1; i++) {
				for(int j = i + 1; j < Card_List.size(); j++) {
					hasDuplicate = Card_List.get(i).isEqualTo(Card_List.get(j));
					if(hasDuplicate) break;
				}
				if(hasDuplicate) break;
			}
			return hasDuplicate;
		}
		//Manually ensures that a string can be converted into a card.
		public static boolean IsCardValid(String Card) {
			Card = Card.toUpperCase();
			if(Card.length()<2) return false;
			
			boolean SuitIsOK = false;
			boolean RankIsOK = false;
			
			char suit = Card.charAt(0);
			
			switch(suit) {
			case 'S' : SuitIsOK = true; break;
			case 'C' : SuitIsOK = true; break;
			case 'D' : SuitIsOK = true; break;
			case 'H' : SuitIsOK = true; break;
			}
			
			//Checks if card is a 10 as it is the only card with three characters.
			if(Card.length()==3) 
				if(Card.substring(1,2).equals("10")) RankIsOK = true;
				else return false;
			
			char rank = Card.charAt(1);
			
			switch(rank) {
			case 'A' : RankIsOK = true; break;
			case '2' : RankIsOK = true; break;
			case '3' : RankIsOK = true; break;
			case '4' : RankIsOK = true; break;
			case '5' : RankIsOK = true; break;
			case '6' : RankIsOK = true; break;
			case '7' : RankIsOK = true; break;
			case '8' : RankIsOK = true; break;
			case '9' : RankIsOK = true; break;
			case 'J' : RankIsOK = true; break;
			case 'Q' : RankIsOK = true; break;
			case 'K' : RankIsOK = true; break;
			}
			
			return SuitIsOK&&RankIsOK;
		}
	}
	
	//Adding card class to facilitate card objects and make it easier to perform calculations.
	public static class Card{
		//Enumerators to make handling each element of each card easier.
		public static enum SUIT{ 
			Spades ('S'), Clubs ('C'), Diamonds ('D'), Hearts ('H'); 
			public char Value;
			private SUIT(char Val) { Value=Val; }
		}
		public static enum RANK{ 
			Ace("A",11), Two("2",2), Three("3",3), Four("4",4), Five("5",5), Six("6",6), Seven("7",7), Eight("8",8), Nine("9",9), Ten("10",10), Jack("J",10), Queen("Q",10), King("K",10);
			public String Name;
			public int Worth;
			private RANK(String N, int W) { Name = N; Worth = W; }
		}
		public SUIT Suit;
		public RANK Rank;
		public Card(SUIT s, RANK r) { Suit = s; Rank = r; }
		//Create card from string using similar code to that of IsCardValid but without the error checking.
		public Card(String Card) {
			Card = Card.toUpperCase();
			char suit = Card.charAt(0);
			
			switch(suit) {
			case 'S' : Suit = SUIT.Spades; break;
			case 'C' : Suit = SUIT.Clubs; break;
			case 'D' : Suit = SUIT.Diamonds; break;
			case 'H' : Suit = SUIT.Hearts; break;
			}
			
			//Checks if card is a 10 as it is the only card with three characters.
			if(Card.length()==3) 
				if(Card.substring(1,2).equals("10")) Rank = RANK.Ten;
			char rank = Card.charAt(1);
			
			switch(rank) {
			case 'A' : Rank = RANK.Ace; break;
			case '2' : Rank = RANK.Two; break;
			case '3' : Rank = RANK.Three; break;
			case '4' : Rank = RANK.Four; break;
			case '5' : Rank = RANK.Five; break;
			case '6' : Rank = RANK.Six; break;
			case '7' : Rank = RANK.Seven; break;
			case '8' : Rank = RANK.Eight; break;
			case '9' : Rank = RANK.Nine; break;
			case 'J' : Rank = RANK.Jack; break;
			case 'Q' : Rank = RANK.Queen; break;
			case 'K' : Rank = RANK.King; break;
			}
		}
		public String toString() {
			return Suit.Value+Rank.Name;
		}
	    public boolean isEqualTo(Card c) {
	    	if(this.Rank!=c.Rank) return false;
	    	if(this.Suit!=c.Suit) return false;
	    	return true;
	    }
	}
	
	public Deck Game_Deck;
	
	public boolean CreateDeckFromFile(String Filename) {
		File file = new File(Filename);
		Scanner File_Scanner;
		try {
			File_Scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			return false;
		}
		String File_Contents = File_Scanner.nextLine();
		File_Scanner.close();
		
		Game_Deck = new Deck();
		String[] File_Contents_Split = File_Contents.split("\\s+");
		for(String Content : File_Contents_Split) {
			if(Content.length() == 2) {
				Game_Deck.addCard(Content);
			}
			
		}
		
		return true;
	}
}
