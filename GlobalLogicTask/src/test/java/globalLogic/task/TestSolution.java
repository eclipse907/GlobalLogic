package globalLogic.task;

import static org.junit.jupiter.api.Assertions.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class TestSolution {

	@Test
	void testExcludedCharacters() {
		IExcludedCharacters excludedCharacters = new ExcludedCharacters(
				" !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~);".toCharArray()
				);
		assertTrue(excludedCharacters.isCharacterExcluded('\\'));
		assertTrue(excludedCharacters.isCharacterExcluded('\"'));
		assertTrue(excludedCharacters.isCharacterExcluded(' '));
		assertTrue(excludedCharacters.isCharacterExcluded('<'));
		assertTrue(excludedCharacters.isCharacterExcluded(','));
		assertFalse(excludedCharacters.isCharacterExcluded('a'));
		assertFalse(excludedCharacters.isCharacterExcluded('G'));
		assertFalse(excludedCharacters.isCharacterExcluded('k'));
		assertFalse(excludedCharacters.isCharacterExcluded('C'));
		assertFalse(excludedCharacters.isCharacterExcluded('V'));
		assertThrows(NullPointerException.class, ()-> {new ExcludedCharacters(null);});
		assertThrows(UnsupportedOperationException.class, ()-> {excludedCharacters.getExcludedCharacters().add('d');});
	}
	
	@Test
	void testLogicWordCharacterSet() {
		IWordCharacterSet characterSet = new LogicWordCharacterSet("Logic");
		assertTrue(characterSet.isCharacterInWord('L'));
		assertTrue(characterSet.isCharacterInWord('l'));
		assertTrue(characterSet.isCharacterInWord('o'));
		assertTrue(characterSet.isCharacterInWord('G'));
		assertTrue(characterSet.isCharacterInWord('i'));
		assertTrue(characterSet.isCharacterInWord('C'));
		assertFalse(characterSet.isCharacterInWord('a'));
		assertFalse(characterSet.isCharacterInWord('D'));
		assertFalse(characterSet.isCharacterInWord('y'));
		assertFalse(characterSet.isCharacterInWord('W'));
		assertThrows(NullPointerException.class, ()-> {new LogicWordCharacterSet(null);});
		assertThrows(UnsupportedOperationException.class, ()-> {characterSet.getWordCharacters().add('a');});
		assertTrue(characterSet.getWordCharacterComparator().compare('L', 'g') < 0);
		assertTrue(characterSet.getWordCharacterComparator().compare('c', 'O') > 0);
		assertTrue(characterSet.getWordCharacterComparator().compare('I', 'I') == 0);
		assertTrue(characterSet.getWordCharacterComparator().compare('O', 'I') < 0);
		assertThrows(IllegalArgumentException.class, ()-> {
			characterSet.getWordCharacterComparator().compare('a', 'G');
		});
	}
	
	@Test
	void testCheckCharacterFrequencyInText1() {
		IWordCharacterSet wordCharSet = new LogicWordCharacterSet("Logic");
		IExcludedCharacters excludedChars = new ExcludedCharacters(
				" !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~);".toCharArray()
				);
		ICheckCharacterFrequencyInText characterFrequencyInText = new CheckCharacterFrequencyInText(wordCharSet, excludedChars);
		try (BufferedReader br1 = Files.newBufferedReader(Paths.get("src\\test\\java\\globalLogic\\task\\Input1.txt"));
			 BufferedWriter bw1 =  Files.newBufferedWriter(Paths.get("src\\test\\java\\globalLogic\\task\\Output1.txt"));
			 BufferedReader br2 = Files.newBufferedReader(Paths.get("src\\test\\java\\globalLogic\\task\\Input2.txt"));
			 BufferedWriter bw2 =  Files.newBufferedWriter(Paths.get("src\\test\\java\\globalLogic\\task\\Output2.txt"))
			) {
			characterFrequencyInText.displayCharacterFrequencyInText(br1, bw1);
			characterFrequencyInText.displayCharacterFrequencyInText(br2, bw2);
		} catch (IOException ex) {
				System.out.println("Error while reading or writing data.");
		}
	}
	
	@Test
	void testCheckCharacterFrequencyInText2() {
		IWordCharacterSet wordCharSet = new LogicWordCharacterSet("Logic");
		IExcludedCharacters excludedChars = new ExcludedCharacters(
				" !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~);".toCharArray()
				);
		ICheckCharacterFrequencyInText characterFrequencyInText = new CheckCharacterFrequencyInText(wordCharSet, excludedChars);
		String text1 = "I love to work in global logic!";
		String text2 = "I love to work in global logic! The plate is on the level's.\n" +
				       "Logic is awesome@[].";
		try (BufferedReader br1 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text1.getBytes())));
			 BufferedReader br2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text2.getBytes())));
			 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))
			) {
			characterFrequencyInText.displayCharacterFrequencyInText(br1, bw);
			characterFrequencyInText.displayCharacterFrequencyInText(br2, bw);
		} catch (IOException ex) {
			System.out.println("Error while reading or writing data.");
		}
	}

}
