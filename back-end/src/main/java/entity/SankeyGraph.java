package entity;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/3/16.
 */
public class SankeyGraph {
    ArrayList<URLNode> nodes;
    ArrayList<StreamEdge> links;

    public SankeyGraph(List<URLNode> nodes, List<StreamEdge> links){
        this.nodes=(ArrayList<URLNode>)nodes;
        this.links=(ArrayList<StreamEdge>)links;
    }
}
