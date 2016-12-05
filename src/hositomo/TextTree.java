package hositomo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hositomo.Cell;

/**
 * テキストをマージを許容するグラフ状に保存するためのクラス。
 * 
 * @author Denji-mk2
 *
 */
// offsetは0インデックス
public class TextTree {
	/*
	 * スタートセルはid=1
	 */
	/**
	 * このテキストツリーが所持しているCellのすべて key = CellのID Value = Cell本体
	 */
	Map<Integer, Cell> cells;
	/**
	 * この文の長さ
	 */
	int offset;
	/**
	 * 文章の最初のセル Id=0である。
	 */
	Cell start;
	/**
	 * このテキストツリーに保存するIDの大元、新しくCellを追加するときはこの番号を使用し、使用したら一つ次の数字に動かしておく
	 */
	int gId;
	/**
	 * このテキストツリーの最後のCell
	 */
	int end;

	public TextTree() {
		super();
		cells = new HashMap<Integer, Cell>();
	}

	public void init() {
		gId = 1;
		Cell c = new Cell("", gId++, true, null, null);
		cells.put(1, c);
		start = c;
		end = c.id;
	}

	@Override
	public String toString() {// TODO toStringの整備 ツリー構造がわかるような
		return getSentence();
	}

	/**
	 * このテキストツリーの現在の文章を取得する。
	 * 
	 * @return 現在の文面
	 */

	public String getSentence() {
		String ret = "";
		Cell focas;
		focas = start;

		while (focas != null) {
			ret = ret + focas.text;
			Cell beforFocas = focas;
			focas = next(beforFocas);
			// 今のフォーカスセル(focas)の次のセルを検索する。複数ある可能性があるが、必要なのはennableがtrueなセルなので、それを探してそれをfocasに入れる。
			// for(int c : beforFocas.backwardAnchors){
			// if(cells.get(c).enable) focas = cells.get(c);
			// }
		}

		return ret;
	}

	/**
	 * このテキストツリーの後ろに文章を追加する。
	 * 
	 * @param s
	 *            追加したい文章
	 */
	public void insert(String s) {
		Cell c = new Cell(s, gId++, true, cells.get(end), null);
		end = c.id;
		cells.put(c.id, c);
	}

	/**
	 * このテキストツリーのoffset位置に文章を追加する。
	 * 
	 * @param s
	 *            追加したい文章
	 * @param offset
	 *            追加したい位置（最初）
	 */
	public void insert(String s, int offset) {// TODO offsetの値が不正だった場合のチェック
		Cell target = start;
		while (offset > 0) {
			target = next(target);
			offset -= target.text.length();
		}
		if (offset != 0) {
			Cell[] sCells = split(target, Math.abs(offset) - 1);
			target = sCells[0];
		}
		if (next(target) == null) {// 最後に挿入の場合（insert(String s)と同じ動作が必要になる)
			insert(s);
			// end = cell.id;//もし最後に追加だったらendを更新
		} else {// 違った場合（最後に挿入では無かった場合）
			Cell cell = new Cell(s, gId++, true, target, null);
			cell.linkCell(null, next(target));
			target.unLinkCell(next(target));
			cells.put(cell.id, cell);
		}

	}

	/**
	 * このテキストツリーの指定された位置の文字を削除する。
	 * 
	 * @param startOffset
	 *            削除の初めの位置
	 * @param length
	 *            削除する長さ
	 */
	public void remove(int startOffset, int length) {
		Cell target = start;

		// startOffset -= next(target).text.length();
		// 削除箇所の始まりを確認
		while (startOffset > 0) {
			target = next(target);
			startOffset -= target.text.length();
		}
		if (startOffset != 0) {// セルの途中だったら分割する
			Cell[] sCells = split(target, Math.abs(startOffset) - 1);
			target = sCells[0];
		}
		// どのセルまで削除すれば良いのかの確認
		List<Integer> list = new ArrayList<Integer>();
		while (length > 0) {
			target = next(target);// TODO オーバーする可能性
			length -= target.text.length();
			if (length < 0) {// 消し過ぎた場合
				Cell[] sCells = split(target, target.text.length() - Math.abs(length));
				target = sCells[0];
			}
			list.add(target.id);
		}
		remove(list);
		// TODO 実行中マーカー
		// TODO エンドの更新処理
	}

	/**
	 * 指定されたidのセルを文章中から削除する。（データを消すわけではない）
	 * 
	 * @param id
	 *            削除したいセルの{@code id}値
	 */
	public void remove(int id) {
		Cell target = cells.get(id);
		target.enable = false;
		Cell targetForward = next(target);
		Cell targetBackward = back(target);
		// 削除ターゲットセルの前後にセルがあるかの確認 TODO この条件の時末端意外ありえるのかの検証 この状況の時の対応を実装
		if (targetForward == null) {
			return;
		}
		if (targetBackward == null) {
			return;
		}
		targetBackward.linkCell(null, targetForward);
	}

	public void remove(List<Integer> ids){
		Cell target = start;
		boolean bre = true;
		while(bre){//消すブロックの最初を確認
			target = next(target);
			for(int i : ids){
				if(bre)bre = (i == target.id ? false : true);
			}
		}
		Cell rStart = target;
		bre = true;
		while(bre){//消すブロックの最後を確認
			if(next(target)== null){
				bre = false;
			}else{
				target = next(target);
				boolean bre2 = false;//ターゲットのidをidsに見つけたら（まだ削除するセルだったら）true まだ見つけてなかったらfalse
				for(int i : ids){
					if(!bre2){
						bre2 = (i == target.id ? true : false);
					}
				}
				if(!bre2) target = back(target);
				bre = bre2;
			}
		}
		for(int i : ids){
			cells.get(i).enable=false;
		}
		if(next(target) != null){
			back(rStart).linkCell(null,next(target));
		}
	}

	private void calcOffset() {
		offset = getSentence().length();
	}

	private Cell next(Cell c) {
		for (int i : c.backwardAnchors) {
			if (cells.get(i).enable)
				return cells.get(i);
		}
		return null;
	}

	private Cell back(Cell c) {
		for (int i : c.forwardAnchors) {
			if (cells.get(i).enable)
				return cells.get(i);
		}
		return null;
	}

	/**
	 * この{@code Cell}を指定位置で分割して二つにする
	 * 
	 * @return 0:オフセットが小さいセル 1:オフセットが大きいセル
	 */
	public Cell[] split(Cell cell, int offset) {
		String[] splitString = Cell.splitByLength(cell.text, offset);
		Cell[] ret = new Cell[2];
		for (int i = 0; i < 2; i++) {
			ret[i] = new Cell(splitString[i], gId++, true, null, null);
			cells.put(ret[i].id, ret[i]);
		}
		ret[0].linkCell(null, ret[1]);
		// 二つの前方をつなげる
		List<Integer> tempList = new ArrayList<Integer>(cell.forwardAnchors);
		for (int i : tempList) {
			ret[0].linkCell(cells.get(i), null);
		}
		// 後方をつなげる
		tempList = new ArrayList<Integer>(cell.backwardAnchors);
		for (int i : tempList) {
			ret[1].linkCell(null, cells.get(i));
		}
		cell.enable = false;

		return ret;
	}

}
