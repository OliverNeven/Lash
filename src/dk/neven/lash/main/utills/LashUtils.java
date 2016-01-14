package dk.neven.lash.main.utills;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <pre>
 * An utilities library for a large variant of tasks.
 * 
 * E-mail: oliver@neven.dk
 * Snail-mail: Denmark, Hillerød 3400, Helsingørsgade 2, 1-4.
 * 
 * Copyright (C) 2016 Oliver Stochholm Neven
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * </pre>
 */
public class LashUtils {
	
	/**
	 * Reads and returns every character in the specified file, except line breaks.
	 * 
	 * @param file the file to read
	 * @param fix if, and only if, true, then change whitespace characters (tabs, etc) to spaces and add newline and end of file tags 
	 * @return A char list of all the read characters
	 */
	public static List<Character> readFileCharacters(File file, boolean fix) throws FileNotFoundException {
		
		// Setup file reader
		final InputStream input_stream = new FileInputStream(file);
		final BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(input_stream));
		
		// Declare list to return
		final List<Character> character_list = new ArrayList<>();
		
		// Read file line-by-line, break the line into characters and add them to the list
		String line;
		try {
			while((line = buffered_reader.readLine()) != null) {
				
				if (fix) line = line.replace('\t', ' ');
				
				for (char c : line.toCharArray())
					character_list.add(c);
				// TODO add newline and end of file tags
			}
		} catch (IOException e) {
			err("Failed to read a line", e, true);
		}
		
		return character_list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Prints a message to the screen.
	 * @param message the message to print
	 * @param nl if, and only if, true, then append a new line character to the end of the message
	 */
	public static void out(String message, boolean nl) {
		if (nl)
			new StringBuilder(message).append('\n');
		System.out.format("%s", message);
	}
	
	/**
	 * Prints an exception stack trace to the screen.
	 * @param e the exception to print
	 */
	public static void err(Exception e) {
		e.printStackTrace();
	}
	
	/**
	 * Prints an error message to the screen.
	 * @param message the error message to print
	 * @param nl if, and only if, true, then append a new line character to the end of the error message
	 */
	public static void err(String message, boolean nl) {
		out(String.format("Error: %s!", message), nl);
	}
	
	/**
	 * Prints an error message and an exception to the screen.
	 * @param message the error message to print
	 * @param e the exception to print
	 * @param nl if, and only if, true, then append a new line character to the end of the error message
	 */
	public static void err(String message, Exception e, boolean nl) {
		err(message, true);
		err(e);
		if (nl)
			System.out.println();
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
