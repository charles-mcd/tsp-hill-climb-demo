import java.util.*;

/*
*Data points first converted to Euclidean distances
*For Data2, maintaining the data's precision to 4 decimal places, converted to int values (x10e4) and reduced back to real numbers for output.
*
*/


public class TSP {

	private int[][] tsp;
	int [][] grand;
	int grandTotal = 100;

	TSP(int[][] inputIn)
	{
		tsp = inputIn;
	}
	
	public int hillClimb(int[][] initialIn)
	{
		int[][] solution = initialIn;
		int solutionTotal = totalCost(initialIn);
		int neighbourTotal;
		String cheaper = "Convergence of cheaper neighbour totals: " + solutionTotal + ", ";
		System.out.println("Initial solution: " + Arrays.deepToString(solution));
		System.out.println("Initial total: " + solutionTotal);
		int i = 100;
		while (i != 0)
		{
			int swap1 = (int) (Math.random()*(tsp.length-4)+1);    //range excludes start node and three back from the end to avoid swapping the last node connecting back to start
			int swap2 = swap1 + 1;
			int x = solution[swap1][0];
			solution[swap1][0] = solution[swap2][0];
			solution[swap2][0] = x;
			solution[swap1 -1][1] = tsp[solution[swap1 -1][0]][solution[swap1][0]];
			solution[swap2][1] = tsp[solution[swap2][0]][solution[swap2 +1][0]];
			neighbourTotal = totalCost(solution);
			
			if (solutionTotal > neighbourTotal)
			{
				cheaper += neighbourTotal + ", ";
				solutionTotal = neighbourTotal;
				i = 101;
			}
			else
			{
				x = solution[swap1][0];
				solution[swap1][0] = solution[swap2][0];
				solution[swap2][0] = x;
				solution[swap1 -1][1] = tsp[solution[swap1 -1][0]][solution[swap1][0]];
				solution[swap2][1] = tsp[solution[swap2][0]][solution[swap2 +1][0]];
			}
			i--;
		}
		System.out.println((cheaper.substring(0, cheaper.length() - 2)) + ".");
		System.out.println("Cheapest route (no improvement after 25 iteration): " + Arrays.deepToString(solution));
		System.out.println("Cost: " + solutionTotal);
		
		//System.out.println("Initial solution: " + Arrays.deepToString(initialIn));
		//System.out.println(cheaper);
		//System.out.println("Cheapest route (no improvement after 25 iteration): " + Arrays.deepToString(solution));
		return solutionTotal;
	}
	
	public int steepestClimb(int[][] initialIn)
	{
		int[][] solution = initialIn;
		int solutionTotal = totalCost(initialIn);
		String cheaper = solutionTotal + ", "; //"Convergence of cheaper neighbour totals: " + 
		System.out.println("Initial solution: " + Arrays.deepToString(solution));
		System.out.println("Initial total: " + solutionTotal);
		final int counter = 1000;
		int i = counter;
		while (i != 0)
		{
			int k = 0;	//tracks best swap1
			int c = 0;	//tracks best swap1 cost improvement
			for (int j = 1; j < solution.length-2; j++)	//search whole neighbourhood
			{
				// calculate initial local cost
				int ilc = solution[j-1][1] + solution[j+1][1];
				// swapped local cost
				int slc = tsp[solution[j-1][0]][solution[j+1][0]] + tsp[solution[j][0]][solution[j+2][0]];
				int diff = ilc - slc;
				if (diff > c)	//update best swap1 and cost improvement is there is an improvement
				{
					k = j;
					c = diff;
				}
			}
			if (c != 0)	//if there is a cost improvement after searching whole neighbourhood, update solution
			{
				int x = solution[k][0];
				solution[k][0] = solution[k+1][0];
				solution[k+1][0] = x;
				solution[k-1][1] = tsp[solution[k-1][0]][solution[k][0]];
				solution[k+1][1] = tsp[solution[k+1][0]][solution[k+2][0]];
				solutionTotal -= c;
				cheaper += (solutionTotal) + ", ";
				i = counter+1;
			}
			i--;
		}
		System.out.println((cheaper.substring(0, cheaper.length() - 2)) + ".");
		System.out.println("Cheapest route (no improvement after " + counter + " iterations): " + Arrays.deepToString(solution));
		System.out.println("Cost: " + solutionTotal);
		//System.out.println("Cost calculated: " + totalCost(solution));
		return solutionTotal;
	}
	
