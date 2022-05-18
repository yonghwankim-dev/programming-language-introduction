

/**
 * @title Sierpinski gasket
 * @author ���ȯ (yh.kim951107@gmail.com)
 * @date 2020-03-16
 * @detail : 1. �Ѻ��� ���̰� "l"�� ���ﰢ������ Fractal ������ ����ȴ�. 
 * 			 2. �����ϴ� �ﰢ���� �� ���� ������ �������� �� �ٸ� �ﰢ���� �����Ѵ�.
 * 			 3. ������ ���� �ﰢ���� �����Ѵ�.
 * 			 4. ���� �ﰢ���� ���ؼ� �ܰ� 2�� �ݺ��Ѵ�.
 * 			 5. ���ڸ� �Է¹޾��� �� �����ϴ� �ﰢ���� ��� ���� ���� ����Ѵ�. 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	/**
	 * 1.���ﰢ���� ���̸� 1L�̶� �����Ѵ�.
	 * 2.���ﰢ���� �� ���� ������ �������� Fractal ������ �� �ٸ� ���� �ﰢ���� �����.
	 * 3.���� ���ﰢ���� ���ؼ� 2�� �ݺ��Ѵ�.
	 * S1 = (1/1)*3
	 * S2 = (3/2)*3
	 * S3 = (9/4)*3
	 * S4 = (27/8)*3
	 * �� ���� ���ڴ� 3^(n-1)��ŭ �����Ѵ�. �и�� 2^(n-1)��ŭ �����Ѵ�.
	 * Fractal ������ ������ ���� �ﰢ���� ������ ������ ���� (3/2)^(n-1)L�̴�.
	 * ���� �����ִ� �ﰢ���� ������ ���� ������ Sn = (3/2)^(n-1)*3�� �ȴ�.
	 * 
	 * �Ű����� n�� 1���� ���ų� ũ�ٰ� �����Ѵ�.
	 * �Ű����� n�� 1�� ���ҽ��� ����(mlclr)�� �и�(dnmnt)�� ��������� �����Ͽ� ������Ų��.
	 * �Ű����� r�� 0(n-1�� ����)���� �����Ͽ� 1�� �����Ͽ� ���ڿ� �и� ������Ų��.
	 * 
	 * @param n		ī��Ʈ
	 * @param mlclr ����
	 * @param dnmnt �и�
	 * @param r		������
	 * @return 		�ﰢ������ ��� ���� ��
	 */
	public static String gasket(int n, long mlclr, long dnmnt, int r) {
		
		if(n==0)
		{
			return ("(" + mlclr*3 + "/" + dnmnt + ")L"); 
		}else {
			mlclr = (long) Math.pow(3, r);
			dnmnt = (long) Math.pow(2, r);
			r++;
			return gasket(n-1, mlclr, dnmnt,r);	//n�� 1���ҽ�Ű�� ����Ѵ�.
		}
	}
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int n;		//�׳ѹ�
		long mlclr;	//����
		long dnmnt;	//�и�
		
		System.out.print("input : ");
		n = Integer.parseInt(br.readLine());	// �׳ѹ� �Է� 
		dnmnt = 2;								
		mlclr = 3;								
		
		String ans = "S" + n + " = " + gasket(n,mlclr,dnmnt,0);	//���
		System.out.println(ans);
	}

}
