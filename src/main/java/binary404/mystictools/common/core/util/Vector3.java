package binary404.mystictools.common.core.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

public class Vector3 {

    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    public static final Vector3 ONE = new Vector3(1, 1, 1);

    public final double x;
    public final double y;
    public final double z;

    public Vector3(double d, double d1, double d2) {
        x = d;
        y = d1;
        z = d2;
    }

    public Vector3(Vector3d vec) {
        this(vec.x, vec.y, vec.z);
    }

    public static Vector3 fromBlockPos(BlockPos pos) {
        return new Vector3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vector3 fromEntityCenter(Entity e) {
        return new Vector3(e.getX(), e.getY() - e.getMyRidingOffset() + e.getBbHeight() / 2, e.getZ());
    }

    public static Vector3 fromTileEntity(BlockEntity e) {
        return fromBlockPos(e.getBlockPos());
    }

    public static Vector3 fromTileEntityCenter(BlockEntity e) {
        return fromTileEntity(e).add(0.5);
    }

    public double dotProduct(Vector3 vec) {
        double d = vec.x * x + vec.y * y + vec.z * z;

        if (d > 1 && d < 1.00001) {
            d = 1;
        } else if (d < -1 && d > -1.00001) {
            d = -1;
        }
        return d;
    }

    public double dotProduct(double d, double d1, double d2) {
        return d * x + d1 * y + d2 * z;
    }

    public Vector3 crossProduct(Vector3 vec) {
        double d = y * vec.z - z * vec.y;
        double d1 = z * vec.x - x * vec.z;
        double d2 = x * vec.y - y * vec.x;
        return new Vector3(d, d1, d2);
    }

    public Vector3 add(double d, double d1, double d2) {
        return new Vector3(x + d, y + d1, z + d2);
    }

    public Vector3 add(Vector3 vec) {
        return add(vec.x, vec.y, vec.z);
    }

    public Vector3 add(double d) {
        return add(d, d, d);
    }

    public Vector3 subtract(Vector3 vec) {
        return new Vector3(x - vec.x, y - vec.y, z - vec.z);
    }

    public Vector3 multiply(double d) {
        return multiply(d, d, d);
    }

    public Vector3 multiply(Vector3 f) {
        return multiply(f.x, f.y, f.z);
    }

    public Vector3 multiply(double fx, double fy, double fz) {
        return new Vector3(x * fx, y * fy, z * fz);
    }

    public double mag() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double magSquared() {
        return x * x + y * y + z * z;
    }

    public Vector3 normalize() {
        double d = mag();
        if (d != 0) {
            return multiply(1 / d);
        }

        return this;
    }

    @Override
    public String toString() {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Vector3(" + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + ")";
    }

    public Vector3 perpendicular() {
        if (z == 0) {
            return zCrossProduct();
        }
        return xCrossProduct();
    }

    public Vector3 xCrossProduct() {
        double d = z;
        double d1 = -y;
        return new Vector3(0, d, d1);
    }

    public Vector3 zCrossProduct() {
        double d = y;
        double d1 = -x;
        return new Vector3(d, d1, 0);
    }

    public Vector3 yCrossProduct() {
        double d = -z;
        double d1 = x;
        return new Vector3(d, 0, d1);
    }

    public Vector3d toVector3d() {
        return new Vector3d(x, y, z);
    }

    public Vec3 toVec3() {
        return new Vec3(x, y, z);
    }

    public double angle(Vector3 vec) {
        double projection = normalize().dotProduct(vec.normalize());
        return Math.acos(Mth.clamp(projection, -1, 1));
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    @OnlyIn(Dist.CLIENT)
    public void vertex(Matrix4f mat, VertexConsumer buffer) {
        buffer.vertex(mat, (float) x, (float) y, (float) z);
    }

    public Vector3 negate() {
        return new Vector3(-x, -y, -z);
    }

    public double scalarProject(Vector3 b) {
        double l = b.mag();
        return l == 0 ? 0 : dotProduct(b) / l;
    }

    public Vector3 project(Vector3 b) {
        double l = b.magSquared();
        if (l == 0) {
            return ZERO;
        }

        double m = dotProduct(b) / l;
        return b.multiply(m);
    }

    public Vector3 rotate(double theta, Vector3 axis) {
        if (Mth.equal(theta, 0)) {
            return this;
        }

        // Rodrigues rotation formula
        Vector3 k = axis.normalize();
        Vector3 v = this;

        float cosTheta = Mth.cos((float) theta);
        Vector3 firstTerm = v.multiply(cosTheta);
        Vector3 secondTerm = k.crossProduct(v).multiply(Mth.sin((float) theta));
        Vector3 thirdTerm = k.multiply(k.dotProduct(v) * (1 - cosTheta));
        return new Vector3(firstTerm.x + secondTerm.x + thirdTerm.x,
                firstTerm.y + secondTerm.y + thirdTerm.y,
                firstTerm.z + secondTerm.z + thirdTerm.z);
    }

    public AABB boxForRange(double range) {
        return boxForRange(range, range, range);
    }

    public AABB boxForRange(double rangeX, double rangeY, double rangeZ) {
        return new AABB(x - rangeX, y - rangeY, z - rangeZ, x + rangeX, y + rangeY, z + rangeZ);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vector3)) {
            return false;
        }

        Vector3 v = (Vector3) o;
        return x == v.x && y == v.y && z == v.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

}
