package ru.doxhost.newhost.server.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

public class Nh2ModeHelper {
    
    public static Logger logger = LoggerFactory.getLogger(Nh2ModeHelper.class);

    public static Optional<Nh2Mode> determineModeFromSystemProperties() {
        
        Nh2Mode mode = null;
        
        // Get mode possibly set via a system property
        String modeFromGetSystemProperty = System.getProperty(Nh2ModeConst.MODE_KEY_NAME);
        
        // If the user specified a mode we set the mode accordingly:
        if (modeFromGetSystemProperty != null) {

            if (modeFromGetSystemProperty.equals(Nh2ModeConst.MODE_TEST)) {
                mode = Nh2Mode.TEST;
                
            } else if (modeFromGetSystemProperty.equals(Nh2ModeConst.MODE_DEV)) {
                mode = Nh2Mode.DEV;
                
            } else if (modeFromGetSystemProperty.equals(Nh2ModeConst.MODE_PROD)) {
                mode = Nh2Mode.PROD;
            }

        }
        return Optional.fromNullable(mode);
    }

    public static Nh2Mode determineModeFromSystemPropertiesOrProdIfNotSet() {
        
        Optional<Nh2Mode> modeOptional = determineModeFromSystemProperties();

        Nh2Mode mode;
        
        if (!modeOptional.isPresent()) {
            mode = Nh2Mode.PROD;
        } else {
            mode = modeOptional.get();
        }
        
        logger.info("newhost is running in mode {}", mode.toString());
        
        return mode;
    }
}
