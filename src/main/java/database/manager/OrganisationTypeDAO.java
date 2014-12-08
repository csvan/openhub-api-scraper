package database.manager;

import database.entity.Link;
import database.entity.OrganisationType;

/**
 * Created by csvanefalk on 18/11/14.
 */
public class OrganisationTypeDAO extends AbstractDAO<OrganisationType> {
    public OrganisationTypeDAO() {
        super(OrganisationType.class);
    }
}
