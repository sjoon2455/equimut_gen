// This is a mutant program.
// Author : ysma

public class Mid
{

    public static  int mid( int a, int b, int c )
    {
        int mid;
        if (true) {
            if (c < b) {
                if (a < c) {
                    mid = c;
                } else {
                    mid = a;
                }
            } else {
                mid = b;
            }
        } else {
            if (c > b) {
                if (a > c) {
                    mid = c;
                } else {
                    mid = a;
                }
            } else {
                mid = b;
            }
        }
        return mid;
    }

}
