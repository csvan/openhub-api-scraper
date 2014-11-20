package database.manager;

import database.entity.IEntity;

import java.util.List;

/**
 * Created by csvanefalk on 18/11/14.
 */
public interface IDAO<T extends IEntity> {

    void persist(T t);

    void remove(int id);

    void update(T t);

    T find(int id);

    List<T> range(int maxResults, int firstResult);

    List<T> all();

    int count();
}
