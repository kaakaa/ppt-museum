package org.kaakaa.pptmuseum.db;

import org.bson.types.ObjectId;
import org.kaakaa.pptmuseum.db.document.Document;
import org.kaakaa.pptmuseum.db.document.Slide;
import org.kaakaa.pptmuseum.db.mongo.MongoConnectionHelper;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;

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
}
