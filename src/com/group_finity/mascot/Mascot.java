package com.group_finity.mascot;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.group_finity.mascot.action.Action;
import com.group_finity.mascot.behavior.Behavior;
import com.group_finity.mascot.environment.*;
import com.group_finity.mascot.exception.CantBeAliveException;
import com.group_finity.mascot.image.MascotImage;
import com.group_finity.mascot.image.TranslucentWindow;

/**
 * マスコットオブジェクト.
 *
 * マスコットは長期的で複雑な振る舞いをあらわす {@link Behavior} と、
 * 短期的で単調な動きを表す {@link Action} で動く.
 *
 * マスコットは内部的にタイマを持っていて、一定間隔ごとに {@link Action} を呼び出す.
 * {@link Action} は {@link #animate(Point, MascotImage, boolean)} メソッドなどを呼ぶことで
 * マスコットをアニメーションさせる.
 *
 * {@link Action} が終了したり、その他の特定のタイミングで {@link Behavior} が呼び出され、次の {@link Action} に移る.
 *
 */
public class Mascot {

	private static final long serialVersionUID = 1L;
	// Storing current border object and the associated IE window for condition checks.
	private Area curIE = new Area();
	private FloorCeiling curFC;
	private Wall curW;

	private static final Logger log = Logger.getLogger(Mascot.class.getName());

	/**
	 * 最後に生成したマスコットのID.
	 */
	private static AtomicInteger lastId = new AtomicInteger();

	/**
	 * マスコットのID.
	 * デバッグ用のログを見やすくするためだけに存在する.
	 */
	private final int id;

	/**
	 * マスコットを表示するウィンドウ.
	 */
	private final TranslucentWindow window = NativeFactory.getInstance().newTransparentWindow();

	/**
	 * マスコットを管理しているマネージャ.
	 */
	private Manager manager = null;

	/**
	 * マスコットの接地座標.
	 * たとえば足元や、ぶら下がっているときの手の部分など.
	 * ここが画像を表示するときの中心になる.
	 */
	private Point anchor = new Point(0, 0);

	/**
	 * 表示する画像.
	 */
	private MascotImage image = null;

	/**
	 * 右向きかどうか.
	 * オリジナル画像は左向きとして扱われるので、trueを設定すると反転して描画される.
	 */
	private boolean lookRight = false;

	/**
	 * 長期的な振る舞いをあらわすオブジェクト.
	 */
	private Behavior behavior = null;

	/**
	 * タイマーの1チックごとに増加する時刻.
	 */
	private int time = 0;

	/**
	 * アニメーション実行中かどうか.
	 */
	private boolean animating = true;

	/**
	 * マスコットの表示環境.
	 */
	
	private MascotEnvironment environment = new MascotEnvironment(this);

	public Mascot() {
		this.id = lastId.incrementAndGet();

		log.log(Level.INFO, "マスコット生成({0})", this);

		// 常に最善面に表示
		getWindow().asJWindow().setAlwaysOnTop(true);
		//getWindow().asJWindow().setBounds(0,0,128,128);

		// マウスハンドラを登録
		getWindow().asJWindow().addMouseListener(new MascotEventHandler(this));

	}

	@Override
	public String toString() {
		return "マスコット" + this.id;
	}
	void tick() {
	// Update the current IE window the mascot is attached to based on floor/wall checks
		setCurIE();
		if (isAnimating()) {
			if (getBehavior() != null) {

				try {
					getBehavior().next();
				} catch (final CantBeAliveException e) {
					log.log(Level.SEVERE, "生き続けることが出来ない状況", e);
					dispose();
				}

				setTime(getTime() + 1);
			}
		}

	}

	public void apply() {
		if (isAnimating()) {
			// 表示できる画像が無ければ何も出来ない
			if (getImage() != null) {

				// 画像を設定
				getWindow().setImage(getImage().getImage());

				// ウィンドウの領域を設定
				getWindow().asJWindow().setBounds(getBounds());

				// 表示
				if (!getWindow().asJWindow().isVisible()) {
					getWindow().asJWindow().setVisible(true);
				}

				// 再描画
				getWindow().updateImage();
			} else {
				if (getWindow().asJWindow().isVisible()) {
					getWindow().asJWindow().setVisible(false);
				}
			}
			if (time == 2 || time == 30) {
				getWindow().setToDock(environment.getDockValue());
			}
			if (time == 3 || time == 30) {
				getWindow().asJWindow().setVisible(false);
			}
		}
	}

	public void dispose() {
		log.log(Level.INFO, "マスコット破棄({0})", this);

		getWindow().asJWindow().dispose();
		if (getManager() != null) {
			getManager().remove(Mascot.this);
		}
	}

	public Manager getManager() {
		return this.manager;
	}

	public void setManager(final Manager manager) {
		this.manager = manager;
	}

	public Point getAnchor() {
		return this.anchor;
	}
	
	public Area getCurIE() {
		return this.curIE;
	}

	public void setAnchor(final Point anchor) {
		this.anchor = anchor;
	}

	public MascotImage getImage() {
		return this.image;
	}

	public void setImage(final MascotImage image) {
		this.image = image;
	}

	public boolean isLookRight() {
		return this.lookRight;
	}

	public void setLookRight(final boolean lookRight) {
		this.lookRight = lookRight;
	}

	public Rectangle getBounds() {

		// 接地座標と画像の中心座標からウィンドウの領域を求める.
		final int top = getAnchor().y - getImage().getCenter().y;
		final int left = getAnchor().x - getImage().getCenter().x;

		final Rectangle result = new Rectangle(left, top, getImage().getSize().width, getImage().getSize().height);

		return result;
	}

	public int getTime() {
		return this.time;
	}

	private void setTime(final int time) {
		this.time = time;
	}
	public void setCurIE() {
		Area temp = new Area();
		if (this.curW != null) {
			this.curIE = curW.getArea();
					} else {
			if (this.curFC != null) {
				this.curIE = curFC.getArea();
			} else {
				this.curIE = new Area();
			}
		}
		if (temp.toRectangle().getWidth() < 5) return;
		if (temp.toRectangle().toString() != curIE.toRectangle().toString()) {
			curIE = temp;
		}
		
	}
	
	public void setCurW(Wall w) {
		this.curW = w;
	}

	public boolean onBorder() {
		return (this.curW != null || this.curFC != null);
	}
	
	public void setCurFC(FloorCeiling fc) {
		this.curFC = fc;
	}
		
	public Behavior getBehavior() {
		return this.behavior;
	}

	public void setBehavior(final Behavior behavior) throws CantBeAliveException {
		this.behavior = behavior;
		this.behavior.init(this);
	}

	public int getTotalCount() {
		return getManager().getCount();
	}

	private boolean isAnimating() {
		return this.animating;
	}

	void setAnimating(final boolean animating) {
		this.animating = animating;
	}

	TranslucentWindow getWindow() {
		return this.window;
	}

	public MascotEnvironment getEnvironment() {
		return environment;
	}

	public int getID() {
		return this.id;
	}

	public void move(int x, int y) {
		anchor.setLocation(anchor.getX()+x,anchor.getY()+y);
	}

}
