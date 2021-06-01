package globalLogic.task;

import java.util.Comparator;
import java.util.Set;

public interface IWordCharacterSet {
	
	Set<Character> getWordCharacters();
	boolean isCharacterInWord(char c);
	Comparator<Character> getWordCharacterComparator();
	
}
