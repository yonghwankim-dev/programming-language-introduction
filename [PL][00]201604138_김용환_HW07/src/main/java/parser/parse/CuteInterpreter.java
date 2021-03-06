package parser.parse;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
		ClassLoader cloader = ParserMain.class.getClassLoader();
        String path = cloader.getResource("interpreter/as07_01.txt").getFile();

        path = URLDecoder.decode(path,"utf-8");
        
        
		File file = new File(path);
		CuteParser cuteParser = new CuteParser(file);
		CuteInterpreter interpreter = new CuteInterpreter();
		
		Node parseTree = cuteParser.parseExpr();
		Node resultNode = interpreter.runExpr(parseTree);
		NodePrinter nodePrinter = new NodePrinter(resultNode);
		nodePrinter.prettyPrint();
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
			// QutoteNode를 감싸고 있는 소괄호(ListNode)가 존재하기 때문에 cdr로 괄호 안으로 들어간다.
			if (list.cdr().car() instanceof QuoteNode)/*Quote 내부의 리스트의 경우 계산하지 않음*/
			{
				return list;
			}
			else {	//Qutoe이 붙어있지 않는 경우
				
				return runFunction((FunctionNode) list.car(), list.cdr());
			}
		 }
		 
		 if (list.car() instanceof BinaryOpNode) {
			if(list.cdr().car() instanceof QuoteNode/*Quote 내부의 리스트의 경우 계산하지 않음*/)
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
		 	// CAR, CDR, CONS, NULL_Q…등 모든 function node에 대한 동작 구현
			case CAR:
				//CAR로 통해 반환하는 되는 것은 순수한 Integer이거나 리스트 이다.
				if (operand.car() instanceof QuoteNode)
				{
					Node result = runExpr(operand.cdr());
					return ((ListNode) result).car();
				}
				//재귀적인 호출 수행
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
					//tail을 재귀 호출
					tail = runFunction(operator, operand.cdr());
					
					//CAR를 호출하여 List를 하나 제거한다.
					//(cons 1 '(2 3 4) => ( 1 2 3 4 )
					tail = ((ListNode) tail).car();
					
					return ListNode.cons(head,(ListNode) tail);
				}else if(operand.car() instanceof ListNode){	// head가 List인 경우
					//head에 대한 cons 재귀 호출
					head = runFunction(operator, (ListNode) operand.car());
					
					//tail에 대한 cons 재귀 호출
					tail = runFunction(operator, operand.cdr());
					
					//CAR를 호출하여 List를 하나 제거한다.
					tail = ((ListNode) tail).car();
					
					return ListNode.cons(head, (ListNode) tail);
				}else {	//operand의 head가 아무것도 없을때.
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
					/*atom? ' a 와 같은 경우*/
					Node result1 = operand.cdr().car();
					Node result2 = operand.cdr().cdr();
					if(!(result1 instanceof ListNode) && result2==ListNode.ENDLIST)
					{
						return BooleanNode.TRUE_NODE;
					}
					
					
					if(operand.cdr()!=ListNode.ENDLIST) {	//List안에 노드가 있는 경우
						return BooleanNode.FALSE_NODE;
					}else {									//List가 끝인경우
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
					// ( eq? '( a b ) ' ( a b ) ) 와 같이 tail의 tail에 ListNode안에 다른 노드가 있다면
					// tail인 ListNode만 반환한다.
					if(operand.cdr().cdr()!=ListNode.ENDLIST)
					{
						return operand.cdr();
					}else {	//tail의 tail이 ENDLIST인경우
						return operand.cdr().car();
					}
					
				}else if(operand.car() instanceof ListNode)
				{
					// head와 tail의 값을 가젼온다.
					eq_head = runFunction(operator, (ListNode) operand.car());
					eq_tail = runFunction(operator, operand.cdr());
					
					//재귀함수를 통하여 tail이 null인 경우는 base case이므로 참조한 eq_head를 반환
					if(eq_tail==null)
					{
						return eq_head;
					}

					//equals를 통한 객체 비교, 리스트끼리의 비교는 #F
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
				//head노드가 booleanNode이면 반대 논리값으로 반환한다.
				if(operand.car() instanceof BooleanNode)
				{
					if(operand.car()==BooleanNode.TRUE_NODE)
					{
						return BooleanNode.FALSE_NODE;
					}else {
						return BooleanNode.TRUE_NODE;
					}
				}
				// 소괄호가 있다면 소괄호의 결과를 참조하여 반대 논리값으로 반환한다.
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
				// ( cond ) 인경우
				if(operand==ListNode.ENDLIST)
				{
					break;
				}else {
					//조건을 실행한다.
					Node expr = runExpr(((ListNode) operand.car()).car());

					if(expr==BooleanNode.TRUE_NODE)
					{
						//조건이 참인경우 해당 조건의 참인 수식 수행
						Node result = runExpr(((ListNode) operand.car()).cdr());
						if(result instanceof IntNode)	// 연산을 통해서 나온 Integer인경우
						{
							return result;
						}else if(result instanceof ListNode) {	// 연산없이 단일 Integer인경우
							return ((ListNode) result).car();
						}
						
					}else if(expr==BooleanNode.FALSE_NODE) {	// 조건이 거짓이 경우 다음 조건으로 이동
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
		// 구현과정에서 필요한 변수 및 함수 작업 가능
		switch (operator.binType) {
			// +, -, *, = 등 모든 binary node에 대한 동작 구현
			// +,-,/ 등에 대한 바이너리 연산 동작 구현
			case PLUS:
				if(list.cdr()==ListNode.ENDLIST)
				{
					return list.car();
				}else if(list.cdr() instanceof ListNode){
					Node first = runExpr(list.cdr().car());			//첫번째값
					Node second = runExpr(list.cdr().cdr().car());	//두번째 값
					
					//둘다 정수이면 합계를 반환
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
						//비교 결과를 result에 저장
						boolean result = ((IntNode) first).getValue() < ((IntNode) second).getValue();
						
						//result의 true, false에 따른 BooleanNode 반환
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
			///…
			default:
			break;
		}
		return null;
	}
}
