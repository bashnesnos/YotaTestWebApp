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

    @Test
    public void testExactEquals() {
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
        assertTrue(prop1.exactEquals(prop2) && prop2.exactEquals(prop1));
    }
    
    @Test
    public void testExactNotEquals() {
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
                        .child("prop1", null)
                            .child("prop1", null)
                .toProperty();
        assertFalse(prop1.exactEquals(prop2));
        assertFalse(prop2.exactEquals(prop1));
    }
    
    @Test
    public void testSubsetEquals() {
        Property prop1 = new PropertyBuilder()
                .setName("prop1")
                .child("prop1", null)
                .child("prop1", null)
                .parent()
                .child("prop1", null)
                .toProperty();

        Property prop3 = new PropertyBuilder()
            .setName("prop1")
            .toProperty();
        assertTrue(prop1.equals(prop3) && prop3.equals(prop1));

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
                    .child("prop1", null) // A
                    .parent()
                    .child("prop1", null) //this won't be put to a tree cause A > A would be equal to the previous A
                        .child("prop1", null)
                        .parent()
                    .parent()
                .parent()
                .child("prop1", null)
                .toProperty();
        
        assertTrue(prop2.exactEquals(prop1));
        assertTrue(prop1.exactEquals(prop2));
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
                    .child("prop1", null)
                .toProperty();
        
        assertTrue(!prop1.exactEquals(prop2) && !prop2.exactEquals(prop1));
        assertTrue(prop1.merge(prop2));
        
        Property mergeResult = new PropertyBuilder()
        .setName("prop1")
        .child("prop1", null)
            .child("prop1", null)
                .child("prop1", null)
        .toProperty();
        
        assertTrue(mergeResult.exactEquals(prop1));
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
        
        assertTrue(prop1.equals(prop2) && prop2.equals(prop1));
        assertTrue(prop1.merge(prop2));
        
        Property mergeResult = new PropertyBuilder()
                .setName("prop1")
                .child("prop1", null)
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
            .toProperty();

        assertEquals(prop, expectedProp);
    }
    
}
