package com.proyecto.model.dao; 

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service  
public class DaoGenerico 
{
    @Autowired
    private SessionFactory sessionFactory;

    public boolean any(String hql) 
    {
        Session session = sessionFactory.openSession();

        Query query = session.createQuery(hql);

        if (query.list().size() > 0) {
            return true;
        }
        return false;
    }

    public <T> T find(String hql) 
    {
    	Session sesion = sessionFactory.openSession();
        
        try{
            Query query = sesion.createQuery(hql);  // "from Persona"
            
            if (query != null && query.list() != null && query.list().size() > 0) {
            	return (T) query.list().get(0);
			}
            else {
            	return null;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            sesion.close();
        }
                
        return null;
    }
    
    
    public <T> T findById(Class<T> entityClass, int id) 
    {
    	Session sesion = sessionFactory.openSession();
        
        try{   
            Object objeto = sesion.get(entityClass, id);

            if (objeto != null) {
                return (T) objeto; 
            } 
            return null;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            sesion.close();
        }
                
        return null;
    }
    
    
    public <T> T findById(Class<T> entityClass, String id) 
    {
    	Session sesion = sessionFactory.openSession();
        
        try{   
            int identificador = Integer.parseInt(id);
            Object objeto = sesion.get(entityClass, identificador);

            if (objeto != null) {
                return (T) objeto; 
            } 
            return null;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            sesion.close();
        }
                
        return null;   
    }


    public <T> List<T> findAll(String hql) 
    {
        List<T> entities = null;

        Session sesion = sessionFactory.openSession();
        
        try{
            entities = sesion.createQuery(hql).list();
            return entities;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            sesion.close();
        }
        
        return null;
    }
    
    
    public void save(Object entity) 
    {
        Session sesion = null;
        Transaction tx = null;
        
        try {
            sesion = sessionFactory.openSession();
            tx = sesion.beginTransaction();
            sesion.save(entity);
            tx.commit();
            sesion.close();
        } catch (Exception ex) {
            tx.rollback();
            ex.printStackTrace();
            throw new RuntimeException("No se pudo insertar el registro");
        }
    }
    
    
    public void update(Object entity) 
    {
        Session sesion = null;
        Transaction tx = null;
        
        try {
            sesion = sessionFactory.openSession();
            tx = sesion.beginTransaction();
            sesion.update(entity);
            tx.commit();
            sesion.close();
        
        } catch (Exception ex) {
            tx.rollback();
            ex.printStackTrace();
            throw new RuntimeException("No se pudo actualizar el registro");
        }
    }
    
    
    public void delete(Object entity) 
    {
        Session sesion = null;
        Transaction tx = null;
        
        try {
            sesion = sessionFactory.openSession();
            tx = sesion.beginTransaction();
            sesion.delete(entity);
            tx.commit();
            sesion.close();
        
        } catch (Exception ex) {
            tx.rollback();
            ex.printStackTrace();
            throw new RuntimeException("No se pudo borrar el registro");
        }
    }
    
    
    public <T> void deleteById(Class<T> entityClass, int id) 
    {
        Session sesion = null;
        Transaction tx = null;
        
        try{   
            sesion = sessionFactory.openSession();
            tx = sesion.beginTransaction();
            
            Object objeto = sesion.get(entityClass, id);

            if (objeto != null) {
                sesion.delete(objeto);
                tx.commit();
            } 
            sesion.close();
        }
        catch(Exception ex){
            tx.rollback();
            ex.printStackTrace();
            throw new RuntimeException("No se pudo borrar el registro");
        }          
    }
    
}
