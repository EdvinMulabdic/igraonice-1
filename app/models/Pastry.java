package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by User on 5/31/2016.
 */
@Entity
public class Pastry extends Model {

    @Id
    public Integer id;
    public String name;
    public String location;
    public String address;
    public String workingHours;

    public String lat;
    public String lng;
    public Boolean isVisible;
    public Integer userId;

    @OneToMany
    public List<Cake> cakes;

    @OneToMany
    public List<Image> images;

    public Pastry(){}

    private static Model.Finder<String, Pastry> finder = new Model.Finder<>(Pastry.class);


        /* --------------- find pastry by id ---------------*/

    public static Pastry findPastryById(Integer pastryId) {
        return finder.where().eq("id", pastryId).findUnique();
    }


     /* --------------- create pastry ---------------*/

    public static Pastry createPastry(String name, String location, String address, String workingHours, String lat, String lng, Integer userId) {
        Pastry pastry = new Pastry();
        pastry.name = name;
        pastry.location = location;
        pastry.address = address;
        pastry.workingHours = workingHours;
        pastry.lat = lat;
        pastry.lng = lng;
        pastry.isVisible = true;
        pastry.userId = userId;

        pastry.save();
        return pastry;
    }


      /* --------------- update pastry ---------------*/

    public static Integer updatePastry(String name, String location, String address, String workingHours, String lat, String lng, Integer pastryId) {
        Pastry pastry = findPastryById(pastryId);
        pastry.name = name;
        pastry.location = location;
        pastry.address = address;
        pastry.workingHours = workingHours;
        pastry.lat = lat;
        pastry.lng = lng;
        pastry.update();
        return pastry.userId;
    }


         /* --------------- delete pastry ---------------*/

    public static Integer deletePastry(Integer pastryId) {
        Pastry pastry = findPastryById(pastryId);
        pastry.delete();
        return pastry.userId;
    }

     /* --------------- Finds all users pastries ---------------*/

    public static List<Pastry> userPastries(Integer userId){

        List<Pastry> pastries = finder.where().eq("user_id", userId).findList();

        return pastries;
    }
}
