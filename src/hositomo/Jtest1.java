package hositomo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.hamcrest.*;
import org.hamcrest.core.Is;

public class Jtest1 {

	@Test
	public void testInsert() {
		TextTree tree2 = new TextTree();
		tree2.init();
		tree2.insert("曇天");
		tree2.insert("です。");
		String s = "曇天です。";
		assertThat(tree2.getSentence(), is("曇天です。"));
	}
	
	@Test
	public void testGetSentence(){//セルを正しく配置した際にgetSentence()
		TextTree tree = new TextTree();
		tree.init();
		Cell c;
		c = new Cell(2);
		c.enable = true;
		c.text = "本日は";
		c.linkCell(tree.cells.get(1), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(3);
		c.enable = true;
		c.text = "晴天";
		c.linkCell(tree.cells.get(2), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(4);
		c.enable = true;
		c.text = "なり。";
		c.linkCell(tree.cells.get(3), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(5);
		c.enable = false;
		c.text = "曇天";
		c.linkCell(tree.cells.get(2), tree.cells.get(4));
		tree.cells.put(c.id, c);
		
		assertThat(tree.getSentence(),is("本日は晴天なり。"));

	}
	@Test
	public void testRemove(){//Remove（int）（idを直接指定したリムーブ）が動くかのテスト
		TextTree tree = new TextTree();
		tree.init();
		tree.insert("本日は");
		tree.insert("晴天");
		tree.insert("なり。");
		tree.remove(tree.cells.get(tree.gId-2).id);
		assertThat(tree.getSentence(),is("本日はなり。"));
	}
	@Test
	public void testUnLinkCell(){//UnLinkCellのテスト 正常実行時
		TextTree tree = new TextTree();
		tree.init();
		Cell c;
		c = new Cell(2);
		c.enable = true;
		c.text = "本日は";
		c.linkCell(tree.cells.get(1), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(3);
		c.enable = true;
		c.text = "晴天";
		c.linkCell(tree.cells.get(2), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(4);
		c.enable = true;
		c.text = "なり。";
		c.linkCell(tree.cells.get(3), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(5);
		c.enable = false;
		c.text = "曇天";
		c.linkCell(tree.cells.get(2), tree.cells.get(4));
		tree.cells.put(c.id, c);
		tree.gId = 6;
		
		c.unLinkCell(tree.cells.get(2));//targetCellが前方(forward)の場合
		
		
		assertTrue(c.forwardAnchors.isEmpty());
		List<Integer> li = new ArrayList<Integer>();
		li.add(5);
		assertThat(tree.cells.get(2).backwardAnchors.containsAll(li),is(false));
		
		c.unLinkCell(tree.cells.get(4));
		
		assertTrue(c.backwardAnchors.isEmpty());
		assertThat(tree.cells.get(4).forwardAnchors.containsAll(li),is(false));

	}
	@Test
	public void testInsert2(){//insert(String ,Int offset)のテスト
		TextTree tree = new TextTree();
		tree.init();
		tree.insert("本日は");
		tree.insert("なり。");
		tree.insert("晴天", 3);
		assertThat(tree.getSentence(),is("本日は晴天なり。"));//セルの分割が無い場合。（セルとセルの間にセルを挿入する）
		
		tree.init();
		tree.insert("本日は");
		tree.insert("晴天");
		tree.insert("なり。",5);
		assertThat(tree.getSentence(),is("本日は晴天なり。"));//offsetが一番後ろをさしているinsert(String s)と同じ動作が必要な場合。
		
		TextTree tree2 = new TextTree();
		tree2.init();
		tree2.insert("本日は");
		tree2.insert("晴天なり。");
		tree2.insert("まことに", 3);
		tree2.insert("かも", 9);
		assertThat(tree2.getSentence(),is("本日はまことに晴天かもなり。"));
		
	}
	
	@Test
	public void testSplitByLength(){
		String s = "本日は晴天なり。";
		String[] ss = Cell.splitByLength(s, 3);
		String[] ac = new String[2];
		ac[0] = "本日は";
		ac[1] = "晴天なり。";
		
		//		System.out.println(kotae.toString());
		assertArrayEquals(ss, ac);
		
	}
	@Test
	public void splitTest(){
		TextTree tree = new TextTree();
		tree.init();
		Cell c;
		c = new Cell(2);
		c.enable = true;
		c.text = "本日は";
		c.linkCell(tree.cells.get(1), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(3);
		c.enable = true;
		c.text = "晴天";
		c.linkCell(tree.cells.get(2), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(4);
		c.enable = true;
		c.text = "なり。";
		c.linkCell(tree.cells.get(3), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(5);
		c.enable = false;
		c.text = "曇天";
		c.linkCell(tree.cells.get(2), tree.cells.get(4));
		tree.cells.put(c.id, c);
		tree.gId = 6;
		
		tree.split(tree.cells.get(3), 1);
//		System.out.println(tree.cells.get(6).text + ":"+tree.cells.get(7).text);
		
		assertThat(tree.cells.get(6).text,is("晴"));
		assertThat(tree.cells.get(7).text,is("天"));
		assertThat(tree.getSentence(),is("本日は晴天なり。"));
		List<Integer> li = new ArrayList<Integer>();
	}
	
	@Test
	public void removeTest2(){//remove(int,int)のテスト
		TextTree tree = new TextTree();
		tree.init();
		Cell c;
		c = new Cell(2);
		c.enable = true;
		c.text = "本日は";
		c.linkCell(tree.cells.get(1), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(3);
		c.enable = true;
		c.text = "晴天";
		c.linkCell(tree.cells.get(2), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(4);
		c.enable = true;
		c.text = "なり。";
		c.linkCell(tree.cells.get(3), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(5);
		c.enable = false;
		c.text = "曇天";
		c.linkCell(tree.cells.get(2), tree.cells.get(4));
		tree.cells.put(c.id, c);
		tree.gId = 6;
		
		tree.remove(1, 1);
		assertThat(tree.getSentence(),is("本は晴天なり。"));
		tree.remove(2,4);
		assertThat(tree.getSentence(),is("本は。"));
	}
	
	@Test
	public void removeTest3(){//remove(int[])の場合
		TextTree tree = new TextTree();
		tree.init();
		Cell c;
		c = new Cell(2);
		c.enable = true;
		c.text = "本日は";
		c.linkCell(tree.cells.get(1), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(3);
		c.enable = true;
		c.text = "晴天";
		c.linkCell(tree.cells.get(2), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(4);
		c.enable = true;
		c.text = "なり。";
		c.linkCell(tree.cells.get(3), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(5);
		c.enable = false;
		c.text = "曇天";
		c.linkCell(tree.cells.get(2), tree.cells.get(4));
		tree.cells.put(c.id, c);
		
		c = new Cell(6);
		c.enable = true;
		c.text = "ようようなりて";
		c.linkCell(tree.cells.get(4), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(7);
		c.enable = true;
		c.text = "白くなりゆく";
		c.linkCell(tree.cells.get(6), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(8);
		c.enable = true;
		c.text = "山。";
		c.linkCell(tree.cells.get(7), null);
		tree.cells.put(c.id, c);
		
		tree.gId = 9;
		
		List<Integer> l = new ArrayList<Integer>();
		l.add(4);
		l.add(6);
		l.add(7);
		tree.remove(l);
		assertThat(tree.getSentence(),is("本日は晴天山。"));
	}
	
	

}
