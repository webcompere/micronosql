package uk.org.webcompere.micronosql.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper util for managing files
 */
public class FileUtil {
	/**
	 * Delete file or directories recursively - modified from http://stackoverflow.com/a/779529/1355930
	 * @param f file or directory to recursively delete
	 */
	public static void deepDelete(File f) throws IOException {
		// no need to delete a thing that doesn't exist
		if (!f.exists()) {
			return;
		}
		
		if (f.isDirectory()) {
			for (File c : f.listFiles()) {
				deepDelete(c);
			}
		}
		if (!f.delete()) {
			throw new FileNotFoundException("Failed to delete file: " + f);
		}
	}
	
	/**
	 * Create directory if necessary
	 * @param directory to create
	 */
	public static void ensureDirectoryIsPresent(File directory) {
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}
	
	/**
	 * Write string to file
	 * @param file target
	 * @param contents contents to write
	 * @throws IOException on file errors
	 */
	public static void saveFile(File file, String contents) throws IOException {
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(contents);
		}
	}
	
	/**
	 * Read the contents of the file into a string
	 * @param file source
	 * @return contents of the file
	 * @throws FileNotFoundException if the file doesn't exist
	 * @throws IOException if there's a problem reading the file
	 */
	public static String readFile(File file) throws FileNotFoundException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			StringBuilder data = new StringBuilder();
			while(reader.ready()) {
				data.append(reader.readLine()).append("\n");
			}
			return data.toString();
		}
	}

	/**
	 * List all files in a directory
	 * @param file directory
	 * @return list of files - empty if directory doesn't exist
	 */
	public static List<String> listFiles(File file) {
		List<String> files = new ArrayList<String>();
		if (file.exists()) {
			for (File item : file.listFiles()) {
				if (item.isFile()) {
					files.add(item.getName());
				}
			}
		}
		
		return files;

	}
	
}
