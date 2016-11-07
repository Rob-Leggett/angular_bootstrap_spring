package au.com.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class CopyUtils {
    private static Logger log = LoggerFactory.getLogger(CopyUtils.class);

    private static final String METHOD_CLONE = "clone";

    @SuppressWarnings("unchecked")
    public static <T extends Cloneable> T clone(T orig) {
        T obj = null;

        if (orig != null) {
            try {
                obj = (T) orig.getClass().getMethod(METHOD_CLONE).invoke(orig);
            } catch (Exception e) {
                log.error("The clone failed: {}: {}", e.getClass().getName(), e.getMessage());
            }
        }

        return obj;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Collection<U>, U extends Cloneable> T cloneCollection(
            T orig) {
        return (T) cloneCollection(orig.getClass(), orig);
    }

    public static <I extends Collection<U>, O extends Collection<U>, U extends Cloneable> O cloneCollection(
            Class<O> outType, I orig) {
        O obj = null;

        if (orig != null) {
            try {
                obj = outType.getConstructor().newInstance();

                for (U element : orig) {
                    obj.add(clone(element));
                }
            } catch (Exception e) {
                log.error("The cloneCollection failed: {}: {}", e.getClass().getName(), e.getMessage());
            }
        }

        return obj;
    }
}
