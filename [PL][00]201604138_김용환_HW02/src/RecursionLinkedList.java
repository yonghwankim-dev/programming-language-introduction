
public class RecursionLinkedList {
	private Node head;
	private static char UNDEF = Character.MIN_VALUE;
	
	/**
	* ���Ӱ� ������ ��带 ����Ʈ�� ó������ ����
	*/
	private void linkFirst(char element) {
		head = new Node(element, head);
	}
	
	/**
	* ���� (1) �־��� Node x�� ���������� ����� Node�� �������� ���Ӱ� ������ ��带 ����
	*
	* @param element
	* ������
	* @param x
	* ���
	*/
	private void linkLast(char element, Node x) {
		/*x��尡 ������ ������� �˻��Ѵ�.*/
		if(x.next==null)
		{
			x.next = new Node(element,null);	//����Ʈ�� ������ ��忡 ���ο� ��带 �����Ѵ�.
			return;
		}
		linkLast(element, x.next);				//��� x�� ������ ��尡 �ƴѰ�� ���� ���� �̵��Ѵ�.
	}
	
	/**
	* ���� Node�� ���� Node�� ���Ӱ� ������ ��带 ����
	*
	* @param element
	* ����
	* @param pred
	* �������
	*/
	private void linkNext(char element, Node pred) {
		Node next = pred.next;
		pred.next = new Node(element, next);
	}
	
	/**
	* ����Ʈ�� ù��° ���� ����(����)
	*
	* @return ù��° ������ ������
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
	* ����Node�� ���� Node���� ����(����)
	*
	* @param pred
	* �������
	* @return ��������� ������
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
	 * ���� (2) x��忡�� index��ŭ ������ Node ��ȯ
	 * @param index
	 * �����ϰ��� �ϴ� index ��ġ
	 * @param x
	 * �����ϰ��� �ϴ� ���
	 * @return
	 */
	private Node node(int index, Node x) {
		// ä���� ���, recursion ���
		
		//index�� 0�ΰ��� �ش� index��ŭ �Ÿ��� �̵��ߴٴ� ���̴�.
		if(index==0) 
		{
			return x;	
		}
		/**
		 * 1. index-1 : index�� ī��Ʈ ���ҷν� 1�� ����Ų��.
		 * 2. x.next  : ���� ���� �̵��Ѵ�.
		 */
		return node(index-1,x.next);	
	}
	
	/**
	 * ���� (3) ���κ��� �������� ����Ʈ�� ��� ���� ��ȯ
	 * next ��带 ���Ͽ� ���� ���� �̵��ϸ鼭 ���̸� 1�� ���Ѵ�.
	 * @param x	���̸� ���ϰ��� �ϴ� ���
	 * @return	����� ����� ����
	 */
	private int length(Node x) {
	// ä���� ���, recursion ���
		
		//x�� null�� ���� ������ ����� �������� �̵��� ���̴�.
		if(x==null)
		{
			return 0;
		}
		return length(x.next)+1;	//��������� ȣ���Ͽ� 1�� ���Ѵ�.
	}
	
	/**
	 * ���� (4) ���κ��� �����ϴ� ����Ʈ�� ���� ��ȯ
	 * @param x ����ϰ��� �ϴ� ���
	 * @return	���� ����� ��ҵ��� ������ ���ڿ��� ������ ���ڿ�
	 */
	private String toString(Node x) {
	// ä���� ���, recursion ���
		//x�� null�� ���� ������ ����� �������� �̵��� ���̴�.
		if(x==null)
		{
			return "";	//�� ���ڿ��� ��ȯ�Ѵ�.
		}
		return x.item +" "+ toString(x.next);	//�ش� ����� ��ҿ� ���� ���ڿ��� �ٿ��� ��������� ȣ���Ѵ�.
	}
	
	/**
	* �߰� ���� (5) ���� ����� ���� ������ ����Ʈ�� �������� �Ųٷ� ����
	* ex)��尡 [s]->[t]->[r]�� ��, reverse ���� �� [r]->[t]->[s]
	* @param x
	* ���� ���
	* @param pred
	* �������� ���� ���
	*/
	private void reverse(Node x, Node pred) {
	// ä���� ���, recursion ���
		
		//x==null�ΰ��� ������ ��带 �������� �̵���Ų ����̴�.
		if(x==null)
		{
			/**
			 * 1.���� ù��° ��带 ����Ű�� head�� ������ ���� ����Ű�� �Ѵ�.
			 * 2.���� x�� ������ ����� �������� �̵��Ͽ��� ������ ������ ���� pred ����̴�.
			 * 3.head�� pred ��带 ����Ű�� �Ͽ� ����Ʈ�� ������ ��带 ����Ű�� �Ѵ�.
			 */
			head = pred;	
			return;
		}
		Node temp = x.next;	//������� ȣ���� ���Ͽ� ��� x�� ���� ��带 �ӽ������Ѵ�.
		
		/**
		 * 1. x.next�� null�ΰ��� x�� ������ ��忡 ������ ����̴�.
		 * 2. pred�� null�� ���� x�� ù��° ��忡 ������ ����̴�.
		 */
		if(x.next==null || pred==null)
		{
			x.next = pred;
		}
		
		//pred�� null�� �ƴ� ���� x�� ù��° ��尡 �ƴ� ����̴�.
		if(pred!=null) {
			x.next = pred;	//����Ʈ�� ������ �ٲ۴�.
		}

		reverse(temp,x);	//x��带 ���� ���� �̵���Ű�� pred ��带 ���� x���� �����Ͽ� ��������� ȣ���Ѵ�.
	}
	
	/**
	* ����Ʈ�� �Ųٷ� ����
	*/
	public void reverse() {
		reverse(head, null);
	}
	
	/**
	* �߰� ���� (6) �� ����Ʈ�� ��ħ ( A + B )
	* ex ) list1 =[l]->[o]->[v]->[e] , list2=[p]->[l] �� ��,
	* list1.addAll(list2) ���� �� [l]->[o]->[v]->[e]-> [p]->[l] 
	* @param x
	* list1�� ���
	* @param y �����ϰ��� �ϴ� ���
	* list2 �� head
	*/
	private void addAll(Node x, Node y) {
		//y==null�ΰ��� y�� ����������� �������� �̵��� ����̴�.
		if(y==null)
		{
			return;	//�޼��� ����
		}
		linkLast(y.item, x);	//x����� ������ ��� ������ y ����� ��Ҹ� �����Ͽ� �����Ѵ�.
		addAll(x, y.next);		//y�� ���� ���� �̵��ϸ� ��������� ȣ���Ѵ�.
	}
	
	/**
	* �� ����Ʈ�� ��ħ ( this + B )
	*/
	public void addAll(RecursionLinkedList list) {
		addAll(this.head, list.head);
	}
	
	/**
	* ���Ҹ� ����Ʈ�� �������� �߰�
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
	* ���Ҹ� �־��� index ��ġ�� �߰�
	*
	* @param index
	* ����Ʈ���� �߰��� ��ġ
	* @param element
	* �߰��� ������
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
	* ����Ʈ���� index ��ġ�� ���� ��ȯ
	*/
	public char get(int index) {
		if (!(index >= 0 && index < size()))
			throw new IndexOutOfBoundsException("" + index);
		return node(index, head).item;
	}
	
	/**
	* ����Ʈ���� index ��ġ�� ���� ����
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
	* ����Ʈ�� ���� ���� ��ȯ
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
	* ����Ʈ�� ���� �ڷᱸ��
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
