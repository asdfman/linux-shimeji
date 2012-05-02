![Shimeji for Linux](http://i.imgur.com/efHyX.png "Shimeji for Linux")
Shimeji for Linux
=================

This is a Linux version of the popular desktop mascot program, Shimeji.

I appreciate any bug reports or suggestions. Use the issue tracker here or contact me directly on IRC (Rizon) where I'm called 'asdfman'.


Changelog
=========
v1.05
- Greatly reduced CPU usage (noticeable with few active mascots)
- Remake of multi monitor behavior 

v1.04
- Window layering detection.
- Fixed multi monitor behavior

v1.03
- Complete remakes of the window state/type handling. AwesomeWM-users should have no more issues.
- A conversion script for Shimeji-EE configurations is now included.

v1.02
- Full functionality with all standard Behavior.xml and Actions.xml files. Feel free to use any Shimeji you find and edit the files as you see fit.

v1.01
- All normal desktop windows should now be detected and interacted with correctly, titles.conf now defaults to an empty file.
- The mascots are now set a 'dock' window type property, using compton/xcompmgr with -C will not draw window shadows on them.

22.4.2012 v1.0
- Initial release, all basic functionality implemented

Requirements
===============
A compositing manager, [Compton](http://github.com/chjj/compton) is highly recommended. 

Tested with both OpenJDK 6 and SunJDK 6 JRE's. There were some issues on Gnome and KDE when using OpenJDK, if you experience them the only solution I can offer is using Sun JRE, should solve all problems. 


Usage
========
Obtain the repository :
git clone https://github.com/asdfman/linux-shimeji.git OR download a zip archive on the github page

To run, execute launch.sh in the project root directory. (eg. ./launch.sh)

To build, execute 'ant' in the project root directory.

Note that the program comes pre-compiled and you do not need to build unless you plan to modify the source.


Configuration
================
- window.conf - set window dimensions to match your window decorations. If you're using a plain WM with no decorations, zero on all values or an empty file should be accurate

- titles.conf - enter window titles, one per line, case insensitive. The Mascots will interact with these windows only. If no window titles are specified, all windows will be interacted with. Leaving this file empty should be ideal for most people.

For more information refer to the configuration files. 


Obtaining more Shimejis
==========================
You can find thousands on www.deviantart.com and www.pixiv.net (tag: しめじ). This version uses Japanese Actions.xml and Behavior.xml files.

To navigate your way through the .xml files more comfortably, I recommend using an English version of the file from the [Shimeji-EE project](http://code.google.com/p/shimeji-ee/) project as a roadmap. 

A conversion file for English .xml files used in the EE-version is included and any ShimejiEE mascot can be made fully functional with this version. Make sure you use a Japanese Mascot.xsd XML-schema after the conversion, do not replace it with the English one. The filenames that are read by this version are the same as in the current official Shimeji, 'Actions.xml' and 'Behavior.xml'. Keep in mind that all images should go into the 'img' directory, no subdirectories inside will be read, same goes for configurations and the 'conf' directory.

You will find 'conv.sed' in the 'conf' directory. You can also find it separately [here](http://gist.github.com/2497639).

Detailed usage instructions can be found within the file.


Known issues
===============
If you encounter trayicon-sized artifacts in the top-left corner of your screen, it's caused by an issue with Compton/XCompmgr and Java system tray spawning. I was unable to solve the issue from within this program so I wrote a [patch for Compton](http://gist.github.com/2472719) instead.


License
==========
This project inherits the ZLIB/LIBPNG license of the original [program](http://www.group-finity.com/Shimeji). 
The included [Java Native Access](http://github.com/twall/jna) library is licensed under the LGPL. [The Mozilla Rhino Javascript Engine](http://www.mozilla.org/rhino)
is licensed under the Mozilla Public License.

