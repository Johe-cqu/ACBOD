package edu.buaa;

import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MysqlTest {

    @Test
    public void testMysql(){
        Connection con = Mysql.createMysqlConnect("int_test");
        assertNotEquals(null,con);//可以正确连接上数据库
        DataSet dataSet = Mysql.readMysqlData(con,"abbdata");
        DataSet dataSetClone = dataSet.cloneDataSet();
        System.out.println("#######################################");

        /*
        dataSet.normalizationDataSet();
        DataSet samplingDataSet=dataSet.SystematicSampling((float) 1);
        dataSet.getDataSetExDx();

        DataSet test = new DataSet();
        test.copyToNewDataSet(samplingDataSet);//反向拷贝数据集
        CsiDataStruct temp1=DataProcessing.getBestCsiDataStruct(samplingDataSet,(float) 0.05);  //samplingDataSet是反序
        CsiDataStruct temp2=DataProcessing.getBestCsiDataStruct(dataSet,(float) 0.05);           //dataSet是正序
        CsiDataStruct temp3=DataProcessing.getBestCsiDataStruct(test,(float) 0.05);              //test是正序      dataSet与test的聚类结果相同,与samplingDataSet结果不同

        CsiDataStruct temp4 = new CsiDataStruct();
        ArrayList<Integer> temp2AbnomalId =  temp4.getAbnormalDataId(dataSet,temp2.getR(),(float) 0.05);  //与dataSet的异常ID一样

        for (int i: temp2AbnomalId){//打印所有异常数据
           new  CellData(dataSetClone.findData(i)).printCellData();
        }
        */
       // DataProcessing.getAbnormalDataInDataSet(dataSet,(float)0.05,(float)0.05);
        DataProcessing.getAbnormalDataInDataSetForward(dataSet,(float)0.05,(float)0.05);
        //System.out.println(dataSet.toString());
        System.out.println("");
    }

}
