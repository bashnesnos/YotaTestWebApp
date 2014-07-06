/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sml.test.ejb;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import sml.test.model.Property;
import sml.test.model.PropertyBuilder;

/**
 *
 * @author asemelit
 */
public class PropertyFacadeTest {
    private static EJBContainer container;
    private static PropertyFacadeLocal instance;
    
    public PropertyFacadeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws NamingException {
        Map<String, Object> props = new HashMap<>();  
        File packagedWar = new File("./build/resources/test/YotaTestWebApp.war");
        if (packagedWar.exists()) { //this should be running from Gralde then
            props.put(EJBContainer.MODULES, new File[]{packagedWar});  
            container = javax.ejb.embeddable.EJBContainer.createEJBContainer(props);
            instance = (PropertyFacadeLocal)container.getContext().lookup("java:global/YotaTestWebApp/PropertyFacade");
        }
        else { //local netbeans stuff
            System.out.println("./build/resources/test/YotaTestWebApp.war not found; using default glassfish domain");
            container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
            instance = (PropertyFacadeLocal)container.getContext().lookup("java:global/classes/PropertyFacade");
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        container.close();
    }

    /**
     * Test of create method, of class PropertyFacade.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("Create");
        Property prop = new PropertyBuilder()
                .setName("prop1")
                .child("child1", null)
                .child("child11", null)
                .toProperty();
        instance.create(prop);
        assertTrue(instance.findByName("prop1").size() == 1);
        Property prop2 = new PropertyBuilder()
                .setName("prop2")
                .child("child2", null)
                .child("child21", null)
                .toProperty();
        instance.create(prop2);
        assertTrue(instance.findByName("prop2").size() == 1);
        assertTrue(instance.findAllTop().size() == 2);
        System.out.println("Removing");
        instance.remove(prop2);
        instance.remove(prop);
        assertTrue(instance.findByName("prop1").isEmpty());
        assertTrue(instance.findByName("child1").isEmpty());
        assertTrue(instance.findByName("child11").isEmpty());
    }
    
    @Test
    public void testMerge() throws Exception {
        System.out.println("Create for merge");
        Property prop = new PropertyBuilder()
                .setName("prop_merge1")
                .child("child_merge1", null)
                .child("child_merge11", null)
                .toProperty();
        instance.create(prop);
        List<Property> storedProps = instance.findAllTop();
        assertTrue(storedProps.size() == 1);
        prop = storedProps.get(0);
        
        Property prop2 = new PropertyBuilder()
                .setName("prop_merge1")
                .child("prop_merge1", null)
                    .child("prop_merge1", null)
                        .child("prop_merge1", null)
                        .parent()
                    .parent()
                    .child("prop_merge1", null)
                    .parent()
                .parent()
                .child("prop_merge1", null)
                .toProperty();
        assertTrue(prop.merge(prop2));
        instance.edit(prop);
        storedProps = instance.findAllTop();
        assertTrue(storedProps.size() == 1);

        prop = storedProps.get(0);

        Property expectedProp = new PropertyBuilder()
            .setName("prop_merge1")
            .child("child_merge1", null)
                .child("child_merge11", null)
                .parent()
            .parent()
            .child("prop_merge1", null)
                .child("prop_merge1", null)
                    .child("prop_merge1", null)
                    .parent()
                .parent()
                .child("prop_merge1", null)
                .parent()
            .parent()
            .child("prop_merge1", null)
            .toProperty();

        assertTrue(prop.exactEquals(expectedProp));
        
        instance.remove(prop);
        assertTrue(instance.findByName("prop_merge1").isEmpty());
        assertTrue(instance.findByName("child_merge1").isEmpty());
        assertTrue(instance.findByName("child_merge11").isEmpty());
    }
}
