package dk.neven.lash.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import dk.neven.lash.main.utills.LashUtils;

/**
 * <pre>
 *
 * The main launch program for executing .lash files. 
 * To execute one or more files, parse them as launch arguments for this class.
 * If no files were specified, the code_sample0.lash file, in the sample folder, will be run.
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
 * 
 * </pre>
 */
public class Lash {
	
	/**
	 * Lexes and parses the given file(s).
	 * 
	 * @param run_files the files to lex and parse
	 */
	public void launch(File[] run_files) {
		for (File file : run_files) {
			
			List<Character> character_list;
			try {
				character_list = LashUtils.readFileCharacters(file, true);
			} catch (FileNotFoundException e) {
				LashUtils.err(String.format("Failed to locate the %s file", file), true);
				continue;
			}
			
			System.out.println(character_list);
			
			
			
			
		}
	}
	
	
	
	
	
	
	
	

	
	/** 
	 * Creates a new instance of Lash and runs the launch method with the given arguments as files or, if none were given, the sample\code_sample0.lash file.
	 * @param args the pathnames for the files to be executed
	 */
	public static void main(String[] args) {
		
		// Declare the files to run
		File[] run_files;
		
		// Get files
		if (args.length <= 0)
			run_files = new File[] {new File("sample\\code_sample0.lash")};
		else {
			run_files = new File[args.length - 1];
			for (int i = 0; i < run_files.length; i ++)
				run_files[i] = new File(args[i]);
		}
		
		// Launch files
		new Lash().launch(run_files);
		System.exit(0);
	}
}
