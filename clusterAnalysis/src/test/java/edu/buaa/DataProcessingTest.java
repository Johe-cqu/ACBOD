package edu.buaa;
import org.junit.Test;

public class DataProcessingTest {
    @Test
    public void dataProcessingTest(){
        DataSet dataSet = new DataSet();
        for(int i=0 ;i<100;i++) {
            float[] aData ={1*i,2*i,3*i,4*i};
            float[] bData ={5*i,6*i,7*i,8*i};
            float[] cData ={4*i,7*i,8*i,9*i};
            dataSet.addNodeInHead(i*3, new CellData(aData.clone()));
            dataSet.addNodeInHead(i*3+1, new CellData(bData.clone()));
            dataSet.addNodeInHead(i*3+2, new CellData(cData.clone()));
        }
        dataSet.normalizationDataSet();
        dataSet.getDataSetExDx();
        CsiDataStruct temp=DataProcessing.getBestCsiDataStruct(dataSet,(float) 0.05);
        System.out.println("");

    }
}