	public int twoOpt(int[][] initialIn)
	{
		int[][] solution = initialIn;
		int n = solution.length;
		int solutionTotal = totalCost(initialIn);
		String cheaper = solutionTotal + ", "; //"Convergence of cheaper neighbour totals: " + 
		System.out.println("Initial solution: " + Arrays.deepToString(solution));
		System.out.println("Initial total: " + solutionTotal);
		final int counter = 100;
		int i = counter;
		while (i != 0)
		{
			for (int j = 0; j < n-2; j++) // account for going over the top back to 0 
			{
				int k = 0;
				int c = 0;
				for (int s = j+2; s < n; s++) // account for going over the top back to 0 
				{
					// calc initial local cost
					int ilc = solution[j][1] + solution[s][1];
					// candidate local cost
					int clc = tsp[solution[j][0]][solution[s][0]] + tsp[solution[j+1][0]][solution[(s+1)%n][0]];
					int diff = ilc - clc;
					if (diff > c)
					{
						k = s;
						c = diff;
					}
				}

				if (c != 0)	//if there is a cost improvement after searching whole neighbourhood, update solution
				{
					solution = twoOptSwap(solution, j, k);
					cheaper += (solutionTotal - c) + ", ";
					solutionTotal -= c;
					i = counter+1;
				}
			}
			i--;
		}
		System.out.println((cheaper.substring(0, cheaper.length() - 2)) + ".");
		System.out.println("Cheapest route (no improvement after " + counter + " iterations): " + Arrays.deepToString(solution));
		System.out.println("Cost: " + solutionTotal);
		//System.out.println("Cost calculated: " + totalCost(solution));
		
		//int totalCheck = 0;
		//for (int u = 0; u < solution.length; u++)
		//{
		//	totalCheck += tsp[solution[u][0]][solution[(u+1)%solution.length][0]];
		//}
		//System.out.println("Checked again!: " + totalCheck);
		
		//if (solutionTotal < grandTotal)
		//{
		//	grand = Arrays.stream(solution).map(int[]::clone).toArray(int[][]::new);
		//	grandTotal = solutionTotal;
		//}
		
		return solutionTotal;
	}
	
	public int[][] twoOptSwap(int[][] s, int v1, int v2)
	{
		int[][] swap = new int[s.length][s[0].length];
		
		for (int i = 0; i <= v1; i++) // fill the array up to v1 as before
		{
			swap[i][0] = s[i][0];
			swap[i][1] = s[i][1];
		}
		
		//reverse inbetween sequence
		int j = v2;
		for (int i = v1+1; i <= v2; i++)
		{
			swap[i][0] = s[j][0];
			swap[i][1] = s[j-1][1];
			j--;
		}
		
		for (int i = (v2)+1; i < s.length; i++) //fill array after v2 as before
		{
			swap[i][0] = s[i][0];
			swap[i][1] = s[i][1];
		}		
		
		//set costs between segments
		swap[v1][1] = tsp[swap[v1][0]][swap[(v1)+1][0]];
		swap[v2][1] = tsp[swap[v2][0]][swap[((v2)+1)%swap.length][0]];
		
		return swap;
	}
	
	public int totalCost(int[][] a)
	{
		int total = 0;
		for (int i = 0; i < a.length; i++)
		{
			total += a[i][1];
		}
		return total;
	}
	
	public int[][] greedyInitial() //nearest neighbour
	{
		int[][] greedy = new int[tsp.length][2];    //1d for order, 2d for cost to next city
		boolean[] explored = new boolean[tsp.length];
		int start = (int) (Math.random()*(tsp.length));
		int k = start;
		int j = 0;
		int smallest = 99999; 
		
		for (int s = 0; s < greedy.length; s++)
		{
			smallest = 99999;
			for (int i = 0; i < tsp.length; i++)
			{ 
				if ((tsp[k][i] < smallest) && !(explored[i]) && (k != i))
				{
					smallest = tsp[k][i];
					j = i;
				}
			}
			greedy[s][0] = k;
			greedy[s][1] = smallest;
			explored[k] = true;
			k = j;
		}
		greedy[greedy.length -1][1] = tsp[greedy[greedy.length -1][0]][start];    //assign final cost back to start
		return greedy;
	}
	
