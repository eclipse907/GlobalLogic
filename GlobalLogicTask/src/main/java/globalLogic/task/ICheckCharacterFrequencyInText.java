package globalLogic.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public interface ICheckCharacterFrequencyInText {

	void displayCharacterFrequencyInText(BufferedReader br, BufferedWriter bw);
	IWordCharacterSet getCharactersToCheck();
	IExcludedCharacters getExcludedCharacters();
	
}
