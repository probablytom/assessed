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
    int tableCapacity;

    int[] guestsAtTables; // An array of table allocation for each guest
    IntVar[] count;  // An array of the counts of guests at each table
    HashMap<Integer, IntVar> constrainedGuests;
    

    //TODO: Comments
    //TODO: Clean up code and delete unnecesary variables
    //TODO: Write report
    // TODO: Outline algorithm here
    /*
     * 
     * The algorithm:
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
        IntVar[] tableArray = createArrayFromHashMap(constrainedGuests);
        int[] values = IntStream.rangeClosed(0,mTables-1).toArray();
        IntVar[] OCC = VF.enumeratedArray("occurences", mTables, 0, tableSize, solver);
        
        solver.post(ICF.global_cardinality(tableArray, values, OCC, false));
        
        
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
        guestsAtTables = new int[mTables];  
        Integer guestID;
        int tableNumber;
        
        int currentTable = 0;
        
    	for (int table = 0; table < mTables; table++) {
    		outputLines[table] = new StringBuffer(Integer.toString(table) + " ");
    	}
    	
    	
    	for (Entry<Integer, IntVar> guestDetails : constrainedGuests.entrySet()) {
    		
    		// for readability, convert entry to variables
    		guestID = guestDetails.getKey();
    		tableNumber = guestDetails.getValue().getValue();
    		
    		outputLines[tableNumber].append(guestID.toString() + " ");
    		guestsAtTables[tableNumber]++; 
    	}
    	
    	
    	for (Integer guestIndex = 0; guestIndex < nGuests; guestIndex++) {
    		
    		// Make sure we don't process a guest twice
    		if (!constrainedGuests.containsKey(guestIndex)) {
    			currentTable = correctCurrentTable(guestsAtTables, currentTable);
    			outputLines[currentTable].append(guestIndex.toString() + " ");
    			guestsAtTables[currentTable]++;
    		}
    	}
    	
    	
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
    
    
    // helper function to pretty up the above code a little. 
    public IntVar[] createArrayFromHashMap(HashMap<Integer, IntVar> originalHashmap) {
    	return originalHashmap.values().toArray(VF.integerArray("constrained guests array", constrainedGuests.size(), 0,  mTables-1, solver));
    }
    
    // Helper function to make sure we're on the right table to add a guest to in the print() statement
    public int correctCurrentTable(int[] guestsAtTables, int currentTable) {
        tableCapacity = tableSize - 1;
    	if (guestsAtTables[currentTable] == tableSize) {
    		currentTable++;
    		return correctCurrentTable(guestsAtTables, currentTable); // there may be a table in guestsAtTables full of constrained guests, so check we haven't reached capacity at the next table
    	}
    	return currentTable;
    }
}

