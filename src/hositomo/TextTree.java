package hositomo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hositomo.Cell;

/**
 * テキストをマージを許容するグラフ状に保存するためのクラス。
 * @author Denji-mk2
 *
 */
//offsetは0インデックス
public class TextTree {
	/*
	 * スタートセルはid=1
	 */
	/**
	 * このテキストツリーが所持しているCellのすべて
	 * key = CellのID
	 * Value = Cell本体
	 */
	Map<Integer,Cell> cells;
	/**
	 * この文の長さ
	 */
	int offset;
	/**
	 * 文章の最初のセル
	 * Id=0である。
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
	
	public TextTree(){
		super();
		cells= new HashMap<Integer,Cell>();
	}
	
	public void init(){
		gId = 1;
		Cell c = new Cell("",gId++,true,null,null);
		cells.put(1,c);
		start = c;
		end = c.id;
	}
	
	
	/**
	 * このテキストツリーの現在の文章を取得する。
	 * @return 現在の文面
	 */
	
	public String getSentence(){
		String ret = "";
		Cell focas;
		focas = start;
		
		while(focas != null){
			ret = ret + focas.text;
			Cell beforFocas = focas;
			focas = next(beforFocas);
			// 今のフォーカスセル(focas)の次のセルを検索する。複数ある可能性があるが、必要なのはennableがtrueなセルなので、それを探してそれをfocasに入れる。
//			for(int c : beforFocas.backwardAnchors){
//				if(cells.get(c).enable) focas = cells.get(c);
//			}
		}
		
		return ret;
	}
	
	
	/**
	 * このテキストツリーの後ろに文章を追加する。
	 * @param s 追加したい文章
	 */
	public void insert(String s){
		Cell c = new Cell(s,gId++, true, cells.get(end), null);
		end = c.id;
		cells.put(c.id, c);
	}
	
	/**
	 * このテキストツリーのoffset位置に文章を追加する。
	 * @param s 追加したい文章
	 * @param offset 追加したい位置（最初）
	 */
	public void insert(String s,int offset){//TODO メソッドの実装 実装中
		Cell target = start;
		while(offset <= 0){
			target = next(target);
			offset-=target.text.length();
		}
		if(offset == 0){//もし計算後のオフセットが0だったら（セルとセルの間に挿入だった場合）
			Cell nCell = new Cell(s,gId++,true,target,null);
			if(next(target) == null){
				end = nCell.id;//もし最後に追加だったらendを更新
			}else{//違った場合（最後に挿入では無かった場合）
				nCell.linkCell(null, next(target));
			}
		}
		
	}
	/**
	 * このテキストツリーの指定された位置の文字を削除する。
	 * @param startOffset 削除の初めの位置
	 * @param length 削除する長さ
	 */
	public void remove(int startOffset,int length){
		//TODO メソッドの実装
	}
	
	/**
	 * 指定されたidのセルを文章中から削除する。（データを消すわけではない）
	 * @param id 削除したいセルの{@code id}値
	 */
	public void remove(int id){
		Cell target = cells.get(id);
		target.enable = false;
		Cell targetForward = next(target);
		Cell targetBackward = back(target);
		//削除ターゲットセルの前後にセルがあるかの確認 TODO この条件の時末端意外ありえるのかの検証 この状況の時の対応を実装
		if(targetForward == null){
			return;
		}
		if(targetBackward == null){
			return;
		}
		targetBackward.linkCell(null, targetForward);
	}
	
	private void calcOffset(){
		offset = getSentence().length();
	}
	
	private Cell next(Cell c){
		for(int i : c.backwardAnchors){
			if(cells.get(i).enable) return cells.get(i);
		}
		return null;
	}
	
	private Cell back(Cell c){
		for(int i : c.forwardAnchors){
			if(cells.get(i).enable) return cells.get(i);
		}
		return null;
	}
	
}
