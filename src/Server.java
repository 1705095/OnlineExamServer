import Connectivity.ConnectionClassTeacher;
import Connectivity.QuestionDBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import javax.security.auth.callback.TextOutputCallback;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.application.Application.launch;


public class Server {




    static  String QuestionName;
    static String QuestionFileName;
    static String AnswerFileName;
    static double count;
    static int CorrectAns,WrongAns;
    static String name;
    static String time;
    static  int TimeCount;

    int port;
    ServerSocket server=null;
    Socket client=null;
    ExecutorService pool = null;
    int clientcount=0;



    public static void main(String[] args) throws IOException {
        Server serverobj=new Server(5000);
        serverobj.startServer();



    }

    Server(int port){
        this.port=port;
        pool = Executors.newFixedThreadPool(100);
    }

    public void startServer() throws IOException {

        server=new ServerSocket(5000);
        System.out.println("Server Booted");
        //text.appendText("Server Booted");


        System.out.println("Any client can stop the server by sending -1");
        while(true)
        {
            client=server.accept();
            clientcount++;
            ServerThread runnable= new ServerThread(client,clientcount,this);
            pool.execute(runnable);
        }

    }

    private static class ServerThread implements Runnable {

        Server server=null;
        Socket client=null;
        BufferedReader cin;
        PrintStream cout;
        Scanner sc=new Scanner(System.in);
        int id;
        String s;

        ServerThread(Socket client, int count ,Server server ) throws IOException {

            this.client=client;
            this.server=server;
            this.id=count;
            System.out.println("Connection "+id+" established with client "+client);

            cin=new BufferedReader(new InputStreamReader(client.getInputStream()));
            cout=new PrintStream(client.getOutputStream());

        }

