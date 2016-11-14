package hositomo;

import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import hositomo.TreeField.TreeDocument;

public class TreeControl {

	String beforS = "";
	String baseS = "";
	String crlf = System.getProperty("line.separator");
	String beforDeleteS = null;
	JTextPane panel;
	
	public TreeControl(JTextPane panel){
		this.panel = panel;
	}

	/**
	 * 入力されたときに走らせるメソッド
	 * @param c 入力された文字
	 * @param document 入力されたPlainDocument
	 */
	public void update(char c, PlainDocument document) {
		// TODO 自動生成されたメソッド・スタブ
		try {
			String s = document.getText(0, document.getLength());
			if (s.length() < beforS.length()) {// 削除されたとき
				if (beforDeleteS == null)
					beforDeleteS = beforS;// 削除一文字目の場合、削除されるストリングを保存する
				Pattern p = Pattern.compile(crlf + ".*$");
			} else {// 追加されるとき
				if (beforDeleteS != null) {
					baseS = baseS + crlf + beforDeleteS;
					beforDeleteS = null;
				}
			}
			panel.setText("*************************");
			panel.setText(baseS + crlf + "---------------------------"
					+ crlf + s);

			beforS = s;

		} catch (BadLocationException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	/**
	 * 削除されたときに起動するメソッド
	 * @param s 削除後の文章全文
	 */
	public void delete(String s) {
		if (s.length() < beforS.length()) {// 削除されたとき
			if (beforDeleteS == null)
				beforDeleteS = beforS;// 削除一文字目の場合、削除されるストリングを保存する
			Pattern p = Pattern.compile(crlf + ".*$");
		} else {// 追加されるとき
			if (beforDeleteS != null) {
				baseS = baseS + crlf + beforDeleteS;
				beforDeleteS = null;
			}
		}
		panel.setText("*************************");
		panel.setText(baseS + crlf + "---------------------------"
				+ crlf + s);

		beforS = s;

	}
	
	/**
	 * 内部のデータを空にリセットする
	 */
	
	public void reset(){
		beforS = "";
		baseS = "";
		beforDeleteS = null;
		panel.setText(" ");
	}

}
