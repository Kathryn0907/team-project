package data_access;

import Entities.Comment;
import Entities.Listing;
import Entities.User;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoDBAddSampleData {

    public void addUser(User user, MongoDBUserDAO mongoDBUserDAO) {
        mongoDBUserDAO.saveUser(user);
        System.out.println("Saved User Id: " + user.getId().toHexString());
    }

    public void addListing(Listing listing, MongoDBListingDao mongoDBListingDao) {
        mongoDBListingDao.saveListing(listing);
        System.out.println("Saved Listing Id: " + listing.getId().toHexString());
    }

    public void addComment(Comment comment, MongoDBCommentDAO mongoDBCommentDAO) {
        mongoDBCommentDAO.saveComment(comment);
        System.out.println("Saved Comment Id: " + comment.getIdForDB().toHexString());
    }

    public void deleteUser(User user, MongoDBUserDAO mongoDBUserDAO) {
        mongoDBUserDAO.deleteUser(user);
        System.out.println("Deleted User Id: " + user.getId().toHexString());
    }

    public void deleteListing(Listing listing, MongoDBListingDao mongoDBListingDao) {
        mongoDBListingDao.deleteListing(listing);
        System.out.println("Deleted Listing Id: " + listing.getId().toHexString());
    }

    public void deleteComment(Comment comment, MongoDBCommentDAO mongoDBCommentDAO) {
        mongoDBCommentDAO.deleteComment(comment);
        System.out.println("Deleted Comment Id: " + comment.getIdForDB().toHexString());
    }



    public static void main(String[] args) {

        MongoDBUserDAO mongoDBUserDAO = new MongoDBUserDAO();
        MongoDBListingDao mongoDBListingDAO = new MongoDBListingDao();
        MongoDBCommentDAO mongoDBCommentDAO = new MongoDBCommentDAO();


//        // Create test users
//        User sampleUser1 = new User("sampleUser1", "password123");
//        User sampleUser2 = new User("sampleUser2", "pass456");
//
//
//        // Create test listings
//        Listing listing1 = new Listing(
//                "Downtown Luxury Apartment",
//                sampleUser2,
//                null,
//                Arrays.asList("modern", "downtown", "luxury"),
//                Arrays.asList("apartment"),
//                "Beautiful modern apartment in downtown",
//                200.0,
//                "123 King St, Toronto",
//                2.0,
//                1000.0,
//                2,
//                2,
//                Listing.BuildingType.APARTMENT,
//                true
//        );
//
//        Listing listing2 = new Listing(
//                "Cozy Beach House",
//                sampleUser2,
//                null,
//                Arrays.asList("beach", "cozy", "family-friendly"),
//                Arrays.asList("house"),
//                "Perfect beach getaway",
//                350.0,
//                "456 Beach Rd, Miami",
//                50.0,
//                1500.0,
//                3,
//                2,
//                Listing.BuildingType.HOUSE,
//                true
//        );
//
//        Listing listing3 = new Listing(
//                "Mountain Cabin Retreat",
//                sampleUser2,
//                null,
//                Arrays.asList("mountain", "nature", "peaceful"),
//                Arrays.asList("villa"),
//                "Peaceful mountain retreat",
//                180.0,
//                "789 Mountain Rd, Aspen",
//                100.0,
//                1200.0,
//                2,
//                1,
//                Listing.BuildingType.VILLA,
//                true
//        );
//
//        MongoDBAddSampleData mongoDBAddSampleData = new MongoDBAddSampleData();
//        mongoDBAddSampleData.addUser(sampleUser1,mongoDBUserDAO);
//        mongoDBAddSampleData.addUser(sampleUser2,mongoDBUserDAO);
//        mongoDBAddSampleData.addListing(listing1,mongoDBListingDAO);
//        mongoDBAddSampleData.addListing(listing2,mongoDBListingDAO);
//        mongoDBAddSampleData.addListing(listing3,mongoDBListingDAO);

        ArrayList<User> users = mongoDBUserDAO.getAllUsers();
        ArrayList<Listing> listings = mongoDBListingDAO.getAllListings();
        List<Listing> favourite = new ArrayList<>();

        for (Listing listing : listings) {
            if (listing.getName().equals("Cozy Beach House")) {
                System.out.println(listing.getId().toHexString() + " " + listing.getName());
                favourite.add(listing);
            }
        }


        for (User user : users) {
            if (user.getUsername().equals("sampleUser1")) {
                System.out.println(user.getId().toHexString() + " " + user.getUsername());
                for(Listing listing : user.getFavourite()) {
                    System.out.println(listing.getId().toHexString());
                }

//                for(Listing listing : favourite) {
//                    System.out.println(listing.getId().toHexString() + " " + listing.getName());
//                    user.removeFavourite(listing);
//                }
//
//                mongoDBUserDAO.saveUser(user);
            }
        }
    }

}
