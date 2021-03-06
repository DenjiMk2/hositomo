package hositomo;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.*;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;

public class TreeField extends JTextArea {

	TextTreeControler ctrl;

	/**
	 * コンストラクタ
	 */

	public TreeField() {
		super();
	}

	public TreeField(TextTreeControler c) {
		super();
		this.ctrl = c;
	}

	public TreeField(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
	}

	public TreeField(Document doc) {
		super(doc);
	}

	public TreeField(int rows, int columns) {
		super(rows, columns);
	}

	public TreeField(String text, int rows, int columns) {
		super(text, rows, columns);
	}

	public TreeField(String text) {
		super(text);
	}

	/**
	 * ドキュメントクラスを作成して取得します。<br>
	 * <p>
	 *
	 * @return このクラスのドキュメントクラス
	 */
	protected Document createDefaultModel() {
		return new TreeDocument();
	}

	/* --------------------------------------------- */
	/* ここからドキュメントクラスのカスタマイズ */
	/* --------------------------------------------- */

	/**
	 * カスタマイズしたドキュメントクラスです。
	 */
	public class TreeDocument extends PlainDocument {

		/**
		 * フィールドに文字列が挿入される際に呼び出される挿入処理です。
		 * <p>
		 * 入力された文字種を判断して、数字のみを挿入します。
		 * <p>
		 *
		 * @param offs
		 *            オフセット(挿入開始位置)
		 * @param str
		 *            挿入文字列
		 * @param a
		 *            AttributeSet（文字の属性）
		 * @exception BadLocationException
		 */
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {

			// 文字列が入ってきていないのにメソッドが呼ばれた場合は何もしない。
			if (str == null)
				return;

			// IMEの変換中の文字列はそのままinsertする
			if ((a != null)
					&& (a.isDefined(StyleConstants.ComposedTextAttribute))) { // <-----ここ
				super.insertString(offs, str, a);
				return;
			}

			// 実際に挿入をするオフセット
			int realOffs = offs;
//			System.out.println("offs="+offs+" rOffs="+realOffs+" len"+str.length());

			// 入力文字列を一文字ずつ判定
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);

				super.insertString(realOffs, String.valueOf(c), a);
				realOffs++;
				// System.out.print(c);
				ctrl.insert(String.valueOf(c), offs);
//				System.out.println("i c="+c+" offs ="+offs+" roffs ="+realOffs);//挿入時のオフセットをデバッグ

			}
		}

		@Override
		protected void removeUpdate(DefaultDocumentEvent chng) {
			super.removeUpdate(chng);
//			System.out.println(chng.getChange());
		}
	}

}