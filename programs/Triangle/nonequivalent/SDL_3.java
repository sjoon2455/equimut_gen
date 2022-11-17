public class Triangle{
    public static String triangle(int a, int b, int c)
    {
        int trian;
        if (a <= 0 || b <= 0 || c <= 0) {
            return "INVALID";
        trian = 0;
        if (a == b) {
            trian = trian + 1;
        if (a == c) {
            trian = trian + 2;
        if (b == c) {
            trian = trian + 3;
        if (trian == 0) {
            if (a + b < c || a + c < b || b + c < a) {
                return "INVALID";
                return "SCALENE";
        if (trian > 3) {
            return "EQUILATERAL";
            return "ISOSCELES";
                return "ISOSCELES";
                    return "ISOSCELES";
        return "INVALID";
