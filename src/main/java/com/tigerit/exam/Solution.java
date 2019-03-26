// package exam;
package com.tigerit.exam;

// import static exam.IO.*;
import static com.tigerit.exam.IO.*;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Collections;
import java.util.Comparator;
/**
 * All of your application logic should be placed inside this class.
 * Remember we will load your application from our custom container.
 * You may add private method inside this class but, make sure your
 * application's execution points start from inside run method.
 */
public class Solution implements Runnable {
   Scanner sc = new Scanner(System.in);
    
    
    
    // Scanner sc;
    // public Solution() throws FileNotFoundException {
    //     this.sc = new Scanner(new File("input.txt"));
    // }

    class Database {
        private ArrayList tableNames = new ArrayList<String>();
        private Hashtable<String, Integer> tableIDs = new Hashtable<String, Integer>();
        private ArrayList<Table> tables = new ArrayList<Table>();

        private int tableIDCount = 0;

        Database() {
        }

        public void createTable(String tableName, ArrayList<String> columnNames) {
            tableNames.add(tableName);
            tableIDs.put(tableName, tableIDCount++);
            tables.add(new Table(tableName, columnNames));
        }
        
        public void addEnitity(String tableName, ArrayList<Integer> entity) {
            int tableID = tableIDs.get(tableName);
            tables.get(tableID).addEntity(entity);
        }
        
        public Table join(String table1, String attr1, String table2, String attr2) {
            /*
            Hashtable<Integer, Boolean> take1 = new Hashtable<Integer, Boolean>();
            Hashtable<Integer, Boolean> take2 = new Hashtable<Integer, Boolean>();
            
            ArrayList<String> attrList1 = new ArrayList<String>();
            ArrayList<String> attrList2 = new ArrayList<String>();
            
            String[] col = select.split(" ");
            for(int i=1; i<col.length; i++) {
                String[] chk = col[i].split(",\\.");
                if(chk[0].compareTo(table1) == 0) attrList1.add(chk[1]);
                else                              attrList2.add(chk[1]);
                //take.put( col[i].split("\\.,")[1]  , Boolean.TRUE);
            }
            */
            int table1ID = tableIDs.get(table1), table2ID = tableIDs.get(table2);
            ArrayList<String> columnNames = new ArrayList<String>();
            for(int i=0; i<tables.get(table1ID).getColumnNames().size(); i++) {
                columnNames.add(table1 + "." + tables.get(table1ID).getColumnNames().get(i));
            }
            for(int i=0; i<tables.get(table2ID).getColumnNames().size(); i++) {
                columnNames.add(table2 + "." + tables.get(table2ID).getColumnNames().get(i));
            }
            /*
            columnNames = (ArrayList<String>) tables.get(table1ID).getColumnNames().clone();
            ArrayList<String> secondTableColumnNames = tables.get(table2ID).getColumnNames();
            //System.out.println(columnNames.toString());
            //System.out.println(secondTableColumnNames.toString());
            columnNames.addAll(secondTableColumnNames);
            
            */
            
            Table ans = new Table("answer ", columnNames );
            
            int attr1ID = tables.get(table1ID).getAttributeID(attr1);
            int attr2ID = tables.get(table2ID).getAttributeID(attr2);
            
            for(int i=0; i<tables.get(table1ID).getEntityCount(); i++) {
                ArrayList<Integer> entity = (ArrayList<Integer>) tables.get(table1ID).getEntity(i).clone();
                int val1 = tables.get(table1ID).getEntityAttribute(i, attr1ID);
                
                for(int j=0; j<tables.get(table2ID).getEntityCount(); j++) {
                    int val2 = tables.get(table2ID).getEntityAttribute(j, attr2ID);
                    
                    if(val1 == val2) {
                        ArrayList<Integer> newEntity = entity;
                        newEntity.addAll(tables.get(table2ID).getEntity(j));
                        
                        ans.addEntity(newEntity);
                    }
                    
                }
            }
            
            return ans;
        }

    }

    class Table {
        private String tableName;
        private ArrayList<String> columnNames = new ArrayList<String>();
        private Hashtable<String, Integer> columnID = new Hashtable<String, Integer>();
        private ArrayList<ArrayList<Integer>> entities = new ArrayList<ArrayList<Integer>>();

        public Table(String tableName, ArrayList<String> columnNames) {
            this.tableName = tableName;
            this.columnNames = columnNames;
            for(int i=0; i<columnNames.size(); i++) columnID.put(columnNames.get(i), i);
        }
        
        public ArrayList<String> getColumnNames() {
            return columnNames;
        }
        
        public int getAttributeID(String attr) {
            return columnID.get(attr);
        }
        
        public void addEntity(ArrayList<Integer> entity) {
            entities.add(entity); /*check validity and generate exception here */
        }
        
        public int getEntityCount() {
            return entities.size();
        }
        public ArrayList<Integer> getEntity(int index) {
            return entities.get(index);
        }
        public int getEntityAttribute(int index, int attrIndex) {
            return entities.get(index).get(attrIndex);
        }
        
        void print() {
            for(int i=0; i<columnNames.size(); i++) {
                if(i != 0) System.out.print(" ");
                System.out.print(columnNames.get(i).split("\\.")[1]);
            }
            System.out.println("");
            
            for(int i=0; i<entities.size(); i++) {
                for(int j=0; j<entities.get(i).size(); j++) {
                    if(j != 0) System.out.print(" ");
                    System.out.print(entities.get(i).get(j));
                }
                System.out.println("");
            }
            
        }
        
