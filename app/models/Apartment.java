package models;

import com.avaje.ebean.Model;
import helpers.Authenticator;
import play.Logger;
import play.data.Form;
import play.mvc.Security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajla on 22-Dec-15.
 */
@Entity
public class Apartment extends Model {
    @Id
    public Integer id;
    public String name;
    public String location;
    public String address;
    public Integer price;
    public Integer capacity;
    @Column(columnDefinition = "TEXT")
    public String description;
    public String lat;
    public String lng;
    public Integer userId;

    @Column
    public Boolean isVisible;
    /**
     * Default constructor
     * @param id
     * @param name
     * @param title
     * @param location
     * @param neighborhood
     * @param address
     * @param price
     * @param capacity
     * @param beds
     * @param rooms
     * @param area
     * @param floor
     * @param description
     * @param lat
     * @param lng
     */
    public Apartment(Integer id, String name,String location, String address, Integer price, Integer capacity,
                      String description, String lat, String lng, Integer userId, Boolean isVisible) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.address = address;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.userId = userId;
        this.isVisible = isVisible;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", capacity=" + capacity +
                ", description=" + description +
                ", lat=" + lat +
                ", lng=" + lng +
                ",userId=" + userId +
                ",isVisible=" + isVisible +
                '}';
    }

    private static Form<Apartment> form = Form.form(Apartment.class);
    private static Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);

    /**
     * Creates an Apartment and saves it in the DB
     * @return
     */
    /* --------------- create apartment ---------------*/
    @Security.Authenticated(Authenticator.AdminFilter.class)
    public static Apartment createApartment(Integer userId) {
        Form<Apartment> boundForm = form.bindFromRequest();
        Apartment apartment = null;
        try {
            apartment = boundForm.get();

            apartment.userId = userId;
            apartment.isVisible = false;
            apartment.save();

            return apartment;
        } catch (Exception e) {
            Logger.debug("Nisam uspio spasiti apartman :(");
            return apartment;
        }
    }
    /* --------------- update apartment ---------------*/
    @Security.Authenticated(Authenticator.AdminFilter.class)
    public static Apartment updateApartment(Integer apartmentId) {
        Form<Apartment> boundForm = form.bindFromRequest();

        Apartment apartment = Apartment.getApartmentById(apartmentId);
        try {
            String name = boundForm.field("name").value();
            String location = boundForm.field("location").value();
            String address = boundForm.field("address").value();
            Integer price = Integer.parseInt(boundForm.field("price").value());
            Integer capacity = Integer.parseInt(boundForm.field("capacity").value());
            String description = boundForm.field("description").value();
            String lat = boundForm.field("lat").value();
            String lng = boundForm.field("lng").value();

            apartment.name = name;
            apartment.location = location;
            apartment.address = address;
            apartment.price = price;
            apartment.capacity = capacity;
            apartment.description = description;
            apartment.lat = lat;
            apartment.lng = lng;

            apartment.update();

            return apartment;
        } catch (Exception e) {
            Logger.debug("Nisam uspio spasiti apartman :(");
            return apartment;
        }
    }
    /* --------------- delete apartment ---------------*/
    @Security.Authenticated(Authenticator.AdminFilter.class)
    public static void deleteApartment(Integer apartmentId){
        Apartment apartment = finder.where().eq("id", apartmentId).findUnique();
        apartment.delete();
    }

        /* --------------- retrieves all apartments ---------------*/
    public static List<Apartment> getAllApartments(){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);
        List<Apartment> apartments = finder.all();
        return apartments;
    }

    /**
     * Retrieves an apartment by provided id.
     * @param apartmentId
     * @return
     */
    public static Apartment getApartmentById(Integer apartmentId) {
        return finder.where().eq("id", apartmentId).findUnique();
    }
        /* --------------- retrieves apartments with neighbourhood centar ---------------*/

    public static List<Apartment> apartmentsCentar(){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);
        List<Apartment> apartments = finder.where().eq("neighborhood", "Centar").findList();
        return apartments;
    }
        /* --------------- retrieves apartments with neighbourhood novo sarajevo ---------------*/

    public static List<Apartment> apartmentsNSarajevo(){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);
        List<Apartment> apartments = finder.where().eq("neighborhood", "Novo Sarajevo").findList();
        return apartments;
    }
        /* --------------- retrieves apartments with neighbourhood novi grad ---------------*/

    public static List<Apartment> apartmentsNGrad(){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);
        List<Apartment> apartments = finder.where().eq("neighborhood", "Novi Grad").findList();
        return apartments;
    }
        /* --------------- retrieves apartments with neighbourhood stari grad ---------------*/

    public static List<Apartment> apartmentsSGrad(){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);
        List<Apartment> apartments = finder.where().eq("neighborhood", "Stari Grad").findList();
        return apartments;
    }
        /* --------------- retrieves apartments with neighbourhood ilidza ---------------*/

    public static List<Apartment> apartmentsIlidza(){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);
        List<Apartment> apartments = finder.where().eq("neighborhood", "Ilidza").findList();
        return apartments;
    }

            /* --------------- retrieves apartments with location Sarajevo ---------------*/

    public static List<Apartment> apartmentsSarajevo(){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);
        List<Apartment> apartments = finder.where().eq("location", "Sarajevo").findList();
        return apartments;
    }


    /* --------------- retrieves list of images names for the current apartment ---------------*/

    public static List<String> getListOfApartmentImages(Apartment apartment) {
        List<String> results = new ArrayList<>();

        String folderName = apartment.name + apartment.id;
        String location = "E:/StanNaDan/public/apartmentPhotos/" + folderName;

        File[] files = new File(location).listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    results.add("/assets/apartmentPhotos/" + folderName + "/" + file.getName());
                }
            }
        }
        return results;
    }

    /* --------------- retrieves first picture name for the current apartment ---------------*/
    public static String getFirstImage(Apartment apartment) {
        List<String> images = getListOfApartmentImages(apartment);

        return (images.size() > 0) ? images.get(0) : "/assets/images/igraonica.jpg";
    }

    /* --------------- Checks if images list for the current apartement is empty ---------------*/
    public static Boolean imagesListIsEmpty(Apartment apartment) {
        List<String> images = getListOfApartmentImages(apartment);
        return (images.size() > 0) ? false : true;
    }

        /* --------------- Finds all users apartments ---------------*/

    public static List<Apartment> userApartments(Integer userId){

        List<Apartment> apartments = finder.where().eq("user_id", userId).findList();

        return apartments;
    }

            /* --------------- Apartments to recommend ---------------*/

    public static List<Apartment> apartmentsToRecommend(Integer apartmentId){
        Model.Finder<String, Apartment> finder = new Model.Finder<>(Apartment.class);

        List<Apartment> recommendedApartments = new ArrayList<>();

        Apartment apartment = finder.where().eq("id", apartmentId).findUnique();
        Integer price = apartment.price;

        List<Apartment> apartments = finder.where().eq("location", apartment.location).findList();
        List<Integer> prices = new ArrayList<>();

        for(int k=0; k < apartments.size(); k++) {
            prices.add(apartments.get(k).price);
        }

        for(int i=0; i < prices.size(); i++) {
            for (int j = price - 10; j <= price + 10; j++) {
                if (apartments.get(i).price == j) {
                    recommendedApartments.add(apartments.get(i));
                }

            }
        }
        return recommendedApartments;
    }

            /* --------------- Apartment visibility on homepage ---------------*/

    public static void isVisible(Integer apartmentId){
        Apartment apartment = Apartment.getApartmentById(apartmentId);
        if(apartment.isVisible == false) {
            apartment.isVisible = true;
        }else if(apartment.isVisible == true){
            apartment.isVisible = false;
        }
        apartment.update();
    }

              /* --------------- List of apartments for homepage ---------------*/

    public static List<Apartment> apartmentsForHomepage(){
        List<Apartment> apartments = finder.where().eq("isVisible", true).findList();
        return apartments;
    }

}

