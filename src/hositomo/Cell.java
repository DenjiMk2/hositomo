package hositomo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cellは文章中の一部分のテキストを表す。
 * 各Cellのつながりはフィールド{@code forwardAnchor,hash,underAnchor}で保持する。
 * @author Denji_mk2
 *
 */
public class Cell {
	/**
	 * このCellが保持している文章
	 */
	public String text;
	/**
	 * このCellの文章が使われるのか使われ無いのか（削除されているか）
	 * {@code true}の場合このCellは有効で、ドキュメントで使われる文章が保存されている。
	 * {@code false}の場合このCellは無効で、ドキュメントで使われない文章が保存されている。
	 */
	public boolean enable;
	/**
	 * このCellの前（offsetが小さい方)のCellの{@code hash}。
	 */
	public List<Integer> forwardAnchors;
	/**
	 * このCellを表すid値
	 * 同一ツリー内では同じidは存在してはいけない。
	 */
	public int id;
	/**
	 * このCellの後（offsetが大きい方)のCellの{@code hash}。
	 */
	public List<Integer> backwardAnchors;
	/**
	 * このCellが追加された順番
	 */
	public int timeStamp;
	
	public Cell(){
		text = "";
		forwardAnchors = new ArrayList<Integer>();
		backwardAnchors = new ArrayList<Integer>();
	}
	
	public Cell(String text,int id,boolean enable,Cell forwardAnchor,Cell backwardAnchor){
		this();
		this.id = id;
		this.enable = enable;
		this.text = text;
		linkCell(forwardAnchor,backwardAnchor);
	}
	
//	public Cell(List<Cell> cells){
//		this();
//		if(cells == null || cells.isEmpty()){
//			this.id=Objects.hash(this);
//		}else{
//			int h;
//			int i = 0;
//			while(true){
//				i++;
//				h = Objects.hash(this,i);
//				for(Cell c : cells){
//					if(c.id == h)	continue;
//				}
//				break;
//			}
//			this.id=h;
//		}
//	}
	/**
	 * 指定されたidのCellを生成する。
	 * @param id id値
	 */
	public Cell(int id) {
		this();
		this.id = id;
	}

	@Override
	public String toString() {
		return forwardAnchors + text + backwardAnchors;
	}

	/**
	 * このセルにセルをつなげる
	 * @param forward このセルの前方（オフセットが小さい）のセル（null可能)
	 * @param backword このセルの後方（オフセットが大きい）のセル(null可能）
	 */
	public void linkCell(Cell forward,Cell backword){
		if(forward == null){
//			this.forwardAnchors = null;
		}else{
			this.forwardAnchors.add(forward.id);
			forward.backwardAnchors.add(this.id);
		}
		
		if(backword == null){
//			this.backwardAnchors = null;
		}else{
			this.backwardAnchors.add(backword.id);
			backword.forwardAnchors.add(this.id);
		}
	}
	
	/**
	 * このセルと対象のセルのリンクを解除する
	 * @param target このセルとリンクを解除したいセル
	 */
	public void unLinkCell(Cell target){
		//targetとthisがどのようなリンク方法（どっちがフォワードなのか）を探る
		boolean this2target = false;
		//thisのbackwardAnchorsの中にtargetのidがあればbooleanをtrueにする（this->taregetの並びと判定する）
		for(int i : this.backwardAnchors){
			if(i == target.id) this2target=true;
		}
		//TODO 実はリンクされていなかったときの記述
		if(this2target){
			this.backwardAnchors.removeIf(a -> {return a == target.id;});
//			this.backwardAnchors.remove(target.id);
			target.forwardAnchors.removeIf(a -> {return a == this.id;});
//			target.forwardAnchors.remove(this.id);
		}else{
			this.forwardAnchors.removeIf(a -> {return a == target.id;});
//			this.forwardAnchors.remove(target.id);//removeはその値を消すものではない
			target.backwardAnchors.removeIf(a -> {return a == this.id;});
//			target.backwardAnchors.remove(this.id);
		}
	}
	

	
	/**
	 * 文字列を長さで分割する
	 * @param s 分割したい文字列
	 * @param length 分割する長さ
	 * @return 0:分割した最初の文字列　1:分割した最後の文字列
	 */
//	public static List<String> splitByLength(String s, int length) {
//	    List<String> list = new ArrayList<>();
//	    if (!s.isEmpty()) {
//	        Matcher m = Pattern.compile("^[\\s\\S]").matcher(s);
////	        Matcher m = Pattern.compile("[\\s\\S]{1," + length + "}").matcher(s);
//	        while (m.find()) {
//	            list.add(m.group());
//	        }
//	    }
//	    return list;
//	}
	public static String[] splitByLength(String s,int length){
		String[] ret = new String[2];

		ret[0] = s.substring(0, length);
		ret[1] = s.substring(length);
		
		return ret;
	}

}
