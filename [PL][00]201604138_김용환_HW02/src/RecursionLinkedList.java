
public class RecursionLinkedList {
	private Node head;
	private static char UNDEF = Character.MIN_VALUE;
	
	/**
	* 새롭게 생성된 노드를 리스트의 처음으로 연결
	*/
	private void linkFirst(char element) {
		head = new Node(element, head);
	}
	
	/**
	* 과제 (1) 주어진 Node x의 마지막으로 연결된 Node의 다음으로 새롭게 생성된 노드를 연결
	*
	* @param element
	* 데이터
	* @param x
	* 노드
	*/
	private void linkLast(char element, Node x) {
		/*x노드가 마지막 노드인지 검사한다.*/
		if(x.next==null)
		{
			x.next = new Node(element,null);	//리스트의 마지막 노드에 새로운 노드를 연결한다.
			return;
		}
		linkLast(element, x.next);				//노드 x가 마지막 노드가 아닌경우 다음 노드로 이동한다.
	}
	
	/**
	* 이전 Node의 다음 Node로 새롭게 생성된 노드를 연결
	*
	* @param element
	* 원소
	* @param pred
	* 이전노드
	*/
	private void linkNext(char element, Node pred) {
		Node next = pred.next;
		pred.next = new Node(element, next);
	}
	
	/**
	* 리스트의 첫번째 원소 해제(삭제)
	*
	* @return 첫번째 원소의 데이터
	*/
	private char unlinkFirst() {
		Node x = head;
		char element = x.item;
		head = head.next;
		x.item = UNDEF;
		x.next = null;
		return element;
	}
	
	/**
	* 이전Node의 다음 Node연결 해제(삭제)
	*
	* @param pred
	* 이전노드
	* @return 다음노드의 데이터
	*/
	private char unlinkNext(Node pred) {
		Node x = pred.next;
		Node next = x.next;
		char element = x.item;
		x.item = UNDEF;
		x.next = null;
		pred.next = next;
		return element;
	}
	
	/**
	 * 과제 (2) x노드에서 index만큼 떨어진 Node 반환
	 * @param index
	 * 참조하고자 하는 index 위치
	 * @param x
	 * 참조하고자 하는 노드
	 * @return
	 */
	private Node node(int index, Node x) {
		// 채워서 사용, recursion 사용
		
		//index가 0인것은 해당 index만큼 거리를 이동했다는 뜻이다.
		if(index==0) 
		{
			return x;	
		}
		/**
		 * 1. index-1 : index를 카운트 역할로써 1씩 감소킨다.
		 * 2. x.next  : 다음 노드로 이동한다.
		 */
		return node(index-1,x.next);	
	}
	
	/**
	 * 과제 (3) 노드로부터 끝까지의 리스트의 노드 갯수 반환
	 * next 노드를 통하여 다음 노드로 이동하면서 길이를 1씩 더한다.
	 * @param x	길이를 구하고자 하는 노드
	 * @return	연결된 노드의 길이
	 */
	private int length(Node x) {
	// 채워서 사용, recursion 사용
		
		//x가 null인 경우는 마지막 노드의 다음으로 이동한 것이다.
		if(x==null)
		{
			return 0;
		}
		return length(x.next)+1;	//재귀적으로 호출하여 1씩 더한다.
	}
	
	/**
	 * 과제 (4) 노드로부터 시작하는 리스트의 내용 반환
	 * @param x 출력하고자 하는 노드
	 * @return	노드와 연결된 요소들을 가공된 문자열로 설정한 문자열
	 */
	private String toString(Node x) {
	// 채워서 사용, recursion 사용
		//x가 null인 경우는 마지막 노드의 다음으로 이동한 것이다.
		if(x==null)
		{
			return "";	//빈 문자열을 반환한다.
		}
		return x.item +" "+ toString(x.next);	//해당 노드의 요소에 공백 문자열을 붙여서 재귀적으로 호출한다.
	}
	
