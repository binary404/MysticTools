package binary404.mystictools.common.core;

import net.minecraft.util.math.Vec3d;

public class Utils {

    public static Vec3d calculateVelocity(final Vec3d from, final Vec3d to, final double heightGain, final double gravity) {
        final double endGain = to.y - from.y;
        final double horizDist = Math.sqrt(distanceSquared2d(from, to));
        final double gain = heightGain;
        final double maxGain = (gain > endGain + gain) ? gain : (endGain + gain);
        final double a = -horizDist * horizDist / (4.0 * maxGain);
        final double b = horizDist;
        final double c = -endGain;
        final double slope = -b / (2.0 * a) - Math.sqrt(b * b - 4.0 * a * c) / (2.0 * a);
        final double vy = Math.sqrt(maxGain * gravity);
        final double vh = vy / slope;
        final double dx = to.x - from.x;
        final double dz = to.z - from.z;
        final double mag = Math.sqrt(dx * dx + dz * dz);
        final double dirx = dx / mag;
        final double dirz = dz / mag;
        final double vx = vh * dirx;
        final double vz = vh * dirz;
        return new Vec3d(vx, vy, vz);
    }

    public static double distanceSquared2d(final Vec3d from, final Vec3d to) {
        final double dx = to.x - from.x;
        final double dz = to.z - from.z;
        return dx * dx + dz * dz;
    }

    public static double distanceSquared3d(final Vec3d from, final Vec3d to) {
        final double dx = to.x - from.x;
        final double dy = to.y - from.y;
        final double dz = to.z - from.z;
        return dx * dx + dy * dy + dz * dz;
    }

}
