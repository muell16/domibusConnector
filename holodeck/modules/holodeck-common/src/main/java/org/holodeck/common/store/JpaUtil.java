package org.holodeck.common.store;

import javax.persistence.*;
import java.util.List;

/**
 * Basic JPA helper class, handles database operations.
 * <p>
 * JpaUtil.setPersistenceUnit() method must be called first prior to using this
 * utility.
 * </p>
 * @author Hamid Ben Malek
 */
public class JpaUtil implements IDataStore
{
    protected EntityManagerFactory emf = null;

    public JpaUtil() {}

    public JpaUtil(String pUnit) { setPersistenceUnit(pUnit); }

    public void setPersistenceUnit(String pUnit)
    {
      emf = Persistence.createEntityManagerFactory(pUnit);
    }

    public EntityManagerFactory getEntityManagerFactory() { return emf; }

    public void create(Object obj)
    {
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      em.persist(obj);
      tx.commit();
      em.clear();
      em.close();
    }

    public void create(Object[] objs)
    {
      if ( objs == null || objs.length == 0 ) return;
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      for (Object obj : objs)
      {
        if (obj != null) em.persist(obj);
      }
      tx.commit();
      em.clear();
      em.close();
    }

    public <T> T find(Class<T> entity, Object primaryKey)
    {
       if ( entity == null || primaryKey == null ) return null;
       EntityManager em = emf.createEntityManager();
       //return em.find(entity, primaryKey);

       EntityTransaction tx = em.getTransaction();
       tx.begin();
       T result = em.find(entity, primaryKey);
       tx.commit();
       em.close();
       return result;
    }

    public Object findByPrimaryKey(Class c, String key)
    {
      if ( key == null || c == null ) return null;
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      Object result = em.find(c, key);
      tx.commit();
      em.clear();
      em.close();
      return result;
    }

    public void update(Object obj)
    {
      if ( obj == null ) return;
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      if ( !em.contains(obj) ) em.merge(obj);
      em.flush();
      tx.commit();
      em.close();
    }

    public int update(String updateQuery)
    {
      if ( updateQuery == null || updateQuery.trim().equals("") )
           return 0;
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      int res = em.createQuery(updateQuery).executeUpdate();
      tx.commit();
      em.close();
      return res;
    }

    public void delete(Class c, String key)
    {
      if ( key == null || c == null ) return;
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      Object result = em.find(c, key);
      if ( result != null ) em.remove(result);
      em.flush();
      tx.commit();
      em.close();
    }

    public void delete(Object obj)
    {
      if ( obj == null ) return;
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      if ( em.contains(obj) ) em.remove(obj);
      else
      {
        Object result = em.merge(obj);
        em.remove(result);
      }
      em.flush();
      tx.commit();
      em.close();
    }

    public void save(Object obj)
    {
      if ( obj == null ) return;
      try {
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      em.merge(obj);
      //em.persist(obj);
      //System.out.println("====== save(): after merge()");
      em.flush();
      //System.out.println("====== save(): after flush()");
      tx.commit();
      //System.out.println("====== save(): after commit()");
      em.close();
      }
      catch(Exception ex) { ex.printStackTrace(); }
    }

    public List findAll(String query)
    {
      if ( query == null || query.trim().equals("") ) return null;
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      List result = em.createQuery(query).getResultList();
      tx.commit();
      em.close();
      return result;
    }

    public List findAll(String query,
                               int[] paramPos, String[] paramValues)
    {
      if ( query == null || query.trim().equals("") ) return null;
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      Query q = em.createQuery(query);
      if ( paramPos != null && paramPos.length > 0 &&
           paramValues != null && paramValues.length > 0 )
      {
        for (int i = 0; i < paramPos.length; i++)
             q.setParameter(paramPos[i], paramValues[i]);
      }
      List result = q.getResultList();
      tx.commit();
      em.close();
      return result;
    }

    public List findAll(Class c)
    {
      if ( c == null ) return null;
      String query = "SELECT c FROM " + c.getName() + " c";
      return findAll(query);
    }

    public List findAll(Class c, String property, String propValue)
    {
      if ( c == null ) return null;
      String query = "SELECT c FROM " + c.getName() + " c";
      if ( property != null && !property.trim().equals("") )
      {
        if (propValue != null &&
            (propValue.equalsIgnoreCase("FALSE") || propValue.equalsIgnoreCase("TRUE")) )
           query = query + " WHERE c." + property + " = " + propValue;
        else query = query + " WHERE c." + property + " = '" + propValue + "'";
      }
      return findAll(query);
    }