	/**
	* 추가 과제 (5) 현재 노드의 이전 노드부터 리스트의 끝까지를 거꾸로 만듬
	* ex)노드가 [s]->[t]->[r]일 때, reverse 실행 후 [r]->[t]->[s]
	* @param x
	* 현재 노드
	* @param pred
	* 현재노드의 이전 노드
	*/
	private void reverse(Node x, Node pred) {
	// 채워서 사용, recursion 사용
		
		//x==null인경우는 마지막 노드를 다음으로 이동시킨 경우이다.
		if(x==null)
		{
			/**
			 * 1.기존 첫번째 노드를 가리키던 head를 마지막 노드로 가리키게 한다.
			 * 2.현재 x는 마지막 노드의 다음으로 이동하였기 때문에 마지막 노드는 pred 노드이다.
			 * 3.head가 pred 노드를 가리키게 하여 리스트의 마지막 노드를 가리키게 한다.
			 */
			head = pred;	
			return;
		}
		Node temp = x.next;	//재귀적인 호출을 위하여 노드 x의 다음 노드를 임시저장한다.
		
		/**
		 * 1. x.next가 null인경우는 x가 마지막 노드에 도착한 경우이다.
		 * 2. pred가 null인 경우는 x가 첫번째 노드에 도착한 경우이다.
		 */
		if(x.next==null || pred==null)
		{
			x.next = pred;
		}
		
		//pred가 null이 아닌 경우는 x가 첫번째 노드가 아닌 경우이다.
		if(pred!=null) {
			x.next = pred;	//리스트의 방향을 바꾼다.
		}

		reverse(temp,x);	//x노드를 다음 노드로 이동시키고 pred 노드를 현재 x노드로 설정하여 재귀적으로 호출한다.
	}
	
	/**
	* 리스트를 거꾸로 만듬
	*/
	public void reverse() {
		reverse(head, null);
	}
	
	/**
	* 추가 과제 (6) 두 리스트를 합침 ( A + B )
	* ex ) list1 =[l]->[o]->[v]->[e] , list2=[p]->[l] 일 때,
	* list1.addAll(list2) 실행 후 [l]->[o]->[v]->[e]-> [p]->[l] 
	* @param x
	* list1의 노드
	* @param y 연결하고자 하는 노드
	* list2 의 head
	*/
	private void addAll(Node x, Node y) {
		//y==null인경우는 y의 마지막노드의 다음으로 이동한 경우이다.
		if(y==null)
		{
			return;	//메서드 종료
		}
		linkLast(y.item, x);	//x노드의 마지막 노드 다음에 y 노드의 요소를 생성하여 연결한다.
		addAll(x, y.next);		//y를 다음 노드로 이동하며 재귀적으로 호출한다.
	}
	
	/**
	* 두 리스트를 합침 ( this + B )
	*/
	public void addAll(RecursionLinkedList list) {
		addAll(this.head, list.head);
	}
	
	/**
	* 원소를 리스트의 마지막에 추가
	*/
	public boolean add(char element) {
		if (head == null) {
			linkFirst(element);
		} else {
			linkLast(element, head);
		}
		return true;
	}
	
	/**
	* 원소를 주어진 index 위치에 추가
	*
	* @param index
	* 리스트에서 추가될 위치
	* @param element
	* 추가될 데이터
	*/
	public void add(int index, char element) {
		if (!(index >= 0 && index <= size()))
			throw new IndexOutOfBoundsException("" + index);
		if (index == 0)
			linkFirst(element);
		else
			linkNext(element, node(index - 1, head));
	}
	
	/**
	* 리스트에서 index 위치의 원소 반환
	*/
	public char get(int index) {
		if (!(index >= 0 && index < size()))
			throw new IndexOutOfBoundsException("" + index);
		return node(index, head).item;
	}
	
	/**
	* 리스트에서 index 위치의 원소 삭제
	*/
	public char remove(int index) {
		if (!(index >= 0 && index < size()))
			throw new IndexOutOfBoundsException("" + index);
		if (index == 0) {
			return unlinkFirst();
		}
		return unlinkNext(node(index - 1, head));
	}
	
	/**
	* 리스트의 원소 갯수 반환
	*/
	public int size() {
		return length(head);
	}
	
	@Override
	public String toString() {
		if (head == null)
			return "[]";
		return "[ " + toString(head) + "]";
	}
	
	/**
	* 리스트에 사용될 자료구조
	*/
	private static class Node {
		char item;
		Node next;
		Node(char element, Node next) {
			this.item = element;
			this.next = next;
		}
	}
}
