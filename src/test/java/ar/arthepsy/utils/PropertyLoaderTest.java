package ar.arthepsy.utils;

import eu.arthepsy.utils.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Properties;

@Test
public class PropertyLoaderTest {

    @Test
    public void testLoadingProperties() {
        Properties properties = PropertyLoader.getProperties("app");
        Assert.assertNotNull(properties);
    }

    @Test
    public void testLoadingPropertyValue() {
        Properties properties = PropertyLoader.getProperties("app");
        Assert.assertNotNull(properties);
        String value = properties.getProperty("test.property");
        Assert.assertEquals(value, "123 free");
    }

}
