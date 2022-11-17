// This is a mutant program.
// Author : ysma

import java.util.*;


public class Profit
{

    public static  double func( double i )
    {
        double bonus1;
        double bonus2;
        double bonus4;
        double bonus6;
        double bonus10;
        double bonus;
        bonus1 = 100000 * 0.1;
        bonus2 = 100000 * 0.1 + 100000 * 0.075;
        bonus4 = 100000 * 0.1 / (100000 * 0.075) + 200000 * 0.05;
        bonus6 = 100000 * 0.1 + 100000 * 0.075 + 200000 * 0.05 + 200000 * 0.03;
        bonus10 = 100000 * 0.1 + 100000 * 0.075 + 200000 * 0.05 + 200000 * 0.03 + 400000 * 0.015;
        if (i <= 100000) {
            bonus = i * 0.1;
        } else {
            if (i <= 200000) {
                bonus = bonus1 + i * 0.075 - 100000 * 0.075;
            } else {
                if (i <= 400000) {
                    bonus = bonus2 + i * 0.05 - 200000 * 0.05;
                } else {
                    if (i <= 600000) {
                        bonus = bonus4 + i * 0.03 - 400000 * 0.03;
                    } else {
                        if (i <= 1000000) {
                            bonus = bonus6 + i * 0.015 - 600000 * 0.015;
                        } else {
                            bonus = bonus10 + i * 0.01 - 1000000 * 0.01;
                        }
                    }
                }
            }
        }
        return bonus;
    }

}
