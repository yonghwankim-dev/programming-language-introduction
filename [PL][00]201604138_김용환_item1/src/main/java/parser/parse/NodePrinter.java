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
    	// ���� �κ��� �־��� ��� ���Ŀ� �°� �ڵ带 �ۼ��Ͻÿ�.
    	
    	//ListNode�� head ����
    	Node node = listNode.car();
    	//ListNode�� head�� �Ǵٸ� ListNode�� ��� ex) List->List
    	if(node instanceof ListNode)
    	{    	
    		//������ head ListNode�� head�� QuoteNode�ΰ��
    		//List->Quote(\')->List
    		if(((ListNode) node).car() instanceof QuoteNode)
    		{
    			sb.append(((ListNode) node).car());	//sb�� (\')�߰�
    			printList(((ListNode) node).cdr());	//tail List�� �ٽ� ����

    		}/*else {	//������ head ListNode�� head�� �Ǵٸ� List�ΰ��
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
		//node�� tail ListNode�� ����
		printNode(listNode.cdr());
    	
    }

    private void printNode(Node node) {
    	if (node == null)
    		return;
    	// ���� �κ��� �־��� ��� ���Ŀ� �°� �ڵ带 �ۼ��Ͻÿ�.
    	//node�� ListNode�� ���
    	if(node instanceof ListNode)
    	{
    		//ListNode�� head�� QuoteNode�ΰ��
    		if(((ListNode) node).car() instanceof QuoteNode)
    		{
    			sb.append("\'");
    			printNode(((ListNode) node).cdr());	//ListNode�� �ٽ� ����
    		}else {
    			//ListNode������ ListNode�� head�� QuoteNode�� �ƴѰ��
    			//List�� ù���� List�ΰ��
    			if(node==root)
    			{
    				sb.append(" ( ");
            		printList((ListNode) node);
            		sb.append(" ) ");	
    			}else {//�Ϲ� ListNode�� ��� List ����
    				printList((ListNode) node);
    			}
    			
    		}
    	}else {
    		//node�� ListNode�� �ƴѰ��
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
        return sb;	//return �߰�
    }
}
