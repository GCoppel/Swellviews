import moviemodel.Movie;
import java.util.ArrayList;

/**
 * A class for containing the username, password, liked and disliked movies for each different user.
 * Can also be used to check whether or not an account exists and return a boolean value.
 * Has a "get" and "set" for each value: Username, Password, liked (the Arraylist of liked Movie objects for the user), and disliked (the Arraylist of disliked Movie objects for the user).
 */
public class User {

    //leave as default for GSON/JSON and capitalize spelling
    private String Username;
    private String Password;
    private ArrayList<Movie> liked;
    private ArrayList<Movie> disliked;

    //CONSTRUCTOR:
    public User(String username, String password) {
        this.Username = username;
        this.Password = password;
        this.liked = new ArrayList<Movie>();
        this.disliked = new ArrayList<Movie>();
    }

    public java.lang.String getUsername() {
        return Username;
    }

    public void setUsername(String newUsername) {
        Username = newUsername;
    }

    public String getPassword(){
        return Password;
    }

    public void setPassword(String newPassword){
        Password = newPassword;
    }

    public ArrayList<Movie> getLiked(){
        return liked;
    }

    public void addLiked(Movie newMovie){
        liked.add(newMovie);
    }

    public ArrayList<Movie> getDisliked(){
        return disliked;
    }

    public void addDisliked(Movie newMovie){
        disliked.add(newMovie);
    }

    //Log-in and check if valid user
    public boolean logIn(User testUser, ArrayList<User> allUsers) {
        boolean finalValue = false;
        for (User user : allUsers) {
            if (testUser.Username.equals(user.Username)) {
                //This means the username was found, so now check password
                if (testUser.Password.equals(user.Password)) {
                    finalValue = true; //User exists
                }
            }
        }
        return finalValue; //This function will only return true if the username and password matches and is in the arraylist
    }
}
