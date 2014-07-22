package no.ntnu.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import static java.lang.Class.forName;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Clazz {

    private static final Map<Class, Class> wrappers = Col.newMap();

    static {
        wrappers.put(Boolean.class, boolean.class);
        wrappers.put(Character.class, char.class);
        wrappers.put(Byte.class, byte.class);
        wrappers.put(Short.class, short.class);
        wrappers.put(Integer.class, int.class);
        wrappers.put(Long.class, long.class);
        wrappers.put(Float.class, float.class);
        wrappers.put(Double.class, double.class);
        wrappers.put(Void.class, void.class);
    }

    public static Object invoke(Object src, String methodName) throws RuntimeException {
        return invoke(src, methodName, null);
    }

    public static Object invoke(Object src, String methodName, Object... args) throws RuntimeException {
        Class[] argsClasses = getArgsClasses(args, false);
        try {
            return invokeImpl(src, methodName, argsClasses, args);
        } catch (NoSuchMethodException e) {
            //System.err.println("trying primitive wrappers...");
            try {
                return invokeImpl(src, methodName, getArgsClasses(args, true), args);
            } catch (Throwable e1) {
                //e1.printStackTrace();
                throw new RuntimeException(e);
            }
//            e.printStackTrace();
        } catch (Throwable e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static Object invokeImpl(Object src, String methodName, Class[] argsClasses, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = null;
        try {
            method = src.getClass().getMethod(methodName, argsClasses);
        } catch (NoSuchMethodException e) {
            //e.printStackTrace();
            Method[] methods = src.getClass().getMethods();
            for (Method m : methods) {
                if (m.getName().equals(methodName)) {
                    Class<?>[] params = m.getParameterTypes();
                    for (int i = 0; i < params.length && params.length == args.length; i++) {
                        String paramName = params[i].getClass().getName();
                        if (paramName.equals(argsClasses[i].getClass().getName())
                                || paramName.equals(argsClasses[i].getSuperclass())
                                ) {
                            //System.out.println("Invoking "+m.getName()+" "+ Arrays.toString(m.getParameterTypes()));
                            return m.invoke(src, args);
                        }
                    }
                }
            }
            throw e;
        }
        return method.invoke(src, args);
    }

    private static Class[] getArgsClasses(Object[] args, boolean useWrappers) {
        Class[] argsClasses = null;
        if (args != null && args.length > 0) {
            argsClasses = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                if (useWrappers && wrappers.containsKey(args[i].getClass())) {
                    argsClasses[i] = wrappers.get(args[i].getClass());
                } else
                    argsClasses[i] = args[i].getClass();
            }
        }
        return argsClasses;
    }

    public static <E> E instance(String name, Arguments args) {
        return instance(name, null, args);
    }

    public static <E> E instantiate(String name) {
        return instance(name);
    }

    public static <E> E instance(String name) {
        return instance(name, null, null);
    }

    public static <E> E instance(String name, String defaultClass) {
        return instance(name, defaultClass, null);
    }

    public static <E> E instance(String name, String defaultClass, Arguments args) {
        try {
            if (Debug.VERBOSE) {
                String defclassDebugStr = defaultClass != null ? ", defaulClass=".concat(defaultClass) : "";
                Debug.d("name " + name + defclassDebugStr);
            }
            if (name == null && defaultClass != null) return instance(defaultClass, null, args);
            final Class<E> c = (Class<E>) forName(name);
            if (args != null)
                return c.getConstructor(args.argsClasses).newInstance(args.args);
            return c.newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
            System.err.print(name + " is not available.");
            if (defaultClass != null) {
                System.err.println(" Using default class: " + defaultClass);
                return instance(defaultClass, null, args);
            } else System.err.println();
        }
        return null;
    }

    public static class Arguments {
        private Class[] argsClasses;
        public Object[] args;

        public Arguments(Object... args) throws NullPointerException {
            this.args = args;
            Collection<Class<? extends Object>> c = new LinkedList<Class<? extends Object>>();
            for (Object o : args)
                c.add(o.getClass());
            this.argsClasses = c.toArray(new Class[c.size()]);
        }
    }
}
