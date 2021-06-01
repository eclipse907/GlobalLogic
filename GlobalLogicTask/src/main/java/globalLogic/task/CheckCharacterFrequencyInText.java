package globalLogic.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CheckCharacterFrequencyInText implements ICheckCharacterFrequencyInText {
	
	private IWordCharacterSet charactersToCheck;
	private IExcludedCharacters excludedCharacters;
	
	public CheckCharacterFrequencyInText(IWordCharacterSet charactersToCheck, IExcludedCharacters excludedCharacters) {
		if (charactersToCheck == null || excludedCharacters == null) {
			throw new NullPointerException("Constructor arguments can't be null");
		}
		this.charactersToCheck = charactersToCheck;
		this.excludedCharacters = excludedCharacters;
	}

	@Override
	public void displayCharacterFrequencyInText(BufferedReader br, BufferedWriter bw) {
		if (br == null || bw == null) {
			throw new NullPointerException("Input and output streams can't be null");
		}
		String text = br.lines().collect(Collectors.joining(" "));
		SimpleEntry<Map<SimpleEntry<Set<Character>, Integer>, Double>, Double> result = calculateCharFrequency(text);
		result.getKey().entrySet()
					   .stream()
					   .sorted(Comparator.comparingDouble(e -> e.getValue()))
					   .forEach(e -> {
						   
						   try {
								bw.write("{" + e.getKey().getKey() + ", " + e.getKey().getValue() + "} = " + e.getValue() + "\n");
						   } catch (IOException ex) {
								System.out.println("Error while writing result to output stream:\n" + ex.getLocalizedMessage());
								System.exit(1);
						   }
					   });
		try {
			bw.write("TOTAL Frequency: " + result.getValue() + "\n");
		} catch (IOException ex) {
			System.out.println("Error while writing result to output stream:\n" + ex.getLocalizedMessage());
			System.exit(1);
		}
	}

	@Override
	public IWordCharacterSet getCharactersToCheck() {
		return charactersToCheck;
	}

	@Override
	public IExcludedCharacters getExcludedCharacters() {
		return excludedCharacters;
	}
	
	private SimpleEntry<Map<SimpleEntry<Set<Character>, Integer>, Double>, Double> calculateCharFrequency(String text) {
		String[] wordsInText = text.split("[\\s\\n]+");
		Map<SimpleEntry<Set<Character>, Integer>, Integer> charNumInText = new HashMap<>();
		int totalNumOfCharsToCheckInText = 0;
		int totalNumOfCharsInText = 0;
		for (String word : wordsInText) {
			int wordSize = 0;
			int numOfCharsToCheckInWord = 0;
			List<Character> charsToCheckInWord = new ArrayList<>();
			for (char c : word.toCharArray()) {
				if (excludedCharacters.isCharacterExcluded(c)) {
					continue;
				}
				wordSize++;
				totalNumOfCharsInText++;
				if (charactersToCheck.isCharacterInWord(c)) {
					totalNumOfCharsToCheckInText++;
					numOfCharsToCheckInWord++;
					charsToCheckInWord.add(Character.toLowerCase(c));
				}
			}
			if (numOfCharsToCheckInWord > 0) {
				charsToCheckInWord.sort(charactersToCheck.getWordCharacterComparator());
				SimpleEntry<Set<Character>, Integer> charNumInWord = new SimpleEntry<>(
						new LinkedHashSet<>(charsToCheckInWord), wordSize
				);
				charNumInText.put(charNumInWord, charNumInText.getOrDefault(charNumInWord, 0) + numOfCharsToCheckInWord);
			}
		}
		Map<SimpleEntry<Set<Character>, Integer>, Double> charFrequency = new HashMap<>();
		for (SimpleEntry<Set<Character>, Integer> key : charNumInText.keySet()) {
			BigDecimal bd = new BigDecimal(charNumInText.get(key).doubleValue() / totalNumOfCharsToCheckInText);
			bd = bd.setScale(2, RoundingMode.HALF_UP);
			charFrequency.put(key, bd.doubleValue());
		}
		BigDecimal bd = new BigDecimal((double)totalNumOfCharsToCheckInText / totalNumOfCharsInText);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return new SimpleEntry<>(charFrequency, bd.doubleValue());
	}

}
