package edu.lifo.operators;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MetisExecutor {
	
	public static void main (String[]args){
		 Runtime r = Runtime.getRuntime();
		 
		        try {
		             /*
		              * Here we are executing the UNIX command ls for directory listing. 
		              * The format returned is the long format which includes file 
		              * information and permissions.
		               */
            String[] command = new String[] { "/usr/local/bin/gpmetis", "graphDir/1505666185963", "3" };


            Process p = r.exec(command);
            
		              InputStream in = p.getInputStream();
		              BufferedInputStream buf = new BufferedInputStream(in);
		              InputStreamReader inread = new InputStreamReader(buf);
		              BufferedReader bufferedreader = new BufferedReader(inread);
		  
		              // Read the ls output
		              String line;
		              while ((line = bufferedreader.readLine()) != null) {
		                  System.out.println(line);
		              }
		              // Check for ls failure
		              try {
		                  if (p.waitFor() != 0) {
		                      System.err.println("exit value = " + p.exitValue());
		                  }
		              } catch (InterruptedException e) {
		                  System.err.println(e);
		              } finally {
		                  // Close the InputStream
		                  bufferedreader.close();
		                  inread.close();
		                  buf.close();
		                  in.close();
		              }
		          } catch (IOException e) {
		              System.err.println(e.getMessage());
		          }
	}

    public static void executeCommand(File file, String k) {

        Runtime r = Runtime.getRuntime();
        try {
            String[] command = new String[] { "/usr/local/bin/gpmetis", file.getAbsolutePath(), k };

            Process p = r.exec(command);
    
            InputStream in = p.getInputStream();
            BufferedInputStream buf = new BufferedInputStream(in);
            InputStreamReader inread = new InputStreamReader(buf);
            BufferedReader bufferedreader = new BufferedReader(inread);

            // Read the ls output
            String line;
            while ((line = bufferedreader.readLine()) != null) {
                System.out.println(line);
            }
            // Check for ls failure
            try {
                if (p.waitFor() != 0) {
                    System.err.println("exit value = " + p.exitValue());
                }
            } catch (InterruptedException e) {
                System.err.println(e);
            } finally {
                // Close the InputStream
                bufferedreader.close();
                inread.close();
                buf.close();
                in.close();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }



}
