package ipleiria.estg.dei.ei.pi.utils;

import ipleiria.estg.dei.ei.pi.model.geneticAlgorithm.Individual;

import java.util.Comparator;

public class FitnessComparator implements Comparator<Individual>{
        // Custom comparator to compare chromosome fitness. Configured to sort in descending order by default instead of ascending.
        @Override
        public int compare(Individual o1, Individual o2) {
            if (o1.getFitness() < o2.getFitness()){
                return 1;
            }

            if (o1.getFitness() > o2.getFitness()){
                return -1;
            }
            return 0;
        }
    }

