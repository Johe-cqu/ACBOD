package edu.buaa;

public class CsiDataStructStruct {
    private int size=0;
    private CsiDataStructNode csiDataStructHead;

    private class CsiDataStructNode{
        private CsiDataStruct csiDataStruct;
        private CsiDataStructNode next;
        public CsiDataStructNode(){
            this.csiDataStruct=null;
            this.next=null;
        }
        public CsiDataStructNode(CsiDataStruct csiDataStruct){
            this.csiDataStruct=csiDataStruct;
            this.next=null;
        }
        public CsiDataStructNode(CsiDataStruct csiDataStruct,CsiDataStructNode next){
            this.csiDataStruct=csiDataStruct;
            this.next=next;
        }
    }

    public CsiDataStructStruct(){
        this.size = 0;
        this.csiDataStructHead=null;
    }

    public CsiDataStructStruct(CsiDataStruct csiDataStruct){
        this.csiDataStructHead=new CsiDataStructNode(csiDataStruct);
        this.size=1;
    }

    public void addCsiDataStruct(CsiDataStruct csiDataStruct){
        CsiDataStructNode tempNode = this.csiDataStructHead ;
        this.csiDataStructHead = new CsiDataStructNode(csiDataStruct);
        this.csiDataStructHead.next=tempNode;
        this.size++;
    }

    /**
     * 设置CsiDataStructStruct内的所有CsiDataStruct内的DB指数
     * 未测试(未使用)
     */
    public void  setCsiDataStructDB(){
        CsiDataStructNode csiDataStructNode=this.csiDataStructHead;
        for (;csiDataStructNode!=null;csiDataStructNode=csiDataStructNode.next){
            csiDataStructNode.csiDataStruct.setDaviesBouldin();
        }
    }


    /**
     * 将DataSet的全部数据按照不同的deta进行聚类分析，并放入某个Struct中
     * @param dataSet 数据集
     * @param beta  beta值
     * 已测试
     */
    public void putDataSetToStructStruct(DataSet dataSet,float beta){
        DataSet tempDataSet = dataSet.cloneDataSet();
        try {
            float r = dataSet.getR(beta);
            this.addCsiDataStruct(new CsiDataStruct());//先创建一个CsiDataStruct
           /* for(;!tempDataSet.isNullHead();tempDataSet.nextNode()){  //每个点均放入Struct中
                this.csiDataStructHead.csiDataStruct.putPointToCluster(tempDataSet.readHeadNodeId(),tempDataSet.readHeadNodeData(),r);
            }*/
            for(int id: tempDataSet.getDataSetHashMap().keySet()){  //每个点均放入Struct中
                this.csiDataStructHead.csiDataStruct.putPointToCluster(id,tempDataSet.getDataSetHashMap().get(id),r);
            }
            this.csiDataStructHead.csiDataStruct.setDetaCAll(dataSet.cloneDataSet());//计算簇内所有CsiData的簇内距离DetaC
            this.csiDataStructHead.csiDataStruct.setDaviesBouldin();//设置簇内的DB指数
            this.csiDataStructHead.csiDataStruct.setBeta(beta);//设置beta
            this.csiDataStructHead.csiDataStruct.setR(r);//设置R
        }catch (Exception e){
            System.out.println("putDataSetToStructStruct error");
            e.printStackTrace();
        }
    }


    /**
     * 将CsiDataStructStruct中含有最小的DB指数的CsiDataStruct提取出来，计算此CsiDataStruct离群因子的OF，标记异常簇
     * @param ratio 异常数据的比例
     * @return  含有最小的DB指数的CsiDataStruct
     * 已测试
     */
    public CsiDataStruct getMinDbStruct(float ratio){
        CsiDataStructNode headNode = this.csiDataStructHead;
        float minDB=(float) (100000);
        CsiDataStruct temp =headNode.csiDataStruct;
        for (;headNode!=null;headNode=headNode.next){
            float DB=headNode.csiDataStruct.getDaviesBouldin();
            if(DB<minDB) {
                minDB = DB;
                temp=headNode.csiDataStruct;
            }
        }
        temp.calculateOF();
        temp.setCsiDataAbNormal(ratio);
        return temp;
    }



    public int getSize(){
        return this.size;
    }

}
