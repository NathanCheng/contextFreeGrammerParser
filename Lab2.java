// package cs_package;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Lab2 {
    
    public static void main(String[] args) throws IOException {
        
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        // skip first line
        String[] nonTs = br.readLine().split(" ");
        
        
        // read txt file by lines
        String line;
        ArrayList<String> lines = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            lines.add(line);
            // System.out.println(line);
        }
        
        // HashMap stores a terminal symbol and its non-terminals first symbols by a
        // ArrayList
        HashMap<String, ArrayList<String>> hm = new HashMap<>();
        
        // starts from last line
        for (int i = lines.size() - 1; i >= 0; i--) {
            // splits each line into two parts
            String[] splits = lines.get(i).split(" ->");
            ArrayList<String> firsts = new ArrayList<>();
            
            // If this terminal symbol has been read before, then get its already-have first
            // terminal symbols
            if (hm.containsKey(splits[0])) {
                firsts = hm.get(splits[0]);
            }
            
            // This means it is a eps symbol
            if (splits.length == 1) {
                firsts.add("eps");
                hm.put(splits[0], firsts);
            } else {
                
                // if not empty, parse those symbols
                String[] symbols = splits[1].split(" ");
                // get the first symbol, symbols[0] is an empty string
                String first = symbols[1];
                
                // Check this symbol is terminal or not, if it is terminal, look up its
                // already-have non-terminal first symbols and add them to this symbol's
                // non-terminal first symbol arraylist
                if (hm.containsKey(first)) {
                    ArrayList<String> temp = hm.get(first);
                    for (String string : temp) {
                        firsts.add(string);
                    }
                } else {
                    // if this symbol is non-terminal, add it
                    firsts.add(first);
                }
                
                // store those non-terminal first symbols
                hm.put(splits[0], firsts);
            }
            
        }
        
        //print out the result line by line according to the non terminal list read at line 1
        for (String nonT : nonTs) {
            ArrayList<String> al = hm.get(nonT);
            StringBuilder sb = new StringBuilder();
            sb.append(al.get(0));
            for (int i = 1; i < al.size(); i++) {
                sb.append(",").append(al.get(i));
            }
            System.out.println("FIRST(" + nonT + ")={" + sb.toString() + "}");
        }
        //        HashSet<String> hs = new HashSet<>();
        //        for (String aline : lines) {
        //            String[] splits = aline.split(" ->");
        //
        //            if (!hs.contains(splits[0])) {
        //                String record;
        //                ArrayList<String> al = hm.get(splits[0]);
        //                StringBuilder sb = new StringBuilder();
        //                sb.append(al.get(0));
        //                for (int i = 1; i < al.size(); i++) {
        //                    sb.append(",").append(al.get(i));
        //                }
        //                System.out.println("FIRST(" + splits[0] + ")={" + sb.toString() + "}");
        //                hs.add(splits[0]);
        //            }
        //
        //        }
        
    }
    
}

