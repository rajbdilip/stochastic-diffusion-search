package com.diliprajbaral;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class SDS {

    private static final int NUM_OF_AGENTS = 5;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/haystack.txt"));
        String haystack = reader.readLine();

        String needle = "hello";

        int haystackLength = haystack.length();
        int needleLength = needle.length();

        // Position in haystack (hypothesis) pointed by agents
        HashMap<Integer, Integer> agentsHypothesis = new HashMap<>(NUM_OF_AGENTS);

        // Status of agents: either Active or Inactive
        HashMap<Integer, Boolean> agentsStatus = new HashMap<>(NUM_OF_AGENTS);

		// Allotted positions (hypotheses)
        NonRepeatingRandomNumber positions = new NonRepeatingRandomNumber(haystackLength - needleLength);


        /* ===========================
                INITIALIZE
           =========================== */

        // NonRepeatingRandomNumber pick up hypothesis (position) randomly
        System.out.println("INITIALIZATION:\n");
        for (int i = 0; i < NUM_OF_AGENTS; i++) {
            int rand = positions.getNextRandomNumber();

            agentsHypothesis.put(i, rand);
            System.out.println("Agent " + i + " -> " + rand);
        }

        // Iteration count for termination
        int count = 0;

		// Main loop
        while (!allAgentsActive(agentsStatus)) {
            /* ===========================
                    TESTING
               =========================== */
            System.out.println("\nTEST:\n");
            for (int i = 0; i < NUM_OF_AGENTS; i++) {
                // Select random position of letter
                int letterPosition = ThreadLocalRandom.current().nextInt(0, needleLength);

                char char1 = haystack.charAt(agentsHypothesis.get(i) + letterPosition);
                char char2 = needle.charAt(letterPosition);

                if (char1 == char2) {
                    agentsStatus.put(i, true);
                    System.out.println("Agent " + i + " : Active");
                } else {
                    agentsStatus.put(i, false);
                    System.out.println("Agent " + i + " : Inactive");
                }
            }

            /* ===========================
                    DIFFUSION
               =========================== */
            System.out.println("\nDIFFUSION:\n");

            // Uses passive method
            NonRepeatingRandomNumber nonRepeatingRandomNumber = new NonRepeatingRandomNumber(NUM_OF_AGENTS);
            for (int i = 0; i < NUM_OF_AGENTS; i++) {
                // Inactive nonRepeatingRandomNumber choose another active agent and adopts it's hypotheses
                // or generates new hypothesis if the selected agent is inactive

                if (!agentsStatus.get(i)) {     // Inactive agent
                    // Choose another agent
                    int newAgent = nonRepeatingRandomNumber.getNextRandomNumber(i);

                    if (agentsStatus.get(newAgent)) {
                        // Adopt new active nonRepeatingRandomNumber hypothesis
                        agentsHypothesis.put(i, agentsHypothesis.get(newAgent));
                        System.out.println("Agent " + i + " adopts Agent " + newAgent);
                    } else {
                        // Selected agent is inactive; Generated new hypothesis
                        int rand = positions.getNextRandomNumber();
                        agentsHypothesis.put(i, rand);
                        System.out.println("Agent " + i + " -> " + rand + " (new)");
                    }
                }
            }

            if (++count > haystackLength) {
                System.out.println("Search aborted!");
                return;
            }
        }

        // Output active agents hypothesis
        System.out.println("Matched positions:");
        for (int i = 0; i < NUM_OF_AGENTS; i++) {
            if (agentsStatus.get(i)) {
                System.out.println("Agent " + i + " -> " + agentsHypothesis.get(i));
            }
        }

        System.out.println("Number of iterations: " + count);
    }

    public static Boolean allAgentsActive(HashMap<Integer, Boolean> agentsStatus) {
        for (int i = 0; i < NUM_OF_AGENTS; i++) {
            if (!agentsStatus.containsKey(i)) {
                return false;
            } else {
                if (!agentsStatus.get(i)) {
                    return false;
                }
            }
        }

        return true;
    }

}
