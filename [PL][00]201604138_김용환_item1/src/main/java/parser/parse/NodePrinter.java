package parser.parse;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import parser.ast.*;

public class NodePrinter {
    private final String OUTPUT_FILENAME = "output07.txt";
    private StringBuffer sb = new StringBuffer();
    private Node root;

    public NodePrinter(Node root) {
        this.root = root;
    }
    
    private void printList(ListNode listNode) {
    	if (listNode == ListNode.EMPTYLIST) {
    		sb.append("( )");
    		return;
    	}
    	if (listNode == ListNode.ENDLIST) {
    		return;
    	}
    	// 이후 부분을 주어진 출력 형식에 맞게 코드를 작성하시오.
    	
    	//ListNode의 head 참조
    	Node node = listNode.car();
    	//ListNode의 head가 또다른 ListNode인 경우 ex) List->List
    	if(node instanceof ListNode)
    	{    	
    		//참조한 head ListNode의 head가 QuoteNode인경우
    		//List->Quote(\')->List
    		if(((ListNode) node).car() instanceof QuoteNode)
    		{
    			sb.append(((ListNode) node).car());	//sb에 (\')추가
    			printList(((ListNode) node).cdr());	//tail List를 다시 참조

    		}/*else {	//참조한 head ListNode의 head가 또다른 List인경우
    			//ex) List->List
    			sb.append(" ( ");
        		printList((ListNode)node);
        		sb.append(" ) ");	
    		}*/
    		
    		else if(((ListNode) node) instanceof ListNode)
    		{
    			sb.append(" ( ");
        		printList((ListNode)node);
        		sb.append(" ) ");
    		}else {
    			printNode(node);
    		}
    	}else {
    			sb.append(" " + node + " ");
    	}
		//node의 tail ListNode를 참조
		printNode(listNode.cdr());
    	
    }

    private void printNode(Node node) {
    	if (node == null)
    		return;
    	// 이후 부분을 주어진 출력 형식에 맞게 코드를 작성하시오.
    	//node가 ListNode인 경우
    	if(node instanceof ListNode)
    	{
    		//ListNode의 head가 QuoteNode인경우
    		if(((ListNode) node).car() instanceof QuoteNode)
    		{
    			sb.append("\'");
    			printNode(((ListNode) node).cdr());	//ListNode를 다시 참조
    		}else {
    			//ListNode이지만 ListNode의 head가 QuoteNode가 아닌경우
    			//List가 첫번재 List인경우
    			if(node==root)
    			{
    				sb.append(" ( ");
            		printList((ListNode) node);
            		sb.append(" ) ");	
    			}else {//일반 ListNode인 경우 List 참조
    				printList((ListNode) node);
    			}
    			
    		}
    	}else {
    		//node가 ListNode가 아닌경우
			sb.append(" " +node + " ");
    	}
    }

    //void ->StringBuffer
    public StringBuffer prettyPrint() {
        printNode(root);

        try (FileWriter fw = new FileWriter(OUTPUT_FILENAME);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;	//return 추가
    }
}
