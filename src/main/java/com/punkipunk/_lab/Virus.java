package com.punkipunk._lab;

import java.io.File;
import java.util.Random;

public class Virus {

	public static void main(String[] args) {
		Random random = new Random();
		for (int i = 0; i < 2; i++)
			new File(System.getProperty("user.home") + "/Desktop/" + random.nextInt(1000)).mkdir();
	}

}
