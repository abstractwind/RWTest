package mocktest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.exceptions.verification.NoInteractionsWanted;

/**
 * 模拟和验证的测试类
 */
public class BasicTest {

	/**
	 * 验证行为
	 */
	@Test
	public void verify_behaviour(){
		//模拟创建一个List对象
		List mock = mock(List.class);
		//使用mock的对象
		mock.add(1);
		mock.clear();
		//验证add(1)和clear()行为是否发生
		verify(mock).add(1);
		verify(mock).clear();
	}
	/**
	 * 模拟结果
	 */
	@Test
	public void when_thenReturn(){
		//mock一个Iterator类
		Iterator iterator = mock(Iterator.class);
		//预设当iterator调用next()时第一次返回hello，第n次都返回world
		when(iterator.next()).thenReturn("hello").thenReturn("world");
		//使用mock的对象
		String result = iterator.next() + " " + iterator.next() + " " + iterator.next();
		//验证结果
		assertEquals("hello world world",result);
	}
	/**
	 * 模拟方法抛出异常
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	public void when_thenThrow() throws IOException {
		OutputStream outputStream = mock(OutputStream.class);
		//预设当流关闭时抛出异常
		doThrow(new IOException()).when(outputStream).close();
		outputStream.close();
	}
	/**
	 * 根据参数模拟结果
	 */
	@Test
	public void with_arguments(){
		Comparable comparable = mock(Comparable.class);
		//预设根据不同的参数返回不同的结果
		when(comparable.compareTo("Test")).thenReturn(1);
		when(comparable.compareTo("Omg")).thenReturn(2);
		assertEquals(1, comparable.compareTo("Test"));
		assertEquals(2, comparable.compareTo("Omg"));
		//对于没有预设的情况会返回默认值
		assertEquals(0, comparable.compareTo("Not stub"));
	}
	/**
	 * 验证调用次数
	 */
	@Test
	public void verifying_number_of_invocations(){
		List list = mock(List.class);
		list.add(1);
		list.add(2);
		list.add(2);
		list.add(3);
		list.add(3);
		list.add(3);
		//验证是否被调用一次，等效于下面的times(1)
		verify(list).add(1);
		verify(list,times(1)).add(1);
		//验证是否被调用2次
		verify(list,times(2)).add(2);
		//验证是否被调用3次
		verify(list,times(3)).add(3);
		//验证是否从未被调用过
		verify(list,never()).add(4);
		//验证至少调用一次
		verify(list,atLeastOnce()).add(1);
		//验证至少调用2次
		verify(list,atLeast(2)).add(2);
		//验证至多调用3次
		verify(list,atMost(3)).add(3);
	}
	/**
	 * 验证执行顺序
	 */
	@Test
	public void verification_in_order(){
		List list = mock(List.class);
		List list2 = mock(List.class);
		list.add(1);
		list2.add("hello");
		list.add(2);
		list2.add("world");
		//将需要排序的mock对象放入InOrder
		InOrder inOrder = inOrder(list,list2);
		//下面的代码不能颠倒顺序，验证执行顺序
		inOrder.verify(list).add(1);
		inOrder.verify(list2).add("hello");
		inOrder.verify(list).add(2);
		inOrder.verify(list2).add("world");
	}
	/**
	 * 验证模拟对象上无行为
	 */
	@Test
	public void verify_interaction(){
		List list = mock(List.class);
		List list2 = mock(List.class);
		List list3 = mock(List.class);
		list.add(1);
		verify(list).add(1);
		verify(list,never()).add(2);
		//验证零互动行为
		verifyZeroInteractions(list2,list3);
	}
	/**
	 * 验证无未发生的互动行为
	 */
	@Test(expected = NoInteractionsWanted.class)
	public void find_redundant_interaction(){
		List list = mock(List.class);
		list.add(1);
		list.add(2);
		verify(list,times(2)).add(anyInt());
		//检查是否有未被验证的互动行为，因为add(1)和add(2)都会被上面的anyInt()验证到，所以下面的代码会通过
		verifyNoMoreInteractions(list);

		List list2 = mock(List.class);
		list2.add(1);
		list2.add(2);
		verify(list2).add(1);
		//检查是否有未被验证的互动行为，因为add(2)没有被验证，所以下面的代码会失败抛出异常
		verifyNoMoreInteractions(list2);
	}
	
	@Test
	public void reset_mock(){
		List list = mock(List.class);
		when(list.size()).thenReturn(10);
		list.add(1);
		assertEquals(10,list.size());
		//重置mock，清除所有的互动和预设
		reset(list);
		assertEquals(0,list.size());
	}
}
