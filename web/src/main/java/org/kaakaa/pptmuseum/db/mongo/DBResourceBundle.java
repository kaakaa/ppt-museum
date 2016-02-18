package org.kaakaa.pptmuseum.db.mongo;

import net.rakugakibox.util.YamlResourceBundle;

import java.util.ResourceBundle;

/**
 * Created by kaakaa on 16/02/16.
 */
public class DBResourceBundle {
    /**
     * resource
     */
    private static final ResourceBundle bundle = ResourceBundle.getBundle("mongo", YamlResourceBundle.Control.INSTANCE);

    /**
     * <p>get property value in String format</p>
     *
     * @param key perperty key
     * @return property value in String format
     */
    public static String getString(String key) {
        return bundle.getString(key);
    }

    /**
     * <p>get propertye value</p>
     *
     * @param key property key
     * @return propertye value
     */
    public static Object get(String key) {
        return bundle.getObject(key);
    }
}
