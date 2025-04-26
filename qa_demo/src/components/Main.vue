<template>
  <!-- 页面布局 -->
  <el-container>
    <el-header class="head1">
      <div class="tt1" style="margin: auto;">梨小食心虫虫害知识图谱问答系统</div>
    </el-header>

      <el-main>
        <el-row>
        <el-input prefix-icon="el-icon-search" clearable v-model="input" placeholder="请输入内容"></el-input>
        <el-button type="primary" style="width: 12%;" @click="find()">查询</el-button>
        <el-button type="primary" style="width: 12%;" @click="clear()">清空图谱</el-button>
        </el-row>



        <div class="content">

            <el-card class="answer" v-loading="loading">
              <div slot="header" class="clearfix">
                <span>查询结果</span>
              </div>
              <div v-for="o in output" :key="o" class="text item">
                {{o.split("::")[0]}}
              </div>
            </el-card>

            <el-card class="grah"  v-loading="loading">
              <div slot="header" class="clearfix">
                <span> 图谱显示</span>
              </div>
              <div class="kg" id="kg"></div>
            </el-card>

        </div>
      </el-main>
  </el-container>
</template>

<script>
  export default{
    data() {
      return{
        input:'',
        option:'',
        output:[],
        loading:false,
        auto:"auto",
        node:[],//头实体列表
        relation:[],//关系列表
        tail:[],//尾实体列表
        book:false,
      }
    },
    methods:{
      find(){
        if(this.input != ''){
          this.loading = true;
          this.$http.get('http://127.0.0.1:8000/question/query',{params:{
            question:this.input
            }}).then(res=>{
              // console.info(res)
              this.loading = false;

              if(res.data.data != null){
                var tmpnode = [];
                var tmprelation = [];
                tmpnode = res.data.entity;
                tmpnode.forEach(item=>{
                  if(!this.node.includes(item)){
                    this.node.push(item);
                  }
                })
                tmprelation = res.data.relation;
                tmprelation.forEach(item=>{
                  if(!this.relation.includes(item)){
                    this.relation.push(item);
                  }
                })
                // this.node.concat(tmpnode);
                // this.relation = res.data.relation;
                this.output = res.data.data;


                console.info(this.node);
                console.info(this.relation);
                // console.info(this.output);

                this.initData();
              }
              else{
                this.$message({
                  type: 'warning',
                  message: '未查询到相关答案!'
                });
              }
          })
        }
        else{
          this.$message({
          type: 'warning',
          message: '请输入问题!'
        });
        }

      },
      show(){
        var myChart = this.$echarts.init(document.getElementById("kg"),"walden");

        myChart.setOption(this.option);
      },
      initData(){
        const nodeList = [];
        const relationList = [];

        for(var i = 0; i < this.node.length; i++) {
          const entityArray = this.node[i].split("::")
          if(entityArray[0].length > 10){
            entityArray[0] = entityArray[0].slice(0,8)+"..."
            console.log("too long")
          }
          const tmp = {
            // name: this.node[i],

            name: entityArray[0],
            category:  Number(entityArray[1]),
            // value: this.node[i],

            draggable: true,
            itemStyle: {
              normal: {
                show: true,
                // color: ['#5470c6', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc']

              }
            }
          }
          nodeList.push(tmp)
        }

        for(let i = 0; i < this.relation.length; i++) {
          const relationshipArray = this.relation[i].split("->")
          if(relationshipArray[0].length > 10){
            relationshipArray[0] = relationshipArray[0].slice(0,8)+"..."
            console.log("too long")
          }
          if(relationshipArray[2].length > 10){
            relationshipArray[2] = relationshipArray[2].slice(0,8)+"..."
            console.log("too long")
          }
          // console.log(relationshipArray[0])
          const tmp = {
            source: relationshipArray[0].split("::")[0],
            name: relationshipArray[1],
            target: relationshipArray[2].split("::")[0]
          }
          relationList.push(tmp)
        }


        const myOption = {
          title: {
            text: '图谱可视化'
          },
          // backgroundColor:'#E6E8FA',
          tooltip: {},
          animationDurationUpdate: 1500,
          animationEasingUpdate: 'quinticInOut',
          series: [
            {
              type: 'graph',
              layout: 'force',
              force: {
                edgeLength: 200,
                repulsion: 500,
                // gravity: 0.1
              },
              symbolSize: (value, params) => {
                console.log(params.data.category)
                switch (params.data.category) {

                    case 4:return 100;break;
                    case 8:return 100;break;
                    case 9:return 80;break;
                    default:return 60;
                    };
              },

              roam: true,
              label: {
                show: true
              },
              categories: [{
                    name: '别名',
                    itemStyle: {
                            color: "lightgreen"
                    }
                }, {
                    name: '防治药剂',
                    itemStyle: {
                            color: "#23238E",
                    }
                }, {
                    name: '形态特征',
                    itemStyle: {
                            color: "#5470c6",
                    }
                }, {
                    name: '生活习性',
                    itemStyle: {
                            color: "lightblue",
                    }
                },{
                    name: '昆虫',
                    itemStyle: {
                            color: "#c9dd22",
                    },
                }, {
                    name: '拉丁名',
                    itemStyle: {
                            color: "pink",
                    }
                }, {
                    name: '地点',
                    itemStyle: {
                            color: "brown",
                    }
                }, {
                    name: '部位',
                    itemStyle: {
                            color: "#73c0de",
                    }
                }, {
                    name: '虫害',
                    itemStyle: {
                            color: "red",
                    }
                }, {
                    name: '植物',
                    itemStyle: {
                            color: "green",
                    }
                }, {
                    name: '防治方法',
                    itemStyle: {
                            color: "#9a60b4",
                    }
                }, {
                    name: '繁殖方式',
                    itemStyle: {
                            color: "#ea7ccc",
                    }
                }, {
                    name: '为害规律',
                    itemStyle: {
                            color: "#B45B3E",
                    }
                }, {
                    name: '简介',
                    itemStyle: {
                            color: "orange",
                    }
                }, {
                    name: '症状',
                    itemStyle: {
                            color: "black",
                    }
                }, {
                    name: '分类',
                    itemStyle: {
                            color: "#6699CC",
                    }
                }],
              edgeColor: 'black',
              edgeSymbol: ['circle', 'arrow'],
              edgeSymbolSize: [5, 10],
              edgeLabel: {
                  normal: {
                      color: 'black',
                      show: true,
                      formatter: function (x) {
                          return x.data.name;
                      },
                      textStyle: {
                        fontSize: 15
                      }
                  }
              },
              data: nodeList,
              links: relationList,
              lineStyle: {
                opacity: 1,
                width: 2,
                // color: "black"
              }
            }
          ]}
        // console.log(this.node);
        // console.log(this.relation);
        // console.log(nodeList);
        // console.log(relationList);
        this.option = myOption;
        this.show();
      },
      clear(){
        this.node.splice(0,this.node.length);
        this.relation.splice(0,this.relation.length);
        this.initData();
      }
    },
    mounted(){
      // this.find();
    }
  }
</script>

<style>
  body{
    margin: 0;
  }
  .content{
    width: 95% ;
    align-content: center;
  }
  .tt1{;
  }
  .el-header.head1{
    background-color: #edfeff;
    height: 120px;
    display: flex;
    justify-content: space-between;
    padding-left: 10px;
    align-items: center;
    color: black;
    font-size: 40px;
    text-align:center
  }
  .el-main{
    background-color: #edfeff;
  }
  .el-container{
    height: 100%;
  }
  .el-input{
    width: 65%;
  }
  .el-button{
    /* margin-left: 20px;
    margin-top: 20px;
    margin-bottom: 20px; */
    margin: 20px;
  }

  .text {
      font-size: 18px;
    }

    .item {
      margin-bottom: 15px;
    }

    .clearfix:before,
    .clearfix:after {
      display: table;
      content: "";
    }
    .clearfix:after {
      clear: both
    }
    .el-row{

    }
    .kg{

      width: 100%;
      height: 420px;
    }
    .answer {
      width: 40%;
      /* width: 80%; */
      height: 460px;
      /* height: 100%; */
      float: left;
      overflow-y:auto
    }
    .grah{
      margin-right: 20px;
      width: 55%;
      height: 460px;
      float: right;

    }

</style>
