package org.kaakaa.pptmuseum.db.document;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Created by kaakaa on 16/02/13.
 */
@Entity
public class Slide {
    @Id
    private ObjectId id;
    @Property("title")
    private String title;
    @Property("description")
    private String description;
    @Property("thumbnail")
    private byte[] thumbnail;

    public Slide() {
    }

    public void setThumbnail(byte[] file) {
        this.thumbnail = file;
    }

    public byte[] getThumbnail() {
        return this.thumbnail;
    }

    public ObjectId getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTags(String tags) {
    }

    public String getTime() {
        Instant instant = Instant.ofEpochSecond(this.id.getTimestamp());
        LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneId.SHORT_IDS.get("JST")));
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}
