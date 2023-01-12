package com.huang.cloudbase.learn.database.mongodb;


import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

/**
 * 另一种使用方式是继承MongoRepository
 *
 * @author huangjunbiao_cdv
 */
public class MongodbTemplateTest {
    public static void main(String[] args) {
        MongoTemplate mongoTemplate = getMongoTemplate(getDbFactory("mongodb://127.0.0.1:27017/appdb"));
        Document document = mongoTemplate.getCollection("ming.as.comment.opinions").find(Filters.eq("_id", "07fdk4fr000-8m5u")).first();
        System.out.println(document.toJson());
    }

    /**
     * 获取mongodbFactory
     *
     * @param dbUrl 数据库地址，必须包含一个数据库名称
     * @return mongodbFactory
     */
    public static MongoDatabaseFactory getDbFactory(String dbUrl) {
        return new SimpleMongoClientDatabaseFactory(dbUrl);
    }

    public static MongoTemplate getMongoTemplate(MongoDatabaseFactory databaseFactory) {
        return new MongoTemplate(databaseFactory);
    }
}
