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
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class PropertyGeneratorTest {
    
    public PropertyGeneratorTest() {
    }

    /**
     * Test of getRoot method, of class PropertyGenerator.
     */
    @Test
    public void testGetRoot() {
        System.out.println("getRoot");
        PropertyGenerator instance = new PropertyGenerator(49, 19);
        Root result = instance.getRoot();
    }
    
}
