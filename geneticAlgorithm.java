import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class geneticAlgorithm {
    private int item_amount;
    private int weight_limit;
    private int[] item_weight = new int[4000];
    private int[] item_value = new int[4000];
    private int population;
    private int mutation_rate;
    private int elitism;
    private int generation;
    private int stop;
    private String[] chromosome = new String[4000];

    class fitnessComparator implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            return fitness(b) - fitness(a);
        }
    }

    public geneticAlgorithm(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            this.item_amount = scanner.nextInt();
            this.weight_limit = scanner.nextInt();
            for (int i = 0; i < this.item_amount; i++) {
                this.item_value[i] = scanner.nextInt();
                this.item_weight[i] = scanner.nextInt();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public geneticAlgorithm(String filename, int population, int mutation_rate,
            int elitism, int generation, int stop) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            this.item_amount = scanner.nextInt();
            this.weight_limit = scanner.nextInt();
            for (int i = 0; i < this.item_amount; i++) {
                this.item_value[i] = scanner.nextInt();
                this.item_weight[i] = scanner.nextInt();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        this.population = population;
        this.mutation_rate = mutation_rate;
        this.elitism = elitism;
        this.generation = generation;
        this.stop = stop;
    }

    public void printInput() {
        System.out.println("");
        System.out.println("Items amount : " + item_amount);
        System.out.println("Weight limit : " + weight_limit);
        for (int i = 0; i < item_amount; i++) {
            System.out.println("Item " + (i + 1) + " => Weight: " + item_weight[i] + ", Value: " + item_value[i]);
        }
        System.out.printf("Setting => Population: %d, Mutation Rate: %d, Elitism: %d, Generations : %d\n",
                this.population, this.mutation_rate, this.elitism, this.generation);
        System.out.println("");
    }

    public String generateChromosome1() {
        String chromosome = "";
        Random rand = new Random();
        for (int i = 0; i < this.item_amount; i++) {
            chromosome += String.valueOf(rand.nextInt(2));
        }
        return chromosome;
    }

    public String generateChromosome() {
        int a = 0;
        String chromosome = "";
        for (int i = 0; i < this.item_amount; i++) {
            chromosome += "0";
        }
        Random rand = new Random();
        for (int i = 0; fitness(chromosome) >= 0 && i < this.item_amount; i++) {
            a = rand.nextInt(item_amount - 1);
            chromosome = chromosome.substring(0, a) + '1' + chromosome.substring(a + 1, item_amount);
            // System.out.println(chromosome+" "+fitness(chromosome));
        }
        chromosome = chromosome.substring(0, a) + '0' + chromosome.substring(a + 1, item_amount);
        return chromosome;
    }

    public String generateChromosome3() {
        int a = 0;
        String chromosome = "";
        for (int i = 0; i < this.item_amount; i++) {
            chromosome += "0";
        }
        Random rand = new Random();
        a = rand.nextInt(item_amount - 1);
        chromosome = chromosome.substring(0, a) + '1' + chromosome.substring(a + 1, item_amount);
        // System.out.println(chromosome+" "+fitness(chromosome));
        return chromosome;
    }

    public int fitness(String chromosome) {
        int point = 0;
        int weight = 0;
        for (int i = 0; i < this.item_amount; i++) {
            if (chromosome.charAt(i) == '1') {
                point += this.item_value[i];
                weight += this.item_weight[i];
            }
        }
        if (weight > weight_limit)
            return -point;
        return point;
    }

    public String crossover(String chromosomeA, String chromosomeB) {
        String chromosome = "";
        Random rand = new Random();
        int crossoverPoint = rand.nextInt(this.item_amount);
        chromosome += chromosomeA.substring(0, crossoverPoint + 1);
        chromosome += chromosomeB.substring(crossoverPoint + 1, this.item_amount);
        return chromosome;
    }

    public String mutation(String chromosomeA) {
        String chromosome = "";
        Random rand = new Random();
        int n;
        for (int i = 0; i < this.item_amount; i++) {
            n = rand.nextInt(this.mutation_rate);
            if (n == 0) {
                if (chromosomeA.charAt(i) == '1')
                    chromosome += '0';
                else
                    chromosome += '1';
            } else {
                chromosome += chromosomeA.charAt(i);
            }
        }
        return chromosome;
    }

    public void generateAllChromosomes() {
        for (int i = 0; i < this.population; i++) {
            chromosome[i] = generateChromosome();
        }
    }

    public void mutationAllChromosomes() {
        for (int i = elitism; i < this.population; i++) {
            chromosome[i] = mutation(chromosome[i]);
        }
    }

    public void crossoverAllChromosomes() {
        String[] copy = new String[chromosome.length];
        Random rand = new Random();
        for (int i = 0; i < elitism; i++) {
            copy[i] = chromosome[i];
        }
        for (int i = elitism; i < this.population; i++) {
            int a = 0;
            int b = 0;
            int rank = rand.nextInt(100);
            if (rank < 9) {
                a = rand.nextInt(this.population * 1 / 4 - 1) + this.population * 3 / 4;
            } else if (rank < 22) {
                a = rand.nextInt(this.population * 1 / 4 - 1) + this.population * 2 / 4;
            } else if (rank < 45) {
                a = rand.nextInt(this.population * 1 / 4 - 1) + this.population * 1 / 4;
            } else {
                a = rand.nextInt(this.population * 1 / 4 - 1);
            }
            rank = rand.nextInt(100);
            if (rank < 9) {
                b = rand.nextInt(this.population * 1 / 4 - 1) + this.population * 3 / 4;
            } else if (rank < 22) {
                b = rand.nextInt(this.population * 1 / 4 - 1) + this.population * 2 / 4;
            } else if (rank < 45) {
                b = rand.nextInt(this.population * 1 / 4 - 1) + this.population * 1 / 4;
            } else {
                b = rand.nextInt(this.population * 1 / 4 - 1);
            }

            copy[i] = crossover(chromosome[a], chromosome[b]);
        }
        chromosome = copy.clone();
    }

    public void sortByFitness() {
        Arrays.sort(chromosome, 0, population, new fitnessComparator());
    }

    public String findKnapsack() {
        String maxString = "";
        int count = 0;
        generateAllChromosomes();
        // printChromosome(0);
        if (generation == 0) {
            for (int i = 0; true; i++) {
                sortByFitness();

                if (chromosome[0] == maxString) {
                    count++;
                } else
                    count = 0;

                if (count > this.stop) {
                    printAnswer(i + 1, fitness(maxString));
                    return maxString;
                }

                maxString = chromosome[0];
                float percent = getPercent(maxString);

                // printChromosome(i + 1);
                System.out.println("Best of Generation " + (i + 1) + " : " + fitness(maxString) + " in "
                        + String.format("%.2f", percent) + "% of population");
                crossoverAllChromosomes();
                mutationAllChromosomes();
            }
        } else
            for (int i = 0; i < this.generation; i++) {
                sortByFitness();
                if (chromosome[0] == maxString) {
                    count++;
                } else
                    count = 0;
                if (count > this.stop) {
                    printAnswer(i + 1, fitness(maxString));
                    return maxString;
                }
                maxString = chromosome[0];
                float percent = getPercent(maxString);
                // printChromosome(i + 1);
                System.out.println("Best of Generation " + (i + 1) + " : " + fitness(maxString)+" in " + String.format("%.2f",percent) + "% of population");
                crossoverAllChromosomes();
                mutationAllChromosomes();
            }
        printAnswer(generation, fitness(maxString));
        return maxString;
    }

    public void printChromosome(int gen) {
        System.out.println("\nGeneration: " + gen);
        for (int i = 0; i < this.population; i++) {
            System.out.println(chromosome[i] + " " + fitness(chromosome[i]));
        }
    }

    public void printAnswer(int gen, int answer) {
        System.out.println(
                "\nAfter " + gen + " generations, best value is : " + answer );
    }

    public float getPercent(String maxString) {
        int count = 0;
        for (int i = 0; i < population; i++) {
            if (maxString.equals(chromosome[i])) {
                count++;
            }
        }
        return (((float)count/ (float)population)*100);
    }

    public static void main(String[] args) {
        geneticAlgorithm ga = new geneticAlgorithm("testcase3.txt", 50, 750, 3, 0, 10000);
        ga.printInput();
        long start, end;
        start = System.nanoTime();
        String output = ga.findKnapsack();
        System.out.print("item selected: ");
        for (int i = 0; i < output.length(); i++) {
            if (output.charAt(i) == '1') {
                System.out.print((i + 1) + ",");
            }
        }
        end = System.nanoTime();
        System.out.println("\nUse " + ((float)(end - start) / 1000000000) + " seconds " + "\n");
    }

}
