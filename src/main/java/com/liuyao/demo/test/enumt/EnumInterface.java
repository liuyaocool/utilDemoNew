package com.liuyao.demo.test.enumt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EnumInterface implements Food{

	public static void main(String[] args) throws IllegalAccessException, InstantiationException {
		
		PeopleEnum.FOOD.drinkMilk();

		Food food = MainFood.MANTOU;
		System.out.println(food);
		food = MainFood.NODDLE;
		System.out.println(food);
		food = Vagetables.POTATO;
		System.out.println(food);

		System.out.println("==============");

//		Foodd[] foodds = FoodMeal.APPE.getMyValues();
//		for (Foodd foodd : foodds) {
//			System.out.println(foodd);
//
//		}

		Class a = ConcurrentHashMap.class;
		Object aa = a.newInstance();
		System.out.println(aa.getClass());
	}
}

//接口
interface Eat{
	void eatRice();
	
}
interface Drink{
	void drinkMilk();
}
enum PeopleEnum implements Eat,Drink{
	FOOD,
	WATER,
	;
	@Override
	public void drinkMilk() {
		System.out.println("喝牛奶");
	}
	@Override
	public void eatRice() {
		System.out.println("吃白米饭");
	}
}

//食物分类
interface Food{
	enum MainFood implements Food{ //分类 主食
		RICE,
		MANTOU,
		NODDLE,
		;
	}
	enum Vagetables implements Food{ // 分来 蔬菜
		POTATO,
		TOMATO,
		;
	}
}

//食物管理
enum FoodMeal{
	APPE(Foodd.Appe.class),
	MAINC(Foodd.Mainc.class),
	DESSERT(Foodd.Dessert.class),
	COFFEE(Foodd.Coffee.class),
	;
	private Foodd[] myValues;
	private FoodMeal(Class<? extends Foodd> kind){
		this.myValues = kind.getEnumConstants();
		
	}
	interface Foodd{
		enum Appe implements Foodd {
	      SALAD, SOUP, SPRING_ROLLS;
	    }
	    enum Mainc implements Foodd {
	      LASAGNE, BURRITO, PAD_THAI,
	      LENTILS, HUMMOUS, VINDALOO;
	    }
	    enum Dessert implements Foodd {
	      TIRAMISU, GELATO, BLACK_FOREST_CAKE,
	      FRUIT, CREME_CARAMEL;
	    }
	    enum Coffee implements Foodd {
	      BLACK_COFFEE, DECAF_COFFEE, ESPRESSO,
	      LATTE, CAPPUCCINO, TEA, HERB_TEA;
	    }
	}
	
	public Foodd[] getMyValues() {
		return myValues;
	}
	public void setMyValues(Foodd[] myValues) {
		this.myValues = myValues;
	}
}