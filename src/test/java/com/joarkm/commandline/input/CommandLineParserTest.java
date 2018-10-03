package com.joarkm.commandline.input;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

class CommandLineParserTest {
	
	/* Command line argument parser configured to parse options */
	private static CommandLineParser _parserOfOptions;
	/* Command line argument parser configured to parse flags */
	private static CommandLineParser _parserOfFlags;
	/* Command line argument parser configured to parse options and flags */
	private static CommandLineParser _parserOfOptionsAndFlags;
	
	private static List<String> testOptions;
	private static List<String> testFlags;
	
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		testOptions = Arrays.asList("-port", "-host", "-path");
		testFlags = Arrays.asList("-no-cache", "-testing");
		_parserOfOptionsAndFlags = new CommandLineParser(
				new String[] { "-port", "80", "-no-cache", "-path", "C:\file" },
				testOptions,
				testFlags);
		_parserOfFlags = new CommandLineParser(
				new String[] { "-no-cache", "-testing" },
				null,
				testFlags);
		_parserOfOptions = new CommandLineParser(
				new String[] { "-host", "my-pc.home", "-path", "C:\file" },
				testOptions);
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void testConstructorNoArgsNoOptsFails() {
		assertThrows(IllegalArgumentException.class, ()-> { new CommandLineParser(null, null); });
	}
	
	@Test
	void testConstructorNoArgsOptsFails() {
		assertThrows(IllegalArgumentException.class, ()-> { new CommandLineParser(null, testOptions); });
	}
	
	@Test
	void testConstructorArgsNoOptionsFails() {
		String[] args = new String[] { "-test", "-port", "80" };
		assertThrows(IllegalArgumentException.class, ()-> { new CommandLineParser(args, null); });
	}

	@Test
	void testParserOfOptions_GetOptions() {
		Map<String, String> options = _parserOfOptions.getOptions();
		assertNotNull(options);
		String[] failingOptions = options.keySet().stream()
				.filter(opt -> !testOptions.contains(opt))
				.toArray(String[]::new);
		
		assertFalse(failingOptions.length > 0,
				String.format("parser unable to detect the following registered"
						+ " options while parsing in-arguments: %s",
						Arrays.toString(failingOptions))
				);
	}
	
	@Test
	void testParserOfOptionsFlags_GetOptions() {
		
		Map<String, String> options = _parserOfOptionsAndFlags.getOptions();
		assertNotNull(options);
		
		String[] failingOptions = options.keySet().stream()
				.filter(opt -> !testOptions.contains(opt))
				.toArray(String[]::new);
		
		assertFalse(failingOptions.length > 0,
				String.format("parser unable to detect the following registered"
						+ " options while parsing in-arguments: %s",
						Arrays.toString(failingOptions))
				);
	}
	
	@Test
	void testParserOfOptionsAndFlags_GetFlags() {
		
		Set<String> flags = _parserOfOptionsAndFlags.getFlags();
		assertNotNull(flags);
		
		String[] failingFlags = flags.stream()
				.filter(opt -> !testFlags.contains(opt))
				.toArray(String[]::new);
		
		assertFalse(failingFlags.length > 0,
				String.format("parser unable to detect the following registered"
						+ " flags while parsing in-arguments: %s",
						Arrays.toString(failingFlags))
				);
	}
	
	@Test
	void testParserOfFlags_GetFlags() {
		Set<String> flags = _parserOfFlags.getFlags();
		assertNotNull(flags);
		
		String[] failingFlags = flags.stream()
				.filter(opt -> !testFlags.contains(opt))
				.toArray(String[]::new);
		
		assertFalse(failingFlags.length > 0,
				String.format("parser unable to detect the following registered"
						+ " flags while parsing in-arguments: %s",
						Arrays.toString(failingFlags))
				);
	}
	
	@Test
	void testParserOfOptionsAndFlags_HasValidFlags() {
		assertTrue(_parserOfOptionsAndFlags.hasValidFlags());
	}
	
	@Test
	void testParserOfFlags_HasValidFlags() {
		assertTrue(_parserOfFlags.hasValidFlags());
	}

	@Test
	void testParserOfOptionsAndFlags_HasValidOptions() {
		assertTrue(_parserOfOptionsAndFlags.hasValidOptions());
	}
	
	@Test
	void testParserOfOptions_HasValidOptions() {
		assertTrue(_parserOfOptions.hasValidOptions());
	}

	@Test
	void testParserOfOptions_HasFlags() {
		assertFalse(_parserOfOptions.hasFlags());
	}
	
	@Test
	void testParserOfOptionsAndFlags_HasFlags() {
		assertTrue(_parserOfOptionsAndFlags.hasFlags());
	}
	
	@Test
	void testParserOfFlags_HasFlags() {
		assertTrue(_parserOfFlags.hasFlags());
	}

	@Test
	void testParserOfOptions_HasOptions() {
		assertTrue(_parserOfOptions.hasOptions());
	}
	
	@Test
	void testParserOfOptionsAndFlags_HasOptions() {
		assertTrue(_parserOfOptionsAndFlags.hasOptions());
	}
	
	@Test
	void testParserOfFlags_HasOptions() {
		assertFalse(_parserOfFlags.hasOptions());
	}
}
