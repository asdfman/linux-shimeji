package com.group_finity.mascot.action;

import com.group_finity.mascot.Mascot;
import com.group_finity.mascot.exception.LostGroundException;
import com.group_finity.mascot.exception.VariableException;

/**
 * マスコットのアニメーションをあらわすオブジェクト.
 * 
 * 一定時間置きに {@link #next(Mascot)} が呼び出される
 */
public interface Action {

	/**
	 * アクションを開始する時に呼び出す.
	 * @param mascot 関連付けるマスコット.
	 */
	public void init(Mascot mascot) throws VariableException;

	/**
	 * 次のフレームがあるかどうか調べる.
	 * @return 次のフレームがあるかどうか.
	 */
	public boolean hasNext() throws VariableException;
	
	/**
	 * マスコットを次のコマに進める.
	 * @throws LostGroundException 地面がない.
	 */
	public void next() throws LostGroundException, VariableException;
	
}
