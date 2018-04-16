package oolab1;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

public class PolyCompute {
	private ArrayList<Poly> polyList;
    private ArrayList<Integer> opList;
    private int num;
    public PolyCompute() {
    	this.num=0;
    	this.polyList=new ArrayList<Poly>();
    	this.opList=new ArrayList<Integer>();
    }
    public Poly parseTerm(String s)
    {
    	int t,re;
    	Poly p=new Poly();
    	ArrayList<Integer> numbers;
    	if(s.equals(""))//多项式为空，合法
    	{
    		return p;
    	}
    	else
    	{
    		Pattern pat=Pattern.compile("\\([+-]?\\d+\\,[+-]?\\d+\\)(\\,\\([+-]?\\d+\\,[+-]?\\d+\\))*");
    		Matcher m=pat.matcher(s);
    		if(!m.matches())//一个多项式内部输入不合法，报错
    		{
    			new Error().outputError(3);
    			return null;
    		}
    		else//合法
    		{
    			numbers=new ArrayList<Integer>();
    			String[] terms=s.split("\\(|\\)|\\,");
        		int len=terms.length;
        		for(int i=0;i<len;i++)
        		{
        			if(terms[i].equals(""))
        			{
        				continue;
        			}
        			else
        			{
        				try {
        					t=Integer.parseInt(terms[i]);
        				}catch(NumberFormatException e)
        				{
        					new Error().outputError(4);
        					return null;
        				}
        				if(t>999999||t<-999999)
        				{
        					new Error().outputError(8);
        					return null;
        				}
        				numbers.add(t);
        			}
        		}
        		if(numbers.size()%2!=0)
        		{
        			new Error().outputError(5);
					return null;
        		}
        		for(int i=1;i<numbers.size();i=i+2)
        		{
        			if(numbers.get(i)<0)
        			{
        				new Error().outputError(6);
    					return null;
        			}
        		}
        		for(int i=0;i<numbers.size();i=i+2)
        		{
        			Term term=new Term();
        			term.coeff=numbers.get(i);
        			term.expn=numbers.get(i+1);
        			p.content.add(term);
        		}
        		re=p.sort(true);
        		if(re==-1)
        		{
        			return null;
        		}        		
    		}
    		if(p.content.size()>50)
        	{
        		new Error().outputError(10);
    			return null;
        	}
    		return p; 
    	}
    }
    public int parsePoly(String s) {//匹配多项式，并存入polyList中
    	int operatorNum=0;
    	String str_no_space = s.replaceAll("\\s+", "");//删除所有空格
    	//先检查是否符合  +{}+{}+{}  格式
    	Pattern p0=Pattern.compile("([+-])?\\{(.*)\\}([+-]\\{(.*)\\})*");
    	Matcher m0=p0.matcher(str_no_space);
    	if(!m0.matches())
    	{
    		new Error().outputError(0);
    		return -1;
    	}
    	else
    	{
    		//读取操作符
    		Pattern p1=Pattern.compile("[+-]\\{");
    		Matcher m1=p1.matcher(str_no_space);
    		while(m1.find())
    		{
    			int index=m1.start();
    			if(str_no_space.charAt(index)=='+')
    			{
    				opList.add(1);
    			}
    			else {
    				opList.add(0);
    			}
    			operatorNum++;
    		}
    		//读取每个多项式
    		String[] polys=str_no_space.split("\\{|\\}");
    		int slicenum=polys.length;
    		if((slicenum==(operatorNum*2))||(slicenum==(operatorNum*2+2)))
    		{
    			for(int i=1;i<slicenum;i=i+2)
    			{
    				Poly temp;
    				temp=parseTerm(polys[i]);
    				if(temp!=null)
    				{
    					polyList.add(temp);
    					num++;
    				}
    				else
    				{
    					return -1;
    				}
    			}
    		}else
    		{
    			new Error().outputError(2);
    			return -1;
    		}
    	}
    	if(this.polyList.size()>20)
    	{
    		new Error().outputError(9);
			return -1;
    	}
    	return 0;
    }
    public Poly compute(){
    	Poly out = new Poly();
    	int opNum=opList.size();
    	if(opNum==num)//第一个多项式前有符号
    	{
    		for(int i=0;i<num;i++)
    		{
    			if(this.opList.get(i)==1)
    			{
    				out=out.add(this.polyList.get(i));
    			}else
    			{
    				out=out.sub(this.polyList.get(i));
    			}
    		}
    	}else//第一个多项式前没符号
    	{
    		out=out.add(this.polyList.get(0));
    		for(int i=1;i<num;i++)
    		{
    			if(this.opList.get(i-1)==1)
    			{
    				out=out.add(this.polyList.get(i));
    			}else
    			{
    				out=out.sub(this.polyList.get(i));
    			}
    		}
    	}
    	return out;
    }
    public static void main(String args[]){
          //从console获取用户输入的多项式计算表达式：String
    	String str;
    	int re;
    	Poly out;
    	PolyCompute pc = new PolyCompute();
    	Scanner sc=new Scanner(System.in);  
    	str = sc.nextLine();
    	if(str.length()==0)
    	{
    		pc.new Error().outputError(7);
    		sc.close();
    		return;
    	}
    	re = pc.parsePoly(str);
    	if(re==-1)
    	{
    		sc.close();
    		return;
    	}
        out=pc.compute();
        out.output();
    	sc.close();
    	return;
    }
    public class Poly{
    	public ArrayList<Term> content ;
    	public Poly()
    	{
    		content=new ArrayList<Term>();
    	}
       	public int sort(boolean ifcheck)//对content进行从小到大的排序，并且可以选择检查是否指数唯一
    	{
    		int coeff_t, expn_t;
    		int min_index;
    		int len=content.size();
    		for(int i=0;i<len;i++)
    		{
    			min_index=i;
    			for(int j=i;j<len;j++)
    			{
    				if(content.get(j).expn<content.get(min_index).expn)
    				{
    					min_index=j;
    				}
    			}
    			if(min_index!=i)
    			{
    				coeff_t=content.get(i).coeff;
    				expn_t=content.get(i).expn;
    				content.get(i).coeff=content.get(min_index).coeff;
    				content.get(i).expn=content.get(min_index).expn;
    				content.get(min_index).coeff=coeff_t;
    				content.get(min_index).expn=expn_t;
    			}
    		}
    		if(ifcheck)
    		{
    			//检查是否有指数一样的项，报错
    			for(int i=1;i<len;i++)
    			{
    				if(content.get(i).expn==content.get(i-1).expn)
    				{
    					new Error().outputError(1);
    					return -1;
    				}
    			}
    		}
    		else
    		{
    			//检查是否有指数一样的项，有的话合并
    			for(int i=1;i<content.size();i++)
    			{
    				if(content.get(i).expn==content.get(i-1).expn)
    				{
    					content.get(i-1).coeff=content.get(i-1).coeff+content.get(i).coeff;
    					content.remove(i);
    					i--;
    				}
    			}
    		}
    		return 0;
    	}
       	public void deleteZero()
       	{
       		for(int i=0;i<this.content.size();i++)
       		{
       			if(this.content.get(i).coeff==0)
       			{
       				this.content.remove(i);
       				i--;
       			}
       		}
       		return ;
       	}
       	public Poly add(Poly p)
       	{
       		int len1=this.content.size();
       		int len2=p.content.size();
       		int i=0,j=0;
       		Poly out=new Poly();
       		while(true)
       		{
       			if(i==len1||j==len2)
       			{
       				break;
       			}
       			if(this.content.get(i).expn<p.content.get(j).expn)
       			{
       				out.content.add(this.content.get(i));
       				i++;
       			}
       			else if(this.content.get(i).expn>p.content.get(j).expn)
       			{
       				out.content.add(p.content.get(j));
       				j++;
       			}
       			else
       			{
       				Term t=new Term();
       				t.expn=this.content.get(i).expn;
       				t.coeff=this.content.get(i).coeff+p.content.get(j).coeff;
       				out.content.add(t);
       				i++;
       				j++;
       			}
       		}
       		if(i<len1)
       		{
       			for(;i<len1;i++)
       			{
       				out.content.add(this.content.get(i));
       			}
       		}else if(j<len2)
       		{
       			for(;j<len2;j++)
       			{
       				out.content.add(p.content.get(j));
       			}
       		}
       		out.deleteZero();
       		return out;
       	}
       	public Poly sub(Poly p)
       	{
       		int len1=this.content.size();
       		int len2=p.content.size();
       		int i=0,j=0;
       		Poly out=new Poly();
       		while(true)
       		{
       			if(i==len1||j==len2)
       			{
       				break;
       			}
       			if(this.content.get(i).expn<p.content.get(j).expn)
       			{
       				out.content.add(this.content.get(i));
       				i++;
       			}
       			else if(this.content.get(i).expn>p.content.get(j).expn)
       			{
       				Term t=new Term();
       				t.expn=p.content.get(j).expn;
       				t.coeff=-p.content.get(j).coeff;
       				out.content.add(t);
       				j++;
       			}
       			else
       			{
       				Term t=new Term();
       				t.expn=this.content.get(i).expn;
       				t.coeff=this.content.get(i).coeff-p.content.get(j).coeff;
       				out.content.add(t);
       				i++;
       				j++;
       			}
       		}
       		if(i<len1)
       		{
       			for(;i<len1;i++)
       			{
       				out.content.add(this.content.get(i));
       			}
       		}else if(j<len2)
       		{
       			for(;j<len2;j++)
       			{
       				Term t=new Term();
       				t.expn=p.content.get(j).expn;
       				t.coeff=-p.content.get(j).coeff;
       				out.content.add(t);
       			}
       		}
       		out.deleteZero();
       		return out;
       	}
       	public void output()
       	{
       		int len=this.content.size();
       		if(len==0)
       		{
       			System.out.print("0");
       			return;
       		}else if(len==1)
       		{
       			System.out.print("{(");
           		System.out.print(Integer.toString(this.content.get(0).coeff)+","+Integer.toString(this.content.get(0).expn));
           		System.out.print(")}");
       		}
       		else
       		{
       			System.out.print("{(");
       			System.out.print(Integer.toString(this.content.get(0).coeff)+","+Integer.toString(this.content.get(0).expn)+")");
       			for(int i=1;i<len;i++)
       			{
       				System.out.print(",("+Integer.toString(this.content.get(i).coeff)+","+Integer.toString(this.content.get(i).expn)+")");
       			}
           		System.out.print("}");
       		}
       	}
    }
    public class Error{
    	private String[] str= {
    			"The input must be in the format: op{}op{}op{}...op{}!",
    			"The expn of each item in one input polynomial cannot be the same!",
    			"The number of polynomials does not match the number of operators!",
    			"The input polynomial must be in the format: {(n,n),(n,n),...,(n,n)}!",
    			"The decimal number in the input must be in the format : +/-xxx !(x means number)",
    			"The number of coefficients and indices in the polynomial does not match!",
    			"The exponent in the polynomial must be greater than or equal to 0!",
    			"The input can not be empty!",
    			"The absolute value of the number cannot exceed 999999!",
    			"The number of polynomials cannot exceed 20!",
    			"The number of pairs of a polynomial cannot exceed 50!"
    	};
    	
    	public void outputError(int no)
    	{
    		if(no>=str.length)
    		{
    			return;
    		}
    		System.out.println("ERROR");
    		System.out.println("#"+str[no]);
    		return;
    	}
    }
    public class Term{
    	public int coeff;
    	public int expn;
    }
}
