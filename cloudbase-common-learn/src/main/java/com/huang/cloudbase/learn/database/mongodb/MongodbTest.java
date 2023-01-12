package com.huang.cloudbase.learn.database.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 * @author huangjunbiao_cdv
 */
public class MongodbTest {

    public static void main(String[] args) {
        MongoCollection<Document> collection = getCollection("mongodb://127.0.0.1:27017", "appdb", "ming.as.comment.opinions");
        FindIterable<Document> documents = collection.find();
        for (Document document : documents) {
            System.out.println(document.toJson());
        }
        Document document = collection.find(Filters.eq("_id", "07fdk4fr000-8m5u")).first();
        System.out.println(document.toJson());
    }

    public static MongoCollection<Document> getCollection(String dbUrl, String dbName, String collectionName) {
        ConnectionString connStr = new ConnectionString(dbUrl);
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientSettings.Builder builder = MongoClientSettings.builder().codecRegistry(pojoCodecRegistry).applyConnectionString(connStr);
        MongoClient mongoClient = MongoClients.create(builder.build());
        return mongoClient.getDatabase(dbName).getCollection(collectionName);
    }
}
