<template>
  <div class="home">
    <div ref="myChart" :style="{width: '500px', height: '300px'}"></div>
    <el-button type="primary" @click="$router.push('/debug')">DeBug</el-button>
    <div style="width: 50%">
      <h-online-users :users="[]"></h-online-users>
    </div>
  </div>
</template>

<script>
import hOnlineUsers from "../components/OnlineUsers"
export default {
  mounted() {
    this.drawLine()
  },
  components:{
    hOnlineUsers
  },
  methods:{
    drawLine(){
      let myChart = this.$echarts.init(this.$refs.myChart)
      // 绘制图表
      myChart.setOption({
        title: { text: '最近一周的用户数量统计' },
        tooltip: {},
        xAxis: {
          data: ["周一","周二","周三","周四","周五","周六","周日"]
        },
        yAxis: {
          type: 'value',
          data:[1,2,3,4,5,6,7,8.9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24]
        },
        series: [{
          name: '在线',
          type: 'line',
          data: [4, 12, 6,3,6, 10, 20]
        }]
      });
    }
  }
}
</script>
