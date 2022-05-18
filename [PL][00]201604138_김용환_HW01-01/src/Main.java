

/**
 * @title Sierpinski gasket
 * @author 김용환 (yh.kim951107@gmail.com)
 * @date 2020-03-16
 * @detail : 1. 한변의 길이가 "l"인 정삼각형에서 Fractal 구조로 진행된다. 
 * 			 2. 존재하는 삼각형의 각 변의 중점을 기준으로 또 다른 삼각형을 생성한다.
 * 			 3. 생성한 작은 삼각형을 제거한다.
 * 			 4. 남은 삼각형에 대해서 단계 2를 반복한다.
 * 			 5. 숫자를 입력받았을 때 존재하는 삼각형의 모든 변의 합을 출력한다. 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	/**
	 * 1.정삼각형의 길이를 1L이라 가정한다.
	 * 2.정삼각형의 각 변의 중점을 기준으로 Fractal 구조로 또 다른 작은 삼각형을 만든다.
	 * 3.남은 정삼각형에 대해서 2를 반복한다.
	 * S1 = (1/1)*3
	 * S2 = (3/2)*3
	 * S3 = (9/4)*3
	 * S4 = (27/8)*3
	 * 과 같이 분자는 3^(n-1)만큼 증가한다. 분모는 2^(n-1)만큼 증가한다.
	 * Fractal 구조로 생성한 작은 삼각형의 세변의 길이의 합은 (3/2)^(n-1)L이다.
	 * 따라서 남아있는 삼각형의 길이의 합의 공식은 Sn = (3/2)^(n-1)*3이 된다.
	 * 
	 * 매개변수 n은 1보다 같거나 크다고 가정한다.
	 * 매개변수 n을 1씩 감소시켜 분자(mlclr)와 분모(dnmnt)를 재귀적으로 제곱하여 증가시킨다.
	 * 매개변수 r은 0(n-1의 역할)부터 시작하여 1씩 증가하여 분자와 분모를 제곱시킨다.
	 * 
	 * @param n		카운트
	 * @param mlclr 분자
	 * @param dnmnt 분모
	 * @param r		지수값
	 * @return 		삼각형들의 모든 변의 합
	 */
	public static String gasket(int n, long mlclr, long dnmnt, int r) {
		
		if(n==0)
		{
			return ("(" + mlclr*3 + "/" + dnmnt + ")L"); 
		}else {
			mlclr = (long) Math.pow(3, r);
			dnmnt = (long) Math.pow(2, r);
			r++;
			return gasket(n-1, mlclr, dnmnt,r);	//n을 1감소시키고 재귀한다.
		}
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int n;		//항넘버
		long mlclr;	//분자
		long dnmnt;	//분모
		
		System.out.print("input : ");
		n = Integer.parseInt(br.readLine());	// 항넘버 입력 
		dnmnt = 2;								
		mlclr = 3;								
		
		String ans = "S" + n + " = " + gasket(n,mlclr,dnmnt,0);	//결과
		System.out.println(ans);
	}

}
