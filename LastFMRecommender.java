// Jonathan Harrington A4 - Algo Graphs - 06/16/2023
import java.util.*;
import java.io.*;

public class LastFMRecommender {

    // class variables for graphs and other needed data structures
    private HashMap<Integer, ArrayList<Integer>> userFriendsGraph = new HashMap<>();
    private HashMap<Integer, ArrayList<Integer>> userArtistsGraph = new HashMap<>();
    private HashMap<Integer, String> artistNames = new HashMap<>();
    private HashMap<Integer, Integer> artistPlayCounts = new HashMap<>();

    //
    // Task one create constructors and parse each data file
    //

    // Constructors
    // path prefix if needed for .dat files here. I kept my files in a separate folder.
    LastFMRecommender(String artistsFile, String userArtistsFile, String userFriendsFile) throws FileNotFoundException,
            IOException {
        String pathPrefix = "src/lastfm_dataset/";
        //
        // Loading data from artists.dat
        //
        BufferedReader reader = new BufferedReader(new FileReader(pathPrefix + artistsFile));
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split("\t");
            int id = Integer.parseInt(fields[0]);
            String name = fields[1];
            artistNames.put(id, name);
        }
        reader.close();
        //
        // Loading data from user_artists.dat
        //
        reader = new BufferedReader(new FileReader(pathPrefix + userArtistsFile));
        reader.readLine();
        // populate the graphs
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split("\t");
            int userId = Integer.parseInt(fields[0]);
            int artistId = Integer.parseInt(fields[1]);
            int playCount = Integer.parseInt(fields[2]);

            userArtistsGraph.putIfAbsent(userId, new ArrayList<>());
            userArtistsGraph.get(userId).add(artistId);

