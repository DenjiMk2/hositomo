package hositomo;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

/**
 * 指定されたテキストコンポーネントにUndo/Redo機能をつけます。
 * 
 * @author a-san
 */
public class UndoHelper {
    public static final String ACTION_KEY_UNDO = "undo";
    public static final String ACTION_KEY_REDO = "redo";
    public static final String ACTION_KEY_SAVE = "save";
    private JTextComponent textC;
    UndoManager undoManager = new UndoManager();
    private JTextPane logTextC;
    
    /** 指定されたテキストコンポーネントにUndo/Redo機能をつけます。　*/
    public UndoHelper(JTextComponent textComponent,JTextPane logTextComponent) {
    	textC = textComponent;
    	logTextC = logTextComponent;
        ActionMap amap = textComponent.getActionMap();
        InputMap imap = textComponent.getInputMap();
        if (amap.get(ACTION_KEY_UNDO) == null) {
            UndoAction undoAction = new UndoAction();
            amap.put(ACTION_KEY_UNDO, undoAction);
            imap.put((KeyStroke) undoAction.getValue(Action.ACCELERATOR_KEY), ACTION_KEY_UNDO);
        }
        if (amap.get(ACTION_KEY_REDO) == null) {
            RedoAction redoAction = new RedoAction();
            amap.put(ACTION_KEY_REDO, redoAction);
            imap.put((KeyStroke) redoAction.getValue(Action.ACCELERATOR_KEY), ACTION_KEY_REDO);
        }
        if (amap.get(ACTION_KEY_SAVE) == null) {
        	SaveAction saveAction = new SaveAction();
        	amap.put(ACTION_KEY_SAVE, saveAction);
        	imap.put((KeyStroke) saveAction.getValue(Action.ACCELERATOR_KEY), ACTION_KEY_SAVE);
        }
        // リスナを登録
        textComponent.getDocument().addDocumentListener(new DocListener());
    }
    public UndoManager getUndoManager() { return undoManager; }
    /**
     * 元に戻す
     */
    class UndoAction extends AbstractAction {
        UndoAction() {
            super("元に戻す(U)");
            putValue(MNEMONIC_KEY, new Integer('U'));
            putValue(SHORT_DESCRIPTION, "元に戻す");
            putValue(LONG_DESCRIPTION, "元に戻す");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('Z', Event.CTRL_MASK));
//            putValue(SMALL_ICON, SwingUtil.getImageIcon("/resources/EditUndo.png"));
        }
        public void actionPerformed(ActionEvent e) {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        }
    }

    /**
     * やり直し
     */
    class RedoAction extends AbstractAction {
        RedoAction() {
            super("やり直し(R)");
            putValue(MNEMONIC_KEY, new Integer('R'));
            putValue(SHORT_DESCRIPTION, "やり直し");
            putValue(LONG_DESCRIPTION, "やり直し");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('Y', Event.CTRL_MASK));
//            putValue(SMALL_ICON, SwingUtil.getImageIcon("/resources/EditRedo.png"));
        }
        public void actionPerformed(ActionEvent e) {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        }
    }
    /**
     * 保存 
     */
    class SaveAction extends AbstractAction{
    	SaveAction(){
    		super("保存(R)");
    		putValue(MNEMONIC_KEY,new Integer('S'));
    		putValue(SHORT_DESCRIPTION,"保存");
    		putValue(LONG_DESCRIPTION,"保存");
    		putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke('S',Event.CTRL_MASK));
    	}
    	public void actionPerformed(ActionEvent e){
    		JFileChooser filechooser = new JFileChooser();

    	    int selected = filechooser.showSaveDialog(textC);
    	    if (selected == JFileChooser.APPROVE_OPTION){
    	      File file = filechooser.getSelectedFile();
//    	      System.out.println(file.getAbsolutePath());
    	      File logFile = new File(file.getAbsolutePath()+".log");
    	      try {
				FileWriter fileWriter = new FileWriter(file);
				FileWriter logFileWriter = new FileWriter(logFile);
				fileWriter.write(textC.getText());
				fileWriter.close();
				logFileWriter.write(logTextC.getText());
				logFileWriter.close();
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
    	      
//    	      label.setText(file.getName());
    	    }else if (selected == JFileChooser.CANCEL_OPTION){
//    	      label.setText("キャンセルされました");
    	    }else if (selected == JFileChooser.ERROR_OPTION){
//    	      label.setText("エラー又は取消しがありました");
    	    }
    	}
    }
    
    /** ドキュメントが変更されたときのリスナー. */
    private class DocListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            if (e instanceof DefaultDocumentEvent) {
                DefaultDocumentEvent de = (DefaultDocumentEvent) e;
                undoManager.addEdit(de);
            }
        }
        public void removeUpdate(DocumentEvent e) {
            if (e instanceof DefaultDocumentEvent) {
                DefaultDocumentEvent de = (DefaultDocumentEvent) e;
                undoManager.addEdit(de);
            }
        }
        public void changedUpdate(DocumentEvent e) {
            // 属性が変わったときは、何もしなくてよい。
        }
    }   
}