    public List findAll(Class c, String[] properties, String[] values)
    {
      if ( c == null ) return null;
      StringBuffer sb = new StringBuffer();
      sb.append("SELECT c FROM ");
      sb.append(c.getName());
      sb.append(" c");
      if ( properties != null && properties.length > 0 &&
           values != null && values.length > 0 )
      {
        int min = Math.min(properties.length, values.length);
        sb.append(" WHERE ");
        for (int i = 0; i < min; i++)
        {
          sb.append("c.");
          sb.append(properties[i]);
          if ( values[i].equalsIgnoreCase("TRUE") ) sb.append(" = TRUE");
          else if ( values[i].equalsIgnoreCase("FALSE") )
                    sb.append(" = FALSE");
          else
          {
            sb.append(" = '");
            sb.append(values[i]);
            sb.append("'");
          }
          if ( (i + 1) < min ) sb.append(" AND ");
        }
      }
      return findAll(sb.toString());
    }

    public List findAll(Class c, String[] properties, String[] values,
                        String minProp, String maxProp, int value)
    {
      if ( c == null ) return null;
      StringBuffer sb = new StringBuffer();
      sb.append("SELECT c FROM ");
      sb.append(c.getName());
      sb.append(" c");
      boolean propDone = false;
      boolean infDone = false;
      if ( properties != null && properties.length > 0 &&
           values != null && values.length > 0 )
      {
        propDone = true;
        int min = Math.min(properties.length, values.length);
        sb.append(" WHERE ");
        for (int i = 0; i < min; i++)
        {
          sb.append("c.");
          sb.append(properties[i]);
          if ( values[i].equalsIgnoreCase("TRUE") ) sb.append(" = TRUE");
          else if ( values[i].equalsIgnoreCase("FALSE") )
                    sb.append(" = FALSE");
          else
          {
            sb.append(" = '");
            sb.append(values[i]);
            sb.append("'");
          }
          if ( (i + 1) < min ) sb.append(" AND ");
        }
      }
      if ( minProp != null && !minProp.trim().equals("") )
      {
        if (!propDone) sb.append(" WHERE ");
        else sb.append(" AND ");
        sb.append("c.");
        sb.append(minProp);
        sb.append(" <= ");
        sb.append(value);
        infDone = true;
      }
      if ( maxProp != null && !maxProp.trim().equals("") )
      {
        if (infDone || propDone) sb.append(" AND c.");
        else sb.append(" WHERE c.");
        sb.append(maxProp);
        sb.append(" >= ");
        sb.append(value);
      }
      return findAll(sb.toString());
    }

    public Object findByUP(Class c, String property, String propValue)
    {
      if ( c == null ) return null;
      String query = "SELECT c FROM " + c.getName() + " c";
      if ( property != null && !property.trim().equals("") )
      {
        if (propValue != null &&
            (propValue.equalsIgnoreCase("FALSE") || propValue.equalsIgnoreCase("TRUE")) )
           query = query + " WHERE c." + property + " = " + propValue;
        else query = query + " WHERE c." + property + " = '" + propValue + "'";
      }
      try
      {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        List resultList = em.createQuery(query).getResultList();
        if ( resultList == null || resultList.size() == 0 ) return null;
        Object result = resultList.get(0);
        tx.commit();
        em.close();
        return result;
      }
      catch(Exception ex) { ex.printStackTrace(); return null; }
    }

    public List findWithPropertyLike(Class c, String property,
                                            String valueLike)
    {
      if ( c == null ) return null;
      String query = "SELECT c FROM " + c.getName() + " c";
      if ( property != null && !property.trim().equals("") )
           query = query + " WHERE c." + property + " LIKE '%" + valueLike + "%'";
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      List result = em.createQuery(query).getResultList();
      tx.commit();
      em.close();
      return result;
    }

    // deletes the objects whose specified property have the specified value
    public int delete(Class c, String property, String propValue)
    {
      if ( c == null ) return 0;
      String query = "DELETE FROM " + c.getName() + " c";
      if ( property != null && !property.trim().equals("") )
      {
        if (propValue != null &&
            (propValue.equalsIgnoreCase("FALSE") || propValue.equalsIgnoreCase("TRUE")) )
           query = query + " WHERE c." + property + " = " + propValue;
        else query = query + " WHERE c." + property + " = '" + propValue + "'";  
      }
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      int res = em.createQuery(query).executeUpdate();
      tx.commit();
      em.close();
      return res;
    }

   /**
    * Deletes all objects whose specified property has a value that
    * contains the specified value.
    * @param c the class type of the objects to delete
    * @param property the property of the object whose value must contain the
    *        specified value in the argument
    */
    public void deleteWithPropertyLike(Class c, String property,
                                              String valueLike)
    {
      if ( c == null ) return;
      String query = "DELETE FROM " + c.getName() + " c";
      if ( property != null && !property.trim().equals("") )
           query = query + " WHERE c." + property + " LIKE '%" + valueLike + "%'";
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      em.createQuery(query).executeUpdate();
      tx.commit();
      em.close();
    }

