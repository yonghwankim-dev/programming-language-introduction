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
		public final TokenType type;//토큰 타입
		public final String lexme;	//토큰의 내용
		
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
		 * trnasM 이차원 배열을 초기화한다.
		 * 아스키 코드 숫자(0~9)는 상태 2으로 초기화한다.
		 * 아스키 코드 마이너스(-)는 상태 1으로 초기화한다.
		 * 아스키 코드 알파벳(a-zA-Z)은 3으로 초기화한다.
		 */
		private void initTM() {
			
			//transM 이차원 배열에 숫자,알파벳,"-"에 상태 초기화
			for(int i=0;i<4;i++)
			{
				for(int j=0;j<128;j++)
				{
					if(j>=48 && j<=57)	//숫자 초기화
					{
						
						transM[i][j]=2;
					}else if(j==45) 	//마이너스바 초기화
					{
						
						transM[i][j]=1;
					}else if((j>=65 && j<=90) || (j>=97 && j<=122))	//알파벳 초기화
					{
						
						transM[i][j] = 3;
					}else {				//그외의 경우
						transM[i][j] = -1;
					}
				}
			}
		}
		/**
		 * st 토큰문자열에서 토큰을 꺼내서 토큰 타입을 분석한다.
		 * @return 토큰정보
		 */
		private Token nextToken(){
			int stateOld = 0;
			int stateNew;
			
			//토큰이 더 있는지 검사
			if(!st.hasMoreTokens()) return null;
			
			//그 다음 토큰을 받음
			String temp = st.nextToken();
			Token result = null;
			for(int i = 0; i<temp.length();i++){
				//문자열의 문자를 하나씩 가져와 현재상태와 TransM를 이용하여 다음상태를 판별
				//만약 입력된 문자의 상태가 reject 이면 에러메세지 출력 후 return함
				//새로 얻은 상태를 현재 상태로 저장
				
				//토큰 문자열중 문자 하나를 추출
				char ch = temp.charAt(i);
				
				/**
				 * 토큰의 첫번째 문자가 숫자이면 Integer이다.
				 * 토큰의 첫번째 문자가 -이면 Integer이다.
				 * 토큰의 첫번째 문자가 알파벳이면 ID이다.
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
					stateNew = stateOld;	//i-1번째 상태를 가져옵니다.
					
					/**i-1번째 상태가 1이면 Integer로 시작
					 * i-1번째 상태가 2이면 Integer로 시작
					 */
					if(stateNew==1 || stateNew==2)
					{
						//현재 참조한 문자(ch)가 마이너스 이거나 문자이면 토큰이 숫자-[문자-마이너스]로 시작하므로 Integer도 ID도 아니다.
						if(transM[stateNew][ch]==1 || transM[stateNew][ch]==3)
						{
							System.out.println(temp + " reject");
							return null;
						}else {	//현재 참조한 문자(ch)들이 숫자로만 이루어지면 Integer이다.
							stateOld=2;	//현재 상태가 Integer로 설정된다.
						}
					}
					else if(stateNew==3)	//i-1번째 상태가 3이면 ID로 시작
					{
						/**
						 * 현재 참조한 문자(ch)가 마이너스이면 문자-[마이너스]로 시작하므로 ID가 아니다.
						 * ID는 문자로 시작하고 그 다음 문자부터는 문자 또는 숫자로 구성된다.
						 */
						if(transM[stateNew][ch]==1)
						{
							System.out.println(temp + " reject");
							return null;
						}else {
							stateOld = 3;	//현재 상태가 ID로 설정된다.
						}
					}
				}
			}
			for (TokenType t : TokenType.values ()){
				//마지막 상태가 무엇인지 물어보고 있음.
				if(t.finalState == stateOld){
					result = new Token(t, temp);
					break;
				}	
			}
			return result;
		}
		/**
		 * nextToken() 메서드를 통해서 토큰을 하나씩 분석해서 토큰을 리스트에 추가한다.
		 * @return 토큰정보가 들어있는 리스트
		 */
		public List<Token> tokenize() {
			//입력으로 들어온 모든 token에 대해
			//nextToken()이용해 식별한 후 list에 추가해 반환
			
			//List 선언
			List<Token> list = new ArrayList<Token>();
			
			//반복문을 통해서 nextToken()메서드가 null이 나올때까지 토큰을 꺼낸다.
			while(true)
			{
				Token result = nextToken();	//토큰을 꺼낸다.
				if(result==null)
				{
					break;
				}else {	//토큰에 대한 정보를 리스트에 추가한다.
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
