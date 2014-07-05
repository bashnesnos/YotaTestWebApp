/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sml.test.ejb;

import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import sml.test.model.Property;

/**
 *
 * @author asemelit
 */
@Local
public interface PropertyFacadeLocal {

    void create(Property property);

    void edit(Property property);

    void remove(Property property);

    Property find(Integer id);
    
    List<Property> findByName(String name);

    List<Property> findTopByName(String name);
    
    List<Property> findAllTop();
    
    List<Property> findAll();

    List<Property> findRange(int[] range);

    int count();
    
}
