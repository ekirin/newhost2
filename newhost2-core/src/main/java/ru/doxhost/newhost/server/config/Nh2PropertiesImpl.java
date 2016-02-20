package ru.doxhost.newhost.server.config;

import java.util.Properties;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.inject.Binder;
import com.google.inject.name.Names;
import ru.doxhost.newhost.server.lib.Nh2Mode;

public class Nh2PropertiesImpl implements Nh2Properties {

    private static final Logger logger = LoggerFactory.getLogger(Nh2PropertiesImpl.class);

    private Nh2Mode mode;
    
    private String contextPath = "";

    private final String ERROR_KEY_NOT_FOUND = "Key %s does not exist. Please include it in your application.conf. Otherwise this app will not work";

    /**
     * This is the final configuration holding all information from 
     * 1. application.conf. 
     * 2. Special properties for the mode you are running on extracted 
     *    from application.conf 
     * 3. An external configuration file defined
     *    by a system property and on the classpath.
     */
    private CompositeConfiguration compositeConfiguration;

    public Nh2PropertiesImpl(
            Nh2Mode mode) {
        
        this.mode = mode;

        // This is our main configuration.
        // In the following we'll read the individual configurations and merge
        // them into the composite configuration at the end.
        compositeConfiguration = new CompositeConfiguration();

        // That is the default config.
        PropertiesConfiguration defaultConfiguration = null;

        // Config of prefixed mode corresponding to current mode (eg.
        // %TEST.myproperty=...)
        Configuration prefixedDefaultConfiguration = null;

        // (Optional) Config set via a system property
        PropertiesConfiguration externalConfiguration = null;

        // (Optional) Config of prefixed mode corresponding to current mode (eg.
        // %TEST.myproperty=...)
        Configuration prefixedExternalConfiguration = null;

        // First step => load application.conf and also merge properties that
        // correspond to a mode into the configuration.

        defaultConfiguration = Nh2Conf.loadConfigurationInUtf8(CONF_FILE_LOCATION_BY_CONVENTION);

        if (defaultConfiguration != null) {
            // Second step:
            // Copy special prefix of mode to parent configuration
            // By convention it will be something like %TEST.myproperty
            prefixedDefaultConfiguration = defaultConfiguration.subset("%" + mode.name());

            // allow application.conf to be reloaded on changes in DEV mode
            if (Nh2Mode.DEV == mode) {
                defaultConfiguration.setReloadingStrategy(new FileChangedReloadingStrategy());
            }

        } else {

            // If the property was set, but the file not found we emit
            // a RuntimeException
            String errorMessage = String
                    .format("Error reading configuration file. Make sure you got a default config file %s",
                            CONF_FILE_LOCATION_BY_CONVENTION);

            logger.error(errorMessage);

            throw new RuntimeException(errorMessage);
        }

        // third step => load external configuration when a system property is
        // defined.
        String NH2ExternalConf = System.getProperty(NH2_EXTERNAL_CONF);

        if (NH2ExternalConf != null) {

            // only load it when the property is defined.

            externalConfiguration = Nh2Conf.loadConfigurationInUtf8(NH2ExternalConf);

            // this should not happen:
            if (externalConfiguration == null) {

                String errorMessage = String
                        .format("NH2 was told to use an external configuration%n"
                                + " %s = %s %n."
                                + "But the corresponding file cannot be found.%n"
                                + " Make sure it is visible to this application and on the classpath.",
                                NH2_EXTERNAL_CONF, NH2ExternalConf);

                logger.error(errorMessage);

                throw new RuntimeException(errorMessage);

            } else {

                // allow the external configuration to be reloaded at
                // runtime based on detected file changes
                final boolean shouldReload = Boolean.getBoolean(NH2_EXTERNAL_RELOAD);
                if (shouldReload) {
                    externalConfiguration
                            .setReloadingStrategy(new FileChangedReloadingStrategy());
                }

                // Copy special prefix of mode to parent configuration
                // By convention it will be something like %TEST.myproperty
                prefixedExternalConfiguration = externalConfiguration
                        .subset("%" + mode.name());
            }

        }

        // /////////////////////////////////////////////////////////////////////
        // Finally add the stuff to the composite configuration
        // Note: Configurations added earlier will overwrite configurations
        // added later.
        // /////////////////////////////////////////////////////////////////////

        if (prefixedExternalConfiguration != null) {
            compositeConfiguration
                    .addConfiguration(prefixedExternalConfiguration);
        }

        if (externalConfiguration != null) {
            compositeConfiguration.addConfiguration(externalConfiguration);
        }

        if (prefixedDefaultConfiguration != null) {
            compositeConfiguration
                    .addConfiguration(prefixedDefaultConfiguration);
        }

        if (defaultConfiguration != null) {
            compositeConfiguration.addConfiguration(defaultConfiguration);
        }
    }

