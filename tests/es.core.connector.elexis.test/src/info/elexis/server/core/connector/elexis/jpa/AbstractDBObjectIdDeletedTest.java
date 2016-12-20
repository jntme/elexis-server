package info.elexis.server.core.connector.elexis.jpa;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import info.elexis.server.core.connector.elexis.jpa.model.annotated.Fall;
import info.elexis.server.core.connector.elexis.jpa.model.annotated.Kontakt;

public class AbstractDBObjectIdDeletedTest {

	@Test
	public void testOverrideEqualsAndHashCode() {
		Kontakt k = new Kontakt();
		k.setId("testId");
		k.setLastupdate(BigInteger.valueOf(1));
		assertEquals(k, k);
		Kontakt k2 = new Kontakt();
		k2.setId("testId");
		k2.setLastupdate(BigInteger.valueOf(1));
		Kontakt k3 = new Kontakt();
		k3.setId("testId");
		k3.setLastupdate(BigInteger.valueOf(1));
		assertTrue(k.equals(k2));
		assertTrue(k2.equals(k));
		assertTrue(k.equals(k3));
		assertTrue(k2.equals(k3));
		assertEquals(k.hashCode(), k2.hashCode());
		assertEquals(k.hashCode(), k3.hashCode());

		Kontakt k4 = new Kontakt();
		k4.setId("testId2");
		k4.setLastupdate(BigInteger.valueOf(2));
		Kontakt k5 = new Kontakt();
		k5.setId("testId");
		k5.setLastupdate(null);
		assertEquals(k5, k5);
		Fall f = new Fall();
		f.setId("testId");
		f.setLastupdate(BigInteger.valueOf(1));
		assertFalse(k.equals(f));
		assertFalse(f.equals(k));
		assertFalse(k.equals(null));
		assertFalse(k4.equals(k));
		assertFalse(k.equals(k4));
		assertFalse(k.equals(k5));
		assertFalse(k5.equals(k));
		assertNotEquals(k.hashCode(), f.hashCode());
		assertNotEquals(k4.hashCode(), k.hashCode());
		assertNotEquals(k5.hashCode(), k.hashCode());
	}

}
