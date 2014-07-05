/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sml.test.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author asemelit
 */
public class PropertyTest {
    
    public PropertyTest() {
    }

    /**
     * Test of getId method, of class Property.
     */
    @Test
    public void testEquals() {
        Property prop1 = new PropertyBuilder()
                .setName("prop1")
                .child("prop1", null)
                .child("prop1", null)
                .parent()
                .child("prop1", null)
                .toProperty();

        Property prop2 = new PropertyBuilder()
                .setName("prop1")
                .child("prop1", null)
                .child("prop1", null)
                .parent()
                .child("prop1", null)
                .toProperty();
        assertEquals(prop1, prop2);
        
        Property prop3 = new PropertyBuilder()
            .setName("prop1")
            .toProperty();
        assertTrue(!prop1.equals(prop3));
    }
    
    @Test
    public void testUniqueness() {
        Property prop1 = new PropertyBuilder()
                .setName("prop1")
                .child("prop1", null)
                    .child("prop1", null)
                    .parent()
                .parent()
                .child("prop1", null)
                .toProperty();

        Property prop2 = new PropertyBuilder()
                .setName("prop1")
                .child("prop1", null)
                    .child("prop1", null)
                    .parent()
                    .child("prop1", null)
                    .parent()
                .parent()
                .child("prop1", null)
                .toProperty();
        
        assertEquals(prop2, prop1);
        assertEquals(prop1, prop2);
    }
    
    @Test
    public void testMerge() {
        Property prop1 = new PropertyBuilder()
                .setName("prop1")
                .child("prop1", null)
                    .child("prop1", null)
                    .parent()
                .parent()
                .child("prop1", null)
                .toProperty();

        Property prop2 = new PropertyBuilder()
                .setName("prop1")
                .child("prop1", null)
                    .child("prop1", null)
                        .child("prop1", null)
                        .parent()
                    .parent()
                    .child("prop1", null)
                    .parent()
                .parent()
                .child("prop1", null)
                .toProperty();
        
        assertTrue(!prop1.equals(prop2) && !prop2.equals(prop1));
        assertTrue(prop1.merge(prop2));
        
        Property mergeResult = new PropertyBuilder()
        .setName("prop1")
        .child("prop1", null)
            .child("prop1", null)
                .child("prop1", null)
                .parent()
            .parent()
            .child("prop1", null)
            .parent()
        .parent()
        .child("prop1", null)
        .toProperty();
        
        assertEquals(mergeResult, prop1);
        
    }

    @Test
    public void testMergeDummy() {
        Property prop1 = new PropertyBuilder()
                .setName("prop1")
                .child("prop1", null)
                    .child("prop1", null)
                    .parent()
                .parent()
                .child("prop1", null)
                .toProperty();

        Property prop2 = new PropertyBuilder()
                .setName("prop1")
                .toProperty();
        
        assertTrue(!prop1.equals(prop2) && !prop2.equals(prop1));
        assertTrue(prop1.merge(prop2));
        
        Property mergeResult = new PropertyBuilder()
                .setName("prop1")
                .child("prop1", null)
                    .child("prop1", null)
                    .parent()
                .parent()
                .child("prop1", null)
                .toProperty();
        
        assertEquals(mergeResult, prop1);
        
    }
    
    @Test
    public void testMergeDifferentBranches(){
        Property prop = new PropertyBuilder()
                .setName("prop_merge1")
                .child("child_merge1", null)
                .child("child_merge11", null)
                .toProperty();
        
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

        assertEquals(prop, expectedProp);
    }
    
}
