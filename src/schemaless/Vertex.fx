/*
 * Vertex.fx
 *
 * Created on 01.08.2009, 17:27:19
 */

package pack;

/**
 * @author Alexandr Scherbatiy
 */

public class Vertex {

    public var edges:Edge[];


    public function getEdge(vertex:Vertex):Edge{
            for(edge in edges){
                if(edge.getOppositeVertex(this)== vertex ){
                    return edge;
                }
            }
            return null;
    }

}
