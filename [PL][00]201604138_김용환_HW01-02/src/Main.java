/**
 * @title Pascal's triangle
 * @author 김용환 (yh.kim951107@gmail.com)
 * @date 2020-03-16
 * @detail :  1. n번째 행은 n개의 수가 있다.
 * 			  2. 데이터는 배열에 다음과 같이 저장된다.
 * 				 example) n=5
 * 						  1
 * 						  1 1
 * 						  1 2 1
 * 						  1 3 3 1
 *						  1 4 6 4 1
 *			  3. 데이터가 저장된 배열의 인덱스는 다음과 같다.
 *				 example) n=5
 *						  (0,0)
 *						  (1,0) (1,1)
 *						  (2,0) (2,1) (2,2)
 *						  (3,0) (3,1) (3,2) (3,3)
 *						  (4,0)	(4,1) (4,2) (4,3) (4,4)
 *			  4. 인덱스 번호, i는 행이고 j는 열로 정의한다.
 *				  파스칼의 삼각형에서 숫자값이 1이 되는 조건은 다음과 같다.
 *				 4-1 i와 j가 같은 경우
 *				 4-2 j가 0인 경우
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
		
    /**
     * 파스칼의 삼각형에서 행과 열값이 주어졌을때 값을 반환한다.
     * 행과 열은 0부터 시작한다.
     * @param i 행
     * @param j 열
     * @return (i,j) 인덱스 좌표에 해당하는 숫자 값
     */
    public static int calVal(int i , int j) 
    {
		if(i==j || j==0)
		{
			return 1;	/*파스칼의 삼각형의 가장자리 부분인 경우 1을 반환한다.*/
		}
		return calVal(i-1, j-1) + calVal(i-1, j);
    }
    
    
    /**
     * 1. 파스칼의 삼각형을 출력한다.
     * 2. 행인 i는 0~n-1까지 반복한다.
     * 3. 열인 j는 0~i까지 반복한다.
     * 4. 기존의 반복문을 재귀적으로 구현하였다.
     * @param triangle 파스칼의 삼각형 데이터를 담을 배열
     * @param i 행
     * @param j 열
     * @param n 행 줄수
     */
    public static void print_Pascal(int triangle[][],int i, int j,int n)
    {
    	//행 i가 마지막 행인지 검사한다.
    	if(i<n)
    	{
    		//열 j가 마지막 열(i)에 도착하였는지 검사한다.
    		if(j<=i)
    		{
    			/**
    			 * 재귀적인 비용과 시간을 해결하기 위하여 0으로 초기화된 초기배열에서 0이 아닌 경우는 데이터가 있다.
    			 * 데이터가 있는 경우는 이미 출력을 마친 상태이기 때문에 메서드를 종료하여 재귀함수의 시간과 비용을
    			 * 해결한다.
    			 */
    			if(triangle[i][j]!=0)
    			{
    				return;
    			}else {
    				triangle[i][j] = calVal(i, j);	/*행(i)과 열(j)에 대한 파스칼의 삼각형 데이터를 구하여 배열에 저장한다.*/
    				
    				/**
    				 * j가 0인 경우는 피라미드에서 왼쪽 가장자리인 경우이다.
    				 * 이런 경우 제일 왼쪽 가장자리 숫자값은 피라미드 모양으로 
    				 * 출력하기 위하여 들여쓰기를 (n-i-1)번 만큼 한다.
    				 * 오른쪽은 들여쓰기를 2번한다.
    				 */
        			if(j==0)
        			{
        				print_tab(n-i-1);
        				System.out.print(triangle[i][j]);
        				print_tab(2);
        			}else {
        				System.out.print(triangle[i][j]);
        				print_tab(2);
        			}
        			/*열을 1 증가시켜 한줄을 완성시킨다.*/
        			print_Pascal(triangle, i, j+1, n);	
    			}

    		}
    		
    		/**
    		 * j==n인 경우는 파스칼의 삼각형에서 마지막 데이터 값을 출력하고 난 바로 직후이다.
    		 * n=5인경우 파스칼의 삼각형에서 마지막 데이터 값을 출력하고 난 이후의 j값은 5이다.
    		 * triangle[i][j]에서 인덱스 5번은 자리가 없기 때문에 조건을 따로 구현하였다.
    		 */
    		if(j==n)
    		{
    			return;
    		}else if(triangle[i][j]!=0)
    		{
    			return;	/*재귀함수의 탈출과정에서 나오는 시간과 비용을 절약하기 위하여 조건을 설정하였다.*/
    		}else {
    			System.out.println();
        		j=0;
        		print_Pascal(triangle, i+1, j, n);	/*행을 증가시킨다.*/
    		}
    	}else {
    		return;	/*행(i)가 n에 도달한 경우*/
    	}
    }

    
    /**
     * n번만큼 탭을 출력합니다.
     * @param n 탭갯수
     */
    public static void print_tab(int n)
    {
    	if(n==0)
    	{
    		return;	/*n==0인 경우 모든 카운트를 소모하였다.*/
    	}
    	System.out.print("\t");
    	print_tab(n-1);	/*n을 1씩 감소시켜 탭을 수행하고 카운트를 감소시킨다.*/
    }
    
	public static void main(String[] args) throws NumberFormatException, IOException{
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int n = Integer.parseInt(br.readLine());	//행 줄수 입력
		int[][] triangle = new int[n][n];			//배열 선언 및 초기화

		print_Pascal(triangle, 0, 0, n);			//n줄에 해당하는 파스칼의 삼각형 출력
	}

}
