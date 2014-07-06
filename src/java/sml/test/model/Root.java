/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sml.test.model;

import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asemelit
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Root {
    @XmlElement(name = "property")
    private Set<Property> properties;

    public Set<Property> getProperties() {
        if (properties == null) {
            properties = new HashSet<>(4096); //medium
        }
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }
    
    public boolean exactEquals(Root other) {
        int thisSize = this.properties == null ? 0 : this.properties.size();
        int otherSize = other.properties == null ? 0 : other.properties.size();
        if (this.properties == null || other.properties == null) {
            return thisSize == otherSize;
        }
        else if (thisSize != otherSize){
            return false;
        }
        else { //going deeper
            for (Property thisChild : this.properties) {
                boolean equalsToAny = false;
                for (Property otherChild : other.properties) {
                    if (equalsToAny = thisChild.exactEquals(otherChild)) {
                        break;
                    }
                }
                if (!equalsToAny) {
                    return false;
                }
            }
        }
        return true;
    }
}
