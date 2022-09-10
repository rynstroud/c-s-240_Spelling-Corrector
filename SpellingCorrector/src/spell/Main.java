package spell;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * A simple main class for running the spelling corrector. This class is not
 * used by the passoff program.
 */
public class Main {
	
	/**
	 * Give the dictionary file name as the first argument and the word to correct
	 * as the second argument.
	 */
	public static void main(String[] args) throws IOException {
		
		String dictionaryFileName = args[0];

		//
        //Create an instance of your corrector here
        //
		ISpellCorrector corrector = new SpellCorrector();//FIXME: finish
		
		corrector.useDictionary(dictionaryFileName);

		for (int i = 1; i < args.length; ++i) {
			String inputWord = args[i];
			String suggestion = corrector.suggestSimilarWord(inputWord);
			if (suggestion == null) {
				suggestion = "No similar word found";
			}

			System.out.println("Suggestion is: " + suggestion);
		}
	}

}
