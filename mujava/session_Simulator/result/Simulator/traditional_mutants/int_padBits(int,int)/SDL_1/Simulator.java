// This is a mutant program.
// Author : ysma

public class Simulator
{

    public  int padBits( int rate, int psdu )
    {
        int NDBPS = 0;
        int NSYM = 0;
        int NDATA = 0;
        NSYM = (int) Math.ceil( (double) (16 + psdu + 6) / (double) NDBPS );
        NDATA = NSYM * NDBPS;
        return NDATA - (16 + psdu + 6);
    }

}
