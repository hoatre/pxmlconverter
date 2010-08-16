/*
 * GraphAlgorithm.fx
 *
 * Created on 01.08.2009, 22:21:49
 */

package pack;

import java.lang.Comparable;

import javafx.util.Sequences;
import javafx.util.Math;

/**
 * @author raindrop
 */

// place your code here

def INFINITY = Number.MAX_VALUE;

public function sign(x:Number):Integer {
    if( x <0 ) { -1 } else if(0 < x) { 1 } else { 0 }
}

class DVertex extends Comparable{

    //var vertex:Vertex;
    
    var value:Number;
    var previous:Integer;

    override function compareTo(obj:Object):Integer{
            var vertex = obj as DVertex;
            return sign(value - vertex.value);
    }

    //override function toString() { ""  }
}


public function dijkstraShortestPath(source:Vertex, destination:Vertex, graph:Graph):Edge[]{


    if(source == null or destination == null) {
        return [];
    }


    var vertices = for(v in graph.vertices) DVertex{
        previous: indexof v
        value: if( v == source) then  0  else INFINITY
    };

    var minIndex = Sequences.indexOf(graph.vertices, source);
    var dstIndex = Sequences.indexOf(graph.vertices, destination);

    //println("src: {minIndex}");
    //println("dst: {dstIndex}");

    var unvisited = vertices;

    var min = vertices[minIndex];
    
    while(minIndex != dstIndex){

        delete min from unvisited;

        var vertex = graph.vertices[minIndex];
        
        for(edge in vertex.edges){
            var v = edge.getOppositeVertex(vertex);
            var ind = Sequences.indexOf(graph.vertices, v);
            var opposite = vertices[ind];
            var distance = 1.0;

            //if(edge instanceof WeightEdge){
            //}
            
            var d = min.value + distance;
            if(d < opposite.value ){
                    opposite.value = d;
                    opposite.previous = minIndex;
            }

            //opposite.value = Math.min(min.value + distance,opposite.value);

        }


        min = Sequences.min(unvisited) as DVertex;
        minIndex = Sequences.indexOf(vertices, min);
        //println("min: {minIndex}");
    }



    var res:Integer[];

    var p = minIndex;
    var prev = min;
    insert p into res;

    while(p != prev.previous){
        p = prev.previous;
        prev = vertices[p];
        insert p into res;
    }


    //println(res);

    for(n in [0..sizeof res - 2]){
        graph.vertices[res[n]].getEdge(graph.vertices[res[n+1]])
    }


}


