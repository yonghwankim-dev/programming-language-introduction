package parser.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.text.StyledEditorKit.BoldAction;

import parser.ast.BooleanNode;
import parser.ast.FunctionNode;
import parser.ast.IdNode;
import parser.ast.IntNode;
import parser.ast.ListNode;
import parser.ast.Node;
import parser.ast.QuoteNode;
import parser.ast.BinaryOpNode;
public class CuteInterpreter {
	public static void main(String[] args) throws UnsupportedEncodingException {
		String save_path = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true)
		{
			try {
				System.out.print(" > ");
				String command = br.readLine();	//input
				Path p = Paths.get("");
				save_path = p.toAbsolutePath().toString() + "/classes/interpreter/as08.txt";	//???? ????
				FileWriter fw = new FileWriter(save_path);
				fw.write(command);
				
				fw.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ClassLoader cloader = ParserMain.class.getClassLoader();
	        String path = cloader.getResource("interpreter/as08.txt").getFile();
	        
	        path = URLDecoder.decode(path,"utf-8");
	        
	        
			File file = new File(path);
			CuteParser cuteParser = new CuteParser(file);
			CuteInterpreter interpreter = new CuteInterpreter(); 
			
			Node parseTree = cuteParser.parseExpr();
			Node resultNode = interpreter.runExpr(parseTree);
			NodePrinter nodePrinter = new NodePrinter(resultNode);
			StringBuffer sb = nodePrinter.prettyPrint();
			
			System.out.println("... " + sb.toString());
		}
		
	}
	
	private void errorLog(String err) {
		System.out.println(err);
	}
	
	public Node runExpr(Node rootExpr) {
		if (rootExpr == null)
		{
			return null;
		}
		
		if (rootExpr instanceof IdNode)
		{
			return rootExpr;
		}
		else if (rootExpr instanceof IntNode)
		{
			return rootExpr;	
		}
		else if (rootExpr instanceof BooleanNode)
		{
			return rootExpr;	
		}else if (rootExpr instanceof ListNode)
		{
			return runList((ListNode) rootExpr);	
		}else
		{
			errorLog("run Expr error");
		 	return null;
		}
	}
	private Node runList(ListNode list) {
		 list = (ListNode)stripList(list);
		 if (list.equals(ListNode.EMPTYLIST))
		 {
			 return list;
		 }
		 
		 if (list.car() instanceof FunctionNode) {
			// QutoteNode?? ???ΰ? ?ִ? ?Ұ?ȣ(ListNode)?? ?????ϱ? ?????? cdr?? ??ȣ ?????? ?????.
			if (list.cdr().car() instanceof QuoteNode)/*Quote ?????? ????Ʈ?? ???? ???????? ????*/
			{
				return list;
			}
			else {	//Qutoe?? ?پ????? ?ʴ? ????
				
				return runFunction((FunctionNode) list.car(), list.cdr());
			}
		 }
		 
		 if (list.car() instanceof BinaryOpNode) {
			if(list.cdr().car() instanceof QuoteNode/*Quote ?????? ????Ʈ?? ???? ???????? ????*/)
			{
				return list;
			}
			else
			{
				return runBinary(list);
			}
		}
		
		 return list;
	}
	