	public int[][] lessGreedyInitial() //randomised nearest neighbour
	{
		int[][] greedy = new int[tsp.length][2];    //1d for order, 2d for cost to next city
		boolean[] explored = new boolean[tsp.length];
		int start = (int) (Math.random()*(tsp.length));
		int k = start;
		int j = 0;
		int m = 0;
		int n = 0;
		int high = 99999;
		
		for (int s = 0; s < greedy.length; s++)
		{
			int smallest = high;
			int second = high;
			int third = high;
			for (int i = 0; i < tsp.length; i++)
			{ 
				if (!(explored[i]) && (k != i))
				{
					if (tsp[k][i] < smallest)
					{
						smallest = tsp[k][i];
						j = i;
					}
					else if (tsp[k][i] < second)
					{
						second = tsp[k][i];
						m = i;
					}
					else if (tsp[k][i] < third)
					{
						third = tsp[k][i];
						n = i;
					}
				}
			}
			int random = (int) (Math.random()*(3));
			int cost = 0;
			int next = 0;
			if (random == 0)
			{
				cost = smallest;
				next = j;
			}
			else if ((random == 1) && (second != high))
			{
				cost = second;
				next = m;
			}
			else if ((random == 2) && (third != high))
			{
				cost = third;
				next = n;
			}
			else
			{
				cost = smallest;
				next = j;	
			}
			
			greedy[s][0] = k;
			greedy[s][1] = cost;
			explored[k] = true;
			k = next;
		}
		greedy[greedy.length -1][1] = tsp[greedy[greedy.length -1][0]][start];    //assign final cost back to start
		return greedy;
	}
		
	public int[][] randomInitial()
	{
		// using Collections.shuffle to randomise numbers 1 - 49 (requires a List)
		// excludes 0 from list to maintain constant starting point
		ArrayList<Integer> a = new ArrayList<>(tsp.length - 1);
		for (int i = 1; i < tsp.length; i++)
		{                 
		    a.add(i);
		}
		Collections.shuffle(a);
		
		int[][] random = new int[tsp.length][2];    //1d for order, 2d for cost to next city
		int k = 0;

		for (int s = 0; s < random.length; s++)
		{
			random[s][0] = k;
			if (s != random.length -1)              // avoids outofbounds error on final iteration (because of excluded 0 earlier)
			{
				k = a.get(s);
			}
			random[s][1] = tsp[random[s][0]][k];
		}
		random[random.length -1][1] = tsp[random[random.length -1][0]][0];    //assign final cost back to start
		return random;
		
	}
		
	public static void main(String[] args) {
		
		//TSP t = new TSP(data1());

		//int[][] b = t.randomInitial();
		//int[][] c = t.lessGreedyInitial();
		//t.hillClimb(a);
		//t.steepestClimb(c);
		//int p = 100;
		//int n = 0;
		//while (p > 53)
		//{
		//	p = t.twoOpt(t.lessGreedyInitial());
		//	n++;
		//}
		//System.out.println(p + " counter: " + n);
		//System.out.println(Arrays.deepToString(t.grand));
		//System.out.println(t.grandTotal);
		
		
		TSP v = new TSP(data2());
		//int[][] d = v.greedyInitial();
		int[][] e = v.randomInitial();
		v.hillClimb(e);
		
		//for (int i = 0; i < 10; i++)
		//	{
		//		int[][] f = v.lessGreedyInitial();
		//				v.twoOpt(f);
		//	}


		
		//int p = 550000;
		//int n = 0;
		//for (int i = 0; i < 100000; i++)
		//{
		//	int[][] e = v.randomInitial();
		//	int z = v.twoOpt(e);
		//	if (z < p)
		//	{
		//		p = z;
		//	}
		//}
		//System.out.println(p + " counter: " + n);
		
		//while (p > 363819)
		//{
		//	int[][] e = v.randomInitial();
		//	p = v.twoOpt(e);
		//}

	}
	
