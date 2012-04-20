package com.group_finity.mascot.x11;

import java.awt.Rectangle;
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
	
	private Display display = new Display();
	public WindowContainer IE = new WindowContainer();
	public Area activeIE = new Area();
	public static final Area workArea = new Area();
	private int q = 400;
	private int xoffset,yoffset,wmod,hmod = 0;
	private ArrayList<String> titles = new ArrayList<String>();
	private boolean checkTitles = true;
	private boolean cleanUp, newRandom = false;
	private Random RNG = new Random();
	private ArrayList<Number> curActiveWin = new ArrayList<Number>();

			
	X11Environment() {
		workArea.set(getWorkAreaRect());
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
				titles.add(strLine.trim());
			}
			br.close();
			in.close();
			fstream.close();
		} catch (Exception e) {}
		if (titles.size() == 0) checkTitles = false;
	}
	
	@Override
	public void tick() {
		super.tick();
		update();
		if (q == 500) {
			getRandomIE();
			q = 0;
		}
		q++;
		if (q%10==0) cleanUp = true;
	}

	private void update() {
		Window[] allWindows = null;
		Window ie = null;
		int x,y,w,h,id;
		Rectangle r = new Rectangle();
		Area a = new Area();
		if (cleanUp) curActiveWin = new ArrayList<Number>();
		if (display == null) return;
		try {
			allWindows = display.getWindows();
			uguu:	
				for (int i=0;i<allWindows.length;i++) {
					if (checkTitles) {
						if (!isIE(allWindows[i].getTitle())) continue uguu;
					}
					id = allWindows[i].getID();
					w = allWindows[i].getGeometry().width + wmod;
					h = allWindows[i].getGeometry().height + hmod;
					x = allWindows[i].getBounds().x + xoffset;
					y = allWindows[i].getBounds().y + yoffset;
					if (IE.containsKey(id)) {
						a = IE.get(id);
						r = a.toRectangle();
						Rectangle newRect = new Rectangle(x,y,w,h);
						try {
							if (r.getLocation() == newRect.getLocation()) {
								if (cleanUp) curActiveWin.add(id);
								continue uguu;
							}
						} catch (Exception e) {}
						a.set(newRect);
						IE.put(id,a);
						if (cleanUp) curActiveWin.add(id);
						continue uguu;
					}
					r = new Rectangle(x,y,w,h);
					a = new Area();
					a.set(r);
					a.setVisible(true);
					IE.put(id,a);
					if (cleanUp) curActiveWin.add(id);
				}
		} catch (X11Exception e) {}
		if (cleanUp) {
			Iterator<Number> keys = IE.keySet().iterator();
			while (keys.hasNext()) {
				Number i = keys.next();
				if (!curActiveWin.contains(i)) {
					IE.remove(i);
				}
			}
			cleanUp = false;
		}
			

	}
	
	private boolean isIE(String titlebar) {
		for (int i=0;i<titles.size();i++) {
			if (titlebar.contains(titles.get(i))) return true;
		}
		return false;
	} 

	private Rectangle getWorkAreaRect() {
		Rectangle r1 = getScreen().toRectangle();
		return r1;
	}

	private void getRandomIE() {
		int max = RNG.nextInt(IE.size());
		if (IE.size() == 0) return;
		Iterator<Number> iter = IE.keySet().iterator();
		int i = 0;
		while (i<max) {
			iter.next();
			i++;
		}
		activeIE = IE.get(iter.next());
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
	

