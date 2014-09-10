package uk.org.webcompere.micronosql.engine;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.org.webcompere.micronosql.mapreducesort.StringAscending;
import uk.org.webcompere.micronosql.pojo.ExampleDocumentWithKeyGeneration;
import uk.org.webcompere.micronosql.storage.StorageManagerFileSystem;
import uk.org.webcompere.micronosql.util.FileUtil;

public class EngineFilePerformanceTest {
	private Engine engine;
	private Random random;
	
	@Before
	public void before() {
		engine = new EngineImpl(new StorageManagerFileSystem("tempTestDbFileSystem/"));
		random = new Random(1234);
	}
	
	@After
	public void after() throws IOException {
		FileUtil.deepDelete(new File("tempTestDbFileSystem/"));
	}
	
	@Test
	public void storeLotsOfRecords() {
		storeLots();
	}

	private void storeLots() {
		for(int i=0; i<100; i++) {
			engine.store(newRandomRecord());
		}
	}
	
	@Test
	public void storeLotsOfRecordsAndSortByGuid() {
		storeLots();
		for(ExampleDocumentWithKeyGeneration item:engine.findAll(ExampleDocumentWithKeyGeneration.class, new StringAscending())) {
			// causes the item to be loaded
			assertNotNull(item);
		}
	}
	
	@Test
	public void storeLotsOfRecordsAndDoTenIterationsThroughAll() {
		storeLots();
		List<ExampleDocumentWithKeyGeneration> all = engine.findAll(ExampleDocumentWithKeyGeneration.class);
		
		for(int i=0; i<10; i++) {
			for(ExampleDocumentWithKeyGeneration item:all) {
				// causes the item to be loaded
				assertNotNull(item);
			}
		}
	}
	
	private ExampleDocumentWithKeyGeneration newRandomRecord() {
		ExampleDocumentWithKeyGeneration doc = new ExampleDocumentWithKeyGeneration();
		doc.setName(randomString());
		return doc;
	}

	private String randomString() {
		StringBuilder builder = new StringBuilder();
		int size = random.nextInt(10);
		for(int i=0; i<size; i++) {
			builder.append((char)'a' + random.nextInt(25));
		}
		return builder.toString();
	}
}
