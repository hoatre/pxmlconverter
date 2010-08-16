package pack;
import javafx.stage.Stage;
import javafx.scene.Scene;




/**
 * @author paul
 */

Stage {  
    title : "JavaFX Simple Graph"  
    scene: Scene {  
        width: 300  
        height: 300  
        content: [  
            GraphView{  
                translateX: 150  
                translateY: 150  
                graph: Graph{  
                        order: 5  
                        connections: [  
                            Connection{ vertex1: 0 vertex2: 1 }  
                            Connection{ vertex1: 0 vertex2: 2 }  
                            Connection{ vertex1: 0 vertex2: 3 }  
                            Connection{ vertex1: 1 vertex2: 2 }  
                            Connection{ vertex1: 3 vertex2: 4 }  
                        ]  
                    }  
                }  
            ]  
    }  
} 