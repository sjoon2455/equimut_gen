/*
 * This file was automatically generated by EvoSuite
 * Thu Nov 11 06:37:35 GMT 2021
 */


import org.junit.Test;
import static org.junit.Assert.*;
// import org.evosuite.runtime.EvoRunner;
// import org.evosuite.runtime.EvoRunnerParameters;
// import org.junit.runner.RunWith;

// @RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true) 
// public class Triangle_ESTest extends Triangle_ESTest_scaffolding {
public class Triangle_ESTest{

  @Test(timeout = 4000)
  public void test00()  throws Throwable  {
      String string0 = Triangle.triangle(1, 2, 1);
      assertEquals("INVALID", string0);
  }

  @Test(timeout = 4000)
  public void test01()  throws Throwable  {
      String string0 = Triangle.triangle(1, 1, 2);
      assertEquals("INVALID", string0);
  }

  @Test(timeout = 4000)
  public void test02()  throws Throwable  {
      String string0 = Triangle.triangle(3, 1, 2);
      assertEquals("SCALENE", string0);
  }

  @Test(timeout = 4000)
  public void test03()  throws Throwable  {
      String string0 = Triangle.triangle(1, 4, 3);
      assertEquals("SCALENE", string0);
  }

  @Test(timeout = 4000)
  public void test04()  throws Throwable  {
      String string0 = Triangle.triangle(1, 2, 3);
      assertEquals("SCALENE", string0);
  }

  @Test(timeout = 4000)
  public void test05()  throws Throwable  {
      String string0 = Triangle.triangle(260, 260, 0);
      assertEquals("INVALID", string0);
  }

  @Test(timeout = 4000)
  public void test06()  throws Throwable  {
      String string0 = Triangle.triangle(1116, 0, 0);
      assertEquals("INVALID", string0);
  }

  @Test(timeout = 4000)
  public void test07()  throws Throwable  {
      String string0 = Triangle.triangle(0, 1, 1);
      assertEquals("INVALID", string0);
  }

  @Test(timeout = 4000)
  public void test08()  throws Throwable  {
      String string0 = Triangle.triangle(2774, 3394, 3394);
      assertEquals("ISOSCELES", string0);
  }

  @Test(timeout = 4000)
  public void test09()  throws Throwable  {
      String string0 = Triangle.triangle(3394, 2774, 3394);
      assertEquals("ISOSCELES", string0);
  }

  @Test(timeout = 4000)
  public void test10()  throws Throwable  {
      String string0 = Triangle.triangle(29, 1064, 29);
      assertEquals("INVALID", string0);
  }

  @Test(timeout = 4000)
  public void test11()  throws Throwable  {
      String string0 = Triangle.triangle(891, 891, 2444);
      assertEquals("INVALID", string0);
  }

  @Test(timeout = 4000)
  public void test12()  throws Throwable  {
      String string0 = Triangle.triangle(897, 29, 29);
      assertEquals("INVALID", string0);
  }

  @Test(timeout = 4000)
  public void test13()  throws Throwable  {
      String string0 = Triangle.triangle(1064, 1064, 897);
      assertEquals("ISOSCELES", string0);
  }

  @Test(timeout = 4000)
  public void test14()  throws Throwable  {
      String string0 = Triangle.triangle(897, 29, 540);
      assertEquals("INVALID", string0);
  }

  @Test(timeout = 4000)
  public void test15()  throws Throwable  {
      String string0 = Triangle.triangle(897, 29, 1064);
      assertEquals("INVALID", string0);
  }

  @Test(timeout = 4000)
  public void test16()  throws Throwable  {
      String string0 = Triangle.triangle(897, 897, 897);
      assertEquals("EQUILATERAL", string0);
  }

  @Test(timeout = 4000)
  public void test17()  throws Throwable  {
      String string0 = Triangle.triangle(1, 1351, 922);
      assertEquals("INVALID", string0);
  }

  @Test(timeout = 4000)
  public void test18()  throws Throwable  {
      String string0 = Triangle.triangle(726, (-449), 1351);
      assertEquals("INVALID", string0);
  }

  @Test(timeout = 4000)
  public void test19()  throws Throwable  {
      String string0 = Triangle.triangle(726, 1351, (-449));
      assertEquals("INVALID", string0);
  }

  @Test(timeout = 4000)
  public void test20()  throws Throwable  {
      String string0 = Triangle.triangle((-449), (-449), (-449));
      assertEquals("INVALID", string0);
  }

  @Test(timeout = 4000)
  public void test21()  throws Throwable  {
      Triangle triangle0 = new Triangle();
  }
}
