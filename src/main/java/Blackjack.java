import java.util.Scanner;

public class Blackjack {
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
	}
}
