/*
 * This file was automatically generated by EvoSuite
 * Mon Dec 27 12:21:12 GMT 2021
 */


import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Prime_num_ESTest {
  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      int[] intArray0 = Prime_num.primenum();
      assertArrayEquals(new int[] {4, 1}, intArray0);
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      Prime_num prime_num0 = new Prime_num();
  }
}
