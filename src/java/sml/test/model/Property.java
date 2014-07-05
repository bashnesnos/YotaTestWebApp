/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sml.test.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author asemelit
 */
@Entity
@Table(name = "property", catalog = "yota_test", schema = "")
@XmlRootElement(name = "property")
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
    @NamedQuery(name = Property.NAMED_QUERY_FIND_ALL, query = "SELECT p FROM Property p"),
    @NamedQuery(name = Property.NAMED_QUERY_FIND_BY_NAME, query = "SELECT p FROM Property p WHERE p.name = :name")})
public class Property implements Serializable {
    public static final String NAMED_QUERY_FIND_BY_NAME = "Property.findByName";
    public static final String NAMED_QUERY_FIND_ALL = "Property.findAll";
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    @XmlTransient
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "parent", referencedColumnName = "id",  insertable = false, updatable = false)
    @XmlTransient
    private Property parent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name", nullable = false)
    @XmlAttribute(required = true)
    private String name;
    @Size(max = 45)
    @Column(name = "value")
    @XmlAttribute(required = false)
    private String value;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent")
    @XmlElementRef(name = "property")
    private Set<Property> children;

    public Property() {
    }

    public Property(Integer id) {
        this.id = id;
    }
    
    public Property(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Property getParent() {
        return parent;
    }
    
    public void setParent(Property parent) {
        this.parent = parent;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<Property> getChildren() {
        if (children == null) {
            children = new HashSet<>(4096);
        }
        return children;
    }

    public void setChildren(Set<Property> children) {
        this.children = children;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + Objects.hashCode(this.value);
        return hash;
    }

    public boolean merge(Property other) {
        if (other == null) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        
        if (!Objects.equals(this.children, other.children)) {
            Set<Property> thisChildren = this.children;
            Set<Property> otherChildren = other.children;
            
            if (thisChildren == null || otherChildren == null) {
                if (thisChildren == null) {
                    this.children = otherChildren;
                }
                else {
                    return true;
                }
            }
            else if (otherChildren.containsAll(thisChildren)) {
                this.children = otherChildren;
            }
            else { // a deeper merge is needed
                Set<Property> distinctThis = new HashSet<>(thisChildren);
                distinctThis.removeAll(otherChildren);
                for (Property thisCandidate : distinctThis) {
                    for (Property otherCandidate : otherChildren) {
                        thisCandidate.merge(otherCandidate);
                    }
                }
                thisChildren.addAll(otherChildren);
            }
            for (Property adoptChild : otherChildren) {
                adoptChild.setParent(this);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Property other = (Property) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        
        // assuming that children are compared after parents, so if we got here - they are equal enough        
        if (this.parent != other.parent && (this.parent == null || other.parent == null)) {
            return false;
        }
        
        if (!Objects.equals(this.children, other.children)) {
            Set<Property> thisChildren = this.children;
            int thisSize = thisChildren == null ? 0 : thisChildren.size();
            Set<Property> otherChildren = other.children;
            int otherSize = otherChildren == null ? 0 : otherChildren.size();
            
            return (thisChildren == null || otherChildren == null) && thisSize == otherSize;
        }
        
        return true;
    }

    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        if (parent instanceof Property) { //we don't care about Root
            this.parent = (Property) parent;
        }
    }
    
    @Override
    public String toString() {
        return "sml.test.model.Property[ name=" + name + "; value=" + value + ";\n children=" + children + " ]";
    }
    
}
