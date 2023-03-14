package de.seven.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.seven.entities.Node;
import de.seven.entities.Path;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
@Setter
public class Data {
    List<Node> nodes;
    List<Path> paths;

    public Data (File file) throws IOException {
        //Create ObjectMapper to get Values from File
        ObjectMapper objectMapper = new ObjectMapper();
        //Get Data as LinkedHashMap
        LinkedHashMap object = objectMapper.readValue(file, LinkedHashMap.class);
        //Map LinkedHashMap to List<Nodes>
        this.nodes = objectMapper.convertValue(object.get("nodes"), new TypeReference<List<Node>>() {});
        this.paths = convertPaths((List<LinkedHashMap>) object.get("paths"));
    }

    public void calculatePath(){
        int randomInt = new Random().nextInt(nodes.size());
        for (Node node : nodes) {
            node.setRoot(node);
            node.setValue(0);
        }
        checkPath(nodes.get(randomInt),nodes.get(randomInt), 0);
    }

    private void checkPath(Node self, Node root, int value){
        List<Path> nodePaths = paths.stream()
                .filter(
                        path -> path.hasNode(self)
                )
                .toList();
        if(self.equals(root)){
            for (Path path:
                 nodePaths) {
                checkPath(path.getOtherNode(self), root, value+path.getValue());
            }
        }else{

        }
    }

    private List<Path> convertPaths(List<LinkedHashMap> pathData){
        List<Path> paths = new ArrayList<>();
        pathData.stream().forEach(
                data -> {
                    paths.add(
                            new Path(
                                    getNodeForPath(data.get("from").toString()),
                                    getNodeForPath(data.get("to").toString()),
                                    Integer.parseInt(data.get("value").toString())
                            )
                    );
                }
        );
        return paths;
    }

    private Node getNodeForPath(String value){
        if(isStringNumber(value)){
            return nodes.stream().filter(node -> node.getId().equals(Integer.parseInt(value))).findFirst().orElse(new Node());
        }else{
            return nodes.stream().filter(node -> node.getName().equals(value)).findFirst().orElse(new Node());
        }
    }

    private boolean isStringNumber(String value){
        try{
            Integer id = Integer.parseInt(value);
            return true;
        } catch(Exception e){
            return false;
        }
    }
}
