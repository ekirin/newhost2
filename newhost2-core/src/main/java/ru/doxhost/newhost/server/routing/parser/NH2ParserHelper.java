
package ru.doxhost.newhost.server.routing.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.CaseFormat;
import com.google.common.primitives.Primitives;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class NH2ParserHelper {

    private static final Logger logger = LoggerFactory.getLogger(NH2ParserHelper.class);

    private static final Map<String, Method> CONVERTERS = new HashMap<>();

    static {
        Method[] methods = Converter.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getParameterTypes().length == 1
                    && method.getParameterTypes()[0] == String.class) {
                
                CONVERTERS.put(method.getReturnType().getName(), method);
            }
        }
    }

    /**
     * Returns the lower class name. Eg. A class named MyObject will become
     * "myObject".
     *
     * @param object Object for which to return the lowerCamelCaseName
     * @return the lowerCamelCaseName of the Object
     */
    public static String getRealClassNameLowerCamelCase(Object object) {

        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, object.getClass().getSimpleName());

    }

    /**
     * Convert value to class type value.
     * 
     * If something goes wrong it returns null.
     *
     * @param from string value
     * @param to type of the class
     * @return class type value or null if something goes wrong.
     */
    public static <T> T convert(String from, Class<T> to) {
       
        Class<T> toAsNonPrimitiveType;
        
        if (from == null) {
            return null;
        }
                      
        T t = null;
         
        toAsNonPrimitiveType = Primitives.wrap(to);

        if (toAsNonPrimitiveType.isAssignableFrom(from.getClass())) {
            return toAsNonPrimitiveType.cast(from);
        }

        Method converter = CONVERTERS.get(toAsNonPrimitiveType.getName());
        
        if (converter == null) {
            
            logger.error("No converter found to convert {}. " + "Returning null. " + "You may want to extend the class." + toAsNonPrimitiveType);
            
        } else {
            
            try {

                t = toAsNonPrimitiveType.cast(converter.invoke(toAsNonPrimitiveType, from));
                
            } catch (IllegalAccessException 
                    | IllegalArgumentException 
                    | InvocationTargetException ex) {

                logger.error(
                        "Cannot convert from "
                        + from.getClass().getName() + " to " + toAsNonPrimitiveType.getName()
                        + ". Conversion failed with " + ex.getMessage(), ex);
            }

        }
        
         return t;

    }

    private static class Converter {

        public static Integer toInteger(String value) {
            return Integer.valueOf(value);
        }

        public static Long toLong(String value) {
            return Long.valueOf(value);
        }

        public static Float toFloat(String value) {
            return Float.valueOf(value);
        }

        public static Double toDouble(String value) {
            return Double.valueOf(value);
        }

        public static Boolean toBoolean(String value) {
            return Boolean.valueOf(value);
        }

        public static Byte toByte(String value) {
            return Byte.valueOf(value);
        }

        public static Short toShort(String value) {
            return Short.valueOf(value);
        }

        public static Date toDate(String value) {
            if (value != null && value.length() > 0) {
                LocalDateTime localDateTime = LocalDateTime.parse(value);
                return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            } else {
                return null;
            }
        }

        public static Character toCharacter(String value) {

            if (value.length() > 0) {
                return value.charAt(0);
            } else {
                return null;
            }

        }
    }

}
