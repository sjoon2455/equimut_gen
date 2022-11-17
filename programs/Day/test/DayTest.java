import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DayTest {

	@Test(timeout=2000)
	public void test01() {
		Day d = new Day();
		assertEquals(121, d.whichDay(30, 4, 2000));
	}
	
	@Test(timeout = 2000)
	public void test02()  throws Throwable  {
	      Day day0 = new Day();
	      int int0 = day0.whichDay(1, 6, 0);
	      assertEquals(153, int0);
	}
	
	@Test(timeout = 2000)
	public void test03()  throws Throwable  {
	      Day day0 = new Day();
	      int int0 = day0.whichDay(0, 0, 12);
	      assertEquals(0, int0);
	  }

	@Test(timeout = 2000)
	public void test04()  throws Throwable  {
	      Day day0 = new Day();
	      int int0 = day0.whichDay(0, 1, 0);
	      assertEquals(0, int0);
	  }

	@Test(timeout = 2000)
	public void test05()  throws Throwable  {
	      Day day0 = new Day();
	      int int0 = day0.whichDay(0, 13, 0);
	      assertEquals(0, int0);
	  }

	@Test(timeout = 2000)
	public void test06()  throws Throwable  {
	      Day day0 = new Day();
	      int int0 = day0.whichDay(1, 1, 0);
	      assertEquals(1, int0);
	  }
	  
	@Test(timeout = 2000)
	public void test07()  throws Throwable  {
	      Day day0 = new Day();
	      int int0 = day0.whichDay(29, 2, (-173));
	      assertEquals(0, int0);
	  }
}
