package de.seven.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.seven.entities.Node;
import de.seven.entities.Path;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataTransferObject {

    @JsonProperty("nodes")
    private List<Node> nodes;
    @JsonProperty("paths")
    private List<Path> paths;

}
