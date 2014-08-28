package uk.org.webcompere.micronosql.storage;

import java.io.IOException;
import java.util.List;

/**
 * Abstraction of a file system for the storage manager to use
 */
public interface FileSystem {
	/**
	 * Write a file to the given path containing the given string
	 * @param path to write
	 * @param contents the string to write into the file
	 * @throws IOException on error
	 */
	void writeFile(String path, String contents) throws IOException;

	/**
	 * Delete the given file from the file system
	 * @param path to delete
	 */
	void delete(String path);

	/**
	 * Return the contents of this file as a String
	 * @param path file to read
	 * @return String or null if no file present
	 */
	String read(String path);

	/**
	 * Find the list of filenames in the given path
	 * @param path directory to read
	 * @return filenames of files in the directory
	 */
	List<String> listFiles(String path);
}