    @Override
    public String get(String key) {

        String value;

        try {
            value = compositeConfiguration.getString(key);
        } catch (Exception e) {
            // Fail silently because we handle errors differently. Simply set
            // them null.
            value = null;
        }

        return value;

    }

    @Override
    public String getOrDie(String key) {

        String value = get(key);

        if (value == null) {
            logger.error(String.format(ERROR_KEY_NOT_FOUND, key));
            throw new RuntimeException(String.format(ERROR_KEY_NOT_FOUND, key));
        } else {
            return value;
        }

    }

    @Override
    public Integer getInteger(String key) {

        Integer value;

        try {
            value = compositeConfiguration.getInt(key);
        } catch (Exception e) {
            // Fail silently because we handle errors differently. Simply set
            // them null.
            value = null;
        }

        return value;

    }

    @Override
    public Integer getIntegerOrDie(String key) {

        Integer value = getInteger(key);

        if (value == null) {
            logger.error(String.format(ERROR_KEY_NOT_FOUND, key));
            throw new RuntimeException(String.format(ERROR_KEY_NOT_FOUND, key));
        } else {
            return value;
        }

    }

    @Override
    public Boolean getBooleanOrDie(String key) {

        Boolean value = getBoolean(key);

        if (value == null) {
            logger.error(String.format(ERROR_KEY_NOT_FOUND, key));
            throw new RuntimeException(String.format(ERROR_KEY_NOT_FOUND, key));
        } else {
            return value;
        }

    }

    @Override
    public Boolean getBoolean(String key) {

        Boolean value;

        try {
            value = compositeConfiguration.getBoolean(key);
        } catch (Exception e) {
            // Fail silently because we handle errors differently. Simply set
            // them null.
            value = null;
        }

        return value;

    }

    public void setProperty(String key, String value) {
        compositeConfiguration.setProperty(key, value);
    }

    public void bindProperties(Binder binder) {
        Names.bindProperties(binder,
                ConfigurationConverter.getProperties(compositeConfiguration));
    }

    @Override
    public boolean isProd() {
        return (mode.equals(Nh2Mode.PROD));
    }

    @Override
    public boolean isDev() {
        return (mode.equals(Nh2Mode.DEV));
    }

    @Override
    public boolean isTest() {
        return (mode.equals(Nh2Mode.TEST));
    }
    
    /**
     * Get the context path on which the application is running
     * 
     * That means:
     * - when running on root the context path is empty
     * - when running on context there is NEVER a trailing slash
     * 
     * We conform to the following rules:
     * Returns the portion of the request URI that indicates the context of the 
     * request. The context path always comes first in a request URI. 
     * The path starts with a "/" character but does not end with a "/" character. 
     * For servlets in the default (root) context, this method returns "". 
     * The container does not decode this string.
     * 
     * As outlined by: http://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html#getContextPath()
     * 
     * @return the context-path with a leading "/" or "" if running on root
     */
    @Override
    public String getContextPath() {
        
        return contextPath;
        
    }
    
    @Override
    public void setContextPath(String contextPath) {
        
        this.contextPath = contextPath;
        
    }

    @Override
    public Properties getAllCurrentNH2Properties() {

        return ConfigurationConverter.getProperties(compositeConfiguration);

    }

    @Override
    public String[] getStringArray(String key) {
        String value = compositeConfiguration.getString(key);
        if (value != null) {
            return Iterables.toArray(Splitter.on(",").trimResults()
                    .omitEmptyStrings().split(value), String.class);
        } else {
            return null;
        }
    }

    @Override
    public String getWithDefault(String key, String defaultValue) {
        String value = get(key);
        if (value != null) {
            return value;
        } else {
            return defaultValue;
        }
    }

    @Override
    public Integer getIntegerWithDefault(String key, Integer defaultValue) {
        Integer value = getInteger(key);
        if (value != null) {
            return value;
        } else {
            return defaultValue;
        }
    }

    @Override
    public Boolean getBooleanWithDefault(String key, Boolean defaultValue) {
        Boolean value = getBoolean(key);
        if (value != null) {
            return value;
        } else {
            return defaultValue;
        }
    }
    
}
