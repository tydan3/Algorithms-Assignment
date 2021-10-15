
import java.util.*;
import java.lang.*;
import java.io.*;

public class tcss343 {
	private static ArrayList<Integer> subsetBF = new ArrayList<Integer>();
	private static boolean[][] dp;
	private static HashMap<Integer, ArrayList<ArrayList<Integer>>> sumSets = new HashMap<Integer, ArrayList<ArrayList<Integer>>>();

	/**
	 * Brute Force
	 * 
	 * @param S
	 * @param i
	 * @param target
	 * @return if Set has some subset that equals a given sum.
	 */
	public static boolean isSumBF(int[] S, int n, int target, ArrayList<Integer> indices) {
		if (target == 0) {
			return true;
		}
		if (n == 0 && target != 0) {
			return false;
		}
		if (S[n - 1] > target) {
			return isSumBF(S, n - 1, target, indices);
		}

		boolean found = isSumBF(S, n - 1, target, indices);
		if (found) {
			return true;
		}
		indices.add(0, n - 1);
		found = isSumBF(S, n - 1, target - S[n - 1], indices);

		if (!found) {
			indices.remove(0);
		}

		subsetBF = indices;
		return found;
	}

	/**
	 * Dynamic Programming
	 * 
	 * @param arr
	 * @param n
	 * @param sum
	 * @return if Set has some subset that equals a given sum.
	 */
	public static boolean isSumDP(int arr[], int n, int sum) {

		boolean subset[][] = new boolean[n + 1][sum + 1];
		// O(n)
		for (int i = 0; i <= n; i++) {
			subset[i][0] = true;
		}
		// O(sum - 1)
		for (int j = 1; j <= sum; j++) {
			subset[0][j] = false;
		}

		// O(n-1)
		for (int i = 1; i <= n; i++) {
			// O(sum - 1)
			for (int j = 1; j <= sum; j++) {
				//
				if (subset[i - 1][j] == true) {
					subset[i][j] = true;
				} else {
					if (arr[i - 1] > j) {
						subset[i][j] = false;
					} else {
						subset[i][j] = subset[i - 1][j - arr[i - 1]];
					}
				}
			}
		}

//		System.out.println(Arrays.deepToString(subset));
		dp = subset;
		return subset[n][sum];
	}

	/**
	 * Clever Algorithm
	 * @param arr
	 * @param n
	 * @param sum
	 * @return if Set has some subset that equals a given sum.
	 */
	public static boolean isSumClever(int arr[], int n, int sum) {
		
		int[] L;
		int[] H;
		if ( n % 2 == 0) {
			L = new int[n/2];

		} else {
			L = new int[(n/2)+1];
			
		}
		
		H = new int[n/2];
		int end; 
		int start;
		if (n % 2 == 0) {
			start = n/2;
			end = (n/2) - 1;
		} else {
			start = (n/2) + 1;
			end = n/2;
		}
		for (int i = 0; i <= end; i++) {
			L[i] = arr[i];
		}
		


		
		
		int index = 0;
		for (int i = start; i < n; i++) {
			H[index] = arr[i];
			index++;
		}
				
		ArrayList<ArrayList<Integer>> T = computeSubsets(L);
		for (ArrayList<Integer> set: T) {
			int total = 0;
			for (int num: set) {
				total += num;
			}
			
			if (total == sum) {
				
				ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
				temp.add(set);
				sumSets.put(sum, temp);
				
				return true;
			}
		}
		
		
		ArrayList<ArrayList<Integer>> W = computeSubsets(H);
		for (ArrayList<Integer> set: W) {
			int total = 0;
			for (int num: set) {
				total += num;
			}
			
			if (total == sum) {
				
				ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
				temp.add(set);
				sumSets.put(sum, temp);
				
				return true;
			}
		}
		
		
		Collections.sort(W, new Comparator<List<Integer>> () {
		    @Override
		    public int compare(List<Integer> a, List<Integer> b) {
		        int aTotal = 0;
		    	for (int num: a) {
		        	aTotal += num;
		        }
		    	int bTotal = 0;
		    	for (int num: b) {
		    		bTotal += num;
		    	}
		    	
		    	return aTotal - bTotal;
		    }
		});
		
		for (ArrayList<Integer> I: T) {
			for (ArrayList<Integer> J: W) {
				int iTotal = 0;
				for (int num: I) {
					iTotal += num;
				}
				
				int jTotal = 0;
				for (int num: J) {
					jTotal += num;
				}
				
				if (iTotal + jTotal == sum) {
					
					ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
					I.addAll(J);
					temp.add(I);
					sumSets.put(sum, temp);
					
					return true;
				} else if (iTotal + jTotal > sum) {
					break;
				}
			}
		}
		
		ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
		temp.add(W.get(0));
		sumSets.put(sum, temp);
		return false;
	}
	
	
	
