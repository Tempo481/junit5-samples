package com.example.project;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

public class ListTest {
	
	LinkedList mockedList = mock(LinkedList.class);
	
	@Test
	public void testMockedListStubbing() {
				 
		 //stubbing
		 when(mockedList.get(0)).thenReturn("first");
		 when(mockedList.get(1)).thenThrow(new RuntimeException());

		 //following prints "first"
		 System.out.println(mockedList.get(0));

		 //following throws runtime exception
		 System.out.println(mockedList.get(1));

		 //following prints "null" because get(999) was not stubbed
		 System.out.println(mockedList.get(999));

		 //Although it is possible to verify a stubbed invocation, usually it's just redundant
		 //If your code cares what get(0) returns, then something else breaks (often even before verify() gets executed).
		 //If your code doesn't care what get(0) returns, then it should not be stubbed.
		 verify(mockedList).get(0);		 		
	}
	
	@Test
	public void testArgumentMatchers() {
		
		 //stubbing using built-in anyInt() argument matcher
		 when(mockedList.get(anyInt())).thenReturn("element");

		 //stubbing using custom matcher (let's say isValid() returns your own matcher implementation):
//		 when(mockedList.contains(argThat(isValid()))).thenReturn(true);

		 //following prints "element"
		 System.out.println(mockedList.get(999));

		 //you can also verify using an argument matcher
		 verify(mockedList).get(anyInt());

		 //argument matchers can also be written as Java 8 Lambdas
//		 verify(mockedList).add(argThat(someString -> someString.length() > 5));
	}
	
	@Test
	public void testVerifyingNumberOfInvocations() {
		
		 //using mock
		 mockedList.add("once");

		 mockedList.add("twice");
		 mockedList.add("twice");

		 mockedList.add("three times");
		 mockedList.add("three times");
		 mockedList.add("three times");

		 //following two verifications work exactly the same - times(1) is used by default
		 verify(mockedList).add("once");
		 verify(mockedList, times(1)).add("once");

		 //exact number of invocations verification
		 verify(mockedList, times(2)).add("twice");
		 verify(mockedList, times(3)).add("three times");

		 //verification using never(). never() is an alias to times(0)
		 verify(mockedList, never()).add("never happened");

		 //verification using atLeast()/atMost()
		 verify(mockedList, atMostOnce()).add("once");
		 verify(mockedList, atLeastOnce()).add("three times");
		 verify(mockedList, atLeast(2)).add("three times");
		 verify(mockedList, atMost(5)).add("three times");
	}
	
	@Test
	public void testStubbingVoidMethodsWithExceptions() {
		 
		 doThrow(new RuntimeException()).when(mockedList).clear();
		 
		 //following throws RuntimeException:
		 mockedList.clear();		
	}	
	
	@Test
	public void testVerificationOfOrder() {
		 // A. Single mock whose methods must be invoked in a particular order
		 List singleMock = mock(List.class);

		 //using a single mock
		 singleMock.add("was added first");
		 singleMock.add("was added second");

		 //create an inOrder verifier for a single mock
		 InOrder inOrder = inOrder(singleMock);

		 //following will make sure that add is first called with "was added first", then with "was added second"
		 inOrder.verify(singleMock).add("was added first");
		 inOrder.verify(singleMock).add("was added second");

		 // B. Multiple mocks that must be used in a particular order
		 List firstMock = mock(List.class);
		 List secondMock = mock(List.class);

		 //using mocks
		 firstMock.add("was called first");
		 secondMock.add("was called second");

		 //create inOrder object passing any mocks that need to be verified in order
		 InOrder inOrder1 = inOrder(firstMock, secondMock);

		 //following will make sure that firstMock was called before secondMock
		 inOrder1.verify(firstMock).add("was called first");
		 inOrder1.verify(secondMock).add("was called second");

		 // Oh, and A + B can be mixed together at will
	}
	
	@Test
	public void testMockIndependence() {
		
		 List mockOne = mock(List.class);
		 List mockTwo = mock(List.class);
		 List mockThree = mock(List.class);
		
		 //using mocks - only mockOne is interacted
		 mockOne.add("one");

		 //ordinary verification
		 verify(mockOne).add("one");

		 //verify that method was never called on a mock
		 verify(mockOne, never()).add("two");

		 //verify that other mocks were not interacted
		 verifyZeroInteractions(mockTwo, mockThree);
	}
	
//	@Test
//	public void testConsecutiveCalls() {
//		
//		 Calculator mock = mock(Calculator.class);
//	
//		 when(mock.add(1,2)
//		   .thenThrow(new RuntimeException())
//		   .thenReturn(3);
//
//		 //First call: throws runtime exception:
//		 mock.someMethod("some arg");
//
//		 //Second call: prints "foo"
//		 System.out.println(mock.someMethod("some arg"));
//
//		 //Any consecutive call: prints "foo" as well (last stubbing wins).
//		 System.out.println(mock.someMethod("some arg"));
//	}
	
	@Test
	public void testSpy() {
		   List list = new LinkedList();
		   List spy = spy(list);

		   //optionally, you can stub out some methods:
		   when(spy.size()).thenReturn(100);

		   //using the spy calls *real* methods
		   spy.add("one");
		   spy.add("two");

		   //prints "one" - the first element of a list
		   System.out.println(spy.get(0));

		   //size() method was stubbed - 100 is printed
		   System.out.println(spy.size());

		   //optionally, you can verify
		   verify(spy).add("one");
		   verify(spy).add("two");
	}



	
}
