package com.punkipunk.utils;

import java.io.*;

public final class Global {

	private Global() {
	}

	public static final int TICKS_PER_SEC = 60; // Define el numero deseado de actualizaciones (ticks) por segundo (tiempo fijo entre cada tick)
	public static final int MAX_FPS = 80;

	public static final int DEFAULT_BUFFER_SIZE = 8192;
	public static final int SERVER_PORT = 7666;
	public static final int CLIENT_PORT = 9090;

	public static final String s = File.separator;

	public static final String DIR_PROJECT = System.getProperty("user.dir");
	public static final String DIR_RESOURCES = "src" + s + "main" + s + "resources";
	public static final String DIR_DAT = "dat";
	public static final String DIR_TEXT = "texts";
	public static final String DIR_TEXTURES = "textures";

	// Nombres de archivos
	public static final String RAF = getDatDir() + "raf.dat";
	public static final String TEXT = getTextDir() + "text.txt";
	public static final String TEXT_GRANDE = getTextDir() + "Conceptos generales de protocolos.txt";
	public static final String BOLA = getTextureDir() + "bola.png";
	public static final String BOLA_AMARILLA2 = getTextureDir() + "bola_amarilla2.png";
	public static final String DRAGON = getTextureDir() + "dragon.png";
	public static final String CONFIG = getResourcesDir() + "config.properties";

	public static String getResourcesDir() {
		return DIR_PROJECT + s + DIR_RESOURCES + s;
	}

	public static String getDatDir() {
		return DIR_PROJECT + s + DIR_RESOURCES + s + DIR_DAT + s;
	}

	public static String getTextDir() {
		return DIR_PROJECT + s + DIR_RESOURCES + s + DIR_TEXT + s;
	}

	public static String getTextureDir() {
		return DIR_PROJECT + s + DIR_RESOURCES + s + DIR_TEXTURES + s;
	}

}
