package org.kaakaa.pptmuseum.db.document;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * Created by kaakaa on 16/02/17.
 */
public class Document {
    @Id
    private ObjectId id;

    @Property("file")
    private byte[] file;

    @Property("type")
    private String contentType;

    public void setFile(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }

    public void setId(String id) {
        this.id = new ObjectId(id);
    }

    public void setContentType(String type) {
        this.contentType = type;
    }

    public String getContentType() {
        return this.contentType;
    }
}
