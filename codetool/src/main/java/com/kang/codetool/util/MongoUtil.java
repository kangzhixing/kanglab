package com.kang.codetool.util;

import com.mintq.conf.core.MintqConfClient;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Map;

public class MongoUtil {

    private static final MongoUtil mongoUtil = new MongoUtil();

    private MongoClient mongoClient;

    private String database;

    public static MongoUtil getMongoUtilInstance() {
        return mongoUtil;
    }

    private MongoUtil() {
        if (mongoClient == null) {
            MongoClientURI uri = new MongoClientURI(MintqConfClient.get("risk-control.spring.data.mongodb.uri"));
            database = uri.getDatabase();
            mongoClient = new MongoClient(uri);
        }
    }

    public MongoDatabase getDatabase(String dbName) {
        return mongoClient.getDatabase(dbName);
    }

    public MongoDatabase getDatabase() {
        return mongoClient.getDatabase(database);
    }

    public FindIterable<Document> findBy(String collectionName, Bson bson) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        return collection.find(bson);
    }

    public DeleteResult deleteMany(String collectionName, Bson bson) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        return collection.deleteMany(bson);
    }

    public DeleteResult deleteOne(String collectionName, Bson bson) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        return collection.deleteOne(bson);
    }

    public long countBy(String collectionName, Bson bson) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        return collection.count(bson);
    }

    public void insertMap(String collectionName, Map<String, Object> map) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        if (collection == null) {
            mongoDatabase.createCollection(collectionName);
        }
        Document doc = new Document();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            doc.put(entry.getKey(), entry.getValue());
        }
        if (collection == null) {
            return;
        }
        collection.insertOne(doc);
    }

    /**
     * FIXME
     *
     * @param collectionName
     * @param id
     * @param newdoc
     * @return
     */
    public UpdateResult updateById(String collectionName, String id, Document newdoc) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        ObjectId _idobj;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Bson filter = Filters.eq("_id", _idobj);
        // coll.replaceOne(filter, newdoc); // 完全替代
        return collection.updateOne(filter, new Document("$set", newdoc));
    }

}
