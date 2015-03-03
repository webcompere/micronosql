package uk.org.webcompere.micronosql.engine;


import java.util.HashSet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import uk.org.webcompere.micronosql.codec.Codec;
import uk.org.webcompere.micronosql.index.KeyIndex;
import uk.org.webcompere.micronosql.pojo.ExampleDocument;
import uk.org.webcompere.micronosql.pojo.IndexedEntity;
import uk.org.webcompere.micronosql.storage.ItemTransfer;
import uk.org.webcompere.micronosql.storage.StorageManager;

/**
 * Tests for indexing
 */
public class EngineIndexTest {
	private EngineImpl engine;
	private StorageManager storageManagerMock;
	
	@Before
	public void before() {
		storageManagerMock = mock(StorageManager.class);
		engine = new EngineImpl(storageManagerMock);
	}
	
	@Test
	public void readingFromNonIndexedEntityHasIndexHit() {
		when(storageManagerMock.findAllKeys(ExampleDocument.class)).thenReturn(new HashSet<String>());
		
		assertThat(engine.findAllKeys(ExampleDocument.class).size(), is(0));
	}
	
	@Ignore("Not implemented")
	@SuppressWarnings("rawtypes")
	@Test
	public void readingFromIndexedEntityHasNoIndexHitButIndexRetrieve() {
		when(storageManagerMock.find(IndexedEntity.class.getCanonicalName(), KeyIndex.class, engine.getCodec()))
			.thenReturn(new ItemTransfer<KeyIndex>(engine.getCodec(), new KeyIndex<IndexedEntity>(IndexedEntity.class)));
		
		assertThat(engine.findAllKeys(IndexedEntity.class).size(), is(0));

		// should not have touched find all keys
		verify(storageManagerMock, times(0)).findAllKeys(IndexedEntity.class);
	}
	
	@Ignore("Not implemented")
	@Test
	public void readingFromIndexedEntityHasNoIndexHitButIndexRetrieveAllItems() {
		KeyIndex<IndexedEntity> keyIndex = new KeyIndex<IndexedEntity>(IndexedEntity.class);
		keyIndex.getKeys().add("item1");
		
		when(storageManagerMock.find(IndexedEntity.class.getCanonicalName(), KeyIndex.class, engine.getCodec()))
			.thenReturn(new ItemTransfer<KeyIndex>(engine.getCodec(), keyIndex));
		
		assertThat(engine.findAll(IndexedEntity.class).size(), is(1));
		assertThat(engine.findAll(IndexedEntity.class).getKey(0), is("item1"));
		
		// should not have touched find all keys
		verify(storageManagerMock, times(0)).findAllKeys(IndexedEntity.class);
	}
}
