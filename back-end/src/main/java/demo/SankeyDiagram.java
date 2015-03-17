package demo;

import DataGenerator.SankeyDataGenerator;
import SqlTransformer.SankeySqltransformer;

/**
 * Created by Administrator on 2015/3/16.
 */
public class SankeyDiagram {
    public static void main(String args[]){
        String[] params={"ÍøÒ³","ÍøÒ³Á÷Á¿"};
        SankeySqltransformer sformer=new SankeySqltransformer(params);
        String result=new SankeyDataGenerator("jdbc:mysql://localhost:3306","log_stream").getGraph(sformer.EdgesSqltransform());
        System.out.println(result);
    }
}
