package database.manager;

import database.entity.Category;

/**
 * Created by csvanefalk on 18/11/14.
 */
public class CategoryDAO extends AbstractDAO<Category> {
    public CategoryDAO() {
        super(Category.class);
    }
}
