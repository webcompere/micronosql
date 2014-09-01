package uk.org.webcompere.micronosql.storage;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import uk.org.webcompere.micronosql.codec.Codec;

/**
 * Implementation of the storage manager against the filesystem
 */
public class StorageManagerFileSystem implements StorageManager {
	private FileSystem fileSystem;
	private String rootDirectory;
	
	public StorageManagerFileSystem(String rootDirectory) {
		this(rootDirectory, new FileSystemImpl());
	}
	
	public StorageManagerFileSystem(String rootDirectory, FileSystem fileSystem) {
		this.rootDirectory = rootDirectory;
		this.fileSystem = fileSystem;
		
		if (!rootDirectory.endsWith("/")) {
			this.rootDirectory = rootDirectory + "/";
		}
	}
	
	
	@Override
	public <T> void store(String key, ItemTransfer<T> payload, Class<T> type) {
		String filename = fileNameFrom(key, type);
		try {
			fileSystem.writeFile(filename, payload.encodeToString());
		} catch (IOException e) {
			// ignore
		}
	}


	private <T> String fileNameFrom(String key, Class<T> type) {
		return directoryFrom(type) + "/" + convertKeyToFilename(key);
	}


	private <T> String directoryFrom(Class<T> type) {
		return rootDirectory + type.getCanonicalName();
	}

	private String convertKeyToFilename(String key) {
		@SuppressWarnings("deprecation")
		String urlEncoded = URLEncoder.encode(key);
		return urlEncoded
				.replaceAll("[\\+]", " ")   // fix URL using + instead of space
				.replaceAll("[\\*]", "%2A") // * is not valid in filenames
				.replaceAll("[%]",";");     // % character is not valid, so replace with ; which is
	}
	
	@SuppressWarnings("deprecation")
	private String convertFileNameToKey(String filename) {
		String toDecode = filename
				.replaceAll("[;]","%")
				.replaceAll("%2A", "*");
		return URLDecoder.decode(toDecode);
	}


	@Override
	public <T> ItemTransfer<T> find(String key, Class<T> type, Codec codec) {
		String content = fileSystem.read(fileNameFrom(key, type));
		return ItemTransfer.fromString(type, codec, content);
	}


	@Override
	public <T> Set<String> findAllKeys(Class<T> type) {
		Set<String> allKeys = new HashSet<String>();
		for(String fileName:fileSystem.listFiles(directoryFrom(type))) {
			allKeys.add(convertFileNameToKey(fileName));
		}
		return allKeys;
	}

	@Override
	public <T> void delete(String key, Class<T> type) {
		fileSystem.delete(fileNameFrom(key, type));
	}
}
