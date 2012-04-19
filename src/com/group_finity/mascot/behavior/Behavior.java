package com.group_finity.mascot.behavior;

import java.awt.event.MouseEvent;

import com.group_finity.mascot.Mascot;
import com.group_finity.mascot.exception.CantBeAliveException;


/**
 * マスコットの長期的な振る舞いをあらわすオブジェクト.
 * 
 * {@link Mascot#setBehavior(Behavior)} で使用する.
 */
public interface Behavior {

	/**
	 * 行動を開始する時に呼び出す.
	 * @param mascot 関連付けるマスコット.
	 */
	public void init(Mascot mascot) throws CantBeAliveException;

	/**
	 * マスコットを次のコマに進める.
	 */
	public void next() throws CantBeAliveException;
	
	/**
	 * マウスボタンが押された.
	 * @param mascot マウスクリックされたマスコット.
	 */
	public void mousePressed(MouseEvent e) throws CantBeAliveException;

	/**
	 * マウスが放された.
	 * @param mascot マウスが放されたマスコット.
	 */
	public void mouseReleased(MouseEvent e) throws CantBeAliveException;
}
