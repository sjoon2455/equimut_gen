// This is a mutant program.
// Author : ysma

public class Day
{

    public static  int day( int day, int month, int year )
    {
        int sum = 0;
        int leap;
        sum = sum + day;
        if (year % 400 == 0 || year % 4 == 0 && year % 100 != 0) {
            leap = 1;
        } else {
            leap = 0;
        }
        if (leap == 1 && month > 2) {
            sum++;
        }
        return sum;
    }

}
