package edu.buaa;

import org.junit.Test;

public class CsiDataTest {

    @Test
    public void csiDataTest(){
        /*
           本次测试的过程如下：首先构造一个DataSet数据集，然后将数据集中的全部添加进入CsiData中,
           最后将数据的标号从CsiData中读出，再到原始的DataSet数据集中读取原始数据。
         */

        //构造一个DataSet数据集
        DataSet abbData = new DataSet();
        float[] data1={1,2,3,4};
        float[] data2={5,6,7,8};
        float[] data3={1,2,3,4};
        float[] data4={5,6,7,8};
        abbData.addNodeInHead(1,new CellData(data1));
        abbData.addNodeInHead(2,new CellData(data2));
        abbData.addNodeInHead(3,new CellData(data1.clone()));
        abbData.addNodeInHead(4,new CellData(data2.clone()));

        DataSet bbbData = new DataSet();
        bbbData.copyToNewDataSet(abbData);

        DataSet cbbData = new DataSet();
        cbbData.copyDataSet(bbbData);
        bbbData.normalizationDataSet();

        cbbData=bbbData;


        //读取DataSet中的数据，添加进入CsiData之中
        CsiData abbCsi = new CsiData();
      /*
        for(;!cbbData.isNullHead();cbbData.nextNode()){
            //CsiData abbCsi = new CsiData(cbbData.readHeadNodeId(),cbbData.readHeadNodeData());
            abbCsi.addOneData(cbbData.readHeadNodeId(),cbbData.readHeadNodeData());
            System.out.println(cbbData.readHeadNodeId());
            float[] tempfloat = cbbData.readHeadNodeData().getData();
            for (int i = 0; i < cbbData.readHeadNodeData().getLen(); i++) {
                System.out.print(tempfloat[i]);//显示链表内各个数字
                System.out.print("  ");
            }
            System.out.println();
        }
*/
        for(int id:cbbData.getDataSetHashMap().keySet()){
            //CsiData abbCsi = new CsiData(cbbData.readHeadNodeId(),cbbData.readHeadNodeData());
            abbCsi.addOneData(id,cbbData.getDataSetHashMap().get(id));
            System.out.println(id);
            float[] tempfloat = cbbData.getDataSetHashMap().get(id).getData();
            for (int i = 0; i < cbbData.getDataSetHashMap().get(id).getLen(); i++) {
                System.out.print(tempfloat[i]);//显示链表内各个数字
                System.out.print("  ");
            }
            System.out.println();
        }


        System.out.println();

        //从原始数据集中读取原始数据
        System.out.println("%%%%%%%%%%%%%%%%%%%%%");
        CsiData temp=new CsiData();
        for(temp.copyCsiDataHead(abbCsi);!temp.isNullMemberHead();temp.nextMemberHead()){
            int tempMemberId=temp.getHeadMemberId();
            float[] tempfloat = abbData.findData(tempMemberId);
            for (int i = 0; i < tempfloat.length; i++) {
                System.out.print(tempfloat[i]);//显示链表内各个数字
                System.out.print("  ");
            }
            System.out.println();
        }
        CsiData bbbCsi= abbCsi.cloneCsiData();
        System.out.println("");
    }

}
