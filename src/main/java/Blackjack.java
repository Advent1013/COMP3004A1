import java.util.Collections.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Blackjack {
	//Variables relating to selecting the user input mode for the game.
	public static enum GameUsingInput { File, Console, Error, Undefined }
	Scanner console_input;
	
	private GameUsingInput GameInputMode = GameUsingInput.Undefined;
	
	public GameUsingInput GetGameInputMode() { return GameInputMode; }
	
	public void SetGameContols(char Input) { 
		if(Input=='f') {
			GameInputMode = GameUsingInput.File;
			System.out.println("File input mode selected.\n");
		}
		else if(Input=='c') {
			GameInputMode = GameUsingInput.Console;
			System.out.println("Console input mode selected.\n");
		}
		else {
			GameInputMode = GameUsingInput.Error;
			System.out.println("Invalid input mode!\n");
		}
	}
	
	public void AskUserForGameInputMode() {
		if(console_input==null) console_input = new Scanner(System.in);
		System.out.println("Please select an input mode:\nConsole: 'c'\nRead from File: 'f'\n");
		String input_mode_result = console_input.nextLine().trim().toLowerCase();
		SetGameContols(input_mode_result.charAt(0));
	}
	
	
	
	//Deck class for which the Blackjack game will draw its cards from.
	public static class Deck{
		private Deque<Card> Cards;
		public Deck() {
			Cards = new LinkedList<Card>();
		}
		
		public Deque<Card> GetCards(){ return Cards; }
		
		public void SetCards(LinkedList<Card> c) { Cards = c; }
		
		//Returns true if card was valid and has been added to the deck
		//Returns false if otherwise.
		public boolean AddCard(String card) {
			if(!IsCardValid(card)) return false;
			Cards.addLast(new Card(card));
			return true;
		}
		
		public Card DrawCard() {
			if(Cards.size()==0) {
				System.out.println("Error! The deck is empty!");
				System.exit(1);
			}
			return Cards.removeFirst();
		}
		//Outputs the contents of the deck to a string
		@Override 
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
			for(int i = 0; i < Card_List.size() - 1; i++) {
				for(int j = i + 1; j < Card_List.size(); j++) {
					if(Card_List.get(i).isEqualTo(Card_List.get(j))) {
						return true;
					}
				}
			}
			return false;
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
				if(Card.substring(1,3).equals("10")) RankIsOK = true;
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
				if(Card.substring(1,3).equals("10")) Rank = RANK.Ten;
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
		
		@Override 
		public String toString() {
			return Suit.Value+Rank.Name;
		}
	    public boolean isEqualTo(Card c) {
	    	if(this.Rank.equals(c.Rank)&&this.Suit.equals(c.Suit)) return true;
	    	return false;
	    }
	}
	
	public Deck GameDeck;
	
	public boolean CreateDeckFromFile(String Filename) {
		EventQueue = new LinkedList<Event>();
		File file = new File(Filename);
		Scanner File_Scanner;
		try {
			File_Scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("File could not be found\n");
			return false;
		}
		String File_Contents = File_Scanner.nextLine();
		File_Scanner.close();
		
		GameDeck = new Deck();
		String[] File_Contents_Split = File_Contents.split("\\s+");
		for(String Content : File_Contents_Split) {
			if(Content.length() == 1) {
				char action = Content.charAt(0);
				switch(action) {
				case 'H' : EventQueue.add(Event.Hit); break;
				case 'S' : EventQueue.add(Event.Stand); break;
				}
			}
			else {
				GameDeck.AddCard(Content);
				EventQueue.add(Event.Card);
			}
		}
		if(Deck.AreCardsUnique(GameDeck)) {
			GameDeck = null;
			return false;
		}
		return true;
	}
	
	public void CreateDeckAtRandom() {
		Card.SUIT[] Suits = Card.SUIT.values();
		Card.RANK[] Ranks = Card.RANK.values();
		GameDeck = new Deck();
		LinkedList<Card> Card_List = new LinkedList<Card>();
		Random rand = new Random();
		
		for(Card.RANK Rank : Ranks) {
			for(Card.SUIT Suit : Suits) {
				Card card = new Card(Suit, Rank);
				if(Card_List.size()==0) Card_List.add(card);
				Card_List.add(rand.nextInt(Card_List.size()), card);
			}
		}
		GameDeck.SetCards(Card_List);
	}
	
	//Functions and variables relating to hand, player and dealer classes.
	public static class Hand{
		private LinkedList<Card> Cards;
		public int VisibleCards;
		
		public Hand() {
			Cards = new LinkedList<Card>();
		}
		
		public void addCard(Card c) {
			Cards.add(c);
		}
		
		public LinkedList<Card> GetHand() { return Cards; }
		
		@Override
		public String toString() {
			String out = "";
			for(int i = 0; (i < VisibleCards) && (i < Cards.size()); i++) {
				out += Cards.get(i).toString() + " ";
			}
			return out.trim();
		}
	}
	
	//Functions and variables relating to game state.
	public static enum GameState{ GameIdle, DistributeCards, ShowHands, PlayerHits, PlayerStands, DealerHits, DealerStands, PlayerWins, DealerWins }
	public static enum Event{ Card, Hit, Stand, Split }
	public GameState CurrentState;
	public Queue<Event> EventQueue;
	private boolean PlayerTurn;
	private boolean GameEnd;
	
	public static int CalcScore(Hand hand) {
		int score = 0;
		int aces = 0;
		for(Card card : hand.GetHand()) {
			score += card.Rank.Worth;
			if(card.Rank==Card.RANK.Ace) aces++;
		}
		while(aces>0&&score>21) { score-=10; aces--; }
		return score;
	}
	
	public Hand PlayerHand;
	public Hand DealerHand;
	
	public void init() {
		CurrentState = GameState.GameIdle;
	}

	public GameState DetermineWinner() {
		if(CalcScore(PlayerHand)>21) return GameState.DealerWins;
		if(CalcScore(DealerHand)>21) return GameState.PlayerWins;
		if(CalcScore(DealerHand)==21) return GameState.DealerWins;
		if(CalcScore(PlayerHand)==21) return GameState.PlayerWins;
		if(GameEnd) {
			if(CalcScore(PlayerHand)>CalcScore(DealerHand)) return GameState.PlayerWins; 
			else return GameState.DealerWins;
		}
		return null;
	}
	
	public GameState EvaluateNextState() {
		GameState PotentialWinner = DetermineWinner();
		if(PotentialWinner!=null) return PotentialWinner;

		if(!PlayerTurn) {
			if(CalcScore(DealerHand)<17) return GameState.DealerHits;
			else if(CalcScore(DealerHand)==17) {
				for(Card card : DealerHand.GetHand()) {
					if(card.Rank==Card.RANK.Ace) {
						return GameState.DealerHits;
					}
				}
			}
			return GameState.DealerStands;
				
		}
		if(GameInputMode==GameUsingInput.Console) {
			System.out.println("Please select an Action:\nHit: 'h'\nStand: 's'\nSplit: 'Unsupported'\n");
			String Action_result = console_input.nextLine().trim().toLowerCase();
			char Action = Action_result.charAt(0);
			if(Action=='h') {
				return GameState.PlayerHits;
			}
			else if(Action=='s') {
				return GameState.PlayerStands;
			}
			else {
				System.out.println("Invalid Action!");
				System.exit(1);
			}
			return null;
		}
		else {
			Event e = EventQueue.remove();
			switch(e){
			case Hit : return GameState.PlayerHits;
			case Stand : return GameState.PlayerStands;
			default:
				break;
			}
			return null;
		}
	}
	
	public void NextStep(boolean recursive) {
		switch(CurrentState){
		case GameIdle:
			PlayerHand = new Hand();
			DealerHand = new Hand();
			PlayerTurn = true;
			GameEnd = false;
			
			CurrentState = GameState.DistributeCards;
			System.out.println("----------------------------------------------------");
			System.out.println("Blackjack game has begun.\n");
			if(recursive) NextStep(recursive);
			break;
			
		case DistributeCards:
			PlayerHand.addCard(GameDeck.DrawCard());
			if(GameInputMode == GameUsingInput.File) EventQueue.remove();
			PlayerHand.addCard(GameDeck.DrawCard());
			if(GameInputMode == GameUsingInput.File) EventQueue.remove();
			DealerHand.addCard(GameDeck.DrawCard());
			if(GameInputMode == GameUsingInput.File) EventQueue.remove();
			DealerHand.addCard(GameDeck.DrawCard());
			if(GameInputMode == GameUsingInput.File) EventQueue.remove();
			DealerHand.VisibleCards = 1;
			PlayerHand.VisibleCards = 2;
			CurrentState = GameState.ShowHands;
			if(recursive) NextStep(recursive);
			break;
			
		case ShowHands: 
			System.out.println("Visible player cards: " + PlayerHand.toString() + " with a score of " + CalcScore(PlayerHand));
			System.out.println("Visible dealer cards: " + DealerHand.toString() + " with a score of " + CalcScore(DealerHand));
			System.out.println("");
			CurrentState = EvaluateNextState();
			if(recursive) NextStep(recursive);
			break;
			
		case PlayerHits: 
			System.out.println("Player hits!");
			PlayerHand.addCard(GameDeck.DrawCard());
			if(GameInputMode == GameUsingInput.File) EventQueue.remove();
			PlayerHand.VisibleCards++;
			System.out.println("Your current hand is: " + PlayerHand.toString() + " with a score of " + CalcScore(PlayerHand));
			CurrentState = EvaluateNextState();
			if(recursive) NextStep(recursive);
			break;
		case PlayerStands: 
			System.out.println("Player Stands!");
			PlayerTurn = false;
			CurrentState = EvaluateNextState();
			if(recursive) NextStep(recursive);
			break;
		case DealerHits: 
			System.out.println("Dealer hits!");
			DealerHand.addCard(GameDeck.DrawCard());
			CurrentState = EvaluateNextState();
			if(GameInputMode == GameUsingInput.File) EventQueue.remove();
			if(recursive) NextStep(recursive);
			break;
		case DealerStands: 
			System.out.println("Dealer Stands!");
			GameEnd = true;
			CurrentState = EvaluateNextState();
			if(recursive) NextStep(recursive);
			break;
		case PlayerWins: 
			DealerHand.VisibleCards = 52;
			System.out.println("The Player Wins!!");
			System.out.println("Player cards: " + PlayerHand.toString() + " with a score of " + CalcScore(PlayerHand));
			System.out.println("Dealer cards: " + DealerHand.toString() + " with a score of " + CalcScore(DealerHand));
			System.out.println("----------------------------------------------------");
			break;
		case DealerWins: 
			DealerHand.VisibleCards = 52;
			System.out.println("The Dealer Wins!!");
			System.out.println("Player cards: " + PlayerHand.toString() + " with a score of " + CalcScore(PlayerHand));
			System.out.println("Dealer cards: " + DealerHand.toString() + " with a score of " + CalcScore(DealerHand));
			System.out.println("----------------------------------------------------");
			break;
		}
	}
	
	public void run() {
		console_input = new Scanner(System.in);
		AskUserForGameInputMode();
		if(GameInputMode == GameUsingInput.File) {
			
			System.out.println("Please enter the filename:");
			String Filename = console_input.nextLine();
			
			CreateDeckFromFile(Filename);
		}
		else {
			CreateDeckAtRandom();
		}
		init();
		NextStep(true);
		console_input.close();
	}
}
