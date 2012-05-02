package com.group_finity.mascot.x11;

import java.awt.Rectangle;
import java.awt.Point;
import java.util.Vector;
import jnacontrib.x11.api.X.Display;
import jnacontrib.x11.api.X.X11Exception;
import jnacontrib.x11.api.X.Window;
import com.group_finity.mascot.environment.Area;
import com.group_finity.mascot.environment.WindowContainer;
import com.group_finity.mascot.environment.Environment;
import com.sun.jna.platform.unix.X11;
import java.io.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.util.Random;

class X11Environment extends Environment {
	
// The X display. See X.java 
	private Display display = new Display(); 

// Hashtable for storing the active windows
	public WindowContainer IE = new WindowContainer();

// Randomly chosen window for jump action targeting
	public Area activeIE = new Area();

// Current screen, never changes after initial assignment
// Environment.java and ComplexArea.java handle detection 
// and dual monitor behavior
	public static final Area workArea = new Area();

	private boolean checkTitles = true;
	private boolean newRandom, updateOnNext = false;

// Counter variable used for stuff we want less 
// frequently than each tick. Initialize at 400 to
// force an early activeIE selection.
	private int q = 400;
	private int z = 0;
	private Number markedForDeletion;

// Variables for configuration options
	private int xoffset,yoffset,wmod,hmod = 0;
	private ArrayList<String> titles = new ArrayList<String>();


// Random number generator for choosing a window for jump actions
	private Random RNG = new Random();

// Storage for Window ID's, only used for comparison when removing 
// user-terminated windows
	private ArrayList<Number> curActiveWin = new ArrayList<Number>();
	private ArrayList<Number> curVisibleWin = new ArrayList<Number>();

// Storage for values of certain state/type atoms on the current display
	private ArrayList<Number> badStateList = new ArrayList<Number>();
	private ArrayList<Number> badTypeList = new ArrayList<Number>();
	private int minimizedValue;
	private int dockValue;
		
// init() - set work area and read configuration files	
	X11Environment() {
		workArea.set(getWorkAreaRect());
		badStateList.add(Integer.decode(display.getAtom("_NET_WM_STATE_MODAL").toString()));
		badStateList.add(Integer.decode(display.getAtom("_NET_WM_STATE_HIDDEN").toString()));
		minimizedValue = Integer.decode(display.getAtom("_NET_WM_STATE_HIDDEN").toString());
		badStateList.add(Integer.decode(display.getAtom("_NET_WM_STATE_ABOVE").toString()));
		badTypeList.add(Integer.decode(display.getAtom("_NET_WM_WINDOW_TYPE_DOCK").toString()));
		dockValue = Integer.decode(display.getAtom("_NET_WM_WINDOW_TYPE_DOCK").toString());
		badTypeList.add(Integer.decode(display.getAtom("_NET_WM_WINDOW_TYPE_MENU").toString()));
		badTypeList.add(Integer.decode(display.getAtom("_NET_WM_WINDOW_TYPE_SPLASH").toString()));
		badTypeList.add(Integer.decode(display.getAtom("_NET_WM_WINDOW_TYPE_DIALOG").toString()));
		badTypeList.add(Integer.decode(display.getAtom("_NET_WM_WINDOW_TYPE_DESKTOP").toString()));
		try {
			FileInputStream fstream = new FileInputStream("window.conf");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int z = 0;
			while ((strLine = br.readLine()) != null) {
				z++;
				switch (z) {
					case 1: break;
					case 2: this.xoffset = Integer.parseInt(strLine.trim()); break;
					case 3: this.yoffset = Integer.parseInt(strLine.trim()); break;
					case 4: this.wmod = Integer.parseInt(strLine.trim()); break;
					case 5: this.hmod = Integer.parseInt(strLine.trim()); break;
					default : break;
				}
			}
			br.close();
			in.close();
			fstream.close();
		} catch (Exception e) {}
		try {
			FileInputStream fstream = new FileInputStream("titles.conf");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				titles.add(strLine.trim().toLowerCase());
			}
			br.close();
			in.close();
			fstream.close();
		} catch (Exception e) {}
		if (titles.size() == 0) checkTitles = false;
	}
	

