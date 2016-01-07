package mocktest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class AMockTest {
	@Mock
	private List mockList;

	// @Captor,@Spy,@InjectMocks
	@Test
	public void shorthand() {
		mockList.add(1);
		verify(mockList).add(1);
	}
	/**
	 * 连续调用的示例
	 */
	@Test(expected = RuntimeException.class)
	public void consecutive_calls() {
		// 模拟连续调用返回期望值，如果分开，则只有最后一个有效
		when(mockList.get(0)).thenReturn(0);
		when(mockList.get(0)).thenReturn(1);
		when(mockList.get(0)).thenReturn(2);
		when(mockList.get(1)).thenReturn(0).thenReturn(1)
				.thenThrow(new RuntimeException());
		assertEquals(2, mockList.get(0));
		assertEquals(2, mockList.get(0));
		assertEquals(0, mockList.get(1));
		assertEquals(1, mockList.get(1));
		// 第三次或更多调用都会抛出异常
		mockList.get(1);
	}
	
	@Test
	public void answer_with_callback(){
		//使用Answer来生成我们我们期望的返回
		when(mockList.get(anyInt())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return "hello world:"+args[0];
			}
		});
		assertEquals("hello world:0",mockList.get(0));
		assertEquals("hello world:999",mockList.get(999));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void spy_on_real_objects(){
		List list = new LinkedList();
		List spy = Mockito.spy(list);
		//下面预设的spy.get(0)会报错，因为会调用真实对象的get(0)，所以会抛出越界异常
		//when(spy.get(0)).thenReturn(3);

		//使用doReturn-when可以避免when-thenReturn调用真实对象api
		doReturn(999).when(spy).get(999);
		//预设size()期望值
		when(spy.size()).thenReturn(100);
		//调用真实对象的api
		spy.add(1);
		spy.add(2);
		assertEquals(100,spy.size());
		assertEquals(1,spy.get(0));
		assertEquals(2,spy.get(1));
		verify(spy).add(1);
		verify(spy).add(2);
		assertEquals(999,spy.get(999));
		spy.get(2);
	}
	
	@Test
	public void unstubbed_invocations(){
		//mock对象使用Answer来对未预设的调用返回默认期望值
		List mock = Mockito.mock(List.class,new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return 999;
			}
		});
		//下面的get(1)没有预设，通常情况下会返回NULL，但是使用了Answer改变了默认期望值
		assertEquals(999, mock.get(1));
		//下面的size()没有预设，通常情况下会返回0，但是使用了Answer改变了默认期望值
		assertEquals(999,mock.size());
	}
}
