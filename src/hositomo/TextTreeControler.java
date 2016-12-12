package hositomo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * SwingとTextTreeの間を取り持つクラス
 * 現状Swingでは入力削除を意図した通りに読み取れないので、それを意図したように変える。
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
	
	public void init(JPanel panel){
		tree  = new TextTree();
		this.panel = panel;
		tree.init();
		GridBagLayout mgr = new GridBagLayout();
		panel.setLayout(mgr);
		draw();
	}
	/**
	 * 文字を入力するメソッド
	 * @param s 入力したい文字
	 * @param offs 入力したい位置、オフセット
	 */
	public void insert(String s , int offs){
		tree.insert(s, offs);
		if(beforDeleteS != null){
			beforDeleteS = null;
		}

		System.out.println(tree.getSentence());
		System.out.println("in s = "+s+" offs = "+offs);
		draw();
	}
	/**
	 * 文字を削除するメソッド
	 * @param s
	 */
	public void remove(String s,int offs, int length){//TODO Deleteキー等で一文字ずつ削除したとき次の入力までをひとつの塊と考える処理
		System.out.println("r");
		if (s.length() < tree.getSentence().length()) {// 削除されたとき
			deleteList.add(offs);
			tree.remove(offs, length);

			System.out.println(tree.getSentence());
			System.out.println("rm s = "+s+" offs = "+offs + " len = "+length);
			if (beforDeleteS == null){//削除はじめの場合
				beforDeleteS = beforS;// 削除一文字目の場合、削除されるストリングを保存する
			}
		} else {// 追加されるとき
			if (beforDeleteS != null) {
				baseS = baseS + crlf + beforDeleteS;
				beforDeleteS = null;
			}
		}
//		panel.setText("*************************");
//		panel.setText(baseS + crlf + "---------------------------"
//				+ crlf + s);

		beforS = s;
		
		draw();

	}
	
	/**
	 * panelにツリーを描画する
	 */
	private void draw(){
		panel.removeAll();
		Cell target = tree.start;
		ArrayList<Integer> mainList = new ArrayList<Integer>();
		int x = 0;
		//getSentenceを表示
		while(target != null){
			mainList.add(target.id);
			JLabel button = new JLabel(target.text);
			GridBagConstraints gbc = new GridBagConstraints();
//			gbc.insets = new Insets(0, 0, 5, 5);
			gbc.gridx = x++;
			gbc.gridy = 0;
			panel.add(button, gbc);
			target = tree.next(target);
		}
		target = tree.start;
		while(target != null){
			
			for(int i : target.backwardAnchors){
				if(mainList.contains(i)) continue;//センテンスに含まれるセルの場合スキップ
				
				
			}
			
			//TODO 作業箇所
			target = tree.next(target);
		}
		panel.revalidate();
		panel.repaint();
	}

}
