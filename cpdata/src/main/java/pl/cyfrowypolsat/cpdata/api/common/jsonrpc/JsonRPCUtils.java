package pl.cyfrowypolsat.cpdata.api.common.jsonrpc;

import java.lang.annotation.Annotation;

final class JsonRPCUtils {
    private JsonRPCUtils() {
        throw new AssertionError("No instances");
    }

    /**
     * Returns an instance of {@code cls} if {@code annotations} contains an instance.
     */
    static <T extends Annotation> T findAnnotation(Annotation[] annotations,
                                                   Class<T> cls) {
        for (Annotation annotation : annotations) {
            if (cls.isInstance(annotation)) {
                //noinspection unchecked
                return (T) annotation;
            }
        }
        return null;
    }
}