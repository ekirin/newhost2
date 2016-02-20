package ru.doxhost.newhost.server.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author Eugene Kirin on 12.11.2015.
 */
public class Nh2Conf {

    private static final Logger logger = LoggerFactory.getLogger(Nh2Conf.class);

    /**
     * This is important: We load stuff as UTF-8.
     *
     * We are using in the default Apache Commons loading mechanism.
     *
     * With two little tweaks: 1. We don't accept any delimimter by default 2.
     * We are reading in UTF-8
     *
     * More about that:
     * http://commons.apache.org/configuration/userguide/howto_filebased
     * .html#Loading
     *
     * From the docs: - If the combination from base path and file name is a
     * full URL that points to an existing file, this URL will be used to load
     * the file. - If the combination from base path and file name is an
     * absolute file name and this file exists, it will be loaded. - If the
     * combination from base path and file name is a relative file path that
     * points to an existing file, this file will be loaded. - If a file with
     * the specified name exists in the user's home directory, this file will be
     * loaded. - Otherwise the file name is interpreted as a resource name, and
     * it is checked whether the data file can be loaded from the classpath.
     *
     * @param fileOrUrlOrClasspathUrl Location of the file. Can be on file
     * system, or on the classpath. Will both work.
     * @return A PropertiesConfiguration or null if there were problems getting
     * it.
     */
    public static PropertiesConfiguration loadConfigurationInUtf8(String fileOrUrlOrClasspathUrl) {

        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.setEncoding(StandardCharsets.UTF_8.toString());
        propertiesConfiguration.setDelimiterParsingDisabled(true);
        propertiesConfiguration.setFileName(fileOrUrlOrClasspathUrl);

        try {

            propertiesConfiguration.load(fileOrUrlOrClasspathUrl);

        } catch (ConfigurationException e) {

            logger.info("Could not load file " + fileOrUrlOrClasspathUrl
                    + " (not a bad thing necessarily, but I am returing null)");

            return null;
        }

        return propertiesConfiguration;
    }
}
