package org.kaakaa.pptmuseum.db.document;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

/**
 * Created by kaakaa on 16/02/21.
 */
@Embedded
public class Comment {
    @Property("name")
    private String name;
    @Property("comment")
    private String comment;

    public Comment() {
    }

    public Comment(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public String toHtml(){
        StringBuffer buffer = new StringBuffer("<li>");
        buffer.append(name);
        buffer.append(": ");
        buffer.append(comment);
        buffer.append("</li>");
        return buffer.toString();
    }
}
