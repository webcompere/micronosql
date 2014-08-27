package uk.org.webcompere.micronosql.engine;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import uk.org.webcompere.micronosql.pojo.ExampleDocument;
import uk.org.webcompere.micronosql.storage.StorageManagerInMemory;

public class OnDemandListAdapterTest {
	private Engine engine;
	private ExampleDocument doc1;
	private ExampleDocument doc2;
	private ExampleDocument doc3;
	private List<ExampleDocument> list;
	
	@Before
	public void before() {
		engine = new EngineImpl(new StorageManagerInMemory());
		doc1 = new ExampleDocument("Key1", "Val1");
		doc2 = new ExampleDocument("Key2", "Val2");
		doc3 = new ExampleDocument("Key3", "Val3");
		engine.store(doc1);
		engine.store(doc2);
		engine.store(doc3);
		list = engine.findAll(ExampleDocument.class);
	}
	
	@Test
	public void hasCorrectSize() {
		assertThat(list.size(), is(3));
	}
	
	@Test
	public void doesntContainWrongItem() {
		ExampleDocument notInThere = new ExampleDocument("Me", "You");
		assertThat(list.indexOf(notInThere), is(-1));
		assertFalse(list.contains(notInThere));
	}
	
	@Test
	public void containsRightItems() {
		assertTrue(list.contains(doc1));
		assertTrue(list.contains(doc2));
		assertTrue(list.contains(doc3));
	}
	
}
