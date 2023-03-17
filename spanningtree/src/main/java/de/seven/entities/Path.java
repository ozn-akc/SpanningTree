package de.seven.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Path {
    @JsonProperty("from")
    Node from;
    @JsonProperty("to")
    Node to;
    @JsonProperty("value")
    Integer value;

    public boolean hasNode(Node node){
        return from.equals(node) || to.equals(node);
    }

    public boolean isFromNode(Node node){
        return from.equals(node);
    }

    public boolean containsNoNull(){
        return from!=null && to!=null;
    }

    public Node getOtherNode(Node node){
        if(hasNode(node)){
            if(from.equals(node)){
                return to;
            }
            return from;
        }else{
            return null;
        }
    }

}
