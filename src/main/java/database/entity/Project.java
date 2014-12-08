package database.entity;

import javax.persistence.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by csvanefalk on 18/11/14.
 */
@Entity
public class Project implements IEntity {

    @Id
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name;

    @Lob
    private String description;

    @Column(length = 2500)
    private String htmlUrl;

    @Column(length = 2500)
    private String homepageUrl;

    @Column(length = 2500)
    private String downloadUrl;

    @Column(length = 2500)
    private String mediumLogoUrl;

    @Column(length = 2500)
    private String smallLogoUrl;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private int userCount;
    private int ratingCount;
    private int reviewCount;
    private int analysisID;

    private float averageRating;

    private boolean isManuallyInserted;

    @ManyToMany(cascade = {CascadeType.REFRESH})
    List<Tag> tags = new LinkedList<>();

    @ManyToMany(cascade = {CascadeType.REFRESH})
    List<License> licenses = new LinkedList<>();

    @ManyToMany(cascade = {CascadeType.REFRESH})
    List<Link> links = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomepageUrl() {
        return homepageUrl;
    }

    public void setHomepageUrl(String homepageUrl) {
        this.homepageUrl = homepageUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
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

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getAnalysisID() {
        return analysisID;
    }

    public void setAnalysisID(int analysisID) {
        this.analysisID = analysisID;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void addLink(Link link) {
        this.links.add(link);
    }

    public void addLicense(License license) {
        this.licenses.add(license);
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public boolean isManuallyInserted() {
        return isManuallyInserted;
    }

    public void setManuallyInserted(boolean isManuallyInserted) {
        this.isManuallyInserted = isManuallyInserted;
    }
}
