/*
 * Graph.fx
 *
 * Created on 01.08.2009, 14:56:46
 */

package pack;

/**
 * @author Alexandr Scherbatiy
 */

public class Graph {


    public var order:Integer;
    
    public var vertices:Vertex[];
    public var edges: Edge[];

    public var connections:Connection[];


    init{
        //println("connections: {sizeof connections}");


        for(i in [sizeof vertices..<order]){
            insert Vertex{} into vertices;
        }

        order = sizeof vertices;
        

        
        for(connection in connections){
            var edge = connection.edge;
            
            if(edge == null){
                edge = Edge{};
            }

            insert edge into edges;

            var v1 = vertices[connection.vertex1];
            var v2 = vertices[connection.vertex2];

            edge.vertex1 = v1;
            edge.vertex2 = v2;

            insert edge into v1.edges;
            insert edge into v2.edges;

        }

    }


}
