package edu.buaa;

import org.junit.Test;

public class CsiDataStructTest {


    //对csiCluster进行测试
    @Test
    public void csiDataStructTest(){

        ///////////////////////////////////////
        /*
        *本次的测试如下：
        * （1）首先建立一个CsiCluster
        * （2）将一个CellData放入数据集中
        * （3）检查点是否按照半径进入各个簇
        * 结果：完成测试
         */
        float[] data1={1,2,3,4};
        float[] data2={5,6,7,8};
        float[] data3={10,12,13,15};
        CsiData oneCsiData = new CsiData();
        oneCsiData.addOneData(1,new CellData(data1));
        oneCsiData.addOneData(2,new CellData(data1.clone()));
        oneCsiData.addOneData(3,new CellData(data1.clone()));
        oneCsiData.addOneData(4,new CellData(data1.clone()));
        CsiData twoCsiData = new CsiData();
        twoCsiData.addOneData(5,new CellData(data2));
        twoCsiData.addOneData(6,new CellData(data2.clone()));
        twoCsiData.addOneData(7,new CellData(data2.clone()));
        twoCsiData.addOneData(8,new CellData(data2.clone()));

        CsiDataStruct csiDataStruct = new CsiDataStruct();
        for(int i=0;i<5;i++) {
            csiDataStruct.putPointToCluster(3*i, new CellData(data3.clone()), 1);
            csiDataStruct.putPointToCluster(3*i+1, new CellData(data2.clone()), 1);
            csiDataStruct.putPointToCluster(3*i+2, new CellData(data1.clone()), 1);
        }
        csiDataStruct.addCsiData(oneCsiData);
        csiDataStruct.addCsiData(twoCsiData);

        csiDataStruct.putPointToCluster(9,new CellData(data3),1);
        System.out.println("");
        ///////////////////////////////////////////////////
        /**
         * 本次的测试如下：
         * （1）根据上面建立的三个簇，分别计算各个簇的离群因子
         * 测试成功
         */
        csiDataStruct.calculateOF();
        System.out.println("");
        /**
         * 本次的测试如下：
         * (1)根据离群因子以及异常点比例，得到异常点
         * 测试完成
         */
        csiDataStruct.setCsiDataAbNormal((float)0.3);
        System.out.println("");
    }

}