    public int delete(String deleteQuery)
    {
      if (deleteQuery == null || deleteQuery.trim().equals("")) return 0;
      int res = 0;
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      res = em.createQuery(deleteQuery).executeUpdate();
      tx.commit();
      em.close();
      return res;
    }

    public int[] delete(String[] deleteQueries)
    {
      if (deleteQueries == null || deleteQueries.length == 0) return null;
      EntityManager em = emf.createEntityManager();
      EntityTransaction tx = em.getTransaction();
      tx.begin();
      int[] res = new int[deleteQueries.length];
      for (int i = 0; i < deleteQueries.length; i++)
      {
        res[i] = em.createQuery(deleteQueries[i]).executeUpdate();
      }
      tx.commit();
      em.close();
      return res;
    }

    public void close()
    {
      if ( emf != null ) emf.close();
    }

  public void delete(Class c, String[] properties, String[] values,
                     String minProp, String maxProp, int value)
  {
    if (c == null) return;
    String whereClause =
       getWhereClause(c, properties, values, minProp, maxProp, value);
    StringBuffer sb = new StringBuffer();
    sb.append("DELETE FROM ");
    sb.append(c.getName());
    sb.append(" c");
    if ( whereClause != null && !whereClause.trim().equals("") )
    {
      sb.append(" WHERE ");
      sb.append(whereClause);
    }
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    em.createQuery(sb.toString()).executeUpdate();
    tx.commit();
    em.close();
  }

  public List findAll(Class c, String[] properties, String[] values,
                     String prop, int minPropValue, int maxPropValue)
  {
    if (c == null) return null;
    String whereClause =
       getWhereClause(c, properties, values, prop, minPropValue, maxPropValue);
    StringBuffer sb = new StringBuffer();
    sb.append("SELECT FROM ");
    sb.append(c.getName());
    sb.append(" c");
    if ( whereClause != null && !whereClause.trim().equals("") )
    {
      sb.append(" WHERE ");
      sb.append(whereClause);
    }
    return findAll(sb.toString());  
  }

  private String getWhereClause(Class c, String[] properties, String[] values,
                                String minProp, String maxProp, int value)
  {
    StringBuffer sb = new StringBuffer();
    boolean propDone = false;
    boolean infDone = false;
    if ( properties != null && properties.length > 0 &&
         values != null && values.length > 0 )
    {
      propDone = true;
      int min = Math.min(properties.length, values.length);
      for (int i = 0; i < min; i++)
      {
        sb.append("c.");
        sb.append(properties[i]);
        if ( values[i].equalsIgnoreCase("TRUE") ) sb.append(" = TRUE");
        else if ( values[i].equalsIgnoreCase("FALSE") )
                  sb.append(" = FALSE");
        else
        {
          sb.append(" = '");
          sb.append(values[i]);
          sb.append("'");
        }
        if ( (i + 1) < min ) sb.append(" AND ");
      }
    }
    if ( minProp != null && !minProp.trim().equals("") )
    {
      if (propDone) sb.append(" AND ");
      sb.append("c.");
      sb.append(minProp);
      sb.append(" <= ");
      sb.append(value);
      infDone = true;
    }
    if ( maxProp != null && !maxProp.trim().equals("") )
    {
      if (infDone || propDone) sb.append(" AND ");
      sb.append("c.");
      sb.append(maxProp);
      sb.append(" >= ");
      sb.append(value);
    }
    return sb.toString();
  }

  private String getWhereClause(Class c, String[] properties, String[] values,
                               String prop, int minPropValue, int maxPropValue)
  {
    StringBuffer sb = new StringBuffer();
    boolean propDone = false;
    if ( properties != null && properties.length > 0 &&
         values != null && values.length > 0 )
    {
      propDone = true;
      int min = Math.min(properties.length, values.length);
      for (int i = 0; i < min; i++)
      {
        sb.append("c.");
        sb.append(properties[i]);
        if ( values[i].equalsIgnoreCase("TRUE") ) sb.append(" = TRUE");
        else if ( values[i].equalsIgnoreCase("FALSE") )
                  sb.append(" = FALSE");
        else
        {
          sb.append(" = '");
          sb.append(values[i]);
          sb.append("'");
        }
        if ( (i + 1) < min ) sb.append(" AND ");
      }
    }
    if ( prop != null && !prop.trim().equals("") )
    {
      if (propDone) sb.append(" AND ");
      sb.append("c.");
      sb.append(prop);
      sb.append(" <= ");
      sb.append(maxPropValue);
      sb.append(" AND ");
      sb.append("c.");
      sb.append(prop);
      sb.append(" >= ");
      sb.append(minPropValue);
    }
    return sb.toString();
  }

  public List select(EntityManager em, Query query)
  {
    if ( em == null || query == null ) return null;
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    List result = query.getResultList();
    tx.commit();
    em.close();
    return result;
  }

  public EntityManager createEntityManager()
  {
    return emf.createEntityManager();
  }
}