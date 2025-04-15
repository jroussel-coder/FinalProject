package com.example.finalproject;

import java.io.Serializable;

/**
 * NASAimage is a data model representing a NASA Astronomy Picture of the Day.
 * This class implements Serializable to allow passing between Activities via Intents.
 */
class NASAimage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String date;
    private String title;
    private String explanation;
    private String url;
    private String hdurl;

    /**
     * Default constructor for serialization and frameworks that require it.
     */
    public NASAimage() {}

    /**
     * Full constructor to initialize all fields of the NASA image.
     *
     * @param date        The date the image was published.
     * @param title       The title of the image.
     * @param explanation The description or explanation of the image.
     * @param url         The standard resolution URL of the image.
     * @param hdurl       The high-definition URL of the image (can be null).
     */
    public NASAimage(String date, String title, String explanation, String url, String hdurl) {
        this.date = date;
        this.title = title;
        this.explanation = explanation;
        this.url = url;
        this.hdurl = hdurl;
    }

    /** @return The date the image was taken or published. */
    public String getDate() {
        return date;
    }

    /** @param date Set the date for this image. */
    public void setDate(String date) {
        this.date = date;
    }

    /** @return The title of the image. */
    public String getTitle() {
        return title;
    }

    /** @param title Set the title for this image. */
    public void setTitle(String title) {
        this.title = title;
    }

    /** @return The explanatory text about the image. */
    public String getExplanation() {
        return explanation;
    }

    /** @param explanation Set the description for this image. */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    /** @return The standard resolution image URL. */
    public String getUrl() {
        return url;
    }

    /** @param url Set the standard image URL. */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The HD image URL if available, otherwise fallback to the standard URL.
     */
    public String getHdurl() {
        return hdurl != null ? hdurl : url;
    }

    /** @param hdurl Set the HD image URL. */
    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }
}
