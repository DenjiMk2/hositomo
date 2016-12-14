package hositomo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * SwingとTextTreeの間を取り持つクラス 現状Swingでは入力削除を意図した通りに読み取れないので、それを意図したように変える。
 * 
 * @author Denji-mk2
 *
 */
public class TextTreeControler {
	TextTree tree;
	String beforS = "";
	String baseS = "";
	String crlf = System.getProperty("line.separator");
	String beforDeleteS = null;
	List<Integer> deleteList = new ArrayList<Integer>();
	JPanel panel = new JPanel();

	public void init(JPanel panel) {
		tree = new TextTree();
		this.panel = panel;
		tree.init();
		GridBagLayout mgr = new GridBagLayout();
		panel.setLayout(mgr);
		draw();
	}

	/**
	 * 文字を入力するメソッド
	 * 
	 * @param s
	 *            入力したい文字
	 * @param offs
	 *            入力したい位置、オフセット
	 */
	public void insert(String s, int offs) {
		tree.insert(s, offs);
		if (beforDeleteS != null) {
			beforDeleteS = null;
		}

//		System.out.println(tree.getSentence());
//		System.out.println("in s = " + s + " offs = " + offs);
		draw();
	}

	/**
	 * 文字を削除するメソッド
	 * 
	 * @param s
	 */
	public void remove(String s, int offs, int length) {// TODO
														// Deleteキー等で一文字ずつ削除したとき次の入力までをひとつの塊と考える処理
//		System.out.println("r");
		if (s.length() < tree.getSentence().length()) {// 削除されたとき
			deleteList.add(offs);
			tree.remove(offs, length);

//			System.out.println(tree.getSentence());
//			System.out.println("rm s = " + s + " offs = " + offs + " len = " + length);
			if (beforDeleteS == null) {// 削除はじめの場合
				beforDeleteS = beforS;// 削除一文字目の場合、削除されるストリングを保存する
			}
		} else {// 追加されるとき
			if (beforDeleteS != null) {
				baseS = baseS + crlf + beforDeleteS;
				beforDeleteS = null;
			}
		}
		// panel.setText("*************************");
		// panel.setText(baseS + crlf + "---------------------------"
		// + crlf + s);

		beforS = s;

		draw();

	}

	/**
	 * panelにツリーを描画する
	 */
	private void draw() {
		panel.removeAll();
		Cell target = tree.start;
		ArrayList<Integer> mainList = new ArrayList<Integer>();// メインのIDを保管
		List<Map<String, Integer>> putList = new ArrayList<Map<String, Integer>>();// 設置済みの座標を保管
		Stack<StackCell> stack = new Stack<StackCell>();// 木表示のためのスタック
		int x = 0;
		int y = 0;
		// メイン（現在入力されている文章）をスタックに積む処理
		while (target != null) {
			mainList.add(target.id);
			StackCell scell = new StackCell(target);
			scell.pos.put("x", x++);
			scell.pos.put("y", y);
			putList.add(scell.pos);
			stack.push(scell);
			target = tree.next(target);
		}
//		System.out.println(stack);
		int buff = 0;
		while (!stack.isEmpty()) {// スタックの処理
			StackCell scell = stack.pop();// 処理対象を取り出す
			// メインだったらバフをクリアする
			for (int i : mainList) {
				if (scell.cell.id == i)
					buff = 0;
			}

			scell.copyBackwardAnchors.removeIf(new Predicate<Integer>() {// メインのアンカーを除去

				@Override
				public boolean test(Integer t) {
					for (int i : mainList) {
						if (t == i)
							return true;
					}
					return false;
				}

			});

			if (scell.copyBackwardAnchors.isEmpty()) {// もしバックワードアンカーが無いなら
				// 描画
				scell.pos.put("y", scell.pos.get("y")+buff);
				draw(scell.cell.text, scell.pos.get("x"), scell.pos.get("y")+buff);
				// putListに描画位置を通達して占有
				putList.add(scell.pos);
			} else {
				List<StackCell> tempStacks = new ArrayList<StackCell>();
				int diff = 0;//何個したにずらせばいいか
				for (int i : scell.copyBackwardAnchors) {// それぞれのバックワードの処理
					// 場所を決める
					boolean getOut = true;
					while (getOut) {
						getOut = false;
						for (Map<String, Integer> m : putList) {//すべての配置済みデータに対して繰り替えす
							if(!getOut && m.get("x") == scell.pos.get("x") + 1 && m.get("y") == scell.pos.get("y")+buff+diff){//一致していたらコリジョン発生
								buff++;
								getOut=true;
							}
						}
					}
//					System.out.println("diff" + diff + " buff "+buff);
					StackCell tempCell = new StackCell(tree.cells.get(i));
					tempCell.pos.put("x", scell.pos.get("x") + 1);
					System.out.println(scell.cell+"y scell "+scell.pos.get("y") + " diff = "+diff+"buff = "+buff);
					tempCell.pos.put("y", scell.pos.get("y") + diff);
					tempStacks.add(tempCell);
					diff++;
				}
				//スタックを積む
				scell.copyBackwardAnchors.clear();
				stack.push(scell);
				for(int i =tempStacks.size()-1; i>=0;i--){
					stack.push(tempStacks.get(i));
				}
			}
		}

		panel.revalidate();
		panel.repaint();
		System.out.println(tree.cells);
	}

	/*
	 * ツリー描画をスタックで管理する。 posを持ち、これを今のセルをどこに配置するかの基準とする{"x" = 2,"y" =
	 * 3}のように0Indexで数える。 この時 メインの文（getSentence()でとれる文章とセル）をoffsetが低い方からスタックに積む
	 * これを上から消化していく、消化する時に
	 * １、copyBackwardAnchorsがあるかを確認する（この時メイン文に該当するID値は除外して考える）
	 * ２、ある場合はまず自分のbackwardAnchorを削除し、スタックに積む。その後backwardAnchorのセルをスタックに積む、
	 * この時posを自身のposから算出して設定するが、衝突した場合buffを＋１する
	 * ２、posの決定はまず自分の隣（x＋１）を基本として、そこから一つのbackwardAnchorごとにyを1ずつ足していく、
	 * スタックに積む前に今まで出力した座標とx,y+buff座標を照らし合わせ、衝突したら、buff値を1上げる、
	 * yの値が小さいStackCellを後にstackに積むようにする
	 * ３、copyBackwardAnchorsが無い場合その時のバフ値を確認して出力する出力ポジションはx,y+buff、
	 * 書き出したら書き出した座標の値を保持しておく ４、メインのStackCellを処理した場合、処理前にバフ値は０にする。
	 */
	private class StackCell {
		Map<String, Integer> pos = new HashMap<String, Integer>();
		Cell cell;
		List<Integer> copyBackwardAnchors;

		public StackCell(Cell cell) {
			this.cell = cell;
			copyBackwardAnchors = new ArrayList<Integer>(cell.backwardAnchors);
		}

	}

	private void draw(String text, int x, int y) {
		JLabel label = new JLabel(text);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		panel.add(label, gbc);
		System.out.println(x+","+y+"text:"+text);
	}

}
