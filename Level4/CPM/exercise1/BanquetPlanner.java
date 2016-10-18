/**
 * We are given n guests to be allocated to m equally sized tables (where n % m = 0) at a banquest.  
 * There are constraint of the form together(i,j) and apart(i,j) where
 * together(i,j) means that guests i and j must sit at the same table and
 * apart(i,j) means that guests i and j must sit at different tables. 
 * By default, guests can sit at any table with any other guest
 */

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.IntStream;

import org.chocosolver.solver.*;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.search.strategy.*;
import org.chocosolver.solver.constraints.*;

public class BanquetPlanner {
    Solver solver;
    int nGuests;
    int mTables;
    int tableSize;
    int[] guestsAtTables; // An array of table allocation for each guest
    HashMap<Integer, IntVar> constrainedGuests;
    

    //TODO: Write report
    /*
     * As an optimisation, we'll only create IntVars for constrained guests. 
     * To identify which guests we've made constraints for, and which IntVars correspond to which guests,
     *   we keep them in a hashmap where the keys are guest IDs and the values are the guests' IntVars.
     * Each guest's IntVar contains that guest's table number. 
     * If two guests are to be placed together, they should have an equal table number. Guests to be kept 
     *   apart are to have nonequal table numbers. 
     * We enforce this via arithmetic constraints. 
     * 
     * We can then enforce table size simits by specifying that an array of the created IntVars (table numbers)
     *   can have at least 0 and at most tableSize occurrences of each table number.
     * 
     * When we come to print our output, we know that if we have no entry in our hashmap for the guest, 
     *   that guest had no constraints and can be placed at any table. 
     *   They are therefore given the next available table. 
     * 
     */
    
    
    BanquetPlanner(String fname) throws IOException {
        try (Scanner sc = new Scanner(new File(fname))) {
            nGuests   = sc.nextInt(); // number of guests
            mTables   = sc.nextInt(); // number of tables
            tableSize = nGuests / mTables;
            solver    = new Solver("banquet planner");
            constrainedGuests = new HashMap<Integer, IntVar>();
            
            
            while (sc.hasNext()) {
                String s = sc.next();
                int i    = sc.nextInt();
                int j    = sc.nextInt();
                
                // Create variables for the constrained guests if they don't yet exist
                if (!constrainedGuests.containsKey(i)) {
                	constrainedGuests.put(i, VF.integer("guest" + Integer.toString(i), 0, mTables-1, solver));
                }
                if (!constrainedGuests.containsKey(j)) {
                	constrainedGuests.put(j, VF.integer("guest" + Integer.toString(j), 0, mTables-1, solver));
                }
                
                // guarantee that two "together" guests have the same table number, and not equal for "apart"
                // ICF.arithm requires that the last argument be an int, so we couldn't simply use i != j.
                if (s.equals("together")) {
                	solver.post(IntConstraintFactory.arithm((IntVar)constrainedGuests.get(i), "-", (IntVar)constrainedGuests.get(j), "=", 0));
                } else { // s.equals("apart")
                	solver.post(IntConstraintFactory.arithm((IntVar)constrainedGuests.get(i), "-", (IntVar)constrainedGuests.get(j), "!=", 0));
                }
            }
        }

        // Set a constraint so the solution can have 0->tableSize occurrences of each table number.
        IntVar[] tableArray = createArrayFromHashMap(constrainedGuests);     // An array of the intvars
        int[] values = IntStream.rangeClosed(0,mTables-1).toArray();         // The values the intVars can take
        IntVar[] OCC = VF.enumeratedArray("occurences", mTables, 
                0, tableSize, solver);                                       // The range of allowable occurences of any value in `values`
        
        solver.post(ICF.global_cardinality(tableArray, values, OCC, false)); // Constrain the table sizes
        
        
        // set a custom variable ordering to process the least constrained first
        solver.set(ISF.custom(ISF.minDomainSize_var_selector(), ISF.min_value_selector(), tableArray));
        
    }

    boolean solve() {
        return solver.findSolution();
    }

	// print out solution in specified format (see readme.txt)
	// so that results can be verified
    void result() {
    	
    	StringBuffer[] outputLines = new StringBuffer[mTables];  // Places to put lines of output
        guestsAtTables = new int[mTables];  // Count how many guests are at each table so far
        int currentTable = 0;  // So we add unconstrained guests to the right tables
        Integer guestID;  // for readability later
        int tableNumber;  // for readability later
        
        // Create our string buffers for output, begin them with their respective tablenumbers
    	for (int table = 0; table < mTables; table++) {
    		outputLines[table] = new StringBuffer(Integer.toString(table) + " ");
    	}
    	
    	// Allocate the constrained guests to their tables
    	for (Entry<Integer, IntVar> guestDetails : constrainedGuests.entrySet()) {
    		
    		// for readability, convert entry to variables
    		guestID = guestDetails.getKey();
    		tableNumber = guestDetails.getValue().getValue();
    		
    		outputLines[tableNumber].append(guestID.toString() + " ");
    		guestsAtTables[tableNumber]++; 
    	}
    	
    	// Add in the unconstrained guests to the tables that aren't yet full
    	for (Integer guestIndex = 0; guestIndex < nGuests; guestIndex++) {
    		
    		// Make sure we don't process a guest twice
    		if (!constrainedGuests.containsKey(guestIndex)) {
    		    
    			currentTable = correctCurrentTable(guestsAtTables, currentTable); // Maybe the table we're at is now full?
    			outputLines[currentTable].append(guestIndex.toString() + " ");
    			guestsAtTables[currentTable]++;
    			
    		}
    	}
    	
    	// We've constructed output with each guest, regardless of constraints. We can print now.
    	for (StringBuffer line : outputLines) {
    		System.out.println(line.toString());
    	}
    	
    }

    void stats() {
        System.out.println("nodes: " + solver.getMeasures().getNodeCount() + "   cpu: " + solver.getMeasures().getTimeCount());
    }

    public static void main(String[] args) throws IOException {
        BanquetPlanner bp = new BanquetPlanner(args[0]);
        if (bp.solve()) {
            bp.result();
        } else {
        	System.out.println(false);
        }
        bp.stats();
    }
    
    
    
    
    // --------------------------------------------------------
    // --- Helper functions to clean up the above code a little
    // --------------------------------------------------------
    
    // Turn a constrained guests hashmap into an array of the guests' IntVars, which contain table numbers.
    public IntVar[] createArrayFromHashMap(HashMap<Integer, IntVar> originalHashmap) {
    	return originalHashmap.values().toArray(VF.integerArray("constrained guests array", constrainedGuests.size(), 0,  mTables-1, solver));
    }
    
    // Helper function to make sure we're on the right table to add a guest to in the result() function
    public int correctCurrentTable(int[] guestsAtTables, int currentTable) {
        
        // If we've reached our capacity, move to the next table and make sure that isn't full too.
    	if (guestsAtTables[currentTable] == tableSize) {
    		currentTable++;
    		return correctCurrentTable(guestsAtTables, currentTable);  // Check the next table isn't full
    	}
    	return currentTable;
    }
}

