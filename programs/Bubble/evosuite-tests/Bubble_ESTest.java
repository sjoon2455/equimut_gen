/*
 * This file was automatically generated by EvoSuite
 * Thu Nov 11 06:39:10 GMT 2021
 */


import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true) 
public class Bubble_ESTest extends Bubble_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      int[] intArray0 = Bubble.main((-1), (-1), (-1), (-354), (-1));
      assertArrayEquals(new int[] {(-1), (-1), (-1), (-1), (-354)}, intArray0);
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      Bubble bubble0 = new Bubble();
  }
}
