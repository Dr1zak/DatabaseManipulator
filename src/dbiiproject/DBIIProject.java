package dbiiproject;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.io.*;
import java.util.Scanner;



public class DBIIProject{
        public static Scanner input;
        
	public static void main(String[] argv) throws SQLException {

                input = new Scanner(System.in);
                Connection connection = null;
                PreparedStatement pstmt = null;
                boolean exit = false; 
                
		System.out.println("-------- Oracle JDBC Connection Testing ------");
                
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
                        // Please edit the following line by replacing username & password
                        // Also, this connection assumes that your are using Oracle Express 11g server and service name
                        // If the project is to be completed in the lab, replace localhost with FF143PC016, and xe with ORCL as you
                        // did in Lab5
                        connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "Salah","admin");
                
            System.out.println("Oracle JDBC Driver Registered!");
                
            System.out.println("______________________________________");
            System.out.println("       DATABASE II PROJECT            ");
            System.out.println("         Group Members ");
            // Maximum 4 please
            System.out.println("   Salahudean Tohmeh  201500027  ");
            System.out.println("______________________________________");

             
            while(exit == false)
	    {	
                System.out.println("");
                System.out.println("<Enter 0 to terminate>");
                System.out.println("<Enter 1 to view the table>");
                System.out.println("<Enter 2 to insert>");
                System.out.println("<Enter 3 to delete>");
                System.out.println("<Enter 4 to update>");
                System.out.println("<Enter 5 to view a student>");
                System.out.println("<Enter 6 to check if ID exists>");
                System.out.println("\n");
                int choice = 0;
                choice = input.nextInt();
                int ID;
                String FNAME,LNAME,GRADE;
                
                switch(choice)
                {
                case(0): //Terminate
                        exit = true;
                        System.out.println(" TERMINATING PROGRAM....");
                    continue;
                case(1): //Display Tables
                        String DISPLAY = "SELECT * FROM ProjectTable ORDER BY ID ASC";
                        pstmt = connection.prepareStatement(DISPLAY );
                        ResultSet QUERY = pstmt.executeQuery(DISPLAY);
                        ResultSetMetaData rsmd = QUERY.getMetaData();
                        showResult(rsmd ,QUERY);
                    continue;
                case(2): //Insert new Record
                        System.out.println("Enter the ID: ");
                        ID=input.nextInt();
                        System.out.println("Enter The First Name: ");
                        FNAME=input.next();
                        System.out.println("Enter The Last Name: ");
                        LNAME=input.next();
                        System.out.println("Enter The Grade: ");
                        GRADE=input.next();
                        
                        String INSERT = "INSERT into ProjectTable values(" + ID + ",'" + FNAME + "','" + LNAME + "','" + GRADE + "')";
                        pstmt = connection.prepareStatement(INSERT);
                        pstmt.executeUpdate(INSERT);
                        System.out.println("Inserted records into the table..."); 
 
                    continue;
                case(3): //Delete Record
                        System.out.println("Enter the ID: ");
                        ID=input.nextInt();
                        boolean check3 = checkID(ID, connection);
                     
                    if(check3 == true){
                        String DELETE = "DELETE FROM ProjectTable WHERE ID = " + ID + ""; //Initialize Query
                        pstmt = connection.prepareStatement(DELETE); //Create Query
                        pstmt.executeQuery(DELETE); //Execute Query
                        System.out.println("Record has been deleted..."); 
                    } 
                    continue;
                case(4): //Update a Record using ID number
                    //Taking neccesary input.
                    System.out.println("Enter the ID of the student you want to modify: ");
                    ID=input.nextInt();
                    boolean check4 = checkID(ID, connection);
                        if(check4 != false){ 
                            System.out.println("Enter the new value of the First Name or press enter if you do not want to update it: ");
                            FNAME=input.next();
                            System.out.println("Enter the new value of the Last Name or press enter if you do not want to update it: ");
                            LNAME=input.next();
                            System.out.println("Enter the new value of the Grade or press enter if you do not want to update it: ");
                            GRADE=input.next();
                        
                    if(pstmt != null){ //IF statement to decide of ID exists.
                        if(FNAME.length() != 0 && LNAME.length() != 0 && GRADE.length() != 0){ //Update if everything was changed.
                            String UPDATE_ALL = "UPDATE PROJECTTABLE SET FNAME=" + "'" + FNAME + "'" + " ,LNAME=" + "'" + LNAME  + "'" +" ,GRADE=" + "'" + GRADE  + "'" + "WHERE ID=" + ID; //Initialize Query
                            pstmt = connection.prepareStatement(UPDATE_ALL); //Create Query
                            pstmt.executeUpdate(); //Execute Query
                            System.out.println("Record modified.");
                        }
                        else if(FNAME.length() == 0 ){ //Update if both LNAME & GRADE were changed.
                            String UPDATE_EXCEPT_FNAME = "UPDATE PROJECTTABLE SET " + "LNAME=" + "'" + LNAME  + "'" +" ,GRADE=" + "'" + GRADE  + "'" + "WHERE ID=" + ID; //Initialize Query
                            pstmt = connection.prepareStatement(UPDATE_EXCEPT_FNAME); //Create Query
                            pstmt.executeUpdate(); //Execute Query
                            System.out.println("Last name & Grade were modified.");
                        }
                        else if(LNAME.length() == 0 ){ //Update if both FNAME & GRADE were changed.
                            String UPDATE_EXCEPT_LNAME = "UPDATE PROJECTTABLE SET FNAME=" + "'" + FNAME + "'" + " ,GRADE=" + "'" + GRADE  + "'" + "WHERE ID=" + ID; //Initialize Query
                            pstmt = connection.prepareStatement(UPDATE_EXCEPT_LNAME); //Create Query
                            pstmt.executeQuery(UPDATE_EXCEPT_LNAME); //Execute Query
                            System.out.println("First name & Grade were modified.");
                        }
                        else if(GRADE.length() == 0){ //Update if both FNAME & LNAME were changed.
                            String UPDATE_EXCEPT_GRADE = "UPDATE PROJECTTABLE SET FNAME=" + "'" + FNAME + "'" + " ,LNAME=" + "'" + LNAME  + "'" + "WHERE ID=" + ID; //Initialize Query
                            pstmt = connection.prepareStatement(UPDATE_EXCEPT_GRADE); //Create Query
                            pstmt.executeQuery(UPDATE_EXCEPT_GRADE); //Execute Query
                            System.out.println("First & Last name were modified.");
                        }
                        else if(FNAME.length() == 0 && LNAME.length() == 0){ //Update if only GRADE was changed.
                            String UPDATE_GRADE = "UPDATE PROJECTTABLE SET GRADE=" + "'" + GRADE  + "'" + "WHERE ID=" + ID; //Initialize Query
                            pstmt = connection.prepareStatement(UPDATE_GRADE); //Create Query
                            pstmt.executeQuery(UPDATE_GRADE); //Execute Query
                            System.out.println("Grade was modified.");
                        }
                        else if(LNAME.length() == 0 && GRADE.length() == 0){ //Update if only FNAME was changed.
                            String UPDATE_FNAME = "UPDATE PROJECTTABLE SET FNAME=" + "'" + FNAME + "'" + "WHERE ID=" + ID; //Initialize Query
                            pstmt = connection.prepareStatement(UPDATE_FNAME); //Create Query
                            pstmt.executeQuery(UPDATE_FNAME); //Execute Query
                            System.out.println("First name was modified.");
                        }
                        else if(FNAME.length() == 0 && GRADE.length() == 0){ //Update if only LNAME was changed.
                            String UPDATE_LNAME = "UPDATE PROJECTTABLE SET LNAME=" + "'" + LNAME  + "'" + "WHERE ID=" + ID; //Initialize Query
                            pstmt = connection.prepareStatement(UPDATE_LNAME); //Create Query
                            pstmt.executeQuery(UPDATE_LNAME); //Execute Query
                            System.out.println("Last name was modified.");
                        }
                        else if(FNAME.length() == 0 && LNAME.length() == 0 && GRADE.length() == 0){ //If nothing was changed.
                            System.out.println("You didn't update anything.");
                        }
                    }     
                }
                continue;
                    case(5):
                        System.out.println("Enter the ID of the student you want to view: ");
                        ID=input.nextInt();
                        boolean check5 = checkID(ID, connection);
                        
                    if (check5 == true){
                        Statement stmt = connection.createStatement();
                        String sql = "SELECT FNAME, LNAME, GRADE FROM ProjectTable WHERE ID="+ID;
                        ResultSet rs = stmt.executeQuery(sql);
                        while(rs.next()){
                                //Retrieve by column name
                                String first = rs.getString("FNAME");
                                String last = rs.getString("LNAME");
                                String grade = rs.getString("GRADE");
                                //Display values
                                System.out.println("");
                                System.out.println("First Name = " + first);
                                System.out.println("Last Name = " + last);
                                System.out.println("Grade = " + grade);
                                System.out.println("");
                        }
                    }
                    continue;
                    case(6):
                            System.out.println("Enter ID");
                            ID=input.nextInt();
                            checkID(ID, connection);
                    continue;
                default:
                        System.out.println("Invalid option. Please try again.");
                } //End of Switch Statement
            } //End of While loop
        } //End of Try
                catch (Exception e) {
			System.out.println("Error! Check output console");
		}
                finally{
                    connection.close();
            }      
        } //End of Class
        static String leftText( String s , int newLen ){  
            while ( s.length() < newLen ){  
                s = s+" ";  
            }  
            return s ;  
        }
        
        static void showResult(ResultSetMetaData RM, ResultSet rs){  
        int TupleCount=0;
        try {
        for(int i = 1; i <= RM.getColumnCount(); i++) 
                       if (i==1)
                       System.out.print(""+RM.getColumnName(i) + "        ");
                       else System.out.print(RM.getColumnName(i) + "     ");
            System.out.println();
            System.out.println("===================================");
            while ( rs.next ()){
                String nextRec = "";
                TupleCount++;
                for ( int x = 1; x <= RM.getColumnCount();  x++ )
                {
                    nextRec += leftText(rs.getString(x),10);
                }
                System.out.println( nextRec );
            } 
         System.out.println( "There are "+TupleCount+" students in the result");
         System.out.println("");
        }
        catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
		}
        } //End of showResult function
        
        public static boolean checkID(int ID, Connection conn) throws SQLException {
        System.out.println("<Verifying ID...>");
        
        Statement stmt = null; // manages prepared statement
        int id = 0;
        boolean check = false;
        // connect to database usernames and query database
        try {
            // query database
            String Statement = "SELECT ID from ProjectTable where ID="+ ID;
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(Statement);
            while (rs.next()) {
                id = rs.getInt("ID");   
            }
            if((ID == id)){
                check=true;
                System.out.println("ID verified.");
                return check;
            }
            else{
               check=false;
               System.out.println("ID does not exist.");
               return check;
            }
        }
        catch (SQLException e) {} //If ID does not exist, print and return false.
        return check;
    } //End of checkID function
} //End of Class
