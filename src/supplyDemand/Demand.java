/**
 * 
 */
package supplyDemand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import au.com.bytecode.opencsv.CSVReader;

import com.google.common.collect.HashBasedTable;

/**
 * @author Elaine
 */
public class Demand {

	/**
	 * @param args
	 */

	HashBasedTable<String, DateTime, Integer> demandByDateCity = HashBasedTable.create();

	public Demand(String filename) {
		File f = new File(filename);

		BufferedReader read;
		try {
			read = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
			CSVReader csvr = new CSVReader(read);
			List<String[]> fileEntries;
			try {
				fileEntries = csvr.readAll();

				/**
				 * 2011-03-31,23:59:59,NYC,UIO,1,2,NYC,UIO,2011-05-16,,UIO,NYC,2011-07-27,,,,,,,,,,,,,,,,,,,,,
				 * ,,,,,,,,,,,,,,,,, [0] search date [1] search time [2] origin airport [3] dest airport [4]
				 * isRoundtrip = 1 [5] number of segments [6] Seg1Departure [7] Seg1Arrival [8] Seg1Date
				 * (YYYY-MM-DD) [9] Seg1Carrier [10] Seg1BookingCode
				 */
				for (String[] strArray: fileEntries) {
					// filter data first - April only
					if (strArray[0].contains("Amadeus")) {
						continue;
					} else {
						String destination = strArray[3];

						if(strArray[8].equals("")){
							continue;
						}else{
							DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
							DateTime dt = formatter.parseDateTime(strArray[8]);
							if (dt.getMonthOfYear() == 4) {
								incrementDemand(destination, dt);
							}
						}

					}

				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void incrementDemand(String destination, DateTime dt) {
		// TODO Auto-generated method stub
		if (!demandByDateCity.containsRow(destination)) {
			demandByDateCity.put(destination, dt, 0);
		}

		if (!demandByDateCity.contains(destination, dt)) {
			demandByDateCity.put(destination, dt, 0);
		}

		demandByDateCity.put(destination, dt, demandByDateCity.get(destination, dt) + 1);

	}

	public String toString() {
		ArrayList<DateTime> dateHrStrings = new ArrayList<DateTime>();
		dateHrStrings.addAll(demandByDateCity.columnKeySet());
		Collections.sort(dateHrStrings);
		// System.out.println("Number of DateHr Strings: " + dateHrStrings.size());
		ArrayList<String> destinations = new ArrayList<String>();
		destinations.addAll(demandByDateCity.rowKeySet());
		Collections.sort(destinations);
		// System.out.println("Number of Departments: " + depts.size());
		// System.out.println(depts.toString());
		String str = "";
		str += "Airport, Day, Number of Queries\n";
		for (int i = 0; i < dateHrStrings.size(); i++) {
			DateTime dateHr = dateHrStrings.get(i);
			for (int j = 0; j < destinations.size(); j++) {
				String dest = destinations.get(j);
				if (demandByDateCity.contains(dest, dateHr)) {
					str += dest + "," + dateHr.toString("MM-dd-YYYY") + "," + demandByDateCity.get(dest, dateHr) + "\n";
				} else {
					str += dest + "," + dateHr.toString("MM-dd-YYYY") + ",0\n";
				}
			}
		}
		return str;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filename = "/Users/Elaine/Documents/Hackathon/data/amadeus/short50K_20110401.csv";

		Demand aprSearches = new Demand(filename);

		//System.out.println(aprSearches.toString());
		
        FileWriter fOut;
		try {
			fOut = new FileWriter("/Users/Elaine/Documents/Hackathon/outputs/apr_demand.csv");
	        BufferedWriter bw = new BufferedWriter(fOut);
	        bw.write(aprSearches.toString());
	        bw.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