	public static int[][] data1()
	{
		int[][] a =
			{{0,7,4,2,5,5,2,6,3,4,6,3,3,7,3,9,10,8,4,6,2,10,9,9,3,6,1,5,4,2,2,5,1,6,5,7,7,7,1,1,4,6,7,5,9,8,10,6,4,2},
			{7,0,7,8,5,1,3,2,3,5,6,5,9,6,10,7,10,3,7,3,7,7,1,3,3,7,9,4,8,7,1,7,4,10,1,5,5,5,8,4,8,5,1,2,8,5,2,4,7,2},
			{4,7,0,8,3,10,3,8,2,3,1,6,7,6,5,7,7,7,7,10,3,8,3,2,7,5,5,7,8,4,7,5,9,9,3,7,6,6,9,3,4,2,10,7,5,7,6,7,6,8},
			{2,8,8,0,6,10,3,2,2,1,5,5,4,8,7,8,10,10,2,2,7,1,6,6,9,5,4,7,8,6,4,2,6,3,1,8,3,5,7,4,8,4,7,8,5,1,4,5,3,2},
			{5,5,3,6,0,9,5,9,4,8,4,9,8,4,3,8,10,4,7,5,9,8,2,9,10,6,9,6,2,2,5,8,9,8,4,6,1,2,2,7,5,2,5,2,1,9,6,10,7,6},
			{5,1,10,10,9,0,9,9,10,1,9,7,10,6,5,9,3,5,10,6,9,8,6,3,7,1,7,7,8,9,10,8,6,10,6,1,2,9,5,9,3,6,7,1,7,4,1,5,2,2},
			{2,3,3,3,5,9,0,3,2,2,1,7,3,6,7,5,6,5,2,5,9,9,3,3,6,7,5,3,10,1,2,2,2,7,6,1,10,8,8,1,9,10,10,9,8,6,2,4,2,1},
			{6,2,8,2,9,9,3,0,10,4,3,4,5,7,1,9,6,9,4,5,1,2,7,4,9,2,10,6,8,10,3,5,5,8,9,2,2,4,1,6,4,2,3,10,7,5,10,2,8,8},
			{3,3,2,2,4,10,2,10,0,6,2,6,3,2,3,9,1,3,1,5,1,9,2,1,4,5,2,10,4,3,1,3,1,6,8,7,1,1,8,10,6,2,9,4,3,8,1,1,7,7},
			{4,5,3,1,8,1,2,4,6,0,6,8,8,8,3,7,6,4,1,8,4,7,8,2,2,6,5,9,8,8,1,1,1,8,10,7,2,8,2,2,7,4,7,8,6,8,3,8,10,9},
			{6,6,1,5,4,9,1,3,2,6,0,1,4,4,7,6,8,4,3,1,8,3,4,6,3,7,5,2,8,2,3,3,6,1,5,2,2,8,3,7,10,5,7,8,5,7,2,10,2,3},
			{3,5,6,5,9,7,7,4,6,8,1,0,8,5,8,4,3,1,7,5,5,7,1,4,8,7,2,2,1,1,5,7,8,6,2,7,2,2,1,2,2,2,4,4,3,3,9,8,6,2},
			{3,9,7,4,8,10,3,5,3,8,4,8,0,3,1,10,8,6,4,2,7,10,2,3,4,1,7,5,10,5,7,2,4,2,8,9,4,7,3,6,9,6,4,3,5,5,4,6,8,5},
			{7,6,6,8,4,6,6,7,2,8,4,5,3,0,5,2,1,3,4,7,10,10,5,3,8,8,8,8,2,7,5,3,1,9,2,2,7,9,6,8,2,10,6,7,1,9,8,2,6,4},
			{3,10,5,7,3,5,7,1,3,3,7,8,1,5,0,6,4,5,2,3,1,10,7,10,2,10,8,6,5,3,8,3,1,8,7,8,7,5,4,9,4,9,8,9,6,7,10,5,1,9},
			{9,7,7,8,8,9,5,9,9,7,6,4,10,2,6,0,7,4,10,3,7,7,4,2,1,5,2,8,4,9,8,6,2,10,3,10,3,4,1,7,2,1,8,4,7,4,7,1,10,9},
			{10,10,7,10,10,3,6,6,1,6,8,3,8,1,4,7,0,8,9,4,7,6,6,3,3,5,3,9,10,1,6,1,9,10,1,10,1,7,8,6,9,9,7,2,3,2,1,2,7,10},
			{8,3,7,10,4,5,5,9,3,4,4,1,6,3,5,4,8,0,4,5,10,10,7,10,8,4,7,3,3,7,6,5,7,8,6,6,6,6,1,8,10,4,10,4,9,5,5,3,2,4},
			{4,7,7,2,7,10,2,4,1,1,3,7,4,4,2,10,9,4,0,8,8,7,1,9,10,8,1,4,8,8,3,3,7,5,7,3,2,9,8,10,2,2,1,5,2,9,1,1,6,8},
			{6,3,10,2,5,6,5,5,5,8,1,5,2,7,3,3,4,5,8,0,4,2,4,3,6,10,7,2,4,1,6,5,10,9,5,9,2,4,10,10,8,7,4,10,2,8,7,9,4,8},
			{2,7,3,7,9,9,9,1,1,4,8,5,7,10,1,7,7,10,8,4,0,9,4,6,10,6,4,7,4,8,5,5,7,10,4,9,8,10,1,4,7,3,3,8,7,6,7,1,4,5},
			{10,7,8,1,8,8,9,2,9,7,3,7,10,10,10,7,6,10,7,2,9,0,3,8,9,3,8,2,9,2,6,4,9,6,5,9,4,5,10,1,10,2,7,6,7,4,7,9,1,1},
			{9,1,3,6,2,6,3,7,2,8,4,1,2,5,7,4,6,7,1,4,4,3,0,10,7,3,5,2,3,3,4,2,4,2,9,1,10,4,1,4,8,8,6,7,9,1,4,1,2,8},
			{9,3,2,6,9,3,3,4,1,2,6,4,3,3,10,2,3,10,9,3,6,8,10,0,8,9,6,1,10,9,3,6,10,8,9,5,5,6,3,8,6,7,4,2,5,4,8,8,7,2},
			{3,3,7,9,10,7,6,9,4,2,3,8,4,8,2,1,3,8,10,6,10,9,7,8,0,1,6,4,10,10,3,9,9,6,9,10,6,8,6,1,5,7,6,4,10,9,9,4,6,9},
			{6,7,5,5,6,1,7,2,5,6,7,7,1,8,10,5,5,4,8,10,6,3,3,9,1,0,10,7,3,7,1,5,7,10,9,5,8,5,10,10,9,4,5,3,8,9,10,6,6,2},
			{1,9,5,4,9,7,5,10,2,5,5,2,7,8,8,2,3,7,1,7,4,8,5,6,6,10,0,9,5,3,9,8,9,3,7,7,2,5,3,8,3,9,9,4,5,7,9,7,6,4},
			{5,4,7,7,6,7,3,6,10,9,2,2,5,8,6,8,9,3,4,2,7,2,2,1,4,7,9,0,5,8,9,8,1,7,5,5,2,9,4,3,4,4,6,6,4,4,6,7,10,8},
			{4,8,8,8,2,8,10,8,4,8,8,1,10,2,5,4,10,3,8,4,4,9,3,10,10,3,5,5,0,5,9,2,1,1,2,4,4,1,6,1,2,7,9,10,6,10,6,6,4,5},
			{2,7,4,6,2,9,1,10,3,8,2,1,5,7,3,9,1,7,8,1,8,2,3,9,10,7,3,8,5,0,5,1,9,1,5,9,4,7,9,9,10,2,3,9,6,6,7,9,6,3},
			{2,1,7,4,5,10,2,3,1,1,3,5,7,5,8,8,6,6,3,6,5,6,4,3,3,1,9,9,9,5,0,5,5,10,7,7,8,4,6,6,2,6,7,5,9,8,4,5,4,8},
			{5,7,5,2,8,8,2,5,3,1,3,7,2,3,3,6,1,5,3,5,5,4,2,6,9,5,8,8,2,1,5,0,8,5,7,10,8,8,2,4,6,5,1,3,9,1,9,1,7,6},
			{1,4,9,6,9,6,2,5,1,1,6,8,4,1,1,2,9,7,7,10,7,9,4,10,9,7,9,1,1,9,5,8,0,3,6,2,7,6,1,1,2,1,5,9,7,6,9,1,10,2},
			{6,10,9,3,8,10,7,8,6,8,1,6,2,9,8,10,10,8,5,9,10,6,2,8,6,10,3,7,1,1,10,5,3,0,6,2,6,1,8,9,10,10,6,3,2,6,6,8,1,7},
			{5,1,3,1,4,6,6,9,8,10,5,2,8,2,7,3,1,6,7,5,4,5,9,9,9,9,7,5,2,5,7,7,6,6,0,6,2,10,6,5,10,7,5,9,6,6,7,4,3,6},
			{7,5,7,8,6,1,1,2,7,7,2,7,9,2,8,10,10,6,3,9,9,9,1,5,10,5,7,5,4,9,7,10,2,2,6,0,9,5,2,5,4,5,9,5,4,4,2,3,1,5},
			{7,5,6,3,1,2,10,2,1,2,2,2,4,7,7,3,1,6,2,2,8,4,10,5,6,8,2,2,4,4,8,8,7,6,2,9,0,3,3,5,2,5,8,3,8,1,4,1,3,1},
			{7,5,6,5,2,9,8,4,1,8,8,2,7,9,5,4,7,6,9,4,10,5,4,6,8,5,5,9,1,7,4,8,6,1,10,5,3,0,2,2,3,2,2,6,10,10,3,5,4,6},
			{1,8,9,7,2,5,8,1,8,2,3,1,3,6,4,1,8,1,8,10,1,10,1,3,6,10,3,4,6,9,6,2,1,8,6,2,3,2,0,3,1,5,2,1,10,5,10,8,1,7},
			{1,4,3,4,7,9,1,6,10,2,7,2,6,8,9,7,6,8,10,10,4,1,4,8,1,10,8,3,1,9,6,4,1,9,5,5,5,2,3,0,8,7,6,3,8,3,4,9,9,5},
			{4,8,4,8,5,3,9,4,6,7,10,2,9,2,4,2,9,10,2,8,7,10,8,6,5,9,3,4,2,10,2,6,2,10,10,4,2,3,1,8,0,4,7,10,10,6,4,9,5,10},
			{6,5,2,4,2,6,10,2,2,4,5,2,6,10,9,1,9,4,2,7,3,2,8,7,7,4,9,4,7,2,6,5,1,10,7,5,5,2,5,7,4,0,1,6,8,2,4,2,4,5},
			{7,1,10,7,5,7,10,3,9,7,7,4,4,6,8,8,7,10,1,4,3,7,6,4,6,5,9,6,9,3,7,1,5,6,5,9,8,2,2,6,7,1,0,6,1,6,3,3,3,2},
			{5,2,7,8,2,1,9,10,4,8,8,4,3,7,9,4,2,4,5,10,8,6,7,2,4,3,4,6,10,9,5,3,9,3,9,5,3,6,1,3,10,6,6,0,10,10,9,8,2,4},
			{9,8,5,5,1,7,8,7,3,6,5,3,5,1,6,7,3,9,2,2,7,7,9,5,10,8,5,4,6,6,9,9,7,2,6,4,8,10,10,8,10,8,1,10,0,2,1,4,7,7},
			{8,5,7,1,9,4,6,5,8,8,7,3,5,9,7,4,2,5,9,8,6,4,1,4,9,9,7,4,10,6,8,1,6,6,6,4,1,10,5,3,6,2,6,10,2,0,6,5,3,6},
			{10,2,6,4,6,1,2,10,1,3,2,9,4,8,10,7,1,5,1,7,7,7,4,8,9,10,9,6,6,7,4,9,9,6,7,2,4,3,10,4,4,4,3,9,1,6,0,8,8,6},
			{6,4,7,5,10,5,4,2,1,8,10,8,6,2,5,1,2,3,1,9,1,9,1,8,4,6,7,7,6,9,5,1,1,8,4,3,1,5,8,9,9,2,3,8,4,5,8,0,8,7},
			{4,7,6,3,7,2,2,8,7,10,2,6,8,6,1,10,7,2,6,4,4,1,2,7,6,6,6,10,4,6,4,7,10,1,3,1,3,4,1,9,5,4,3,2,7,3,8,8,0,2},
			{2,2,8,2,6,2,1,8,7,9,3,2,5,4,9,9,10,4,8,8,5,1,8,2,9,2,4,8,5,3,8,6,2,7,6,5,1,6,7,5,10,5,2,4,7,6,6,7,2,0}};
		return a;
	}
	
