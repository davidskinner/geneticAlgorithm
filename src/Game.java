import jdk.nashorn.api.scripting.JSObject;

import java.util.ArrayList;
import java.util.Random;

class Game
{

	public static Matrix Initialmutate(Matrix pop)
	{
		Random r = new Random();
		for (int i = 0; i < pop.rows(); i++) {
			double[] individual = pop.row(i);
			for(int j = 0; j < individual.length; j++)
				individual[j] = .09 * r.nextGaussian();//around .09 is a good starting number
		}
		return pop;
	}

	public static Matrix cloneChad(double[] chad)
	{
		
		Matrix temp = new Matrix(100,291);
		for (int i = 0; i < temp.rows(); i++) {
			for (int j = 0; j <291 ; j++) {
				temp.row(i)[j] = chad[j];

			}
		}
		return temp;
	}

	public static int[][] pickHalfPopulationToFight()
	{
		int[][] participants = new int[25][2];
		Random random = new Random();

		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 2; j++) {
				participants[i][j] = random.nextInt(100);

			}
		}
		return participants;
	}

	public static void log(String val)
	{
		System.out.println(val);
	}

	public static Matrix evolveMutate(Matrix pop)
	{
		Random r = new Random();
		for (int i = 0; i < pop.rows(); i++) {

			if(r.nextGaussian() < 0)
			{
				double[] individual = pop.row(i);

				//evolve population 3% of the time
				float w = r.nextFloat();
				for(int j = 0; j < individual.length; j++)
					if(w < 0.01)
						individual[j] = 0.03 * r.nextGaussian();
			}
		}
		return pop;
	}

	public static Matrix drasticMutate(Matrix pop)
	{
		Random r = new Random();
		for (int i = 0; i < pop.rows(); i++) {

			if(r.nextGaussian() < 0)
			{
				double[] individual = pop.row(i);

				//evolve population 3% of the time
				float w = r.nextFloat();
				for(int j = 0; j < individual.length; j++)
					if(w < 0.07)
						individual[j] = 0.03 * r.nextGaussian();
			}
		}
		return pop;
	}

	public static int getPositionOfMax(ArrayList<Long> list){
		int position = 5;
		long maxValue = 0;


		for(int i=5;i < list.size();i++){
			long t = list.get(i);
			if(list.get(i) > maxValue){
//				log(String.valueOf(list.get(i)));
				maxValue = list.get(i);
				position = i;
			}
		}
		return position;
	}

	public static double[] crossOver(double[] Chad, double[] Stacy)
	{
		//set the Loser of tournament to the crossover
		double[] temp;
		temp = Stacy;
		System.arraycopy(Chad,0,temp,0,250);
		return temp;
	}

	public static int getPositionOfMin(ArrayList<Long> list) {
		int position = 5;
		long minValue = -1;
		for (int i = 5; i < list.size(); i++) {
			if (list.get(i) < minValue) {
					minValue = list.get(i);
					position = i;
			}
		}
		return position;
	}

	static double[] evolveWeights()
	{

		//meta parameters
		// 80% mut
		//10 torn
		// 1.5 mut dev
		// winner .9

		// SR .9
		//0.27
		//mut dev 11
		// 999 gen
		//< 3 mins

		//gashler weights 2 = 0.1 * rand.nextGaussian()


		// Create a random initial population

		Matrix population = new Matrix(100, 291);
		population = Initialmutate(population);

			ArrayList<IAgent> agentList = new ArrayList<>();

			agentList.add(new ReflexAgent());
			for (int j = 0; j < population.rows(); j++) {

				agentList.add(new NeuralAgent(population.row(j)));
			}

		int winnerVal =0;
			int neuralWinnerVal = 0;
			int chadPosition = -1;
			boolean postChad = false;

//			ArrayList<Integer> footballTeam = new ArrayList<>();

			// fitness function
			for (int i = 0; i < 1000; i++) {

				if(i % 5 == 0 & !postChad)
				{
					population = drasticMutate(population);
				}
				else
				{
					population = evolveMutate(population);
				}

				ArrayList<Long> times = new ArrayList<>();
				long iter = 0;

				//loop through every individual in the population and make them fight the ReflexAgent

					try{
						//every individual fights the Reflex Agent


							for (int j = 0; j < population.rows(); j++) {

								winnerVal = Controller.doBattleNoGui(new ReflexAgent(), new NeuralAgent(population.row(j)));

								//if a neural agent beats a reflex
								if(!postChad) {
								if (winnerVal < 0) {
									chadPosition = j;
//									footballTeam.add(chadPosition);
									postChad = true;
									System.out.println("we have a chad!" + " " + String.valueOf(-1 * winnerVal) + " " + String.valueOf(j));

//									population = cloneChad(population.row(chadPosition));

									// crossover the Chad with 95% of the population
									//repopulate world with chads
//									for (int k = 0; k < 100; k++) {

//										population.row(k) = population.row(chadPosition);
//										if(!footballTeam.contains(chadPosition))
//										{
////											double[] s = crossOver(population.row(chadPosition), population.row(k));
//											System.arraycopy(population.row(chadPosition),0,population.row(k),0,291);
//										}

									//make chads battle
//										if(footballTeam.size() > 2)
//										{
//											for (int l = 0; l < footballTeam.size() -1; l++) {
//
//												int winnerValue = Controller.doBattleNoGui(new NeuralAgent(population.row(footballTeam.get(l))), new NeuralAgent(population.row(footballTeam.get(l +1))));
//
//												if(winnerValue > 0)
//												{
//													double[] s = crossOver(population.row(footballTeam.get(l)), population.row(footballTeam.get(l+1)));
//													System.arraycopy(s,0,population.row(),0,291);
//												}
//												//p2 wins
//												else
//												{
//													double[] s = crossOver(population.row(k+1), population.row(k));
//													System.arraycopy(s,0,population.row(k),0,291);
//												}
//											}
//										}

//									}
								}
							}
						}

						if(postChad)
						{
							break;
						}

						
							//tournaments per cycle
							int[][] participants = pickHalfPopulationToFight();
							ArrayList<Integer> secondRound = new ArrayList<>();
							int winnerValue = 0;

							//First tournament
							for (int j = 0; j < participants.length ; j++) {

								winnerValue = Controller.doBattleNoGui(new NeuralAgent(population.row(participants[j][0])), new NeuralAgent(population.row(participants[j][1])));

								//p1 wins
								if(winnerValue > 0)
								{
									double[] s = crossOver(population.row(participants[j][0]), population.row(participants[j][1]));
									System.arraycopy(s,0,population.row(participants[j][1]),0,291);
									secondRound.add(participants[j][0]);
								}
								//p2 wins
								else
								{
									double[] s = crossOver(population.row(participants[j][1]), population.row(participants[j][0]));
									System.arraycopy(s,0,population.row(participants[j][0]),0,291);
									secondRound.add(participants[j][1]);
								}
							}

							//second round of tournament
							for (int k = 0; k < secondRound.size() - 1; k++) {
								winnerValue = Controller.doBattleNoGui(new NeuralAgent(population.row(secondRound.get(k))), new NeuralAgent(population.row(secondRound.get(k+1))));

								if(winnerValue > 0)
								{
									double[] s = crossOver(population.row(k), population.row(k+1));
									System.arraycopy(s,0,population.row(k+1),0,291);
								}
								//p2 wins
								else
								{
									double[] s = crossOver(population.row(k+1), population.row(k));
									System.arraycopy(s,0,population.row(k),0,291);
								}
							}

					}
					catch(Exception e)
					{
						System.out.println(String.valueOf(e));
					}
				System.out.println(String.valueOf(winnerVal)+ " " + String.valueOf(i % 100) + " " + population.row(i %100)[0]); //+ " " + String.valueOf(footballTeam.size()

			}









//		double mutationRate = .2;
//		double averageMutationDeviation = .03;
//		int tournaments = 100000;
//		double survivalProbability = .7;
//		int matesForCrossover = 10;

		//continuous measure of fitness.... number of frames to win?
		/// void interpolation (double [] mom, double[] dad)
//		{
//		double d = rand.nextdouble();
//		doublep[] child = new double[mom.lenth];
//			for (int i = 0; i < child.length; i++) {
//				child[i] = k * mom[i] + (1.0-d) * dad[i];
//			}
//			return child;
//		}


//		boolean selectionCondition = false;
//		while(true)
//		{
//			if(selectionCondition)
//				break;
//
//			double prob = r.nextInt(5);
//
//			if (propb < .2)
//				mutate();
//			else if(prob < .45)
//				tourn();
//			else
//				catastrophicMutation();
//
//			doTournament();
//			52 kill loser;
//			48 kill winner;



//			swith(operation)
//			{
//				case 0: mutate(); break;
//				case 1: tournament(); break;
//				case 2: replenishPop(); break;
//
//			}
//		}
//		(1) Promote diversity within the population.
//		for(int i = 0; i < 100; i++)
//		{
//			double[] chromosome = population.row(i);
//			for(int j = 0; j < chromosome.length; j++)
//				chromosome[j] = 0.03 * r.nextGaussian();
//		}

//		(2) Select the "more fit" chromosomes to survive. Kill the "less fit" ones.

//		Controller.doBattleNoGui(agent1, agent2))

//		(3) Replenish the population.


		// Evolve the population
		// todo: YOUR CODE WILL START HERE.

		//       Please write some code to evolve this population.
		//       (For tournament selection, you will need to call Controller.doBattleNoGui(agent1, agent2).)

		// Return an arbitrary member from the population
		//wrap up double array to json file
		Json j = Json.newList();

		for (int i = 0; i < population.cols(); i++) {
			j.add(population.row(chadPosition)[i]);
		}

		j.save("chad.json");


		return population.row(chadPosition);
	}


	public static void main(String[] args) throws Exception
	{
		//read in json file and build array.

//		double[] w = evolveWeights();
		double[] chad = new double[291];
		Json j = Json.load("chad.json");
		Json js = Json.parse(j.toString());
		for (int i = 0; i < chad.length; i++) {
			chad[i] = js.get(i).asDouble();
		}



		Controller.doBattle(new ReflexAgent(), new NeuralAgent(chad));
	}

}
