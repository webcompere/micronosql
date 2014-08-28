package uk.org.webcompere.micronosql.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import uk.org.webcompere.micronosql.pojo.ExampleDocument;
import uk.org.webcompere.micronosql.storage.FileSystem;
import uk.org.webcompere.micronosql.storage.StorageManagerFileSystem;
import static org.mockito.Mockito.*;

/**
 * Tests for the storage manager to file system
 */
public class StorageManagerFileSystemTest {
	private FileSystem fileSystem;
	private StorageManagerFileSystem storageManager;
	private String rootDirectory = "/root/dir/";
	private String examplePojoDirectoryName = "uk.org.webcompere.micronosql.pojo.ExampleDocument";
	
	@Before
	public void before() {
		fileSystem = mock(FileSystem.class);
		storageManager = new StorageManagerFileSystem(rootDirectory, fileSystem);
	}
	
	@Test
	public void store() throws IOException {
		storageManager.store("somekey", "somedata", ExampleDocument.class);
		
		verify(fileSystem, times(1)).writeFile(rootDirectory + examplePojoDirectoryName + "/somekey", "somedata");
	}
	
	@Test
	public void filePathUsedIsTolerantOfNoTrailingSlashInRootDirectory() throws IOException {
		storageManager = new StorageManagerFileSystem("someroot", fileSystem);
		storageManager.store("somekey", "somedata", ExampleDocument.class);
		
		verify(fileSystem, times(1)).writeFile("someroot/" + examplePojoDirectoryName + "/somekey", "somedata");
	}
	
	@Test
	public void keyIsConvertedCorrectly() throws IOException {
		checkKeyConversions("a bit wrong", "a bit wrong",
				"some star*", "some star;2A",
				"@amazing", ";40amazing",
				"a;b", "a;3Bb");
	}
	
	/**
	 * @param values pairs of item - original key name, then expected file conversion
	 */
	private void checkKeyConversions(String ... values) throws IOException {
		for(int i=0; i<values.length; i+=2) {
			storageManager.store(values[i], "somedata", ExampleDocument.class);
		}

		for(int i=1; i<values.length; i+=2) {
			verify(fileSystem, times(1)).writeFile(rootDirectory + examplePojoDirectoryName + "/" + values[i], "somedata");
		}

	}
	
	@Test
	public void deleteKeyWillDeleteFile() {
		storageManager.delete("deleteMe", ExampleDocument.class);
		
		verify(fileSystem, times(1)).delete(rootDirectory + examplePojoDirectoryName + "/deleteMe");
	}

	@Test
	public void findAbsentKey() {
		when(fileSystem.read(rootDirectory + examplePojoDirectoryName + "/mykey")).thenReturn(null);
		
		assertNull(storageManager.find("mykey", ExampleDocument.class));
	}
	
	@Test
	public void findExistingKey() {
		when(fileSystem.read(rootDirectory + examplePojoDirectoryName + "/mykey")).thenReturn("myJson");
		
		assertThat(storageManager.find("mykey", ExampleDocument.class), is("myJson"));
	}
	
	@Test
	public void listsNoKeys() {
		when(fileSystem.listFiles(rootDirectory+examplePojoDirectoryName)).thenReturn(new ArrayList<String>());
		
		assertThat(storageManager.findAllKeys(ExampleDocument.class).size(), is(0));
	}
	
	@Test
	public void listsAKey() {
		when(fileSystem.listFiles(rootDirectory+examplePojoDirectoryName)).thenReturn(Arrays.asList("somekey"));
		
		assertThat(storageManager.findAllKeys(ExampleDocument.class), org.hamcrest.Matchers.contains("somekey"));
	}
	
	@Test
	public void conversionsFromFileNameBackToKey() {
		checkKeyReturnConversions(
				"a bit wrong", "a bit wrong",
				"some star*", "some star;2A",
				"@amazing", ";40amazing",
				"a;b", "a;3Bb");
	}
	
	/**
	 * @param values pairs of item - expected key name, then file name it should come from
	 */
	private void checkKeyReturnConversions(String ... values) {
		List<String> keyNames = new ArrayList<String>();
		for(int i=0; i<values.length; i+=2) {
			keyNames.add(values[i]);
		}

		List<String> fileNames = new ArrayList<String>();
		for(int i=1; i<values.length; i+=2) {
			fileNames.add(values[i]);
		}
		
		when(fileSystem.listFiles(rootDirectory+examplePojoDirectoryName)).thenReturn(fileNames);
		
		Set<String> keys = storageManager.findAllKeys(ExampleDocument.class);
		
		for(String key:keyNames) {
			assertTrue("Conversion of " + key, keys.contains(key));
		}
	}
}
