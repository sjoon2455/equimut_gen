// This is a mutant program.
// Author : ysma

public class Prime_num
{

    public static  int[] primenum()
    {
        int m;
        int i;
        int k;
        int h = 0;
        int leap = 1;
        for (m = 1; m <= 5; m++) {
            k = (int) Math.sqrt( m + 1 );
            for (i = 2; i != k; i++) {
                if (m % i == 0) {
                    leap = 0;
                    break;
                }
            }
            if (leap != 0) {
                h++;
            }
            leap = 1;
        }
        return new int[]{ h, leap };
    }

}
