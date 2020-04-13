var myApp = angular.module('shop', ['ngCookies'])

myApp.controller('shopController', ['$scope', '$http', '$templateCache','$interval','$cookies','$cookieStore', function ($scope, $http, $templateCache,$interval,$cookies,$cookieStore) {
    var vm = $scope
    var request=$http
    var user = $cookieStore.get("user");
    console.log(user)
    if(null==user){
        swal("错误","请先登录","error")
        window.location.href="/login"
    }
    vm.getALltime=function () {
        dt=new Date()
        hour=dt.getHours()
        minute=dt.getMinutes()
        sercond=dt.getSeconds()
        if(minute<10) {
            return hour + ":0" + minute+":"+sercond
        }
        return hour + ":" + minute+":"+sercond
    }
    //右侧两部排行榜
    vm.orderList=new Array()
    vm.narn=function(type,order) {

        var str="下单时间:"+order.time+"<br>"+
        "客户名字:"+order.name+"<br>"+
        "客户年龄:"+order.age+"<br>"+
          "客户地址:"+order.city+"<br>"+
            "商品名称:"+order.goods.title+"<br>"+
            "订单金额:"+order.goods.price

        swal({
            title: "订单详情",
            text: str,
            imageUrl: order.goods.picUrl,
            html: true,
            timer: 5000,
            showConfirmButton: false
        });
    }

    vm.loadLunbo=function(){
        request({
            method: 'GET',
            url: 'http://localhost:8080/target/getRandomOrder',

        }).then(function (response) {
            // console.log(response.data)

            var obj=response.data.data
            if(vm.orderList.length>20){
                vm.orderList.shift();
            }
            vm.orderList.push(obj)





        }, function (response) {

        });

    }
    vm.loadLunbo()
    $interval(vm.loadLunbo,5000)

    vm.load3=function(){

        request({
            method: 'GET',
            url: 'http://localhost:8080/target/getTypeMoneyTop5',



        }).then(function (response) {
            // console.log(response.data)

            vm.topList=response.data.data





        }, function (response) {

        });
        request({
            method: 'GET',
            url: 'http://localhost:8080/target/getTypeMoneyLast5',



        }).then(function (response) {
            // console.log(response.data)

            vm.lastList=response.data.data





        }, function (response) {

        });

    }
    vm.load3()
 $interval(vm.load3,10000)


    vm.getHour=function () {
        dt=new Date()
        hour=dt.getHours()
        if(hour<10) {
            return "0" + hour
        }
        return hour
    }
    $(window).load(function(){
        $(".loading").fadeOut()
    })
    vm.loadHourJob=function () {

        request({
            method: 'GET',
            url: 'http://localhost:8080/target/getAge'



        }).then(function (response) {
            // console.log(response.data)

            var rs=response.data.data
            vm.hourJobKey=Object.keys(rs)
            vm.hourObj=new Array()
            vm.hourJobKey.forEach(x=>{

                vm.hourObj.push({
                    name:x,
                    value:rs[x]
                })
            })

         echarts_1()




        }, function (response) {

        });
    }
    vm.loadHourJob()
    $interval(vm.loadHourJob,7000)
    function echarts_1() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart1'));
        option = {
            tooltip : {
                trigger: 'item',
                formatter: "{b} : {c} ({d}%)"
            },
            legend: {
                right:0,
                top:'0%',
                height:160,
                itemWidth:10,
                itemHeight:10,
                itemGap:10,
                textStyle:{
                    color: 'rgba(255,255,255,.6)',
                    fontSize:12
                },
                orient:'vertical',
                data:vm.hourJobKey
            },
            calculable : true,
            series : [
                {
                    name:' ',
                    color: ['#62c98d', '#2f89cf', '#C9C42B', '#C962B9', '#00B7BE', '#205acf', '#c9c42b', '#c98b62', '#c962b9', '#7562c9','#c96262','#c24844','#00b7be'],
                    type:'pie',
                    radius : [30, 50],
                    center : ['45%', '50%'],
                    roseType : 'radius',
                    label: {
                        normal: {
                            show: true
                        },
                        emphasis: {
                            show: true
                        }
                    },

                    lableLine: {
                        normal: {
                            show: true
                        },
                        emphasis: {
                            show: true
                        }
                    },

                    data:vm.hourObj
                },
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        window.addEventListener("resize",function(){
            myChart.resize();
        });
    }
    vm.loadHourCity=function () {

        request({
            method: 'GET',
            url: 'http://localhost:8080/target/getCityIncome',



        }).then(function (response) {
            // console.log(response.data)

            var rs=response.data.data
            vm.hourCityKey=Object.keys(rs)
            vm.hourCityObj=new Array()
            vm.hourCityKey.forEach(x=>{

                vm.hourCityObj.push({
                    name:x,
                    value:rs[x]
                })
            })


            echarts_2()




        }, function (response) {

        });
    }
    vm.loadHourCity()
 //   $interval(vm.loadHourCity,7000)


   //此处加载较慢，共访问二十五次redis
    vm.loadHourCitySex=function(){

        request({
            method: 'GET',
            url: 'http://localhost:8080/target/getCityNum',



        }).then(function (response) {
            // console.log(response.data)

            var rs=response.data.data
            vm.houtCityKey=rs[0]
            vm.hourCityValue=rs[1]

             echarts_3()


        }, function (response) {

        });
    }
    vm.loadHourCitySex()

 $interval(vm.loadHourCitySex,10000)
    vm.loadLastCity=function(){

        request({
            method: 'GET',
            url: 'http://localhost:8080/target/getLastShop10',



        }).then(function (response) {
            // console.log(response.data)

            var rs=response.data.data
            vm.lastShopName=rs.keys
            vm.lastShopSaleNum=rs.key1
            vm.lastShopPrice=rs.key2

            echarts_4()


        }, function (response) {

        });
    }
    vm.loadLastCity()
 $interval(vm.loadLastCity,10000)
    function echarts_4() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart4'));
        option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    lineStyle: {
                        color: '#57617B'
                    }
                }
            },
            "legend": {

                "data": [
                    {"name": "订单数"},

                    {"name": "商品售价"}
                ],
                "top": "0%",
                "textStyle": {
                    "color": "rgba(255,255,255,0.9)"//图例文字
                }
            },

            "xAxis": [
                {
                    "type": "category",
                    axisLabel : {

                        formatter: function(){

                            return "";

                        }

                    },
                    data: vm.lastShopName,
                    // axisLine: { lineStyle: {color: "rgba(255,255,255,.1)"}},
                    // axisLabel:  { textStyle: {color: "rgba(255,255,255,.6)", fontSize: '14', },
                    // },

                },
            ],
            "yAxis": [
                {
                    // "type": "value",
                    "name": "订单数",

                    // "interval": 10,
                    // "axisLabel": {
                    //     "show": true,
                    //
                    // },
                    axisLine: {
                        lineStyle: {color: 'rgba(255,255,255,.4)'},
                        fontSize:1

                    },//左线色
                    axisLabel : {

                        formatter: function(){

                            return "";

                        }

                    }

                },
                {
                    "type": "value",
                    "name": "产品售价",
                    "show": true,
                    "axisLabel": {
                        "show": true,

                    },
                    axisLine: {lineStyle: {color: 'rgba(255,255,255,.4)'}},//右线色
                    splitLine: {show:true,lineStyle: {color:"#001e94"}},//x轴线
                },
            ],
            "grid": {
                "top": "10%",
                "right":"30",
                "bottom":"30",
                "left":"30",
            },
            "series": [
                {
                    "name": "订单数",

                    "type": "bar",
                    "data": vm.lastShopSaleNum,
                    "barWidth": "auto",
                    "itemStyle": {
                        "normal": {
                            "color": {
                                "type": "linear",
                                "x": 0,
                                "y": 0,
                                "x2": 0,
                                "y2": 1,
                                "colorStops": [
                                    {
                                        "offset": 0,
                                        "color": "#609db8"
                                    },

                                    {
                                        "offset": 1,
                                        "color": "#609db8"
                                    }
                                ],
                                "globalCoord": false
                            }
                        }
                    }
                },

                {
                    "name": "产品售价（元）",
                    "type": "line",
                    "yAxisIndex": 1,

                    "data": vm.lastShopPrice,
                    lineStyle: {
                        normal: {
                            width: 2
                        },
                    },
                    "itemStyle": {
                        "normal": {
                            "color": "#cdba00",

                        }
                    },
                    "smooth": true
                }
            ]
        };


        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        window.addEventListener("resize",function(){
            myChart.resize();
        });
    }

       vm.load1=function() {
           request({
               method: 'GET',
               url: 'http://localhost:8080/target/getTypeTop3',

           }).then(function (response) {
               // console.log(response.data)

               vm.zhong = response.data.data.zhong
               vm.allnum = response.data.data.allnum
               vm.haoren = response.data.data.hao
               vm.cha = response.data.data.cha
               vm.type1=response.data.data.type1
               vm.type2=response.data.data.type2
               vm.type3=response.data.data.type3



               zb1();
               zb2();
               zb3();


           }, function (response) {

           });
       }
       vm.load2=function(){
           request({
               method: 'GET',
               url: 'http://localhost:8080/target/getTopShop10',
           }).then(function (response) {
                console.log(response.data)

              vm.zhanbiObject=response.data.data

               vm.key1=vm.zhanbiObject.key1
               vm.key2=vm.zhanbiObject.key2
               vm.keys=vm.zhanbiObject.keys
               console.log(vm.key1,vm.key2,vm.keys)


               echarts_5()


           }, function (response) {

           });
       }
    function echarts_5() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart5'));
