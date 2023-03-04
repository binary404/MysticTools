package binary404.mystictools.common.core.helper.util;


import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class MathUtils {

    public static double map(double value, double x0, double y0, double x1, double y1) {
        return x1 + (y1 - x1) * ((value - x0) / (y0 - x0));
    }

    public static double extractYaw(Vec3 vec) {
        return Math.atan2(vec.z, vec.x);
    }

    public static double extractPitch(Vec3 vec) {
        return Math.asin(vec.y / vec.length());
    }

    public static Vec3 rotateYaw(Vec3 vec, float yaw) {
        float f = Mth.cos(yaw);
        float f1 = Mth.sin(yaw);
        double d0 = vec.x * (double) f + vec.z * (double) f1;
        double d1 = vec.y;
        double d2 = vec.z * (double) f - vec.x * (double) f1;
        return new Vec3(d0, d1, d2);
    }

    public static Vec3 rotateRoll(Vec3 vec, float roll) {
        float f = Mth.cos(roll);
        float f1 = Mth.sin(roll);
        double d0 = vec.x * (double) f + vec.y * (double) f1;
        double d1 = vec.y * (double) f - vec.x * (double) f1;
        double d2 = vec.z;
        return new Vec3(d0, d1, d2);
    }

}
