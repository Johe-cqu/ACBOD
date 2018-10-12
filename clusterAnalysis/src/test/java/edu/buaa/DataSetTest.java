package edu.buaa;

import org.junit.Test;

import java.util.Date;

public class DataSetTest {

    @Test
    public void testDataSet(){
        DataSet abbData = new DataSet();
        float[] data1={1,2,3,4};
        float[] data2={5,6,7,8};
        //CellData Data1 = new CellData(data1);
       // CellData Data2 = new CellData(data2);
        abbData.addNodeInHead(1,new CellData(data1));
        abbData.addNodeInHead(2,new CellData(data2));
        abbData.addNodeInHead(3,new CellData(data2.clone()));
        //abbData.addNodeInHead(new CellData(data1));
        CellData a = new CellData(data1);

        CellData b =  a.clone();
        System.out.println("");


        abbData.printDataSet();
        DataSet dbbData = abbData.cloneDataSet();

        //copy一个数据集的起始位置
        DataSet bbbData = new DataSet();
        bbbData.copyDataSet(abbData);

        if (!abbData.isEmpty())
            abbData.normalizationDataSet();
        bbbData.printDataSet();


        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        DataSet cbbData=new DataSet();
        /*
        for(cbbData.copyDataSet(abbData);!cbbData.isNullHead();cbbData.nextNode()){
            System.out.println(cbbData.readHeadNodeId());
            float[] tempfloat = cbbData.readHeadNodeData().getData();
            for (int i = 0; i < cbbData.readHeadNodeData().getLen(); i++) {
                System.out.print(tempfloat[i]);//显示链表内各个数字
                System.out.print("  ");
            }
            System.out.println();
        }
        */
        DataSet temp=new DataSet();
        temp.copyToNewDataSet(abbData);

//        DataSet temp=abbData;
//        temp.nextNode();
        System.out.println(10*20+10);
        System.out.println(String.format("%.1f",403555.562));
        byte abc = Byte.parseByte("10");
        boolean adc =Boolean.parseBoolean("true");
        System.out.println(10*20+10);
        System.out.println(String.format("%tr ",new Date()));
    }



}
