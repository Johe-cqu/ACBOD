package edu.buaa;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Mysql {

    public static Connection createMysqlConnect(String databaseName){    //如何返回con对象？
        //声明Connection对象
        Connection con;
        //参数
        String url ="jdbc:mysql://localhost:3306/"+databaseName;
        String user="root";
        String password="123456";
        try{
            //动态加载类
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection(url,user,password);
            if (!con.isClosed()) {
                System.out.println("Succeeded connecting to the Database!");
                return con;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        }finally {
           // System.out.println("数据库连接完成！");
        }
        return null;
    }

    public static DataSet readMysqlData(Connection conn,String tableName){
        DataSet dataSet = new DataSet();
        int id = 0;
        try {
            Statement statement = conn.createStatement();
            //要执行的SQL语句
            String sql = "select * from " + tableName+" order by id desc limit 500";

            //3.ResultSet类，用来存放获取的结果集！！
            ResultSet rs = statement.executeQuery(sql);

            String Data1 = null;
            String Data2 = null;
            String Data3 = null;
            String Data4 = null;
         /*   dataSet.addNodeInHead(++id,new CellData(new float[]{10,10,10,10}));
            dataSet.addNodeInHead(++id,new CellData(new float[]{12,10,11,13}));
            dataSet.addNodeInHead(++id,new CellData(new float[]{13,14,11,15}));
            dataSet.addNodeInHead(++id,new CellData(new float[]{13,11,13,14}));
            dataSet.addNodeInHead(++id,new CellData(new float[]{10,10,10,10}));
            dataSet.addNodeInHead(++id,new CellData(new float[]{12,10,11,13}));
            dataSet.addNodeInHead(++id,new CellData(new float[]{13,14,11,15}));
            dataSet.addNodeInHead(++id,new CellData(new float[]{13,11,13,14}));*/
            while (rs.next()) {
                //获取Data1这列数据
                Data1 = rs.getString("Data1");
                //获取Data1这列数据
                Data2 = rs.getString("Data2");
                //获取Data3这列数据
                Data3 = rs.getString("Data3");
                //获取Data4这列数据
                Data4 = rs.getString("Data4");
                //输出结果
                float[] temp={Float.parseFloat(Data1),Float.parseFloat(Data2),Float.parseFloat(Data3),Float.parseFloat(Data4)};
                dataSet.addNodeInHead(++id,new CellData(temp));
                System.out.println(Data1 + "\t" + Data2+ "\t" + Data3+ "\t" + Data4);
            }
          /*  dataSet.addNodeInHead(++id,new CellData(new float[]{20,21,21,20}));
            dataSet.addNodeInHead(++id,new CellData(new float[]{22,21,23,20}));
            dataSet.addNodeInHead(++id,new CellData(new float[]{22,22,22,22}));
            dataSet.addNodeInHead(++id,new CellData(new float[]{23,23,23,23}));
            dataSet.addNodeInHead(++id,new CellData(new float[]{22,22,23,24}));
            dataSet.addNodeInHead(++id,new CellData(new float[]{20,19,18,17}));
            dataSet.addNodeInHead(++id,new CellData(new float[]{20,19,18,17}));
            dataSet.addNodeInHead(++id,new CellData(new float[]{20,19,18,17}));*/
            rs.close();
            //conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            return dataSet;
        }

    }



}