// 颜色
        var lightBlue = {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [{
                offset: 0,
                color: 'rgba(41, 121, 255, 1)'
            }, {
                offset: 1,
                color: 'rgba(0, 192, 255, 1)'
            }],
            globalCoord: false
        }

        var option = {
            tooltip: {
                show: true
            },
            grid: {
                top: '0%',
                left: '120',
                right: '14%',
                bottom: '0%',
            },
            xAxis: {
                interval: 0,
                min: 0,
                max: 100,
                rotate:45,
               textStyle:{
                    fontSize:5
               }
                // splitLine: {
                //     show: false
                // },
                // axisTick: {
                //     show: false
                // },
                // axisLine: {
                //     show: false
                // },
                // axisLabel: {
                //     show: false
                // }
            },
            yAxis: {
                data: vm.keys,
                //offset: 15,
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                axisLabel: {
                    color: 'rgba(255,255,255,.6)',
                    fontSize: 14
                }
            },
            // grid:[
            //     {
            //         bottom: "2%"
            //     }
            // ],
            // grid: {
            //     left: '100%',
            //     right: '100%',
            //     // containLabel: true
            // },
            series: [{
                type: 'bar',
                label: {
                    show: true,
                    zlevel: 10000,
                    position: 'right',
                    padding: 10,
                    color: '#49bcf7',
                    fontSize: 14,
                    formatter: '{c}%'
                },
                itemStyle: {
                    color:'#49bcf7'
                },
                barWidth: '15',
                //展示的是百分比
                data: vm.key2,
                z: 10
            }, {
                type: 'bar',
                barGap: '-100%',
                itemStyle: {
                    color:'#fff',
                    opacity: 0.1
                },
                barWidth: '15',
                //展示的是销量
                data: vm.key1,
                z: 5
            }],
        };
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        window.addEventListener("resize",function(){
            myChart.resize();
        });
    }
       vm.load1()
        vm.load2()
     $interval(vm.load1,5000)





        function echarts_2() {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('echart2'));

            option = {
                tooltip: {
                    trigger: 'item',
                    formatter: "{b} : {c} ({d}%)"
                },
                legend: {

                    top:'0%',
                    data: vm.hourCityKey,
                    icon: 'circle',
                    textStyle: {
                        color: 'rgba(255,255,255,.6)',
                    }
                },
                calculable: true,
                series: [{
                    name: '',
                    color: ['#62c98d', '#2f89cf', '#4cb9cf', '#53b666', '#62c98d', '#205acf', '#c9c862', '#c98b62', '#c962b9','#c96262'],
                    type: 'pie',
                    //起始角度，支持范围[0, 360]
                    startAngle: 180,
                    //饼图的半径，数组的第一项是内半径，第二项是外半径
                    radius: [51, 80],
                    //支持设置成百分比，设置成百分比时第一项是相对于容器宽度，第二项是相对于容器高度
                    center: ['50%', '55%'],

                    //是否展示成南丁格尔图，通过半径区分数据大小。可选择两种模式：
                    // 'radius' 面积展现数据的百分比，半径展现数据的大小。
                    //  'area' 所有扇区面积相同，仅通过半径展现数据大小
                    roseType: 'radius',
                    //是否启用防止标签重叠策略，默认开启，圆环图这个例子中需要强制所有标签放在中心位置，可以将该值设为 false。
                    avoidLabelOverlap: false,
                    label: {
                        normal: {
                            show: true,
                            //  formatter: '{c}辆'
                        },
                        emphasis: {
                            show: true
                        }
                    },
                    labelLine: {
                        normal: {
                            show: true,
                            length2: 1,
                        },
                        emphasis: {
                            show: true
                        }
                    },
                    data: vm.hourCityObj
                }]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
            window.addEventListener("resize",function(){
                myChart.resize();
            });
        }
        function echarts_3() {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('echart3'));

            option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        lineStyle: {
                            color: '#57617B'
                        }
                    }
                },
                legend: {

                    //icon: 'vertical',
                    data: ['下单数'],
                    //align: 'center',
                    // right: '35%',
                    top:'0',
                    textStyle: {
                        color: "#fff"
                    },
                    // itemWidth: 15,
                    // itemHeight: 15,
                    itemGap: 20,
                },
                grid: {
                    left: '0',
                    right: '20',
                    top:'10',
                    bottom: '20',
                    containLabel: true
                },
                xAxis: [{
                    type: 'category',
                    boundaryGap: false,
                    axisLabel: {
                        show: true,
                        textStyle: {
                            color: 'rgba(255,255,255,.6)'
                        }
                    },
                    axisLine: {
                        lineStyle: {
                            color: 'rgba(255,255,255,.1)'
                        }
                    },
                    data: vm.houtCityKey
                }, {




                }],
                yAxis: [{
                    axisLabel: {
                        show: true,
                        textStyle: {
                            color: 'rgba(255,255,255,.6)'
                        }
                    },
                    axisLine: {
                        lineStyle: {
                            color: 'rgba(255,255,255,.1)'
                        }
                    },
                    splitLine: {
                        lineStyle: {
                            color: 'rgba(255,255,255,.1)'
                        }
                    }
                }],
                series: [{
                    name: '下单数',
                    type: 'line',
                    smooth: true,
                    symbol: 'circle',
                    symbolSize: 5,
                    showSymbol: false,
                    lineStyle: {
                        normal: {
                            width: 2
                        }
                    },
                    areaStyle: {
                        normal: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                offset: 0,
                                color: 'rgba(24, 163, 64, 0.3)'
                            }, {
                                offset: 0.8,
                                color: 'rgba(24, 163, 64, 0)'
                            }], false),
                            shadowColor: 'rgba(0, 0, 0, 0.1)',
                            shadowBlur: 10
                        }
                    },
                    itemStyle: {
                        normal: {
                            color: '#cdba00',
                            borderColor: 'rgba(137,189,2,0.27)',
                            borderWidth: 12
                        }
                    },
                    data:   vm.hourCityValue
                }
                     ]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
            window.addEventListener("resize",function(){
                myChart.resize();
            });
        }


        function zb1() {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('zb1'));

            option = {
                // tooltip: {
                //     show: true
                //
                //
                //
                // },
                series: [{

                    type: 'pie',
                    radius: ['60%', '70%'],
                    color:'#49bcf7',
                    label: {
                        normal: {
                            position: 'center'
                        }
                    },
                    data: [{
                        value: vm.haoren,
                        name: '第一名',
                        label: {
                            normal: {
                                formatter: vm.haoren +'',
                                textStyle: {
                                    fontSize: 20,
                                    color:'#fff',
                                }
                            }
                        }
                    },{
                        value:vm.type1,
                        name:vm.type1
                    }, {
                        value: vm.allnum,
                        name: '全部',
                        label: {
                            normal: {
                                formatter : function (params){
                                    return '占比'+Math.round( (vm.haoren/vm.allnum)*100)+ '%'
                                },
                                textStyle: {
                                    color: '#aaa',
                                    fontSize: 12
                                }
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: 'rgba(255,255,255,.2)'
                            },
                            emphasis: {
                                color: '#fff'
                            }
                        },
                    }]
                }]
            };
            myChart.setOption(option);
            window.addEventListener("resize",function(){
                myChart.resize();
            });
        }
        function zb2() {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('zb2'));

            option = {

//animation: false,
                series: [{
                    type: 'pie',
                    radius: ['60%', '70%'],
                    color:'#cdba00',
                    label: {
                        normal: {
                            position: 'center'
                        }
                    },
                    data: [{
                        value: vm.zhong,
                        name: '第二名',
                        label: {
                            normal: {
                                formatter: vm.zhong +'',
                                textStyle: {
                                    fontSize: 20,
                                    color:'#fff',
                                }
                            }
                        }
                    },{
                        value:vm.type2,
                        name:vm.type2
                    }, {
                        value: vm.allnum,
                        name: '总销售',
                        label: {
                            normal: {
                                formatter : function (params){
                                    return '占比'+Math.round( vm.zhong/vm.allnum*100)+ '%'
                                },
                                textStyle: {
                                    color: '#aaa',
                                    fontSize: 12
                                }
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: 'rgba(255,255,255,.2)'
                            },
                            emphasis: {
                                color: '#fff'
                            }
                        },
                    }]
                }]
            };
            myChart.setOption(option);
            window.addEventListener("resize",function(){
                myChart.resize();
            });
        }
        function zb3() {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('zb3'));

            option = {
                series: [{

                    type: 'pie',
                    radius: ['60%', '70%'],
                    color:'#62c98d',
                    label: {
                        normal: {
                            position: 'center'
                        }
                    },
                    data: [{
                        value: vm.cha,
                        name: '第三名',
                        label: {
                            normal: {
                                formatter: vm.cha +'',
                                textStyle: {
                                    fontSize: 20,
                                    color:'#fff',
                                }
                            }
                        }
                    },{
                        value:vm.type3,
                        name:vm.type3
                    }, {
                        value: vm.allnum,
                        name: '总消费',
                        label: {
                            normal: {
                                formatter : function (params){
                                    return '占比'+Math.round( vm.cha/vm.allnum*100)+ '%'
                                },
                                textStyle: {
                                    color: '#aaa',
                                    fontSize: 12
                                }
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: 'rgba(255,255,255,.2)'
                            },
                            emphasis: {
                                color: '#fff'
                            }
                        },
                    }]
                }]
            };
            myChart.setOption(option);
            window.addEventListener("resize",function(){
                myChart.resize();
            });
        }





















}]);
