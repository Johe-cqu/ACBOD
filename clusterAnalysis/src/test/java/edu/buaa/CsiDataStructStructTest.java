package edu.buaa;

import org.junit.Test;

public class CsiDataStructStructTest {

    @Test
    public void csiDataStructStrut(){
        /**
         * 该测试的目的是根据beta和数据集，将数据集变成一个分簇结果（CsiDataStructStruct）
         * 已测试成功
         */
       // float[] aData ={1,2,3,4};
       // float[] bData ={5,6,7,8};
       // float[] cData ={6,7,8,9};
        DataSet dataSet = new DataSet();
        CsiDataStructStruct csiDataStructStruct = new CsiDataStructStruct();
        for(int i=0 ;i<100;i++) {
            float[] aData ={1*i,2*i,3*i,4*i};
            float[] bData ={5*i,6*i,7*i,8*i};
            float[] cData ={6*i,7*i,8*i,9*i};
            dataSet.addNodeInHead(i*3, new CellData(aData.clone()));
            dataSet.addNodeInHead(i*3+1, new CellData(bData.clone()));
            dataSet.addNodeInHead(i*3+2, new CellData(cData.clone()));
        }
        dataSet.getDataSetExDx();
        for (int beta=25;beta>-25;beta--){
            csiDataStructStruct.putDataSetToStructStruct(dataSet,(float) beta/100);//建立数据
        }
        //csiDataStructStruct.putDataSetToStructStruct(dataSet,(float) 0.1);
        System.out.println("");

        /**
         *
         */

    }

}
