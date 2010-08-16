/*
 * GraphView.fx
 *
 * Created on 01.08.2009, 17:53:21
 */

package pack;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.util.Math;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.util.Sequences;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

/**
 * @author Alexandr Scherbatiy
 */

class Point{
    var x:Number;
    var y:Number;
}

//Sequences.

function contains(obj:Object, seq:Object[]):Boolean {
    -1 < Sequences.indexOf(seq, obj);
}

public class GraphView extends CustomNode{

    public var radius:Number  = 100;

    public var graph:Graph;


    public var sourceVertex:Vertex;
    public var destinationVertex:Vertex;

    public var selectedEdges:Edge[];

    public var onSelectionChanged:function(source:Vertex, desination:Vertex);

    var points:Point[];


    function getX(n:Integer,N:Integer):Number{
        radius * Math.cos(2.0 * n * Math.PI / N)
    }

    function getY(n:Integer,N:Integer):Number{
        radius * Math.sin(2.0 * n * Math.PI / N)
    }

    public override function create (){

        var cx = 0.0;
        var cy = 0.0;

        var N = sizeof graph.vertices;
        //println("[graph view] connections: {sizeof graph.connections}");

        Group{
            content: [
                for(edge in graph.edges){
                    var n1 = Sequences.indexOf(graph.vertices, edge.vertex1);
                    var n2 = Sequences.indexOf(graph.vertices, edge.vertex2);
                    //println("[graph view] {n1}, {n2}");
                    Line{
                        startX: bind points[n1].x
                        startY: bind points[n1].y
                        endX: bind points[n2].x
                        endY: bind points[n2].y
                        stroke: bind if(contains(edge,selectedEdges)) Color.GREEN else Color.DARKORANGE
                        strokeWidth: 2

                    }
                }
                for(vertex in graph.vertices){
                    Group{
                            
                        var n = indexof vertex;
                        
                        translateX: bind points[n].x
                        translateY: bind points[n].y

                        blocksMouse: true

                        onMousePressed: function( e: MouseEvent ):Void {
                            cx = e.sceneX;
                            cy = e.sceneY;
                            var changed = false;
                            if(sourceVertex != vertex and e.button == MouseButton.PRIMARY){
                                    sourceVertex = vertex;
                                    changed = true;
                            }else if(destinationVertex != vertex and e.button == MouseButton.SECONDARY){
                                destinationVertex = vertex;
                                changed = true;
                            }

                            if(changed){
                                onSelectionChanged(sourceVertex, destinationVertex);
                            }



                        }
                        onMouseDragged: function( e: MouseEvent ):Void {
                            points[n].x += (e.sceneX - cx);
                            points[n].y += (e.sceneY - cy);
                            cx  = e.sceneX;
                            cy  = e.sceneY;
                        }

                        content: [
                            Circle{
                                radius: 15
                                //fill: Color.YELLOW
                                //stroke: Color.DARKORANGE
                                fill: bind if(sourceVertex == vertex) then Color.LIGHTGREEN
                                    else if(destinationVertex == vertex) then Color.LIGHTBLUE
                                    else Color.YELLOW

                                //stroke: bind if(sourceVertex == vertex) then Color.BLUE else Color.DARKORANGE
                                stroke: bind if(sourceVertex == vertex) then Color.GREEN
                                    else if(destinationVertex == vertex) then Color.BLUE
                                    else Color.ORANGE
                            }
                            Text{
                                x: -5 y: 5
                                content: "{n}"
                                fill:Color.GREEN
                                font: Font{ size: 16 }
                            }
                        ]
                    }
                }

            ]

        }

    }

    init{
        var N = sizeof graph.vertices;
        points = for (n in [0..N-1]) Point{
            x: -getX(n,N)
            y: -getY(n,N)
        }

    }


}
