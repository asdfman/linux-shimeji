package com.group_finity.mascot.x11;

import java.awt.Rectangle;
import jnacontrib.x11.api.X.Display;
import jnacontrib.x11.api.X.X11Exception;
import jnacontrib.x11.api.X.Window;
import com.group_finity.mascot.environment.Area;
import com.group_finity.mascot.environment.ManyAreas;
import com.group_finity.mascot.environment.Environment;
import com.sun.jna.platform.unix.X11;
import java.io.*;
import java.util.ArrayList;

class X11Environment extends Environment {
	
	private Display display = new Display();
	public ManyAreas IE = new ManyAreas();
	public Area activeIE = new Area();
	public static final Area workArea = new Area();
	private int q = 400;
	private int xoffset,yoffset,wmod,hmod = 0;
	private ArrayList<String> titles = new ArrayList<String>();
	private boolean checkTitles = true;
			
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
			activeIE = IE.getRandom();
			q = 0;
		} 
		q++;
	}

	private void update() {
		Window[] allWindows = null;
		Window ie = null;
		int x,y,w,h;
		Rectangle r = new Rectangle();
		Area a = new Area();
			
		if (display == null) return;
		try {
			allWindows = display.getWindows();
			IE.clear();
			uguu:	
				for (int i=0;i<allWindows.length;i++) {
					if (checkTitles) {
						if (!isIE(allWindows[i].getTitle())) continue uguu;
					}
					w = allWindows[i].getGeometry().width;
					h = allWindows[i].getGeometry().height;
					x = allWindows[i].getBounds().x;
					y = allWindows[i].getBounds().y;
					r = new Rectangle(x+xoffset,y+yoffset,w+wmod,h+hmod);
					a = new Area();
					a.set(r);
					a.setVisible(true);
					IE.add(a);
					activeIE = a;
				}
		} catch (X11Exception e) {}

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

	@Override
	public Area getActiveIE() {
		return this.activeIE;
	}
	
	@Override
	public ManyAreas getIE() {
		return this.IE;
	}
	
	@Override
	public Area getWorkArea() {
		return this.workArea;
	}

}
	

