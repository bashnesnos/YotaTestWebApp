/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sml.test.ejb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import sml.test.model.Property;

/**
 *
 * @author asemelit
 */
@Stateless
public class PropertyFacade extends AbstractFacade<Property> implements PropertyFacadeLocal {
    @PersistenceContext(unitName = "YotaTestWebAppPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PropertyFacade() {
        super(Property.class);
    }

    @Override
    public List<Property> findByName(String name) {
        TypedQuery<Property> findByName = em.createNamedQuery(Property.NAMED_QUERY_FIND_BY_NAME, Property.class);
        findByName.setParameter("name", name);
        return findByName.getResultList();
    }

    @Override
    public List<Property> findAllTop() {
        TypedQuery<Property> findAll = em.createNamedQuery(Property.NAMED_QUERY_FIND_ALL, Property.class); //fetching all as we need that data
        List<Property> result = new ArrayList<>();
        for (Property property : findAll.getResultList()) {
            if (property.getParent() == null) {
                result.add(property);
            }
        }
        return result;

    }

    @Override
    public List<Property> findTopByName(String name) {
        TypedQuery<Property> findByName = em.createNamedQuery(Property.NAMED_QUERY_FIND_BY_NAME, Property.class);
        findByName.setParameter("name", name);
        List<Property> result = new ArrayList<>();
        for (Property property : findByName.getResultList()) {
            if (property.getParent() == null) {
                result.add(property);
            }
        }
        return result;
    }
    
}
