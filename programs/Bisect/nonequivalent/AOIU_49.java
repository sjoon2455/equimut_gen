import java.lang.Math;

public class Bisect{
    public static double sqrt(double N)
    {
        double x = N;
        double M = N;
        double m = 1;
        double r = x;
double diff = -x * x - N;
        double mEpsilon = 0.01;
        double mResult;        
        while (Math.abs(diff) > mEpsilon) {
            if (diff < 0) {
                m = x;
                x = (M + x) / 2;
            } else {
                if (diff > 0) {
                    M = x;
                    x = (m + x) / 2;
                }
            }
            diff = x * x - N;
        }
        r = x;
        mResult = r;
        return r;
    }
}
