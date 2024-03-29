package de.seven.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.seven.entities.Node;
import de.seven.entities.Path;
import lombok.Getter;
import lombok.Setter;

import javax.xml.stream.events.NotationDeclaration;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Data {
    HashMap<Node, List<Path>> data = new HashMap<>();

    public Data (File file) throws IOException {
        //Create ObjectMapper to get Values from File
        ObjectMapper objectMapper = new ObjectMapper();
        //Get Data as LinkedHashMap
        String content = Files.readString(java.nio.file.Path.of(file.getPath()));
        DataTransferObject object = objectMapper.readValue(content, DataTransferObject.class);
        object.setPaths(completePaths(object.getNodes(), object.getPaths()));
        object.getNodes().forEach(node ->
                data.put(
                        node,
                        object.getPaths()
                                .stream()
                                .filter(path -> path.hasNode(node))
                                .toList()
                )
        );
    }

    public void calculatePath(){
        int randomInt = new Random().nextInt(data.size());
        data.forEach( (k,v) -> {
            k.setRoot(k);
            k.setValue(0);
            k.setRootDepth(0);
        });
        Node start = data.keySet().stream().toList().get(randomInt);
        System.out.println("Startnode: " + start.getName());
        newBroadcast(Collections.singletonList(start));
    }

    public void newBroadcast(List<Node> usedNodes){
        HashMap<Node, List<Path>> usedPaths = new HashMap<>();
        usedNodes.forEach(node -> usedPaths.put(node, data.get(node)));
        List<Node> newNodes = new ArrayList<>();
        for (Map.Entry<Node, List<Path>> entry:
            usedPaths.entrySet()){
                entry.getValue().forEach(path ->
                        checkRoot(entry.getKey(), path)
                );
                newNodes.addAll(
                        entry.getValue()
                                .stream()
                                .map(path ->
                                        path.getOtherNode(entry.getKey()))
                                .toList()
                );
        }
        newNodes = newNodes.stream().distinct().filter(node -> node.getRootDepth()<=10).toList();
        if(newNodes.size()!=0){
            newBroadcast(newNodes);
        }
    }

    private void checkRoot(Node sender, Path path){
        Node receiver = path.getOtherNode(sender);
        sender.increaseRootDepth();
        if(getRootNode(sender).getId()<getRootNode(receiver).getId()){
            receiver.setRoot(sender);
        }else if(getRootNode(sender).getId().equals(getRootNode(receiver).getId())){
            if(getPathValue(sender)+path.getValue()<getPathValue(receiver)){
                receiver.setRoot(sender);
            }
        }
        receiver.setValue(getPathValue(receiver));
    }

    private List<Path> completePaths(List<Node> nodes,List<Path> incomplete){
        incomplete.forEach(path -> {
            path.setTo(getNodeForPath(nodes, path.getTo()));
            path.setFrom(getNodeForPath(nodes, path.getFrom()));
        });
        incomplete = incomplete.stream().filter(Path::containsNoNull).toList();
        return incomplete;
    }

    private Node getNodeForPath(List<Node> nodes, Node incompleteNode){
        if(incompleteNode!=null){
            if(incompleteNode.getId()!=null){
                return nodes.stream().filter(node -> node.getId().equals(incompleteNode.getId())).findFirst().orElse(new Node());
            }else{
                return nodes.stream().filter(node -> node.getName().equals(incompleteNode.getName())).findFirst().orElse(new Node());
            }
        }
        return null;
    }

    private Node getRootNode(Node node){
        while(!node.getId().equals(node.getRoot().getId())){
            node = node.getRoot();
        }
        return node;
    }

    public int getPathValue(Node node){
        Node parent = node.getRoot();
        if(parent == node){
            return parent.getValue();
        }else{
            Path path = data.get(node).stream().filter(pa -> pa.getOtherNode(node).equals(parent)).findFirst().get();
            return getPathValue(parent) + path.getValue();
        }
    }

    public String print(){
        String output = "";
        List<Node> list = data.keySet().stream().sorted(Comparator.comparing(Node::getName)).toList();
        for (Node node :
                list) {
            output += node.getName() + " -> " + node.getRoot().getName() + " : " + node.getValue() +"\n";
        }
        return output;
    }
}
