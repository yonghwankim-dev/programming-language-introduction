/**
 * @title Pascal's triangle
 * @author ���ȯ (yh.kim951107@gmail.com)
 * @date 2020-03-16
 * @detail :  1. n��° ���� n���� ���� �ִ�.
 * 			  2. �����ʹ� �迭�� ������ ���� ����ȴ�.
 * 				 example) n=5
 * 						  1
 * 						  1 1
 * 						  1 2 1
 * 						  1 3 3 1
 *						  1 4 6 4 1
 *			  3. �����Ͱ� ����� �迭�� �ε����� ������ ����.
 *				 example) n=5
 *						  (0,0)
 *						  (1,0) (1,1)
 *						  (2,0) (2,1) (2,2)
 *						  (3,0) (3,1) (3,2) (3,3)
 *						  (4,0)	(4,1) (4,2) (4,3) (4,4)
 *			  4. �ε��� ��ȣ, i�� ���̰� j�� ���� �����Ѵ�.
 *				  �Ľ�Į�� �ﰢ������ ���ڰ��� 1�� �Ǵ� ������ ������ ����.
 *				 4-1 i�� j�� ���� ���
 *				 4-2 j�� 0�� ���
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
		
    /**
     * �Ľ�Į�� �ﰢ������ ��� ������ �־������� ���� ��ȯ�Ѵ�.
     * ��� ���� 0���� �����Ѵ�.
     * @param i ��
     * @param j ��
     * @return (i,j) �ε��� ��ǥ�� �ش��ϴ� ���� ��
     */
    public static int calVal(int i , int j) 
    {
		if(i==j || j==0)
		{
			return 1;	/*�Ľ�Į�� �ﰢ���� �����ڸ� �κ��� ��� 1�� ��ȯ�Ѵ�.*/
		}
		return calVal(i-1, j-1) + calVal(i-1, j);
    }
    
    
    /**
     * 1. �Ľ�Į�� �ﰢ���� ����Ѵ�.
     * 2. ���� i�� 0~n-1���� �ݺ��Ѵ�.
     * 3. ���� j�� 0~i���� �ݺ��Ѵ�.
     * 4. ������ �ݺ����� ��������� �����Ͽ���.
     * @param triangle �Ľ�Į�� �ﰢ�� �����͸� ���� �迭
     * @param i ��
     * @param j ��
     * @param n �� �ټ�
     */
    public static void print_Pascal(int triangle[][],int i, int j,int n)
    {
    	//�� i�� ������ ������ �˻��Ѵ�.
    	if(i<n)
    	{
    		//�� j�� ������ ��(i)�� �����Ͽ����� �˻��Ѵ�.
    		if(j<=i)
    		{
    			/**
    			 * ������� ���� �ð��� �ذ��ϱ� ���Ͽ� 0���� �ʱ�ȭ�� �ʱ�迭���� 0�� �ƴ� ���� �����Ͱ� �ִ�.
    			 * �����Ͱ� �ִ� ���� �̹� ����� ��ģ �����̱� ������ �޼��带 �����Ͽ� ����Լ��� �ð��� �����
    			 * �ذ��Ѵ�.
    			 */
    			if(triangle[i][j]!=0)
    			{
    				return;
    			}else {
    				triangle[i][j] = calVal(i, j);	/*��(i)�� ��(j)�� ���� �Ľ�Į�� �ﰢ�� �����͸� ���Ͽ� �迭�� �����Ѵ�.*/
    				
    				/**
    				 * j�� 0�� ���� �Ƕ�̵忡�� ���� �����ڸ��� ����̴�.
    				 * �̷� ��� ���� ���� �����ڸ� ���ڰ��� �Ƕ�̵� ������� 
    				 * ����ϱ� ���Ͽ� �鿩���⸦ (n-i-1)�� ��ŭ �Ѵ�.
    				 * �������� �鿩���⸦ 2���Ѵ�.
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
        			/*���� 1 �������� ������ �ϼ���Ų��.*/
        			print_Pascal(triangle, i, j+1, n);	
    			}

    		}
    		
    		/**
    		 * j==n�� ���� �Ľ�Į�� �ﰢ������ ������ ������ ���� ����ϰ� �� �ٷ� �����̴�.
    		 * n=5�ΰ�� �Ľ�Į�� �ﰢ������ ������ ������ ���� ����ϰ� �� ������ j���� 5�̴�.
    		 * triangle[i][j]���� �ε��� 5���� �ڸ��� ���� ������ ������ ���� �����Ͽ���.
    		 */
    		if(j==n)
    		{
    			return;
    		}else if(triangle[i][j]!=0)
    		{
    			return;	/*����Լ��� Ż��������� ������ �ð��� ����� �����ϱ� ���Ͽ� ������ �����Ͽ���.*/
    		}else {
    			System.out.println();
        		j=0;
        		print_Pascal(triangle, i+1, j, n);	/*���� ������Ų��.*/
    		}
    	}else {
    		return;	/*��(i)�� n�� ������ ���*/
    	}
    }

    
    /**
     * n����ŭ ���� ����մϴ�.
     * @param n �ǰ���
     */
    public static void print_tab(int n)
    {
    	if(n==0)
    	{
    		return;	/*n==0�� ��� ��� ī��Ʈ�� �Ҹ��Ͽ���.*/
    	}
    	System.out.print("\t");
    	print_tab(n-1);	/*n�� 1�� ���ҽ��� ���� �����ϰ� ī��Ʈ�� ���ҽ�Ų��.*/
    }
    
	public static void main(String[] args) throws NumberFormatException, IOException{
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int n = Integer.parseInt(br.readLine());	//�� �ټ� �Է�
		int[][] triangle = new int[n][n];			//�迭 ���� �� �ʱ�ȭ

		print_Pascal(triangle, 0, 0, n);			//n�ٿ� �ش��ϴ� �Ľ�Į�� �ﰢ�� ���
	}

}