// tick() - executed every x milliseconds, defined in Manager.java
	@Override
	public void tick() {
		super.tick();
		if (q%5==0 || updateOnNext) update();
	// New jump action target window every 1000 ticks
		if (q == 1000) {
			getRandomIE();
			q = 0;
		}
		q++;
	}

// update() - window handling, executed each tick
	private void update() {
		Window[] allWindows = null;
		Window ie = null;
		int x,y,w,h,id,curDesktop = 0;
		Rectangle r = new Rectangle();
		Area a = new Area();
		if (curVisibleWin != null) curVisibleWin.clear();
		curActiveWin = new ArrayList<Number>();
		if (display == null) return;
		updateOnNext = false;
		try {
		// Retrieve all windows from the X Display
			allWindows = display.getWindows();
			curDesktop = display.getActiveDesktopNumber();
			uguu:	
				for (int i=0;i<allWindows.length;i++) {
				// Break for-loop if the window title does not match config.
					if (checkTitles) {
						if (!isIE(allWindows[i].getTitle())) continue uguu;
					}
				// Get window attributes.
					id = allWindows[i].getID();
					w = allWindows[i].getGeometry().width + wmod;
					h = allWindows[i].getGeometry().height + hmod;
					x = allWindows[i].getBounds().x + xoffset;
					y = allWindows[i].getBounds().y + yoffset;
					if (IE.containsKey(id)) {
						a = IE.get(id);
						int desktop = allWindows[i].getDesktop();
						boolean badDesktop = ((desktop != curDesktop)&&(desktop != -1));
						boolean badState = checkState(allWindows[i].getState());
						if (!checkTitles) {
							boolean badType = checkType(allWindows[i].getType());
							if (badDesktop||badType||badState) {
								IE.get(id).setVisible(false);
							} else {
								IE.get(id).setVisible(true);
								curVisibleWin.add(id);
							}
						} else {
							if (badDesktop||badState) {
								IE.get(id).setVisible(false);
							} else {
								IE.get(id).setVisible(true);
								curVisibleWin.add(id);
							}
						}
						r = a.toRectangle();
						Rectangle newRect = new Rectangle(x,y,w,h);
						if (!r.equals(newRect)) {
							updateOnNext = true;
						}
						a.set(newRect);
						curActiveWin.add(id);
						continue uguu;
					} else {
						r = new Rectangle(x,y,w,h);
						a = new Area();
						a.set(r);
						a.setVisible(false);
						IE.put(id,a);
						curActiveWin.add(id);
					}
				}
		} catch (X11Exception e) {}
	// Remove user-terminated windows from the container every 5th tick
		Iterator<Number> keys = IE.keySet().iterator();
		while (keys.hasNext()) {
			Number i = keys.next();
			if (!curActiveWin.contains(i)) {
				IE.get(i).setVisible(false);
				IE.remove(i);
				break;
			}
		}
	}
	
	private boolean isIE(String titlebar) {
		for (String s : titles) {
			if (titlebar.toLowerCase().contains(s)) return true;
		}
		return false;
	} 

	private boolean checkState(int state) {
		if (!checkTitles) {
			if (badStateList.contains(state)) return true;
			return false;
		} else {
			if (state == minimizedValue) return true;
			return false;
		}
	}

	private boolean checkType(int type) {
		if (badTypeList.contains(type)) return true;	
		return false;
	}

	private Rectangle getWorkAreaRect() {
		Rectangle r1 = getScreen().toRectangle();
		return r1;
	}

// getRandomIE() - assign a new randomly selected activeIE
// for jump action targeting.
	private void getRandomIE() {
		ArrayList<Area> visibleWin = new ArrayList<Area>();
		if (curVisibleWin == null) return;
		for (Number n : curVisibleWin) {
			if (n != null) visibleWin.add(IE.get(n));
		}
		if (visibleWin.size() == 0) return;
		activeIE = visibleWin.get(RNG.nextInt(visibleWin.size()));
	}

	public int getDockValue() {
		return dockValue;
	}

	public ArrayList<Number> getVisible() {
		return this.curVisibleWin;
	}

	@Override
	public Area getActiveIE() {
		return this.activeIE;
	}
	
	@Override
	public WindowContainer getIE() {
		return this.IE;
	}
	
	@Override
	public Area getWorkArea() {
		return this.workArea;
	}

}
	

