/*
 * Edge.fx
 *
 * Created on 01.08.2009, 17:27:28
 */

package pack;

/**
 * @author Alexandr Scherbatiy
 */

public class Edge {
        
    public var vertex1:Vertex;
    public var vertex2:Vertex;

    public function getOppositeVertex(vertex:Vertex):Vertex{
            if(vertex == vertex1) then vertex2 else vertex1;
    }



}
