/* DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER 
 *
 * Copyright © 2009 Sun Microsystems, Inc. All rights reserved. Use is subject to license terms. 
 * 
 * This file is available and licensed under the following license:
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: 
 * 
 *  * Redistributions of source code must retain the above copyright notice, this list of 
 *    conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright notice, this list of 
 *    conditions and the following disclaimer in the documentation and/or other materials 
 *    provided with the distribution.
 *  * Neither the name of Sun Microsystems nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without specific prior
 *    written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package mediabrowser;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Thumbnail
 *
 * This class encapsulates the scaled-down image. There is not much to it
 * as it only has to worry about scaling the image to fit the thumbnail 
 * dimensions and to handle a mouse click to show the scaled-up image. 
 * Later we will embellish the thumbnail with watermarks to show whether 
 * the related content is a media file or audio clip. 
 */

package class Thumbnail extends CustomNode {
   
   /** The url of the image to display */
    package var url : String;
    
    /** The x coordinate of the upper-left corner of the image */
    package var xOrigin : Number;

    /** The y coordinate of the upper-left corner of the image */
    package var yOrigin : Number;
    
    /** 
      * When the user clicks on the thumbnail, the image is scaled-up. This
      * class has no knowledge of how to accomplish that, nor should it. This
      * is delegated to the fullView function which is a reference to a 
      * function in another class. In this case, fullView is set in Main to
      * its showPhoto function. This helps keep a loose coupling between
      * the Thumbnail class and the showing of the full image. 
      */
    package var fullView : function(url : String, placeholder: Image) : Void;
    
    /** 
      * We need a reference to the image to pass to fullView when the user
      * clicks on the thumbnail. When we get to handling feed data, the URL 
      * for the thumbnail may be different than the URL for the content, so
      * this will have to be revisited. 
      *
      * Why is this a def and not a var?
      * A def cannot be on the left-hand side of an assignment except to
      * initialze the value. If the variable isn't going to be reassigned
      * after initialization, then make it a def.
      */
    def image : Image = Image {
        url: url
        backgroundLoading: true
        placeholder: Constants.PHOTO_PLACEHOLDER

    }
    
    override function create() : Node {
        ImageView {
            fitHeight: Constants.THUMB_HEIGHT
            fitWidth : Constants.THUMB_WIDTH
            x: bind xOrigin
            y: bind yOrigin
            image: image
            onMouseClicked: function(evt : MouseEvent) : Void {
                fullView(url, image);
            }
        }
       
   }
            
}

