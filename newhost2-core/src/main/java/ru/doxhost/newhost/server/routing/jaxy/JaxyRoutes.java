package ru.doxhost.newhost.server.routing.jaxy;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import ru.doxhost.newhost.server.config.Nh2Config;
import ru.doxhost.newhost.server.lib.Nh2Mode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class JaxyRoutes {

    private static final Logger logger = LoggerFactory.getLogger(JaxyRoutes.class);

    private final Nh2Mode mode = Nh2Config.getConf().isTest() ? Nh2Mode.TEST :
            Nh2Config.getConf().isProd() ? Nh2Mode.PROD : Nh2Mode.DEV;

    private Reflections reflections;

    private Set<Method> methods = Sets.newHashSet();

    private Map<Class<?>, Set<String>> controllers = Maps.newHashMap();;

    private JaxyPathRegister jaxyRegister;


    /**
     * Scans, identifies, and registers annotated controller methods for the
     * current runtime settings.
     *
     * @param jaxyRegister
     */
    public void findJaxyPaths(final JaxyPathRegister jaxyRegister) {

        this.jaxyRegister = jaxyRegister;

        configureReflections();

        processFoundMethods();

        sortMethods();

        registerMethods();
    }

    /**
     * Takes all methods and registers them at the controller using the path: Class:@Path + Method:@Path.
     * If no @Path Annotation is present at the method just the Class:@Path is used.
     */
    private void registerMethods() {
        // register routes for all the methods
        for (Method method : methods) {

            final Class<?> controllerClass = method.getDeclaringClass();
            final Path methodPath = method.getAnnotation(Path.class);
            final Set<String> controllerPaths = controllers.get(controllerClass);

            String[] paths = {""};
            if (methodPath != null) {
                paths = methodPath.value();
            }

            if (controllerPaths.size() != 0) {
                for (String controllerPath : controllerPaths) {

                    for (String methodPathSpec : paths) {

                        final io.vertx.core.http.HttpMethod httpMethod = getHttpMethod(method);
                        final String fullPath = controllerPath + methodPathSpec;
                        final String methodName = method.getName();

                        jaxyRegister.register(httpMethod, fullPath, controllerClass, methodName);
                    }

                }
            } else {
                for (String methodPathSpec : paths) {
                    final io.vertx.core.http.HttpMethod httpMethod = getHttpMethod(method);
                    final String fullPath = methodPathSpec;
                    final String methodName = method.getName();

                    jaxyRegister.register(httpMethod, fullPath, controllerClass, methodName);
                }
            }

        }
    }

    /**
     * Takes the found methods and checks if they have a valid format.
     * If they do, the controller path classes for these methods are generated
     */
    private void processFoundMethods() {
        for (Method method : findControllerMethods()) {
            if (allowMethod(method)) {

                // add the method to our todo list
                methods.add(method);

                // generate the paths for the controller class
                final Class<?> controllerClass = method.getDeclaringClass();

                if (!controllers.containsKey(controllerClass)) {

                    Set<String> paths = collectPaths(controllerClass);
                    controllers.put(controllerClass, paths);

                }
            }
        }
    }

    /**
     * Sorts the methods into registration order
     */
    private void sortMethods() {
        List<Method> methodList = new ArrayList<>(methods);

        Collections.sort(methodList, (m1, m2) -> {
            int o1 = Integer.MAX_VALUE;
            if (m1.isAnnotationPresent(Order.class)) {
                Order order = m1.getAnnotation(Order.class);
                o1 = order.value();
            }

            int o2 = Integer.MAX_VALUE;
            if (m2.isAnnotationPresent(Order.class)) {
                Order order = m2.getAnnotation(Order.class);
                o2 = order.value();
            }

            if (o1 == o2) {
                // same or unsorted, compare controller+method
                String s1 = m1.getDeclaringClass().getName() + "."
                        + m1.getName();
                String s2 = m2.getDeclaringClass().getName() + "."
                        + m2.getName();
                return s1.compareTo(s2);
            }

            if (o1 < o2) {
                return -1;
            } else {
                return 1;
            }
        });

        methods = Collections.unmodifiableSet(new LinkedHashSet<>(methodList));
    }

    @SuppressWarnings("unchecked")
    private Set<Method> findControllerMethods() {
        Set<Method> methods = Sets.newLinkedHashSet();

        methods.addAll(reflections.getMethodsAnnotatedWith(Path.class));
        Reflections annotationReflections = new Reflections("", new TypeAnnotationsScanner(), new SubTypesScanner());
        for (Class<?> httpMethod : annotationReflections.getTypesAnnotatedWith(HttpMethod.class)) {
            if (httpMethod.isAnnotation()) {
                methods.addAll(reflections.getMethodsAnnotatedWith((Class<? extends Annotation>) httpMethod));
            }
        }

        return methods;
    }

    private void configureReflections() {

        ConfigurationBuilder builder = new ConfigurationBuilder();

        Set<URL> packagesToScan = getPackagesToScanForRoutes();
        builder.addUrls(packagesToScan);

        builder.addScanners(new MethodAnnotationsScanner());
        reflections = new Reflections(builder);
    }

    private Set<String> collectPaths(Class<?> controllerClass) {
        Set<String> parentPaths = Collections.emptySet();
        if (controllerClass.getSuperclass() != null) {
            parentPaths = collectPaths(controllerClass.getSuperclass());
        }

        Set<String> paths = Sets.newLinkedHashSet();
        Path controllerPath = controllerClass.getAnnotation(Path.class);

        if (controllerPath == null) {
            return parentPaths;
        }

        if (parentPaths.isEmpty()) {

            // add all controller paths
            paths.addAll(Arrays.asList(controllerPath.value()));

        } else {

            // create controller paths based on the parent paths
            for (String parentPath : parentPaths) {

                for (String path : controllerPath.value()) {
                    paths.add(parentPath + path);
                }

            }

        }

        return paths;
    }

    public Set<URL> getPackagesToScanForRoutes() {

        Set<URL> packagesToScanForRoutes = Sets.newHashSet();

        packagesToScanForRoutes.addAll(ClasspathHelper.forPackage(Nh2Config.packageJaxy()));
        packagesToScanForRoutes.addAll(ClasspathHelper.forPackage("ru.doxhost")); // TODO - need more independent package

        return packagesToScanForRoutes;
    }

    private boolean allowMethod(Method method) {

        if (method.isAnnotationPresent(Requires.class)) {
            String key = method.getAnnotation(Requires.class).value();
            String value = Nh2Config.getConf().get(key);
            if (value == null) {
                return false;
            }
        }

        Set<Nh2Mode> modes = Sets.newTreeSet();

        for (Annotation annotation : method.getAnnotations()) {

            Class<? extends Annotation> annotationClass = annotation
                    .annotationType();

            if (annotationClass.isAnnotationPresent(RuntimeMode.class)) {

                RuntimeMode mode = annotationClass.getAnnotation(RuntimeMode.class);
                modes.add(mode.value());

            }
        }

        return modes.isEmpty() || modes.contains(mode);
    }

    private io.vertx.core.http.HttpMethod getHttpMethod(Method method) {

        for (Annotation annotation : method.getAnnotations()) {

            Class<? extends Annotation> annotationClass = annotation.annotationType();

            if (annotationClass.isAnnotationPresent(HttpMethod.class)) {
                HttpMethod httpMethod = annotationClass.getAnnotation(HttpMethod.class);
                return httpMethod.value();
            }

        }

        // default to GET
        logger.info(String.format("%s.%s does not specify an HTTP method annotation! Defaulting to GET.",
                method.getClass().getName(), method.getName()));

        return io.vertx.core.http.HttpMethod.GET;
    }
}