package uk.org.webcompere.micronosql.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.org.webcompere.micronosql.util.FileUtil;

public class FileSystemImpl implements FileSystem {

	@Override
	public void writeFile(String path, String contents) throws IOException {
		File file = new File(path);
		FileUtil.ensureDirectoryIsPresent(file.getParentFile());
		FileUtil.saveFile(file, contents);
		
	}

	@Override
	public void delete(String path) {
		try {
			FileUtil.deepDelete(new File(path));
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not delete " + path, e);
		}
		
	}

	@Override
	public String read(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		try {
			return FileUtil.readFile(file);
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not read " + path, e);
		}
	}

	@Override
	public List<String> listFiles(String path) {
		File file = new File(path);
		return FileUtil.listFiles(file);
	}

}
