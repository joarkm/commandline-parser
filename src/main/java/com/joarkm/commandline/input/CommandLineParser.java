package com.joarkm.commandline.input;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

interface ICommandLineParser {
	boolean hasFlags();
	boolean hasValidFlags();
	boolean hasFlag(String flag);
	boolean hasOptions();
	boolean hasValidOptions();
	String getArgument(String option);
	Map<String, String> getOptions();
	Set<String> getFlags();
}

public class CommandLineParser implements ICommandLineParser {
	/* ---- Constants ---- */
	protected List<String> validOptions;
	protected List<String> validFlags;
	protected boolean optionsSupported;
	protected boolean flagsSupported;
	
	/* --- Properties ---- */
	protected List<String> args;
	protected Set<String> _flags;
	protected Map<String, String> _options; // A map of values {option, argument}
	protected Boolean _hasValidOptions; // null if not set, else true or false
	protected Boolean _hasValidFlags; // null if not set, else true or false
	private boolean _hasOptions;
	private boolean _hasFlags;
	
	/* --- Constructors ---- */
	private CommandLineParser(String[] args) {
		if (args == null || args.length == 0)
			throw new IllegalArgumentException("The arguments array cannot be empty!");
		this.args = Arrays.asList(args);
	}
	public CommandLineParser(String[] args, List<String> validOptions) {
		this(args);
		if (validOptions == null || validOptions.isEmpty())
			throw new IllegalArgumentException("validOptions cannot be null or empty");
		optionsSupported = true;
		this.validOptions = validOptions;
		readOptions();
	}
	public CommandLineParser(String[] args, List<String> validOptions, List<String> validFlags) {
		if (args == null || args.length == 0)
			throw new IllegalArgumentException("The arguments array cannot be empty!");
		this.args = Arrays.asList(args);
		if (validFlags == null || validFlags.isEmpty())
			throw new IllegalArgumentException("validFlags cannot be null or empty");
		flagsSupported = true;
		this.validFlags = validFlags;
		if (validOptions != null)
		{
			optionsSupported = true;
			this.validOptions = validOptions;
			readOptions();			
		}
		// Read flags depend on the valid options array, and must be run after that is created.
		readFlags();
	}
	
	private void readFlags()
	{
		// Extract the flags
		Set<String> flags = new LinkedHashSet<>();
		ListIterator<String> it = args.listIterator();
		while (it.hasNext()) {
			String arg = it.next();
			if (arg.startsWith("-") && validFlags.contains(arg))
				flags.add(arg);
			else
			{
				if (optionsSupported && validOptions.contains(arg))
				{
					// Read next argument and check if it is a flag
					String nextItem = it.hasNext() ? it.next() : null;
					if (nextItem != null && !nextItem.startsWith("-"))
						// We found an option and an argument
						continue;
				}
				_hasValidFlags = false;
				return;
			}
				
		}
		if (!flags.isEmpty())
		{
			_hasFlags = true;
			_flags = flags;
		}
	}
	
	private void readOptions()
	{
		if (_options != null)
			return;
		
		Map<String, String> options = new HashMap<String, String>();
		ListIterator<String> it = args.listIterator();
		// Extract the options
		while(it.hasNext())
		{
			String option = it.next();
			// If option is a valid option
			if (isValidOption(option))
			{
				// Check if argument is present
				if (it.hasNext())
				{
					String argument = it.next();
					/* Add this option and the corresponding argument */
					// Check if next is an argument
					if (argument.startsWith("-"))
					{
						// Option without an argument found. Stop search.
						_hasValidOptions = false;
						return;
					}
					// else add the {option, argument} pair
					options.put(option, argument);
				} else {
					options.put(option, null);
				}
			}
			// If the parsed value is neither a valid option nor a flag
			else if (option.startsWith("-") 
					&& flagsSupported ? !validFlags.contains(option) : true
					&& !isIgnored(option)
					)
			{
				// Stop search.
				_hasValidOptions = false;
				return;
			}
		}
		if (!options.isEmpty())
		{
			_hasOptions = true;
			_options = options;
		}
	}
	
	public Set<String> getFlags()
	{
		if (_flags != null && !_flags.isEmpty())
			return _flags;
		
		// If flags has not yet been validated
		if (_hasValidFlags == null)
			_hasValidFlags = hasValidFlags();
		return _hasValidFlags ? _flags : null;
	}
	
	public Map<String, String> getOptions()
	{
		if (_options != null && !_options.isEmpty())
			return _options;
		
		// If options has not yet been validated
		if (_hasValidOptions == null)
			_hasValidOptions = hasValidOptions();
		
		return _hasValidOptions ? _options : null;
	}
	
	
	public boolean hasValidFlags()
	{
		if (_hasValidFlags != null)
			return _hasValidFlags;
		
		if (!flagsSupported)
			throw new UnsupportedOperationException("This commandline parser does not support flags!");
		
		// If any items not contained in the valid flags array are found
		boolean flagsValid = !(_flags.stream().anyMatch(flag -> !validFlags.contains(flag)));
		return flagsValid;
	}

	@Override
	public boolean hasFlag(String flag) {
		return _flags.contains(flag);
	}

	public boolean hasValidOptions()
	{
		if (_hasValidOptions != null)
			return _hasValidOptions;
		
		if (!optionsSupported)
			throw new UnsupportedOperationException("This commandline parser does not support options!");
		
		// If any items not contained in the valid options array are found
		boolean optionsValid = !(_options.keySet().stream().anyMatch(opt -> !validOptions.contains(opt)));
		return optionsValid;
	}

	@Override
	public String getArgument(String option) {
		return _options.get(option);
	}

	public boolean hasFlags()
	{
		return _hasFlags;
	}
	
	public boolean hasOptions() {
		return _hasOptions;
	}
	
	private boolean isValidOption(String option)
	{
		return validOptions.contains(option);
	}
	
	// This method can be overridden by sub-classes to add flags/options that should be ignored
	protected boolean isIgnored(String option)
	{
		return false;
	}
}


