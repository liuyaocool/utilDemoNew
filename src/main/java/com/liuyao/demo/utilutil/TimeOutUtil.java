package com.liuyao.demo.utilutil;

import java.util.concurrent.*;

public class TimeOutUtil {

	private static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public static void main(String[] args) {

		try {
			String result = timeoutMethod(10);
			System.out.println(result);
			System.out.println("aaaa");
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 有超时时间的方法
	 * @param timeout 秒
	 * @return
	 */
	private static String timeoutMethod(int timeout) {
		String result;
		FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {

			@Override
			public String call() throws Exception {
				unknowMethod();
				return "方法结束。";
			}
		});

		executorService.execute(futureTask);
		try {
			result = futureTask.get(timeout*1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			//e.printStackTrace();
			futureTask.cancel(true);
			result = "默认";
		}

		return result;
	}

	private static void unknowMethod() throws InterruptedException {
		int aa = 0;
		while (true){
			aa++;
			Thread.sleep(1000);
			System.out.println(aa);
		}
	}
}
