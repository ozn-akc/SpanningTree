package de.seven.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Path {
    Node from;
    Node to;
    Integer value;

    public boolean hasNode(Node node){
        return from.equals(node) || to.equals(node);
    }

    public boolean isFromNode(Node node){
        return from.equals(node);
    }

    public Node getOtherNode(Node node){
        if(hasNode(node)){
            if(from.equals(node)){
                return from;
            }
            return to;
        }else{
            return null;
        }
    }

}
