package com._kforbreakfast._kforbreakfast.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    private String caption;

    @Column(nullable = false)
    private LocalDateTime uploadDatetime;

    private Double pace;

    private String filePath;

    private String tags;

    private String location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public LocalDateTime getUploadDatetime() {
        return uploadDatetime;
    }

    public void setUploadDatetime(LocalDateTime uploadDatetime) {
        this.uploadDatetime = uploadDatetime;
    }

    public Double getPace() {
        return pace;
    }

    public void setPace(Double pace) {
        this.pace = pace;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", caption='" + caption + '\'' +
                ", uploadDatetime=" + uploadDatetime +
                ", pace=" + pace +
                ", filePath='" + filePath + '\'' +
                ", tags='" + tags + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}

