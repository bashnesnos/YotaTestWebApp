/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sml.test.model;

/**
 *
 * @author asemelit
 */
public class PropertyBuilder {
    private Property result;
    private Property current;
    
    public PropertyBuilder() {
        this.result = new Property();
        this.current = result;
    }
    
    public PropertyBuilder setName(String name) {
        current.setName(name);
        return this;
    }
    
    public PropertyBuilder setValue(String value) {
        current.setVal(value);
        return this;
    }

    public PropertyBuilder child(String name, String value) {
        Property child = new Property();
        child.setName(name);
        child.setVal(value);
        child.setParent(current);
        current = child;
        return this;
    }
    
    private void childReady(Property parent, boolean recursive) {
        if (parent != null) {
            parent.getChildren().add(current);
            if (recursive) {
                current = parent;
                childReady(parent.getParent(), true);
            }
        }
    }

    public PropertyBuilder parent() {
        Property parent = current.getParent();
        if (parent != null) {
            childReady(parent, false);
            current = parent;
        }
        return this;
    }
    
    public Property toProperty() {
        childReady(current.getParent(), true);
        return result;
    }
    
}