        void print(ArrayList<String> attrList) {
            
            Collections.sort(entities, new Comparator<ArrayList<Integer>>() {
                @Override
                public int compare(ArrayList<Integer> t, ArrayList<Integer> t1) {
                    for(int i=0; i<t.size(); i++) {
                        if(t.get(i) != t1.get(i)) return t.get(i).compareTo(t1.get(i));
                    }
                    return 0;
                }
                
            });
            
            if(attrList.isEmpty()) {
                print();
                return;
            }
            
            for(int i=0; i<attrList.size(); i++) {
                if(i != 0) System.out.print(" ");
                System.out.print( attrList.get(i).split("\\.")[1] );
            }
            System.out.println();
            
            ArrayList<Integer> order = new ArrayList<Integer>();
            for(int i=0; i<attrList.size(); i++) {
                for(int j=0; j<columnNames.size(); j++) {
                    if(attrList.get(i).compareTo(columnNames.get(j)) == 0) {
                        order.add(j);
                        break;
                    }
                }
            } 
            
            for(int i=0; i<entities.size(); i++) {
                for(int j=0; j<order.size(); j++) {
                    if(j != 0) System.out.print(" ");
                    System.out.print(entities.get(i).get( order.get(j) ));
                }
                System.out.println("");
            }
            
        }
        
    }

    @Override
    public void run() {
        int T;
        T = sc.nextInt();

        for(int t=1; t<=T; t++) {
            Database database = new Database();
            int tableNumber = sc.nextInt();

            for(int i=0; i<tableNumber; i++) {
                String tableName = sc.next();
                int columnNumber = sc.nextInt();
                int entityNumber = sc.nextInt();

                ArrayList<String> columnNames = new ArrayList<String>();
                for(int j=0; j<columnNumber; j++) columnNames.add(sc.next());
                
                database.createTable(tableName, columnNames);
                //System.out.println("Table " + i + " created!");
                
                for(int j=0; j<entityNumber; j++) {
                    ArrayList<Integer> entity = new ArrayList<Integer>();
                    for(int k=0; k<columnNumber; k++) entity.add(sc.nextInt());
                    //System.out.println("Entity " + j + " input taken");
                    database.addEnitity(tableName, entity);
                }
            }

            int queryNumber = Integer.parseInt(sc.next());
            sc.nextLine();
            //System.out.println("QQ " + queryNumber);
            
            System.out.println("Test: " + t);
            
            while(queryNumber-- > 0) {
                boolean all = false;
                String cmd;
                
                String select = sc.nextLine();

                
                //PARSING FIRST TABLE NAME
                cmd = sc.nextLine();
                String[] line2 = cmd.split(" ");
                String table1 = line2[1]; //line2[0] := "FROM"
                String alias1 = null;
                if(line2.length == 3) alias1 = line2[2];
                
                //PARSING SECOND TABLE NAME
                cmd = sc.nextLine();
                String[] line3 = cmd.split(" ");
                String table2 = line3[1]; //line3[0] := "JOIN"
                String alias2 = null;
                if(line3.length == 3) alias2 = line3[2];
                
                //PARSING "ON" CONDITION := ON ta.column_a0 = tb.column_b0
                cmd = sc.nextLine();
                String[] line4 = cmd.split(" ");
                String qtable1 = line4[1].split("\\.")[0];
                String qtable2 = line4[3].split("\\.")[0];
                String attr1 = line4[1].split("\\.")[1];
                String attr2 = line4[3].split("\\.")[1];
                
                //System.out.println("JOIN " + table1 + " " + table2 + " " + qtable1 + " " + qtable2 + " " + attr1 + " " + attr2);
                
                
                ArrayList<String>attrList = new ArrayList<String>();
                //ArrayList<Integer>tableIDs = new ArrayList<Integer>();
                
                String[] line1 = select.split(" ");
                
                if(line1[1].compareTo("*") != 0) {
                    //System.out.println("Jhamela");
                    for(int i=1; i<line1.length; i++) {
                        String[] tmp = line1[i].split(",|\\.");

                        if(tmp[0].compareTo(qtable1) == 0) attrList.add(table1 + "." + tmp[1]);
                        else                               attrList.add(table2 + "." + tmp[1]);
                    }
                }
                
                
                Table ans = null;
//                System.out.println("a " + qtable1 + " a" );
//                System.out.println("a " +  table1 + " a" );
                if(qtable1.compareTo(table1) == 0)      ans = database.join(table1, attr1, table2, attr2);
                else if(qtable1.compareTo(table2) == 0) ans = database.join(table1, attr2, table2, attr1);
                else if(qtable1.compareTo(alias1) == 0) ans = database.join(table1, attr1, table2, attr2);
                else if(qtable1.compareTo(alias2) == 0) ans = database.join(table1, attr2, table2, attr1);
                
                //System.out.println("JOIN " + table1 + " and " + table2);
                //System.out.println("ON " + firstTableAttribute + " and " + secondTableAttribute);
                
                ans.print(attrList);
                System.out.println();
                
                sc.nextLine();
            }
        }

    }
}
