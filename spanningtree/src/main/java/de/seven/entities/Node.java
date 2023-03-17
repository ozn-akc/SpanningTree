package de.seven.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Node {
    @JsonProperty("id")
    Integer id;
    @JsonProperty("name")
    String name;
    @JsonIgnore
    Node root;
    @JsonIgnore
    Integer value;
    @JsonIgnore
    Integer routeDepth;

    public Node(Integer id){
        this.id = id;
    }

    public Node(String name){
        this.name= name;
    }

    public void increaseRouteDepth(){
        routeDepth++;
    }
}
