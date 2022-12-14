/*
 * This file was automatically generated by EvoSuite
 * Thu Jun 14 07:05:45 CST 2018
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class PrimeNumberTest {

  @Test(timeout = 2000)
  public void test00()  throws Throwable  {
      PrimeNumber primeNumber0 = new PrimeNumber();
      primeNumber0.factorzation(109);
      primeNumber0.factorzation(109);
      primeNumber0.factorzation((-1));
      primeNumber0.factorzation(109);
      primeNumber0.factorzation((-1));
      primeNumber0.factorzation((-1));
      primeNumber0.factorzation(109);
      primeNumber0.factorzation((-1));
      primeNumber0.factorzation(109);
      primeNumber0.factorzation((-1353));
      primeNumber0.factorzation((-1));
      primeNumber0.factorzation(0);
      primeNumber0.factorzation(0);
      List<Integer> list0 = primeNumber0.factorzation(109);
      assertTrue(list0.contains(109));
      
      List<Integer> list1 = primeNumber0.factorzation(3072);
      assertEquals(11, list1.size());
      assertFalse(list1.contains(3072));
  }

  @Test(timeout = 2000)
  public void test01()  throws Throwable  {
      PrimeNumber primeNumber0 = new PrimeNumber();
      primeNumber0.factorzation(0);
      primeNumber0.factorzation((-1331));
      primeNumber0.factorzation(1);
      primeNumber0.factorzation(3372);
      primeNumber0.factorzation(3877);
      List<Integer> list0 = primeNumber0.factorzation(3877);
      assertTrue(list0.contains(3877));
      
      List<Integer> list1 = primeNumber0.factorzation(1);
      primeNumber0.factorzation((-1));
      List<Integer> list2 = primeNumber0.factorzation(2849);
      assertEquals(3, list2.size());
      
      primeNumber0.factorzation((-1));
      primeNumber0.factorzation(0);
      primeNumber0.factorzation((-1));
      List<Integer> list3 = primeNumber0.factorzation((-1256));
      assertTrue(list3.equals((Object)list1));
  }

  @Test(timeout = 2000)
  public void test02()  throws Throwable  {
      PrimeNumber primeNumber0 = new PrimeNumber();
      primeNumber0.factorzation(2526);
      primeNumber0.factorzation(2);
      primeNumber0.factorzation(2);
      primeNumber0.factorzation(2526);
      primeNumber0.factorzation(2);
      primeNumber0.factorzation(2526);
      primeNumber0.factorzation(4739);
      primeNumber0.factorzation(2);
      primeNumber0.factorzation(2526);
      primeNumber0.factorzation(47);
      primeNumber0.factorzation(2526);
      primeNumber0.factorzation(4739);
      primeNumber0.factorzation(2);
      primeNumber0.factorzation(4);
      primeNumber0.factorzation(2);
      primeNumber0.factorzation(2526);
      primeNumber0.factorzation(4739);
      primeNumber0.factorzation(4);
      primeNumber0.factorzation(48);
      primeNumber0.factorzation(2);
      primeNumber0.factorzation(4);
      primeNumber0.factorzation(60);
      primeNumber0.factorzation(21);
  }

}
