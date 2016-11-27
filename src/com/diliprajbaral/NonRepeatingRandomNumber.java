package com.diliprajbaral;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

class NonRepeatingRandomNumber {
	private ArrayList<Integer> numbers;

	NonRepeatingRandomNumber(int max) {
		numbers = new ArrayList<>(max);
		for (int i = 0; i < max; i++) {
			numbers.add(i);
		}
	}

	NonRepeatingRandomNumber(int min, int max) {
		numbers = new ArrayList<>(max - min + 1);
		for (int i = min; i <= max; i++) {
			numbers.add(i);
		}
	}

	public int getNextRandomNumber() {
		if (numers.size() < 1) {
			return Integer.MIN_VALUE;
		} else if (numbers.size() == 1) {
			return numbers.get(0);
		}

		int randomIndex = ThreadLocalRandom.current().nextInt(0, numbers.size());
		int newRandomNumber = numbers.get(randomIndex);
		numbers.remove(randomIndex);
		return newRandomNumber;
	}

	public int getNextRandomNumber(int numberToExclude) {
		if (numbers.size() == 1) {
			return numbers.get(0);
		}

		int lastIndex = numbers.size() - 1;
		int numberToExcludeIndex = numbers.indexOf(numberToExclude);

		if (numberToExcludeIndex == -1) {
			return getNextRandomNumber();
		}

		int temp = numbers.get(lastIndex);
		numbers.set(lastIndex, numberToExclude);
		numbers.set(numberToExcludeIndex, temp);

		int index = ThreadLocalRandom.current().nextInt(0, lastIndex);
		int newRandomAgent = numbers.get(index);
		numbers.remove(index);
		return newRandomAgent;
	}

}
