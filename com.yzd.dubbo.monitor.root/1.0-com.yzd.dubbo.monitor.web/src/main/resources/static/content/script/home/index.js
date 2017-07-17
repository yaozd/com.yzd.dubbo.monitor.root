/**
 * Created by zd.yao on 2017/7/17.
 */
$(function () {
    initeCharts();
});
function initeCharts() {
    var urlPath="/content/echarts/dist";
    require.config({
        paths: {
            echarts: urlPath
        }
    });
    require(
        [
            'echarts',
            'echarts/chart/line',   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
            'echarts/chart/bar',
            'echarts/chart/chord',
            'echarts/chart/force'
        ],
        function (ec) {
            allAPPRelationForceChart(ec);

        }
    )
}
//所有app之间的依赖关系
function allAPPRelationForceChart(ec) {
    $.ajax({
        url: "/application/getAllAPPAndRelation",
        success: function (resultVO) {
            if (!resultVO.success) {
                $("#all_app_relation_force_echarts").html("加载失败~！原因：" + resultVO.msg);

            } else {
                if (ec == undefined) {
                    ec = echartsEc;
                }
                var charts_id = 'all_app_relation_force_echarts';
                var myChart = ec.init(document.getElementById(charts_id));
                myChart.showLoading({
                    text: 'Loading...',
                    effect: 'bubble',
                    textStyle: {
                        fontSize: 20
                    }
                });
                allAppResutMap = resultVO.data;
                var appMap = allAppResutMap.allApp;
                var nodesList = [];
                var linkList = [];
                $.each(appMap, function (key, value) {
                    var consumersSet = value.consumersSet;
                    var nodes_value = 1;
                    if (consumersSet != undefined) {
                        nodes_value = consumersSet.length * 1.2;
                    }
                    var categoryVal=0;
                    if(value.isProvider==false&&value.isConsumer)
                    {
                        categoryVal=1;
                    }
                    if(value.isProvider&&value.isConsumer)
                    {
                        categoryVal=2;
                    }
                    var nodesMap = {
                        category: categoryVal, name: key, value: nodes_value, draggable: true
                    };
                    nodesList.push(nodesMap);

                    if (consumersSet != undefined) {
                        $.each(consumersSet, function (i, target_value) {
                            var linkMap = {
                                source: key,
                                target: target_value,
                                weight: 1,
                                name: key + "提供服务" + target_value,
                                itemStyle: {normal: {color: 'red'}}
                            };
                            linkList.push(linkMap);
                        });
                    }
                });
                var option = {
                    title : {
                        text: 'DUBBO应用依赖关系图',
                        x:'right',
                        y:'bottom'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: ' {b}'
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            restore: {show: true},
                            magicType: {show: true, type: ['force', 'chord']},
                            saveAsImage: {show: true}
                        }
                    },
                    legend: {
                        x: 'left',
                        data:['提供者节点','消费者节点','提供者与消费者节点']
                    },
                    series: [
                        {
                            type: 'force',
                            name: "依赖关系",
                            ribbonType: false,
                            categories: [
                                {
                                    name: '提供者节点'
                                },
                                {
                                    name: '消费者节点'
                                },
                                {
                                    name:'提供者与消费者节点'
                                }
                            ],
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true,
                                        textStyle: {
                                            color: '#333'
                                        }
                                    },
                                    nodeStyle: {
                                        brushType: 'both',
                                        borderColor: 'yellow',
                                        borderWidth: 1
                                    }
                                },
                                emphasis: {
                                    label: {
                                        show: false
                                    }
                                }
                            },
                            useWorker: false,
                            minRadius : 15,
                            maxRadius : 30,
                            gravity: 1.1,
                            scaling: 2.0,
                            roam: 'move',
                            gravity: 1.1,
                            draggable: true,
                            large: true,
                            linkSymbol: 'arrow',
                            steps: 10,
                            coolDown: 0.9,
                            //preventOverlap: true,
                            nodes: nodesList,
                            links: linkList
                        }
                    ]
                };
                myChart.setOption(option);
                myChart.hideLoading();
            }
        }
    });
}