	private Node runFunction(FunctionNode operator, ListNode operand) {
		switch (operator.funcType) {
		 	// CAR, CDR, CONS, NULL_Q???? ???? function node?? ???? ???? ????
			case CAR:
				//CAR?? ???? ??ȯ?ϴ? ?Ǵ? ???? ?????? Integer?̰ų? ????Ʈ ?̴?.
				if (operand.car() instanceof QuoteNode)
				{
					Node result = runExpr(operand.cdr());
					return ((ListNode) result).car();
				}
				//???????? ȣ?? ????
				return runFunction(operator,(ListNode) operand.car());
				
			case CDR:
				if (operand.car() instanceof QuoteNode)
				{
					Node result = runExpr(operand.cdr());
					return ((ListNode) result).cdr(); //Integer or List
				}
				return runFunction(operator,(ListNode) operand.car());

				
			case CONS:
				Node head = null;
				Node tail = null;
				if(operand.car() instanceof QuoteNode)
				{
					return operand.cdr();
				}else if(operand.car() instanceof IntNode)
				{
					head = operand.car();
					//tail?? ???? ȣ??
					tail = runFunction(operator, operand.cdr());
					
					//CAR?? ȣ???Ͽ? List?? ?ϳ? ?????Ѵ?.
					//(cons 1 '(2 3 4) => ( 1 2 3 4 )
					tail = ((ListNode) tail).car();
					
					return ListNode.cons(head,(ListNode) tail);
				}else if(operand.car() instanceof ListNode){	// head?? List?? ????
					//head?? ???? cons ???? ȣ??
					head = runFunction(operator, (ListNode) operand.car());
					
					//tail?? ???? cons ???? ȣ??
					tail = runFunction(operator, operand.cdr());
					
					//CAR?? ȣ???Ͽ? List?? ?ϳ? ?????Ѵ?.
					tail = ((ListNode) tail).car();
					
					return ListNode.cons(head, (ListNode) tail);
				}else {	//operand?? head?? ?ƹ??͵? ??????.
					return operand;
				}
				
			case NULL_Q:
				if(operand.car() instanceof QuoteNode) {
					if (operand.cdr()==ListNode.ENDLIST)
					{
						return BooleanNode.TRUE_NODE;
					}else {
						return runFunction(operator, operand.cdr());
					}
				}else if(operand.car() instanceof ListNode){
					if(operand.car()==ListNode.ENDLIST) {
						return BooleanNode.FALSE_NODE;
					}else
					{
						return runFunction(operator, (ListNode) operand.car());
					}
				
				}else{
					return BooleanNode.FALSE_NODE;
				}
			case ATOM_Q:
				
				if(operand.car() instanceof QuoteNode)		//Qoute
				{		
					/*atom? ' a ?? ???? ????*/
					Node result1 = operand.cdr().car();
					Node result2 = operand.cdr().cdr();
					if(!(result1 instanceof ListNode) && result2==ListNode.ENDLIST)
					{
						return BooleanNode.TRUE_NODE;
					}
					
					
					if(operand.cdr()!=ListNode.ENDLIST) {	//List?ȿ? ???尡 ?ִ? ????
						return BooleanNode.FALSE_NODE;
					}else {									//List?? ???ΰ???
						return BooleanNode.TRUE_NODE;
					}
				}else if (operand.car() instanceof ListNode){	//List										//List
					return runFunction(operator, (ListNode) operand.car());
				}
				break;
			case EQ_Q:
				Node eq_head = null;
				Node eq_tail = null;
				if(operand.car() instanceof QuoteNode)
				{
					// ( eq? '( a b ) ' ( a b ) ) ?? ???? tail?? tail?? ListNode?ȿ? ?ٸ? ???尡 ?ִٸ?
					// tail?? ListNode?? ??ȯ?Ѵ?.
					if(operand.cdr().cdr()!=ListNode.ENDLIST)
					{
						return operand.cdr();
					}else {	//tail?? tail?? ENDLIST?ΰ???
						return operand.cdr().car();
					}
					
				}else if(operand.car() instanceof ListNode)
				{
					// head?? tail?? ???? ?????´?.
					eq_head = runFunction(operator, (ListNode) operand.car());
					eq_tail = runFunction(operator, operand.cdr());
					
					//?????Լ??? ???Ͽ? tail?? null?? ?????? base case?̹Ƿ? ?????? eq_head?? ??ȯ
					if(eq_tail==null)
					{
						return eq_head;
					}

					//equals?? ???? ??ü ????, ????Ʈ?????? ?񱳴? #F
					if(eq_head.equals(eq_tail))
					{
						return BooleanNode.TRUE_NODE;
					}else {
						return BooleanNode.FALSE_NODE;
					}
				}else {
					return operand.car();
				}
			case NOT:
				//head???尡 booleanNode?̸? ?ݴ? ?????????? ??ȯ?Ѵ?.
				if(operand.car() instanceof BooleanNode)
				{
					if(operand.car()==BooleanNode.TRUE_NODE)
					{
						return BooleanNode.FALSE_NODE;
					}else {
						return BooleanNode.TRUE_NODE;
					}
				}
				// ?Ұ?ȣ?? ?ִٸ? ?Ұ?ȣ?? ?????? ?????Ͽ? ?ݴ? ?????????? ??ȯ?Ѵ?.
				else if(operand.car() instanceof ListNode){
					Node result = runExpr(operand.car());
					if(result instanceof BooleanNode)
					{
						if(result==BooleanNode.TRUE_NODE)
						{
							return BooleanNode.FALSE_NODE;
						}else {
							return BooleanNode.TRUE_NODE;
						}
					}
				}
			case COND:
				// ( cond ) ?ΰ???
				if(operand==ListNode.ENDLIST)
				{
					break;
				}else {
					//?????? ?????Ѵ?.
					Node expr = runExpr(((ListNode) operand.car()).car());

					if(expr==BooleanNode.TRUE_NODE)
					{
						//?????? ???ΰ??? ?ش? ?????? ???? ???? ????
						Node result = runExpr(((ListNode) operand.car()).cdr());
						if(result instanceof IntNode)	// ?????? ???ؼ? ???? Integer?ΰ???
						{
							return result;
						}else if(result instanceof ListNode) {	// ???????? ???? Integer?ΰ???
							return ((ListNode) result).car();
						}
						
					}else if(expr==BooleanNode.FALSE_NODE) {	// ?????? ?????? ???? ???? ???????? ?̵?
						return runFunction(operator, (ListNode) operand.cdr());
					}
				}
			default:
				break;
		}
		
		return null;
	}
	private Node stripList(ListNode node) {
		if (node.car() instanceof ListNode && node.cdr().car() == null) {
			Node listNode = node.car();
			return listNode;
		}else {
			 return node;
		}
	}
	
