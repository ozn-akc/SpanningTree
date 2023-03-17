package de.seven.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.seven.entities.Node;
import de.seven.entities.Path;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
        String content = Files.readString(java.nio.file.Path.of(file.getPath()));
        DataTransferObject object = objectMapper.readValue(content, DataTransferObject.class);
        nodes = object.getNodes();
        paths = completePaths(object.getPaths());
        calculatePath();
        System.out.println("test");
    }

    public void calculatePath(){
        int randomInt = new Random().nextInt(nodes.size());
        nodes.forEach( node -> {
            node.setRoot(node);
            node.setValue(0);
            node.setRoutDepth(0);
        });
        Node start = nodes.get(randomInt);
        broadcast(start);
    }

    private void broadcast(Node sender){
        List<Path> senderPaths= paths.stream()
                .filter(path -> path.hasNode(sender))
                .filter(path -> path.getOtherNode(sender).getRoutDepth()<=4)
                .toList();
        senderPaths.forEach(path -> checkRoute(sender, path));
        senderPaths.forEach(path -> broadcast(path.getOtherNode(sender)));
    }

    private void checkRoute(Node sender, Path path){
        Node receiver = path.getOtherNode(sender);
        receiver.setRoutDepth(receiver.getRoutDepth()+1);
        if(sender.getRoot().getId()<receiver.getRoot().getId()){
            receiver.setRoot(sender);
            receiver.setValue(receiver.getRoot().getValue() + sender.getValue());
        }else if(sender.getRoot().getId()==receiver.getRoot().getId()){

        }
    }

    private List<Path> completePaths(List<Path> incomplete){
        incomplete.forEach(path -> {
            path.setTo(getNodeForPath(path.getTo()));
            path.setFrom(getNodeForPath(path.getFrom()));
        });
        return incomplete;
    }

    private Node getNodeForPath(Node incompleteNode){
        if(incompleteNode.getId()!=null){
            return nodes.stream().filter(node -> node.getId().equals(incompleteNode.getId())).findFirst().orElse(new Node());
        }else{
            return nodes.stream().filter(node -> node.getName().equals(incompleteNode.getName())).findFirst().orElse(new Node());
        }
    }
}
