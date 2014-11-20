package database

import database.entity.Category
import database.manager.CategoryDAO

object CategoryManager extends AbstractManager[Category, CategoryDAO](new CategoryDAO())
