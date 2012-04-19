package com.group_finity.mascot.generic;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JWindow;

import com.group_finity.mascot.image.NativeImage;
import com.group_finity.mascot.image.TranslucentWindow;
import com.sun.jna.platform.WindowUtils;

class GenericTranslucentWindow extends JWindow implements TranslucentWindow {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 表示する画像.
	 */
	private GenericNativeImage image;

	private JPanel panel;
	private float alpha = 1.0f;

	public GenericTranslucentWindow() {
		super(WindowUtils.getAlphaCompatibleGraphicsConfiguration());
		this.init();

		this.panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(final Graphics g) {
				g.drawImage(getImage().getManagedImage(), 0, 0, null);
			}
		};
		this.setContentPane(this.panel);
	}

	private void init() {
		System.setProperty("sun.java2d.noddraw", "true");
		System.setProperty("sun.java2d.opengl", "true");
	}

	@Override
	public void setVisible(final boolean b) {
		super.setVisible(b);
		if (b) {
			WindowUtils.setWindowTransparent(this, true);
		}
	}

	@Override
	protected void addImpl(final Component comp, final Object constraints, final int index) {
		super.addImpl(comp, constraints, index);
		if (comp instanceof JComponent) {
			final JComponent jcomp = (JComponent) comp;
			jcomp.setOpaque(false);
		}
	}

	public void setAlpha(final float alpha) {
		WindowUtils.setWindowAlpha(this, alpha);
	}

	public float getAlpha() {
		return this.alpha;
	}


	@Override
	public JWindow asJWindow() {
		return this;
	}

	@Override
	public String toString() {
		return "LayeredWindow[hashCode="+hashCode()+",bounds="+getBounds()+"]";
	}

	public GenericNativeImage getImage() {
		return this.image;
	}

	public void setImage(final NativeImage image) {
		this.image = (GenericNativeImage)image;
	}

	public void updateImage() {
		WindowUtils.setWindowMask(this, this.getImage().getIcon());
		validate();
		this.repaint();
	}

}
