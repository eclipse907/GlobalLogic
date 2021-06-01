package globalLogic.task;

import java.util.Set;

public interface IExcludedCharacters {
	
	Set<Character> getExcludedCharacters();
	boolean isCharacterExcluded(char c);

}
