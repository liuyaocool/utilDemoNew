package com.liuyao.demo.test.enumt;

public class EnumBasic {

	public static void main(String[] args) {
		
		System.out.println(DayEnum.FRIDAY);
		System.out.println(DayEnum.MONDAY.getClass().toString());
		
		System.out.println(DayEnum2.SATURDAY.getName());
		System.out.println(DayEnum2.MONDAY);
		
		System.out.println(AbstractEnum.FIRST.getInfo());
		
		printDay(DayEnum.FRIDAY);
	}
	
	static void printDay(DayEnum dayEnum){
		switch (dayEnum) {
		case MONDAY:
			System.out.println("星期一");
			break;
		case TUESDAY:
			System.out.println("星期二");
			break;
		case WEDNESDAY:
			System.out.println("星期三");
			break;

		default:
			System.out.println("其他");
			break;
		}
	}

}

enum DayEnum{
	MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY, SUNDAY,
    ;
}

//为枚举添加属性
enum DayEnum2{
	MONDAY,//构造方法
    TUESDAY("星期二") {
		public String getInfo() {
			return "自定义星期二方法";
		}
	},//构造方法
    WEDNESDAY("星期三"),THURSDAY("星期四"),FRIDAY("星期五"),SATURDAY("星期六"),SUNDAY("星期日"),
    ;
	
	private String name;

	public String getName() { return name; }
	public void setName(String name) { this.name = name;	}
	private DayEnum2(String name){	this.name = name;	}
	private DayEnum2(){}
}

//抽象方法
enum AbstractEnum{
	
	FIRST{
		@Override
		public String getInfo() {
			return "第一个";
		}
	},
	SECOND("第二个"){
		@Override
		public String getInfo() {
			return "第二个";
		}
	},
	;
	
	private String name;
	private AbstractEnum(String name) {	this.name = name; }
	private AbstractEnum() {}
	public String getName() {	return name; }
	public void setName(String name) {	this.name = name;	}
	public abstract String getInfo();
	
}

