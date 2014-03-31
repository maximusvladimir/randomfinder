package com.maximusvladimir.randomfinder;

import java.util.Random;

public class Searcher {
	private Random pRand;
	private double pProgress;
	private String pStr;
	private boolean pCaseMatters;
	private long pResultSeed;
	private boolean pFinished;
	private static final int fHalfLong = Integer.MAX_VALUE / 2;
	private static final double fInvLong = 1.0 / Integer.MAX_VALUE;
	private boolean halt;
	public Searcher() {
		this(null);
	}
	
	public Searcher(String str) {
		this(str, true);
	}
	
	public Searcher(String str, boolean caseMatters) {
		pStr = str;
		pCaseMatters = caseMatters;
		pRand = new Random();
	}
	
	public void setString(String str) {
		pStr = str;
		validateString();
	}
	
	public String getString() {
		return pStr;
	}
	
	public boolean doesCaseMatter() {
		return pCaseMatters;
	}
	
	public void setCaseMatters(boolean matters) {
		pCaseMatters = matters;
	}
	
	private void setProgress(double d) {
		pProgress = d;
	}
	
	public double getProgress() {
		return pProgress;
	}
	
	public long getResultSeed() {
		return pResultSeed;
	}
	
	public boolean isFinished() {
		return pFinished;
	}
	
	public void halt() {
		halt = true;
	}
	
	private void validateString() {
		if (getString() == null || getString().length() == 0)
			throw new IllegalArgumentException("String must be greater than 0 char(s) and non null.");
	}
	
	public void startAsync() {
		validateString(); // Crash on current thread, not on next.
		Thread thread = new Thread(new Runnable() {
			public void run() {
				start();
			}
		});
		thread.start();
	}
	
	public void start() {
		validateString();
		pResultSeed = 0; // Reset seed
		pFinished = false; // Reset results
		halt = false;
		int ff = 0;
		final int strLength = pStr.length();
		int[] str = new int[strLength]; // build an int array so processing is faster.
		for (int i = 0; i < strLength; i++) {
			str[i] = pStr.charAt(i);
		}
		for (int i = Integer.MIN_VALUE+1; i < Integer.MAX_VALUE-1; i++) {
			pRand.setSeed(i);
			ff = 0;
			for (int j = 0; j < strLength; j++) {
				if (!validChar(str[j], pRand.nextInt(96)+32)) {
					break;
				}
				else {
					ff++;
				}
			}
			if (ff == pStr.length()) {
				pResultSeed = i;
				pFinished = true;
				return;
			}
			if (i % 20 == 0) {
				setProgress((i * 0.5 + fHalfLong) * fInvLong);
				if (halt)
					return;
			}
		}
		pFinished = true;
		pResultSeed = Integer.MIN_VALUE;
	}
	
	private boolean validChar(int c, int num) {
		if (doesCaseMatter())
			return c == num;
		else
			return c == num || Character.toLowerCase(c) == num || Character.toUpperCase(c) == num;
	}
}
