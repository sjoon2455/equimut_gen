// This is a mutant program.
// Author : ysma

import java.util.*;


public class Insert
{

    public static  int[] insert( int number )
    {
        int[] a = new int[]{ -14, 6, 28, 0 };
        int mytemp1;
        int mytemp2;
        int end;
        int i;
        int j;
        end = a[2];
        if (number >= end) {
            a[3] = number;
        } else {
            for (i = 0; i < 3; i++) {
                if (a[i] <= number) {
                    mytemp1 = a[i];
                    a[i] = number;
                    for (j = i + 1; j < 4; j++) {
                        mytemp2 = a[j];
                        a[j] = mytemp1;
                        mytemp1 = mytemp2;
                    }
                    break;
                }
            }
        }
        return a;
    }

}
