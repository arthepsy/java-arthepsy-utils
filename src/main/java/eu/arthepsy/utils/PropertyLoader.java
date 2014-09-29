package eu.arthepsy.utils;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public abstract class PropertyLoader {

    private static final String EXTENSION = ".properties";

    public static Properties getProperties(final String resourceName) {
        return loadProperties(false, resourceName, getDefaultClassLoader());
    }
    public static Properties getProperties(final String resourceName, ClassLoader loader) {
        return loadProperties(false, resourceName, ensureClassLoader(loader));
    }

    public static Properties getResourceBundleProperties(final String resourceName) {
        return loadProperties(true, resourceName, getDefaultClassLoader());
    }
    public static Properties getResourceBundleProperties(final String resourceName, ClassLoader loader) {
        return loadProperties(true, resourceName, ensureClassLoader(loader));
    }

    // properties
    private static Properties loadProperties(final boolean resourceBundle, String resourceName, ClassLoader classLoader) throws IllegalArgumentException {
        if (resourceName == null) {
            throw new IllegalArgumentException("resourceName is null");
        }
        if (classLoader == null) {
            throw new IllegalArgumentException("classLoader is null");
        }
        if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
        }
        if (resourceName.endsWith(EXTENSION)) {
            resourceName = resourceName.substring(0, resourceName.length() - EXTENSION.length());
        }
        Properties properties = null;
        InputStream stream = null;
        try {
            if (resourceBundle) {
                resourceName = resourceName.replace ('/', '.');
                final ResourceBundle rb = ResourceBundle.getBundle(resourceName, Locale.getDefault(), classLoader);
                properties = new Properties();
                for (Enumeration keys = rb.getKeys(); keys.hasMoreElements();) {
                    final String key = (String)keys.nextElement();
                    final String value = rb.getString(key);
                    properties.put(key, value);
                }
            } else {
                resourceName = resourceName.replace ('.', '/');
                if (! resourceName.endsWith(EXTENSION)) {
                    resourceName = resourceName.concat(EXTENSION);
                }

                stream = classLoader.getResourceAsStream(resourceName);
                // classLoader = Thread.currentThread().getContextClassLoader();
                // stream = classLoader.getResourceAsStream(resourceName);

                if (stream != null) {
                    properties = new Properties();
                    properties.load(stream);
                }
            }
        } catch (Exception e) {
            properties = null;
        } finally {
            if (stream != null) {
                try { stream.close(); } catch (Throwable ignore) {}
            }
        }
        return properties;
    }

    private static ClassLoader getDefaultClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private static ClassLoader ensureClassLoader(ClassLoader loader) {
        return (loader == null ? ClassLoader.getSystemClassLoader() : loader);
    }

}