	public static ArrayList<ArrayList<Integer>> computeSubsets(int[] set) {
		ArrayList<ArrayList<Integer>> subsets = new ArrayList<ArrayList<Integer>>();
		int n = set.length; 


		for (int i = 0; i < (1 << n); i++) {
			ArrayList<Integer> temp = new ArrayList<Integer>();

			for (int j = 0; j < n; j++)
				if ((i & (1 << j)) > 0)
					temp.add(set[j]);
			subsets.add(temp);
		}
		
		return subsets;
	}


	public static void Driver(int n, int r, boolean v) {
		int[] S = new int[n];
		Random rand = new Random();
		for (int i = 0; i < n; i++) {
			S[i] = rand.nextInt(r);

		}

		int target = 0;
		if (v) {
			
			HashSet<Integer> usedNums = new HashSet<Integer>();
			
			int numElem = rand.nextInt(n);

			int[] subset = new int[numElem];
			for (int i = 0; i < numElem; i++) {
				int val = rand.nextInt(n);
				
				while (usedNums.contains(val)) {
					val = rand.nextInt(n);
				}
				subset[i] = S[val];
				usedNums.add(val);
			}

			for (int i = 0; i < numElem; i++) {
				target += subset[i];
			}

		} else {
			int maxSum = 0;
			for (int i = 0; i < n; i++) {
				maxSum = maxSum + S[i];
			}
			target = maxSum + 1;
		}

//		 Solve Using Brute Force

		long startTimeBF = System.currentTimeMillis();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		boolean BF = isSumBF(S, n, target, indices);
		long endTimeBF = System.currentTimeMillis();
		long runTimeBF = (endTimeBF - startTimeBF); 
		System.out.println("_________________________________________________________");
		System.out.println("Runtime for Brute Force Algo with n = " + n + " is " + runTimeBF + " milliseconds");
		
		if(BF) {
			System.out.println();
			ArrayList<Integer> solutionBF = new ArrayList<Integer>();
			for(int i = 0; i < subsetBF.size(); i++) {
				solutionBF.add(S[subsetBF.get(i)]);
			}
			System.out.println("There Exists a subset that adds to " + target + " which is " + solutionBF.toString() );
		} else {
			System.out.println();
			System.out.println("No Subset Exists");
	}

		System.out.println("_________________________________________________________");
		
		// Solve Using DP

		long startTimeDP = System.currentTimeMillis();
		boolean DP = isSumDP(S, n, target);
		long endTimeDP = System.currentTimeMillis();
		long runTimeDP = (endTimeDP - startTimeDP);
		System.out.println("Runtime for Dynamic Programming Algo with n = " + n + " is " + runTimeDP + " milliseconds");
		if (DP) {
			System.out.println();
			ArrayList<Integer> searchedSet = new ArrayList<Integer>();
			for (int j = S.length, i = target; j > 0 && i != 0; j--) {
				if (S[j - 1] != i) {
					while (dp[j - 1][i]) {
						j--;
					}
				}
				searchedSet.add(S[j - 1]);
				i = i - S[j - 1];
			}
			System.out.println("There Exists a subset that adds to " + target + " which is " + searchedSet.toString() );
			
		} else {
			System.out.println();
			System.out.println("No Subset Exists");
		}

		// Solve Using Clever
		
		long startTimeClever = System.currentTimeMillis();
		boolean clever = isSumClever(S, n, target);
		long endTimeClever = System.currentTimeMillis();
		long runTimeClever = (endTimeClever - startTimeClever);
		System.out.println("_________________________________________________________");
		System.out.println("Runtime for Clever Programming Algo with n = " + n + " is " + runTimeClever + " milliseconds");

		if (clever) {
			System.out.println();
			System.out.println("There exists a subset with given sum " + target + " which is the subset: "
					+ sumSets.get(target).toString());
		} else {
			System.out.println();
			System.out.println("No subset exists");
		}
		
		System.out.println("_________________________________________________________");
	}
	

	public static void main(String[] args) {
		int n = 30;
		int r = 1000000;
		boolean v = false;

		Driver(n, r, v);
	}
}
