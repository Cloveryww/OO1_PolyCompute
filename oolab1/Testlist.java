package oolab1;

import java.util.ArrayList;


public class Testlist {

	private class node
	{
		int no;
		int num;
	}
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		ArrayList<node> list1 = new ArrayList<node>();
		ArrayList<node> list2 = new ArrayList<node>();
		for(int i=0;i<10;i++)
		{
			node temp = new Testlist().new node();
			temp.no=i;
			temp.num = i+100;
			list1.add(temp);
		}
		for(int i=0;i<10;i=i+2)
		{
			list2.add(list1.get(i));
		}
		list1.remove(8);
		list2.remove(0);
		list1.get(2).no=-1;
		
		int c =1;

	}

}
