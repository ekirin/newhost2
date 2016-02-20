package ru.doxhost.newhost.server.config;

import java.util.Properties;

public interface Nh2Properties {

    /**
     * Often you may want to use a separate configuration file. You can define
     * the file to use in addition to your default config at
     * conf/application.conf by setting NH2_EXTERNAL_CONF as JVM system
     * property.
     * 
     * You must make sure that the specified file is on the classpath of the
     * application. Eg servlet containers usually have a special directory that
     * gets included into the classpath. That makes using different
     * configurations for different environments quite simple.
     * 
     * We don't use file IO to get the file because we would most likely run
     * into security issues easily.
     * 
     */
    String NH2_EXTERNAL_CONF = "newhost2.external.configuration";

    /**
     * The System property used to enable hot-reloading of the external
     * configuration file at runtime.
     *
     */
    String NH2_EXTERNAL_RELOAD = "newhost2.external.reload";

    /**
     * The default configuration. Make sure that file exists. Otherwise the
     * application won't start up.
     */
    String CONF_FILE_LOCATION_BY_CONVENTION = "conf.properties";

    /**
     * Get a String property or null if it is not there...
     * 
     * @param key
     * @return the property of null if not there
     */
    String get(String key);

    /**
     * Get a String property or a default value when property cannot be found in
     * any configuration file.
     * 
     * @param key
     *            the key used in the configuration file.
     * @param defaultValue
     *            Default value returned, when value cannot be found in
     *            configuration.
     * @return the value of the key or the default value.
     */
    String getWithDefault(String key, String defaultValue);

    /**
     * Get a property as Integer of null if not there / or property no integer
     * 
     * @param key
     * @return the property or null if not there or property no integer
     */
    Integer getInteger(String key);

    /**
     * Get a Integer property or a default value when property cannot be found
     * in any configuration file.
     * 
     * @param key
     *            the key used in the configuration file.
     * @param defaultValue
     *            Default value returned, when value cannot be found in
     *            configuration.
     * @return the value of the key or the default value.
     */
    Integer getIntegerWithDefault(String key, Integer defaultValue);

    /**
     * 
     * @param key
     * @return the property or null if not there or property no boolean
     */
    Boolean getBoolean(String key);

    /**
     * Get a Boolean property or a default value when property cannot be found
     * in any configuration file.
     * 
     * @param key
     *            the key used in the configuration file.
     * @param defaultValue
     *            Default value returned, when value cannot be found in
     *            configuration.
     * @return the value of the key or the default value.
     */
    Boolean getBooleanWithDefault(String key, Boolean defaultValue);

    /**
     * The "die" method forces this key to be set. Otherwise a runtime exception
     * will be thrown.
     * 
     * @param key
     * @return the boolean or a RuntimeException will be thrown.
     */
    Boolean getBooleanOrDie(String key);

    /**
     * The "die" method forces this key to be set. Otherwise a runtime exception
     * will be thrown.
     * 
     * @param key
     * @return the Integer or a RuntimeException will be thrown.
     */
    Integer getIntegerOrDie(String key);

    /**
     * The "die" method forces this key to be set. Otherwise a runtime exception
     * will be thrown.
     * 
     * @param key
     * @return the String or a RuntimeException will be thrown.
     */
    String getOrDie(String key);

    /**
     * eg. key=myval1,myval2
     * 
     * Delimiter is a comma "," as outlined in the example above.
     * 
     * @return an array containing the values of that key or null if not found.
     */
    String[] getStringArray(String key);

    /**
     * Whether we are in DEV mode
     * 
     * @return True if we are in DEV mode
     */
    boolean isDev();

    /**
     * Whether we are in TEST mode
     * 
     * @return True if we are in TEST mode
     */
    boolean isTest();

    /**
     * Whether we are in PROD mode
     * 
     * @return True if we are in PROD mode
     */
    boolean isProd();
    
    /**
     * Returns context under which the application is currently running.
     * For instance on Java application servers it is common to use something like
     * http://www.myserver.com/myapplication/index.html - where myapplication
     * would be set by the applcation server.
     * 
     * We need the context to generate for instance correct reversed routes.
     * 
     * @return The context or "" if empty.
     */
    String getContextPath();
    
    void setContextPath(String contextPath);

    /**
     * 
     * @return All properties that are currently loaded from internal and
     *         external files
     */
    Properties getAllCurrentNH2Properties();
}
