package database.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by christopher on 22/11/14.
 */
@Entity
public class Organisation extends AbstractEntity {

    private String name;

    @Lob
    private String description;

    @Column(length = 2500)
    private String htmlUrl;

    @Column(length = 2500)
    private String url;

    @Column(length = 2500)
    private String urlName;

    @Column(length = 2500)
    private String homepageUrl;

    @Column(length = 2500)
    private String mediumLogoUrl;

    @Column(length = 2500)
    private String smallLogoUrl;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private int projectsCount;

    private int affiliatedCommitters;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    OrganisationType organisationType;

    @JoinTable(name = "ORGANISATION_PORTFOLIO_PROJECTS")
    @OneToMany(cascade = {CascadeType.REFRESH})
    List<Project> portfolioProjects = new LinkedList<>();

    @JoinTable(name = "ORGANISATION_OUTSIDE_PROJECTS")
    @ManyToMany(cascade = {CascadeType.REFRESH})
    List<Project> outsideProjects = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public String getHomepageUrl() {
        return homepageUrl;
    }

    public void setHomepageUrl(String homepageUrl) {
        this.homepageUrl = homepageUrl;
    }

    public String getMediumLogoUrl() {
        return mediumLogoUrl;
    }

    public void setMediumLogoUrl(String mediumLogoUrl) {
        this.mediumLogoUrl = mediumLogoUrl;
    }

    public String getSmallLogoUrl() {
        return smallLogoUrl;
    }

    public void setSmallLogoUrl(String smallLogoUrl) {
        this.smallLogoUrl = smallLogoUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getProjectsCount() {
        return projectsCount;
    }

    public void setProjectsCount(int projectsCount) {
        this.projectsCount = projectsCount;
    }

    public int getAffiliatedCommitters() {
        return affiliatedCommitters;
    }

    public void setAffiliatedCommitters(int affiliatedCommitters) {
        this.affiliatedCommitters = affiliatedCommitters;
    }

    public OrganisationType getOrganisationType() {
        return organisationType;
    }

    public void setOrganisationType(OrganisationType organisationType) {
        this.organisationType = organisationType;
    }

    public List<Project> getPortfolioProjects() {
        return portfolioProjects;
    }

    public void setPortfolioProjects(List<Project> portfolioProjects) {
        this.portfolioProjects = portfolioProjects;
    }

    public void addPortfolioProject(Project project) {
        this.portfolioProjects.add(project);
    }

    public void addOutsideProject(Project project) {
        this.outsideProjects.add(project);
    }
}
