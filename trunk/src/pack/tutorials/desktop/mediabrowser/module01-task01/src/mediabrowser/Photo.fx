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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.Resizable;

/**
 * Photo
 *
 * Photo encapsulates the scaled-up image of the thumbnail. At present,
 * it is very simple. Later, however, we can embellish it with text
 * fields for showing image meta-data, or putting a frame around the image. 
 * The key points to this class for now are that it is a CustomNode, and that it
 * handles scaling the image to fit the bounds defined by the designer.
 */
package class Photo extends CustomNode,  Resizable {

    /** 
      * The designers have determined a box within which the Photo is to fit.
      * This is the width of that box. This value is used in scaling the image.
      */
    package var boundsWidth  : Number on replace {
        resize();
    }


    /** 
      * The designers have determined a box within which the Photo is to fit.
      * This is the height of that box. This value is used in scaling the image.
      */
    package var boundsHeight : Number on replace {
        resize();
    }

   
   /** 
     * The url of the image to be displayed. This is passed from Thumbnail to
     * the showPhoto function in Main where it is assigned to the Photo object.
     */
    package var url : String;

    /**
      * An Image to serve as a placeholder during background loading
      */
    package var placeholder : Image;

    /**
     * Gap is the space between the ImageView and the meta-data text and also
     * the space between the ImageView and the rectangle used as a border.
     */
    def gap : Number = 10;
       
     /** 
      * When the user clicks on the scaled-up image, the image is hidden. 
      * How this happens is a detail this class has no knowledge of; nor
      * does it need to. Rather, this class delegates this to the hide
      * function which is assigned elsewhere. In the case of this task, 
      * Main assigns this function reference to its hidePhoto function.
      */
    package var hide : function() : Void;

    /**
     * The height of the image must be adjusted to fit within boundsHeight,
     * leaving enough room for the border at the top, plus the gap between the
     * image and the text, plus the height of the text, plus the border at
     * the bottom.
     */
    bound function imageBoundsHeight() : Number {
        boundsHeight - text.boundsInLocal.height - 3 * gap
    }

    /**
     * The width of the image must be adjusted to fit with boundsWidth,
     * leaving enough room for the border on each side
     */
    bound function imageBoundsWidth() : Number {
        boundsWidth - 2 * gap
    }

  
    /**
     * The ImageView, which is a container for the image.
     * Why is this a def and not a var?
     * A def cannot be on the left-hand side of an assignment, so if the
     * variable isn't going to be assigned to, then make it a def. You can
     * still use bind with def. 
     */
    def imageView : ImageView = ImageView {
        // If we don't block the mouse, mouse events will go to all nodes
        // which intersect with the click location. This causes the click
        // to only be seen by this, the Photo, node.
        blocksMouse: true
        // fitHeight and fitWidth are what achieves the scaling. We could
        // set one or the other, but by setting both, the image is scaled
        // to give a best fit. Further, we set perserveRatio to true so
        // the image ratio is preserved. This can cause the image to not
        // fully fit the height and width.
        fitHeight: bind imageBoundsHeight()
        fitWidth : bind imageBoundsWidth()
        preserveRatio: true

        // translateX and translateY move the ImageView relative to this Node.
        translateX: gap
        translateY: gap

        // You cannot create one Image object with the intent of changing
        // the url in order to change the image. url is a public-init variable,
        // meaning it can only be initialized, never assigned or bound.
        image: Image {
            url: url
            backgroundLoading: true
            placeholder: placeholder
        }

        // When the user clicks the mouse, the hide() function is called.
        onMouseClicked: function(evt : MouseEvent) : Void {
            hide();
        }
    } on replace {
        resize();
    }


    /**
     * This is the text label for the meta-data displayed under the image.
     * It uses the font defined above and a fill color of white (since our
     * background is black).
     */
    def text : Text = Text {
        font : Font {
            size : 16
        }
        fill: Color.WHITE

        // Move the x coordinate this number of pixels. This is the same
        // amount as we move the imageView so the imageView and the text
        // will be left-aligned.
        translateX: gap
        
        // We want to move the text so it is below the ImageView. The
        // bottom of the ImageView is at imageView.boundsInLocal.maxX.
        // The y coordinate of the text defaults to TextOrigin.BASELINE,
        // so if we only move the text by imageView.boundsInLocal.maxX,
        // the text will end up inside the ImageView. So we add the height
        // of the text, plus the gap.
        translateY: bind imageView.boundsInLocal.maxY + 
                            text.boundsInLocal.height + gap

        content: basename(url)
    }

    // return the last part of the path, which is the filename itself.
    function basename(path : String) : String {
        var basename : String = null;
        if ( path != null ) {
            // We want one char past where the last '/' is found.
            // If '/' is not found, then lastIndexOf will return -1,
            // so adding one will give us the start of the string.
            var offset : Integer = path.lastIndexOf('/') + 1;
            basename = path.substring(offset);
        }
        return basename;
    }

    /** A frame that goes around the photo and meta-data text */
    def frame : Rectangle = Rectangle {
        // 2 * gap because there is a gap at the top and a gap at the bottom!
        width: bind imageView.boundsInLocal.width + 2 * gap
        height: bind imageView.boundsInLocal.height +
                        text.boundsInLocal.height + 2 * gap
        arcWidth: 10
        arcHeight: 10
        fill: Color.BLACK
        stroke: Color.GRAY
        strokeWidth: 2
        opacity: .5
    }

    /** 
      * CustomNode requires a create function. For now, we just return
      * an instance of ImageView. Later we can add more features.
                    f = font: Font {
      */
    override function create(): Node {
        return Group {
            content: [
                frame,
                imageView,
                text
            ]
        };
    }

    override function getPrefWidth(h) {
        var imageWidth: Number = 0.0;
        var imageHeight: Number = 0.0;
        var image:Image  = imageView.image;
        if (image == null or image.width==0 or image.height==0) {
            if (placeholder != null) {
                imageWidth = placeholder.width;
                imageHeight = placeholder.height;
            }
        } else {
            imageWidth = image.width;
            imageHeight = image.height;
        }

        if (image!= null and imageHeight > 0 and imageWidth > 0) {
            if (h>2*gap) {
                return (h-(2*gap)) *(imageWidth/imageHeight) + 2*gap
            } else {
                return imageWidth + (2*gap);
            }
        } else {
            if (h > (2*gap)) {
                return (h-(2*gap))*3/2 + (2*gap);
             } else {
                 return 360 + (2*gap);
             }

        }
     }


    override function getPrefHeight(w) {
        var imageWidth: Number = 0.0;
        var imageHeight: Number = 0.0;

        var image:Image = imageView.image;

        if (image == null or image.width==0 or image.height==0) {
            if (placeholder != null) {
                imageWidth = placeholder.width;
                imageHeight = placeholder.height;
            }
        } else {
            imageWidth = image.width;
            imageHeight = image.height;
        }

        if (image!= null and imageHeight > 0 and imageHeight > 0) {
            if (w>(2*gap)) {
                return (w-(2*gap)) *(imageHeight/imageWidth) + (2*gap)
            } else {
                return imageHeight  + (2*gap);
            }
        } else {
            if (w > (2*gap) ) {
                return (w-(2*gap))*2/3;
            } else {
                return 240 + (2*gap);
            }
        }
     }

    /**
     * resizes the component to fit withing the bounds specified by boundsWidth
     * and boundsHeight using the getPrefWidth and getPrefHeight methods of this
     * and its enclosing components
     */
     package function resize():Void {
         if (getPrefHeight(boundsWidth) > boundsHeight){ //too tall
             if (getPrefWidth(boundsHeight) > boundsWidth) { // too wide
                 width = boundsWidth;
                 height = boundsHeight;
             } else {
                height = boundsHeight;
                width = getPrefWidth(boundsHeight);
             }
         } else { // too wide
            if (getPrefHeight(boundsWidth) > boundsWidth) {
               width = boundsWidth;
               height = boundsHeight;
            } else {
                width = boundsWidth;
                height = getPrefHeight(boundsWidth);
            }
        }
     }
}
