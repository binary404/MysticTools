package binary404.mystictools.common.core.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class MathUtils {

    public static double map(double value, double x0, double y0, double x1, double y1) {
        return x1 + (y1 - x1) * ((value - x0) / (y0 - x0));
    }

    public static double extractYaw(Vector3d vec) {
        return Math.atan2(vec.getZ(), vec.getX());
    }

    public static double extractPitch(Vector3d vec) {
        return Math.asin(vec.getY() / vec.length());
    }

    public static Vector3d rotateYaw(Vector3d vec, float yaw) {
        float f = MathHelper.cos(yaw);
        float f1 = MathHelper.sin(yaw);
        double d0 = vec.getX() * (double) f + vec.getZ() * (double) f1;
        double d1 = vec.getY();
        double d2 = vec.getZ() * (double) f - vec.getX() * (double) f1;
        return new Vector3d(d0, d1, d2);
    }

    public static Vector3d rotateRoll(Vector3d vec, float roll) {
        float f = MathHelper.cos(roll);
        float f1 = MathHelper.sin(roll);
        double d0 = vec.getX() * (double) f + vec.getY() * (double) f1;
        double d1 = vec.getY() * (double) f - vec.getX() * (double) f1;
        double d2 = vec.getZ();
        return new Vector3d(d0, d1, d2);
    }

}
