import java.io.*;
import java.util.*;

/**
 * Represents an actor with a name and a set of movies they have acted in.
 * 
 * @author Amir Eihab El Abidou, Arianit Paso, Omar Almassri
 */
class ActorNode {
    String name;
    HashSet<String> movies = new HashSet<>();

    /**
     * Constructs an ActorNode with a specified name.
     * 
     * @param name The name of the actor.
     */
    ActorNode(String name) {
        this.name = name;
    }
}

/**
 * Represents a movie with a title and a set of actors that have acted in it.
 * 
 * @author Amir Eihab El Abidou, Arianit Paso, Omar Almassri
 */
class MovieNode {
    String title;
    HashSet<ActorNode> actors = new HashSet<>();

    /**
     * Constructs a MovieNode with a specified title.
     * 
     * @param title The title of the movie.
     */
    MovieNode(String title) {
        this.title = title;
    }
}

/**
 * Represents a bipartite graph of actors and movies they have acted in.
 * 
 * @author Amir Eihab El Abidou, Arianit Paso, Omar Almassri
 */
class Graph {
    Map<String, ActorNode> actorNodes = new HashMap<>();
    Map<String, MovieNode> movieNodes = new HashMap<>();

    /**
     * Adds an actor with the specified name to the graph. If the actor is already
     * present, does nothing.
     * 
     * @param actorName The name of the actor to add.
     */
    void addActor(String actorName) {
        actorNodes.putIfAbsent(normalizeName(actorName), new ActorNode(actorName));
    }

    /**
     * Adds a movie with the specified title to the graph and connects it to the
     * specified actor. If the movie is already present, it just connects it to the
     * actor.
     * 
     * @param actorName  The name of the actor.
     * @param movieTitle The title of the movie.
     */
    void addMovie(String actorName, String movieTitle) {
        movieNodes.putIfAbsent(movieTitle, new MovieNode(movieTitle));
        ActorNode actor = actorNodes.get(normalizeName(actorName));
        MovieNode movie = movieNodes.get(movieTitle);
        actor.movies.add(movieTitle);
        movie.actors.add(actor);
    }

    /**
     * Performs a breadth-first search (BFS) starting from the specified actor to
     * find the shortest path to all other actors.
     * 
     * @param startActor The name of the actor to start the search from.
     * @return A map of each actor to their parent in the shortest path from the
     *         start actor.
     */
    Map<ActorNode, ActorNode> bfs(String startActor) {
        Map<ActorNode, ActorNode> parentMap = new HashMap<>();
        Queue<ActorNode> queue = new LinkedList<>();
        ActorNode startNode = actorNodes.get(normalizeName(startActor));
        if (startNode == null)
            return parentMap;

        queue.add(startNode);
        parentMap.put(startNode, null);

        while (!queue.isEmpty()) {
            ActorNode currentActor = queue.poll();
            for (String movieTitle : currentActor.movies) {
                MovieNode movie = movieNodes.get(movieTitle);
                for (ActorNode neighborActor : movie.actors) {
                    if (!parentMap.containsKey(neighborActor)) {
                        parentMap.put(neighborActor, currentActor);
                        queue.add(neighborActor);
                    }
                }
            }
        }
        return parentMap;
    }

    /**
     * Normalizes the name of an actor or movie by trimming whitespace and removing
     * certain characters.
     * 
     * @param name The name to normalize.
     * @return The normalized name.
     */
    static String normalizeName(String name) {
        return name.trim().replaceAll("['\"]", "");
    }
}

/**
 * A class to play the Kevin Bacon game, which involves finding the shortest
 * path from any actor to Kevin Bacon in the "six degrees of separation"
 * concept.
 * The main method reads a file containing actor and movie data to build a
 * graph, then prompts the user for actors to find their separation from Kevin
 * Bacon.
 * 
 * @author Amir Eihab El Abidou, Arianit Paso, Omar Almassri
 */
public class KevinBaconGame {

    /**
     * Builds the graph from the given file.
     * 
     * @param filename The name of the file containing actor and movie information.
     * @return The constructed graph.
     * @throws IOException If there is an issue reading the file.
     */
    static Graph buildGraphFromFile(String filename) throws IOException {
        Graph graph = new Graph();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String currentActor = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("<a>")) {
                    currentActor = line.substring(3).trim();
                    graph.addActor(currentActor);
                } else if (line.startsWith("<t>")) {
                    String movieTitle = line.substring(3).trim();
                    graph.addMovie(currentActor, movieTitle);
                }
            }
        }
        return graph;
    }

    /**
     * Finds the shortest path from a specified actor to Kevin Bacon.
     * It does this by using a breadth-first search algorithm and backtracking from
     * the target actor to Kevin Bacon
     * using a map of actor relationships.
     * 
     * @param graph       The graph containing actors and movies.
     * @param targetActor The name of the target actor.
     * @param parentMap   A map representing the shortest path from Kevin Bacon to
     *                    each actor.
     * @return A string representation of the path from the target actor to Kevin
     *         Bacon, including the number of steps.
     */
    public static String getPathToKevinBacon(Graph graph, String targetActor, Map<ActorNode, ActorNode> parentMap) {

        ActorNode targetNode = graph.actorNodes.get(Graph.normalizeName(targetActor));
        if (targetNode == null || !parentMap.containsKey(targetNode)) {
            return "\"" + targetActor + "\"" + " could not be found or has no connection to Kevin Bacon!";
        }
        LinkedList<String> path = new LinkedList<>();
        ActorNode current = targetNode;

        /**
         * Main method to execute the Kevin Bacon game. It builds the graph from a
         * specified file and then
         * interacts with the user to find the shortest path from any actor to Kevin
         * Bacon.
         * 
         * @param args Command-line arguments (not used).
         */
        while (current != null && !current.name.equals("Bacon, Kevin (I)")) {
            ActorNode parent = parentMap.get(current);
            if (parent != null) {
                Set<String> sharedMovies = new HashSet<>(current.movies);
                sharedMovies.retainAll(parent.movies);
                String connectingMovie = sharedMovies.iterator().next();
                path.addFirst("<a>" + current.name + "<a>");
                path.addFirst("<t>" + connectingMovie + "<t>");
            }
            current = parent;
        }
        path.addFirst("<a>Bacon, Kevin (I)<a>");
        int steps = path.size() / 2;

        return "\"" + targetActor + "\" is " + steps + " steps away from Kevin B. The path is "
                + String.join("", path);
    }

    /**
     * Main method to execute the Kevin Bacon game. It builds the graph from a
     * specified file and then
     * interacts with the user to find the shortest path from any actor to Kevin
     * Bacon.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {

        String filename = "D:\\Plugg\\ALDA\\ALDA prog\\Tema 7 - grafer\\moviedata.txt";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Wait for graph and path to be built.");
        try {
            Graph graph = buildGraphFromFile(filename);
            System.out.println("Graph has been built successfully.");

            Map<ActorNode, ActorNode> parentMap = graph.bfs("Bacon, Kevin (I)");
            System.out.println("Path has been built successfully.");
            System.out.print("? ");
            String actorName = scanner.nextLine();

            while (!"quit".equalsIgnoreCase(actorName)) {
                String path = getPathToKevinBacon(graph, actorName, parentMap);
                System.out.println(path);
                System.out.print("? ");
                actorName = scanner.nextLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not read the file: " + filename);
        }
        scanner.close();
    }
}
