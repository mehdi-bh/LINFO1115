package utils;

import com.opencsv.CSVReader;
import model.Designer;
import model.Followed;
import model.Shot;

import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

public class CSVHandler {
    // Create a graph without all information, only with followers.csv
    public static HashMap<Designer,HashSet<Followed>> createPartialGraph(String followers_file){
        HashMap<Designer,HashSet<Followed>> graph = new HashMap<>();

        try {
            CSVReader reader = new CSVReader(new FileReader(followers_file));
            reader.skip(1);

            String[] line = reader.readNext();
            while(line != null){
                Designer follower = new Designer(Integer.parseInt(line[0]));
                Designer followed = new Designer(Integer.parseInt(line[1]));
                Followed follow = new Followed(followed,Integer.parseInt(line[2]));

                if (!graph.containsKey(follower)) {
                    graph.put(follower, new HashSet<>());
                }
                graph.get(follower).add(follow);
                line = reader.readNext();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return graph;
    }

    // Create a graph with all information
    public static HashMap<Designer, HashSet<Followed>> createCompleteGraph(String followers_file, String designers_file, String shots_file){
        HashMap<Designer, HashSet<Followed>> graph = new HashMap<>();

        // Shots
        HashMap<Integer, HashSet<Shot>> shots = new HashMap<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(shots_file));
            reader.skip(1);

            String[] line = reader.readNext();
            while(line != null){
                Designer designer = new Designer(Integer.parseInt(line[0]));
                Shot shot = new Shot(Integer.parseInt(line[1]),Integer.parseInt(line[2]));

                if (!shots.containsKey(designer.getId())) {
                    shots.put(designer.getId(),new HashSet<>());
                }
                shots.get(designer.getId()).add(shot);
                line = reader.readNext();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Designers
        try {
            CSVReader reader = new CSVReader(new FileReader(designers_file));
            reader.skip(1);

            String[] line = reader.readNext();
            while(line != null){
                int id = Integer.parseInt(line[0]);
                Designer designer = new Designer(id, line[1].trim(),shots.get(id) == null ? new HashSet<>() : shots.get(id));

                if (!graph.containsKey(designer)) {
                    graph.put(designer, new HashSet<>());
                }

                line = reader.readNext();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Followers
        try {
            CSVReader reader = new CSVReader(new FileReader(followers_file));
            reader.skip(1);

            String[] line = reader.readNext();
            while(line != null){
                Designer follower = new Designer(Integer.parseInt(line[0]));

                int idFollowed = Integer.parseInt(line[1]);
                Designer followed = new Designer(idFollowed, line[1].trim(),shots.get(idFollowed) == null ? new HashSet<>() : shots.get(idFollowed));
                Followed follow = new Followed(followed, Integer.parseInt(line[2]));

                graph.get(follower).add(follow);
                line = reader.readNext();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return graph;
    }
}
