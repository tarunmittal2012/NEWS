package com.example.tarunmittal.news;

public class News {

    String webTitle;
    String webPublicationDate;
    String webUrl;
    String type;
    String authorName;
    String sectionName;

    public News(String webTitle, String webPublicationDate, String webUrl, String type, String sectionName,String authorName) {

        this.webTitle = webTitle;
        this.webPublicationDate = webPublicationDate;
        this.webUrl = webUrl;
        this.type = type;
        this.authorName=authorName;
        this.sectionName = sectionName;
    }

    public String getWebTitle() {

        return webTitle;
    }

    public String getWebPublicationDate() {

        return webPublicationDate;
    }

    public String getWebUrl() {

        return webUrl;
    }

    public String getType() {

        return type;
    }

    public String getSectionName() {

        return sectionName;
    }

    public String getAuthorName() {

        return authorName;

    }
}
