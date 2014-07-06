/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sml.test.jaxb;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import sml.test.model.Property;
import sml.test.model.PropertyBuilder;
import sml.test.model.Root;

/**
 *
 * @author asemelit
 */
public class PropertyJAXBTest {
    
    private static JAXBContext context;
    
    public PropertyJAXBTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws JAXBException {
        context = JAXBContext.newInstance(Root.class);
    }
    
    @Test
    public void testMarshal() throws JAXBException {
        StringWriter output = new StringWriter();
        Marshaller propMarshaller = context.createMarshaller();
        propMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
        Property prop = new PropertyBuilder()
                .setName("prop1")
                .child("child1", null)
                .child("child11", null)
                .toProperty();

        propMarshaller.marshal(prop, output);
        String result = output.toString();
        System.out.println(result);
        assertTrue("<property name=\"prop1\"><property name=\"child1\"><property name=\"child11\"/></property></property>".equals(result));

        Root root = new Root();
        root.getProperties().add(prop);
        
        prop = new PropertyBuilder()
                .setName("prop2")
                .child("child2", null)
                .child("child21", null)
                .toProperty();
        root.getProperties().add(prop);
        
        output = new StringWriter();
        propMarshaller.marshal(root, output);
        result = output.toString();
        System.out.println(result);
        assertTrue("<root><property name=\"prop1\"><property name=\"child1\"><property name=\"child11\"/></property></property><property name=\"prop2\"><property name=\"child2\"><property name=\"child21\"/></property></property></root>".equals(result));
    }

    @Test
    public void testUnMarshal() throws JAXBException {
        StringReader input = new StringReader("<property name=\"prop1\"><property name=\"child1\"><property name=\"child11\"/></property></property>");
        
        Unmarshaller propUnmarshaller = context.createUnmarshaller();
        //propUnmarshaller.
        Property unmarshalledProp = (Property) propUnmarshaller.unmarshal(input);
        
        Property expectedProp = new PropertyBuilder()
                .setName("prop1")
                .child("child1", null)
                .child("child11", null)
                .toProperty();
        
        assertTrue(unmarshalledProp != null);
        assertTrue("Not equal after the unmarshalling", unmarshalledProp.exactEquals(expectedProp));

        Root expectedRoot = new Root();
        expectedRoot.getProperties().add(expectedProp);
        
        expectedProp = new PropertyBuilder()
                .setName("prop2")
                .child("child2", null)
                .child("child21", null)
                .toProperty();
        expectedRoot.getProperties().add(expectedProp);
        
        input = new StringReader("<root><property name=\"prop1\"><property name=\"child1\"><property name=\"child11\"/></property></property><property name=\"prop2\"><property name=\"child2\"><property name=\"child21\"/></property></property></root>");
        Root unmarshalledRoot = (Root) propUnmarshaller.unmarshal(input);
        
        assertTrue(unmarshalledRoot.exactEquals(expectedRoot));
    }   
    
    @Test
    public void testUnmarshalNonUnique() throws JAXBException {
        StringReader input = new StringReader("<root>\n" +
        "	<property name=\"some name\">\n" +
        "		<property name=\"some name\">\n" +
        "			<property name=\"some name\">\n" +
        "			</property>\n" +
        "			<property name=\"some name\">\n" +
        "			</property>\n" +
        "			<property name=\"some name\">\n" +
        "			</property>\n" +
        "			<property name=\"some name\">\n" +
        "			</property>\n" +
        "		</property>\n" +
        "		<property name=\"some name\">\n" +
        "		</property>\n" +
        "		<property name=\"some name\">\n" +
        "			<property name=\"some name\">\n" +
        "				<property name=\"some name\">\n" +
        "				</property>\n" +
        "			</property>\n" +
        "		</property>\n" +
        "	</property>\n" +
        "	<property name=\"some name\">\n" +
        "	</property>\n" +
        "	<property name=\"some name\">\n" +
        "	</property>\n" +
        "	<property name=\"some name\">\n" +
        "	</property>\n" +
        "	<property name=\"some name\">\n" +
        "	</property>\n" +
        "</root>");
        Unmarshaller propUnmarshaller = context.createUnmarshaller();
        Root unmarshalledRoot = (Root) propUnmarshaller.unmarshal(input);

        Root expectedRoot = new Root();
        Property expectedProp = new PropertyBuilder()
            .setName("some name")
                .child("some name", null)
                    .child("some name", null)
                        .child("some name", null)
            .toProperty();
        expectedRoot.getProperties().add(expectedProp);
        assertTrue(unmarshalledRoot.getProperties().size() == 1);
        assertTrue(unmarshalledRoot.exactEquals(expectedRoot));
    }
    
}
