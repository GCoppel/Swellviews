import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.io.*;

import com.google.gson.*; //import for Gson capabilities

import moviemodel.*; //import moviemodel package

/**
 * The homepage. Contains the Main.
 * Contains static variables:
 * movieCounter - used to keep track of the current position in an ArrayList of movies
 * movieEnd  - used when the movieGrid displays less than 8 items. Keeps track of how many extra spaces there were for use when going backwards in the grid.
 * movieListEnd - used to know when the ArrayList of movies has ended. 0 if there are more to read, 1 if not.
 * arrayListName - an ArrayList of Movie objects that is given new ArrayLists by Search, Filter, Sort, and Collection models. Used by movie display views.
 * displayName - a String used by movieGridUpdater
 * currentUser - a User object that holds the username of the person logged in. Is set to username=john and password=doe by default. Used by MovieDetailsDisplay to know which user to add a movie to.
 */
public class Home extends JFrame {

    private static int movieCounter = 0;
    private static int movieEnd = 0;
    private static int movieListEnd = 0;
    private static ArrayList<Movie> arrayListName = new ArrayList<Movie>();

    private static String displayName;

    private static User currentUser = new User("john", "doe");

    private static int darkMode = 0;

    //STATIC BUTTONS/CHECKBOXES USED FOR FILTERING THE MOVIE ARRAYLIST
    static JButton buttonApply = new JButton("Apply Filters");
    static JCheckBox ratedG = new JCheckBox("G");
    static JCheckBox ratedPG = new JCheckBox("PG");
    static JCheckBox ratedPG13 = new JCheckBox("PG-13");
    static JCheckBox ratedR = new JCheckBox("R");
    static JCheckBox ratedUR = new JCheckBox("Unrated");
    static JCheckBox genreAction = new JCheckBox("Action");
    static JCheckBox genreAdventure = new JCheckBox("Adventure");
    static JCheckBox genreMystery = new JCheckBox("Mystery");
    static JCheckBox genreRomance = new JCheckBox("Romance");
    static JCheckBox genreHorror = new JCheckBox("Horror");
    static JCheckBox genreComedy = new JCheckBox("Comedy");
    static JCheckBox genreDocumentary = new JCheckBox("Documentary");
    static JCheckBox genreDrama = new JCheckBox("Drama");
    static JCheckBox genreShort = new JCheckBox("Short");
    static JCheckBox genreSciFi = new JCheckBox("SciFi");
    static JCheckBox genreCrime = new JCheckBox("Crime");
    static JCheckBox genreThriller = new JCheckBox("Thriller");
    static JCheckBox genreFantasy = new JCheckBox("Fantasy");
    static JCheckBox genreAnimation = new JCheckBox("Animation");
    static JCheckBox genreFamily = new JCheckBox("Family");
    static JCheckBox genreMusical = new JCheckBox("Musical");
    static JCheckBox genreBiography = new JCheckBox("Biography");
    static JCheckBox genreSport = new JCheckBox("Sport");
    static JCheckBox genreHistory = new JCheckBox("History");

    //STATIC BUTTONS/CHECKBOXES USED FOR SORTING THE CURRENT MOVIE ARRAYLIST
    static JButton buttonApplySort = new JButton("Apply Sort");
    static JRadioButton sortAZ = new JRadioButton("A-Z");
    static JRadioButton sortZA = new JRadioButton("Z-A");
    static JRadioButton sortOldNew = new JRadioButton("Oldest to Newest");
    static JRadioButton sortNewOld = new JRadioButton("Newest to Oldest");
    static JRadioButton sortShortLong = new JRadioButton("Shortest to Longest");
    static JRadioButton sortLongShort = new JRadioButton("Longest to Shortest");

    //STATIC ARRAYLIST TO HOLD ALL USER DATA
    static ArrayList<User> userDatabase = new ArrayList<User>();

    //STATIC ARRAYLISTS FOR LIKED AND DISLIKED MOVIES
    static ArrayList<Movie> likedMovies = new ArrayList<Movie>();
    static ArrayList<Movie> dislikedMovies = new ArrayList<Movie>();

    static Image icon = Toolkit.getDefaultToolkit().getImage("src\\SwellviewsLogo.png");

