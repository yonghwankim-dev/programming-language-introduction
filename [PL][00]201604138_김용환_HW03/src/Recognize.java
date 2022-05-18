import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Recognize {
	
	public enum TokenType{
		ID(3), INT(2);
		private final int finalState;
		TokenType(int finalState) {
			this.finalState = finalState;
		}
	}
	public static class Token {
		public final TokenType type;//��ū Ÿ��
		public final String lexme;	//��ū�� ����
		
		Token(TokenType type, String lexme) {
			this.type = type;
			this.lexme = lexme;
		}
		@Override
		public String toString() {
			return String.format("[%s: %s]", type.toString(), lexme);
		}
	}
	
	public static class Scanner{
		private int transM[][];
		private String source;
		private StringTokenizer st;
		
		public Scanner(String source) {
			this.transM = new int[4][128];
			this.source = source == null ? "" : source;
			this.st = new StringTokenizer(this.source, " ");
			initTM();
		}
		
		/**
		 * trnasM ������ �迭�� �ʱ�ȭ�Ѵ�.
		 * �ƽ�Ű �ڵ� ����(0~9)�� ���� 2���� �ʱ�ȭ�Ѵ�.
		 * �ƽ�Ű �ڵ� ���̳ʽ�(-)�� ���� 1���� �ʱ�ȭ�Ѵ�.
		 * �ƽ�Ű �ڵ� ���ĺ�(a-zA-Z)�� 3���� �ʱ�ȭ�Ѵ�.
		 */
		private void initTM() {
			
			//transM ������ �迭�� ����,���ĺ�,"-"�� ���� �ʱ�ȭ
			for(int i=0;i<4;i++)
			{
				for(int j=0;j<128;j++)
				{
					if(j>=48 && j<=57)	//���� �ʱ�ȭ
					{
						
						transM[i][j]=2;
					}else if(j==45) 	//���̳ʽ��� �ʱ�ȭ
					{
						
						transM[i][j]=1;
					}else if((j>=65 && j<=90) || (j>=97 && j<=122))	//���ĺ� �ʱ�ȭ
					{
						
						transM[i][j] = 3;
					}else {				//�׿��� ���
						transM[i][j] = -1;
					}
				}
			}
		}
		/**
		 * st ��ū���ڿ����� ��ū�� ������ ��ū Ÿ���� �м��Ѵ�.
		 * @return ��ū����
		 */
		private Token nextToken(){
			int stateOld = 0;
			int stateNew;
			
			//��ū�� �� �ִ��� �˻�
			if(!st.hasMoreTokens()) return null;
			
			//�� ���� ��ū�� ����
			String temp = st.nextToken();
			Token result = null;
			for(int i = 0; i<temp.length();i++){
				//���ڿ��� ���ڸ� �ϳ��� ������ ������¿� TransM�� �̿��Ͽ� �������¸� �Ǻ�
				//���� �Էµ� ������ ���°� reject �̸� �����޼��� ��� �� return��
				//���� ���� ���¸� ���� ���·� ����
				
				//��ū ���ڿ��� ���� �ϳ��� ����
				char ch = temp.charAt(i);
				
				/**
				 * ��ū�� ù��° ���ڰ� �����̸� Integer�̴�.
				 * ��ū�� ù��° ���ڰ� -�̸� Integer�̴�.
				 * ��ū�� ù��° ���ڰ� ���ĺ��̸� ID�̴�.
				 */
				if(i==0)
				{
					if(transM[stateOld][ch]==1)
					{
						stateOld = 1;
					}else if(transM[stateOld][ch]==2)
					{
						stateOld = 2;
					}else if(transM[stateOld][ch]==3)
					{
						stateOld = 3;
					}
				}else {
					stateNew = stateOld;	//i-1��° ���¸� �����ɴϴ�.
					
					/**i-1��° ���°� 1�̸� Integer�� ����
					 * i-1��° ���°� 2�̸� Integer�� ����
					 */
					if(stateNew==1 || stateNew==2)
					{
						//���� ������ ����(ch)�� ���̳ʽ� �̰ų� �����̸� ��ū�� ����-[����-���̳ʽ�]�� �����ϹǷ� Integer�� ID�� �ƴϴ�.
						if(transM[stateNew][ch]==1 || transM[stateNew][ch]==3)
						{
							System.out.println(temp + " reject");
							return null;
						}else {	//���� ������ ����(ch)���� ���ڷθ� �̷������ Integer�̴�.
							stateOld=2;	//���� ���°� Integer�� �����ȴ�.
						}
					}
					else if(stateNew==3)	//i-1��° ���°� 3�̸� ID�� ����
					{
						/**
						 * ���� ������ ����(ch)�� ���̳ʽ��̸� ����-[���̳ʽ�]�� �����ϹǷ� ID�� �ƴϴ�.
						 * ID�� ���ڷ� �����ϰ� �� ���� ���ں��ʹ� ���� �Ǵ� ���ڷ� �����ȴ�.
						 */
						if(transM[stateNew][ch]==1)
						{
							System.out.println(temp + " reject");
							return null;
						}else {
							stateOld = 3;	//���� ���°� ID�� �����ȴ�.
						}
					}
				}
			}
			for (TokenType t : TokenType.values ()){
				//������ ���°� �������� ����� ����.
				if(t.finalState == stateOld){
					result = new Token(t, temp);
					break;
				}	
			}
			return result;
		}
		/**
		 * nextToken() �޼��带 ���ؼ� ��ū�� �ϳ��� �м��ؼ� ��ū�� ����Ʈ�� �߰��Ѵ�.
		 * @return ��ū������ ����ִ� ����Ʈ
		 */
		public List<Token> tokenize() {
			//�Է����� ���� ��� token�� ����
			//nextToken()�̿��� �ĺ��� �� list�� �߰��� ��ȯ
			
			//List ����
			List<Token> list = new ArrayList<Token>();
			
			//�ݺ����� ���ؼ� nextToken()�޼��尡 null�� ���ö����� ��ū�� ������.
			while(true)
			{
				Token result = nextToken();	//��ū�� ������.
				if(result==null)
				{
					break;
				}else {	//��ū�� ���� ������ ����Ʈ�� �߰��Ѵ�.
					list.add(result);
				}
			}
			
			return list;
		}
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileReader fr = new FileReader("as03.txt");
		BufferedReader br = new BufferedReader(fr);
		String source = br.readLine();
		
		
		Scanner s = new Scanner(source);
		System.out.println("input : " + source);
		List<Token> tokens = s.tokenize();
		System.out.println(tokens);
		
	}

}