            artistPlayCounts.put(artistId, artistPlayCounts.getOrDefault(artistId, 0) + playCount);
        }
        reader.close();
        //
        // Loading data from user_friends.dat
        //
        reader = new BufferedReader(new FileReader(pathPrefix + userFriendsFile));
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split("\t");
            int userId = Integer.parseInt(fields[0]);
            int friendId = Integer.parseInt(fields[1]);

            userFriendsGraph.putIfAbsent(userId, new ArrayList<>());
            userFriendsGraph.get(userId).add(friendId);
        }
        reader.close();

        // testing print outs for graphs to make sure they were constructed correctly.
        // System.out.println(userFriendsGraph);
        // System.out.println(artistNames);
        // System.out.println(userArtistsGraph);
        // System.out.println(artistPlayCounts);

    }
    // task 2: take the pk of the user and list their freinds pk
    void listFriends(int user) {
        // Check if the user has any friends
        if (!userFriendsGraph.containsKey(user)) {
            System.out.println("This user has no friends.");
            return;
        }

        // Retrieve the list of friends from userFriendsGraph for the given user
        ArrayList<Integer> friendsList = userFriendsGraph.get(user);

        // Print out the list
        System.out.println("Friends of user " + user + ":");
        for (int friend : friendsList) {
            System.out.println(friend);
        }
    }
    // task 3 take the pk of 2 users and see what friends they have in common list the pks of the friends they have same
    void commonFriends(int user1, int user2) {
        // Check if the users exist and have friends
        if (!userFriendsGraph.containsKey(user1) || !userFriendsGraph.containsKey(user2)) {
            System.out.println("One or both users do not have any friends.");
            return;
        }
        // Retrieve the lists of friends from userFriendsGraph for both users
        ArrayList<Integer> friendsList1 = userFriendsGraph.get(user1);
        ArrayList<Integer> friendsList2 = userFriendsGraph.get(user2);
        // Make a copy of list1 and retain only the elements that are also in list2
        ArrayList<Integer> commonFriendsList = new ArrayList<>(friendsList1);
        commonFriendsList.retainAll(friendsList2);
        // Print out the common friends
        System.out.println("Common friends of user " + user1 + " and user " + user2 + ":");
        if (commonFriendsList.isEmpty()){
            System.out.println("None");
        } else {
            for (int friend : commonFriendsList) {
                System.out.println(friend);
            }
        }
    }
    // task 4 take the pk of 2 users and return list the actual name of the artist they have in common
    void listArtists(int user1, int user2) {
        // makes sure the users lists artist
        if (!userArtistsGraph.containsKey(user1) || !userArtistsGraph.containsKey(user2)) {
            System.out.println("One or both users do not listen to any artists.");
            return;
        }

        // Retrieve the lists of artists from userArtistsGraph for both users
        ArrayList<Integer> artistList1 = userArtistsGraph.get(user1);
        ArrayList<Integer> artistList2 = userArtistsGraph.get(user2);

        // Make a copy of list1 and retain only the elements that are also in list2
        ArrayList<Integer> commonArtistList = new ArrayList<>(artistList1);
        commonArtistList.retainAll(artistList2);

        // Print out the common artists
        System.out.println("Common artists of user " + user1 + " and user " + user2 + ":");
        if (commonArtistList.isEmpty()){
            System.out.println("None");
        } else {
            for (int artist : commonArtistList) {
                System.out.println(artistNames.get(artist));
            }
        }
    }
    // task 5 this will return the top t10 artits given a specific data set
    void listTop10() {
        // PriorityQueue used for making entries in artistPlayCounts, ordered by the number of plays
        PriorityQueue<Map.Entry<Integer, Integer>> queue = new PriorityQueue<>(
                (a, b) -> b.getValue() - a.getValue()
        );
        queue.addAll(artistPlayCounts.entrySet());

        // grab the top 10 entries from the PriorityQueue and print them out
        //
        System.out.println("Top 10 artists:");
        for (int i = 0; i < 10 && !queue.isEmpty(); i++) {
            Map.Entry<Integer, Integer> entry = queue.poll();
            System.out.println(artistNames.get(entry.getKey()) + ": " + entry.getValue());
        }
    }
    // task 6 take a user pk and return a list of artist recomendation based on their friends list.
    void recommend10(int user) {
        // Create a new count map similar to artistPlayCounts
        HashMap<Integer, Integer> recommendationCounts = new HashMap<>();

        // Use the friend list in the  userFriendsGraph to find rec songs,
        if (userFriendsGraph.containsKey(user)) {
            for (int friend : userFriendsGraph.get(user)) {
                // For artist in userArtistsGraph grab the friend info,
                if (userArtistsGraph.containsKey(friend)) {
                    for (int artist : userArtistsGraph.get(friend)) {
                        // Incre the artist count in a map
                        recommendationCounts.put(artist, recommendationCounts.getOrDefault(artist, 0) + 1);
                    }
                }
            }
        }

        // PriorityQueue of entries in the new map, should be ordered by play count
        PriorityQueue<Map.Entry<Integer, Integer>> queue = new PriorityQueue<>(
                (a, b) -> b.getValue() - a.getValue()
        );
        queue.addAll(recommendationCounts.entrySet());

        // pull top 10 entries from the PriorityQueue and print them out
        System.out.println("Recommendations for user " + user + ":");
        for (int i = 0; i < 10 && !queue.isEmpty(); i++) {
            Map.Entry<Integer, Integer> entry = queue.poll();
            System.out.println(artistNames.get(entry.getKey()));
        }
    }

    // task 7 make a terminal interface

    ////
    // Terminal interface
    ////
    // This is also where the specific dat files are inputed to be parsed
    // use the method name as commands
    // type help for list of commands
    public static void main(String[] args) throws IOException {
        LastFMRecommender recommender = new LastFMRecommender("artists.dat",
                "user_artists.dat", "user_friends.dat");


        // Simple command line interface
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("-------------------------\n" +
                    "Please enter a command or type help:");
            String command = scanner.nextLine();

            if (command.startsWith("listFriends ")) {
                int user = Integer.parseInt(command.split(" ")[1]);
                recommender.listFriends(user);
            } else if (command.startsWith("commonFriends ")) {
                int user1 = Integer.parseInt(command.split(" ")[1]);
                int user2 = Integer.parseInt(command.split(" ")[2]);
                recommender.commonFriends(user1, user2);
            } else if (command.startsWith("listArtists ")) {
                int user1 = Integer.parseInt(command.split(" ")[1]);
                int user2 = Integer.parseInt(command.split(" ")[2]);
                recommender.listArtists(user1, user2);
            } else if (command.equals("listTop10")) {
                recommender.listTop10();
            } else if (command.startsWith("recommend10 ")) {
                int user = Integer.parseInt(command.split(" ")[1]);
                recommender.recommend10(user);
            } else if (command.equals("exit")) {
                break;
            } else if (command.equals("help")){
                System.out.print("-------------------------\n" +
                                "-^ Here is a list of commands ^-\n" +
                                "-^ pk = \"user id of a specific user\" ^-\n" +
                                "-^ Below are the list of available commands: ^-\n" +
                                "listFriends pk\n" +
                                "commonFriends pk pk\n" +
                                "listArtists pk\n" +
                                "listTop10\n" +
                                "recommend10 pk\n" +
                                "exit\n" +
                                "-------------------------\n"
                        );

            } else {
                System.out.println("Invalid command.");
            }
        }
        scanner.close();
    }
}
