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


import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
/**
 * Main
 * 
 * For this task, the main stage of the application will display a thumbnail
 * of an image. The thumbnail is centered on the stage. If the user clicks on
 * the thumbnail, the full-size image will be displayed. The full size image is
 * scaled to fit within the bounds defined by the design team.
 * 
 * Main is where the application Stage is created. The contents of the Stage's
 * scene is a Thumbnail. When the user clicks on the Thumbnail, the Thumbnail
 * class calls Main's <code>showPhoto</code> function. <code>showPhoto</code>
 * creates a Photo and inserts the Photo into the contents of the Stage's
 * scene. When the user clicks on the Photo, the Photo calls the
 * <code>hidePhoto</code> function which simply removes the Photo from the
 * content of the Stage's scene.
 */

/**
 * Photo is the scaled-up image that will be displayed when the thumbnail is
 * clicked on. There is only one scaled-up image displayed at a time, so
 * we only need a reference to one Photo object. This Photo object will be
 * reused any time an image is shown.
 */
var photo : Photo;

/**
 * This function is called from Thumbnail when the user clicks on the
 * thumbnail. A new instance Photo is created and the Photo is inserted
 * into the content of the Stage's scene. Recall that the Stage content
 * is a sequence. The act of inserting the Photo into content causes
 * the scaled-up image (Photo) to be displayed.
 * @param url The URL of the image to be displayed
 * @param placeholder An Image to display while the Photo's image is loading
 */
function showPhoto(url : String, placeholder : Image) : Void {

    photo = Photo {

        url: url

        placeholder: placeholder

        // The designers defined a height and width for the scaled-up image.
        // This should reference a Constant and should be private to Photo.
        // But we haven't gotten the full spec yet, so we'll go with this.
        boundsHeight: bind stage.scene.height * .75
        boundsWidth: bind stage.scene.width * .75

        // When the user clicks on the scaled-up image, the scaled-up image is
        // removed from the contents of the Stage's scene. Rather than having
        // Photo manipulate the Stage's scene, we pass a reference
        // to the hidePhoto() function that the Photo class can invoke.
        // This helps create a loose coupling between Main and Photo. Photo
        // does not have to know about how to hide itself as all of that logic
        // is handled here in Main in the hidePhoto function.
        hide: hidePhoto

        // translateX and translateY are variables of the Node class. By
        // adjusting translateX and translateY, we can easily center the photo.
        // The bind statements here cause the translateX and translateY values
        // to be recalculated when the width or height of the photo changes.
        // Thus, the photo will be recentered. When does the photo width or height
        // change? When the photo is shown (as a side effect of setting photo.image)
        // or when the stage is resized (as a side effect of the stage.width
        // and stage.height changing).
        translateX: bind (stage.scene.width - photo.width)/2

        translateY: bind (stage.scene.height - photo.height)/2
    }
    insert photo into stage.scene.content;
}

/**
 * Similar to showPhoto, this method is called from Photo when the user clicks
 * on the scaled-up image. The Photo is hidden simply by removing the photo
 * reference from the stage content.
 */
function hidePhoto() : Void {
    delete photo from stage.scene.content;
    photo = null;
}

/**
 * We need a reference to the Stage object so we can use its width and height
 * to figure out how to center the thumbnail and the scaled image.
 */
var stage : Stage = Stage {
    
    title: "JavaFX Media Browser"

    // The design team defined the default height and width of the application
    // window. These defaults are defined in Constants.fx.
    height: Constants.STAGE_HEIGHT
    width : Constants.STAGE_WIDTH
    
    // The Stage's scene is where the thumbnails and the scaled image are shown.
    // For this task, we put a canned image in the scene as a thumbnail.
    // When the user clicks on the thumbnail, the scaled-up image is added
    // to the Stage's scene. When the user clicks on the scaled-up image, the
    // scaled-up image is remove from the Stage's scene.
    scene : Scene {
        fill: Constants.STAGE_BACKGROUND_COLOR
        content: [
            
            // Our content is a Thumbnail. The origin of the thumbnail is its
            // upper left corner. The xOrigin and yOrigin calculations below
            // cause the image to be centered in the stage. The variables are
            // bound so that a change in the stage's width or height will cause
            // the x,y location of the thumbnail to be recalculated. Thus,
            // the thumbnail stays in the center of the screen. In the next
            // topic we'll have multiple thumbnails.
            Thumbnail {
                xOrigin: bind (stage.scene.width - Constants.THUMB_WIDTH) / 2
                yOrigin: bind (stage.scene.height - Constants.THUMB_HEIGHT) / 2
                // The URL of the canned image.
                url: "{Constants.CODEBASE}images/image_01.jpg"
                // We need a way of showing the scaled-up image. Rather than
                // having thumbnail manipulate the Stage's scene, we pass a
                // reference to the "showPhoto" function which the Thumbnail
                // class can invoke. The showPhoto() function might be coded
                // differently on a different screen (say, mobile). By simply
                // providing the correct function reference, we can easily
                // change the behavior without changing the code in Thumbnail.
                // This creates a loose coupling between the code in this
                // script module and the code in the Thumbnai script module.
                // Further, in the future, the thumbnail will represent
                // some other media content such as a video or audio clip.
                // We can assign different function references to fullView
                // to handle the different content types.
                fullView: showPhoto
            }
        ]
    }
 }
