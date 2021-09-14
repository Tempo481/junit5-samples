package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

public class SpiedListTest {
		
	@Spy
	List<String> spiedList = new ArrayList<String>();

	@Test
	public void whenUseSpyAnnotation_thenSpyIsInjectedCorrectly() {
	    spiedList.add("one");
	    spiedList.add("two");

	    Mockito.verify(spiedList).add("one");
	    Mockito.verify(spiedList).add("two");

	    assertEquals(2, spiedList.size());

	    Mockito.doReturn(100).when(spiedList).size();
	    assertEquals(100, spiedList.size());
	}
	
}
