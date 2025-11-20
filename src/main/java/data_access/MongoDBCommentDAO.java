package data_access;

import Entities.Comment;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoDBCommentDAO {

    private final MongoCollection<Document> collection;
    private MongoDBExtractToCache data;

    public MongoDBCommentDAO() {
        MongoClient client = MongoConfig.getClient();
        MongoDatabase db = client.getDatabase("CSC207_group_project_2025");
        this.collection = db.getCollection("Comments");
        this.data = new MongoDBExtractToCache();
    }

    public void saveComment(Comment comment) {

        if (collection.find(Filters.eq("idForDB", comment.getIdForDB())).first() != null) {
            collection.deleteOne(Filters.eq("idForDB", comment.getIdForDB()));
        }

        Document doc = new Document()
                .append("id", comment.getId())
                .append("content", comment.getContent())
                .append("userId", comment.getUser().getId())
                .append("listingId", comment.getListing().getId())
                .append("createdAt", comment.getCreatedAt().getEpochSecond())
                .append("idForDB", comment.getIdForDB());

        collection.insertOne(doc);
    }


    /**
     * Find the comment in local cache. You can use refresh method if you just
     * save something but didn't find it.
     * @param idForDB The idForDB of the comment, not the String id.
     * @return null if not found.
     */
    public Comment findCommentById(ObjectId idForDB) {
        return data.findCommentById(idForDB);
    }


    public void refreshData() {
        this.data = new MongoDBExtractToCache();
    }
}
