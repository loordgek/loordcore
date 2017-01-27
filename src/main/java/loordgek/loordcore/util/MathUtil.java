package loordgek.loordcore.util;

public class MathUtil {
    public static double getpercentage(int Current, int max){
        return scale(Current, max, 100);
    }

    public static double getpercentagereverse(int Current, int max){
        return scalereverse(Current, max, 100);
    }

    public static double scale(int currentint, int maxint, int size) {
        if (maxint == 0) return 0;
        return currentint * size / maxint;
    }

    public static double scalereverse(double currentint, double maxint, double size) {
        if (maxint == 0) return 0;
        return (reverseNumber(currentint, 0, maxint) * size / maxint);
    }

    public static double reverseNumber(double num, double min, double max) {
        return (max + min) - num;
    }
}
