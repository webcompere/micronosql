package uk.org.webcompere.micronosql.engine;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import uk.org.webcompere.micronosql.pojo.ExampleDocument;
import uk.org.webcompere.micronosql.pojo.OtherExample;

public class TypeWrapperTest {
	@Test
	public void canReadTheKey() {
		TypeWrapper wrapper = new TypeWrapper(ExampleDocument.class);
		ExampleDocument doc1 = new ExampleDocument("Clef", "le value");
		assertThat(wrapper.getKey(doc1), is("Clef"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void wontReadKeyIfWrongClass() {
		TypeWrapper wrapper = new TypeWrapper(ExampleDocument.class);
		OtherExample example = new OtherExample();
		wrapper.getKey(example);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void cantReadKeyIfNoKey() {
		TypeWrapper wrapper = new TypeWrapper(OtherExample.class);
		wrapper.getKey(new OtherExample());
	}
}
