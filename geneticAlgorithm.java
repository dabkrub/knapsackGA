import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class geneticAlgorithm {
    private int item_amount;
    private int weight_limit;
    private int[] item_weight = new int[500];
    private int[] item_value = new int[500];;
    private int population;
    private int mutation_rate;
    private int elitism;
    private int generation;

    public geneticAlgorithm(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            this.item_amount = scanner.nextInt();
            this.weight_limit = scanner.nextInt();
            for (int i = 0; i < this.item_amount; i++) {
                this.item_weight[i] = scanner.nextInt();
                this.item_value[i] = scanner.nextInt();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public geneticAlgorithm(String filename, int population, int mutation_rate, int elitism,int generation) {
        try{
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            this.item_amount = scanner.nextInt();
            this.weight_limit = scanner.nextInt();
            for(int i=0; i<this.item_amount; i++)
            {
                this.item_weight[i] = scanner.nextInt();
                this.item_value[i] = scanner.nextInt();
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        this.population = population;
        this.mutation_rate = mutation_rate;
        this.elitism = elitism;
        this.generation = generation;
    }

    public void printInput(){
        System.out.println("");
        System.out.println("Items amount : "+item_amount);
        System.out.println("Weight limit : "+weight_limit);
        for(int i=0;i<item_amount;i++)
        {
            System.out.println("Item "+(i+1)+" => Weight: "+item_weight[i]+", Value: "+item_value[i]);
        }
        System.out.printf("Setting => Population: %d, Mutation Rate: %d, Elitism: %d, Generations : %d\n",this.population,this.mutation_rate,this.elitism,this.generation);
        System.out.println("");
    }

    public String generateChromosome(){
        String chromosome = "";
        Random rand = new Random();
        for(int i=0;i<this.population;i++){
            chromosome+=String.valueOf(rand.nextInt(2));
        }
        return chromosome;
    }

    public int fitness(String chromosome){
        int point = 0;
        int weight = 0;
        for(int i=0;i<this.item_amount;i++)
        {
            if(chromosome.charAt(i)=='1')
            {
                point+=this.item_value[i];
                weight+=this.item_weight[i];
            }
            if(weight>this.weight_limit)
            {
                return 0;
            }
        }
        return point;
    }

    public String crossover(String chromosomeA,String chromosomeB)
    {
        String chromosome = "";
        Random rand = new Random();
        int crossoverPoint = rand.nextInt(this.item_amount);
        chromosome+=chromosomeA.substring(0, crossoverPoint+1);
        chromosome+=chromosomeB.substring(crossoverPoint+1, this.item_amount);
        return chromosome;
    }
    
    public String mutation(String chromosomeA)
    {
        String chromosome = "";
        Random rand = new Random();
        int n;
        for(int i=0;i<this.item_amount;i++)
        {
            n = rand.nextInt(this.mutation_rate);
            if(n==0)
            {
                if(chromosomeA.charAt(i)=='1') chromosome+='0';
                else chromosome+='1';
            }
            else
            {
                chromosome += chromosomeA.charAt(i);
            }
        }
        return chromosome;
    }

    public static void main(String[] args){
        geneticAlgorithm ga = new geneticAlgorithm("testcase.txt",10,10,100,10);
        ga.printInput();
        String random = ga.generateChromosome();
        String mutant = ga.mutation(random);
        System.out.println(random);
        System.out.println(mutant);
    }

}