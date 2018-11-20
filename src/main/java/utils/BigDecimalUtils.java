package utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

    public static BigDecimal divide(BigDecimal f, BigDecimal m){

        if (f == null || m == null){
            return BigDecimal.ZERO;
        }
        return f.divide(m,2, BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal divide4Int(int f, int m){

        if (m == 0){
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(f).divide(BigDecimal.valueOf(m),4,BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal divide4Long(long f, long m){

        if (m == 0){
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(f).divide(BigDecimal.valueOf(m),4,BigDecimal.ROUND_HALF_EVEN);
    }



}