    public static void main(String[] args) {

        //GSON IMPLEMENTATION CODE--------------------------------------------------------------------------------------
        String jsonString = "";
        Scanner inFile = null;
        try {
            inFile = new Scanner(new FileReader("src\\\\SampleMovieFile.json"));
        } catch (FileNotFoundException fe) {
            System.out.println("The file could not be opened.");
            System.exit(0);
        }

        // Build the jsonString object line by line
        while (inFile.hasNextLine()) {
            jsonString = jsonString + inFile.nextLine();
        }

        Gson gson = new Gson();
        Movie[] movieList;  //A java primitive of moviemodel.Movie class
        ArrayList<Movie> CompleteMovieArrayList = new ArrayList<Movie>(); // An array list to hold a collection of movies
        movieList = gson.fromJson(jsonString, Movie[].class);
        Collections.addAll(CompleteMovieArrayList, movieList);

        arrayListName = CompleteMovieArrayList;
        //--------------END GSON IMPLEMENTATION-------------------------------------------------------------------------

        //USER DATA GSON IMPLEMENTATION -------------------------
        String newjsonString = "";
        Scanner inFileUser = null;
        try {
            inFileUser = new Scanner(new FileReader("src\\UserData.json"));
        } catch (FileNotFoundException fe) {
            System.out.println("The file could not be opened.");
            System.exit(0);
        }

        // Build the jsonString object line by line
        while (inFileUser.hasNextLine()) {
            newjsonString = newjsonString + inFileUser.nextLine();
        }

        //Build an ArrayList with all of the users found in the JSON String
        Gson gsonUser = new Gson();
        User[] userList;  //A java primitive of the User class
        userList = gsonUser.fromJson(newjsonString, User[].class);
        Collections.addAll(userDatabase, userList);

        //END USER DATA IMPLEMENTATION-----------------------------

        //Homepage Attribute Declarations
        JFrame homeFrame = new JFrame("Swellviews");
        homeFrame.setIconImage(icon);
        JTextField searchField = new JTextField("Enter Movie Name"); //figureout how to erase text on click in field
        //so can search without having to delete default text, or just make label (see accountmenu/login)
        JButton buttonSearch = new JButton("Search");
        JSeparator spacer = new JSeparator(); //Temporary Solution (maybe?) for separating header buttons
        JButton buttonFilter = new JButton("Filter");//JCheckBox allows multiselect, JRadioButton allows single
        JButton buttonSort = new JButton("Sort");
        JButton buttonCollections = new JButton("Collections");
        JButton buttonAccount = new JButton("Account");

        //Homepage Header Attributes (added from above)
        JMenuBar header = new JMenuBar();
        header.setLayout(new GridLayout(1, 8));
        header.add(searchField);
        header.add(buttonSearch);
        header.add(spacer);
        header.add(buttonSort);
        header.add(buttonFilter);
        header.add(buttonCollections);
        header.add(buttonAccount);

        JPanel forwardAndBackButtons = new JPanel();

        displayName = "Browse"; //Contains homepage grid name for display. Will be changed to show where movies are coming from (Collections, Search Results, etc.)

        //MovieDisplay grid layout on homepage:
        JPanel movieGrid = new JPanel();
        movieGrid.setLayout(new GridLayout(2, 4));

        // DISPLAY MOVIES BY SEARCH------------------------------------------------------------------
        buttonSearch.addActionListener(new ActionListener() {
            ArrayList<Movie> searchedForMovies = new ArrayList<Movie>(); //An array list to hold movies that match search criteria

            @Override
            public void actionPerformed(ActionEvent button_pressed) {
                searchedForMovies.removeAll(searchedForMovies); //Reset the array

                //Since there is no way to tell what category the search term is (horror, title, actor, etc.), each attribute will need to be checked individually and the array of movies that match will need to be checked so it is not put in multiple times

                //SEARCH BY TITLE
                for (Movie testMovie : CompleteMovieArrayList) {
                    if (!searchedForMovies.contains(testMovie)) {
                        if (testMovie.getTitle().toLowerCase().contains(searchField.getText().toLowerCase())) {
                            searchedForMovies.add(testMovie);
                        }
                    }
                }
                //SEARCH BY GENRE
                for (Movie testMovie : CompleteMovieArrayList) {
                    if (!searchedForMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains(searchField.getText())) {
                            searchedForMovies.add(testMovie);
                        }
                    }
                }
                //SEARCH BY YEAR
                for (Movie testMovie : CompleteMovieArrayList) {
                    String searchedForYear = String.valueOf(testMovie.getYear());
                    if (!searchedForMovies.contains(testMovie)) {
                        if (searchField.getText().contains(searchedForYear)) {
                            searchedForMovies.add(testMovie);
                        }
                    }
                }
                //SEARCH BY DIRECTOR
                for (Movie testMovie : CompleteMovieArrayList) {
                    if (!searchedForMovies.contains(testMovie)) {
                        if (testMovie.getDirector().contains(searchField.getText())) {
                            searchedForMovies.add(testMovie);
                        }
                    }
                }
                //SEARCH BY ACTORS
                for (Movie testMovie : CompleteMovieArrayList) {
                    if (!searchedForMovies.contains(testMovie)) {
                        if (testMovie.getActors().contains(searchField.getText())) {
                            searchedForMovies.add(testMovie);
                        }
                    }
                }
                //SEARCH BY WRITER
                for (Movie testMovie : CompleteMovieArrayList) {
                    if (!searchedForMovies.contains(testMovie)) {
                        if (testMovie.getWriters().contains(searchField.getText())) {
                            searchedForMovies.add(testMovie);
                        }
                    }
                }
                //Display movies that were found in the search
                arrayListName = searchedForMovies;
                movieCounter = 0;
                movieGrid.removeAll();
                SwingUtilities.updateComponentTreeUI(homeFrame);
                movieListEnd = 0;
                displayName = "Search Results";
                movieGridUpdater(homeFrame, forwardAndBackButtons, arrayListName, movieGrid);
            }
        });

