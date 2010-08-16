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

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Constants
 * 
 * One of the considerations is the different screen sizes on which the 
 * application might. By using constants, and by having different definitions
 * of these constants for the different screens, the code can be easily 
 * adapted to run on mobile or the desktop. 
 * 
 * The sizes, spacing, colors, stroke widths and other such details are
 * be defined by the design team. 
 */

/** The height of a thumbnail image */ 
package def THUMB_HEIGHT = 75;

/** The width of a thumbnail image */
package def THUMB_WIDTH  = 100;

/** The initial height of the application stage */
package def STAGE_HEIGHT = 500;

/** The initial width of the application stage */
package def STAGE_WIDTH  = 900;

/** The background color of the Stage on which the thumbnails are shown */ 
package def STAGE_BACKGROUND_COLOR = Color.BLACK;

/** An image to use as a placeholder during background loading of an Image */
package def PHOTO_PLACEHOLDER : Image = Image {
    url: "{__DIR__}resources/photoIcon.png"
}

/**
 * When running as an application or launcing in the browser from NetBeans,
 * the sample images and thumbnails can be loaded from the jar file. But when
 * deploying as an applet, putting all of these images in the jar file
 * would bloat the jar file unnecessarily. In the applet case the images
 * should be loaded from a server.
 * <p>
 * When running in the browser, the javafx.application.codebase property will
 * be set to the applet's codebase URL. For the Media Browser tutorial, the
 * sample images and thumbs are stored in directories rooted at this URL.
 * The javafx.application.codebase property can be used, therefore, to locate
 * the sample images and thumbnails.
 */
package def CODEBASE : String = {
    var codebase = FX.getProperty("javafx.application.codebase");
    // If we don't have a codebase defined, or codebase starts with "file:",
    // assume we're running locally and load images from the jar.
    if ( codebase == "" or codebase.startsWith("file:") ) {
        codebase = __DIR__
    }
    codebase
}