        @Override
        public void run() {
            int x=1;
            try{
                while(true){
                    s=cin.readLine();

                  //  System. out.print("Client("+id+") :"+s+"\n");

                    //kaj kore

                    if(s.equals("login"))
                    {
                        PrintWriter okOUT=new PrintWriter(client.getOutputStream());
                        okOUT.println("OKlogin");
                        okOUT.flush();
                        InputStreamReader IN = new InputStreamReader(client.getInputStream());
                        BufferedReader ebf = new BufferedReader(IN);
                        String outtxt = ebf.readLine();

                        //System.out.println(outtxt);

                        BufferedReader Reader = new BufferedReader(new FileReader("C:\\Users\\USER\\IdeaProjects\\MultipleClientServer1\\src\\test.txt"));
                        String string = Reader.readLine();
                        int log = 0;

                        while (string != null) {
                            if (outtxt.equals(string)) {
                                PrintWriter out = new PrintWriter(client.getOutputStream());
                                out.println("found");// sending one output to client
                                out.flush();
                                log = 1;
                                break;
                            } else {
                                string = Reader.readLine();
                            }
                        }
                        if (log == 0) {
                            PrintWriter error = new PrintWriter(client.getOutputStream());
                            error.println("NotFound");
                            error.flush();
                        }


                    }

                    // kaj kore

                    else if(s.equals("Reg"))
                    {
                        PrintWriter okOUT=new PrintWriter(client.getOutputStream());
                        okOUT.println("okRegister");
                        okOUT.flush();


                        InputStreamReader in=new InputStreamReader(client.getInputStream());
                        BufferedReader bufferedReader=new BufferedReader(in);
                        String name=bufferedReader.readLine();
                        String email=bufferedReader.readLine();
                        String password=bufferedReader.readLine();
                        String DoB=bufferedReader.readLine();
                        String stat=bufferedReader.readLine();
                        System.out.println(name);
                        System.out.println(email);
                        System.out.println(password);
                        System.out.println(DoB);
                        System.out.println(stat);
                        String Cryptic=stat+email+password;


                        if (stat.equals("Teacher")) {
                            FileWriter fileWriter = new FileWriter("C:\\Users\\USER\\IdeaProjects\\MultipleClientServer1\\src\\test.txt",true);
                            PrintWriter printWriter = new PrintWriter(fileWriter);
                            printWriter.append("\n"+Cryptic);
                            printWriter.close();
                            ConnectionClassTeacher connectionClass = new ConnectionClassTeacher();
                            Connection connection = connectionClass.getConnection();
                            String sql = "INSERT INTO TEACHER VALUES('" + name + "','" + email + "','" + DoB + "','" + stat + "')";
                            Statement statement = connection.createStatement();
                            statement.executeUpdate(sql);
                        }


                        else if (stat.equals("Student"))
                        {
                            FileWriter fileWriter = new FileWriter("C:\\Users\\USER\\IdeaProjects\\MultipleClientServer1\\src\\test.txt",true);
                            PrintWriter printWriter = new PrintWriter(fileWriter);
                            printWriter.append("\n"+Cryptic);
                            printWriter.close();
                            ConnectionClassTeacher connectionClass = new ConnectionClassTeacher();
                            Connection connection = connectionClass.getConnection();
                            String sql = "INSERT INTO STUDENT VALUES('" + name + "','" + email + "','" + DoB + "','" + stat + "')";
                            Statement statement = connection.createStatement();
                            statement.executeUpdate(sql);

                        }

                    }

                    // majhe moddhe kaj kore debug korte hobe
                    else if (s.equals("QuesData"))
                    {
                        PrintWriter Confirm=new PrintWriter(client.getOutputStream());
                        Confirm.println("OKQuesData");
                        Confirm.flush();

                        InputStreamReader Qname=new InputStreamReader(client.getInputStream());
                        BufferedReader name=new BufferedReader(Qname);
                        QuestionName=name.readLine();
                        System.out.println(QuestionName);


                        if (QuestionName.equals("QuesData"))
                        {
                           QuestionName=name.readLine();
                           System.out.println(QuestionName);

                        }


                            FileWriter fileWriter = new FileWriter("C:\\Users\\USER\\IdeaProjects\\MultipleClientServer1\\src\\QuestionName.txt", true);
                            PrintWriter printWriter = new PrintWriter(fileWriter);
                            printWriter.append("\n" + QuestionName);
                            fileWriter.close();

                       /* questionArray[numQ]=QuestionName;
                        numQ++;*/

                            String QFile = "C:\\\\Users\\\\USER\\\\IdeaProjects\\\\MultipleClientServer1\\\\src\\\\Question\\\\" + QuestionName + ".txt";
                            String Afile = "C:\\\\Users\\\\USER\\\\IdeaProjects\\\\MultipleClientServer1\\\\src\\\\Answer\\\\" + QuestionName + "Ans.txt";
                            System.out.println(QFile);
                            System.out.println(Afile);


                            try {
                                File file = new File(QFile);
                                boolean fvar = file.createNewFile();
                                if (fvar) {
                                    System.out.println("File has been created successfully");
                                } else {
                                    System.out.println("File already present at the specified location");
                                }
                            } catch (IOException e) {
                                System.out.println("Exception Occurred:");
                                e.printStackTrace();
                            }
                            try {
                                File file = new File(Afile);
                                boolean fvar = file.createNewFile();
                                if (fvar) {
                                    System.out.println("File has been created successfully");
                                } else {
                                    System.out.println("File already present at the specified location");
                                }
                            } catch (IOException e) {
                                System.out.println("Exception Occurred:");
                                e.printStackTrace();
                            }



                    }

                    //kaj korena

                   else if(s.equals("SetQues"))
                    {


                        String QFile="C:\\\\Users\\\\USER\\\\IdeaProjects\\\\MultipleClientServer1\\\\src\\\\Question\\\\"+QuestionName+".txt";
                        String Afile="C:\\\\Users\\\\USER\\\\IdeaProjects\\\\MultipleClientServer1\\\\src\\\\Answer\\\\"+QuestionName+"Ans.txt";
                        System.out.println("IN QUESTION::"+QFile);



                        FileWriter quesWriter=new FileWriter(QFile,true);
                        FileWriter ansWriter=new FileWriter(Afile,true);




                        InputStreamReader ques=new InputStreamReader(client.getInputStream());
                        BufferedReader ansget=new BufferedReader(ques);
                        String num=ansget.readLine();
                        String Q=ansget.readLine();
                        String A= ansget.readLine();
                        String B=ansget.readLine();
                        String C=ansget.readLine();
                        String D=ansget.readLine();
                        String CA=ansget.readLine();

                        PrintWriter printWriter = new PrintWriter(quesWriter);
                        PrintWriter answrite=new PrintWriter(ansWriter);
                        printWriter.append("\n"+num+".");

                        printWriter.append(Q);

                        printWriter.append("\n"+"a. "+A);

                        printWriter.append("\n"+"b. "+B);

                        printWriter.append("\n"+"c. "+C);

                        printWriter.append("\n"+"d. "+D);

                        answrite.append("\n" +CA);
                        printWriter.close();
                        answrite.close();


                    }

                    //works perfectly

                    else if (s.equals("Notice"))
                    {

                        FileWriter fileWriter=new FileWriter("C:\\Users\\USER\\IdeaProjects\\MultipleClientServer1\\src\\notice.txt",true);


                        InputStreamReader IN = new InputStreamReader(client.getInputStream());
                        BufferedReader ebf = new BufferedReader(IN);
                        String outtxt = ebf.readLine();

                        Date date = new Date();
                        String strDateFormat = "hh:mm:ss a";
                        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                        String formattedDate= dateFormat.format(date);


                        PrintWriter printWriter=new PrintWriter(fileWriter);
                        printWriter.append("\n"+"["+formattedDate+"]"+": " +outtxt);
                        fileWriter.close();



                    }

                    //works perfectly

                    else  if(s.equals("SeeNotice"))
                    {
                        File file=new File("C:\\Users\\USER\\IdeaProjects\\MultipleClientServer1\\src\\notice.txt");

                        BufferedReader reader=new BufferedReader(new FileReader(file));

                        String st;
                        PrintWriter printWriter=new PrintWriter(client.getOutputStream());

                        while (true)
                        {
                            st=reader.readLine();
                            if(st!=null)
                            {
                                printWriter.println(st);
                                printWriter.flush();
                            }
                            else
                            {
                                printWriter.println("null");
                                printWriter.flush();
                                break;
                            }
                        }

                    }

                    ///works perfectly


                    else if (s.equals("SeeQuesBeforeAns"))
                    {
                        PrintWriter okOUT=new PrintWriter(client.getOutputStream());
                        okOUT.println("OKSEE");
                        okOUT.flush();

                        File file =new File("C:\\Users\\USER\\IdeaProjects\\MultipleClientServer1\\src\\QuestionName.txt");
                        BufferedReader reader=new BufferedReader(new FileReader(file));

                        String st;
                        PrintWriter printWriter=new PrintWriter(client.getOutputStream());

                        while (true)
                        {
                            st=reader.readLine();
                            if (st!=null)
                            {
                                printWriter.println(st);
                                printWriter.flush();
                            }
                            else
                            {
                                printWriter.println("null");
                                printWriter.flush();
                                break;
                            }
                        }
                    }

                    //perfecto

                    else if (s.equals("SeeQuestion"))
                    {



                        String QFile="C:\\\\Users\\\\USER\\\\IdeaProjects\\\\MultipleClientServer1\\\\src\\\\Question\\\\"+name+".txt";
                        File file=new File(QFile);

                        BufferedReader reader=new BufferedReader(new FileReader(file));
                        String st;
                        PrintWriter printWriter=new PrintWriter(client.getOutputStream());

                        while (true)
                        {
                            st=reader.readLine();
                            if(st!=null)
                            {
                                printWriter.println(st);
                                printWriter.flush();
                            }
                            else
                            {
                                printWriter.println("null");
                                printWriter.flush();
                                break;
                            }
                        }



                    }

                    //kaj kore

                    else if (s.equals("GoAnswer"))
                    {
                        InputStreamReader IN = new InputStreamReader(client.getInputStream());
                        BufferedReader ebf = new BufferedReader(IN);
                        name=ebf.readLine();
                        System.out.println(name);


                       BufferedReader Reader = new BufferedReader(new FileReader("C:\\Users\\USER\\IdeaProjects\\MultipleClientServer1\\src\\QuestionName.txt"));
                        String string = Reader.readLine();
                        int log = 0;

                        while (string != null) {
                            if (name.equals(string)) {
                                System.out.println("found");
                                PrintWriter out = new PrintWriter(client.getOutputStream());
                                out.println("found");// sending one output to client
                                out.flush();
                                log = 1;
                                break;
                            } else {
                                string = Reader.readLine();
                            }
                        }
                        if (log == 0) {
                            PrintWriter error = new PrintWriter(client.getOutputStream());
                            error.println("NotFound");
                            error.flush();
                        }


                    }

                    //kaj kore

                    else if (s.equals("submitAns"))
                    {
                         count=0;
                        InputStreamReader IN = new InputStreamReader(client.getInputStream());
                        BufferedReader ebf = new BufferedReader(IN);
                        String outtxt = ebf.readLine();
                        System.out.println(outtxt);
                       // File file=new File("C:\\Users\\USER\\IdeaProjects\\MultipleClientServer1\\src\\ans.txt");

                        String Afile="C:\\\\Users\\\\USER\\\\IdeaProjects\\\\MultipleClientServer1\\\\src\\\\Answer\\\\"+name+"Ans.txt";
                        File file=new File(Afile);
                        BufferedReader reader=new BufferedReader(new FileReader(file));
                        String str=reader.readLine();
                        System.out.println(str);
                        count=0;
                        CorrectAns=0;
                        WrongAns=0;

                        for (int i=0;i<20;i++)
                        {
                            if (outtxt.charAt(i)==str.charAt(i))
                            {
                                count++;
                                CorrectAns++;
                            }
                            else if ((outtxt.charAt(i)!=str.charAt(i))&& outtxt.charAt(i)!='0')
                            {
                                count=count-.25;
                                WrongAns++;
                            }
                        }
                        System.out.println(count);


                    }
                    else if (s.equals("SeeMarks"))
                    {
                        //System.out.println("Seeing marks");
                        System.out.println(count);
                        System.out.println(CorrectAns);
                        System.out.println(WrongAns);

                        PrintWriter printWriter=new PrintWriter(client.getOutputStream());
                        printWriter.println(count);
                        printWriter.flush();
                        printWriter.println(CorrectAns);
                        printWriter.flush();
                        printWriter.println(WrongAns);
                        printWriter.flush();

                    }



                    else if (s.equalsIgnoreCase("logout"))
                    {
                        cout.println("Logging out");
                        x=0;//todo::look here to resolve logout error
                        System.out.println("Connection ended by server");
                        break;
                    }
                    cout.println(s);
                }


                cin.close();
                client.close();
                cout.close();
                if(x==0) {
                    System.out.println( "Server cleaning up." );
                    System.exit(0);
                }
            }
            catch(IOException | SQLException ex){
                System.out.println("Error : "+ex);
            }


        }
    }

}