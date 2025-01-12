import com.google.gson.Gson;
import moviemodel.Movie;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * \ Creates a new window for a given Movie object within an ArrayList of Movie objects. Displays details from JSON file as well as the objects related MovieDisplay object.
 */
public class MovieDetailsDisplay extends JFrame {

    /**
     * MovieDetailsDisplay constructor, automatically creates new JFrame using MovieDisplay object given and movie details from Movie.java, and attaches new MouseListener to the MovieDisplay given that shows the frame on click.
     * @param movieSelected The MovieDisplay object used in the movieGrid (used to attach MouseListener)
     * @param movieArrayList ArrayList of Movie objects being viewed
     * @param movieCounter The static int "movieCounter", used to know where in the ArrayList the Movie is
     * @param darkMode The static int "darkMode" (or manual 1 if yes, 0 if no)
     * @param currentUser The User object which is currently logged in. Used to find which user object the "add" functions should add a movie to.
     * @param userDatabase The Arraylist of all existing users as compiled from the JSON file by Home.java.
     */
    public MovieDetailsDisplay(MovieDisplay movieSelected, ArrayList<Movie> movieArrayList, int movieCounter, int darkMode, User currentUser, ArrayList<User> userDatabase){

        JFrame movieDetailsFrame = new JFrame(movieArrayList.get(movieCounter - 1).getTitle());
        movieDetailsFrame.setIconImage(Home.icon);
        JPanel movieDetailsRightPanel = new JPanel();
        movieDetailsRightPanel.setLayout(new GridLayout(13,1));

        JLabel movieTitle = new JLabel("Title: " + movieArrayList.get(movieCounter - 1).getTitle());
        JLabel movieYear = new JLabel("Year: " + movieArrayList.get(movieCounter - 1).getYear().toString());
        JLabel movieGenre = new JLabel("Genre: " + movieArrayList.get(movieCounter - 1).getGenres());
        JLabel movieAgeRating = new JLabel("Age Rating: " + movieArrayList.get(movieCounter - 1).getMPARating());
        JLabel movieRuntime = new JLabel("Runtime: " + movieArrayList.get(movieCounter - 1).getRunTime());
        JLabel movieDirector = new JLabel("Director: " + movieArrayList.get(movieCounter - 1).getDirector());
        JLabel movieWriter = new JLabel("Writer: " + movieArrayList.get(movieCounter - 1).getWriters());
        JLabel movieActors = new JLabel("Actors: " + movieArrayList.get(movieCounter - 1).getActors());

        JLabel moviePlotLabel = new JLabel("Plot:");
        JTextArea moviePlot = new JTextArea (movieArrayList.get(movieCounter - 1).getPlot());
            JScrollPane moviePlotContainer = new JScrollPane(moviePlot, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        moviePlot.setEditable(false);
        moviePlot.setWrapStyleWord(true);
        moviePlot.setLineWrap(true);

        JLabel movieLanguage = new JLabel();
        JLabel movieCountry = new JLabel();
        JLabel movieAwards = new JLabel("Awards: " + movieArrayList.get(movieCounter - 1).getAwards());
        JLabel movieRatings = new JLabel();
        JLabel movieRating = new JLabel();

        JPanel rateMovieButtons = new JPanel();
        JButton dislikeMovie = new JButton("Dislike");
        JButton likeMovie = new JButton("Like");
        rateMovieButtons.add(dislikeMovie);
        rateMovieButtons.add(likeMovie);

        movieDetailsRightPanel.add(movieTitle);
        movieDetailsRightPanel.add(movieGenre);
        movieDetailsRightPanel.add(movieYear);
        movieDetailsRightPanel.add(movieAgeRating);
        movieDetailsRightPanel.add(movieRuntime);
        movieDetailsRightPanel.add(movieDirector);
        movieDetailsRightPanel.add(movieWriter);
        movieDetailsRightPanel.add(movieActors);
        movieDetailsRightPanel.add(movieAwards);
        movieDetailsRightPanel.add(moviePlotLabel);
        movieDetailsRightPanel.add(moviePlotContainer);
        movieDetailsRightPanel.add(rateMovieButtons);

        //Add movie to the likedMovies Arraylist used for displaying to movieGrid.
        //Also add movie to the current user's JSON section if logged in
        likeMovie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Home.likedMovies.contains(movieArrayList.get(movieCounter - 1))){ //Prevents duplicate movies in the collection
                    Home.likedMovies.add(movieArrayList.get(movieCounter - 1)); //Add the movie to the likedMovies Arraylist used for displaying to MovieGrid.
                    for (int i=0; i<userDatabase.size(); i++){ //Iterate through all existing user accounts to compare with current user's username
                        if (currentUser.getUsername().equals(userDatabase.get(i).getUsername())){ //If user exists:
                            userDatabase.get(i).addLiked(movieArrayList.get(movieCounter - 1)); //Add the movie to their liked movies collection
                            updateJSON(userDatabase);//Update JSON file of user data
                        }
                    }
                }
            }
        });

        //Add movie to the dislikedMovies Arraylist used for displaying to movieGrid.
        //Also add movie to the current user's JSON section if logged in
        dislikeMovie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Home.dislikedMovies.contains(movieArrayList.get(movieCounter - 1))){ //Prevents duplicate movies in the collection
                    Home.dislikedMovies.add(movieArrayList.get(movieCounter - 1)); //Add the movie to the dislikedMovies Arraylist used for displaying to MovieGrid.
                    for (int i=0; i<userDatabase.size(); i++){ //Iterate through all existing user accounts to compare with current user's username
                        if (currentUser.getUsername().equals(userDatabase.get(i).getUsername())){ //If user exists:
                            userDatabase.get(i).addDisliked(movieArrayList.get(movieCounter - 1)); //Add the movie to their disliked movies collection
                            updateJSON(userDatabase); //Update JSON file of user data
                        }
                    }
                }
            }
        });

        Dimension frameSize = new Dimension(700, 550); //Controls default and min size of details frame
        movieDetailsFrame.setLayout(new GridLayout(1,2));
        movieDetailsFrame.setSize(frameSize);
        movieDetailsFrame.setMinimumSize(frameSize); //Prevents details on right from overlapping with the image if shrunken past the default size
        movieDetailsFrame.setLocationRelativeTo(null);

        if (darkMode == 1) {
            movieTitle.setForeground(Color.white);
            movieGenre.setForeground(Color.white);
            movieYear.setForeground(Color.white);
            movieAgeRating.setForeground(Color.white);
            movieRuntime.setForeground(Color.white);
            movieDirector.setForeground(Color.white);
            movieWriter.setForeground(Color.white);
            movieActors.setForeground(Color.white);
            movieAwards.setForeground(Color.white);
            moviePlotLabel.setForeground(Color.white);
            movieDetailsFrame.getContentPane().setBackground(Color.darkGray);
            moviePlot.setBackground(Color.darkGray);
            moviePlot.setForeground(Color.white);
            movieDetailsRightPanel.setBackground(Color.darkGray);
            rateMovieButtons.setBackground(Color.darkGray);
        }

        //Create a movieDetailsDisplay object for each movie currently being displayed:
        //Uses movieCounter-1 because movieCounter is iterated before this constructor method. Finds the correct movie from the complete list of movies.
        MovieDisplay movieSelectionDisplay = new MovieDisplay(movieArrayList.get(movieCounter-1).getTitle(), movieArrayList.get(movieCounter-1).getPosterLink(), darkMode, 1);

        movieSelected.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { //On click:
                movieDetailsFrame.add(movieSelectionDisplay);
                movieDetailsFrame.add(movieDetailsRightPanel);
                movieDetailsFrame.setVisible(true);
            }
            //These shouldn't be needed:
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    //Same exact code as in Home when new user is added. Can definitely be optimized better.
    /**
     * Used to update the JSON file using GSON to directly add movie objects.
     * @param userDatabase is the arraylist containing all user objects.
     */
    public void updateJSON(ArrayList<User> userDatabase){
        Gson gson = new Gson();
        try (FileWriter jsonOut = new FileWriter("src\\UserData.json")) {
            jsonOut.write("[\n"); //needed for end of file
            for (int i = 0; i < userDatabase.size(); i++) {
                gson.toJson(userDatabase.get(i), jsonOut);
                if(!(i == userDatabase.size() - 1)){ jsonOut.write(", \n");} //only adds when not the last line
            }
            jsonOut.write("\n]"); //needed for end of file
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }
}