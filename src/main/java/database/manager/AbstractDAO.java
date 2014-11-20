package database.manager;

import database.entity.IEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Created by csvanefalk on 18/11/14.
 */
public abstract class AbstractDAO<T extends IEntity> implements IDAO<T> {

    protected final EntityManagerFactory entityManagerFactory;
    protected final Class<T> entityType;

    public AbstractDAO(Class<T> entityType) {

        this.entityManagerFactory = Persistence.createEntityManagerFactory("api_scraper");
        this.entityType = entityType;
    }

    protected EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    @Override
    public void persist(T t) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
        } catch (Exception ex) {
            //nothing for now
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Removes the entity identified by id.
     *
     * @param id
     */
    @Override
    public void remove(int id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            T t = em.getReference(entityType, id);
            em.remove(t);
            em.getTransaction().commit();
        } catch (Exception ex) {
            // Nothing for now
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Updates a given entity.
     *
     * @param t
     */
    @Override
    public void update(T t) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(t);
            em.getTransaction().commit();
        } catch (Exception ex) {
            // Nothing for now
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * @param id
     * @return the entity identified by id.
     */
    @Override
    public T find(int id) {
        EntityManager em = getEntityManager();
        T t = em.find(entityType, id);
        return t;
    }

    /**
     * @return all entity instances of this type
     */
    @Override
    public List<T> all() {
        return get(true, -1, -1);
    }

    /**
     * @param maxResults
     * @param firstResult
     * @return all entity instances starting at position firstResult, up to maxResults
     */
    @Override
    public List<T> range(int maxResults, int firstResult) {
        return get(false, maxResults, firstResult);
    }

    @Override
    public int count() {
        EntityManager em = getEntityManager();
        int count = -1;
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<T> rt = cq.from(entityType);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            count = ((Long) q.getSingleResult()).intValue();
        } catch (Exception ex) {
            // Nothing for now
        } finally {
            em.close();
        }
        return count;
    }

    /**
     * @param all
     * @param maxResults
     * @param firstResult
     * @return
     */
    private List<T> get(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        List<T> found = new ArrayList<>();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery(entityType);
            cq.select(cq.from(entityType));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            found.addAll(q.getResultList());
        } catch (Exception ex) {
            // Nothing for now
        } finally {
            em.close();
        }
        return found;
    }

    public List<T> findBy(Object... conditions) {
        Map<String, Object> conditionsMap = new HashMap<>();
        for (int i = 0; i < conditions.length; i += 2) {
            String key = (String) conditions[i];
            Object value = conditions[i + 1];
            conditionsMap.put(key, value);
        }
        return findBy(conditionsMap);
    }

    public List<T> findBy(Map<String, Object> conditions) {
        EntityManager em = getEntityManager();
        List<T> found = new ArrayList<>();
        try {
            String query = "SELECT o FROM " + entityType.getSimpleName() + " o WHERE ";
            Iterator<String> iterator = conditions.keySet().iterator();
            while (iterator.hasNext()) {
                String column = iterator.next();
                Object value = conditions.get(column);
                if (value instanceof Number) {
                    query += " " + "o." + column + " = " + value;
                } else {
                    query += " " + "o." + column + " = " + "'" + value + "'";
                }
                if (iterator.hasNext()) {
                    query += " AND ";
                }
            }
            found = em.createQuery(query, entityType).getResultList();
        } catch (Exception ex) {
            // Nothing for now
        } finally {
            em.close();
        }
        return found;
    }

    public T findOneBy(Object... conditions) {
        Map<String, Object> conditionsMap = new HashMap<>();
        for (int i = 0; i < conditions.length; i += 2) {
            String key = (String) conditions[i];
            Object value = conditions[i + 1];
            conditionsMap.put(key, value);
        }
        return findOneBy(conditionsMap);
    }

    public T findOneBy(final Map<String, Object> conditions) {
        List<T> result = findBy(conditions);
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }
}
