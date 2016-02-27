package org.kaakaa.pptmuseum.db;

import org.bson.types.ObjectId;
import org.kaakaa.pptmuseum.db.document.Comment;
import org.kaakaa.pptmuseum.db.document.Comments;
import org.kaakaa.pptmuseum.db.document.Document;
import org.kaakaa.pptmuseum.db.document.Slide;
import org.kaakaa.pptmuseum.db.mongo.MongoConnectionHelper;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

import java.util.List;

/**
 * Created by kaakaa on 16/02/14.
 */
public class MongoDBClient {
    /**
     * Morphia
     */
    private Datastore datastore = MongoConnectionHelper.getMorphiaInstance();

    /**
     * <p>Upload slide to db</p>
     *
     * @param slide    Slide model
     * @param document Document model
     * @return return key
     */
    public Object upload(Slide slide, Document document) {
        Key<Slide> result = datastore.save(slide);
        document.setId(result.getId().toString());
        return datastore.save(document);
    }

    /**
     * <p>Get all slide information</p>
     *
     * @return all slide information
     */
    public List<Slide> searchAll() {
        return datastore.createQuery(Slide.class).asList();
    }

    /**
     * <p>Get pdf contents</p>
     *
     * @param id id
     * @return Document Model
     */
    public Document getPDF(String id) {
        return datastore.get(Document.class, new ObjectId(id));
    }

    public String getComments(String id) {
        Comments comments = datastore.find(Comments.class, "slideId =", id).get();
        if (null == comments) {
            return "No Comments.";
        } else {
            return comments.toHtml();
        }
    }

    public Key<Comments> addComments(String id, String name, String comment) {
        Comments comments = datastore.find(Comments.class, "slideId =", id).get();
        if (null == comments) {
            comments = new Comments();
            comments.setSlideId(id);
        }
        Comment c = new Comment(name, comment);
        // save commments
        comments.addComment(c);
        return datastore.save(comments);
    }

    /**
     * <p>DELETE uploaded slides</p>
     *
     * @param id id
     */
    public void delete(String id) {
        datastore.delete(Slide.class, new ObjectId(id));
        Query<Document> documentQuery = datastore.createQuery(Document.class).filter("id =", new ObjectId(id));
        datastore.delete(documentQuery);
        Query<Comments> commentsQuery = datastore.createQuery(Comments.class).filter("slideId =", id);
        datastore.delete(commentsQuery);
    }

    public byte[] getThumbnail(String id) {
        Slide slide = datastore.get(Slide.class, new ObjectId(id));
        return slide.getThumbnail();
    }
}
