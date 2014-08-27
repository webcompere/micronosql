package uk.org.webcompere.micronosql.engine;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uk.org.webcompere.micronosql.mapreducesort.Predicate;
import uk.org.webcompere.micronosql.mapreducesort.StringAscending;
import uk.org.webcompere.micronosql.mapreducesort.StringDescending;
import uk.org.webcompere.micronosql.pojo.ExampleDocument;
import uk.org.webcompere.micronosql.storage.StorageManagerInMemory;

public class EngineTest {
	private Engine engine;
	
	@Before
	public void before() {
		engine = new EngineImpl(new StorageManagerInMemory());
	}
	
	@Test
	public void findsNoRecord() {
		ExampleDocument document = engine.find("A key", ExampleDocument.class);
		assertNull(document);
	}
	
	@Test
	public void findsARecordAfterStoringIt() {
		ExampleDocument doc1 = new ExampleDocument("Key", "Value");
		engine.store(doc1);
	
		ExampleDocument found = engine.find("Key", ExampleDocument.class);
		assertThat(found, is(doc1));
	}
	
	@Test
	public void findsARecordAfterStoringItAndCanModifyTransientCopyWithoutAffectingOriginal() {
		ExampleDocument original = new ExampleDocument("Key", "Value");
		engine.store(original);
	
		ExampleDocument found = engine.find("Key", ExampleDocument.class);
		assertThat(found, is(original));
		
		found.setValue("NewValue");
		
		assertThat(original.getValue(), is("Value"));
	}
	
	@Test
	public void canDeleteWhenNothingIsThere() {
		engine.delete("something", ExampleDocument.class);
		
		// no exception expected, nothing to assert - proving that the api doesn't mind
	}
	
	@Test
	public void canDeleteSomethingThatIsThere() {
		ExampleDocument original = new ExampleDocument("Key", "Value");
		engine.store(original);
		
		// should be there now
		assertNotNull(engine.find("Key", ExampleDocument.class));
		
		engine.delete("Key", ExampleDocument.class);
		
		// should not be there anymore
		assertNull(engine.find("Key", ExampleDocument.class));
	}
	
	@Test
	public void listsAllItemKeys() {
		engine.store(new ExampleDocument("Key1", "Value1"));
		engine.store(new ExampleDocument("Key2", "Value2"));
		
		Set<String> keys = engine.findAllKeys(ExampleDocument.class);
		
		assertThat(keys.size(), is(2));
		assertTrue(keys.contains("Key1"));
		assertTrue(keys.contains("Key2"));
	}
	
	@Test
	public void listAllItemsAsItems() {
		ExampleDocument ed1 = new ExampleDocument("Key1", "Value1");
		ExampleDocument ed2 = new ExampleDocument("Key2", "Value2");
		engine.store(ed1);
		engine.store(ed2);
		
		List<ExampleDocument> items = engine.findAll(ExampleDocument.class);
		
		assertThat(items.size(), is(2));
		ExampleDocument item1 = items.get(0);
		ExampleDocument item2 = items.get(1);
		
		// should have extracted both items
		assertTrue(item1.equals(ed1) || item1.equals(ed2));
		assertTrue(item2.equals(ed1) || item2.equals(ed2));
		assertFalse(item1.equals(item2));
	}
	
	@Test
	public void findKeysInOrder() {
		ExampleDocument ed1 = new ExampleDocument("Key1", "Value1");
		ExampleDocument ed2 = new ExampleDocument("Key2", "Value2");
		ExampleDocument ed3 = new ExampleDocument("Key3", "Value3");	
		ExampleDocument ed4 = new ExampleDocument("Key4", "Value4");	
		
		List<ExampleDocument> allExampleDocuments = engine.findAll(ExampleDocument.class);
		allExampleDocuments.addAll(Arrays.asList(ed3, ed1, ed4, ed2));
		
		List<String> keysInOrder = engine.findAllKeys(ExampleDocument.class, new StringAscending());
		assertThat(keysInOrder, contains("Key1", "Key2", "Key3", "Key4"));

		keysInOrder = engine.findAllKeys(ExampleDocument.class, new StringDescending());
		assertThat(keysInOrder, contains("Key4", "Key3", "Key2", "Key1"));

	}
	
	@Test
	public void retrieveItemsInKeyOrder() {
		ExampleDocument ed1 = new ExampleDocument("Key1", "Value1");
		ExampleDocument ed2 = new ExampleDocument("Key2", "Value2");
		ExampleDocument ed3 = new ExampleDocument("Key3", "Value3");	
		ExampleDocument ed4 = new ExampleDocument("Key4", "Value4");	
		
		List<ExampleDocument> allExampleDocuments = engine.findAll(ExampleDocument.class);
		allExampleDocuments.addAll(Arrays.asList(ed3, ed1, ed4, ed2));
		
		List<ExampleDocument> retrieved = engine.findAll(ExampleDocument.class, new StringAscending());
		
		// check the list is now in order
		assertThat(retrieved, contains(ed1, ed2, ed3, ed4));
	}
	
	@Test
	public void findByCriteria() {
		ExampleDocument ed1 = new ExampleDocument("Key1", "Value1");
		ExampleDocument ed2 = new ExampleDocument("Key2", "Value2");
		ExampleDocument ed3 = new ExampleDocument("Key3", "Value3");	
		ExampleDocument ed4 = new ExampleDocument("Key4", "Value4");
		
		engine.store(ed1);
		engine.store(ed2);
		engine.store(ed3);
		engine.store(ed4);
		
		// do a search which would return two items based on their values
		List<ExampleDocument> results = engine.find(ExampleDocument.class, new Predicate<ExampleDocument>() {

			@Override
			public boolean includes(ExampleDocument item) {
				return item.getValue().equals("Value2") || item.getValue().equals("Value4");
			}
			
		});

		// check the results
		assertThat(results, contains(ed2, ed4));
	}
	
}
