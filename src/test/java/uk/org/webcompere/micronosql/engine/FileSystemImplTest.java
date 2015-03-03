package uk.org.webcompere.micronosql.engine;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import uk.org.webcompere.micronosql.storage.FileSystem;
import uk.org.webcompere.micronosql.storage.FileSystemImpl;
import uk.org.webcompere.micronosql.util.FileUtil;

/**
 * Tests for the filesystem
 */
public class FileSystemImplTest {
	private FileSystem fileSystem = new FileSystemImpl();
	private static final String root = "fileSystemImplTest/";
	
	@Before
	public void clearUp() throws IOException {
		FileUtil.deepDelete(new File(root));
	}
	
	@Test
	public void saveAndRetrieve() throws IOException {
		String data = "Hello world!\nHello again\n";
		
		fileSystem.writeFile(root+"test", data);
		
		String readData = fileSystem.read(root+"test");
		
		assertThat(readData, is(data));
	}
	
	
	@Test
	public void saveAndRetrieveInSubDir() throws IOException {
		String data = "Hello world!\nHello again\n";
		
		fileSystem.writeFile(root+"/subdir/subdir1/test", data);
		
		String readData = fileSystem.read(root+"/subdir/subdir1/test");
		
		assertThat(readData, is(data));

		
	}
	
	@Test
	public void saveAndDelete() throws IOException {
		String filePath = root+"stuff";
		assertNull(fileSystem.read(filePath));
		
		fileSystem.writeFile(filePath, "Some contents");
		
		assertNotNull(fileSystem.read(filePath));
		
		fileSystem.delete(filePath);
		
		assertNull(fileSystem.read(filePath));
	}

	@Test
	public void listsFiles() throws Exception {
		fileSystem.writeFile(root+"file1", "");
		fileSystem.writeFile(root+"file2", "");
		fileSystem.writeFile(root+"file3", "");
		fileSystem.writeFile(root+"file4", "");
		
		assertThat(fileSystem.listFiles(root), containsInAnyOrder("file1", "file2", "file3", "file4"));
	}
	
	@Test
	public void listsFilesWhenNoneThere() throws Exception {
		assertThat(fileSystem.listFiles(root+"nothing").size(), is(0));
	}
	
	@Test
	public void canOverwrite() throws IOException {
		String data = "Hello world!\nHello again\n";
		String data2 = "Goodbye world!\nGoodbye again\n";		

		fileSystem.writeFile(root+"test", data);
		fileSystem.writeFile(root+"test", data2);
		
		String readData = fileSystem.read(root+"test");
		
		assertThat(readData, is(data2));		
	}
	
}
