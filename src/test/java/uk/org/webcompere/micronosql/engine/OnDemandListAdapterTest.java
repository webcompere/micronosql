package uk.org.webcompere.micronosql.engine;

import java.util.Arrays;
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
	private ListWithKeys<ExampleDocument> list;
	
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
	
	@Test
	public void containsAll() {
		assertTrue(list.containsAll(Arrays.asList(doc1, doc2, doc3)));
		assertFalse(list.containsAll(Arrays.asList(doc1, new ExampleDocument("a", "b"))));
	}
	
	@Test
	public void retainAll() {
		// drop doc2
		list.retainAll(Arrays.asList(doc1, doc3));
		assertThat(list.size(), is(2));
	}
	
	@Test
	public void toArray() {
		ExampleDocument[] array = list.toArray(new ExampleDocument[0]);
		checkSameAnyOrder(Arrays.asList(array), doc1, doc2, doc3);
	}
	
	@Test
	public void subListAndArray() {
		Object[] array = list.toArray();
		
		List<ExampleDocument> subList = list.subList(1, 3);
		checkSameAnyOrder(subList, (ExampleDocument)array[1], (ExampleDocument)array[2]);
	}
	
	@Test
	public void addingToListGivesItemAtEnd() {
		ExampleDocument doc4 = new ExampleDocument("New thing", "Newy");
		
		list.add(doc4);
		assertThat(list.size(), is(4));
		assertThat(list.get(3), is(doc4));
	}

	@Test
	public void addingToListCausesPersistence() {
		// no "new thing" in the db
		assertNull(engine.find("New thing", ExampleDocument.class));
		
		ExampleDocument doc4 = new ExampleDocument("New thing", "Newy");
		
		list.add(doc4);
		
		assertThat(engine.find("New thing", ExampleDocument.class), is(doc4));
	}
	
	@Test
	public void clearReallyClearsTheDb() {
		assertNotNull(engine.find("Key1", ExampleDocument.class));
		
		list.clear();
		
		assertNull(engine.find("Key1", ExampleDocument.class));
		assertThat(engine.findAllKeys(ExampleDocument.class).size(), is(0));
	}
	
	@Test
	public void isEmpty() {
		assertFalse(list.isEmpty());
		list.clear();
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void insertItem() {
		ExampleDocument myDoc = new ExampleDocument("My", "Doc");
		list.add(1, myDoc);
		
		// item should be in the right place
		assertThat(list.get(1), is(myDoc));
		
		// item should be in the db
		assertNotNull(engine.find("My", ExampleDocument.class));
	}
	
	@Test
	public void indexOf() {
		assertThat(list.indexOf(doc1), is(not(-1)));
		assertThat(list.lastIndexOf(doc1), is(not(-1)));
		
		ExampleDocument notThere = new ExampleDocument("Not", "There");
		assertThat(list.indexOf(notThere), is(-1));
		assertThat(list.lastIndexOf(notThere), is(-1));
	}
	
	@Test
	public void addAll() {
		list.clear();
		assertThat(list.size(), is(0));
		
		list.addAll(Arrays.asList(doc1, doc2, doc3));
		checkSameAnyOrder(list, doc1, doc2, doc3);
	}
	
	@Test
	public void addAllAtIndex() {
		list.clear();
		list.add(doc1);
		
		list.addAll(0, Arrays.asList(doc2, doc3));
		checkSameAnyOrder(list, doc1, doc2, doc3);
	}
	
	@Test
	public void removeAll() {
		list.removeAll(Arrays.asList(doc3, doc1));
		checkSameAnyOrder(list, doc2);
	}
	
	@Test
	public void setReplacesTheGivenItemInTheDb() {
		ExampleDocument existingItem = list.get(1);
		ExampleDocument newItem = new ExampleDocument("New", "Item");
		assertNotNull(engine.find(existingItem.getKey(), ExampleDocument.class));
		assertNull(engine.find("New", ExampleDocument.class));

		list.set(1, newItem);
		
		assertNull(engine.find(existingItem.getKey(), ExampleDocument.class));
		assertNotNull(engine.find("New", ExampleDocument.class));
	}

	@Test
	public void keysMatch() {
		for(int i=0; i<list.size(); i++) {
			assertThat(list.getKey(i), is(list.get(i).getKey()));
		}
	}
	
	private void checkSameAnyOrder(List<ExampleDocument> actual, ExampleDocument ... expected) {
		assertThat(actual.size(), is(expected.length));
		for(ExampleDocument d:expected) {
			assertTrue(actual.contains(d));
		}
	}
}