	private Node runBinary(ListNode list) {
		BinaryOpNode operator = (BinaryOpNode) list.car();
		// ???????????? ?ʿ??? ???? ?? ?Լ? ?۾? ????
		switch (operator.binType) {
			// +, -, *, = ?? ???? binary node?? ???? ???? ????
			// +,-,/ ? ???? ???̳ʸ? ???? ???? ????
			case PLUS:
				if(list.cdr()==ListNode.ENDLIST)
				{
					return list.car();
				}else if(list.cdr() instanceof ListNode){
					Node first = runExpr(list.cdr().car());			//ù??°??
					Node second = runExpr(list.cdr().cdr().car());	//?ι?° ??
					
					//?Ѵ? ?????̸? ?հ踦 ??ȯ
					if(first instanceof IntNode && second instanceof IntNode)
					{
						String result = Integer.toString(((IntNode) first).getValue() + ((IntNode) second).getValue());
						return new IntNode(result);
					}
				}
				break;
				
			case MINUS:
				if(list.cdr()==ListNode.ENDLIST)
				{
					return list.car();
				}else if(list.cdr() instanceof ListNode){
					Node first = runExpr(list.cdr().car());
					Node second = runExpr(list.cdr().cdr().car());
					if(first instanceof IntNode && second instanceof IntNode)
					{
						String result = Integer.toString(((IntNode) first).getValue() - ((IntNode) second).getValue());
						return new IntNode(result);
					}
				}
				break;
			case TIMES:
				if(list.cdr()==ListNode.ENDLIST)
				{
					return list.car();
				}else if(list.cdr() instanceof ListNode){
					Node first = runExpr(list.cdr().car());
					Node second = runExpr(list.cdr().cdr().car());
					if(first instanceof IntNode && second instanceof IntNode)
					{
						String result = Integer.toString(((IntNode) first).getValue() * ((IntNode) second).getValue());
						return new IntNode(result);
					}
				}
				break;
			case DIV:
				if(list.cdr()==ListNode.ENDLIST)
				{
					return list.car();
				}else if(list.cdr() instanceof ListNode){
					Node first = runExpr(list.cdr().car());
					Node second = runExpr(list.cdr().cdr().car());
					if(first instanceof IntNode && second instanceof IntNode)
					{
						String result = Integer.toString(((IntNode) first).getValue() / ((IntNode) second).getValue());
						return new IntNode(result);
					}
				}
				break;
				
			case LT:
				if(list.cdr()==ListNode.ENDLIST)
				{
					return list.car();
				}else if(list.cdr() instanceof ListNode){
					Node first = runExpr(list.cdr().car());
					Node second = runExpr(list.cdr().cdr().car());
					if(first instanceof IntNode && second instanceof IntNode)
					{
						//???? ?????? result?? ????
						boolean result = ((IntNode) first).getValue() < ((IntNode) second).getValue();
						
						//result?? true, false?? ???? BooleanNode ??ȯ
						if (result)
						{
							return BooleanNode.TRUE_NODE;
						}else {
							return BooleanNode.FALSE_NODE;
						}
					}
				}
				break;
			case EQ:
				if(list.cdr()==ListNode.ENDLIST)
				{
					return list.car();
				}else if(list.cdr() instanceof ListNode){
					Node first = runExpr(list.cdr().car());
					Node second = runExpr(list.cdr().cdr().car());
					if(first instanceof IntNode && second instanceof IntNode)
					{
						boolean result = ((IntNode) first).getValue().equals(((IntNode) second).getValue());
						if (result)
						{
							return BooleanNode.TRUE_NODE;
						}else {
							return BooleanNode.FALSE_NODE;
						}
					}
				}
				break;
			case GT:
				if(list.cdr()==ListNode.ENDLIST)
				{
					return list.car();
				}else if(list.cdr() instanceof ListNode){
					Node first = runExpr(list.cdr().car());
					Node second = runExpr(list.cdr().cdr().car());
					if(first instanceof IntNode && second instanceof IntNode)
					{
						boolean result = ((IntNode) first).getValue() > ((IntNode) second).getValue();
						if (result)
						{
							return BooleanNode.TRUE_NODE;
						}else {
							return BooleanNode.FALSE_NODE;
						}
					}
				}
				break;				
			///??
			default:
			break;
		}
		return null;
	}
}
