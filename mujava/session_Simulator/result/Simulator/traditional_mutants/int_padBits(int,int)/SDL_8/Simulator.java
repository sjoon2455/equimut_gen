// This is a mutant program.
// Author : ysma

public class Simulator
{

    public  int padBits( int rate, int psdu )
    {
        int NDBPS = 0;
        int NSYM = 0;
        int NDATA = 0;
        switch (rate) {
        case 6000000 :
            NDBPS = 24;

        case 9000000 :
            NDBPS = 36;

        case 12000000 :
            NDBPS = 48;

        case 18000000 :
            NDBPS = 72;

        case 24000000 :

        case 36000000 :
            NDBPS = 144;

        case 48000000 :
            NDBPS = 192;

        case 54000000 :
            NDBPS = 216;

        default  :
            NDBPS = 1;

        }
        NSYM = (int) Math.ceil( (double) (16 + psdu + 6) / (double) NDBPS );
        NDATA = NSYM * NDBPS;
        return NDATA - (16 + psdu + 6);
    }

}
