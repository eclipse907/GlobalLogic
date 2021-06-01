package globalLogic.task;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ExcludedCharacters implements IExcludedCharacters {
	
	private Set<Character> charactersToIgnore;
	
	public ExcludedCharacters(char[] excludedCharacters) {
		if (excludedCharacters == null) {
			throw new NullPointerException("Input array is null");
		}
		charactersToIgnore = new HashSet<>();
		for (char c : excludedCharacters) {
			charactersToIgnore.add(c);
		}
	}

	@Override
	public Set<Character> getExcludedCharacters() {
		return Collections.unmodifiableSet(charactersToIgnore);
	}

	@Override
	public boolean isCharacterExcluded(char c) {
		return charactersToIgnore.contains(c);
	}

}
