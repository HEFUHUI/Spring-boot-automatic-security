<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>控制行走</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="restore">复位</el-button>
          </div>
          <el-slider :min="0" v-model="data_data.x"></el-slider>
          <el-slider :min="0" v-model="data_data.y"></el-slider>
          <el-slider :min="0" v-model="data_data.z"></el-slider>
          <el-button type="primary" @click="move">行走</el-button>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card class="box-card" style="text-align: center">
          <div slot="header" class="clearfix">
            <span>手动控制</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="move">复位</el-button>
          </div>
          <el-form label-width="70px">
            <el-form-item label="速度">
              <el-slider :min="0" :max="10" v-model="speed"></el-slider>
            </el-form-item>
            <el-form-item label="方向">
              <el-switch v-model="direction" inactive-text="正向" active-color="#409eff" active-text="逆向"></el-switch>
            </el-form-item>
          </el-form>
          <el-button @click="moveUp" type="primary" size="large">前进</el-button>
          <p>
            <el-button @click="moveLeft" type="primary" size="large">向左</el-button>
            <el-button @click="rotate" :icon="direction ? 'el-icon-refresh-left' : 'el-icon-refresh-right'"></el-button>
            <el-button @click="moveRight" type="primary" size="large">向右</el-button>
          </p>
          <el-button @click="moveDown" type="primary" size="large">后退</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  name: "MonitorEqu",
  data() {
    return {
      equ: {
        data: {},
      },
      direction:false,
      speed: 0,
      data_data: {
        x: 0, y: 0, z: 0
      }
    }
  },
  created() {
    document.onkeydown = (e) => {
      switch (e.which) {
        case 38:
          this.moveUp()
          break;
        case 39:
            this.moveLeft()
          break;
        case 40:
          this.moveDown()
          break;
        case 37:
          this.moveRight()
          break;
      }
    }
    this.fetch();
  },
  mounted() {
    this.$ws.onmessage = (msg)=>{
      try {
        const data = JSON.parse(msg.data)
        if(data.code === 406){
          this.$message({type:"error",message:data.msg})
        }
      }catch (e) {
        console.log(e)
      }
    }
  },
  methods: {
    moveUp() {
      this.data_data = {y: this.speed, x: 0, z: 0}
      this.handleMove();
    },
    rotate(){
      this.data_data = {y:0,x:0,z:this.speed}
      this.handleMove();
    },
    moveLeft() {
      this.data_data = {y: 0, x: this.speed, z: 0}
      this.handleMove();
    },
    moveRight() {
      this.data_data = {y: 0, x: -this.speed, z: 0}
      this.handleMove();
    },
    moveDown(){
      this.data_data = {y: -this.speed, x: 0, z: 0}
      this.handleMove();
    },
    async fetch() {
      this.equ = (await this.$axios.get(`equ/find/${this.$route.params.id}`)).data
    },
    async move() {
      this.$ws.send(JSON.stringify({
        code: 120,
        data: {
          data: {
            data: this.data_data,
            action: "move"
          },
          destination: this.equ.data.userId
        },
      }))
    },
    async restore() {
      this.data_data = {x: 0, y: 0, z: 0}
      await this.move();
    },
    async handleMove() {
      this.$ws.send(JSON.stringify({
        code: 120,
        data: {
          data: {
            data: this.data_data,
            action: "handle"
          },
          destination: this.equ.data.userId
        },
      }))
    },
    drawLine() {
      let myChart = this.$echarts.init(this.$refs.myChart)
      // 绘制图表
      myChart.setOption({
        title: {text: '该设备在近一周的在线时间统计(小时)'},
        tooltip: {},
        xAxis: {
          data: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"]
        },
        yAxis: {
          type: 'value',
          data: [1, 2, 3, 4, 5, 6, 7, 8.9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24]
        },
        series: [{
          name: '在线',
          type: 'line',
          data: [4, 12, 6, 3, 6, 10, 20]
        }]
      });
    }
  },
}
</script>

<style scoped>

</style>