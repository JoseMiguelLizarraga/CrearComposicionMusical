package com.proyecto.service;

import java.util.List;

public interface IDaoGenericoService 
{
    public boolean any(String hql);

    public <T> T find(String hql);
       
    public <T> T findById(Class<T> entityClass, int id);
        
    public <T> T findById(Class<T> entityClass, String id);

    public <T> List<T> findAll(String hql);   
    
    public void save(Object entity);   
    
    public void update(Object entity);  
    
    public void delete(Object entity);
    
    public <T> void deleteById(Class<T> entityClass, int id);
}