        //SORT THE MOVIES DISPLAYED
        buttonApplySort.addActionListener(new ActionListener() {
            ArrayList<Movie> movieCopy = new ArrayList<Movie>(); //An array list to hold a copy of whatever list in being sorted

            @Override
            public void actionPerformed(ActionEvent button_pressed) {
                //Sort whatever the last ArrayList that was used and display to user based off of what they selected
                movieCopy = (ArrayList<Movie>) arrayListName.clone(); //Clone the array most recently used
                //Sort alphabetically A-Z
                if (sortAZ.isSelected()) {
                    NameCompare nameCompare = new NameCompare();
                    Collections.sort(movieCopy, nameCompare);
                    //Display the sorted movies
                    arrayListName = movieCopy;
                    movieCounter = 0;
                    movieGrid.removeAll();
                    SwingUtilities.updateComponentTreeUI(homeFrame);
                    movieListEnd = 0;
                    movieGridUpdater(homeFrame, forwardAndBackButtons, arrayListName, movieGrid);

                }
                //Sort alphabetically Z-A
                else if (sortZA.isSelected()) {
                    NameCompare nameCompare = new NameCompare();
                    Collections.sort(movieCopy, nameCompare);
                    Collections.reverse(movieCopy);
                    //Display the sorted movies
                    arrayListName = movieCopy;
                    movieCounter = 0;
                    movieGrid.removeAll();
                    SwingUtilities.updateComponentTreeUI(homeFrame);
                    movieListEnd = 0;
                    movieGridUpdater(homeFrame, forwardAndBackButtons, arrayListName, movieGrid);
                }
                //Sort by release old-new
                else if (sortOldNew.isSelected()) {
                    YearCompare yearCompare = new YearCompare();
                    Collections.sort(movieCopy, yearCompare);
                    //Display the sorted movies
                    arrayListName = movieCopy;
                    movieCounter = 0;
                    movieGrid.removeAll();
                    SwingUtilities.updateComponentTreeUI(homeFrame);
                    movieListEnd = 0;
                    movieGridUpdater(homeFrame, forwardAndBackButtons, arrayListName, movieGrid);
                }
                //Sort by release new-old
                else if (sortNewOld.isSelected()) {
                    YearCompare yearCompare = new YearCompare();
                    Collections.sort(movieCopy, yearCompare);
                    Collections.reverse(movieCopy);
                    //Display the sorted movies
                    arrayListName = movieCopy;
                    movieCounter = 0;
                    movieGrid.removeAll();
                    SwingUtilities.updateComponentTreeUI(homeFrame);
                    movieListEnd = 0;
                    movieGridUpdater(homeFrame, forwardAndBackButtons, arrayListName, movieGrid);
                }
                //Sort by runtime short-long
                else if (sortShortLong.isSelected()) {
                    TimeCompare timeCompare = new TimeCompare();
                    Collections.sort(movieCopy, timeCompare);
                    //Display the sorted movies
                    arrayListName = movieCopy;
                    movieCounter = 0;
                    movieGrid.removeAll();
                    SwingUtilities.updateComponentTreeUI(homeFrame);
                    movieListEnd = 0;
                    movieGridUpdater(homeFrame, forwardAndBackButtons, arrayListName, movieGrid);
                }
                //Sort by runtime long-short
                else if (sortLongShort.isSelected()) {
                    TimeCompare timeCompare = new TimeCompare();
                    Collections.sort(movieCopy, timeCompare);
                    Collections.reverse(movieCopy);
                    //Display the sorted movies
                    arrayListName = movieCopy;
                    movieCounter = 0;
                    movieGrid.removeAll();
                    SwingUtilities.updateComponentTreeUI(homeFrame);
                    movieListEnd = 0;
                    movieGridUpdater(homeFrame, forwardAndBackButtons, arrayListName, movieGrid);
                }
            }
        });

