import java.util.Collections.*;
import java.util.*;

public class Blackjack {
		public static enum GameUsingInput { File, Console, Error, Undefined }
	
	private GameUsingInput GameInputMode = GameUsingInput.Undefined;
	
	public static class Deck{
		private Deque<String> Cards;
		public Deck() {
			Cards = new LinkedList<String>();
		}
		
		public Deque<String> GetCards(){ return Cards; }
		
		//Returns true if card was valid and has been added to the deck
		//Returns false if otherwise.
		public boolean addCard(String Card) {
			if(Card.length()!=2) return false;
			Cards.push(Card);
			return true;
		}
		public String toString() {
			LinkedList<String> Card_List = (LinkedList<String>) Cards;
			String out = "";
			for(String Card : Card_List) {
				out += Card + " ";
			}
			return out.trim();
		}
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
	}
	
	public Deck Game_Deck;
	
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
	
	
	
}
