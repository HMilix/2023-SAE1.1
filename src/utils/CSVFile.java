package utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVFile {
   protected List<List<String>> table;

   public CSVFile(String var1, char var2) {
      this.table = new ArrayList();

      try {
         Scanner var3 = new Scanner(new FileReader(var1));

         while(var3.hasNext()) {
            String var4 = var3.nextLine();
            Scanner var5 = (new Scanner(var4)).useDelimiter("" + var2);
            ArrayList var6 = new ArrayList();

            while(var5.hasNext()) {
               var6.add(var5.next());
            }

            this.table.add(var6);
         }
      } catch (FileNotFoundException var7) {
         var7.printStackTrace();
      }

   }

   public CSVFile(String var1) {
      this(var1, ',');
   }

   public int rowCount() {
      return this.table.size();
   }

   public int columnCount() {
      return this.columnCount(0);
   }

   public int columnCount(int var1) {
      return ((List)this.table.get(var1)).size();
   }

   public String getCell(int var1, int var2) {
      return (String)((List)this.table.get(var1)).get(var2);
   }

   public static void save(String[][] var0, String var1, char var2) throws IOException {
      BufferedWriter var3 = new BufferedWriter(new FileWriter(var1));

      for(int var4 = 0; var4 < var0.length; ++var4) {
         for(int var5 = 0; var5 < var0[0].length - 1; ++var5) {
            var3.write(var0[var4][var5] + var2);
         }

         var3.write(var0[var4][var0[0].length - 1] + "\n");
      }

      var3.close();
   }

   public void dump() {
      for(int var1 = 0; var1 < this.rowCount(); ++var1) {
         for(int var2 = 0; var2 < this.columnCount(); ++var2) {
            PrintStream var10000 = System.out;
            String var10001 = this.getCell(var1, var2);
            var10000.print(var10001 + " ");
         }

         System.out.println();
      }

   }

   public static void main(String[] var0) throws IOException {
      if (var0.length == 0 || var0.length > 2) {
         System.err.println("Usage: java SimpleTable nameOfaCSVfile [separator]");
         System.exit(1);
      }

      char var1 = ',';
      if (var0.length == 2) {
         var1 = var0[1].charAt(0);
      } else {
         System.err.println("No separator provided: using comma by default");
      }

      CSVFile var2 = new CSVFile(var0[0], var1);
      var2.dump();
      PrintStream var10000 = System.out;
      int var10001 = var2.rowCount();
      var10000.println("There are " + var10001 + " lines with " + var2.columnCount() + " columns.");
      String[][] var3 = new String[var2.rowCount()][2];

      for(int var4 = 0; var4 < var2.rowCount(); ++var4) {
         var3[var4][0] = var2.getCell(var4, 0);
         var3[var4][1] = var2.getCell(var4, 1);
      }

      save(var3, "Test.csv", ';');
   }
}
