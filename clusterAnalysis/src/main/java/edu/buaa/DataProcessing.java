package edu.buaa;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DataProcessing {


    /**
     * 根据抽样数据集，建立CsiDataStructStruct，返回最优的分簇结果
     * @param dataSet  抽样数据集
     * @param ratio 异常数据比例
     * @return 最优CsiDataStruct
     * 已测试
     */
    public static CsiDataStruct getBestCsiDataStruct(DataSet dataSet,float ratio){
        float beta;
        CsiDataStructStruct csiDataStructStruct = new CsiDataStructStruct();
        for (beta=(float) 0.25;beta>-0.25;beta-=0.01){
            csiDataStructStruct.putDataSetToStructStruct(dataSet.cloneDataSet(),beta);//建立数据集
        }
        //找到最小DB
        CsiDataStruct minDbStruct = csiDataStructStruct.getMinDbStruct(ratio);
        return minDbStruct;
    }

    /**
     * 输入数据集，打印异常数据
     * @param dataSet 数据集
     * @param DataSetRatio 抽样比例（等间隔抽样）
     * @param abnormalDataRatio 异常数据比例
     */
    public static void getAbnormalDataInDataSet(@NotNull DataSet dataSet, float DataSetRatio, float abnormalDataRatio){
        DataSet dataSetClone = dataSet.cloneDataSet();
        dataSetClone.normalizationDataSet();//数据集数据标准化
        DataSet samplingDataSet = new DataSet();
        samplingDataSet.copyToNewDataSet(dataSetClone.SystematicSampling(DataSetRatio));// 对进行数据集采样得到采样数据集（反向）   将反向采样数据集进行反向，得到正向采样数据集
        CsiDataStruct bestSampleCluster=DataProcessing.getBestCsiDataStruct(samplingDataSet,abnormalDataRatio);   //得到采样数据集最优分簇结果
        CsiDataStruct bestCluster = new CsiDataStruct();
        ArrayList<Integer> bestClusterAbnomalId =  bestCluster.getAbnormalDataId(dataSetClone,bestSampleCluster.getR(),abnormalDataRatio);  //得到异常数据的ID

        for (int i: bestClusterAbnomalId){//打印所有异常数据
            new  CellData(dataSet.findData(i)).printCellData();
        }
    }

    /**
     * 输入数据集，打印异常数据
     * @param dataSet 数据集
     * @param DataSetRatio 抽样比例（等间隔抽样）
     * @param abnormalDataRatio 异常数据比例
     */
    public static void getAbnormalDataInDataSetForward(@NotNull DataSet dataSet, float DataSetRatio, float abnormalDataRatio){
        DataSet dataSetClone = new DataSet();
       // DataSet dataSetClone = dataSet.cloneDataSet();
        dataSetClone=dataSet.cloneDataSet();
        dataSetClone.normalizationDataSet();//数据集数据标准化
        DataSet samplingDataSet = new DataSet();
        samplingDataSet.copyToNewDataSet(dataSetClone.SystematicSampling(DataSetRatio));// 对进行数据集采样得到采样数据集（反向）   将反向采样数据集进行反向，得到正向采样数据集
        CsiDataStruct bestSampleCluster=DataProcessing.getBestCsiDataStruct(samplingDataSet,abnormalDataRatio);   //得到采样数据集最优分簇结果
        CsiDataStruct bestCluster = new CsiDataStruct();
        ArrayList<Integer> bestClusterAbnomalId =  bestCluster.getAbnormalDataId(dataSetClone,bestSampleCluster.getR(),abnormalDataRatio);  //得到异常数据的ID

        int abnormalClusterSum=0;
        for (int i: bestClusterAbnomalId){//打印所有异常数据
            if (i==0){
                System.out.println("#####");
                abnormalClusterSum++;
            }
            else
            new  CellData(dataSet.findData(i)).printCellData();
        }
        System.out.println("异常簇个数为:"+abnormalClusterSum);
        System.out.println("异常数据量为:"+(bestClusterAbnomalId.size()-abnormalClusterSum));
    }



}