	public static int[][] data2()
	{
		int[][] a =
		{{0,7548,13450,25648,27960,33277,41112,46412,50988,58154,51859,43933,45262,47439,43867,39885,37877,32955,27229,22792,20987,15506,12609,8739},
		{7548,0,6623,18221,21098,27054,35530,42051,47394,56106,50164,42640,45346,48668,46291,43814,43119,38882,32942,27532,24441,20502,19618,16286},
		{13450,6623,0,12506,14574,20437,29018,35996,41707,51304,45667,38553,42276,46480,45196,44185,44846,41466,35436,29255,25170,22933,24110,21980},
		{25648,18221,12506,0,6279,13491,22837,32358,39317,51374,46824,41146,46825,52433,52884,53737,55709,53033,47033,40471,35784,34777,36597,34331},
		{27960,21098,14574,6279,0,7312,16706,26079,33055,45288,40940,35648,41816,47847,49044,50927,53856,51920,46043,39211,34139,34358,37508,36293},
		{33277,27054,20437,13491,7312,0,9394,19040,26228,39067,35279,30937,37936,44591,46938,50242,54347,53328,47706,40739,35388,36974,41355,41108},
		{41112,35530,29018,22837,16706,9394,0,10667,18139,31897,29262,26844,34802,42121,46018,51098,56560,56660,51478,44587,39160,42144,47637,48335},
		{46412,42051,35996,32358,26079,19040,10667,0,7473,21410,19592,19279,27630,35242,40568,47347,54128,55523,51053,44567,39374,43851,50509,52487},
		{50988,47394,41707,39317,33055,26228,18139,7473,0,14163,13588,16022,24011,31519,37901,45834,53441,55745,51880,45861,41019,46312,53533,56264},
		{58154,56106,51304,51374,45288,39067,31897,21410,14163,0,6661,14789,18726,24530,32408,41976,50705,54619,52102,47331,43499,49880,57741,61751},
		{51859,50164,45667,46824,40940,35279,29262,19592,13588,6661,0,8160,12756,19420,26868,36067,44599,48244,45555,40689,36839,43238,51115,55212},
		{43933,42640,38553,41146,35648,30937,26844,19279,16022,14789,8160,0,8353,15971,21888,30005,38002,41036,37932,32764,28750,35092,42958,47075},
		{45262,45346,42276,46825,41816,37936,34802,27630,24011,18726,12756,8353,0,7621,14210,23330,31982,36027,33953,29977,27098,33924,41902,46873},
		{47439,48668,46480,52433,47847,44591,42121,35242,31519,24530,19420,15971,7621,0,8157,18204,27296,32376,31524,29021,27471,34314,42061,47662},
		{43867,46291,45196,52884,49044,46938,46018,40568,37901,32408,26868,21888,14210,8157,0,10094,19215,24617,24481,23218,22992,29457,36731,42753},
		{39885,43814,44185,53737,50927,50242,51098,47347,45834,41976,36067,30005,23330,18204,10094,0,9126,14925,16106,17293,19373,24468,30543,36934},
		{37877,43119,44846,55709,53856,54347,56560,54128,53441,50705,44599,38002,31982,27296,19215,9126,0,6903,10909,15653,19925,22646,26673,33093},
		{32955,38882,41466,53033,51920,53328,56660,55523,55745,54619,48244,41036,36027,32376,24617,14925,6903,0,6030,12710,17948,18572,21002,27252},
		{27229,32942,35436,47033,46043,47706,51478,51053,51880,52102,45555,37932,33953,31524,24481,16106,10909,6030,0,6971,12380,12555,15764,22188},
		{22792,27532,29255,40471,39211,40739,44587,44567,45861,47331,40689,32764,29977,29021,23218,17293,15653,12710,6971,0,5432,7286,13573,19802},
		{20987,24441,25170,35784,34139,35388,39160,39374,41019,43499,36839,28750,27098,27471,22992,19373,19925,17948,12380,5432,0,6889,14822,20192},
		{15506,20502,22933,34777,34358,36974,42144,43851,46312,49880,43238,35092,33924,34314,29457,24468,22646,18572,12555,7286,6889,0,7986,13375},
		{12609,19618,24110,36597,37508,41355,47637,50509,53533,57741,51115,42958,41902,42061,36731,30543,26673,21002,15764,13573,14822,7986,0,6462},
		{8739,16286,21980,34331,36293,41108,48335,52487,56264,61751,55212,47075,46873,47662,42753,36934,33093,27252,22188,19802,20192,13375,6462,0}};

		return a;
	}

}
