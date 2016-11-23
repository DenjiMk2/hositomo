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
	
	public String getSentence(){//TODO
		String ret = "";
		Cell focas;
		focas = start;
		
		while(focas != null){
			ret = ret + focas.text;
			Cell beforFocas = focas;
			focas = null;
			// 今のフォーカスセル(focas)の次のセルを検索する。複数ある可能性があるが、必要なのはennableがtrueなセルなので、それを探してそれをfocasに入れる。
			for(int c : beforFocas.backwardAnchors){
				if(cells.get(c).enable) focas = cells.get(c);
			}
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
	
	private void calcOffset(){
		offset = getSentence().length();
	}
	
}
