package org.kaakaa.pptmuseum.db.mongo;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by kaakaa on 16/02/16.
 */
public class MongoConnectionHelper {
    private static MongoClient mongoSingleton = null;

    /**
     * <p>create Morphia instance</p>
     *
     * @return Datastore of Morphia
     */
    public static Datastore getMorphiaInstance() {
        Morphia morphia = new Morphia();
        morphia.getMapper().getOptions().setStoreEmpties(true);
        morphia.mapPackage(DBResourceBundle.getString("morphia.package"));
        Datastore datastore = morphia.createDatastore(MongoConnectionHelper.getMongoClient(), DBResourceBundle.getString("morphia.collection"));
        datastore.ensureIndexes();
        return datastore;
    }

    /**
     * <p>create client object for MongoDB</p>
     *
     * @return client
     */
    private static synchronized MongoClient getMongoClient() {
        if (mongoSingleton == null) {
            synchronized (MongoConnectionHelper.class) {
                if (mongoSingleton == null) {
                    String host = DBResourceBundle.getString("mongo.host");
                    int port = (int) DBResourceBundle.get("mongo.port");

                    mongoSingleton = new MongoClient(host, port);
                }
            }
        }

        return mongoSingleton;
    }
}
