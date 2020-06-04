package binary404.mystictools.common.core.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.ObjectUtils;
import sun.misc.Unsafe;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;


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

    @Nonnull
    public static Optional<ItemStack> findSmeltingResult(World world, ItemStack input) {
        RecipeManager mgr = world.getRecipeManager();
        IInventory inv = new Inventory(input);
        Optional<IRecipe<IInventory>> optRecipe = (Optional<IRecipe<IInventory>>) ObjectUtils.firstNonNull(
                mgr.getRecipe(IRecipeType.SMELTING, inv, world),
                mgr.getRecipe(IRecipeType.CAMPFIRE_COOKING, inv, world),
                mgr.getRecipe(IRecipeType.SMOKING, inv, world),
                Optional.empty());
        return optRecipe.map(recipe -> recipe.getCraftingResult(inv).copy());
    }

    public static final String URL_CLASS_LOADER_CLASS = "java.net.URLClassLoader";

    public static final String BUILTIN_CLASS_LOADER_CLASS = "jdk.internal.loader.BuiltinClassLoader";
    public static final String URL_CLASS_PATH_CLASS = "jdk.internal.loader.URLClassPath";

    @SuppressWarnings("unchecked")
    public static void appendToClassPath(ClassLoader classLoader, URL url) throws Throwable {
        try {
            // Java 8
            Class<?> classLoaderClass = Class.forName(URL_CLASS_LOADER_CLASS);
            if (classLoaderClass.isInstance(classLoader)) {
                // java.net.URLClassLoader.addURL
                Method addURLMethod = classLoaderClass.getDeclaredMethod("addURL", URL.class);
                addURLMethod.setAccessible(true);
                addURLMethod.invoke(classLoader, url);
                return;
            }
        } catch (ClassNotFoundException ex) {
            // no-op
        }

        try {
            // Java 9+
            Class<?> classLoaderClass = Class.forName(BUILTIN_CLASS_LOADER_CLASS);
            Class<?> classPathClass = Class.forName(URL_CLASS_PATH_CLASS);
            if (classLoaderClass.isInstance(classLoader)) {
                Unsafe unsafe = getUnsafe();

                // jdk.internal.loader.BuiltinClassLoader.ucp
                Field ucpField = classLoaderClass.getDeclaredField("ucp");
                long ucpFieldOffset = unsafe.objectFieldOffset(ucpField);
                Object ucpObject = unsafe.getObject(classLoader, ucpFieldOffset);

                // jdk.internal.loader.URLClassPath.path
                Field pathField = classPathClass.getDeclaredField("path");
                long pathFieldOffset = unsafe.objectFieldOffset(pathField);
                ArrayList<URL> path = (ArrayList<URL>) unsafe.getObject(ucpObject, pathFieldOffset);

                // Java 9 & 10 - jdk.internal.loader.URLClassPath.urls
                // Java 11 - jdk.internal.loader.URLClassPath.unopenedUrls
                Field urlsField = getField(classPathClass.getDeclaredFields(), "urls", "unopenedUrls");
                long urlsFieldOffset = unsafe.objectFieldOffset(urlsField);
                Collection<URL> urls = (Collection<URL>) unsafe.getObject(ucpObject, urlsFieldOffset);

                // noinspection SynchronizationOnLocalVariableOrMethodParameter
                synchronized (urls) {
                    if (!path.contains(url)) {
                        urls.add(url);
                        path.add(url);
                    }
                }

                return;
            }
        } catch (ClassNotFoundException ex) {
            // no-op
        }

        throw new UnsupportedOperationException("Unsupported ClassLoader");
    }

    public static URL[] getSystemClassPathURLs() throws Throwable {
        return getSystemClassPathURLs(Utils.class.getClassLoader());
    }

    /**
     * https://stackoverflow.com/questions/46519092/how-to-get-all-jars-loaded-by-a-java-application-in-java9
     * https://github.com/cpw/grossjava9classpathhacks/blob/master/src/main/java/cpw/mods/gross/Java9ClassLoaderUtil.java
     */
    @SuppressWarnings("unchecked")
    public static URL[] getSystemClassPathURLs(ClassLoader classLoader) throws Throwable {
        Objects.requireNonNull(classLoader, "ClassLoader cannot be null");
        try {
            // Java 8
            Class<?> classLoaderClass = Class.forName(URL_CLASS_LOADER_CLASS);
            if (classLoaderClass.isInstance(classLoader)) {
                // java.net.URLClassLoader.getURLs
                Method getURLsMethod = classLoaderClass.getMethod("getURLs");
                return (URL[]) getURLsMethod.invoke(classLoader);
            }
        } catch (ClassNotFoundException ex) {
            // no-op
        }

        try {
            // Java 9+
            Class<?> classLoaderClass = Class.forName(BUILTIN_CLASS_LOADER_CLASS);
            if (classLoaderClass.isInstance(classLoader)) {
                Unsafe unsafe = getUnsafe();

                // jdk.internal.loader.BuiltinClassLoader.ucp
                Field ucpField = classLoaderClass.getDeclaredField("ucp");
                long ucpFieldOffset = unsafe.objectFieldOffset(ucpField);
                Object ucpObject = unsafe.getObject(classLoader, ucpFieldOffset);

                // jdk.internal.loader.URLClassPath.path
                Field pathField = ucpField.getType().getDeclaredField("path");
                long pathFieldOffset = unsafe.objectFieldOffset(pathField);
                ArrayList<URL> path = (ArrayList<URL>) unsafe.getObject(ucpObject, pathFieldOffset);
                return path.toArray(new URL[0]);
            }
        } catch (ClassNotFoundException ex) {
            // no-op
        }

        throw new UnsupportedOperationException("Unsupported ClassLoader");
    }

    private static Field getField(Field[] fields, String... names) throws NoSuchFieldException {
        for (Field field : fields) {
            for (String name : names) {
                if (field.getName().equals(name)) {
                    return field;
                }
            }
        }

        throw new NoSuchFieldException(String.join(", ", names));
    }

    private static Unsafe getUnsafe() throws Throwable {
        // sun.misc.Unsafe.theUnsafe
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        return (Unsafe) unsafeField.get(null);
    }

}
