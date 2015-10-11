/**
 * We are given n guests to be allocated to m equally sized tables (where n % m = 0) at a banquest.  
 * There are constraint of the form together(i,j) and apart(i,j) where
 * together(i,j) means that guests i and j must sit at the same table and
 * apart(i,j) means that guests i and j must sit at different tables. 
 * By default, guests can sit at any table with any other guest
 */

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.chocosolver.solver.*;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.constraints.*;

public class BanquetPlanner {
    Solver solver;
    int nGuests;
    int mTables;
    int tableSize;

    IntVar[] tables; // An array of table allocation for each guest
    IntVar[] count;  // An array of the counts of guests at each table

    
    // TODO: Outline algorithm here
    BanquetPlanner(String fname) throws IOException {
        try (Scanner sc = new Scanner(new File(fname))) {
            nGuests   = sc.nextInt(); // number of guests
            mTables   = sc.nextInt(); // number of tables
            tableSize = nGuests / mTables;
            solver    = new Solver("banquet planner");

            // Create an bounded array of length nGuests, each location a guest
            // This is bounded instead of enumerated for speed...should it be?
            tables = VariableFactory.boundedArray("tables", nGuests, 
            		0, nGuests, solver); 
            // The count of how many guests are at each table
            count = VariableFactory.boundedArray("count", mTables, 
            		0, tableSize, solver);
            
            while (sc.hasNext()) {
                String s = sc.next();
                int i    = sc.nextInt();
                int j    = sc.nextInt();
                
                // guarantee that two "together" guests have the same table number, and not equal for "apart"
                if (s.equals("together")) {
                	solver.post(IntConstraintFactory.arithm(tables[i], "=", tables[j]));
                } else { // s.equals("apart")
                	solver.post(IntConstraintFactory.arithm(tables[i], "!=", tables[j]));
                }
            }
        }

       
        
        //
        // post constraints to ensure that every table is of size tableSize
        //
        for (int tableNumber = 0; tableNumber < mTables; tableNumber++) {
        	solver.post(ICF.count(tableNumber, tables, count[tableNumber])); // count the guests
            solver.post(ICF.arithm(count[tableNumber], "=", tableSize));     // guarantee that the count is what is should be
        }
        
    }

    boolean solve() {
        return solver.findSolution();
    }

    void result() {
	// print out solution in specified format (see readme.txt)
	// so that results can be verified
	//
    	StringBuffer[] outputLines = new StringBuffer[mTables];
    	for (int tableNumber = 0; tableNumber < mTables; tableNumber++) {
    		outputLines[tableNumber] = new StringBuffer(Integer.toString(tableNumber) + " ");
    	}
    	for (int guestIndex = 0; guestIndex < nGuests; guestIndex++) {
    		int guestTable = tables[guestIndex].getValue();
    		outputLines[guestTable].append(Integer.toString(guestIndex) + " ");
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
}

