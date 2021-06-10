package globalLogic.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
	public void displayCharacterFrequencyInText(BufferedReader inputTextReader, BufferedWriter outputWriter) {
		if (inputTextReader == null || outputWriter == null) {
			throw new NullPointerException("Input and output streams can't be null");
		}
		try {
			Result result = calculateCharFrequency(inputTextReader);
			result.getCharsWordGroupingFrequency().entrySet()
			   .stream()
			   .sorted(Comparator.comparingDouble(e -> e.getValue().doubleValue()))
			   .forEach(e -> {
				try {
					outputWriter.write("{" + e.getKey().getCharsToCheckInWord() + ", " + e.getKey().getWordSize() + "} = " + e.getValue() + System.lineSeparator());
				} catch (IOException ex) {
					System.out.println("Error while writing result to output stream:" + System.lineSeparator() + ex.getLocalizedMessage());
					System.exit(1);
				}
			});
			outputWriter.write("TOTAL Frequency: " + result.getTotalCharFrequency() + System.lineSeparator());
		} catch (IOException ex) {
			System.out.println("Error while reading from input stream or writing to output stream:" + System.lineSeparator() + ex.getLocalizedMessage());
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
	
	private Result calculateCharFrequency(BufferedReader inputTextReader) throws IOException {
		Map<CharactersWordSizeGrouping, Integer> charNumPerGrouping = new HashMap<>();
		int totalNumOfCharsToCheckInText = 0;
		int totalNumOfCharsInText = 0;
		while (true) {
			String line = inputTextReader.readLine();
			if (line == null) {
				break;
			}
			for (String word : line.split("[\\s]+")) {
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
					CharactersWordSizeGrouping charNumInWord = new CharactersWordSizeGrouping(new LinkedHashSet<>(charsToCheckInWord), wordSize);
					charNumPerGrouping.put(charNumInWord, charNumPerGrouping.getOrDefault(charNumInWord, 0) + numOfCharsToCheckInWord);
				}
			}
		}
		Map<CharactersWordSizeGrouping, BigDecimal> charsWordGroupingFrequency = new HashMap<>();
		for (CharactersWordSizeGrouping key : charNumPerGrouping.keySet()) {
			BigDecimal frequency = new BigDecimal(charNumPerGrouping.get(key).doubleValue() / totalNumOfCharsToCheckInText);
			frequency = frequency.setScale(2, RoundingMode.HALF_UP);
			charsWordGroupingFrequency.put(key, frequency);
		}
		BigDecimal totalCharFrequency = new BigDecimal((double)totalNumOfCharsToCheckInText / totalNumOfCharsInText);
		totalCharFrequency = totalCharFrequency.setScale(2, RoundingMode.HALF_UP);
		return new Result(charsWordGroupingFrequency, totalCharFrequency);
	}
	
	private static class CharactersWordSizeGrouping {
		
		private Set<Character> charsToCheckInWord;
		private int wordSize;
		
		public CharactersWordSizeGrouping(Set<Character> charsToCheckInWord, int wordSize) {
			this.charsToCheckInWord = charsToCheckInWord;
			this.wordSize = wordSize;
		}

		public Set<Character> getCharsToCheckInWord() {
			return charsToCheckInWord;
		}

		public Integer getWordSize() {
			return wordSize;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((charsToCheckInWord == null) ? 0 : charsToCheckInWord.hashCode());
			result = prime * result + wordSize;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CharactersWordSizeGrouping other = (CharactersWordSizeGrouping) obj;
			if (charsToCheckInWord == null) {
				if (other.charsToCheckInWord != null)
					return false;
			} else if (!charsToCheckInWord.equals(other.charsToCheckInWord))
				return false;
			if (wordSize != other.wordSize)
				return false;
			return true;
		}
					
	}
	
	private static class Result {
		
		private Map<CharactersWordSizeGrouping, BigDecimal> charsWordGroupingFrequency;
		private BigDecimal totalCharFrequency;
		
		public Result(Map<CharactersWordSizeGrouping, BigDecimal> charsWordGroupingFrequency, BigDecimal totalCharFrequency) {
			this.charsWordGroupingFrequency = charsWordGroupingFrequency;
			this.totalCharFrequency = totalCharFrequency;
		}

		public Map<CharactersWordSizeGrouping, BigDecimal> getCharsWordGroupingFrequency() {
			return charsWordGroupingFrequency;
		}

		public BigDecimal getTotalCharFrequency() {
			return totalCharFrequency;
		}
				
	}

}
