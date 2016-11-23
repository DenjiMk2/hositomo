package hositomo;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.hamcrest.*;
import org.hamcrest.core.Is;

public class Jtest1 {

	@Test
	public void test() {
		TextTree tree2 = new TextTree();
		tree2.init();
		tree2.insert("曇天");
		tree2.insert("です。");
		String s = "曇天です。";
		assertThat(tree2.getSentence(), is("曇天です。"));
	}
	
	@Test
	public void test2(){//セルを正しく配置した際にgetSentence()
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
	public void removeTest(){
		TextTree tree = new TextTree();
		tree.init();
		tree.insert("本日は");
		tree.insert("晴天");
		tree.insert("なり。");
		tree.remove(tree.cells.get(tree.gId-2).id);
		assertThat(tree.getSentence(),is("本日はなり。"));
	}

}
