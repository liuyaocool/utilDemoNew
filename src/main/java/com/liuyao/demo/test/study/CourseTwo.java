package com.liuyao.demo.test.study;

;

//��̬������Class.forName("���ȫ��")
//����ʱ���ص����Ǿ�̬������(new ����),����ʱ��̬...
public class CourseTwo {

	public static void main(String[] args) {
		try {
			//��̬������,������ʱ�̼���
			Class c = Class.forName(args[0]);
			//��������,ͨ��������
			/*����ִ���,���
			 * Word word = (Word)c.newInstance();
			 * �������Ķ�����Excel�ͻ���ִ���
			 * ����ͨ���ӿڽ��,ֻ��ʵ��OfficeAble�ӿڼ���
			 * */
			OfficeAble ob = (OfficeAble)c.newInstance();
			ob.start();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