        //DISPLAY MOVIES BY USER SELECTED FILTER
        buttonApply.addActionListener(new ActionListener() {
            ArrayList<Movie> filteredMovies = new ArrayList<Movie>(); //An array list to hold movies that match filter criteria

            @Override
            public void actionPerformed(ActionEvent button_pressed) {
                filteredMovies.removeAll(filteredMovies); //Reset the Array
                //Go through each criteria and check the entire database for matching movies. Check if the movie has already been
                //added to the list of found movies. If it has, do not add again
                //FILTER BY MATURITY RATING
                //Since movies only have one rating, this can be condensed into one for loop
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if ((testMovie.getMPARating().equals("G") || testMovie.getMPARating().equals("TV-G")) && ratedG.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Rated G"
                        }
                        if (testMovie.getMPARating().equals("PG") && ratedPG.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Rated PG"
                        }
                        if (testMovie.getMPARating().equals("PG-13") && ratedPG13.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Rated PG-13"
                        }
                        if ((testMovie.getMPARating().equals("R") || testMovie.getMPARating().equals("TV-MA") || testMovie.getMPARating().equals("X")) && ratedR.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Rated R"
                        }
                        if (testMovie.getMPARating().equals("Not Rated") && ratedUR.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Unrated/Not rated"
                        }
                    }
                }
                //SEARCH BY GENRE
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Action") && genreAction.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Action"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Adventure") && genreAdventure.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Adventure"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Mystery") && genreMystery.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Mystery"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Romance") && genreRomance.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Romance"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Horror") && genreHorror.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Horror"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Comedy") && genreComedy.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Comedy"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Documentary") && genreDocumentary.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Documentary"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Drama") && genreDrama.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Drama"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Short") && genreShort.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Short"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Sci-Fi") && genreSciFi.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Sci-Fi"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Crime") && genreCrime.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Crime"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Thriller") && genreThriller.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Thriller"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Fantasy") && genreFantasy.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Fantasy"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Animation") && genreAnimation.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Animation"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Family") && genreFamily.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Family"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Musical") && genreMusical.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Musical"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Biography") && genreBiography.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Biography"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("Sport") && genreSport.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "Sport"
                        }
                    }
                }
                for (Movie testMovie : arrayListName) {
                    if (!filteredMovies.contains(testMovie)) {
                        if (testMovie.getGenres().contains("History") && genreHistory.isSelected()) {
                            filteredMovies.add(testMovie); //Filter: "History"
                        }
                    }
                }
                //Display movies that were found in the search
                arrayListName = filteredMovies;
                movieCounter = 0;
                movieGrid.removeAll();
                SwingUtilities.updateComponentTreeUI(homeFrame);
                movieListEnd = 0;
                movieGridUpdater(homeFrame, forwardAndBackButtons, arrayListName, movieGrid);
            }
        });

        JButton getMoreMovies = new JButton("Show More");
        getMoreMovies.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent button_pressed) {
                if (movieListEnd == 0) {
                    movieGrid.removeAll();
                    SwingUtilities.updateComponentTreeUI(homeFrame);
                    movieGridUpdater(homeFrame, forwardAndBackButtons, arrayListName, movieGrid);
                }
            }
        });

        JButton goBack = new JButton("Go Back");
        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (movieCounter > 15 - movieEnd) {
                    movieGrid.removeAll();
                    SwingUtilities.updateComponentTreeUI(homeFrame);
                    movieCounter = movieCounter - 16 + movieEnd;
                    movieGridUpdater(homeFrame, forwardAndBackButtons, arrayListName, movieGrid);
                    movieEnd = 0;
                    movieListEnd = 0;
                }
            }
        });


        JButton goHome = new JButton("Home");
        goHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent button_pressed) {
                arrayListName = CompleteMovieArrayList;
                movieCounter = 0;
                movieGrid.removeAll();
                SwingUtilities.updateComponentTreeUI(homeFrame);
                movieListEnd = 0;
                displayName = "Browse";
                movieGridUpdater(homeFrame, forwardAndBackButtons, arrayListName, movieGrid);
            }
        });

        forwardAndBackButtons.add(goBack); //Add JButton for going back a page in the list of currently displayed movies
        forwardAndBackButtons.add(getMoreMovies); //Add JButton for going forward a page in the list of currently displayed movies.
        forwardAndBackButtons.add(goHome); //Add JButton for resetting the movieGrid to it's default "browse"

        collectionMenu(buttonCollections, forwardAndBackButtons, homeFrame, movieGrid); // Calls the collectionMenu function and attaches it to the "Collections" button (buttonCollections)
        accountMenu(buttonAccount); // Calls the accountMenu function and attaches it to the "Account" button (buttonAccount)
        filterMenu(buttonFilter); //Calls the filterMenu function and attaches it to the "Filter" button (buttonFilter)
        sortMenu(buttonSort); //Calls the sortMenu function and attaches it to the "Sort" button (buttonSort)

        homeFrame.setLayout(new BorderLayout()); // Sets the homepage frame to a border layout (5 sections: north, south, east, west, and center)

        movieGridUpdater(homeFrame, forwardAndBackButtons, CompleteMovieArrayList, movieGrid); //Call function to set movieGrid to "Browse" for startup.

        //Positioning homepage frame elements
        homeFrame.add(header, BorderLayout.PAGE_START);
        homeFrame.add(forwardAndBackButtons, BorderLayout.PAGE_END);
        homeFrame.setSize(1400, 1100);
        homeFrame.setResizable(false);
        homeFrame.setLocationRelativeTo(null);

        //Standard enabling and closing statements
        homeFrame.setVisible(true);
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /**
     * Used to change the Movie and MovieDisplay obejcts in movieGrid
     * @param home homeFrame JFrame needed to add the updated movieGrid
     * @param forwardAndBackButtons Used for dark mode to change background color
     * @param movieArrayList The ArrayList of movies being viewed
     * @param movieGrid JPanel movieGrid, contents will be re-added and the border name changed based on action being performed
     */
    public static void movieGridUpdater(JFrame home, JPanel forwardAndBackButtons, ArrayList<Movie> movieArrayList, JPanel movieGrid) {

        movieGrid.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), displayName)); //Etched border to display type of content being presented (set by string displayName above)

        //Set background color depending on if darkmode is enabled (1)
        if (darkMode == 0) {
            movieGrid.setBackground(null); //null = default light-grey
            forwardAndBackButtons.setBackground(null); //null = default light-grey
        } else {
            movieGrid.setBackground(Color.darkGray); //Set background color to a darker grey when darkMode is enabled
            forwardAndBackButtons.setBackground(Color.darkGray); //Set background color to a darker grey when darkMode is enabled
        }

        Iterator e = movieArrayList.iterator(); //Iterates through the Arraylist of movies currently being shown to determine how many there are.
        int iteratorCounter = 0; //Stores the number of movies in the Arraylist, used to know where it ends so that the user can't go past.

        while (movieCounter > iteratorCounter) { //Finding value of iteratorCounter to now where the Arraylist ends so the user can't go past it.
            e.next();
            iteratorCounter++;
        }
        //Find the next 8 movies for display and display them. If there are less than 8 to display, break early.
        for (int counter = 0; counter < 8; counter++) {
            if (e.hasNext()) {
                MovieDisplay movie1 = new MovieDisplay(movieArrayList.get(movieCounter).getTitle(), movieArrayList.get(movieCounter).getPosterLink(), darkMode, 1);
                movieCounter++;
                movieGrid.add(movie1);
                new MovieDetailsDisplay(movie1, movieArrayList, movieCounter, darkMode, currentUser, userDatabase); //Simply calling MovieDetailsDisplay does everything
                e.next();
            } else {
                movieEnd = 8 - counter;
                movieListEnd = 1;
                break;
            }
        }
        home.add(movieGrid, BorderLayout.CENTER); //Adds MovieDisplay test to center of page
    }

    /**
     * Controls the account menu where users can log in, log out, create account, and toggle dark mode.
     * "Create Account" is located within the login menu, uses a JSON file run through GSON to directly insert Movie objects.
     * @param buttonAccount JButton for "Account" (used to attach ActionListener that opens the account menu)
     */
    public static void accountMenu(JButton buttonAccount) {

        JPopupMenu accountmenu = new JPopupMenu();
        JButton loginButton = new JButton("Log In/Create an Account");
        JButton toggleDarkMode = new JButton("ToggleDarkMode               ");

        JFrame loginWindow = new JFrame("Login"); //"Popup" window for entering user info
        loginWindow.setIconImage(icon);
        JPanel loginWindowPanel = new JPanel(); //Needed for BoxLayout
        loginWindow.add(loginWindowPanel);
        loginWindowPanel.setLayout(new BoxLayout(loginWindowPanel, BoxLayout.Y_AXIS)); // BoxLayout is simple to organize elements in a column
        loginWindow.setResizable(false); // Makes the window non-resizable

        //Setting size and location of popup menu
        loginWindow.setSize(300, 200);
        loginWindow.setLocationRelativeTo(null);

        JButton enterButton = new JButton("Enter");          // Confirm login button
        JButton createAccountButton = new JButton("Create new account ");

        JButton logoutB = new JButton("Log out                                 ");   // Logout button

        JLabel userLabel = new JLabel("Username:");     // Username Label
        JLabel passLabel = new JLabel("Password:");     // Password Label

        JTextField userField = new JTextField();            // User enters username
        JTextField passField = new JTextField();            // User enters password

        userField.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
        passField.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));

        userField.setSize(190, 10);
        passField.setSize(190, 10);

        loginWindowPanel.add(userLabel); //setMenuLoction(int x, int y) for login window
        loginWindowPanel.add(userField);
        loginWindowPanel.add(passLabel);
        loginWindowPanel.add(passField);
        loginWindowPanel.add(enterButton);
        loginWindowPanel.add(createAccountButton);

        accountmenu.add(loginButton);
        accountmenu.add(toggleDarkMode);


        buttonAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent button_pressed) {
                accountmenu.show(buttonAccount, buttonAccount.getHorizontalAlignment(), buttonAccount.getHeight());
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent button_pressed) {
                loginWindow.setVisible(true);
            }
        });

        JFrame loginFailed = new JFrame("Log-In Failed");
        loginFailed.setIconImage(icon);
        loginFailed.setResizable(false); // Makes the window non-resizable

        //Setting size and location of popup menu
        loginFailed.setSize(250, 100);
        loginFailed.setLocationRelativeTo(null);

        //Contents of loginFailed frame:
        loginFailed.add(new JLabel("    Log-In Failed: Invalid Credentials"));

        //When user clicks "Enter" after inputting credentials:
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent button_pressed) {
                User checkUser = new User(userField.getText(), passField.getText()); //create a temp user from username and password fields for comparison against existing accounts.
                if (checkUser.logIn(checkUser, userDatabase)) {
                    currentUser.setUsername(userField.getText()); //Save username of current user to temp (static) variable to know who/where new info should be saved to
                    accountmenu.add(logoutB); //Add logout button to accountMenu.
                    logoutB.setVisible(true); //Make sure logout button is visible (important if user has already logged out once before).
                    loginButton.setVisible(false); //Hide login button since user must log out first.

                    //Empty temp collections:
                    likedMovies.clear();
                    dislikedMovies.clear();

                    //Load in user's collections from JSON file:
                    for (int i=0; i<userDatabase.size(); i++){
                        if (currentUser.getUsername().equals(userDatabase.get(i).getUsername())){ //Only load the collections of the currently logged in user.
                            likedMovies.addAll(userDatabase.get(i).getLiked()); //Get liked movies and add them to the likedMovies Arraylist used for displaying in movieGrid.
                            dislikedMovies.addAll(userDatabase.get(i).getDisliked()); //Get disliked movies and add them to the dislikedMovies Arraylist used for displaying in movieGrid.
                        }
                    }
                }
                else {
                    loginFailed.setVisible(true); //Notify user of failed login attempt.
                }
                loginWindow.setVisible(false); //Close the window after
            }
        });

        //When user choose to create a new account from within the "Log-In" JFrame menu
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                loginWindow.setVisible(false); //Close login window

                //Creating new popup for account creation
                JFrame createNewUser = new JFrame("Create a New Account");
                JPanel enterUserInfo = new JPanel();
                enterUserInfo.setLayout(new GridLayout(5, 1));
                enterUserInfo.setBorder(BorderFactory.createEmptyBorder(0,5,5,5)); //Add margin to the panel and it's fields so they aren't directly on the edge

                //Adding SwellViews icon to new JFrame
                createNewUser.setIconImage(icon);
                createNewUser.setResizable(false); // Makes the window non-resizable

                //Setting size and location of popup menu
                createNewUser.setSize(250, 200);
                createNewUser.setLocationRelativeTo(null);
                createNewUser.setVisible(true);

                //Creating input fields, labels, and "enter" button
                JLabel newUsernameLabel = new JLabel("Enter New Username:"); //Username
                JTextField newUsername = new JTextField("username");
                JLabel newPasswordLabel = new JLabel("Enter New Password:"); //Password
                JTextField newPassword = new JTextField("password");
                JButton createAccountButton = new JButton("Create Account"); //The JButton used to confirm account creation

                //Adding input field and labels into JPanel
                enterUserInfo.add(newUsernameLabel);
                enterUserInfo.add(newUsername);
                enterUserInfo.add(newPasswordLabel);
                enterUserInfo.add(newPassword);
                enterUserInfo.add(createAccountButton);

                //Adding content to JFrame
                createNewUser.add(enterUserInfo);

                //When user confirms account creation:
                createAccountButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //Add the new user to the temp database before outputting entire database to JSON
                        userDatabase.add(new User(newUsername.getText(), newPassword.getText()));
                        //Output to JSON file through GSON (used to add Movie objects in same format as SampleMovieFile.json)
                        Gson gson = new Gson();
                        try (FileWriter jsonOut = new FileWriter("src\\UserData.json")) { //Open JSON file being outputted to
                            jsonOut.write("[\n"); //needed for start of file
                            for (int i = 0; i < userDatabase.size(); i++) { //Iterates through Arraylist of existing users
                                gson.toJson(userDatabase.get(i), jsonOut); //Adds each user in the Arraylist being iterated into the JSON file using GSON to directly insert Movie objects
                                if(!(i == userDatabase.size() - 1)){ jsonOut.write(", \n");} //only adds when not the last line
                            }
                            jsonOut.write("\n]"); //needed for end of file
                        }
                        catch (IOException exception){
                            exception.printStackTrace();
                        }
                    }
                });
            }
        });

        //Toggles darkMode to ON (1) or OFF (0) when button is clicked.
        toggleDarkMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (darkMode == 1) { //If darkMode is ON (1) turn to OFF (0)
                    darkMode = 0;
                } else { //If darkMode is OFF (0) turn to ON (1)
                    darkMode = 1;
                }
            }
        });

        //Reactivates login menu and resets currentUser to default values when user clicks the logout button
        logoutB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //Re-enable visibility to the log-in and create account buttons, disable visibility on log-out:
                loginButton.setVisible(true);
                logoutB.setVisible(false);
                currentUser.setUsername("john"); //Resets to default username "john"

                //Clear logged in user's collections from GUI:
                likedMovies.clear();
                dislikedMovies.clear();
            }
        });
    }

    /**
     * Creates a popup menu with existing collections and the ability to add a new collection through a button linked to a new JFrame with a JTextField
     * @param buttonCollections JButton buttonCollections, used to attach ActionListener that displays the collection Menu
     * @param forwardAndBackButtons JPanel forwardAndBackButtons contains "Go Back," "Show More," and "Home" buttons needed to call movieGridUpdator
     * @param home homeFrame JFrame needed to call movieGridUpdator
     * @param movieGrid JPanel movieGrid needed to call movieGridUpdator
     */
    public static void collectionMenu(JButton buttonCollections, JPanel forwardAndBackButtons, JFrame home, JPanel movieGrid) {

        JPopupMenu collectionMenu = new JPopupMenu(); // Main popup for list of collections
        JFrame createNewCollection = new JFrame("Create a New Collection"); //"Popup" window for entering the name of a new collection
        JPanel createNewCollectionPanel = new JPanel(); //Needed for BoxLayout
        createNewCollection.add(createNewCollectionPanel);
        createNewCollectionPanel.setLayout(new BoxLayout(createNewCollectionPanel, BoxLayout.Y_AXIS)); // BoxLayout is simple to organize elements in a column
        createNewCollection.setResizable(false); // Makes the window non-resizable

        //Button for creating a new collection:
        JButton createCollection = new JButton("Create a New Collection");

        //Creating items for the collectionMenu list:
        JMenuItem likedMoviesList  = new JMenuItem("Likes");
        JMenuItem dislikedMoviesList = new JMenuItem("Dislikes");

        likedMoviesList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movieCounter = 0;
                movieGrid.removeAll();
                SwingUtilities.updateComponentTreeUI(home);
                movieListEnd = 0;
                displayName = "Liked Movies";
                arrayListName = likedMovies; //arrayListName used by "Show More" and "Go Back" buttons
                movieGridUpdater(home, forwardAndBackButtons, likedMovies, movieGrid);
            }
        });

        dislikedMoviesList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movieCounter = 0;
                movieGrid.removeAll();
                SwingUtilities.updateComponentTreeUI(home);
                movieListEnd = 0;
                displayName = "Disliked Movies";
                arrayListName = dislikedMovies; //arrayListName used by "Show More" and "Go Back" buttons
                movieGridUpdater(home, forwardAndBackButtons, dislikedMovies, movieGrid);
            }
        });

        //Adding items to the collectionMenu list:
        collectionMenu.add(likedMoviesList);
        collectionMenu.add(dislikedMoviesList);

        //ActionListener for showing collection list menu when "Collections" button pressed
        buttonCollections.addActionListener(new ActionListener() { // When the collections button (buttonCollections) is pressed...
            @Override
            public void actionPerformed(ActionEvent button_pressed) {
                collectionMenu.show(buttonCollections, buttonCollections.getHorizontalAlignment(), buttonCollections.getHeight()); //Menu appears below and on the right side of the collections button (buttonCollections)
            }
        });

        //ActionListener for showing new collection creation menu when "Create a New Collection" button pressed
        createCollection.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createNewCollection.setVisible(true);
            }
        });
    }

    /**
     * Creates a JPopupMenu with options for sorting by various criteria.
     * @param buttonSort JButton buttonSort, used to attach ActionListener that reveals the sort menu JPopupMenu
     */
    //Sort by year (oldest to newest, newest to oldest), alphabetically (A-Z, Z-A), or runtime (short to longest, longest to shortest)
    public static void sortMenu(JButton buttonSort) {
        JPopupMenu sortPopupMenu = new JPopupMenu();
        JPanel sortBox = new JPanel();
        sortBox.setLayout(new BoxLayout(sortBox, BoxLayout.Y_AXIS));

        //Connect JPanel to button
        sortPopupMenu.add(sortBox);

        //Build the sort option drop-down
        ButtonGroup sortOptions = new ButtonGroup();
        JLabel alphabetLabel = new JLabel("Alphabetically:");
        JLabel yearLabel = new JLabel("Release Year:");
        JLabel runtimeLabel = new JLabel("Runtime:");

        sortBox.add(alphabetLabel);
        sortBox.add(sortAZ);
        sortBox.add(sortZA);
        sortBox.add(yearLabel);
        sortBox.add(sortOldNew);
        sortBox.add(sortNewOld);
        sortBox.add(runtimeLabel);
        sortBox.add(sortShortLong);
        sortBox.add(sortLongShort);
        sortBox.add(buttonApplySort);

        buttonSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent button_pressed) {
                sortPopupMenu.show(buttonSort, buttonSort.getHorizontalAlignment(), buttonSort.getHeight());
            }
        });
    }

    /**
     * Creates a JPopupMenu with options for filtering the current ArrayList of movies by various criteria.
     * @param buttonFilter JButton buttonFilter, used to attach ActionListener that displays the filter menu JPopupMenu
     */
    //filter: genre, maturity rating, runtime (under 2 hours etc)
    public static void filterMenu(JButton buttonFilter) {
        JPopupMenu filterPopupMenu = new JPopupMenu();
        JPanel filterBox = new JPanel();
        filterBox.setLayout(new BoxLayout(filterBox, BoxLayout.Y_AXIS));

        filterPopupMenu.add(filterBox);

        ButtonGroup filterOptions = new ButtonGroup();

        JLabel ratingFilter = new JLabel("Maturity Rating:");
        JLabel genreLabel = new JLabel("Genre:");
        JButton buttonClear = new JButton("Clear Filters");

        filterBox.add(buttonClear);

        filterBox.add(ratingFilter);
        filterBox.add(ratedG);
        filterBox.add(ratedPG);
        filterBox.add(ratedPG13);
        filterBox.add(ratedR);
        filterBox.add(ratedUR);

        filterBox.add(genreLabel);
        filterBox.add(genreAction);
        filterBox.add(genreAdventure);
        filterBox.add(genreMystery);
        filterBox.add(genreRomance);
        filterBox.add(genreHorror);
        filterBox.add(genreComedy);
        filterBox.add(genreDocumentary);
        filterBox.add(genreDrama);
        filterBox.add(genreShort);
        filterBox.add(genreSciFi);
        filterBox.add(genreCrime);
        filterBox.add(genreThriller);
        filterBox.add(genreFantasy);
        filterBox.add(genreAnimation);
        filterBox.add(genreFamily);
        filterBox.add(genreMusical);
        filterBox.add(genreBiography);
        filterBox.add(genreSport);
        filterBox.add(genreHistory);

        filterBox.add(buttonApply);

        buttonFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent button_pressed) {
                filterPopupMenu.show(buttonFilter, buttonFilter.getHorizontalAlignment(), buttonFilter.getHeight());
            }
        });

        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent button_pressed) {
                ratedG.setSelected(false);
                ratedPG.setSelected(false);
                ratedPG13.setSelected(false);
                ratedR.setSelected(false);
                ratedUR.setSelected(false);

                genreAction.setSelected(false);
                genreAdventure.setSelected(false);
                genreMystery.setSelected(false);
                genreRomance.setSelected(false);
                genreHorror.setSelected(false);
                genreComedy.setSelected(false);
                genreDocumentary.setSelected(false);
                genreDrama.setSelected(false);
                genreShort.setSelected(false);
                genreSciFi.setSelected(false);
                genreCrime.setSelected(false);
                genreThriller.setSelected(false);
                genreFantasy.setSelected(false);
                genreAnimation.setSelected(false);
                genreFamily.setSelected(false);
                genreMusical.setSelected(false);
                genreBiography.setSelected(false);
                genreSport.setSelected(false);
                genreHistory.setSelected(false);
            }
        });
    }
}