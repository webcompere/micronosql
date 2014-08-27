package uk.org.webcompere.micronosql.engine;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

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
	
}
