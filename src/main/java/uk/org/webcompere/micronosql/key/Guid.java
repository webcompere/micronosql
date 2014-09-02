package uk.org.webcompere.micronosql.key;

import java.util.UUID;

import uk.org.webcompere.micronosql.annotation.KeyGenerator;

public class Guid implements KeyGenerator {

	@Override
	public Object generate() {
		return UUID.randomUUID().toString();
	}

}
