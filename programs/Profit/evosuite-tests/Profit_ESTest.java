/*
 * This file was automatically generated by EvoSuite
 * Thu Nov 11 06:41:45 GMT 2021
 */


import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true) 
public class Profit_ESTest extends Profit_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test00()  throws Throwable  {
      double double0 = Profit.func(600014.5174628868);
      assertEquals(33500.2177619433, double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test01()  throws Throwable  {
      double double0 = Profit.func(599998.3877622946);
      assertEquals(33499.95163286883, double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test02()  throws Throwable  {
      double double0 = Profit.func(200001.10980373007);
      assertEquals(17500.055490186503, double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test03()  throws Throwable  {
      double double0 = Profit.func(199990.03566657798);
      assertEquals(17499.252674993346, double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test04()  throws Throwable  {
      double double0 = Profit.func(100000.0);
      assertEquals(10000.0, double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test05()  throws Throwable  {
      double double0 = Profit.func((-1.0));
      assertEquals((-0.1), double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test06()  throws Throwable  {
      double double0 = Profit.func(1000032.2475869302);
      assertEquals(39500.3224758693, double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test07()  throws Throwable  {
      double double0 = Profit.func(600000.0);
      assertEquals(33500.0, double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test08()  throws Throwable  {
      double double0 = Profit.func(400000.0);
      assertEquals(27500.0, double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test09()  throws Throwable  {
      double double0 = Profit.func(200000.0);
      assertEquals(17500.0, double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test10()  throws Throwable  {
      double double0 = Profit.func(0.0);
      assertEquals(0.0, double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test11()  throws Throwable  {
      double double0 = Profit.func(1000000.0);
      assertEquals(39500.0, double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test12()  throws Throwable  {
      Profit profit0 = new Profit();
  }
}
