import java.util.Collections.*;
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
		private Deque<String> Cards;
		public Deck() {
			Cards = new LinkedList<String>();
		}
		
		public Deque<String> GetCards(){ return Cards; }
		
		//Returns true if card was valid and has been added to the deck
		//Returns false if otherwise.
		public boolean addCard(String Card) {
			if(Card.length()<2) return false;
			Cards.push(Card);
			return true;
		}
		//Outputs the contents of the deck to a string.
		public String toString() {
			LinkedList<String> Card_List = (LinkedList<String>) Cards;
			String out = "";
			for(String Card : Card_List) {
				out += Card + " ";
			}
			return out.trim();
		}
		//Ensures that each card in the deck are different.
		public static boolean AreCardsUnique(Deck Card_Deck) {
			LinkedList<String> Card_List = (LinkedList<String>) Card_Deck.GetCards();
			boolean hasDuplicate = false;
			for(int i = 0; i < Card_List.size() - 1; i++) {
				for(int j = i + 1; j < Card_List.size(); j++) {
					hasDuplicate = Card_List.get(i).equals(Card_List.get(j));
					if(hasDuplicate) break;
				}
				if(hasDuplicate) break;
			}
			return hasDuplicate;
		}
		public static boolean IsCardValid(String Card) {
			if(Card.length()<2) return false;
			
			boolean result = false;
			char suit = Card.charAt(0);
			
		}
	}
	
	//Adding card class to facilitate card objects and make it easier to perform calculations.
	public static class Card{
		//Enumerators to make handling each element of each card easier.
		public static enum SUIT{ 
			Spades ('S'), Clubs ('C'), Diamonds ('D'), Hearts ('H'); 
			public char value;
			private SUIT(char val) { value=val; }
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
	}
	
	public Deck Game_Deck;
	
}
