package com.group_finity.mascot;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.group_finity.mascot.config.Configuration;
import com.group_finity.mascot.exception.BehaviorInstantiationException;
import com.group_finity.mascot.exception.CantBeAliveException;

/**
 *
 * マスコットのリストを管理し、タイミングを取るオブジェクト.
 * 各マスコットが非同期に動くといろいろ困る(ウィンドウを投げる時など)ので、このクラスが全体のタイミングを合わせる.
 * {@link #tick()} メソッドが、まず最新の環境情報を取得し、それから全てのマスコットを動かす。
 *
 * @author Yuki Yamada
 */
public class Manager {

	private static final Logger log = Logger.getLogger(Manager.class.getName());

	/**
	 * タイマの実行間隔.
	 */
	public static final int TICK_INTERVAL = 20;

	/**
	 * マスコットの一覧.
	 */
	private final List<Mascot> mascots = new ArrayList<Mascot>();

	/**
	 * 追加される予定のマスコットのリスト.
	 * {@link ConcurrentModificationException} を防ぐため、マスコットの追加は {@link #tick()} ごとにいっせいに反映される.
	 */
	private final Set<Mascot> added = new LinkedHashSet<Mascot>();

	/**
	 * 追加される予定のマスコットのリスト.
	 * {@link ConcurrentModificationException} を防ぐため、マスコットの削除は {@link #tick()} ごとにいっせいに反映される.
	 */
	private final Set<Mascot> removed = new LinkedHashSet<Mascot>();

	/**
	 * 最後のマスコットを削除した時にプログラムを終了すべきかどうか.
	 * トレイアイコンの作成に失敗した時などは、マスコットがいなくなったらプログラムを終了しないと、プロセスがずっと残ってしまう.
	 */
	private boolean exitOnLastRemoved;

	/**
	 * {@link #tick()}をループするスレッド.
	 */
	private transient Thread thread;

	public Manager() {

		// これは Windows 上で動く Java のバグを修正するための処置
		// 短い長さの Thread.sleep を頻繁に呼ぶと Windows の時計が狂う
		// 長い Thread.sleep を呼んでいるとこの問題を回避できる.
		new Thread() {
			{
				this.setDaemon(true);
				this.start();
			}

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(Integer.MAX_VALUE);
					} catch (final InterruptedException ex) {
					}
				}
			}
		};
	}

	/**
	 * スレッドを開始する.
	 */
	public void start() {

		if ( thread!=null && thread.isAlive() ) {
			// もうスレッドが動いている
			return;
		}

		thread = new Thread() {
			@Override
			public void run() {

				// 前回の時間
				long prev = System.nanoTime() / 1000000;
				try {
					for (;;) {
						for (;;) {
							// 現在の時間
							// TICK_INTERVAL 経つまでループ.
							final long cur = System.nanoTime() / 1000000;
							if (cur - prev >= TICK_INTERVAL) {
								if (cur > prev + TICK_INTERVAL * 2) {
									prev = cur;
								} else {
									prev += TICK_INTERVAL;
								}
								break;
							}
							Thread.sleep(1, 0);
						}

						// マスコットたちを動かす.
						tick();
					}
				} catch (final InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		};
		thread.setDaemon(false);

		thread.start();
	}

	/**
	 * スレッドを停止する.
	 */
	public void stop() {
		if ( thread==null || !thread.isAlive() ) {
			// もう動いていない
			return;
		}

		thread.interrupt();

		try {
			thread.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * マスコットを一フレーム進める.
	 */
	private void tick() {

		// まず環境情報を更新
		NativeFactory.getInstance().getEnvironment().tick();

		synchronized (this.getMascots()) {

			// 追加すべきマスコットを追加
			for (final Mascot mascot : this.getAdded()) {
				this.getMascots().add(mascot);
			}
			this.getAdded().clear();

			// 削除すべきマスコットを削除
			for (final Mascot mascot : this.getRemoved()) {
				this.getMascots().remove(mascot);
			}
			this.getRemoved().clear();

			// マスコットの時間を進める
			for (final Mascot mascot : this.getMascots()) {
				mascot.tick();
			}

			// マスコットの絵や位置を最新にする.
			for (final Mascot mascot : this.getMascots()) {
				mascot.apply();
			}
		}

		if (isExitOnLastRemoved() && this.getMascots().size() == 0) {
			// exitOnLastRemoved が true でマスコットが一匹もいなくなったので終了.
			Main.getInstance().exit();
		}
	}

	/**
	 * マスコットを追加する.
	 * 追加は次の {@link #tick()} のタイミングで行われる.
	 * @param mascot 追加するマスコット.
	 */
	public void add(final Mascot mascot) {
		synchronized (this.getAdded()) {
			this.getAdded().add(mascot);
			this.getRemoved().remove(mascot);
		}
		mascot.setManager(this);
	}

	/**
	 * マスコットを削除する.
	 * 削除は次の {@link #tick()} のタイミングで行われる.
	 * @param mascot 削除するマスコット.
	 */
	public void remove(final Mascot mascot) {
		synchronized (this.getAdded()) {
			this.getAdded().remove(mascot);
			this.getRemoved().add(mascot);
		}
		mascot.setManager(null);
	}

	/**
	 * 全てのマスコットの行動を設定する.
	 * @param configuration
	 * @param name
	 */
	public void setBehaviorAll(final Configuration configuration, final String name) {
		synchronized (this.getMascots()) {
			for (final Mascot mascot : this.getMascots()) {
				try {
					mascot.setBehavior(configuration.buildBehavior(name));
				} catch (final BehaviorInstantiationException e) {
					log.log(Level.SEVERE, "次の行動の初期化に失敗しました", e);
					mascot.dispose();
				} catch (final CantBeAliveException e) {
					log.log(Level.SEVERE, "生き続けることが出来ない状況", e);
					mascot.dispose();
				}
			}
		}
	}

	/**
	 * 一匹だけ残して他を全て削除する.
	 */
	public void remainOne() {
		synchronized (this.getMascots()) {
			for (int i = this.getMascots().size() - 1; i > 0; --i) {
				this.getMascots().get(i).dispose();
			}
		}
	}

	/**
	 * 全て削除する.
	 */
	public void disposeAll() {
		synchronized (this.getMascots()) {
			for (int i = this.getMascots().size() - 1; i >= 0; --i) {
				this.getMascots().get(i).dispose();
			}
		}
	}

	/**
	 * マスコットの現在数を取得する.
	 * @return マスコットの現在数.
	 */
	public int getCount() {
		synchronized (this.getMascots()) {
			return this.getMascots().size();
		}
	}

	public void setExitOnLastRemoved(boolean exitOnLastRemoved) {
		this.exitOnLastRemoved = exitOnLastRemoved;
	}

	public boolean isExitOnLastRemoved() {
		return exitOnLastRemoved;
	}

	private List<Mascot> getMascots() {
		return this.mascots;
	}

	private Set<Mascot> getAdded() {
		return this.added;
	}

	private Set<Mascot> getRemoved() {
		return this.removed;
	}

}
