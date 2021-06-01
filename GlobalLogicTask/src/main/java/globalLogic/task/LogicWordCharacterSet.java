package globalLogic.task;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

public class LogicWordCharacterSet implements IWordCharacterSet {
	
	private Set<Character> charactersInWord;
	private Comparator<Character> wordCharacterComparator;
	
	public LogicWordCharacterSet(String word) {
		if (word == null) {
			throw new NullPointerException("Input string is null");
		}
		charactersInWord = new LinkedHashSet<>();
		for (char c : word.toCharArray()) {
			charactersInWord.add(Character.toUpperCase(c));
		}
		String upperCaseWord = word.toUpperCase();
		wordCharacterComparator = (o1, o2) -> {
			int indexO1 = upperCaseWord.indexOf(Character.toUpperCase(o1));
			int indexO2 = upperCaseWord.indexOf(Character.toUpperCase(o2));
			if (indexO1 == -1 || indexO2 == -1) {
				throw new IllegalArgumentException("Character not in word");
			}
			return indexO1 - indexO2;
		};
	}

	@Override
	public Set<Character> getWordCharacters() {
		return Collections.unmodifiableSet(charactersInWord);
	}

	@Override
	public boolean isCharacterInWord(char c) {
		return charactersInWord.contains(Character.toUpperCase(c));
	}

	@Override
	public Comparator<Character> getWordCharacterComparator() {
		return wordCharacterComparator;
	}
	
}
