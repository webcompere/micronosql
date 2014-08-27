package uk.org.webcompere.micronosql.engine;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

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
	
}
