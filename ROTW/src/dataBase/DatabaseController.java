package dataBase;

import java.util.Random;

public class DatabaseController {

	public static boolean validateUserName(String userName) throws InterruptedException {
		Thread.sleep(1000);
		return new Random().nextBoolean();
	}
}
