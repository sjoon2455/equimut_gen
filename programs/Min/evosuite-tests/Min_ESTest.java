/*
 * This file was automatically generated by EvoSuite
 * Thu Nov 11 06:41:05 GMT 2021
 */


import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Min_ESTest {
  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      int int0 = Min.min((-1547), (-1));
      assertEquals((-1547), int0);
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      int int0 = Min.min(230, 0);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      int int0 = Min.min(230, 230);
      assertEquals(230, int0);
  }

  @Test(timeout = 4000)
  public void test3()  throws Throwable  {
      Min min0 = new Min();
  }
}
