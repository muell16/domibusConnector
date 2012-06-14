package org.holodeck.common.store;

import java.util.List;

/**
 * @author Hamid Ben Malek
 */
public interface IDataStore
{
    public void setPersistenceUnit(String persistenceUnit);

    public void create(Object obj);

    public void delete(Object obj);
    public void delete(Class c, String key);
    public void deleteWithPropertyLike(Class c, String property,
                                       String valueLike);
    public void delete(Class c, String[] properties, String[] values,
                       String minProp, String maxProp, int value);
    // deletes the objects whose specified property have the specified value
    public int delete(Class c, String property, String propValue);
    public int delete(String deleteQuery);
    public int[] delete(String[] deleteQueries);

    public void update(Object obj);
    // if object don't exist, it will be created, otherwise it is updated
    public void save(Object obj);



    public <T> T find(Class<T> entity, Object primaryKey);
    public List findAll(String query);
    public List findAll(String query,
                        int[] paramPos, String[] paramValues);
    public List findAll(Class c);
    public Object findByPrimaryKey(Class c, String key);
    public Object findByUP(Class c, String property, String propValue);
    // returns the list of objects with their property having the specified value
    public List findAll(Class c, String property, String propValue);
    public List findAll(Class c, String[] properties, String[] values);
    public List findAll(Class c, String[] properties, String[] values,
                        String minProp, String maxProp, int value);
    public List findAll(Class c, String[] properties, String[] values,
                        String prop, int minPropValue, int maxPropValue);
    public List findWithPropertyLike(Class c, String property,
                                     String valueLike);
}
