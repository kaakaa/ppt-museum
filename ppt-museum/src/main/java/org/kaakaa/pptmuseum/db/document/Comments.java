package org.kaakaa.pptmuseum.db.document;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kaakaa on 16/02/21.
 */
@Entity
public class Comments {
    @Id
    private ObjectId id;

    @Indexed(name = "slideId", unique = true)
    private String slideId;

    @Embedded
    private List<Comment> comments;

    public void addComment(Comment comment) {
        if (null == comments) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
    }

    public void setSlideId(String slideId) {
        this.slideId = slideId;
    }

    public String toHtml() {
        StringBuffer buffer = new StringBuffer("<ul>");
        buffer.append(this.comments.stream().map(Comment::toHtml).collect(Collectors.joining()));
        buffer.append("</ul>");
        return buffer.toString();
    }
}
